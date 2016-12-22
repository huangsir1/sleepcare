package com.sleep.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

import android.os.Environment;
import android.util.Log;

public class Utils {

	// BluetoothUtils, mDataHandler
	public static final int INITIAL_FILES = 0;
	public static final int SAVE_DATA = 1;
	public static final int SHUTDOWN = 2;
	public static final int EXE_COMPUTATION = 3;
	public static final int DATA_ERROR = 4;
	public static final int INSERT_SEPARATOR = 5;
	public static final int TRANSMIT_MAC_AND_PATH = 6;

	// MainActivity, mHandler
	public static final int DELIVER_RESULTS = 0;
	public static final int TOAST_BLUETOOTH = 1;
	public static final int TOAST_COMPUTATION = 2;
	public static final int TOAST_MEASURE_REEOR = 3;
	public static final int SAVE_TO_SHAREDPREFERENCES = 4;
	public static final int ISCALCULATED = 5;
	public static final int DISMISS_DIALOG = 6;
	public static final int DELETE_FILE = 7;
	public static final int NO_DATA_TIMEOUT = 8;
	public static final int FINAL_REPORT = 9;

	public static final int BT_CONNECTING = 10;
	public static final int BT_CONNECT_SUCCESS = 11;
	public static final int BT_CONNECT_FAILED = 12;
	public static final int COMPUTATION_TOO_SHORT = 21;
	public static final int COMPUTATION_FINISH = 22;
	public static final int COMPUTATION_DUE_TO_NORMAL = 23;
	public static final int COMPUTATION_DUE_TO_ERROR = 24;
	public static final int COMPUTATION_RUNNING = 25;
	public static final int COMPUTATION_OVER = 26;
	public static final int ERROR_ABNORMAL = 30;
	public static final int ERROR_SENSOR_OFF = 31;
	public static final int ERROR_NO_FINGER = 32;
	public static final int ERROR_NO_PULSE_SIGNAL = 34;
	public static final int SAVE_BLUETOOTH_MAC = 41;
	public static final int SAVE_FILE_PATH = 42;
	public static final int SAVE_BLUETOOTH_MAC_AND_FILE_PATH = 43;
	public static final int ISCALCULATED_FALSE = 51;
	public static final int ISCALCULATED_TRUE = 52;
	public static final int ISCALCULATED_COMPUTATION = 53;
	public static final int DISMISS_ALL_DIALOG = 61;
	public static final int DISMISS_DIALOG_BY_INSERT_FINGER = 62;
	public static final int DELETE_FILE_ONLY = 71;
	public static final int DELETE_FILE_DATA_INSUFFICIENT = 72;

	public static final int BLUETOOTH__REQUEST_ENABLE = 99;

	// MonitorFragment, mHandler
	public static final int SHOW_DATA = 0;
	public static final int CHRONOMETER = 1;
	public static final int RESET_DATA = 2;

	public static final int CHRONOMETER_START = 11;
	public static final int CHRONOMETER_STOP = 12;
	public static final int CHRONOMETER_RESET = 13;
	public static final int CHRONOMETER_CONTINUE = 14;

	// ReportFragment, mHandler
	public static final int DEAL_RESULTS = 0;
	public static final int NETWORKBROADCAST = 1;

	// 这条是蓝牙串口通用的UUID，不要更改
	public static final UUID COMMON_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static final String TAG = "btSocket";
	public static final String DEVICE_NAME = "BerryMed";
	public static final String DATA_SEPARATOR = "@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%";
	public static DateFormat in = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
	
	// 数据文件内数据的编码方式
	public static final int DATATYPE_ASCII = 0;
	public static final int DATATYPE_BINARY = 1;
	
	// Const
	public static final UUID UUID_SERVICE_DATA = UUID.fromString("49535343-fe7d-4ae5-8fa9-9fafd205e455");
	public static final UUID UUID_CHARACTER_RECEIVE = UUID.fromString("49535343-1e4d-4bd9-ba61-23c647249616");
	public static final UUID UUID_MODIFY_BT_NAME = UUID.fromString("00005343-0000-1000-8000-00805F9B34FB");

	public static final UUID UUID_CLIENT_CHARACTER_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

	public static final int MESSAGE_OXIMETER_PARAMS = 2003;
	public static final int MESSAGE_OXIMETER_WAVE = 2004;

	public static final String GITHUB_SITE = "https://github.com/zh2x/SpO2-BLE-for-Android";
	
	public static final String REFRESHLIST_ACTION = "com.sleep.updatedata.activity.MonitoringRecordActivity.RefreshList";

	/**
	 * 获取内置SD卡的路径
	 * 
	 * @return 路径的String值
	 */
	public static String getInnerSDCardPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	/**
	 * 计算文件内最新数据的行数，并返回该数值
	 * 
	 * @param filepath
	 *            文件的路径
	 * @return 最新数据的行数
	 */
	public static int lineCountNewest(String filepath) {
		int count = 0; // 用于统计行数，从0开始
		String strbuff = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
			br.readLine(); // 去掉第一行
			while ((strbuff = br.readLine()) != null) { // readLine()方法是按行读的，返回值是这行的内容
				count++; // 每读一行，则变量累加1
				if (DATA_SEPARATOR.equals(strbuff))
					count = 0; // 如果读到分隔符，则重置计数
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 计算文件内所有数据的行数，并返回该数值
	 * 
	 * @param filepath
	 *            文件的路径
	 * @return 所有数据的行数
	 */
	public static int lineCountTotal(String filepath, int dataType) {
		int count = 0; // 用于统计行数，从0开始
		
		if(dataType == DATATYPE_ASCII){
			String strbuff = null;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
				br.readLine(); // 去掉第一行
				while ((strbuff = br.readLine()) != null) { // readLine()方法是按行读的，返回值是这行的内容
					if (!DATA_SEPARATOR.equals(strbuff))
						count++;
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(dataType == DATATYPE_BINARY){
			// binary
			final int data_length = 21; //1+1+1+1+4+1+1+1+1+8+1 = 21
			RandomAccessFile raf = null;
			try {
				raf = new RandomAccessFile(filepath,"r");
				count = (int)(raf.length()/data_length);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return count;
	}

	/**
	 * 计算文件内最新数据的测量时间，并返回该数值
	 * 
	 * @param filepath
	 *            文件的路径
	 * @return 测量时间
	 */
	public static long timeCountNewest(String filepath) {
		long timeCount = 0; // 测量总时间
		String strbuff_new = null; // 最新读取到的数据
		String strbuff_start = null; // 开始时间
		String strbuff_end = null; // 结束时间

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
			strbuff_new = br.readLine(); // 去掉第一行
			while ((strbuff_new = br.readLine()) != null) { // readLine()方法是按行读的，返回值是这行的内容
				if (!DATA_SEPARATOR.equals(strbuff_new)) {
					if (strbuff_start == null) {
						strbuff_start = strbuff_new;
					} else {
						strbuff_end = strbuff_new;
					}
				} else {
					strbuff_start = null;
					strbuff_end = null;
					Log.d(TAG, "*********************strbuff_new == DATA_SEPARATOR*************");
				}
			}
			if(strbuff_start != null && strbuff_end != null)
				timeCount = Long.valueOf(strbuff_end.split(",")[3]) - Long.valueOf(strbuff_start.split(",")[3]);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return timeCount;
	}

	/**
	 * 计算文件内所有数据的测量总时间，并返回该数值
	 * 
	 * @param filepath
	 *            文件的路径
	 * @return 测量总时间
	 */
	public static long timeCountTotal(String filepath, int dataType) {
		long timeCount = 0; // 测量总时间
		
		if(dataType == DATATYPE_ASCII){
			String strbuff_new = null; // 最新读取到的数据
			String strbuff_start = null; // 开始时间
			String strbuff_end = null; // 结束时间
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
				strbuff_new = br.readLine(); // 去掉第一行
				while ((strbuff_new = br.readLine()) != null) { // readLine()方法是按行读的，返回值是这行的内容
					if (!DATA_SEPARATOR.equals(strbuff_new)) {
						if (strbuff_start == null) {
							strbuff_start = strbuff_new;
						} else {
							strbuff_end = strbuff_new;
						}
					} else {
						Log.d(TAG, "*********************strbuff_new == DATA_SEPARATOR*************");
					}
				}
				if(strbuff_start != null && strbuff_end != null)
//					try {
//						timeCount = in.parse(strbuff_end.split(",")[3]).getTime() - in.parse(strbuff_start.split(",")[3]).getTime();
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					timeCount = Long.valueOf(strbuff_end.split(",")[9]) - Long.valueOf(strbuff_start.split(",")[9]);
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(dataType == DATATYPE_BINARY){
			// binary
			final int data_length = 21; //1+1+1+1+4+1+1+1+1+8+1 = 21
			RandomAccessFile raf = null;
			int len = 0;
			try {
				raf = new RandomAccessFile(filepath,"r");
				len = (int)(raf.length()/data_length);
				
				long start = 0, end = 0;
				raf.seek(0*data_length);
				raf.readByte();
				raf.readByte();
				raf.readByte();
				raf.readByte();
				raf.readInt();
				raf.readByte();
				raf.readByte();
				raf.readByte();
				raf.readByte();
				start = raf.readLong();
				raf.readByte();
				
				raf.seek((len - 1)*data_length);
				raf.readByte();
				raf.readByte();
				raf.readByte();
				raf.readByte();
				raf.readInt();
				raf.readByte();
				raf.readByte();
				raf.readByte();
				raf.readByte();
				end = raf.readLong();
				raf.readByte();
				
				timeCount = end - start;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return timeCount;
	}

	/**
	 * 将float型的数值保留若干位小数
	 * 
	 * @param param
	 *            需要处理的数值
	 * @param power
	 *            保留的小数位数
	 * @return
	 */
	public static float formFloat(float param, int power) {
		return (int) (param * 100) / 100f;
	}

	/**
	 * 如果分钟数为个位数，则在十位添加一个0，变成两位数
	 * 
	 * @param time
	 *            分钟数
	 * @return
	 */
	public static String formTime(String time) {
		if (time.length() == 1) {
			time = "0" + time;
		}
		return time;
	}
}
