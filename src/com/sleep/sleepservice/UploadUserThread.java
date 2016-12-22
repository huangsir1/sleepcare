package com.sleep.sleepservice;

import java.util.List;

import taiyi.web.jason.WebAPI;
import taiyi.web.model.dto.Status;
import android.content.Context;
import android.util.Log;

import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserNumber;
import com.sleep.utils.Utils;

public class UploadUserThread extends Thread {
	private Context context;

	public UploadUserThread(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void run() {
		try {
			PublicDao pd = new PublicDaoImpl(context);
			List<UserManager> users = pd.findAllWebUploadUser();
			UserNumber findTelorEmail = pd.findTelorEmail();
			if (users != null) {
				for (UserManager user : users) {
					String token = findTelorEmail == null ? null : findTelorEmail.getToken();
					Status status = new WebAPI().registerUser(WebHelper.userManagerToUser(user), token);
					if (status.getCode() >= 2000 && status.getCode() < 3000) {
						String userUUID = status.getMessage();
						user.setUuid(userUUID);
						user.setUpload(1);
						pd.modifyusermanager(user);
						Log.i("TAG", "用户-sucess " + status.getMessage());
					} else {
						System.out.print(status + "\n");
						Log.i("TAG", "用户-fail " + status.getMessage());
					}
				}
			} else {
				Log.d(Utils.TAG, "没有未上传的用户！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
