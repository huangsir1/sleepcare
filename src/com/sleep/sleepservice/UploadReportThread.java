package com.sleep.sleepservice;

import java.util.List;
import java.util.Locale;

import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.UserHistory;
import com.sleep.local.classs.UserIndex;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserSleep;
import com.sleep.utils.Utils;

import android.content.Context;
import android.util.Log;
import taiyi.web.jason.WebAPI;
import taiyi.web.model.dto.BaseReport;
import taiyi.web.model.dto.Status;

public class UploadReportThread extends Thread {
	private Context context;

	public UploadReportThread(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		try {
			PublicDao pdReport = new PublicDaoImpl(context);
			List<UserHistory> uhListForReport = pdReport.findAllWebUploadUserHistory();
			if (uhListForReport != null) {
				for (UserHistory userHistory : uhListForReport) {
					int id = userHistory.getUserid();
					UserIndex userIndex = userHistory.getUserIndex();
					UserSleep userSleep = userHistory.getUserSleep();
					UserManager manager = pdReport.findAllUserById(id);
					String reportuuid = userHistory.getReportuuid();
					BaseReport baseReport = WebHelper.toBaseReport(reportuuid, userSleep, userIndex, manager);
					Locale locale = context.getResources().getConfiguration().locale;
					Status status = new WebAPI().sendAllReport(baseReport, locale);
					if (status.getCode() >= 2000 && status.getCode() < 3000) {
						userHistory.setHistoryupload(1);
						pdReport.modifyHistorys(userHistory);
						Log.i("TAG", "报告-sucess " + status.getMessage());
					} else {
						Log.i("TAG", "报告-fail " + status.getMessage());
					}
				}
			} else {
				Log.i(Utils.TAG, "没有未上传的报告！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
