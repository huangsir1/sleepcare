package com.sleep.utils;

import java.io.File;

import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.UserHistory;

import android.content.Context;
import android.util.Log;

public class DeleteFileAndUserHistory {
	private final String TAG = this.getClass().getSimpleName();
	
	/**
	 * 本类的对象用于执行文件的删除，为防止阻塞原线程，需开辟新线程进行删除操作
	 */
	public DeleteFileAndUserHistory() {
		Log.i(TAG, "DeleteFileAndUserHistory构造方法被调用！");
	}

	/**
	 * 当测量数据足够得出报告时，若已完成报告和文件的上传，删除本地数据文件
	 * @param mContext 上下文
	 * @param userId 用户的ID
	 * @param historyId 历史记录的ID
	 * @param filePath 数据文件的路径
	 * @return
	 */
	public boolean deleteBySufficientData(Context mContext, int userId, int historyId, String filePath){
		Log.i(TAG, "deleteBySufficientData()");
		PublicDao mPublicDao = new PublicDaoImpl(mContext);
		UserHistory mUserHistory = mPublicDao.findGivenHistory(userId, historyId);
		if (mUserHistory != null) {
			Log.i(TAG,
					"last_uh.getUploadfile() = " + mUserHistory.getUploadfile() + ", last_uh.getHistoryupload() = "
							+ mUserHistory.getHistoryupload() + ", last_uh.getIsFinalReport() = "
							+ mUserHistory.getIsFinalReport());
		}else{
			Log.w(TAG, "该历史记录为空");
		}
		if (mUserHistory != null && mUserHistory.getUploadfile() == 1 && mUserHistory.getHistoryupload() == 1
				&& mUserHistory.getIsFinalReport() == 1) {
			if (filePath != null) {
				File datafile1 = new File(filePath);
				if (datafile1 != null && datafile1.exists()) {
					datafile1.delete();
					Log.i(TAG, "数据文件删除成功-----1, " + filePath);
				}else{
					Log.w(TAG, "数据文件不存在-----1");
				}
				
				String filepath2 = null;
				if(filePath.endsWith(".taiir")){
					filepath2 = filePath.replace(".taiir", ".dat");
				}else if(filePath.endsWith(".dat")){
					filepath2 = filePath.replace(".dat", ".taiir");
				}else{
					Log.w(TAG, "数据文件后缀异常！");
				}
				File datafile2 = new File(filepath2);
				if (datafile2 != null && datafile2.exists()) {
					datafile2.delete();
					Log.i(TAG, "数据文件删除成功-----2, " + filepath2);
				}else{
					Log.w(TAG, "数据文件不存在-----2");
				}
			}
		}else{
			Log.w(TAG, "该历史记录未完成上传");
		}
		return true;
	}
	
	/**
	 * 当测量数据不足以得出报告时，删除本地数据文件及数据库对应的历史记录表
	 * @param mContext 上下文
	 * @param userId 用户的ID
	 * @param historyId 历史记录的ID
	 * @param filePath 数据文件的路径
	 * @return
	 */
	public boolean deleteByInsufficientData(Context mContext, int userId, int historyId, String filePath){
		Log.i(TAG, "deleteByInsufficientData()");
		if(historyId != 0){
			Log.e(TAG, "历史数据被删除！！！");
			PublicDao mPublicDao = new PublicDaoImpl(mContext);
			mPublicDao.deleteSpecificHistory(historyId, userId);
		}
		
		if (filePath != null) {
			File datafile1 = new File(filePath);
			if (datafile1 != null && datafile1.exists()) {
				datafile1.delete();
				Log.i(TAG, "数据文件删除成功-----1, " + filePath);
			}else{
				Log.w(TAG, "数据文件不存在-----1");
			}
			
			String filepath2 = null;
			if(filePath.endsWith(".taiir")){
				filepath2 = filePath.replace(".taiir", ".dat");
			}else if(filePath.endsWith(".dat")){
				filepath2 = filePath.replace(".dat", ".taiir");
			}else{
				Log.w(TAG, "数据文件后缀异常！");
			}
			File datafile2 = new File(filepath2);
			if (datafile2 != null && datafile2.exists()) {
				datafile2.delete();
				Log.i(TAG, "数据文件删除成功-----2, " + filepath2);
			}else{
				Log.w(TAG, "数据文件不存在-----2");
			}
		}else{
			Log.w(TAG, "数据文件路径为空");
		}
		return true;
	}
	
	/**
	 * 当历史记录的数量超过7次时，删除最旧的历史记录
	 * @return
	 */
	public boolean deleteByExceedLimitation(Context mContext, int userId){
		Log.i(TAG, "deleteByExceedLimitation()");
		PublicDao mPublicDao = new PublicDaoImpl(mContext);
		UserHistory mUserHistory = mPublicDao.findMinHistoryIdByUserId(userId);
		String filepath1 = mUserHistory.getFilepaths();
		String pdfpath = mUserHistory.getReportfilepath();
		if (filepath1 != null) {
			File datafile1 = new File(filepath1);
			if (datafile1 != null && datafile1.exists()) {
				datafile1.delete();
				Log.i(TAG, "数据文件删除成功-----1, " + filepath1);
			}else{
				Log.w(TAG, "数据文件不存在-----1");
			}
			
			String filepath2 = null;
			if(filepath1.endsWith(".taiir")){
				filepath2 = filepath1.replace(".taiir", ".dat");
			}else if(filepath1.endsWith(".dat")){
				filepath2 = filepath1.replace(".dat", ".taiir");
			}else{
				Log.w(TAG, "数据文件后缀异常！");
			}
			File datafile2 = new File(filepath2);
			if (datafile2 != null && datafile2.exists()) {
				datafile2.delete();
				Log.i(TAG, "数据文件删除成功-----2, " + filepath2);
			}else{
				Log.w(TAG, "数据文件不存在-----2");
			}
		}else{
			Log.w(TAG, "数据文件路径为空");
		}
		if (pdfpath != null) {
			File pdffile = new File(pdfpath);
			if (pdffile != null && pdffile.exists()) {
				pdffile.delete();
				Log.i(TAG, "PDF文件删除成功");
			}else{
				Log.w(TAG, "PDF文件不存在");
			}
		}else{
			Log.w(TAG, "PDF文件路径为空");
		}
		mPublicDao.deletes(userId);
		Log.e(TAG, "最小历史删除成功");
		return true;
	}
	
	public boolean deleteByUnconnect(String filePath){
		Log.i(TAG, "deleteByUnconnect()");
		
		if (filePath != null) {
			File datafile1 = new File(filePath);
			if (datafile1 != null && datafile1.exists()) {
				datafile1.delete();
				Log.i(TAG, "数据文件删除成功-----1, " + filePath);
			}else{
				Log.w(TAG, "数据文件不存在-----1");
			}
			
			String filepath2 = null;
			if(filePath.endsWith(".taiir")){
				filepath2 = filePath.replace(".taiir", ".dat");
			}else if(filePath.endsWith(".dat")){
				filepath2 = filePath.replace(".dat", ".taiir");
			}else{
				Log.w(TAG, "数据文件后缀异常！");
			}
			File datafile2 = new File(filepath2);
			if (datafile2 != null && datafile2.exists()) {
				datafile2.delete();
				Log.i(TAG, "数据文件删除成功-----2, " + filepath2);
			}else{
				Log.w(TAG, "数据文件不存在-----2");
			}
		}else{
			Log.w(TAG, "数据文件路径为空");
		}
		return true;
	}
}
