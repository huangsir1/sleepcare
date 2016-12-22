package com.sleep.local.classs;

import java.io.Serializable;

public class UserHistory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;

	private int userid;
	private String filepaths;
	private int uploadfile;
	private String macaddress;
	private String reportfilepath;
	private String uuid;
	private int historyupload;
	private int isFinalReport;
	private int isComputed;
	private int calculationflag;
	// 睡眠开始时长
	private int hstarthour, hstartminute;
	// 睡眠结束时长
	private int hendhour, hendminute;

	// 睡眠时长
	private int hsmschour, hsmscminute;

	// 浅睡时长
	private int hqshour, hqsminute;

	// 深睡时长
	private int hsshour, hssminute;

	// 清醒时长
	private int hqxhour, hqxminute;

	// 最高脉率 次/分钟
	private String hmaxml;
	// 发生于 时 分 秒
	private int hmaxmlhour, hmaxmlminute, hmaxmlsec;

	// 最低脉率 次/分钟
	private String hminml;
	// 发生于 时 分 秒
	private int hminmlhour, hminmlminute, hminmlsec;
	// 平均脉率 次/分钟
	private String havgml;

	// 睡眠打分
	private int hsleepscore;

	/**
	 * Long类型时间
	 */
	private int hstarttimes;
	private int hendtimes;
	private int hsmsctimes;
	private int hqssctimes;
	private int hsssctimes;
	private int hqxsctimes;
	private int hmaxmltimes;
	private int hminmltimes;

	private String hahiIndex;
	private String hhxztIndex;
	private String hdtqIndex;
	private String hyjzcsIndex;
	private String hpjxybhdIndex;
	private String hzdxybhdIndex;
	private String hxybhdzbIndex;
	// 呼吸打分
	private int hscoreHxzb;
	// 平均血流灌注度
	private String havgxlgzd;
	// osahs
	private String hosahsdegree;
	// 低氧血症
	private String hdyxzdegree;
	private String hyjzsIndex;
	/**
	 * 新增呼吸内容
	 */
	// 最长呼吸暂停时
	private int hzchxzthour;
	// 最长呼吸暂停分
	private int hzchxztminute;
	// 最长呼吸暂停秒
	private int hzchxztsec;
	// 发生于时
	private int hhxhapperhour;
	// 发生于分
	private int hhxhapperminute;
	// 发生于秒
	private int hhxhappersec;
	// "hxSpO2 integer," +
	// 呼吸暂停总时间时
	private int hhxztzsjhour;
	// 呼吸暂停总时间分
	private int hhxztzsjminute;
	// 呼吸暂停总时间秒
	private int hhxztzsjsec;
	// 占总睡眠时间(%)
	// "hxzzsmsj varchar(20)," +
	// 呼吸暂停指数
	// "hxztzs varchar(20)," +

	/**
	 * 新增低通气内容
	 */
	// 最长低通气时
	private int hzcdtqhour;
	// 最长低通气分
	private int hzcdtqminute;
	// 最长低通气秒
	private int hzcdtqsec;
	// 发生于时
	private int hdtqhapperhour;
	// 发生于分
	private int hdtqhapperminute;
	// 发生于秒
	private int hdtqhappersec;
	// "dtqSpO2 integer," +
	// 低通气总时间时
	private int hdtqzsjhour;
	// 低通气总时间分
	private int hdtqzsjminute;
	// 低通气总时间秒
	private int hdtqzsjsec;
	// 占总睡眠时间(%)
	// "dtqzsjzzsmsj varchar(20)," +
	// 低通气指数
	// "dtqzs varchar(20)," +

	/**
	 * 新增ODI内容
	 */
	// odi
	private String hxyodi;
	// 醒时
	private String hsmsober;
	// 最高
	private String hsmmax;

	/**
	 * 血氧分布总时间
	 */
	/*
	 * a-代表(90%-100%)
	 */
	// a总时间时
	private int haxyfbzsjhour;
	// a总时间分
	private int haxyfbzsjminute;
	// a总时间秒
	private int haxyfbzsjsec;
	// a-氧减总次数
	private String hayjzcsnum;

	/*
	 * b-代表(80%-89%)
	 */
	// b总时间时
	private int hbxyfbzsjhour;
	// b总时间分
	private int hbxyfbzsjminute;
	// b总时间秒
	private int hbxyfbzsjsec;
	// b-氧减总次数
	private String hbyjzcsnum;

	/*
	 * c-代表(70%-79%)
	 */
	// c总时间时
	private int hcxyfbzsjhour;
	// c总时间分
	private int hcxyfbzsjminute;
	// c总时间秒
	private int hcxyfbzsjsec;
	// c-氧减总次数
	private String hcyjzcsnum;

	/*
	 * d-代表(60%-69%)
	 */
	// d总时间时
	private int hdxyfbzsjhour;
	// d总时间分
	private int hdxyfbzsjminute;
	// d总时间秒
	private int hdxyfbzsjsec;
	// d-氧减总次数
	private String hdyjzcsnum;

	/*
	 * e-代表(50%-59%)
	 */
	// e总时间时
	private int hexyfbzsjhour;
	// e总时间分
	private int hexyfbzsjminute;
	// e总时间秒
	private int hexyfbzsjsec;
	// e-氧减总次数
	private String heyjzcsnum;

	/*
	 * f-代表(<50%)
	 */
	// f总时间时
	private int hfxyfbzsjhour;
	// f总时间分
	private int hfxyfbzsjminute;
	// f总时间秒
	private int hfxyfbzsjsec;
	// f-氧减总次数
	private String hfyjzcsnum;

	// osahs病情程度
	private String hudegree;
	/**
	 * 氧降
	 */
	// 最大氧降次数
	// 最大氧降 持续时
	private int hmaxyjcxhour;
	// 最大氧降 持续分
	private int hmaxyjcxminute;
	// 最大氧降 持续秒
	private int hmaxyjcxsec;
	// 最大氧降 发生时
	private int hmaxyjfshour;
	// 最大氧降 发生分
	private int hmaxyjfsminute;
	// 最大氧降 发生秒
	private int hmaxyjfssec;
	// 最长氧降次数
	// 最长氧降 持续时
	private int hlongyjcxhour;
	// 最长氧降 持续分
	private int hlongyjcxminute;
	// 最长氧降 持续秒
	private int hlongyjcxsec;
	// 最长氧降 发生时
	private int hlongyjfshour;
	// 最长氧降 发生分
	private int hlongyjfsminute;
	// 最长氧降 发生秒
	private int hlongyjfssec;
	// 氧减危害指数
	private String hxywhzsIndex;

	private int testdate;
	private String reportuuid;
	/**
	 * Long类型时间
	 */
	private int hmaxyjcxtimes;
	private int hmaxyjfstimes;
	private int hlongyjcxtimes;
	private int hlongyjfstimes;
	private int hzchxzttimes;
	private int hhxhappertimes;
	private int hhxztzsjtimes;
	private int hzcdtqtimes;
	private int hdtqhappertimes;
	private int hdtqzsjtimes;
	private int haxyfbzsjtimes;
	private int hbxyfbzsjtimes;
	private int hcxyfbzsjtimes;
	private int hdxyfbzsjtimes;
	private int hexyfbzsjtimes;
	private int hfxyfbzsjtimes;

	public UserHistory() {
		super();
	}
	
	public UserIndex getUserIndex(){
		UserIndex ui = new UserIndex();
		/**
		 * 指标
		 */
		// AHI
		ui.setAhiIndex(hahiIndex);
		// 呼吸暂停次数
		ui.setHxztIndex(hhxztIndex);
		// 低通气次数
		ui.setDtqIndex(hdtqIndex);
		// 氧减次数
		ui.setYjzcsIndex(hyjzcsIndex);
		// 平均血氧饱和度
		ui.setPjxybhdIndex(hpjxybhdIndex);
		// 最低血氧饱和度
		ui.setZdxybhdIndex(hzdxybhdIndex);
		// 血氧低于90%占比
		ui.setXybhdzbIndex(hxybhdzbIndex);
		/** 氧降 */
		// 最大氧降 持续时
		ui.setMaxyjcxhour(hmaxyjcxhour);
		// 最大氧降 持续分
		ui.setMaxyjcxminute(hmaxyjcxminute);
		// 最大氧降 持续秒
		ui.setMaxyjcxsec(hmaxyjcxsec);
		ui.setMaxyjcxhour(hmaxyjcxhour);
		// 最大氧降 发生时
		ui.setMaxyjfshour(hmaxyjfshour);
		// 最大氧降 发生分
		ui.setMaxyjfsminute(hmaxyjfsminute);
		// 最大氧降 发生秒
		ui.setMaxyjfssec(hmaxyjfssec);
		ui.setMaxyjfstimes(hmaxyjfstimes);
		// 最长氧降 持续时
		ui.setLongyjcxhour(hlongyjcxhour);
		// 最长氧降 持续分
		ui.setLongyjcxminute(hlongyjcxminute);
		// 最长氧降 持续秒
		ui.setLongyjcxsec(hlongyjcxsec);
		ui.setLongyjcxtimes(hlongyjcxtimes);
		// 最长氧降 发生时
		ui.setLongyjfshour(hlongyjfshour);
		// 最长氧降 发生分
		ui.setLongyjfsminute(hlongyjfsminute);
		// 最长氧降 发生秒
		ui.setLongyjfssec(hlongyjfssec);
		ui.setLongyjfstimes(hlongyjfstimes);
		// 氧减危害指数
		ui.setXywhzsIndex(hxywhzsIndex);
		// 平均血流灌注度
		ui.setAvgxlgzd(havgxlgzd);
		// 评分
		ui.setScoreHxzb(hscoreHxzb);
		// 最长呼吸暂停时长
		ui.setZchxzthour(hzchxzthour);
		ui.setZchxztminute(hzchxztminute);
		ui.setZchxztsec(hzchxztsec);
		ui.setZchxzttimes(hzchxzttimes);
		// 发生于
		ui.setHxhapperhour(hhxhapperhour);
		ui.setHxhapperminute(hhxhapperminute);
		ui.setHxhappersec(hhxhappersec);
		ui.setHxhappertimes(hhxhappertimes);
		// 总时间
		ui.setHxztzsjhour(hhxztzsjhour);
		ui.setHxztzsjminute(hhxztzsjminute);
		ui.setHxztzsjsec(hhxztzsjsec);
		ui.setHxztzsjtimes(hhxztzsjtimes);
		// 最长低通气时长
		ui.setZcdtqhour(hzcdtqhour);
		ui.setZcdtqminute(hzcdtqminute);
		ui.setZcdtqsec(hzcdtqsec);
		ui.setZcdtqtimes(hzcdtqtimes);
		// 发生于
		ui.setDtqhapperhour(hdtqhapperhour);
		ui.setDtqhapperminute(hdtqhapperminute);
		ui.setDtqhappersec(hdtqhappersec);
		ui.setDtqhappertimes(hdtqhappertimes);
		// 总时间
		ui.setDtqzsjhour(hdtqzsjhour);
		ui.setDtqzsjminute(hdtqzsjminute);
		ui.setDtqzsjsec(hdtqzsjsec);
		ui.setDtqzsjtimes(hdtqzsjtimes);
		// ODI
		ui.setXyodi(hxyodi);
		// 醒时
		// 最高
		ui.setSmmax(hsmmax);

		// 90-100总时间
		ui.setAxyfbzsjhour(haxyfbzsjhour);
		ui.setAxyfbzsjminute(haxyfbzsjminute);
		ui.setAxyfbzsjsec(haxyfbzsjsec);
		ui.setAxyfbzsjtimes(haxyfbzsjtimes);
		// 90-100氧减总次数
		ui.setAyjzcsnum(hayjzcsnum);
		// 80-90
		ui.setBxyfbzsjhour(hbxyfbzsjhour);
		ui.setBxyfbzsjminute(hbxyfbzsjminute);
		ui.setBxyfbzsjsec(hbxyfbzsjsec);
		ui.setBxyfbzsjtimes(hbxyfbzsjtimes);
		ui.setByjzcsnum(hbyjzcsnum);
		// 70-80
		ui.setCxyfbzsjhour(hcxyfbzsjhour);
		ui.setCxyfbzsjminute(hcxyfbzsjminute);
		ui.setCxyfbzsjsec(hcxyfbzsjsec);
		ui.setCxyfbzsjtimes(hcxyfbzsjtimes);
		ui.setCyjzcsnum(hcyjzcsnum);
		// 60-70
		ui.setDxyfbzsjhour(hdxyfbzsjhour);
		ui.setDxyfbzsjminute(hdxyfbzsjminute);
		ui.setDxyfbzsjsec(hdxyfbzsjsec);
		ui.setDxyfbzsjtimes(hdxyfbzsjtimes);
		ui.setDyjzcsnum(hdyjzcsnum);
		// 50-60
		ui.setExyfbzsjhour(hexyfbzsjhour);
		ui.setExyfbzsjminute(hexyfbzsjminute);
		ui.setExyfbzsjsec(hexyfbzsjsec);
		ui.setExyfbzsjtimes(hexyfbzsjtimes);
		ui.setEyjzcsnum(heyjzcsnum);
		// 0-50
		ui.setFxyfbzsjhour(hfxyfbzsjhour);
		ui.setFxyfbzsjminute(hfxyfbzsjminute);
		ui.setFxyfbzsjsec(hfxyfbzsjsec);
		ui.setFxyfbzsjtimes(hfxyfbzsjtimes);
		ui.setFyjzcsnum(hfyjzcsnum);

		// OSAHS
		ui.setUdegree(hudegree);
		// OSAHS程度
		ui.setOsahsdegree(hosahsdegree);
		// 低血氧症程度
		ui.setDyxzdegree(hdyxzdegree);
		
		ui.setUpload(historyupload);
		ui.setFilepaths(filepaths);
		ui.setUuid(uuid);
		
		ui.setMacaddress(macaddress);
		return ui;
	}
	
	public UserSleep getUserSleep(){
		UserSleep us = new UserSleep();
		/**
		 * 睡眠
		 */
		// 起始时间
		us.setStarthour(hstarthour);
		us.setStartminute(hstartminute);
		us.setStarttimes(hstarttimes);
		// 结束时间
		us.setEndhour(hendhour);
		us.setEndminute(hendminute);
		us.setEndtimes(hendtimes);
		// Log.d("TAG", "-------------------" + (int)
		// (mSleep_stageSeperate.getTime_end2() / 1000));

		// 持续时间
		us.setSmschour(hsmschour);
		us.setSmscminute(hsmscminute);
		us.setSmsctimes(hsmsctimes);

		// 浅睡
		us.setQshour(hqshour);
		us.setQsminute(hqsminute);
		us.setQssctimes(hqssctimes);

		// 深睡
		us.setSshour(hsshour);
		us.setSsminute(hssminute);
		us.setSssctimes(hsssctimes);

		// 清醒
		us.setQxhour(hqxhour);
		us.setQxminute(hqxminute);
		us.setQxsctimes(hqxsctimes);

		/** 脉率 */
		// 最高脉率
		us.setMaxml(hmaxml);
		// 发生于 时
		us.setMaxmlhour(hmaxmlhour);
		// 发生于 分
		us.setMaxmlminute(hmaxmlminute);
		// 发生于 秒
		us.setMaxmlsec(hmaxmlsec);
		us.setMaxmltimes(hmaxmltimes);
		// 最低脉率
		us.setMinml(hminml);
		// 发生于 时
		us.setMinmlhour(hminmlhour);
		// 发生于 分
		us.setMinmlminute(hminmlminute);
		// 发生于 秒
		us.setMinmlsec(hminmlsec);
		us.setMinmltimes(hminmltimes);
		// 平均脉率
		us.setAvgml(havgml);

		// 睡眠评分
		us.setScoreDate(hsleepscore);
		
		us.setUpload(historyupload);
		return us;
	}
	
	

	public UserHistory(int id, int userid, String filepaths, int uploadfile, String macaddress, String reportfilepath,
			String uuid, int historyupload, int isFinalReport, int isComputed, int hstarthour, int hstartminute,
			int hendhour, int hendminute, int hsmschour, int hsmscminute, int hqshour, int hqsminute, int hsshour,
			int hssminute, int hqxhour, int hqxminute, String hmaxml, int hmaxmlhour, int hmaxmlminute, int hmaxmlsec,
			String hminml, int hminmlhour, int hminmlminute, int hminmlsec, String havgml, int hsleepscore,
			int hstarttimes, int hendtimes, int hsmsctimes, int hqssctimes, int hsssctimes, int hqxsctimes,
			int hmaxmltimes, int hminmltimes, String hahiIndex, String hhxztIndex, String hdtqIndex, String hyjzcsIndex,
			String hpjxybhdIndex, String hzdxybhdIndex, String hxybhdzbIndex, int hscoreHxzb, String havgxlgzd,
			String hosahsdegree, String hdyxzdegree, String hyjzsIndex, int hzchxzthour, int hzchxztminute,
			int hzchxztsec, int hhxhapperhour, int hhxhapperminute, int hhxhappersec, int hhxztzsjhour,
			int hhxztzsjminute, int hhxztzsjsec, int hzcdtqhour, int hzcdtqminute, int hzcdtqsec, int hdtqhapperhour,
			int hdtqhapperminute, int hdtqhappersec, int hdtqzsjhour, int hdtqzsjminute, int hdtqzsjsec, String hxyodi,
			String hsmsober, String hsmmax, int haxyfbzsjhour, int haxyfbzsjminute, int haxyfbzsjsec, String hayjzcsnum,
			int hbxyfbzsjhour, int hbxyfbzsjminute, int hbxyfbzsjsec, String hbyjzcsnum, int hcxyfbzsjhour,
			int hcxyfbzsjminute, int hcxyfbzsjsec, String hcyjzcsnum, int hdxyfbzsjhour, int hdxyfbzsjminute,
			int hdxyfbzsjsec, String hdyjzcsnum, int hexyfbzsjhour, int hexyfbzsjminute, int hexyfbzsjsec,
			String heyjzcsnum, int hfxyfbzsjhour, int hfxyfbzsjminute, int hfxyfbzsjsec, String hfyjzcsnum,
			String hudegree, int hmaxyjcxhour, int hmaxyjcxminute, int hmaxyjcxsec, int hmaxyjfshour,
			int hmaxyjfsminute, int hmaxyjfssec, int hlongyjcxhour, int hlongyjcxminute, int hlongyjcxsec,
			int hlongyjfshour, int hlongyjfsminute, int hlongyjfssec, String hxywhzsIndex, int testdate,
			String reportuuid, int hmaxyjcxtimes, int hmaxyjfstimes, int hlongyjcxtimes, int hlongyjfstimes,
			int hzchxzttimes, int hhxhappertimes, int hhxztzsjtimes, int hzcdtqtimes, int hdtqhappertimes,
			int hdtqzsjtimes, int haxyfbzsjtimes, int hbxyfbzsjtimes, int hcxyfbzsjtimes, int hdxyfbzsjtimes,
			int hexyfbzsjtimes, int hfxyfbzsjtimes) {
		super();
		this.id = id;
		this.userid = userid;
		this.filepaths = filepaths;
		this.uploadfile = uploadfile;
		this.macaddress = macaddress;
		this.reportfilepath = reportfilepath;
		this.uuid = uuid;
		this.historyupload = historyupload;
		this.isFinalReport = isFinalReport;
		this.isComputed = isComputed;
		this.hstarthour = hstarthour;
		this.hstartminute = hstartminute;
		this.hendhour = hendhour;
		this.hendminute = hendminute;
		this.hsmschour = hsmschour;
		this.hsmscminute = hsmscminute;
		this.hqshour = hqshour;
		this.hqsminute = hqsminute;
		this.hsshour = hsshour;
		this.hssminute = hssminute;
		this.hqxhour = hqxhour;
		this.hqxminute = hqxminute;
		this.hmaxml = hmaxml;
		this.hmaxmlhour = hmaxmlhour;
		this.hmaxmlminute = hmaxmlminute;
		this.hmaxmlsec = hmaxmlsec;
		this.hminml = hminml;
		this.hminmlhour = hminmlhour;
		this.hminmlminute = hminmlminute;
		this.hminmlsec = hminmlsec;
		this.havgml = havgml;
		this.hsleepscore = hsleepscore;
		this.hstarttimes = hstarttimes;
		this.hendtimes = hendtimes;
		this.hsmsctimes = hsmsctimes;
		this.hqssctimes = hqssctimes;
		this.hsssctimes = hsssctimes;
		this.hqxsctimes = hqxsctimes;
		this.hmaxmltimes = hmaxmltimes;
		this.hminmltimes = hminmltimes;
		this.hahiIndex = hahiIndex;
		this.hhxztIndex = hhxztIndex;
		this.hdtqIndex = hdtqIndex;
		this.hyjzcsIndex = hyjzcsIndex;
		this.hpjxybhdIndex = hpjxybhdIndex;
		this.hzdxybhdIndex = hzdxybhdIndex;
		this.hxybhdzbIndex = hxybhdzbIndex;
		this.hscoreHxzb = hscoreHxzb;
		this.havgxlgzd = havgxlgzd;
		this.hosahsdegree = hosahsdegree;
		this.hdyxzdegree = hdyxzdegree;
		this.hyjzsIndex = hyjzsIndex;
		this.hzchxzthour = hzchxzthour;
		this.hzchxztminute = hzchxztminute;
		this.hzchxztsec = hzchxztsec;
		this.hhxhapperhour = hhxhapperhour;
		this.hhxhapperminute = hhxhapperminute;
		this.hhxhappersec = hhxhappersec;
		this.hhxztzsjhour = hhxztzsjhour;
		this.hhxztzsjminute = hhxztzsjminute;
		this.hhxztzsjsec = hhxztzsjsec;
		this.hzcdtqhour = hzcdtqhour;
		this.hzcdtqminute = hzcdtqminute;
		this.hzcdtqsec = hzcdtqsec;
		this.hdtqhapperhour = hdtqhapperhour;
		this.hdtqhapperminute = hdtqhapperminute;
		this.hdtqhappersec = hdtqhappersec;
		this.hdtqzsjhour = hdtqzsjhour;
		this.hdtqzsjminute = hdtqzsjminute;
		this.hdtqzsjsec = hdtqzsjsec;
		this.hxyodi = hxyodi;
		this.hsmsober = hsmsober;
		this.hsmmax = hsmmax;
		this.haxyfbzsjhour = haxyfbzsjhour;
		this.haxyfbzsjminute = haxyfbzsjminute;
		this.haxyfbzsjsec = haxyfbzsjsec;
		this.hayjzcsnum = hayjzcsnum;
		this.hbxyfbzsjhour = hbxyfbzsjhour;
		this.hbxyfbzsjminute = hbxyfbzsjminute;
		this.hbxyfbzsjsec = hbxyfbzsjsec;
		this.hbyjzcsnum = hbyjzcsnum;
		this.hcxyfbzsjhour = hcxyfbzsjhour;
		this.hcxyfbzsjminute = hcxyfbzsjminute;
		this.hcxyfbzsjsec = hcxyfbzsjsec;
		this.hcyjzcsnum = hcyjzcsnum;
		this.hdxyfbzsjhour = hdxyfbzsjhour;
		this.hdxyfbzsjminute = hdxyfbzsjminute;
		this.hdxyfbzsjsec = hdxyfbzsjsec;
		this.hdyjzcsnum = hdyjzcsnum;
		this.hexyfbzsjhour = hexyfbzsjhour;
		this.hexyfbzsjminute = hexyfbzsjminute;
		this.hexyfbzsjsec = hexyfbzsjsec;
		this.heyjzcsnum = heyjzcsnum;
		this.hfxyfbzsjhour = hfxyfbzsjhour;
		this.hfxyfbzsjminute = hfxyfbzsjminute;
		this.hfxyfbzsjsec = hfxyfbzsjsec;
		this.hfyjzcsnum = hfyjzcsnum;
		this.hudegree = hudegree;
		this.hmaxyjcxhour = hmaxyjcxhour;
		this.hmaxyjcxminute = hmaxyjcxminute;
		this.hmaxyjcxsec = hmaxyjcxsec;
		this.hmaxyjfshour = hmaxyjfshour;
		this.hmaxyjfsminute = hmaxyjfsminute;
		this.hmaxyjfssec = hmaxyjfssec;
		this.hlongyjcxhour = hlongyjcxhour;
		this.hlongyjcxminute = hlongyjcxminute;
		this.hlongyjcxsec = hlongyjcxsec;
		this.hlongyjfshour = hlongyjfshour;
		this.hlongyjfsminute = hlongyjfsminute;
		this.hlongyjfssec = hlongyjfssec;
		this.hxywhzsIndex = hxywhzsIndex;
		this.testdate = testdate;
		this.reportuuid = reportuuid;
		this.hmaxyjcxtimes = hmaxyjcxtimes;
		this.hmaxyjfstimes = hmaxyjfstimes;
		this.hlongyjcxtimes = hlongyjcxtimes;
		this.hlongyjfstimes = hlongyjfstimes;
		this.hzchxzttimes = hzchxzttimes;
		this.hhxhappertimes = hhxhappertimes;
		this.hhxztzsjtimes = hhxztzsjtimes;
		this.hzcdtqtimes = hzcdtqtimes;
		this.hdtqhappertimes = hdtqhappertimes;
		this.hdtqzsjtimes = hdtqzsjtimes;
		this.haxyfbzsjtimes = haxyfbzsjtimes;
		this.hbxyfbzsjtimes = hbxyfbzsjtimes;
		this.hcxyfbzsjtimes = hcxyfbzsjtimes;
		this.hdxyfbzsjtimes = hdxyfbzsjtimes;
		this.hexyfbzsjtimes = hexyfbzsjtimes;
		this.hfxyfbzsjtimes = hfxyfbzsjtimes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHstarthour() {
		return hstarthour;
	}

	public void setHstarthour(int hstarthour) {
		this.hstarthour = hstarthour;
	}

	public int getHstartminute() {
		return hstartminute;
	}

	public void setHstartminute(int hstartminute) {
		this.hstartminute = hstartminute;
	}

	public int getHendhour() {
		return hendhour;
	}

	public void setHendhour(int hendhour) {
		this.hendhour = hendhour;
	}

	public int getHendminute() {
		return hendminute;
	}

	public void setHendminute(int hendminute) {
		this.hendminute = hendminute;
	}

	public int getHsmschour() {
		return hsmschour;
	}

	public void setHsmschour(int hsmschour) {
		this.hsmschour = hsmschour;
	}

	public int getHsmscminute() {
		return hsmscminute;
	}

	public void setHsmscminute(int hsmscminute) {
		this.hsmscminute = hsmscminute;
	}

	public int getHqshour() {
		return hqshour;
	}

	public void setHqshour(int hqshour) {
		this.hqshour = hqshour;
	}

	public int getHqsminute() {
		return hqsminute;
	}

	public void setHqsminute(int hqsminute) {
		this.hqsminute = hqsminute;
	}

	public int getHsshour() {
		return hsshour;
	}

	public void setHsshour(int hsshour) {
		this.hsshour = hsshour;
	}

	public int getHssminute() {
		return hssminute;
	}

	public void setHssminute(int hssminute) {
		this.hssminute = hssminute;
	}

	public int getHqxhour() {
		return hqxhour;
	}

	public void setHqxhour(int hqxhour) {
		this.hqxhour = hqxhour;
	}

	public int getHqxminute() {
		return hqxminute;
	}

	public void setHqxminute(int hqxminute) {
		this.hqxminute = hqxminute;
	}

	public String getHmaxml() {
		return hmaxml;
	}

	public void setHmaxml(String hmaxml) {
		this.hmaxml = hmaxml;
	}

	public int getHmaxmlhour() {
		return hmaxmlhour;
	}

	public void setHmaxmlhour(int hmaxmlhour) {
		this.hmaxmlhour = hmaxmlhour;
	}

	public int getHmaxmlminute() {
		return hmaxmlminute;
	}

	public void setHmaxmlminute(int hmaxmlminute) {
		this.hmaxmlminute = hmaxmlminute;
	}

	public int getHmaxmlsec() {
		return hmaxmlsec;
	}

	public void setHmaxmlsec(int hmaxmlsec) {
		this.hmaxmlsec = hmaxmlsec;
	}

	public String getHminml() {
		return hminml;
	}

	public void setHminml(String hminml) {
		this.hminml = hminml;
	}

	public int getHminmlhour() {
		return hminmlhour;
	}

	public void setHminmlhour(int hminmlhour) {
		this.hminmlhour = hminmlhour;
	}

	public int getHminmlminute() {
		return hminmlminute;
	}

	public void setHminmlminute(int hminmlminute) {
		this.hminmlminute = hminmlminute;
	}

	public int getHminmlsec() {
		return hminmlsec;
	}

	public void setHminmlsec(int hminmlsec) {
		this.hminmlsec = hminmlsec;
	}

	public String getHavgml() {
		return havgml;
	}

	public void setHavgml(String havgml) {
		this.havgml = havgml;
	}

	public int getHsleepscore() {
		return hsleepscore;
	}

	public void setHsleepscore(int hsleepscore) {
		this.hsleepscore = hsleepscore;
	}

	public int getHstarttimes() {
		return hstarttimes;
	}

	public void setHstarttimes(int hstarttimes) {
		this.hstarttimes = hstarttimes;
	}

	public int getHendtimes() {
		return hendtimes;
	}

	public void setHendtimes(int hendtimes) {
		this.hendtimes = hendtimes;
	}

	public int getHsmsctimes() {
		return hsmsctimes;
	}

	public void setHsmsctimes(int hsmsctimes) {
		this.hsmsctimes = hsmsctimes;
	}

	public int getHqssctimes() {
		return hqssctimes;
	}

	public void setHqssctimes(int hqssctimes) {
		this.hqssctimes = hqssctimes;
	}

	public int getHsssctimes() {
		return hsssctimes;
	}

	public void setHsssctimes(int hsssctimes) {
		this.hsssctimes = hsssctimes;
	}

	public int getHqxsctimes() {
		return hqxsctimes;
	}

	public void setHqxsctimes(int hqxsctimes) {
		this.hqxsctimes = hqxsctimes;
	}

	public int getHmaxmltimes() {
		return hmaxmltimes;
	}

	public void setHmaxmltimes(int hmaxmltimes) {
		this.hmaxmltimes = hmaxmltimes;
	}

	public int getHminmltimes() {
		return hminmltimes;
	}

	public void setHminmltimes(int hminmltimes) {
		this.hminmltimes = hminmltimes;
	}

	public String getHahiIndex() {
		return hahiIndex;
	}

	public void setHahiIndex(String hahiIndex) {
		this.hahiIndex = hahiIndex;
	}

	public String getHhxztIndex() {
		return hhxztIndex;
	}

	public void setHhxztIndex(String hhxztIndex) {
		this.hhxztIndex = hhxztIndex;
	}

	public String getHdtqIndex() {
		return hdtqIndex;
	}

	public void setHdtqIndex(String hdtqIndex) {
		this.hdtqIndex = hdtqIndex;
	}

	public String getHyjzcsIndex() {
		return hyjzcsIndex;
	}

	public void setHyjzcsIndex(String hyjzcsIndex) {
		this.hyjzcsIndex = hyjzcsIndex;
	}

	public String getHpjxybhdIndex() {
		return hpjxybhdIndex;
	}

	public void setHpjxybhdIndex(String hpjxybhdIndex) {
		this.hpjxybhdIndex = hpjxybhdIndex;
	}

	public String getHzdxybhdIndex() {
		return hzdxybhdIndex;
	}

	public void setHzdxybhdIndex(String hzdxybhdIndex) {
		this.hzdxybhdIndex = hzdxybhdIndex;
	}

	public String getHxybhdzbIndex() {
		return hxybhdzbIndex;
	}

	public void setHxybhdzbIndex(String hxybhdzbIndex) {
		this.hxybhdzbIndex = hxybhdzbIndex;
	}

	public int getHscoreHxzb() {
		return hscoreHxzb;
	}

	public void setHscoreHxzb(int hscoreHxzb) {
		this.hscoreHxzb = hscoreHxzb;
	}

	public String getHavgxlgzd() {
		return havgxlgzd;
	}

	public void setHavgxlgzd(String havgxlgzd) {
		this.havgxlgzd = havgxlgzd;
	}

	public String getHosahsdegree() {
		return hosahsdegree;
	}

	public void setHosahsdegree(String hosahsdegree) {
		this.hosahsdegree = hosahsdegree;
	}

	public String getHdyxzdegree() {
		return hdyxzdegree;
	}

	public void setHdyxzdegree(String hdyxzdegree) {
		this.hdyxzdegree = hdyxzdegree;
	}

	public int getHzchxzthour() {
		return hzchxzthour;
	}

	public void setHzchxzthour(int hzchxzthour) {
		this.hzchxzthour = hzchxzthour;
	}

	public int getHzchxztminute() {
		return hzchxztminute;
	}

	public void setHzchxztminute(int hzchxztminute) {
		this.hzchxztminute = hzchxztminute;
	}

	public int getHzchxztsec() {
		return hzchxztsec;
	}

	public void setHzchxztsec(int hzchxztsec) {
		this.hzchxztsec = hzchxztsec;
	}

	public int getHhxhapperhour() {
		return hhxhapperhour;
	}

	public void setHhxhapperhour(int hhxhapperhour) {
		this.hhxhapperhour = hhxhapperhour;
	}

	public int getHhxhapperminute() {
		return hhxhapperminute;
	}

	public void setHhxhapperminute(int hhxhapperminute) {
		this.hhxhapperminute = hhxhapperminute;
	}

	public int getHhxhappersec() {
		return hhxhappersec;
	}

	public void setHhxhappersec(int hhxhappersec) {
		this.hhxhappersec = hhxhappersec;
	}

	public int getHhxztzsjhour() {
		return hhxztzsjhour;
	}

	public void setHhxztzsjhour(int hhxztzsjhour) {
		this.hhxztzsjhour = hhxztzsjhour;
	}

	public int getHhxztzsjminute() {
		return hhxztzsjminute;
	}

	public void setHhxztzsjminute(int hhxztzsjminute) {
		this.hhxztzsjminute = hhxztzsjminute;
	}

	public int getHhxztzsjsec() {
		return hhxztzsjsec;
	}

	public void setHhxztzsjsec(int hhxztzsjsec) {
		this.hhxztzsjsec = hhxztzsjsec;
	}

	public int getHzcdtqhour() {
		return hzcdtqhour;
	}

	public void setHzcdtqhour(int hzcdtqhour) {
		this.hzcdtqhour = hzcdtqhour;
	}

	public int getHzcdtqminute() {
		return hzcdtqminute;
	}

	public void setHzcdtqminute(int hzcdtqminute) {
		this.hzcdtqminute = hzcdtqminute;
	}

	public int getHzcdtqsec() {
		return hzcdtqsec;
	}

	public void setHzcdtqsec(int hzcdtqsec) {
		this.hzcdtqsec = hzcdtqsec;
	}

	public int getHdtqhapperhour() {
		return hdtqhapperhour;
	}

	public void setHdtqhapperhour(int hdtqhapperhour) {
		this.hdtqhapperhour = hdtqhapperhour;
	}

	public int getHdtqhapperminute() {
		return hdtqhapperminute;
	}

	public void setHdtqhapperminute(int hdtqhapperminute) {
		this.hdtqhapperminute = hdtqhapperminute;
	}

	public int getHdtqhappersec() {
		return hdtqhappersec;
	}

	public void setHdtqhappersec(int hdtqhappersec) {
		this.hdtqhappersec = hdtqhappersec;
	}

	public int getHdtqzsjhour() {
		return hdtqzsjhour;
	}

	public void setHdtqzsjhour(int hdtqzsjhour) {
		this.hdtqzsjhour = hdtqzsjhour;
	}

	public int getHdtqzsjminute() {
		return hdtqzsjminute;
	}

	public void setHdtqzsjminute(int hdtqzsjminute) {
		this.hdtqzsjminute = hdtqzsjminute;
	}

	public int getHdtqzsjsec() {
		return hdtqzsjsec;
	}

	public void setHdtqzsjsec(int hdtqzsjsec) {
		this.hdtqzsjsec = hdtqzsjsec;
	}

	public String getHxyodi() {
		return hxyodi;
	}

	public void setHxyodi(String hxyodi) {
		this.hxyodi = hxyodi;
	}

	public String getHsmsober() {
		return hsmsober;
	}

	public void setHsmsober(String hsmsober) {
		this.hsmsober = hsmsober;
	}

	public String getHsmmax() {
		return hsmmax;
	}

	public void setHsmmax(String hsmmax) {
		this.hsmmax = hsmmax;
	}

	public int getHaxyfbzsjhour() {
		return haxyfbzsjhour;
	}

	public void setHaxyfbzsjhour(int haxyfbzsjhour) {
		this.haxyfbzsjhour = haxyfbzsjhour;
	}

	public int getHaxyfbzsjminute() {
		return haxyfbzsjminute;
	}

	public void setHaxyfbzsjminute(int haxyfbzsjminute) {
		this.haxyfbzsjminute = haxyfbzsjminute;
	}

	public int getHaxyfbzsjsec() {
		return haxyfbzsjsec;
	}

	public void setHaxyfbzsjsec(int haxyfbzsjsec) {
		this.haxyfbzsjsec = haxyfbzsjsec;
	}

	public String getHayjzcsnum() {
		return hayjzcsnum;
	}

	public void setHayjzcsnum(String hayjzcsnum) {
		this.hayjzcsnum = hayjzcsnum;
	}

	public int getHbxyfbzsjhour() {
		return hbxyfbzsjhour;
	}

	public void setHbxyfbzsjhour(int hbxyfbzsjhour) {
		this.hbxyfbzsjhour = hbxyfbzsjhour;
	}

	public int getHbxyfbzsjminute() {
		return hbxyfbzsjminute;
	}

	public void setHbxyfbzsjminute(int hbxyfbzsjminute) {
		this.hbxyfbzsjminute = hbxyfbzsjminute;
	}

	public int getHbxyfbzsjsec() {
		return hbxyfbzsjsec;
	}

	public void setHbxyfbzsjsec(int hbxyfbzsjsec) {
		this.hbxyfbzsjsec = hbxyfbzsjsec;
	}

	public String getHbyjzcsnum() {
		return hbyjzcsnum;
	}

	public void setHbyjzcsnum(String hbyjzcsnum) {
		this.hbyjzcsnum = hbyjzcsnum;
	}

	public int getHcxyfbzsjhour() {
		return hcxyfbzsjhour;
	}

	public void setHcxyfbzsjhour(int hcxyfbzsjhour) {
		this.hcxyfbzsjhour = hcxyfbzsjhour;
	}

	public int getHcxyfbzsjminute() {
		return hcxyfbzsjminute;
	}

	public void setHcxyfbzsjminute(int hcxyfbzsjminute) {
		this.hcxyfbzsjminute = hcxyfbzsjminute;
	}

	public int getHcxyfbzsjsec() {
		return hcxyfbzsjsec;
	}

	public void setHcxyfbzsjsec(int hcxyfbzsjsec) {
		this.hcxyfbzsjsec = hcxyfbzsjsec;
	}

	public String getHcyjzcsnum() {
		return hcyjzcsnum;
	}

	public void setHcyjzcsnum(String hcyjzcsnum) {
		this.hcyjzcsnum = hcyjzcsnum;
	}

	public int getHdxyfbzsjhour() {
		return hdxyfbzsjhour;
	}

	public void setHdxyfbzsjhour(int hdxyfbzsjhour) {
		this.hdxyfbzsjhour = hdxyfbzsjhour;
	}

	public int getHdxyfbzsjminute() {
		return hdxyfbzsjminute;
	}

	public void setHdxyfbzsjminute(int hdxyfbzsjminute) {
		this.hdxyfbzsjminute = hdxyfbzsjminute;
	}

	public int getHdxyfbzsjsec() {
		return hdxyfbzsjsec;
	}

	public void setHdxyfbzsjsec(int hdxyfbzsjsec) {
		this.hdxyfbzsjsec = hdxyfbzsjsec;
	}

	public String getHdyjzcsnum() {
		return hdyjzcsnum;
	}

	public void setHdyjzcsnum(String hdyjzcsnum) {
		this.hdyjzcsnum = hdyjzcsnum;
	}

	public int getHexyfbzsjhour() {
		return hexyfbzsjhour;
	}

	public void setHexyfbzsjhour(int hexyfbzsjhour) {
		this.hexyfbzsjhour = hexyfbzsjhour;
	}

	public int getHexyfbzsjminute() {
		return hexyfbzsjminute;
	}

	public void setHexyfbzsjminute(int hexyfbzsjminute) {
		this.hexyfbzsjminute = hexyfbzsjminute;
	}

	public int getHexyfbzsjsec() {
		return hexyfbzsjsec;
	}

	public void setHexyfbzsjsec(int hexyfbzsjsec) {
		this.hexyfbzsjsec = hexyfbzsjsec;
	}

	public String getHeyjzcsnum() {
		return heyjzcsnum;
	}

	public void setHeyjzcsnum(String heyjzcsnum) {
		this.heyjzcsnum = heyjzcsnum;
	}

	public int getHfxyfbzsjhour() {
		return hfxyfbzsjhour;
	}

	public void setHfxyfbzsjhour(int hfxyfbzsjhour) {
		this.hfxyfbzsjhour = hfxyfbzsjhour;
	}

	public int getHfxyfbzsjminute() {
		return hfxyfbzsjminute;
	}

	public void setHfxyfbzsjminute(int hfxyfbzsjminute) {
		this.hfxyfbzsjminute = hfxyfbzsjminute;
	}

	public int getHfxyfbzsjsec() {
		return hfxyfbzsjsec;
	}

	public void setHfxyfbzsjsec(int hfxyfbzsjsec) {
		this.hfxyfbzsjsec = hfxyfbzsjsec;
	}

	public String getHfyjzcsnum() {
		return hfyjzcsnum;
	}

	public void setHfyjzcsnum(String hfyjzcsnum) {
		this.hfyjzcsnum = hfyjzcsnum;
	}

	public String getHudegree() {
		return hudegree;
	}

	public void setHudegree(String hudegree) {
		this.hudegree = hudegree;
	}

	public int getHmaxyjcxhour() {
		return hmaxyjcxhour;
	}

	public void setHmaxyjcxhour(int hmaxyjcxhour) {
		this.hmaxyjcxhour = hmaxyjcxhour;
	}

	public int getHmaxyjcxminute() {
		return hmaxyjcxminute;
	}

	public void setHmaxyjcxminute(int hmaxyjcxminute) {
		this.hmaxyjcxminute = hmaxyjcxminute;
	}

	public int getHmaxyjcxsec() {
		return hmaxyjcxsec;
	}

	public void setHmaxyjcxsec(int hmaxyjcxsec) {
		this.hmaxyjcxsec = hmaxyjcxsec;
	}

	public int getHmaxyjfshour() {
		return hmaxyjfshour;
	}

	public void setHmaxyjfshour(int hmaxyjfshour) {
		this.hmaxyjfshour = hmaxyjfshour;
	}

	public int getHmaxyjfsminute() {
		return hmaxyjfsminute;
	}

	public void setHmaxyjfsminute(int hmaxyjfsminute) {
		this.hmaxyjfsminute = hmaxyjfsminute;
	}

	public int getHmaxyjfssec() {
		return hmaxyjfssec;
	}

	public void setHmaxyjfssec(int hmaxyjfssec) {
		this.hmaxyjfssec = hmaxyjfssec;
	}

	public int getHlongyjcxhour() {
		return hlongyjcxhour;
	}

	public void setHlongyjcxhour(int hlongyjcxhour) {
		this.hlongyjcxhour = hlongyjcxhour;
	}

	public int getHlongyjcxminute() {
		return hlongyjcxminute;
	}

	public void setHlongyjcxminute(int hlongyjcxminute) {
		this.hlongyjcxminute = hlongyjcxminute;
	}

	public int getHlongyjcxsec() {
		return hlongyjcxsec;
	}

	public void setHlongyjcxsec(int hlongyjcxsec) {
		this.hlongyjcxsec = hlongyjcxsec;
	}

	public int getHlongyjfshour() {
		return hlongyjfshour;
	}

	public void setHlongyjfshour(int hlongyjfshour) {
		this.hlongyjfshour = hlongyjfshour;
	}

	public int getHlongyjfsminute() {
		return hlongyjfsminute;
	}

	public void setHlongyjfsminute(int hlongyjfsminute) {
		this.hlongyjfsminute = hlongyjfsminute;
	}

	public int getHlongyjfssec() {
		return hlongyjfssec;
	}

	public void setHlongyjfssec(int hlongyjfssec) {
		this.hlongyjfssec = hlongyjfssec;
	}

	public String getHxywhzsIndex() {
		return hxywhzsIndex;
	}

	public void setHxywhzsIndex(String hxywhzsIndex) {
		this.hxywhzsIndex = hxywhzsIndex;
	}

	public int getHmaxyjcxtimes() {
		return hmaxyjcxtimes;
	}

	public void setHmaxyjcxtimes(int hmaxyjcxtimes) {
		this.hmaxyjcxtimes = hmaxyjcxtimes;
	}

	public int getHmaxyjfstimes() {
		return hmaxyjfstimes;
	}

	public void setHmaxyjfstimes(int hmaxyjfstimes) {
		this.hmaxyjfstimes = hmaxyjfstimes;
	}

	public int getHlongyjcxtimes() {
		return hlongyjcxtimes;
	}

	public void setHlongyjcxtimes(int hlongyjcxtimes) {
		this.hlongyjcxtimes = hlongyjcxtimes;
	}

	public int getHlongyjfstimes() {
		return hlongyjfstimes;
	}

	public void setHlongyjfstimes(int hlongyjfstimes) {
		this.hlongyjfstimes = hlongyjfstimes;
	}

	public int getHzchxzttimes() {
		return hzchxzttimes;
	}

	public void setHzchxzttimes(int hzchxzttimes) {
		this.hzchxzttimes = hzchxzttimes;
	}

	public int getHhxhappertimes() {
		return hhxhappertimes;
	}

	public void setHhxhappertimes(int hhxhappertimes) {
		this.hhxhappertimes = hhxhappertimes;
	}

	public int getHhxztzsjtimes() {
		return hhxztzsjtimes;
	}

	public void setHhxztzsjtimes(int hhxztzsjtimes) {
		this.hhxztzsjtimes = hhxztzsjtimes;
	}

	public int getHzcdtqtimes() {
		return hzcdtqtimes;
	}

	public void setHzcdtqtimes(int hzcdtqtimes) {
		this.hzcdtqtimes = hzcdtqtimes;
	}

	public int getHdtqhappertimes() {
		return hdtqhappertimes;
	}

	public void setHdtqhappertimes(int hdtqhappertimes) {
		this.hdtqhappertimes = hdtqhappertimes;
	}

	public int getHdtqzsjtimes() {
		return hdtqzsjtimes;
	}

	public void setHdtqzsjtimes(int hdtqzsjtimes) {
		this.hdtqzsjtimes = hdtqzsjtimes;
	}

	public int getHaxyfbzsjtimes() {
		return haxyfbzsjtimes;
	}

	public void setHaxyfbzsjtimes(int haxyfbzsjtimes) {
		this.haxyfbzsjtimes = haxyfbzsjtimes;
	}

	public int getHbxyfbzsjtimes() {
		return hbxyfbzsjtimes;
	}

	public void setHbxyfbzsjtimes(int hbxyfbzsjtimes) {
		this.hbxyfbzsjtimes = hbxyfbzsjtimes;
	}

	public int getHcxyfbzsjtimes() {
		return hcxyfbzsjtimes;
	}

	public void setHcxyfbzsjtimes(int hcxyfbzsjtimes) {
		this.hcxyfbzsjtimes = hcxyfbzsjtimes;
	}

	public int getHdxyfbzsjtimes() {
		return hdxyfbzsjtimes;
	}

	public void setHdxyfbzsjtimes(int hdxyfbzsjtimes) {
		this.hdxyfbzsjtimes = hdxyfbzsjtimes;
	}

	public int getHexyfbzsjtimes() {
		return hexyfbzsjtimes;
	}

	public void setHexyfbzsjtimes(int hexyfbzsjtimes) {
		this.hexyfbzsjtimes = hexyfbzsjtimes;
	}

	public int getHfxyfbzsjtimes() {
		return hfxyfbzsjtimes;
	}

	public void setHfxyfbzsjtimes(int hfxyfbzsjtimes) {
		this.hfxyfbzsjtimes = hfxyfbzsjtimes;
	}

	public String getHyjzsIndex() {
		return hyjzsIndex;
	}

	public void setHyjzsIndex(String hyjzsIndex) {
		this.hyjzsIndex = hyjzsIndex;
	}

	public int getTestdate() {
		return testdate;
	}

	public void setTestdate(int testdate) {
		this.testdate = testdate;
	}

	public String getReportuuid() {
		return reportuuid;
	}

	public void setReportuuid(String reportuuid) {
		this.reportuuid = reportuuid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getFilepaths() {
		return filepaths;
	}

	public void setFilepaths(String filepaths) {
		this.filepaths = filepaths;
	}

	public int getUploadfile() {
		return uploadfile;
	}

	public void setUploadfile(int uploadfile) {
		this.uploadfile = uploadfile;
	}

	public String getMacaddress() {
		return macaddress;
	}

	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}

	public String getReportfilepath() {
		return reportfilepath;
	}

	public void setReportfilepath(String reportfilepath) {
		this.reportfilepath = reportfilepath;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getHistoryupload() {
		return historyupload;
	}

	public int getIsComputed() {
		return isComputed;
	}

	public void setIsComputed(int isComputed) {
		this.isComputed = isComputed;
	}

	public void setHistoryupload(int historyupload) {
		this.historyupload = historyupload;
	}

	public int getIsFinalReport() {
		return isFinalReport;
	}

	public void setIsFinalReport(int isFinalReport) {
		this.isFinalReport = isFinalReport;
	}

	public int getCalculationflag() {
		return calculationflag;
	}

	public void setCalculationflag(int calculationflag) {
		this.calculationflag = calculationflag;
	}

}
