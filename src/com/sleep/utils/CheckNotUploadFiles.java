package com.sleep.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.loopj.android.application.MyApplication;
import com.sleep.analysis.Sleep_ApneaDetection;
import com.sleep.analysis.Sleep_stageSeperate;
import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.UserHistory;
import com.sleep.local.classs.UserIndex;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserNumber;
import com.sleep.local.classs.UserSleep;
import com.sleep.sleepservice.WebHelper;
import com.taiir.sleepcare.home.R;

import android.content.Context;
import android.util.Log;
import taiyi.web.jason.WebAPI;
import taiyi.web.model.dto.BaseReport;
import taiyi.web.model.dto.ESSAndDisEaseHistoryDto;
import taiyi.web.model.dto.Status;

/**
 * 检查未计算的文件、未上传的报告和未上传的文件，并执行上传 2016.7.27
 */
public class CheckNotUploadFiles {
	private final String TAG = this.getClass().getSimpleName();

	public static CheckNotUploadFiles mCheckNotUploadFiles = new CheckNotUploadFiles();
	private Object mObject = new Object(); // 用于线程的等待与唤醒
	private List<UserHistory> list_UserHistory = new ArrayList<UserHistory>();
	private LinkedBlockingQueue<UserHistory> userHistoryQueue = new LinkedBlockingQueue<UserHistory>();
	private int currentUserId = 0; // 用户ID
	private ComputationAndUploadThread mComputationAndUploadThread = null; // 当前正在执行上传的线程
	private Context mContext = null;
	private String actionUrl = WebAPI.DEFAULT_URL + "/api/report/file/upload";

	// private boolean isCurrentThreadRunning = false;

	private CheckNotUploadFiles() {
		Log.i(TAG, "CheckNotUploadFiles构造方法被调用！");
	}

	/**
	 * 获取CheckNotUploadFiles实例
	 * 
	 * @return
	 */
	public static CheckNotUploadFiles getInstance() {
		return mCheckNotUploadFiles;
	}

	/**
	 * 传入上下文，用于获取数据库
	 * 
	 * @param mContext
	 *            上下文
	 */
	private void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * 传入未完成上传操作的历史记录的集合
	 * 
	 * @param list_UserHistory
	 *            历史记录的列表
	 */
	private void setList_UserHistory(List<UserHistory> list_UserHistory) {
		if (list_UserHistory != null) {
			synchronized (this.list_UserHistory) {
				this.list_UserHistory = list_UserHistory;
			}
		}
	}

	/**
	 * 传入用户ID，跳过该用户的上传操作
	 * 
	 * @param userId
	 *            用户ID
	 */
	// @SuppressWarnings("deprecation")
	public void setUserId(int currentUserId) {
		// ******没有设置线程的中断机制，可能会导致点击用户后发生重复上传，但是并不会抛出导致程序崩溃的异常
		this.currentUserId = currentUserId;
		// if(mComputationAndUploadThread != null){
		// if (mComputationAndUploadThread.mUserHistory.getUserid() == userId) {
		// Log.d(TAG, "mComputationAndUploadThread.stop()");
		// mComputationAndUploadThread.stop(); // 会导致线程抛出异常
		// synchronized (mObject) {
		// Log.d(TAG, "-----------------mObject.notify()------------");
		// mObject.notify();
		// }
		// }else{
		// Log.i(TAG, "mComputationAndUploadThread.mUserHistory.getUserid() = "
		// + mComputationAndUploadThread.mUserHistory.getUserid() + ", userId =
		// " + userId);
		// }
		// }else{
		// Log.i(TAG, "mComputationAndUploadThread == null");
		// }
	}

	/**
	 * 开启线程，执行文件的上传（执行该方法的时候会传入UserHistory的列表，上次上传操作传入的UserHistory的列表还没完成上传，
	 * 则该操作会被锁住，因此该方法的调用需开辟一个新线程，防止原线程阻塞）
	 * 
	 * @param mContext
	 *            上下文
	 * @param list_UserHistory
	 *            需要上传的UserHistory列表
	 */
	public void ExecutionThread(final Context mContext, final List<UserHistory> list_UserHistory) {
		Log.i(TAG, "ExecutionThread()被调用，开始上传操作！");
		new Thread() {
			@Override
			public void run() {
				setList_UserHistory(list_UserHistory);
				setmContext(mContext);
				upload();
			}
		}.start();
	}

	/**
	 * 重置上传的变量
	 */
	public void reset() {
		list_UserHistory.clear();
		currentUserId = 0; // 用户ID
		mComputationAndUploadThread = null; // 当前正在执行上传的线程
	}

	/**
	 * 判断网络是否打开，若打开则上传
	 */
	private void upload() {
		boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
		boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());

		if (isConnected || isConnected3g) {
			Log.i(TAG, "网络已打开，开始上传！");
			synchronized (list_UserHistory) {
				PublicDao mPublicDao = new PublicDaoImpl(mContext);
				// 上传所有未上传的用户（可能存在问题，最好只上传该历史记录对应的用户）
				List<UserManager> list_UserManager = mPublicDao.findAllWebUploadUser();
				UserNumber um = mPublicDao.findTelorEmail() == null ? new UserNumber() : mPublicDao.findTelorEmail();
				if (list_UserManager != null && list_UserManager.size() > 0) {
					Log.i(TAG, "有未上传的用户！");
					for (UserManager mUserManager : list_UserManager) {
						Status status;
						try {
							status = new WebAPI().registerUser(WebHelper.userManagerToUser(mUserManager),
									um.getToken());
							if (status.getCode() >= 2000 && status.getCode() < 3000) {
								String userUUID = status.getMessage();
								mUserManager.setUuid(userUUID);
								mUserManager.setUpload(1);
								mPublicDao.modifyusermanager(mUserManager);
								Log.i(TAG, "用户-sucess " + status.getMessage());
							} else {
								System.out.print(status + "\n");
								Log.w(TAG, "用户-fail " + status.getMessage());
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					Log.i(TAG, "没有未上传的用户！");
				}
				// 上传所有未上传的用户的病史（可能存在问题，最好只上传该历史记录对应的用户的病史）
				List<DiabetesHy> list_DiabetesHy = mPublicDao.findAllWebUploadeDiabetesHy();
				if (list_DiabetesHy != null && list_DiabetesHy.size() > 0) {
					Log.i(TAG, "有未上传的病史！");
					for (DiabetesHy mDiabetesHy : list_DiabetesHy) {
						int id = mDiabetesHy.getId();
						UserManager mUserManager = mPublicDao.findAllUserById(id);
						Epworth mEpworth = mPublicDao.findAllEpworthById(id);
						if (mUserManager != null && mUserManager.getUuid() != null) {
							ESSAndDisEaseHistoryDto ud = WebHelper.userESS(mUserManager.getUuid(), mEpworth,
									mDiabetesHy);
							Status status;
							try {
								status = new WebAPI().uploadEssAndDiseaseHistory(ud);
								if (status.getCode() >= 2000 && status.getCode() < 3000) {
									mEpworth.setUpload(1);
									mDiabetesHy.setUpload(1);
									mPublicDao.modifyDiabetesHy(mDiabetesHy);
									mPublicDao.modifyepworth(mEpworth);
									Log.i(TAG, "病史-ESS-sucess " + status.getMessage());
								} else {
									Log.w(TAG, "病史-ESS-fail " + status.getMessage());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							Log.i(TAG, "病史" + "没有->um.getUuid");
						}
					}
				} else {
					Log.i(TAG, "没有未上传的病史！");
				}
				Log.w(TAG, "list_UserHistory.size() = " + list_UserHistory.size());
				if (list_UserHistory.size() > 0) {
					for (UserHistory mUserHistory : list_UserHistory) {
						if (mUserHistory.getUserid() != currentUserId) {
							Log.d(TAG, "测试正在上传的是哪个用户的信息：mUserHistory.getUserid() = " + mUserHistory.getUserid());

							mComputationAndUploadThread = new ComputationAndUploadThread(mUserHistory);
							mComputationAndUploadThread.start();

							synchronized (mObject) {
								try {
									Log.d(TAG, "-----------------mObject.wait()------------");
									mObject.wait();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}else {
							Log.w(TAG, "mUserHistory.getUserid() = " + mUserHistory.getUserid() + ", currentUserId = " + currentUserId);
						}
					}
				} else {
					Log.i(TAG, "没有未上传的历史记录！");
				}

				// 上传完成后，重置上传的变量
				reset();
			}
		} else {
			Log.w(TAG, "网络未打开！");
		}
	}

	/**
	 * 执行单个UserHistory表上传操作的线程
	 */
	private class ComputationAndUploadThread extends Thread {
		private UserHistory mUserHistory = null;
		private int userId = 0;
		private int historyId = 0;
		private String filePath = null;

		public ComputationAndUploadThread(UserHistory mUserHistory) {
			this.mUserHistory = mUserHistory;
			this.userId = mUserHistory.getUserid();
			this.historyId = mUserHistory.getId();
			this.filePath = mUserHistory.getFilepaths();
		}

		@Override
		public void run() {
			PublicDao mPublicDao;

			// try {
			// Log.d(TAG, "Thread.sleep(10000)");
			// Thread.sleep(10000);
			// } catch (InterruptedException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }

			if (mUserHistory.getIsComputed() == 0) { // 数据未计算
				mPublicDao = new PublicDaoImpl(mContext);
				DiabetesHy mDiabetesHy = mPublicDao.findAllDiabetesHyById(userId);
				Epworth mEpworth = mPublicDao.findAllEpworthById(userId);

				Sleep_ApneaDetection mSleep_ApneaDetection = null; // 呼吸指标计算类
				Sleep_stageSeperate mSleep_stageSeperate = null; // 睡眠分期指标计算类
				int dataType = Utils.DATATYPE_BINARY;
				long time_last = Utils.timeCountTotal(filePath, dataType);
				int line_count = Utils.lineCountTotal(filePath, dataType);
				if (time_last / 1000 > 300 && line_count > 300) { // 设定测量最短时间，5分钟
					Log.i(TAG, "数据足够，结果计算中！");
					if (time_last / 1000 > 600 && line_count > 600) { // 如果测量时间大于10分钟，则计算时舍去前5分钟的数据
						mSleep_stageSeperate = new Sleep_stageSeperate();
						mSleep_stageSeperate.calculate(filePath, true, false, Utils.DATA_SEPARATOR, dataType);
						mSleep_ApneaDetection = new Sleep_ApneaDetection();
						mSleep_ApneaDetection.calculate(filePath, true, false, Utils.DATA_SEPARATOR, dataType);
					} else {
						mSleep_stageSeperate = new Sleep_stageSeperate();
						mSleep_stageSeperate.calculate(filePath, false, false, Utils.DATA_SEPARATOR, dataType);
						mSleep_ApneaDetection = new Sleep_ApneaDetection();
						mSleep_ApneaDetection.calculate(filePath, false, false, Utils.DATA_SEPARATOR, dataType);
					}

					/**
					 * 睡眠
					 */
					// 起始时间
					mUserHistory.setHstarthour(mSleep_stageSeperate.getTime_start()[0]);
					mUserHistory.setHstartminute(mSleep_stageSeperate.getTime_start()[1]);
					mUserHistory.setHstarttimes((int) (mSleep_stageSeperate.getTime_start2() / 1000));
					// 结束时间
					mUserHistory.setHendhour(mSleep_stageSeperate.getTime_end()[0]);
					mUserHistory.setHendminute(mSleep_stageSeperate.getTime_end()[1]);
					mUserHistory.setHendtimes((int) (mSleep_stageSeperate.getTime_end2() / 1000));

					// 持续时间
					mUserHistory.setHsmschour(mSleep_stageSeperate.getTime_last()[0]);
					mUserHistory.setHsmscminute(mSleep_stageSeperate.getTime_last()[1]);
					mUserHistory.setHsmsctimes((int) (mSleep_stageSeperate.getTime_last2() / 1000));

					// 浅睡
					mUserHistory.setHqshour(mSleep_stageSeperate.getTime_light()[0]);
					mUserHistory.setHqsminute(mSleep_stageSeperate.getTime_light()[1]);
					mUserHistory.setHqssctimes((int) (mSleep_stageSeperate.getTime_light2() / 1000));

					// 深睡
					mUserHistory.setHsshour(mSleep_stageSeperate.getTime_deep()[0]);
					mUserHistory.setHssminute(mSleep_stageSeperate.getTime_deep()[1]);
					mUserHistory.setHsssctimes((int) (mSleep_stageSeperate.getTime_deep2() / 1000));

					// 清醒
					mUserHistory.setHqxhour(mSleep_stageSeperate.getTime_wake()[0]);
					mUserHistory.setHqxminute(mSleep_stageSeperate.getTime_wake()[1]);
					mUserHistory.setHqxsctimes((int) (mSleep_stageSeperate.getTime_wake2() / 1000));

					/** 脉率 */
					// 最高脉率
					mUserHistory.setHmaxml(String.valueOf(mSleep_stageSeperate.getPr_max()));
					// 发生于 时
					mUserHistory.setHmaxmlhour(mSleep_stageSeperate.getPr_max_timePoint()[0]);
					// 发生于 分
					mUserHistory.setHmaxmlminute(mSleep_stageSeperate.getPr_max_timePoint()[1]);
					// 发生于 秒
					mUserHistory.setHmaxmlsec(mSleep_stageSeperate.getPr_max_timePoint()[2]);
					mUserHistory.setHmaxmltimes((int) (mSleep_stageSeperate.getPr_max_timePoint2() / 1000));
					// 最低脉率
					mUserHistory.setHminml(String.valueOf(mSleep_stageSeperate.getPr_min()));
					// 发生于 时
					mUserHistory.setHminmlhour(mSleep_stageSeperate.getPr_min_timePoint()[0]);
					// 发生于 分
					mUserHistory.setHminmlminute(mSleep_stageSeperate.getPr_min_timePoint()[1]);
					// 发生于 秒
					mUserHistory.setHminmlsec(mSleep_stageSeperate.getPr_min_timePoint()[2]);
					mUserHistory.setHminmltimes((int) (mSleep_stageSeperate.getPr_min_timePoint2() / 1000));
					// 平均脉率
					mUserHistory.setHavgml(String.valueOf(Utils.formFloat(mSleep_stageSeperate.getPr_average(), 2)));

					// 睡眠评分
					int qs = mSleep_stageSeperate.getTime_light()[0] * 60 + mSleep_stageSeperate.getTime_light()[1];
					int ss = mSleep_stageSeperate.getTime_deep()[0] * 60 + mSleep_stageSeperate.getTime_deep()[1];
					int qx = mSleep_stageSeperate.getTime_wake()[0] * 60 + mSleep_stageSeperate.getTime_wake()[1];
					int ss_rate = ss * 100 / (qs + ss + qx);
					if (ss_rate > 35) {
						mUserHistory.setHsleepscore(95 + (int) (Math.random() * 5));
					} else if (ss_rate > 25) {
						mUserHistory.setHsleepscore(90 + (int) (Math.random() * 5));
					} else if (ss_rate > 20) {
						mUserHistory.setHsleepscore(85 + (int) (Math.random() * 5));
					} else if (ss_rate > 15) {
						mUserHistory.setHsleepscore(80 + (int) (Math.random() * 5));
					} else if (ss_rate > 10) {
						mUserHistory.setHsleepscore(70 + (int) (Math.random() * 10));
					} else if (ss_rate > 5) {
						mUserHistory.setHsleepscore(60 + (int) (Math.random() * 10));
					} else {
						mUserHistory.setHsleepscore(50 + (int) (Math.random() * 10));
					}
					/**
					 * 指标
					 */
					// AHI
					mUserHistory.setHahiIndex(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getAHI(), 2)));
					// 呼吸暂停次数
					mUserHistory.setHhxztIndex(String.valueOf(mSleep_ApneaDetection.getAI_count()));
					// 低通气次数
					mUserHistory.setHdtqIndex(String.valueOf(mSleep_ApneaDetection.getHI_count()));
					// 氧减次数
					mUserHistory.setHyjzcsIndex(String.valueOf(mSleep_ApneaDetection.getODI4_count()));
					// 平均血氧饱和度
					mUserHistory.setHpjxybhdIndex(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getMSPO2(), 2)));
					// 最低血氧饱和度
					mUserHistory.setHzdxybhdIndex(String.valueOf(mSleep_ApneaDetection.getLSPO2()));
					// 血氧低于90%占比
					mUserHistory.setHxybhdzbIndex(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getTS90(), 2)));
					/** 氧降 */
					// 最大氧降 持续时
					mUserHistory.setHmaxyjcxhour(mSleep_ApneaDetection.getODI4_largest_duration()[0]);
					// 最大氧降 持续分
					mUserHistory.setHmaxyjcxminute(mSleep_ApneaDetection.getODI4_largest_duration()[1]);
					// 最大氧降 持续秒
					mUserHistory.setHmaxyjcxsec(mSleep_ApneaDetection.getODI4_largest_duration()[2]);
					mUserHistory.setHmaxyjcxhour((int) (mSleep_ApneaDetection.getODI4_largest_duration2() / 1000));
					// 最大氧降 发生时
					mUserHistory.setHmaxyjfshour(mSleep_ApneaDetection.getODI4_largest_timePoint()[0]);
					// 最大氧降 发生分
					mUserHistory.setHmaxyjfsminute(mSleep_ApneaDetection.getODI4_largest_timePoint()[1]);
					// 最大氧降 发生秒
					mUserHistory.setHmaxyjfssec(mSleep_ApneaDetection.getODI4_largest_timePoint()[2]);
					mUserHistory.setHmaxyjfstimes((int) (mSleep_ApneaDetection.getODI4_largest_timePoint2() / 1000));
					// 最长氧降 持续时
					mUserHistory.setHlongyjcxhour(mSleep_ApneaDetection.getODI4_longest_duration()[0]);
					// 最长氧降 持续分
					mUserHistory.setHlongyjcxminute(mSleep_ApneaDetection.getODI4_longest_duration()[1]);
					// 最长氧降 持续秒
					mUserHistory.setHlongyjcxsec(mSleep_ApneaDetection.getODI4_longest_duration()[2]);
					mUserHistory.setHlongyjcxtimes((int) (mSleep_ApneaDetection.getODI4_longest_duration2() / 1000));
					// 最长氧降 发生时
					mUserHistory.setHlongyjfshour(mSleep_ApneaDetection.getODI4_longest_timePoint()[0]);
					// 最长氧降 发生分
					mUserHistory.setHlongyjfsminute(mSleep_ApneaDetection.getODI4_longest_timePoint()[1]);
					// 最长氧降 发生秒
					mUserHistory.setHlongyjfssec(mSleep_ApneaDetection.getODI4_longest_timePoint()[2]);
					mUserHistory.setHlongyjfstimes((int) (mSleep_ApneaDetection.getODI4_longest_timePoint2() / 1000));
					// 氧减危害指数
					mUserHistory.setHxywhzsIndex(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getODHI(), 2)));
					// 平均血流灌注度
					mUserHistory.setHavgxlgzd(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getPI(), 2)));
					// 评分
					int score;
					if (mSleep_ApneaDetection.getAHI() % 1 < 0.5) {
						score = 100 - (int) mSleep_ApneaDetection.getAHI();
					} else {
						score = 99 - (int) mSleep_ApneaDetection.getAHI();
					}
					if (score < 0)
						score = 0;
					mUserHistory.setHscoreHxzb(score);
					// 最长呼吸暂停时长
					mUserHistory.setHzchxzthour(mSleep_ApneaDetection.getAI_longest_duration()[0]);
					mUserHistory.setHzchxztminute(mSleep_ApneaDetection.getAI_longest_duration()[1]);
					mUserHistory.setHzchxztsec(mSleep_ApneaDetection.getAI_longest_duration()[2]);
					mUserHistory.setHzchxzttimes((int) (mSleep_ApneaDetection.getAI_longest_duration2() / 1000));
					// 发生于
					mUserHistory.setHhxhapperhour(mSleep_ApneaDetection.getAI_longest_timePoint()[0]);
					mUserHistory.setHhxhapperminute(mSleep_ApneaDetection.getAI_longest_timePoint()[1]);
					mUserHistory.setHhxhappersec(mSleep_ApneaDetection.getAI_longest_timePoint()[2]);
					mUserHistory.setHhxhappertimes((int) (mSleep_ApneaDetection.getAI_longest_timePoint2() / 1000));
					// 总时间
					mUserHistory.setHhxztzsjhour(mSleep_ApneaDetection.getAI_time_sum()[0]);
					mUserHistory.setHhxztzsjminute(mSleep_ApneaDetection.getAI_time_sum()[1]);
					mUserHistory.setHhxztzsjsec(mSleep_ApneaDetection.getAI_time_sum()[2]);
					mUserHistory.setHhxztzsjtimes((int) (mSleep_ApneaDetection.getAI_time_sum2() / 1000));
					// 最长低通气时长
					mUserHistory.setHzcdtqhour(mSleep_ApneaDetection.getHI_longest_duration()[0]);
					mUserHistory.setHzcdtqminute(mSleep_ApneaDetection.getHI_longest_duration()[1]);
					mUserHistory.setHzcdtqsec(mSleep_ApneaDetection.getHI_longest_duration()[2]);
					mUserHistory.setHzcdtqtimes((int) (mSleep_ApneaDetection.getHI_longest_duration2() / 1000));
					// 发生于
					mUserHistory.setHdtqhapperhour(mSleep_ApneaDetection.getHI_longest_timePoint()[0]);
					mUserHistory.setHdtqhapperminute(mSleep_ApneaDetection.getHI_longest_timePoint()[1]);
					mUserHistory.setHdtqhappersec(mSleep_ApneaDetection.getHI_longest_timePoint()[2]);
					mUserHistory.setHdtqhappertimes((int) (mSleep_ApneaDetection.getHI_longest_timePoint2() / 1000));
					// 总时间
					mUserHistory.setHdtqzsjhour(mSleep_ApneaDetection.getHI_time_sum()[0]);
					mUserHistory.setHdtqzsjminute(mSleep_ApneaDetection.getHI_time_sum()[1]);
					mUserHistory.setHdtqzsjsec(mSleep_ApneaDetection.getHI_time_sum()[2]);
					mUserHistory.setHdtqzsjtimes((int) (mSleep_ApneaDetection.getHI_time_sum2() / 1000));
					// ODI
					mUserHistory.setHxyodi(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getODI4_index(), 2)));
					// 醒时
					// 最高
					mUserHistory.setHsmmax(String.valueOf(mSleep_ApneaDetection.getHSPO2()));

					// 90-100总时间
					mUserHistory.setHaxyfbzsjhour(mSleep_ApneaDetection.getDistribution_90_100()[0]);
					mUserHistory.setHaxyfbzsjminute(mSleep_ApneaDetection.getDistribution_90_100()[1]);
					mUserHistory.setHaxyfbzsjsec(mSleep_ApneaDetection.getDistribution_90_100()[2]);
					mUserHistory.setHaxyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_90_1002()[0] / 1000);
					// 90-100氧减总次数
					mUserHistory.setHayjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_90_100()[3]));
					// 80-90
					mUserHistory.setHbxyfbzsjhour(mSleep_ApneaDetection.getDistribution_80_90()[0]);
					mUserHistory.setHbxyfbzsjminute(mSleep_ApneaDetection.getDistribution_80_90()[1]);
					mUserHistory.setHbxyfbzsjsec(mSleep_ApneaDetection.getDistribution_80_90()[2]);
					mUserHistory.setHbxyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_80_902()[0] / 1000);
					mUserHistory.setHbyjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_80_90()[3]));
					// 70-80
					mUserHistory.setHcxyfbzsjhour(mSleep_ApneaDetection.getDistribution_70_80()[0]);
					mUserHistory.setHcxyfbzsjminute(mSleep_ApneaDetection.getDistribution_70_80()[1]);
					mUserHistory.setHcxyfbzsjsec(mSleep_ApneaDetection.getDistribution_70_80()[2]);
					mUserHistory.setHcxyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_70_802()[0] / 1000);
					mUserHistory.setHcyjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_70_80()[3]));
					// 60-70
					mUserHistory.setHdxyfbzsjhour(mSleep_ApneaDetection.getDistribution_60_70()[0]);
					mUserHistory.setHdxyfbzsjminute(mSleep_ApneaDetection.getDistribution_60_70()[1]);
					mUserHistory.setHdxyfbzsjsec(mSleep_ApneaDetection.getDistribution_60_70()[2]);
					mUserHistory.setHdxyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_60_702()[0] / 1000);
					mUserHistory.setHdyjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_60_70()[3]));
					// 50-60
					mUserHistory.setHexyfbzsjhour(mSleep_ApneaDetection.getDistribution_50_60()[0]);
					mUserHistory.setHexyfbzsjminute(mSleep_ApneaDetection.getDistribution_50_60()[1]);
					mUserHistory.setHexyfbzsjsec(mSleep_ApneaDetection.getDistribution_50_60()[2]);
					mUserHistory.setHexyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_50_602()[0] / 1000);
					mUserHistory.setHeyjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_50_60()[3]));
					// 0-50
					mUserHistory.setHfxyfbzsjhour(mSleep_ApneaDetection.getDistribution_0_50()[0]);
					mUserHistory.setHfxyfbzsjminute(mSleep_ApneaDetection.getDistribution_0_50()[1]);
					mUserHistory.setHfxyfbzsjsec(mSleep_ApneaDetection.getDistribution_0_50()[2]);
					mUserHistory.setHfxyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_0_502()[0] / 1000);
					mUserHistory.setHfyjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_0_50()[3]));

					int essscore = mEpworth.getSumscore();
					float AHI = mSleep_ApneaDetection.getAHI();
					int lspo2 = mSleep_ApneaDetection.getLSPO2();
					boolean flag_osahs = false;
					if (mContext.getString(R.string.insomnia).equals(mDiabetesHy.getLosesleep())
							|| mContext.getString(R.string.hypertension).equals(mDiabetesHy.getHypertension())
							|| mContext.getString(R.string.coronary_heart_disease)
									.equals(mDiabetesHy.getCoronaryheartdisease())
							|| mContext.getString(R.string.cerebral_vascular_disease)
									.equals(mDiabetesHy.getCerebrovasculardisease())
							|| mContext.getString(R.string.diabetes).equals(mDiabetesHy.getDiabetes())) {
						flag_osahs = true;
					}
					// 判断是否患OSAHS
					if ((essscore >= 9 && AHI > 5) || (essscore < 9 && AHI >= 10) || (AHI > 5 && flag_osahs)) {
						mUserHistory.setHudegree(mContext.getString(R.string.osahs_text));
					} else {
						mUserHistory.setHudegree(null);
					}
					// OSAHS
					if (mContext.getString(R.string.osahs_text).equals(mUserHistory.getHudegree())) {
						if (AHI <= 5 && lspo2 >= 90)
							mUserHistory.setHudegree(mContext.getString(R.string.no_input));
						if (AHI > 30) {
							mUserHistory.setHudegree(mContext.getString(R.string.severe_osahs));
						} else if (AHI > 15) {
							mUserHistory.setHudegree(mContext.getString(R.string.moderate_osahs));
						} else if (AHI > 5) {
							mUserHistory.setHudegree(mContext.getString(R.string.light_osahs));
						}
					}
					if (mUserHistory.getHudegree() != null) {
						if (lspo2 < 80) {
							mUserHistory.setHudegree(
									mUserHistory.getHudegree() + mContext.getString(R.string.and_severe_hypoxia));
						} else if (lspo2 < 85) {
							mUserHistory.setHudegree(
									mUserHistory.getHudegree() + mContext.getString(R.string.and_moderate_hypoxia));
						} else if (lspo2 < 90) {
							mUserHistory.setHudegree(
									mUserHistory.getHudegree() + mContext.getString(R.string.and_mild_hypoxia));
						}
					} else {
						if (lspo2 < 80) {
							mUserHistory.setHudegree(mContext.getString(R.string.severe_hypoxia));
						} else if (lspo2 < 85) {
							mUserHistory.setHudegree(mContext.getString(R.string.moderate_hypoxia));
						} else if (lspo2 < 90) {
							mUserHistory.setHudegree(mContext.getString(R.string.mild_hypoxia));
						}
					}
					// OSAHS程度
					if (AHI > 30) {
						mUserHistory.setHosahsdegree(mContext.getString(R.string.severe));
					} else if (AHI > 15) {
						mUserHistory.setHosahsdegree(mContext.getString(R.string.moderate));
					} else if (AHI > 5) {
						mUserHistory.setHosahsdegree(mContext.getString(R.string.light));
					} else {
						mUserHistory.setHosahsdegree(mContext.getString(R.string.no_input));
					}
					// 低血氧症程度
					if (lspo2 < 80) {
						mUserHistory.setHdyxzdegree(mContext.getString(R.string.severe));
					} else if (lspo2 < 85) {
						mUserHistory.setHdyxzdegree(mContext.getString(R.string.moderate));
					} else if (lspo2 < 90) {
						mUserHistory.setHdyxzdegree(mContext.getString(R.string.light));
					} else {
						mUserHistory.setHdyxzdegree(mContext.getString(R.string.no_input));
					}

					// ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！

					// 监测记录的开始时间
					mUserHistory.setTestdate(mUserHistory.getHstarttimes());

					String filepath = mUserHistory.getFilepaths();
					try {
						GZipUtils.compress(filepath, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
					final String fileurl = filepath.replace(".dat", ".taiir");

					// 保存数据文件的路径
					mUserHistory.setFilepaths(fileurl);

					String reportfilepath = mUserHistory.getReportfilepath();
					if (reportfilepath != null) {
						File pdffile = new File(reportfilepath);
						if (pdffile != null && pdffile.exists()) {
							pdffile.delete();
							Log.i("TAG", "PDF文件删除成功");
						}
					}
					mUserHistory.setReportfilepath(null);

					mUserHistory.setHistoryupload(0);
					mUserHistory.setUploadfile(0);
					mUserHistory.setIsFinalReport(1);
					mUserHistory.setIsComputed(1);

					mPublicDao.modifyHistorys(mUserHistory);

					// 历史数据个数上限为7
					mPublicDao = new PublicDaoImpl(mContext);
					List<UserHistory> ListUserHistory = mPublicDao.findAllHistoryByUserId(userId);
					Log.i(TAG, "ListUserHistory.size() = " + ListUserHistory.size());
					if (ListUserHistory.size() > 7) {
						if (userId != currentUserId || ListUserHistory.size() > 8) {
							new DeleteFileAndUserHistory().deleteByExceedLimitation(mContext, userId);
						} else {
							Log.w(TAG, "userId == currentUserId");
						}
					}
					uploadReport(mContext, userId, historyId);
					uploadDataFile(mContext, userId, historyId);
					new DeleteFileAndUserHistory().deleteBySufficientData(mContext, userId, historyId, filePath);
				} else {
					Log.w(TAG, "数据不足，将相应的UserHistory表及文件删除！");
					new DeleteFileAndUserHistory().deleteByInsufficientData(mContext, userId, historyId, filePath);
				}
			} else {
				if (mUserHistory.getHistoryupload() == 0) {
					uploadReport(mContext, userId, historyId);
				} else {
					Log.i(TAG, "mUserHistory.getHistoryupload() = " + mUserHistory.getHistoryupload());
				}
				if (mUserHistory.getUploadfile() == 0) {
					uploadDataFile(mContext, userId, historyId);
				} else {
					Log.i(TAG, "mUserHistory.getUploadfile() = " + mUserHistory.getUploadfile());
				}
				new DeleteFileAndUserHistory().deleteBySufficientData(mContext, userId, historyId, filePath);
			}

			synchronized (mObject) {
				Log.d(TAG, "-----------------mObject.notify()------------");
				mObject.notify();
			}
		}
	}

	/**
	 * 上传报告
	 * 
	 * @param mContext
	 *            上下文
	 * @param userId
	 *            用户的ID
	 * @param historyId
	 *            历史记录的ID
	 * @return
	 */
	private boolean uploadReport(Context mContext, int userId, int historyId) {
		Log.i(TAG, "报告上传中！");
		try {
			PublicDao mPublicDao = new PublicDaoImpl(mContext);
			UserHistory mUserHistory = mPublicDao.findGivenHistory(userId, historyId);

			UserIndex mUserIndex = mUserHistory.getUserIndex();
			UserSleep mUserSleep = mUserHistory.getUserSleep();
			UserManager mUserManager = mPublicDao.findAllUserById(userId);
			String reportuuid = mUserHistory.getReportuuid();
			BaseReport baseReport = WebHelper.toBaseReport(reportuuid, mUserSleep, mUserIndex, mUserManager);
			Locale locale = mContext.getResources().getConfiguration().locale;
			Status status = new WebAPI().sendAllReport(baseReport, locale);
			if (status.getCode() >= 2000 && status.getCode() < 3000) {
				mUserHistory.setHistoryupload(1);
				mPublicDao.modifyHistorys(mUserHistory);
				Log.i(TAG, "报告-sucess " + status.getMessage());
			} else {
				Log.w(TAG, "报告-fail " + status.getMessage());
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 上传数据文件
	 * 
	 * @param mContext
	 *            上下文
	 * @param userId
	 *            用户的ID
	 * @param historyId
	 *            历史记录的ID
	 * @return
	 */
	private boolean uploadDataFile(Context mContext, int userId, int historyId) {
		Log.i(TAG, "数据文件上传中！");
		PublicDao mPublicDao = new PublicDaoImpl(mContext);
		UserHistory mUserHistory = mPublicDao.findGivenHistory(userId, historyId);

		Map<String, File> file = new HashMap<String, File>();
		Map<String, String> reportId = new HashMap<String, String>();
		String reportUuid = mUserHistory.getReportuuid();
		String filePath = mUserHistory.getFilepaths();
		Log.i(TAG, "uploadDataFile()" + filePath);
		String retStr = "";
		reportId.put("reportId", reportUuid);
		if (filePath != null) {
			file.put("file", new File(filePath));
		} else {
			Log.w(TAG, "上传文件失败，文件不存在！");
			return false;
		}
		try {
			Locale locale = MyApplication.getContext().getResources().getConfiguration().locale;
			retStr = JsonUtils.post(actionUrl, reportId, file, locale);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (retStr != null && retStr.contains("文件上传成功")) {
			mUserHistory.setUploadfile(1);
			mPublicDao.modifyHistorys(mUserHistory);
			Log.i(TAG, "文件上传成功，retStr = " + retStr);
		} else {
			Log.w(TAG, "文件上传失败，retStr " + retStr);
			return false;
		}
		return true;
	}

	/**
	 * 将list_UserHistory中的元素依次加入队尾
	 * 
	 * @param list_UserHistory
	 */
	public void addUserHistory(List<UserHistory> list_UserHistory) {
		for (UserHistory mUserHistory : list_UserHistory) {
			if (!userHistoryQueue.contains(mUserHistory)) {
				try {
					userHistoryQueue.put(mUserHistory);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从队列中取出队首的UserHistory，但不移除该UserHistory
	 * 
	 * @return
	 */
	private UserHistory peekUserHistory() {
		return userHistoryQueue.peek();
	}

	/**
	 * 从队列中移除UserHistory
	 * 
	 * @return
	 */
	private UserHistory removeUserHistory() {
		userHistoryQueue.remove();
		userHistoryQueue.remove(list_UserHistory);
		userHistoryQueue.add(null);
		userHistoryQueue.isEmpty();
		userHistoryQueue.size();
		// userHistoryQueue.take();
		return userHistoryQueue.poll();
	}
}
