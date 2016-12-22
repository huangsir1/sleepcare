package com.sleep.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.sleep.utils.Utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import android.util.Log;

public class BluetoothSocketService {

	// TAG
	private final String TAG = this.getClass().getSimpleName();

	private Context mContext = null;
	private BluetoothAdapter mBluetoothAdapter = null;
	private ConnectThread mConnectThread = null;
	private DataReadThread mDataReadThread = null;

	private DataTransfer mDataTransfer = null;

	public void setConnectListener(DataTransfer dataTransfer) {
		mDataTransfer = dataTransfer;
	}

	public interface DataTransfer {
		public void onConnectSuccess();
		public void onConnectFail();
		public void onReceiveData(byte[] dat, int len);
	}

	public BluetoothSocketService(Context context, BluetoothAdapter bluetoothAdapter) {
		mContext = context;
		mBluetoothAdapter = bluetoothAdapter;
	}

	/**
	 * 进行蓝牙连接。
	 * 
	 * @param device
	 *            需要连接的蓝牙设备。
	 */
	public void connect(BluetoothDevice device) {
		// 开始连接蓝牙
		if (mBluetoothAdapter.isDiscovering())
			mBluetoothAdapter.cancelDiscovery();
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
	}

	/**
	 * 关闭所有数据接收的线程，关闭数据接收功能。
	 */
	public void stop() {
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread.interrupt();
			mConnectThread = null;
		}
		if (mDataReadThread != null) {
			mDataReadThread.cancel();
			mDataReadThread.interrupt();
			mDataReadThread = null;
		}
	}

	/**
	 * 传入BluetoothDevice，获取BluetoothSocket并连接。
	 */
	private class ConnectThread extends Thread {

		private BluetoothSocket socket;

		@SuppressLint("NewApi")
		public ConnectThread(BluetoothDevice device) {

			int sdk = Build.VERSION.SDK_INT;
			if (sdk >= 10) {
				try {
					Log.d(TAG, "ConnectThread create, socket create***1");
					socket = device.createInsecureRfcommSocketToServiceRecord(Utils.COMMON_UUID);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Log.d(TAG, "ConnectThread create, socket create***2");
					socket = device.createRfcommSocketToServiceRecord(Utils.COMMON_UUID);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@SuppressLint("NewApi")
		public void run() {
			try {
				socket.connect(); // 蓝牙连接
			} catch (IOException e) {
				Log.e(TAG, "ConnectThread run failed, socket.connect() failed: " + e.toString());
				// 提示连接失败
				mDataTransfer.onConnectFail();
			}

			if (socket.isConnected()) {
				Log.d(TAG, "ConnectThread run, socket connect success");
				// 提示连接成功
				mDataTransfer.onConnectSuccess();
				
				mDataReadThread = new DataReadThread(socket);
				mDataReadThread.start();
			}
		}

		public void cancel() {
			try {
				Log.d(TAG, "ConnectThread cancel, socket.close()");
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				Log.e(TAG, "ConnectThread cancel failed, socket.close() failed: " + e.toString());
			}
		}
	}

	/**
	 * 传入BluetoothSocket，获取InputStream，接收传来的数据。
	 */
	private class DataReadThread extends Thread {

		private InputStream inputStream;

		public DataReadThread(BluetoothSocket socket) {
			try {
				Log.d(TAG, "DataReadThread create, inputStream ready");
				inputStream = socket.getInputStream();
				// bluetooth_connecting = true;
			} catch (IOException e) {
				Log.e(TAG, "DataReadThread create fail, socket.getInputStream() fail: " + e.toString());
			}
		}

		public void run() {
			Log.d(TAG, "DataReadThread run, receving data");
			int bytes = 0;
			byte[] buffer = new byte[512];
			
			while (true) {
				try {
					// Read from the InputStream
					Arrays.fill(buffer, (byte) 0);
					bytes = inputStream.read(buffer);
					if (bytes > 0) {
						//发送数据
						mDataTransfer.onReceiveData(buffer, bytes);
						if (bytes >= 512) {
							Log.w(TAG, "read data out of length, data length = " + bytes);
						}
					}
				} catch (IOException e) {
					Log.e(TAG, "DataReadThread run failed. inputStream.read() fail: " + e.toString());
					try {
						Log.d(TAG, "DataReadThread run failed, inputStream.close()");
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e1) {
						Log.e(TAG, "DataReadThread run failed. inputStream.close() fail: " + e1.toString());
					}
					break;
				}
			}
		}

		public void cancel() {
			try {
				Log.d(TAG, "DataReadThread cancel, inputStream.close()");
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				Log.e(TAG, "DataReadThread cancel fail. inputStream.close() failed: " + e.toString());
			}
		}
	}
}
