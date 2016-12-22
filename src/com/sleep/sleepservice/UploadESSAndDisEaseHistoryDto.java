package com.sleep.sleepservice;

import java.io.IOException;
import java.util.List;

import taiyi.web.jason.WebAPI;
import taiyi.web.model.dto.ESSAndDisEaseHistoryDto;
import taiyi.web.model.dto.Status;

import android.content.Context;
import android.util.Log;

import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.UserManager;
import com.sleep.utils.Utils;

public class UploadESSAndDisEaseHistoryDto extends Thread {
	private Context context;

	public UploadESSAndDisEaseHistoryDto(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		try {
			PublicDao pd = new PublicDaoImpl(context);
			List<DiabetesHy> dh = pd.findAllWebUploadeDiabetesHy();
			if (dh != null) {
				for (DiabetesHy diabetesHy : dh) {
					int id = diabetesHy.getId();
					UserManager um = pd.findAllUserById(id);
					Epworth epworth = pd.findAllEpworthById(id);
					if (um.getUuid() != null) {
						ESSAndDisEaseHistoryDto ud = WebHelper.userESS(um.getUuid(), epworth, diabetesHy);
						Status status = new WebAPI().uploadEssAndDiseaseHistory(ud);
						if (status.getCode() >= 2000 && status.getCode() < 3000) {
							epworth.setUpload(1);
							diabetesHy.setUpload(1);
							pd.modifyDiabetesHy(diabetesHy);
							pd.modifyepworth(epworth);
							Log.i("TAG", "病史-ESS-sucess " + status.getMessage());
						} else {
							Log.i("TAG", "病史-ESS-fail " + status.getMessage());
						}
					}else {
						Log.i("TAG", "病史" + "没有->um.getUuid");
					}
				}
			} else {
				Log.d(Utils.TAG, "没有未上传的病史！");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
