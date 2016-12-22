package com.sleep.bluetooth;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.sleep.activity.MainActivity;
import com.sleep.analysis.Sleep_ApneaDetection;
import com.sleep.analysis.Sleep_stageSeperate;
import com.sleep.bluetooth.BluetoothSocketService.DataTransfer;
import com.sleep.bluetooth.DataParser.NewDataNotify;
import com.sleep.utils.Utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class BluetoothUtils implements DataTransfer {
	// TAG
	private final String TAG = this.getClass().getSimpleName();
	// Instance
	public static BluetoothUtils mBluetoothUtils = null;
	// INITIAL
	private Context mContext = null;
	private Handler mHandler_MainActivity = null;
	private Handler mHandler_MonitorFragment = null;
	private String userName = null;
	// Bluetooth
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothLeService mBluetoothLeService = null;
	private BluetoothSocketService mBluetoothSocketService = null;
	private BluetoothDevice mBluetoothDevice = null;
	private BluetoothAdapter.LeScanCallback mLeScanCallback = null;
	private final int SCAN_PERIOD = 15000; // 蓝牙搜索持续时间
	private String strTargetBluetoothName = "BerryMed";
	private boolean isBindService = false;
	private boolean isRegisted = false;
	private boolean reconnection = false; // 重新搜索
	private String recent_connected_bluetooth_mac = null;
	public static final int DISCONNECTED = 0;
	public static final int CONNECTING = 1;
	public static final int CONNECTED = 2;
	private int btConnectionState = DISCONNECTED;
	private boolean haveConnected = false; // 是否连接成功过，在设备断开连接时，若为false则删除数据文件
	private int connectingDeviceCount = 0; // 限制正在连接的设备数，等于0时可以连接新的设备，大于0时不能连接新的设备
	// Data
	private DataParser mDataParser = null;
//	private DataStorage mDataStorage = null;
	private int finish_count = 0; // 判断测量终点的计数器
	private boolean finish_flag = false; // 是否停止接收数据，true：测量结束，false：测量未结束
	private EquipmentData mEquipmentData = null; // 定时存储测量数据的数据源
	private String dataFilePath = null; // 数据文件的路径
	// Other
	private boolean isFileInitialed = false;
	private boolean isScanning = false;
	private int isKeepConnected = 0; // 0：保持连接，1：断开连接，生成最终报告
	private Thread mDataThread = null;
	private Handler mDataHandler = null;
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	private final String dataPath = Utils.getInnerSDCardPath() + File.separator + "SleepCare" + File.separator + "data"; // 数据的存储路径
	private boolean noFingerAssist = false; // 刚开始测量的时候，如果没有佩戴传感器，提示佩戴；当接收到数据后，赋值为true，改为提醒测量结束
	// 计算
	public static final int STATE_UNCALCULATING = 0;
	public static final int STATE_CALCULATING = 1;
	private int calculationState = STATE_UNCALCULATING;
	private boolean isDataFileCalculated = true; // 当前的数据文件是否被计算过，false：未计算，true：已计算
	private boolean isAutoCalculate = false; // 判断是否是拔掉手指10秒后自动计算，若是，则不取消弹窗
	private boolean isDataSufficient = false; // 最后一次计算时，数据是否足够，是否得到报告
	// binary
	private FileOutputStream fos_status = null;
	private DataOutputStream dos_status = null;
	private final String filename_extension_dat = ".txt";
	// private final String filename_extension_taiir = ".taiir";
	private int dataType = Utils.DATATYPE_ASCII; // 存储在文件中的数据类型
	// Timer
	private static final boolean ON = true;
	private static final boolean OFF = false;
	private boolean timerState = OFF;
	private Timer saveData_1s_Timer = new Timer(); // 定时存储测量数据的定时器
	private TimerTask saveData_1s_Task = null;
	private long time_err = 0; // 判断设备异常关闭的时间值
	private int count_err = 0; // 判断设备异常关闭的计数器
	// private int temp_fileLines = 0; //
	// 每次由于设备脱落引起计算时，保存文件内数据的行数，用于设备关闭引起计算时进行对比，若文件行数未发生变化，则跳过计算
	private boolean isNoData = false; // 判断是否设备脱落
	private int no_data_finish_count = 0; // 设备脱落时开始计时，10分钟后终止测量
	// save to userhistory // 2016.7.25
	private String mFilePath = null;
	private String mMacAddress = null;
	// auto connect
	private boolean autoConnectFlag = false; // 自动搜索是否开启
	private Timer autoConnectTimer = new Timer();
	private TimerTask autoConnectTask = null;
	private final int AUTO_CONNECT_INTERVAL = 120000; // 10分钟 = 600000ms
	private int connect_times = 0;
	private boolean autoConnectTimerUseable = false;
	private Writer out=null;

	public synchronized int getBtConnectionState() {
		return btConnectionState;
	}

	public synchronized void setBtConnectionState(int btConnectionState) {
		this.btConnectionState = btConnectionState;
	}

	public synchronized boolean isScanning() {
		return isScanning;
	}

	public synchronized void setScanning(boolean isScanning) {
		this.isScanning = isScanning;
	}

	public synchronized int getCalculationState() {
		return calculationState;
	}

	public synchronized void setCalculationState(int calculationState) {
		this.calculationState = calculationState;
	}

	public synchronized boolean isAutoConnectFlag() {
		return autoConnectFlag;
	}

	public synchronized void setAutoConnectFlag(boolean autoConnectFlag) {
		this.autoConnectFlag = autoConnectFlag;
	}

	private BluetoothUtils() {
		Log.d(TAG, "BluetoothUtils构造方法被调用！");
	}

	/**
	 * 创建新的BluetoothUtils实例，并返回该实例
	 * 
	 * @return
	 */
	public static void creatNewInstance() {
		mBluetoothUtils = new BluetoothUtils();
	}

	/**
	 * 获取BluetoothUtils的实例
	 * 
	 * @return
	 */
	public static BluetoothUtils getInstance() {
		return mBluetoothUtils;
	}

	/**
	 * BluetoothUtils类的初始化
	 * 
	 * @param mContext
	 *            上下文
	 * @param mHandler_MainActivity
	 *            MainActivity中的Handler
	 * @param mHandler_MonitorFragment
	 *            MonitorFragment中的Handler
	 * @param userName
	 *            当前选择的用户的名字
	 */
	public void BluetoothUtilsInitial(final Context mContext, Handler mHandler_MainActivity,
			Handler mHandler_MonitorFragment, String userName) {
		this.mContext = mContext;
		this.mHandler_MainActivity = mHandler_MainActivity;
		this.mHandler_MonitorFragment = mHandler_MonitorFragment;
		this.userName = userName;
		initDataThread();
		initial_package_parse();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		recent_connected_bluetooth_mac = ((MainActivity) mContext).getMacAddress();
		if (Build.VERSION.SDK_INT >= 18) {
			mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
				@Override
				public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
					Log.i(TAG, "find device by onLeScan(): " + device.getName() + ", " + device.getAddress());
					// 蓝牙设备搜索时，筛选出名字为"BerryMed"的设备
					if (device != null && device.getName() != null && strTargetBluetoothName.equals(device.getName())) {
						connect(mContext, device);
					} else {
						Log.i(TAG, "find unknow device: " + device.getAddress());
					}
				}
			};
		} else {
			mLeScanCallback = null;
		}
	}

	/**
	 * 创建两个线程，一个用于判断测量异常和测量终止，一个用于创建Handler来接收数据及计算等
	 */
	private void initDataThread() {

		saveData_1s_Timer.purge();
		saveData_1s_Task = new TimerTask() {
			int toastcount = 0; // 防止连续弹出提示

			@Override
			public void run() {
				if (mEquipmentData != null) {
					// 测量发生异常时的提醒，传感器异常、指端传感器脱落、未检测到脉率信号
					if (toastcount == 0 && (mEquipmentData.getStatus() != 0x00 && mEquipmentData.getStatus() != 0x08)) {
						isNoData = true;
						toastcount++;

						if (!isAutoConnectFlag()) {
							if (noFingerAssist) {
								Log.i(TAG, "弹出toast，测量异常，没有测量数据");
								mHandler_MainActivity.obtainMessage(Utils.TOAST_MEASURE_REEOR, Utils.ERROR_ABNORMAL, -1)
										.sendToTarget();
							} else {
								Log.i(TAG, "弹出toast，测量异常，传感器脱落");
								mHandler_MainActivity
										.obtainMessage(Utils.TOAST_MEASURE_REEOR, Utils.ERROR_NO_FINGER, -1)
										.sendToTarget();
							}
						}
					} else if (mEquipmentData.getStatus() == 0x00 || mEquipmentData.getStatus() == 0x08) {
						isNoData = false;
						toastcount = 0;
						setAutoConnectFlag(false);
					}

					// 根据时间值是否发生改变，判断测量是否中断
					if (time_err == mEquipmentData.getNumTime()) {
						count_err++;
					} else {
						time_err = mEquipmentData.getNumTime();
						count_err = 0;
					}
				}

				// 设备关闭后超过20秒时的处理
				if (count_err == 20) {
					Log.i(TAG, "saveData_1s, count_err > 20s");
					count_err++;
					mEquipmentData = null;
					
					// 暂停计时
					timerCountStop();
					
					// 取消所有弹框
					mHandler_MainActivity
							.obtainMessage(Utils.DISMISS_DIALOG, Utils.DISMISS_ALL_DIALOG, -1)
							.sendToTarget();
					
					if (!isDataFileCalculated) {
						isAutoCalculate = true;
						// 进行计算
						exeCalculation(0);
					}

					// 开启自动连接
					if (((MainActivity) mContext).getCurrentPageId() == 1) {
						mDataHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								startAutoConnect();
							}
						}, 10000);
					}
				}

				// 设备脱落超过10分钟时的处理
				if (isNoData) {
					no_data_finish_count++;
					if (no_data_finish_count == 300) { // 10分钟
						Log.i(TAG, "saveData_1s_Timer, no_data_finish_count > 10min");
						no_data_finish_count++;
						// 取消弹窗及跳转到报告页面
						if (isDataSufficient) {
							mHandler_MainActivity.obtainMessage(Utils.NO_DATA_TIMEOUT, 1, -1).sendToTarget();
						} else {
							mHandler_MainActivity.obtainMessage(Utils.NO_DATA_TIMEOUT, 0, -1).sendToTarget();
						}
						mHandler_MainActivity.sendEmptyMessage(Utils.FINAL_REPORT);
						// killDataThread(false);
						mBluetoothUtils.stop();

						// 开启自动连接
						if (((MainActivity) mContext).getCurrentPageId() == 1) {
							mDataHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									startAutoConnect();
								}
							}, 10000);
						}
					}
				} else {
					no_data_finish_count = 0;
				}
			}
		};
		saveData_1s_Timer.scheduleAtFixedRate(saveData_1s_Task, 0, 1000);

		mDataThread = new Thread() {
			@SuppressLint("HandlerLeak")
			@Override
			public void run() {
				Looper.prepare();
				mDataHandler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						switch (msg.what) {
						case Utils.INITIAL_FILES:
							Log.i(TAG, "mDataHandler, INITIAL_FILES");
							// 判断文件夹是否存在，若不存在，则创建文件夹
							File folder = new File(dataPath);
							if (!folder.exists()) {
								folder.mkdirs();
							}
							// 获取数据文件路径
							dataFilePath = dataPath + File.separator + userName + "_" + sdf.format(new Date())
									+ "_SleepData_status" + filename_extension_dat;
							// 输出文件路径
							Log.i(TAG, "Utils.INITIAL_FILES-----------dataFilePath:" + dataFilePath);
							// 2016.7.25
							mFilePath = dataFilePath;
							
							// 2016.9.15
//							mDataStorage = new DataStorage(dataFilePath, dataType);
//							mDataStorage.start();
							
							// 创建文件
							if (dataType == Utils.DATATYPE_ASCII) {
								try {
									fos_status = new FileOutputStream(dataFilePath);
									dos_status = new DataOutputStream(fos_status);
									out=new OutputStreamWriter(fos_status);
									
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
							} else if (dataType == Utils.DATATYPE_BINARY) { }
							break;
						case Utils.SAVE_DATA:
							final EquipmentData mEquipmentData = (EquipmentData) msg.obj;
							// 界面上实时显示数据
							mHandler_MonitorFragment.obtainMessage(Utils.SHOW_DATA, mEquipmentData).sendToTarget();

							// 存储用于分析的数据(按状态位存储)
							if ((mEquipmentData.getStatus() == 0x00 || mEquipmentData.getStatus() == 0x08)) {
								// 有数据接收到，就将UserHistory中的字段修改为未计算
								if (isDataFileCalculated) {
									mHandler_MainActivity.sendMessageDelayed(mHandler_MainActivity
											.obtainMessage(Utils.ISCALCULATED, Utils.ISCALCULATED_FALSE, -1), 500);
									isDataFileCalculated = false;
								}
								try {
									if (dataType == Utils.DATATYPE_ASCII) {
										// binary 1+1+1+1+4+1+1+1+1+8+1 = 21
										out.write(String.valueOf( mEquipmentData.getMl()+","
																+mEquipmentData.getXy()+","
																+mEquipmentData.getPI()+","
																+mEquipmentData.getStatus()+","
																+mEquipmentData.getRR()+","
																+mEquipmentData.getHead1()+","
																+mEquipmentData.getHead2()+","
																+mEquipmentData.getBattery()+","
																+mEquipmentData.getIndex()+","
																+mEquipmentData.getNumTime()+","
																+mEquipmentData.getCheckSum()+","
																+mEquipmentData.getPress()+","
																+mEquipmentData.getAxis_x()+","
																+mEquipmentData.getAxis_y()+","
																+mEquipmentData.getAxis_z()+"\r\n"));
										out.flush();
										
									} else if (dataType == Utils.DATATYPE_BINARY) {
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							break;
						case Utils.SHUTDOWN:
							Log.i(TAG, "mDataHandler, SHUTDOWN");
							break;
						case Utils.EXE_COMPUTATION:
							Log.i(TAG, "mDataHandler, EXE_COMPUTATION");
							if (!isDataFileCalculated) {
								setCalculationState(STATE_CALCULATING);
								Sleep_stageSeperate mSleep_stageSeperate = null;
								Sleep_ApneaDetection mSleep_ApneaDetection = null;
								long time_last = Utils.timeCountTotal(dataFilePath, dataType);
								int line_count = Utils.lineCountTotal(dataFilePath, dataType);
								Log.i(TAG, "mDataHandler, time_last = " + time_last + ", line_count = " + line_count);
								if (time_last / 1000 > 300 && line_count > 240) { // 设定测量最短时间，5分钟
									isDataSufficient = true;
									if (!isAutoCalculate) {
//										mHandler_MainActivity
//												.obtainMessage(Utils.TOAST_COMPUTATION, Utils.COMPUTATION_RUNNING, -1)
//												.sendToTarget();
									}
									if (time_last / 1000 > 600 && line_count > 600) { // 如果测量时间大于10分钟，则计算时舍去前5分钟的数据
										Log.i(TAG, "mDataHandler, 开始计算");
//										mSleep_stageSeperate = new Sleep_stageSeperate();
//										mSleep_stageSeperate.calculate(dataFilePath, true, false, Utils.DATA_SEPARATOR,
//												dataType);
										mSleep_ApneaDetection = new Sleep_ApneaDetection();
										mSleep_ApneaDetection.calculate(dataFilePath, true, false, Utils.DATA_SEPARATOR,
												dataType);
									} else {
										Log.i(TAG, "mDataHandler, 开始计算，不去前五分钟");
//										mSleep_stageSeperate = new Sleep_stageSeperate();
//										mSleep_stageSeperate.calculate(dataFilePath, false, false, Utils.DATA_SEPARATOR,
//												dataType);
										mSleep_ApneaDetection = new Sleep_ApneaDetection();
										mSleep_ApneaDetection.calculate(dataFilePath, false, false,
												Utils.DATA_SEPARATOR, dataType);
									}

									// 将计算结果传回界面存入数据库并显示
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("mSleep_stageSeperate", mSleep_stageSeperate);
									map.put("mSleep_ApneaDetection", mSleep_ApneaDetection);
									if (isKeepConnected == 1) {
										map.put("isFinalReport", 1);
									} else {
										map.put("isFinalReport", 0);
									}
									map.put("isAutoCalculate", isAutoCalculate);
									mHandler_MainActivity.obtainMessage(Utils.DELIVER_RESULTS, map).sendToTarget();
								} else {
									if (!isAutoCalculate) {
										// 取消所有弹框
										mHandler_MainActivity
												.obtainMessage(Utils.DISMISS_DIALOG, Utils.DISMISS_ALL_DIALOG, -1)
												.sendToTarget();
									}
									// 弹出toast，数据太短
									mHandler_MainActivity
											.obtainMessage(Utils.TOAST_COMPUTATION, Utils.COMPUTATION_TOO_SHORT, -1)
											.sendToTarget();
									if (isKeepConnected == 1) {
										// 数据不足时，删除文件及对应的UserHistory表
										mHandler_MainActivity.obtainMessage(Utils.DELETE_FILE,
												Utils.DELETE_FILE_DATA_INSUFFICIENT,
												((MainActivity) mContext).getCurrentUserHistoryId(), dataFilePath)
												.sendToTarget();
									}
									setCalculationState(STATE_UNCALCULATING);
								}

								// count_err = 0;
								// temp_fileLines = line_count;
								isDataFileCalculated = true;
								isAutoCalculate = false;
							}
							
							if (isKeepConnected == 1) {
								// killDataThread(false);
								mBluetoothUtils.stop();
								isKeepConnected = 0;
								
								noFingerAssist = false;

								// 开启自动连接
								if (((MainActivity) mContext).getCurrentPageId() == 1) {
									mDataHandler.postDelayed(new Runnable() {
										@Override
										public void run() {
											startAutoConnect();
										}
									}, 10000);
								}
							}
							break;
						case Utils.INSERT_SEPARATOR:
							Log.i(TAG, "mDataHandler, INSERT_SEPARATOR");
							break;
						// 2016.7.25
						case Utils.TRANSMIT_MAC_AND_PATH:
							Log.i(TAG, "mDataHandler, TRANSMIT_MAC_AND_PATH");
							if (mFilePath != null && mMacAddress != null) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("mFilePath", mFilePath);
								map.put("mMacAddress", mMacAddress);
								mHandler_MainActivity.obtainMessage(Utils.SAVE_TO_SHAREDPREFERENCES,
										Utils.SAVE_BLUETOOTH_MAC_AND_FILE_PATH, -1, map).sendToTarget();
								mFilePath = null;
								mMacAddress = null;
							} else {
								mDataHandler.sendEmptyMessageDelayed(Utils.TRANSMIT_MAC_AND_PATH, 500);
							}
							break;
						}
					}
				};
				Looper.loop();
			}
		};
		mDataThread.start();
	}
	/**
	 * 初始化接收数据的类
	 */
	private void initial_package_parse() {
		mDataParser = new DataParser(new NewDataNotify() {

			@Override
			public void notifyNewData(EquipmentData equipmentData) {
				// Timer中使用
				mEquipmentData = equipmentData;
				
				// 根据status值进行计数，判断测量是否终止
				if (mEquipmentData.getStatus() != 0x00 && mEquipmentData.getStatus() != 0x08) {
					if(finish_count == 0){
						timerCountStop();
					}
					finish_count++;
				} else {
					// 仅用于刚连接上设备时，如果用户未佩戴设备，提醒用户佩戴
					noFingerAssist = true;

					if (finish_count > 0 && isFileInitialed) {
						timerCountContinue();
					}
					if (finish_count > 0) {
						mHandler_MainActivity
								.obtainMessage(Utils.DISMISS_DIALOG, Utils.DISMISS_DIALOG_BY_INSERT_FINGER, -1)
								.sendToTarget(); // 取消弹窗
						finish_count = 0;
						finish_flag = false; // 开启数据接收
					}

				}
				
				// 测量终止
				if (finish_count >= 10) { // 测量终点延时，每秒1个数据，10秒延时
					if (!finish_flag && finish_count <= 10) {
						Log.i(TAG, "notifyNewData(), finish_count > 10s");
						isAutoCalculate = true;
						exeCalculation(0);// 进行计算
					}
				}
				
				// 测量进行中
				if (!finish_flag) {
					// 存储用于计算和分析的数据
					mDataHandler.obtainMessage(Utils.SAVE_DATA, equipmentData).sendToTarget();
					
					// 2016.9.15
//					// 界面上实时显示数据
//					mHandler_MonitorFragment.obtainMessage(Utils.SHOW_DATA, equipmentData).sendToTarget();
//
//					// 存储用于分析的数据(按状态位存储)
//					if ((equipmentData.getStatus() != 0x00 && equipmentData.getStatus() != 0x01
//							&& equipmentData.getStatus() != 0x02 && equipmentData.getStatus() != 0x04)) {
//						// 有数据接收到，就将UserHistory中的字段修改为未计算
//						if (isDataFileCalculated) {
//							mHandler_MainActivity.obtainMessage(Utils.ISCALCULATED, Utils.ISCALCULATED_FALSE, -1)
//									.sendToTarget();
//							isDataFileCalculated = false;
//						}
//						mDataStorage.addData(equipmentData);
//					}
				}
			}
		});
		mDataParser.start();
	}

	/**
	 * 删除数据不足的文件
	 */
	private void deleteFile() {
		Log.i(TAG, "deleteFile()");
		// 数据不足时，删除文件及对应的UserHistory表
		if (dataFilePath != null) {
			mHandler_MainActivity.obtainMessage(Utils.DELETE_FILE, Utils.DELETE_FILE_ONLY, -1, dataFilePath)
					.sendToTarget();
			isFileInitialed = false;
			dataFilePath = null;
		} else {
			Log.w(TAG, "dataFilePath == null, file is already deleted!");
		}
	}

	/**
	 * 打开蓝牙
	 */
	public void enable() {
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}
	}

	/**
	 * 判断蓝牙是否可用
	 * 
	 * @return true：可用，false：不可用
	 */
	public boolean isEnable() {
		return mBluetoothAdapter.isEnabled();
	}

	private final Runnable cancelRunnable = new Runnable() {
		@Override
		public void run() {
			Log.i(TAG, "Timeout, StopScan！");
			startScan(false);
			if (getBtConnectionState() != CONNECTED) {
				setBtConnectionState(DISCONNECTED);
				if (!isAutoConnectFlag()) {
					mHandler_MainActivity.obtainMessage(Utils.TOAST_BLUETOOTH, Utils.BT_CONNECT_FAILED, -1)
							.sendToTarget();
				}
				connectingDeviceCount = 0;
				mBluetoothUtils.stop();
				deleteFile();
			}
		}
	};

	private final Handler postHandler = new Handler();

	/**
	 * 扫描蓝牙设备
	 * 
	 * @param flag
	 *            true: 进行扫描；false: 停止扫描
	 */
	@TargetApi(21)
	public void startScan(boolean flag) {
		if (!flag) {
			Log.i(TAG, "StopScan！");
			setScanning(false);
			if (Build.VERSION.SDK_INT >= 18) {
				postHandler.removeCallbacks(cancelRunnable);
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
			} else {
				postHandler.removeCallbacks(cancelRunnable);
				mBluetoothAdapter.cancelDiscovery();
			}
		} else if (recent_connected_bluetooth_mac != null && !reconnection) {
			Log.i(TAG, "Directly connect to the last device: " + recent_connected_bluetooth_mac);
			// 参数初始化及创建存储文件
			if (!isFileInitialed) {
				initials();
			}
			// 显示蓝牙搜索动画
			if (!isAutoConnectFlag()) {
				mHandler_MainActivity.obtainMessage(Utils.TOAST_BLUETOOTH, Utils.BT_CONNECTING, -1).sendToTarget();
			}
			mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(recent_connected_bluetooth_mac);
			connect(mContext, mBluetoothDevice);
		} else {
			Log.i(TAG, "StartScan！");
			setScanning(true);
			// 显示蓝牙搜索动画
			if (!isAutoConnectFlag()) {
				mHandler_MainActivity.obtainMessage(Utils.TOAST_BLUETOOTH, Utils.BT_CONNECTING, -1).sendToTarget();
			}
			if (Build.VERSION.SDK_INT >= 18) {
				mBluetoothAdapter.startLeScan(mLeScanCallback);
				postHandler.postDelayed(cancelRunnable, SCAN_PERIOD);
			} else {
				mBluetoothAdapter.startDiscovery();
				postHandler.postDelayed(cancelRunnable, SCAN_PERIOD);
			}
			// 参数初始化及创建存储文件
			if (!isFileInitialed) {
				initials();
			}
			reconnection = false;
		}
	}

	/**
	 * 连接蓝牙设备
	 * 
	 * @param context
	 *            上下文
	 * @param device
	 *            要进行连接的设备
	 */
	@SuppressLint("NewApi")
	public void connect(Context context, BluetoothDevice device) {
		Log.i(TAG, "Connecting device: " + device.getName() + ", " + device.getAddress() + ", "
				+ "connectingDeviceCount = " + connectingDeviceCount);
		setBtConnectionState(CONNECTING);

		if (connectingDeviceCount == 0) {
			connectingDeviceCount++;
			mBluetoothDevice = device;
			if (device.getAddress().toLowerCase().startsWith("00:a0:50") && Build.VERSION.SDK_INT >= 18) {
				Log.i(TAG, "该设备是低功耗设备！" + device.getAddress());
				startScan(false);
				bindService(context);
			} else if (!device.getAddress().toLowerCase().startsWith("00:a0:50")) {
				Log.i(TAG, "该设备不是低功耗设备！" + device.getAddress());
				startScan(false);
				mBluetoothSocketService = new BluetoothSocketService(context, mBluetoothAdapter);
				mBluetoothSocketService.setConnectListener(BluetoothUtils.this);
				mBluetoothSocketService.connect(device);
			}
		}
	}

	/**
	 * 当搜索设备失败，点击重新搜索时需要进行的操作
	 */
	public void reconnectionAndResetCount() {
		reconnection = true;
		connectingDeviceCount = 0;
	}

	/**
	 * 初始化变量及创建文件
	 */
	private void initials() {
		Log.i(TAG, "initials()");
		// 初始化参数
		setCalculationState(STATE_UNCALCULATING);
		setBtConnectionState(DISCONNECTED);
		finish_count = 0; // 判断测量终点的计数器
		finish_flag = false; // 是否停止接收数据，true：测量结束，false：测量未结束
		isDataFileCalculated = true;
		isDataSufficient = false;
		mEquipmentData = null; // 定时存储测量数据的数据源
		// time_err = 0; // 判断设备异常关闭字符串
		count_err = 0; // 判断设备异常关闭的计数器
		// temp_fileLines = 0; //
		// 每次由于设备脱落引起计算时，保存文件内数据的行数，用于设备关闭引起计算时进行对比，若文件行数未发生变化，则跳过计算
		mDataHandler.sendEmptyMessage(Utils.INITIAL_FILES);
		isFileInitialed = true;
		haveConnected = false;
	}

	/**
	 * 计时器开始计时
	 */
	private void timerCountStart() {
		Log.i(TAG, "timerCountStart()");
		// 2016.7.25
		mDataHandler.sendEmptyMessage(Utils.TRANSMIT_MAC_AND_PATH);
		connectingDeviceCount = 0; // 允许设备连接
		if (!timerState) {
			timerState = ON;
			mHandler_MonitorFragment.obtainMessage(Utils.CHRONOMETER, Utils.CHRONOMETER_START, -1).sendToTarget();
		}
	}

	/**
	 * 计时器停止计时，界面显示的指标重置
	 */
	public void timerCountStop() {
		Log.i(TAG, "timerCountStop()");
		if (timerState) {
			timerState = OFF;
			mHandler_MonitorFragment.obtainMessage(Utils.CHRONOMETER, Utils.CHRONOMETER_STOP, -1).sendToTarget();
		}
		mHandler_MonitorFragment.sendEmptyMessage(Utils.RESET_DATA); // 重置界面上的数据
	}

	/**
	 * 计时器重置并停止计时
	 */
	private void timerCountReset() {
		Log.i(TAG, "timerCountReset()");
		timerState = OFF;
		mHandler_MonitorFragment.obtainMessage(Utils.CHRONOMETER, Utils.CHRONOMETER_STOP, -1).sendToTarget();
		mHandler_MonitorFragment.obtainMessage(Utils.CHRONOMETER, Utils.CHRONOMETER_RESET, -1).sendToTarget();
	}

	/**
	 * 计时器继续计时
	 */
	private void timerCountContinue() {
		Log.i(TAG, "timerCountContinue()");
		if (!timerState) {
			timerState = ON;
			mHandler_MonitorFragment.obtainMessage(Utils.CHRONOMETER, Utils.CHRONOMETER_CONTINUE, -1).sendToTarget();
		}
	}

	/**
	 * 断开蓝牙设备
	 */
	public void disconnect() {
		if (mBluetoothLeService != null) {
			mBluetoothLeService.disconnect();
		}
	}

	/**
	 * 关闭蓝牙连接，结束一次测量，下次测量会重新创建数据文件
	 */
	public void stop() {
		Log.i(TAG, "BluetoothUtils.stop()");
		connectingDeviceCount = 0;
		isFileInitialed = false;
		isNoData = false;
		setBtConnectionState(DISCONNECTED);
		postHandler.removeCallbacks(cancelRunnable);
		timerCountReset();
		if (mBluetoothSocketService != null) {
			mBluetoothSocketService.stop();
			mBluetoothSocketService = null;
		}
		if (mBluetoothLeService != null) {
			mBluetoothLeService.disconnect();
			mBluetoothLeService = null;
		}
		unbindService(mContext);
	}

	/**
	 * 确定蓝牙不再会被使用的情况下，彻底关闭蓝牙功能模块
	 * 
	 * @param afterStop
	 *            是否在调用stop()方法之后被调用
	 */
	public void killDataThread(boolean afterStop) {
		Log.i(TAG, "BluetoothUtils.killDataThread()");
		if (!afterStop) {
			mBluetoothUtils.stop();
		}
		mDataParser.stop();
//		mDataStorage.stop();
		unregisterBroadcastReceiver(mContext);
		if (saveData_1s_Task != null) {
			saveData_1s_Task.cancel();
			saveData_1s_Timer.purge();
			saveData_1s_Timer.cancel();
			saveData_1s_Timer = null;
		}
		if (mDataThread != null) {
			if (mDataHandler != null) {
				mDataHandler.removeCallbacksAndMessages(null);
				mDataHandler = null;
			}
			mDataThread.interrupt();
			mDataThread = null;
		}
		mBluetoothUtils = null;
	}

	/**
	 * 执行计算
	 * 
	 * @param iskillDataThread
	 *            计算结束后是否调用killDataThread(). 0：不调用，生成阶段报告；1：调用，则生成的报告为最终报告
	 */
	public void exeCalculation(int isKeepConnected) {
		Log.i(TAG, "exeCalculation()");
		finish_flag = true;  // 停止接收数据
		this.isKeepConnected = isKeepConnected;
		if (getCalculationState() == STATE_UNCALCULATING) {
			mDataHandler.sendEmptyMessage(Utils.EXE_COMPUTATION);
		}
	}

	/**
	 * 开启自动蓝牙连接
	 */
	public void startAutoConnect() {
		Log.d(TAG, "startAutoConnect()");
		if (!isAutoConnectFlag() && getBtConnectionState() == DISCONNECTED
				&& (recent_connected_bluetooth_mac = ((MainActivity) mContext).getMacAddress()) != null) {
			setAutoConnectFlag(true);
			autoConnectTimerUseable = true;

			if (autoConnectTask == null) {
				autoConnectTask = new TimerTask() {
					@Override
					public void run() {
						cancelAutoConnect();
					}
				};
			}
			autoConnectTimer.scheduleAtFixedRate(autoConnectTask, AUTO_CONNECT_INTERVAL, AUTO_CONNECT_INTERVAL);

			AutoConnect();
		}
	}

	/**
	 * 执行自动蓝牙连接
	 */
	private void AutoConnect() {
		Log.d(TAG, "AutoConnect()");
		if (autoConnectTimerUseable && getBtConnectionState() == DISCONNECTED) {
			Log.d(TAG, "Test, AutoConnect(), connect_times = " + connect_times);
			startScan(true);
			connect_times++;
		} else {
			Log.d(TAG, "The auto connection is successed or canceled!");
			cancelAutoConnect();
		}
	}

	/**
	 * 取消自动蓝牙连接
	 */
	private void cancelAutoConnect() {
		Log.d(TAG, "cancelAutoConnect()");
		autoConnectTimerUseable = false;
		connect_times = 0;
		if (autoConnectTask != null) {
			autoConnectTask.cancel();
			autoConnectTask = null;
			autoConnectTimer.purge();
			
			mDataHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					setAutoConnectFlag(false);
				}
			}, 60000);
		}
	}

	@SuppressWarnings("static-access")
	private void bindService(Context context) {
		if (!isBindService) {
			Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
			boolean flag = context.bindService(gattServiceIntent, mServiceConnection, context.BIND_AUTO_CREATE);
			isBindService = true;
			Log.i(TAG, "bindService() = " + flag);
		} else {
			Log.i(TAG, "Service is already binded");
		}
	}

	private void unbindService(Context context) {
		if (isBindService) {
			context.unbindService(mServiceConnection);
			isBindService = false;
		} else {
			Log.i(TAG, "Service is already unbinded");
		}
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder service) {
			Log.i(TAG, "onServiceConnected()");
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
			}
			boolean flag = mBluetoothLeService.connect(mBluetoothDevice.getAddress());
			Log.i(TAG, "mBluetoothLeService.connect(mBluetoothDevice.getAddress()): " + flag);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			Log.i(TAG, "onServiceDisconnected()");
			mBluetoothLeService = null;
			// mBluetoothDevice = null;
		}
	};

	private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			// BLE
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				Log.i(TAG, "BluetoothLeService.ACTION_GATT_CONNECTED");
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
				Log.i(TAG, "BluetoothLeService.ACTION_GATT_DISCONNECTED");
				setBtConnectionState(DISCONNECTED);
				unbindService(mContext);
				if (count_err == 0) {
					timerCountReset();
				}
				// 蓝牙连接失败
				if (!haveConnected) {
					if (!isAutoConnectFlag()) {
						mHandler_MainActivity.obtainMessage(Utils.TOAST_BLUETOOTH, Utils.BT_CONNECT_FAILED, -1)
								.sendToTarget();
					}
					deleteFile();
				}
				connectingDeviceCount = 0;
				haveConnected = false;
				if (isAutoConnectFlag()) {
					AutoConnect();
				} else {
					cancelAutoConnect();
				}
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
				Log.i(TAG, "BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED");
				for (BluetoothGattService service : mBluetoothLeService.getSupportedGattServices()) {
					if (service.getUuid().equals(Utils.UUID_SERVICE_DATA)) {
						for (BluetoothGattCharacteristic ch : service.getCharacteristics()) {
							if (ch.getUuid().equals(Utils.UUID_CHARACTER_RECEIVE)) {
								mBluetoothLeService.setCharacteristicNotification(ch, true);
								setBtConnectionState(CONNECTED);
								haveConnected = true;
								// 2016.7.25
								mMacAddress = mBluetoothDevice.getAddress();
								recent_connected_bluetooth_mac = mBluetoothDevice.getAddress();
								if (count_err == 0) {
									timerCountStart();
								} else {
									timerCountContinue();
								}
								// 弹出toast，蓝牙连接成功
								if (!isAutoConnectFlag()) {
									mHandler_MainActivity
											.obtainMessage(Utils.TOAST_BLUETOOTH, Utils.BT_CONNECT_SUCCESS, -1)
											.sendToTarget();
								}
								cancelAutoConnect();
							}
						}
					}
				}
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				Log.i(TAG, "BluetoothLeService.ACTION_DATA_AVAILABLE");
				Toast.makeText(mContext, intent.getStringExtra(BluetoothLeService.EXTRA_DATA), Toast.LENGTH_SHORT)
						.show();
			} else if (BluetoothLeService.ACTION_SPO2_DATA_AVAILABLE.equals(action)) {
				mDataParser.addData(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA), 0);
			}

			// not BLE
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				Log.i(TAG, "BluetoothDevice.ACTION_FOUND");
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.i(TAG, "find device by BluetoothDevice.ACTION_FOUND: " + device.getName() + ", "
						+ device.getAddress());
				// 蓝牙设备搜索时，筛选出名字为"BerryMed"的设备
				if (device != null && device.getName() != null && strTargetBluetoothName.equals(device.getName())) {
					connect(mContext, device);
				} else {
					Log.i(TAG, "find unknow device: " + device.getAddress());
				}
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				Log.i(TAG, "BluetoothDevice.ACTION_BOND_STATE_CHANGED");
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				switch (device.getBondState()) {
				case BluetoothDevice.BOND_BONDING:
					Log.i(TAG, "正在配对......");
					device.setPairingConfirmation(true);
					break;
				case BluetoothDevice.BOND_BONDED:
					Log.i(TAG, "完成配对");
					try {
						ClsUtils.cancelPairingUserInput(device.getClass(), device);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case BluetoothDevice.BOND_NONE:
					Log.i(TAG, "取消配对");
					break;
				default:
					break;
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				Log.i(TAG, "BluetoothDevice.ACTION_DISCOVERY_FINISHED");
			} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				Log.i(TAG, "BluetoothDevice.ACTION_DISCOVERY_STARTED");
			}
		}
	};

	private IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeService.ACTION_SPO2_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		return intentFilter;
	}

	public void registerBroadcastReceiver(Context context) {
		if (!isRegisted) {
			context.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
			isRegisted = true;
		} else {
			Log.i(TAG, "mGattUpdateReceiver is already registered");
		}
	}

	private void unregisterBroadcastReceiver(Context context) {
		if (isRegisted) {
			context.unregisterReceiver(mGattUpdateReceiver);
			isRegisted = false;
		} else {
			Log.i(TAG, "mGattUpdateReceiver is already unregistered");
		}
	}

	@Override
	public void onConnectSuccess() {
		// 蓝牙连接成功
		setBtConnectionState(CONNECTED);
		haveConnected = true;
		// 2016.7.25
		mMacAddress = mBluetoothDevice.getAddress();
		recent_connected_bluetooth_mac = mBluetoothDevice.getAddress();
		if (count_err == 0) {
			timerCountStart();
		} else {
			timerCountContinue();
		}
		if (!isAutoConnectFlag()) {
			mHandler_MainActivity.obtainMessage(Utils.TOAST_BLUETOOTH, Utils.BT_CONNECT_SUCCESS, -1).sendToTarget();
		}
		cancelAutoConnect();
	}

	@Override
	public void onConnectFail() {
		// 蓝牙连接失败
		setBtConnectionState(DISCONNECTED);
		connectingDeviceCount = 0;
		if (count_err == 0) {
			timerCountReset();
		}
		if (!haveConnected) {
			if (!isAutoConnectFlag()) {
				mHandler_MainActivity.obtainMessage(Utils.TOAST_BLUETOOTH, Utils.BT_CONNECT_FAILED, -1).sendToTarget();
			}
			deleteFile();
		}
		haveConnected = false;
		if (isAutoConnectFlag()) {
			AutoConnect();
		} else {
			cancelAutoConnect();
		}
	}

	@Override
	public void onReceiveData(byte[] dat, int len) {
		mDataParser.addData(dat, len);
	}

}
