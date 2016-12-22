package com.sleep.bluetooth;

import java.util.concurrent.LinkedBlockingQueue;

import android.util.Log;

public class DataParser {
	/*
	 * Package length: 20 bytes
	 * Package transfer rate: N/A （maximum transfer time span is 1 second）
	 * 
	 * Package Format:
	 * Byte0：0xff				Head Mark1
	 * Byte1：0xaa 				Head Mark2
	 * Byte2：Package Index 		Package index series
	 * Byte3：Status Byte		SpO2 status info
	 * Byte4：PulseRate 	       	Average PulseRate
	 * Byte5：SpO2Sat  	       	Average SpO2Sat
	 * Byte6：PlethSignal	    Pulse infusion intensity
	 * Byte7：Battery	 		Battery power in percent
	 * Byte8：rrInterval 		Low Byte of rrInterval
	 * Byte9：rrInterval 		High Byte of rrInterval
	 * Byte10：Reserved Byte 	Reserved Byte for future usage
	 * Byte11：Reserved Byte 	Reserved Byte for future usage
	 * Byte12：Reserved Byte 	Reserved Byte for future usage
	 * Byte13：Reserved Byte 	Reserved Byte for future usage
	 * Byte14：Reserved Byte 	Reserved Byte for future usage
	 * Byte15：Reserved Byte 	Reserved Byte for future usage
	 * Byte16：Reserved Byte 	Reserved Byte for future usage
	 * Byte17：Reserved Byte 	Reserved Byte for future usage
	 * Byte18：Reserved Byte 	Reserved Byte for future usage
	 * Byte19：CheckSum			Checksum of the whole package
	 * 
	 * Package Index:			Range (0~255), the index of the package
	 * Status Byte				0x01	SpO2 Sensor Off
	 * 							0x02	No finger
	 * 							0x04	No pulse signal
	 * 							0x08	Pulse Beat Tips （rrInterval update Tip）
	 * PulseRate				Range (25~250, invalid value=255) unit：bpm
	 * SpO2Sat 	       			Range (35~100, invalid value=127) unit：%
	 * PlethSignal				Range (1~200, invalid value=0) unit：‰
	 * Battery:					Range (0~100) unit：%
	 * rrInterval:				Range (20~300, invalid value=0), 
	 * 							Formula: PulseRate = 60 * 100 / rrInterval
	 * Reserved Byte Reserved Byte for future usage, default value is 0x00
	 * CheckSum					Range (0~255)
	 * 							Formula: CheckSum = (Byte0+Byte1+…+Byte18) % 256
	 */

	// TAG
	private final String TAG = this.getClass().getSimpleName();
	private static final int[] PKG_HEAD = new int[] { 0xff, 0xaa }; // 包头校验值
	private byte[] pkgData; // 解析数据临时存储数组
	private int pkgLength = 20; // 数据包长度
	private final int parseLength = 24; // 数据解析数组的长度
	private int pkgIndexOld, pkgIndexNew; // 数据包新、旧index值，检验数据包是否连续
	private boolean IsCheckSumError; // 检验校验是否通过
	private boolean IsPackageLost; // 检验数据包是否丢失
	// Buffer queue
	private LinkedBlockingQueue<Byte> bufferQueue = new LinkedBlockingQueue<Byte>();
	// Parse Runnable
	private Thread mParseThread;
	private boolean running = false;

	private NewDataNotify mNewDataNotify;

	public interface NewDataNotify {
		void notifyNewData(EquipmentData equipmentData);
	}

	public DataParser(NewDataNotify newDataNotify) {
		this.mNewDataNotify = newDataNotify;
		pkgData = new byte[parseLength];
		pkgIndexOld = -1;
		IsCheckSumError = false;
		IsPackageLost = false;
	}

	/**
	 * 打开数据解析线程。
	 */
	public void start() {
		mParseThread = new Thread(new ParseRunnable());
		mParseThread.start();
		running = true;
	}

	/**
	 * 关闭数据解析线程。
	 */
	public void stop() {
		running = false;
		mParseThread = null;
	}

	/**
	 * 读取队列中数据的线程。
	 */
	private class ParseRunnable implements Runnable {

		@Override
		public void run() {
			while (running) {
				for (int i = 0; i < parseLength - 1; i++) {
					pkgData[i] = pkgData[i + 1];
				}
				pkgData[parseLength - 1] = getData();
				if ((0xFF & pkgData[0]) == PKG_HEAD[0] && (0xFF & pkgData[1]) == PKG_HEAD[1]) {
					if ((0xFF & pkgData[11]) == PKG_HEAD[0] && (0xFF & pkgData[12]) == PKG_HEAD[1]) {
						pkgLength = 11;
					} else if ((0xFF & pkgData[20]) == PKG_HEAD[0] && (0xFF & pkgData[21]) == PKG_HEAD[1]) {
						pkgLength = 20;
					} else {
						Log.e(TAG, "data parse error, unknown package length");
					}
					// 测试输出
					// DumpPackage_All(pkgData);
					parsePackage(pkgData);
				}
			}
		}
	}

	/**
	 * 将数据添加到队列中。
	 * 
	 * @param dat
	 *            接受到的数据。
	 * @param len
	 *            有效数据的长度。低功耗设备发送的数据，len的值为0。
	 */
	public void addData(byte[] dat, int len) {
		if (len == 0) {
			for (byte b : dat) {
				try {
					bufferQueue.put(b);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else if (len > 0) {
			for (int i = 0; i < len; i++) {
				try {
					bufferQueue.put(dat[i]);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			Log.e(TAG, "error of the data to be added to bufferQueue, data length = " + len);
		}

	}

	/**
	 * 从队列中获取数据。
	 * 
	 * @return
	 */
	private byte getData() {
		byte dat = 0;
		try {
			dat = bufferQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return dat;
	}

	/**
	 * 数据解析。
	 * 
	 * @param pkgData
	 */
	private void parsePackage(byte[] pkgData) {

		CheckIfPackageLost(pkgData);
		CheckIfCheckSumError(pkgData);

		if (!IsCheckSumError) {
			EquipmentData equipmentData = new EquipmentData();
			equipmentData.setHead1(pkgData[0]);
			equipmentData.setHead2(pkgData[1]);
			equipmentData.setIndex(pkgData[2]);
			equipmentData.setStatus(pkgData[3]);
			equipmentData.setMl(pkgData[4]);
			equipmentData.setXy(pkgData[5]);
			equipmentData.setPI(pkgData[6]);
			equipmentData.setBattery(pkgData[7]);
			equipmentData.setRR((0xFF & pkgData[8]) + ((int) (0xFF & pkgData[9]) << 8));
			equipmentData.setAxis_x((short) ((byte)(0xFF & pkgData[10]) + ((short) (0xFF & pkgData[11]) << 8)));;
			equipmentData.setAxis_y((short) ((byte)(0xFF & pkgData[12]) + ((short) (0xFF & pkgData[13]) << 8)));
			equipmentData.setAxis_z((short) ((byte)(0xFF & pkgData[14]) + ((short) (0xFF & pkgData[15]) << 8)));
			equipmentData.setPress(pkgData[18]);
			equipmentData.setCheckSum(pkgData[pkgLength - 1]);
			equipmentData.setNumTime(System.currentTimeMillis());
			if (equipmentData.getMl() == 255 || equipmentData.getXy() == 127 || equipmentData.getPI() == 0
					|| equipmentData.getRR() == 0) {
				equipmentData.setValid(false);
			} else {
				equipmentData.setValid(true);
			}
			if (mNewDataNotify != null)
				mNewDataNotify.notifyNewData(equipmentData);
		}
	}

	/**
	 * 检查丢包。
	 * 
	 * @param pkgData
	 */
	private void CheckIfPackageLost(byte[] pkgData) {

		pkgIndexNew = 0xFF & pkgData[2];

		IsPackageLost = false;
		if (pkgIndexOld != -1 && (pkgIndexOld + 1) % 256 != pkgIndexNew) {
			IsPackageLost = true;
			Log.w(TAG, String.format("Package lost, pkgIndexOld = %d, pkgIndexNew = %d", pkgIndexOld, pkgIndexNew));
			DumpPackage(pkgData);
		}
		pkgIndexOld = pkgIndexNew;
	}

	/**
	 * 校验检查。
	 * 
	 * @param pkgData
	 */
	private void CheckIfCheckSumError(byte[] pkgData) {
		IsCheckSumError = false;
		byte checkSum;

		// wdl, 特别关注包尾最后2个字节与包头2个字节完全相同的情况，
		// wdl, 采用异或校验方式时，上述情况发生几率较大，会导致CheckIfPackageLost()发生错误判断，
		// wdl, 采用和校验方式时，上述情况发生几率较小，但仍需进一步观察
//		if ((0xFF & pkgData[pkgLength - 2]) == PKG_HEAD[0] && (0xFF & pkgData[pkgLength - 1]) == PKG_HEAD[1]) {
//			DumpPackage(pkgData);
//		}

		checkSum = CalcCheckSum(pkgData);
		if (checkSum != pkgData[pkgLength - 1]) {
			IsCheckSumError = true;
			Log.w(TAG, String.format("check sum failed, checkSum = 0x%02x, pkgData[%d] = 0x%02x", checkSum,
					pkgLength - 1, pkgData[pkgLength - 1]));
			DumpPackage(pkgData);
		}
	}

	/**
	 * 计算被解析数据的校验值
	 * 
	 * @param pkgData
	 * @return 校验值
	 */
	private byte CalcCheckSum(byte[] pkgData) {
		byte checkSum = 0;

		for (int i = 0; i < pkgLength - 1; i++) {
			// checkSum ^= pkgData[i]; //wdl, 异或校验
			checkSum += pkgData[i]; // wdl, 和校验
		}
		return checkSum;
	}

	/**
	 * 根据包长输出原始数据
	 * 
	 * @param pkgData
	 */
	private void DumpPackage(byte[] pkgData) {
		String str = "pkgData = { ";

		for (int i = 0; i < pkgLength; i++) {
			str += String.format("0x%02x ", pkgData[i]);
		}
		str += ("}, pkgLength = " + pkgLength);
		Log.i(TAG, str);
	}

	/**
	 * 输出缓存数组中所有的数据
	 * 
	 * @param pkgData
	 */
	private void DumpPackage_All(byte[] pkgData) {
		String str = "pkgData = { ";

		for (int i = 0; i < parseLength; i++) {
			str += String.format("0x%02x ", pkgData[i]);
		}
		str += ("}, parseLength = " + parseLength);
		Log.i(TAG, str);
	}
}
