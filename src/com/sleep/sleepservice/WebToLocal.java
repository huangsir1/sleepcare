package com.sleep.sleepservice;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import android.annotation.SuppressLint;
import com.loopj.android.application.MyApplication;
import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.UserManager;
import com.taiir.sleepcare.home.R;

import taiyi.web.model.EssUser;
import taiyi.web.model.dto.DiseaseHistoryDto;
import taiyi.web.model.dto.EssDto;
import taiyi.web.model.dto.UserEssAndDHDto;

public class WebToLocal {
	@SuppressLint("SimpleDateFormat")
	public static UserManager webtousermanager(UserEssAndDHDto userByToken) {
		UserManager um = new UserManager();
		um.setUsername(userByToken.getName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date birthday = userByToken.getBirthday();
		um.setUserage(sdf.format(birthday));
		um.setMobilenumber(userByToken.getPhone());
		um.setUsersex(userByToken.getGender());
		um.setUserweight(userByToken.getWeight().intValue());
		um.setUserheight(userByToken.getHeight());
		um.setUuid(userByToken.getId());

		return um;

	}

	public static DiabetesHy webtouserMedicalhistory(UserEssAndDHDto userByToken) {
		DiabetesHy dh = new DiabetesHy();
		DiseaseHistoryDto diseaseHistoryDto = userByToken.getDiseaseHistories();
		Integer[] diseaseHistoryIds = diseaseHistoryDto.getDiseaseHistoryIds();
		HashSet<Integer> diseaseHistoryIdSet = new HashSet<Integer>(Arrays.asList(diseaseHistoryIds));
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.INSOMNIA)) {
			dh.setLosesleep(MyApplication.getContext().getString(R.string.insomnia));
		} else {
			dh.setLosesleep(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.DIABETES)) {
			dh.setDiabetes(MyApplication.getContext().getString(R.string.diabetes));
		} else {
			dh.setDiabetes(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.HYPERTENSION)) {
			dh.setHypertension(MyApplication.getContext().getString(R.string.hypertension));
		} else {
			dh.setHypertension(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.CORONARY_HEART_DISEASE)) {
			dh.setCoronaryheartdisease(MyApplication.getContext().getString(R.string.coronary_heart_disease));
		} else {
			dh.setCoronaryheartdisease(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.HEART_FAILURE)) {
			dh.setHeartfailure(MyApplication.getContext().getString(R.string.heart_failure));
		} else {
			dh.setHeartfailure(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.ARRHYTHMIA)) {
			dh.setArrhythmia(MyApplication.getContext().getString(R.string.arrhythmia));
		} else {
			dh.setArrhythmia(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.NASAL_OBSTRUCTION)) {
			dh.setCongestion(MyApplication.getContext().getString(R.string.nasal_obstruction));
		} else {
			dh.setCongestion(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.LONG_TERM_SMOKING)) {
			dh.setLongsmoking(MyApplication.getContext().getString(R.string.long_term_smoking));
		} else {
			dh.setLongsmoking(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.CEREBRAL_VASCULAR_DISEASE)) {
			dh.setCerebrovasculardisease(MyApplication.getContext().getString(R.string.cerebral_vascular_disease));
		} else {
			dh.setCerebrovasculardisease(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.RENAL_FUNCTION_DAMAGE)) {
			dh.setRenalfailure(MyApplication.getContext().getString(R.string.renal_function_damage));
		} else {
			dh.setRenalfailure(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.TO_USE_A_SEDATIVE_OR_DRUG)) {
			dh.setTakesedatives(MyApplication.getContext().getString(R.string.take_a_sedative));
		} else {
			dh.setTakesedatives(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.LONG_TERM_HEAVY_DRINKING)) {
			dh.setLongdrinking(MyApplication.getContext().getString(R.string.long_term_heavy_drinking));
		} else {
			dh.setLongdrinking(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.FAMILY_HISTORY_OF_OSAHS)) {
			dh.setWhetherfmhy(MyApplication.getContext().getString(R.string.a_family_history_of_osahs));
		} else {
			dh.setWhetherfmhy(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.BULKY_UVULA)) {
			dh.setWhetherxyccd(MyApplication.getContext().getString(R.string.bulky_uvula));
		} else {
			dh.setWhetherxyccd(null);
		}
		if (diseaseHistoryIdSet.contains(DiseaseHistoryDto.IS_MENOPAUSE)) {
			dh.setWhetherjjm(MyApplication.getContext().getString(R.string.menopause));
		} else {
			dh.setWhetherjjm(null);
		}
		dh.setUuid(userByToken.getId());
		return dh;

	}

	public static Epworth webtoEpworth(UserEssAndDHDto userByToken) {
		List<EssUser> ess = userByToken.getEss();
		Epworth ep = new Epworth();
		int sum = 0;
		for (EssUser essUser : ess) {
			Integer rank = essUser.getRank();
			sum += rank;
			switch (essUser.getEssId()) {
			case EssDto.SITTING_READING:
				/**
				 * 坐着阅读时
				 */
				ep.setSatreading(rank);
				break;
			/**
			 * 看电视时
			 */
			case EssDto.WATCHING_TV:
				ep.setWatchtv(essUser.getRank());
				break;
			/**
			 * 在公共场所坐着不动时( 如在剧场或开会)
			 */
			case EssDto.WHEN_SITTING_IN_A_PUBLIC_PLACE:
				ep.setSatnotmove(essUser.getRank());
				break;
			/**
			 * 长时间坐车时中间不休息( 超过 ! )
			 */
			case EssDto.LONG_RIDE_WITHOUT_A_BREAK:
				ep.setLongnotrest(essUser.getRank());
				break;
			/**
			 * 坐着与人谈话时
			 */
			case EssDto.WHEN_PEOPLE_SIT_AND_TALK:
				ep.setSatconversation(essUser.getRank());
				break;
			/**
			 * 饭后休息时( 未饮酒时)
			 */
			case EssDto.WHEN_RESTING_AFTER_A_MEAL:
				ep.setAfterdinnerrest(essUser.getRank());
				break;
			/**
			 * 开车等红绿灯时
			 */
			case EssDto.DRIVING_WHILE_WAITING_FOR_TRAFFIC_LIGHTS:
				ep.setTrafficlights(essUser.getRank());
				break;
			/**
			 * 下午静卧休息时
			 */
			case EssDto.PM_SUPINE_REST:
				ep.setJingworest(essUser.getRank());
				break;

			default:
				break;
			}
		}
		ep.setSumscore(sum);
		ep.setUuid(userByToken.getId());
		return ep;
	}
}
