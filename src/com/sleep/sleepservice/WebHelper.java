package com.sleep.sleepservice;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.UserIndex;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserSleep;
import com.sleep.overriding_methods.Version;

import android.annotation.SuppressLint;
import taiyi.web.model.BreatheReport;
import taiyi.web.model.SleepReport;
import taiyi.web.model.SubReport;
import taiyi.web.model.User;
import taiyi.web.model.dto.BaseReport;
import taiyi.web.model.dto.ESSAndDisEaseHistoryDto;

public class WebHelper {

	/**
	 * 用户
	 */
	@SuppressLint("SimpleDateFormat")
	public static User userManagerToUser(UserManager usermanager) {
		User user = new User();
		if (usermanager.getUuid() != null) {
			user.setId(usermanager.getUuid());
		}
		user.setName(usermanager.getUsername());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dstr = usermanager.getUserage();
		try {
			user.setBirthday(sdf.parse(dstr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		user.setPhone(usermanager.getMobilenumber());
		String gender = usermanager.getUsersex().equals("男") ? "男"
				: usermanager.getUsersex().equalsIgnoreCase("male") ? "男" : "女";
		user.setGender(gender);
		user.setWeight((double) usermanager.getUserweight());
		user.setHeight(usermanager.getUserheight());
		// 杜恩版(需添加)
		// user.setDoctorId("FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF");
		user.setAddress(usermanager.getUseremail());
		return user;
	}

	public static ESSAndDisEaseHistoryDto userESS(String userid, Epworth eh, DiabetesHy dh) {
		ArrayList<Integer> ints = new ArrayList<Integer>(15);
		if (!isEmpty(dh.getLosesleep())) {
			ints.add(1);
		}
		if (!isEmpty(dh.getDiabetes())) {
			ints.add(2);
		}
		if (!isEmpty(dh.getHypertension())) {
			ints.add(3);
		}
		if (!isEmpty(dh.getCoronaryheartdisease())) {
			ints.add(4);
		}
		if (!isEmpty(dh.getHeartfailure())) {
			ints.add(5);
		}
		if (!isEmpty(dh.getArrhythmia())) {
			ints.add(6);
		}
		if (!isEmpty(dh.getCongestion())) {
			ints.add(7);
		}
		if (!isEmpty(dh.getLongsmoking())) {
			ints.add(8);
		}
		if (!isEmpty(dh.getWhetherxyccd())) {
			ints.add(9);
		}
		if (!isEmpty(dh.getWhetherfmhy())) {
			ints.add(10);
		}
		if (!isEmpty(dh.getCerebrovasculardisease())) {
			ints.add(11);
		}
		if (!isEmpty(dh.getRenalfailure())) {
			ints.add(12);
		}
		if (!isEmpty(dh.getTakesedatives())) {
			ints.add(13);
		}
		if (!isEmpty(dh.getLongdrinking())) {
			ints.add(14);
		}
		if (!isEmpty(dh.getWhetherjjm())) {
			ints.add(15);
		}
		Integer[] array = ints.toArray(new Integer[0]);
		ESSAndDisEaseHistoryDto essadehd = new ESSAndDisEaseHistoryDto(userid, eh.getSatreading(), eh.getWatchtv(),
				eh.getSatnotmove(), eh.getLongnotrest(), eh.getSatconversation(), eh.getAfterdinnerrest(),
				eh.getTrafficlights(), eh.getJingworest(), array);
		return essadehd;
	}

	/**
	 * 睡眠
	 */
	public static SleepReport userSleepToSleepReport(UserSleep userSleep, UserManager manager) {
		SleepReport sr = new SleepReport();
		String uuid = manager.getUuid();
		if (userSleep.getAvgml() != null) {
			sr.setUserId(uuid);
			// 开始睡眠时间
			if (userSleep.getStarttimes() != 0) {
				long st = (long) userSleep.getStarttimes() * 1000;
				Date d1 = new Date(st);
				sr.setStartTime(d1);
			} else {
				sr.setStartTime(null);
			}
			// sr.setStartTime(new Date());
			// 结束睡眠时间
			if (userSleep.getEndtimes() != 0) {
				long et = (long) userSleep.getEndtimes() * 1000;
				Date d2 = new Date(et);
				sr.setEndTime(d2);
			} else {
				sr.setEndTime(null);
			}
			// sr.setEndTime(new Date());
			// 睡眠总时间
			sr.setTotalSeconds(userSleep.getSmsctimes());
			// 清醒
			sr.setAwakeSeconds(userSleep.getQxsctimes());
			// 深睡
			sr.setDeepSleepSeconds(userSleep.getSssctimes());
			// 浅睡
			sr.setLightSleepSeconds(userSleep.getQssctimes());
			// 睡眠评分
			sr.setScore(userSleep.getScoreDate());

		}
		return sr;
	}

	/**
	 * 指标
	 */
	public static BreatheReport userIndexToBreatheReport(UserIndex userIndex, UserManager manager) {
		BreatheReport br = new BreatheReport();
		// SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String uuid = manager.getUuid();
		if (userIndex.getAhiIndex() != null) {
			br.setUserId(uuid);
			// 呼吸暂停低通气指数
			br.setApneaHypopneaIndex(Double.parseDouble(userIndex.getAhiIndex()));
			// 呼吸暂停次数
			br.setApneaTimes(Integer.parseInt(userIndex.getHxztIndex()));
			// 低通气次数
			br.setHypopneaTimes(Integer.parseInt(userIndex.getDtqIndex()));
			// 最长低通气秒数
			br.setMaxHyponeaSeconds(userIndex.getZcdtqtimes());
			// 低通气总时间秒数
			br.setTotalHyponeaSeconds(userIndex.getDtqzsjtimes());
			// 低通气发生时间
			if (userIndex.getDtqhappertimes() != 0) {
				long l1 = (long) userIndex.getDtqhappertimes() * 1000;
				Date d3 = new Date(l1);
				br.setHyponeaHappenDate(d3);
			} else {
				br.setHyponeaHappenDate(null);
			}
			// br.setHyponeaHappenDate(new Date());
			// 氧减次数
			br.setReducedOxygenTimes(Integer.parseInt(userIndex.getYjzcsIndex()));
			// 平均血氧饱和度
			br.setAverageOxygenSaturation(Double.parseDouble(userIndex.getPjxybhdIndex()));
			// 最低血氧饱和度
			br.setMinOxygenSaturation(Double.parseDouble(userIndex.getZdxybhdIndex()));
			// odi
			br.setOdi(Double.parseDouble(userIndex.getXyodi()));
			// 醒时
			br.setAwakeSeconds(0);
			br.setOxygenSaturationLessthanNinetyPercent(Double.parseDouble(userIndex.getXybhdzbIndex()));
			// 总分
			br.setScore(userIndex.getScoreHxzb());
			// 血氧低于90%的百分比 ，
			br.setOxygenSaturationNinetyToHundredPercentHyponea(userIndex.getAxyfbzsjtimes());
			br.setOxygenSaturationNinetyToHundredPercentTimes(Integer.parseInt(userIndex.getAyjzcsnum()));
			// 血氧80%-89%的持续时间秒数
			br.setOxygenSaturationEightyToEightyNinePercentHyponea(userIndex.getBxyfbzsjtimes());
			br.setOxygenSaturationEightyToEightyNinePercentTimes(Integer.parseInt(userIndex.getByjzcsnum()));
			// 血氧70%-79%的持续时间秒数
			br.setOxygenSaturationSeventyToSeventyNinePercentHyponea(userIndex.getCxyfbzsjtimes());
			br.setOxygenSaturationSeventyToSeventyNinePercentTimes(Integer.parseInt(userIndex.getCyjzcsnum()));
			// 血氧60%-69%的持续时间秒数
			br.setOxygenSaturationSixtyToSixtyNinePercentHyponea(userIndex.getDxyfbzsjtimes());
			br.setOxygenSaturationSixtyToSixtyNinePercentTimes(Integer.parseInt(userIndex.getDyjzcsnum()));
			// 血氧50%-59%的持续时间秒数
			br.setOxygenSaturationFiftyToFiftyNinePercentHyponea(userIndex.getExyfbzsjtimes());
			br.setOxygenSaturationFiftyToFiftyNinePercentTimes(Integer.parseInt(userIndex.getEyjzcsnum()));
			// 血氧<50%的持续时间秒数
			br.setOxygenSaturationLessthanFiftyPercentHyponea(userIndex.getFxyfbzsjtimes());
			br.setOxygenSaturationLessthanFiftyPercentTimes(Integer.parseInt(userIndex.getFyjzcsnum()));
			// //睡眠报告的UUID客户端生成
			// String uuids=UUID.randomUUID().toString();
			// br.setId(uuids);
		}

		return br;
	}

	/**
	 * 附表
	 */
	public static SubReport srTosr(UserSleep userSleep, UserIndex userIndex) {
		SubReport sur = new SubReport();
		if (userIndex.getXyodi() != null && userIndex.getXywhzsIndex() != null) {
			// 氧减指数
			sur.setOxygenReductionIndex(Double.parseDouble(userIndex.getXyodi()));
			// 最长呼吸暂停时长
			sur.setLongestApneaSeconds(userIndex.getZchxzttimes());
			// 最长呼吸暂停发生时间
			if (userIndex.getHxhappertimes() != 0) {
				long st1 = (long) userIndex.getHxhappertimes() * 1000 + 1;
				Date h1 = new Date(st1);
				sur.setLongestApneaTime(h1);
			} else {
				sur.setLongestApneaTime(null);
			}
			// sur.setLongestApneaTime(new Date());
			// 最大氧降
			sur.setMaxOxygenReduceSeconds(userIndex.getMaxyjcxhour());
			// 最大氧降发生时间
			if (userIndex.getMaxyjfstimes() != 0) {
				long st2 = (long) userIndex.getMaxyjfstimes() * 1000;
				Date h2 = new Date(st2);
				sur.setMaxOxygenReduceTime(h2);
			} else {
				sur.setMaxOxygenReduceTime(null);
			}

			// sur.setMaxOxygenReduceTime(new Date());
			// 平均脉率
			sur.setAveragePulse(Double.parseDouble(userSleep.getAvgml()));
			// 最大脉率
			sur.setMaxPulse(Double.parseDouble(userSleep.getMaxml()));
			// 最大脉率发生时间
			if (userSleep.getMaxmltimes() != 0) {
				long st3 = (long) userSleep.getMaxmltimes() * 1000;
				Date h3 = new Date(st3);
				sur.setMaxPulseTime(h3);
			} else {
				sur.setMaxPulseTime(null);
			}

			// sur.setMaxPulseTime(new Date());
			// 最小脉率
			sur.setMinPulse(Double.parseDouble(userSleep.getMinml()));
			// 最小脉率发生时间
			if (userSleep.getMinmltimes() != 0) {
				long st4 = (long) userSleep.getMinmltimes() * 1000;
				Date h4 = new Date(st4);
				sur.setMinPulseTime(h4);
			} else {
				sur.setMinPulseTime(null);
			}
			// sur.setMinPulseTime(new Date());
			// 最长氧降时长
			sur.setLongestOxygenReduceSeconds(userIndex.getLongyjcxtimes());
			// 最长氧降发生时间
			if (userIndex.getLongyjfstimes() != 0) {
				long st5 = (long) userIndex.getLongyjfstimes() * 1000;
				Date h5 = new Date(st5);
				sur.setLongestOxygenReduceTime(h5);
			} else {
				sur.setLongestOxygenReduceTime(null);
			}
			// sur.setLongestOxygenReduceTime(new Date());
			// 氧减危害指数
			sur.setBloodOxygenHazardIndex(Double.parseDouble(userIndex.getXywhzsIndex()));
			// mac地址
			sur.setMacAddress(userIndex.getMacaddress());
			// 平均血流灌注度
			sur.setPerfusionIndex(Double.parseDouble(userIndex.getAvgxlgzd()));
			// 呼吸暂停总时间
			sur.setTotalApneaTimeSeconds(userIndex.getHxztzsjtimes());
			// 低通气总时间
			sur.setTotalHypoventilationTimeSeconds(userIndex.getDtqzsjtimes());
			// 版本号
			String version = Version.getVersionName();
			sur.setAppVersion(version);
		}
		return sur;
	}

	public static BaseReport toBaseReport(String id, UserSleep userSleep, UserIndex userIndex, UserManager manager)
			throws IllegalAccessException, InvocationTargetException {
		SubReport srTosr = srTosr(userSleep, userIndex);
		SleepReport userSleepToSleepReport = userSleepToSleepReport(userSleep, manager);
		BreatheReport userIndexToBreatheReport = userIndexToBreatheReport(userIndex, manager);
		srTosr.setId(id);
		userSleepToSleepReport.setId(id);
		userIndexToBreatheReport.setId(id);
		BaseReport baseReport = new BaseReport(userSleepToSleepReport, userIndexToBreatheReport, srTosr);
		return baseReport;
	}

	public static boolean isEmpty(String str) {
		if (str != null && !"".equals(str)) {
			return false;
		}
		return true;
	}
}
