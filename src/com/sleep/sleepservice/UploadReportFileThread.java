package com.sleep.sleepservice;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.UserHistory;
import com.sleep.utils.JsonUtils;
import com.sleep.utils.Utils;

import android.content.Context;
import android.util.Log;
import taiyi.web.jason.WebAPI;

public class UploadReportFileThread extends Thread {
	private String actionUrl = WebAPI.DEFAULT_URL + "/api/report/file/upload";
	private Context context;

	public UploadReportFileThread(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		PublicDao pdFile = new PublicDaoImpl(context);
		List<UserHistory> uhListForfile = pdFile.findAllUnuploadFile();
		if (uhListForfile != null) {
			for (UserHistory userHistory : uhListForfile) {
				Map<String, File> file = new HashMap<String, File>();
		
				Map<String, String> reportId = new HashMap<String, String>();
				String reportUuid = userHistory.getReportuuid();
				Log.i(Utils.TAG, "reportUuid = " + reportUuid);
				String filePath = userHistory.getFilepaths();
				Log.i(Utils.TAG, "filePath = " + filePath);
				String retStr = "";
				reportId.put("reportId", reportUuid);
				if (filePath != null) {
					file.put("file", new File(filePath));
				} else {
					Log.i("TAG", "上传文件失败，文件不存在！");
				}
				try {
				
//					retStr = JsonUtils.post(actionUrl, reportId, file,locale);
					Locale locale = context.getResources().getConfiguration().locale;
					retStr = JsonUtils.post(actionUrl, reportId, file, locale);
//					Locale locale = context.getResources().getConfiguration().locale;
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (retStr != null && retStr.contains("文件上传成功")) {
					userHistory.setUploadfile(1);
					pdFile.modifyHistorys(userHistory);
					Log.i("retStr", "文件上传成功，retStr = " + retStr);
				} else {
					Log.i("retStr", "文件上传失败，retStr " + retStr);
				}
			}
		} else {
			Log.i(Utils.TAG, "没有未上传的文件！");
		}
	}
}
