package com.sleep.local.classs;

import java.io.Serializable;

/**
 * 指标
 * 
 * @author magic
 * 
 */
public class UserIndex implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 呼吸指标
	private int id;
	private String ahiIndex;
	private String hxztIndex;
	private String dtqIndex;
	private String yjzcsIndex;
	private String pjxybhdIndex;
	private String zdxybhdIndex;
	private String xybhdzbIndex;

	private int userid;
	private String filepaths;
	private int uploadfile;
	private String macaddress;
	private String reportfilepath;
	// 最大氧减发生时的血氧下降幅度
	private int odi4largestrange;

	// 呼吸打分
	private int scoreHxzb;
	// 平均血流灌注度
	private String avgxlgzd;
	// osahs
	private String osahsdegree;
	// 低氧血症
	private String dyxzdegree;
	/**
	 * 新增呼吸内容
	 */
	// 最长呼吸暂停时
	private int zchxzthour;
	// 最长呼吸暂停分
	private int zchxztminute;
	// 最长呼吸暂停秒
	private int zchxztsec;
	// 发生于时
	private int hxhapperhour;
	// 发生于分
	private int hxhapperminute;
	// 发生于秒
	private int hxhappersec;
	// "hxSpO2 integer," +
	// 呼吸暂停总时间时
	private int hxztzsjhour;
	// 呼吸暂停总时间分
	private int hxztzsjminute;
	// 呼吸暂停总时间秒
	private int hxztzsjsec;
	// 占总睡眠时间(%)
	// "hxzzsmsj varchar(20)," +
	// 呼吸暂停指数
	// "hxztzs varchar(20)," +

	/**
	 * 新增低通气内容
	 */
	// 最长低通气时
	private int zcdtqhour;
	// 最长低通气分
	private int zcdtqminute;
	// 最长低通气秒
	private int zcdtqsec;
	// 发生于时
	private int dtqhapperhour;
	// 发生于分
	private int dtqhapperminute;
	// 发生于秒
	private int dtqhappersec;
	// "dtqSpO2 integer," +
	// 低通气总时间时
	private int dtqzsjhour;
	// 低通气总时间分
	private int dtqzsjminute;
	// 低通气总时间秒
	private int dtqzsjsec;
	// 占总睡眠时间(%)
	// "dtqzsjzzsmsj varchar(20)," +
	// 低通气指数
	// "dtqzs varchar(20)," +

	/**
	 * 新增ODI内容
	 */
	// odi
	private String xyodi;
	// 醒时
	private String smsober;
	// 最高
	private String smmax;

	/**
	 * 血氧分布总时间
	 */
	/*
	 * a-代表(90%-100%)
	 */
	// a总时间时
	private int axyfbzsjhour;
	// a总时间分
	private int axyfbzsjminute;
	// a总时间秒
	private int axyfbzsjsec;
	// a-氧减总次数
	private String ayjzcsnum;

	/*
	 * b-代表(80%-89%)
	 */
	// b总时间时
	private int bxyfbzsjhour;
	// b总时间分
	private int bxyfbzsjminute;
	// b总时间秒
	private int bxyfbzsjsec;
	// b-氧减总次数
	private String byjzcsnum;

	/*
	 * c-代表(70%-79%)
	 */
	// c总时间时
	private int cxyfbzsjhour;
	// c总时间分
	private int cxyfbzsjminute;
	// c总时间秒
	private int cxyfbzsjsec;
	// c-氧减总次数
	private String cyjzcsnum;

	/*
	 * d-代表(60%-69%)
	 */
	// d总时间时
	private int dxyfbzsjhour;
	// d总时间分
	private int dxyfbzsjminute;
	// d总时间秒
	private int dxyfbzsjsec;
	// d-氧减总次数
	private String dyjzcsnum;

	/*
	 * e-代表(50%-59%)
	 */
	// e总时间时
	private int exyfbzsjhour;
	// e总时间分
	private int exyfbzsjminute;
	// e总时间秒
	private int exyfbzsjsec;
	// e-氧减总次数
	private String eyjzcsnum;

	/*
	 * f-代表(<50%)
	 */
	// f总时间时
	private int fxyfbzsjhour;
	// f总时间分
	private int fxyfbzsjminute;
	// f总时间秒
	private int fxyfbzsjsec;
	// f-氧减总次数
	private String fyjzcsnum;

	// osahs病情程度
	private String udegree;
	/**
	 * 氧降
	 */
	// 最大氧降次数
	// 最大氧降 持续时
	private int maxyjcxhour;
	// 最大氧降 持续分
	private int maxyjcxminute;
	// 最大氧降 持续秒
	private int maxyjcxsec;
	// 最大氧降 发生时
	private int maxyjfshour;
	// 最大氧降 发生分
	private int maxyjfsminute;
	// 最大氧降 发生秒
	private int maxyjfssec;
	// 最长氧降次数
	// 最长氧降 持续时
	private int longyjcxhour;
	// 最长氧降 持续分
	private int longyjcxminute;
	// 最长氧降 持续秒
	private int longyjcxsec;
	// 最长氧降 发生时
	private int longyjfshour;
	// 最长氧降 发生分
	private int longyjfsminute;
	// 最长氧降 发生秒
	private int longyjfssec;
	// 氧减危害指数
	private String xywhzsIndex;

	private String uuid;
	private int upload;

	/**
	 * Long类型时间
	 */
	private int maxyjcxtimes;
	private int maxyjfstimes;
	private int longyjcxtimes;
	private int longyjfstimes;
	private int zchxzttimes;
	private int hxhappertimes;
	private int hxztzsjtimes;
	private int zcdtqtimes;
	private int dtqhappertimes;
	private int dtqzsjtimes;
	private int axyfbzsjtimes;
	private int bxyfbzsjtimes;
	private int cxyfbzsjtimes;
	private int dxyfbzsjtimes;
	private int exyfbzsjtimes;
	private int fxyfbzsjtimes;

	public UserIndex() {
		super();
	}

	public UserIndex(int id, String ahiIndex, String hxztIndex, String dtqIndex, String yjzcsIndex, String pjxybhdIndex,
			String zdxybhdIndex, String xybhdzbIndex, int userid, String filepaths, int uploadfile, String macaddress,
			String reportfilepath, int odi4largestrange, int scoreHxzb, String avgxlgzd, String osahsdegree,
			String dyxzdegree, int zchxzthour, int zchxztminute, int zchxztsec, int hxhapperhour, int hxhapperminute,
			int hxhappersec, int hxztzsjhour, int hxztzsjminute, int hxztzsjsec, int zcdtqhour, int zcdtqminute,
			int zcdtqsec, int dtqhapperhour, int dtqhapperminute, int dtqhappersec, int dtqzsjhour, int dtqzsjminute,
			int dtqzsjsec, String xyodi, String smsober, String smmax, int axyfbzsjhour, int axyfbzsjminute,
			int axyfbzsjsec, String ayjzcsnum, int bxyfbzsjhour, int bxyfbzsjminute, int bxyfbzsjsec, String byjzcsnum,
			int cxyfbzsjhour, int cxyfbzsjminute, int cxyfbzsjsec, String cyjzcsnum, int dxyfbzsjhour,
			int dxyfbzsjminute, int dxyfbzsjsec, String dyjzcsnum, int exyfbzsjhour, int exyfbzsjminute,
			int exyfbzsjsec, String eyjzcsnum, int fxyfbzsjhour, int fxyfbzsjminute, int fxyfbzsjsec, String fyjzcsnum,
			String udegree, int maxyjcxhour, int maxyjcxminute, int maxyjcxsec, int maxyjfshour, int maxyjfsminute,
			int maxyjfssec, int longyjcxhour, int longyjcxminute, int longyjcxsec, int longyjfshour, int longyjfsminute,
			int longyjfssec, String xywhzsIndex, String uuid, int upload, int maxyjcxtimes, int maxyjfstimes,
			int longyjcxtimes, int longyjfstimes, int zchxzttimes, int hxhappertimes, int hxztzsjtimes, int zcdtqtimes,
			int dtqhappertimes, int dtqzsjtimes, int axyfbzsjtimes, int bxyfbzsjtimes, int cxyfbzsjtimes,
			int dxyfbzsjtimes, int exyfbzsjtimes, int fxyfbzsjtimes) {
		super();
		this.id = id;
		this.ahiIndex = ahiIndex;
		this.hxztIndex = hxztIndex;
		this.dtqIndex = dtqIndex;
		this.yjzcsIndex = yjzcsIndex;
		this.pjxybhdIndex = pjxybhdIndex;
		this.zdxybhdIndex = zdxybhdIndex;
		this.xybhdzbIndex = xybhdzbIndex;
		this.userid = userid;
		this.filepaths = filepaths;
		this.uploadfile = uploadfile;
		this.macaddress = macaddress;
		this.reportfilepath = reportfilepath;
		this.odi4largestrange = odi4largestrange;
		this.scoreHxzb = scoreHxzb;
		this.avgxlgzd = avgxlgzd;
		this.osahsdegree = osahsdegree;
		this.dyxzdegree = dyxzdegree;
		this.zchxzthour = zchxzthour;
		this.zchxztminute = zchxztminute;
		this.zchxztsec = zchxztsec;
		this.hxhapperhour = hxhapperhour;
		this.hxhapperminute = hxhapperminute;
		this.hxhappersec = hxhappersec;
		this.hxztzsjhour = hxztzsjhour;
		this.hxztzsjminute = hxztzsjminute;
		this.hxztzsjsec = hxztzsjsec;
		this.zcdtqhour = zcdtqhour;
		this.zcdtqminute = zcdtqminute;
		this.zcdtqsec = zcdtqsec;
		this.dtqhapperhour = dtqhapperhour;
		this.dtqhapperminute = dtqhapperminute;
		this.dtqhappersec = dtqhappersec;
		this.dtqzsjhour = dtqzsjhour;
		this.dtqzsjminute = dtqzsjminute;
		this.dtqzsjsec = dtqzsjsec;
		this.xyodi = xyodi;
		this.smsober = smsober;
		this.smmax = smmax;
		this.axyfbzsjhour = axyfbzsjhour;
		this.axyfbzsjminute = axyfbzsjminute;
		this.axyfbzsjsec = axyfbzsjsec;
		this.ayjzcsnum = ayjzcsnum;
		this.bxyfbzsjhour = bxyfbzsjhour;
		this.bxyfbzsjminute = bxyfbzsjminute;
		this.bxyfbzsjsec = bxyfbzsjsec;
		this.byjzcsnum = byjzcsnum;
		this.cxyfbzsjhour = cxyfbzsjhour;
		this.cxyfbzsjminute = cxyfbzsjminute;
		this.cxyfbzsjsec = cxyfbzsjsec;
		this.cyjzcsnum = cyjzcsnum;
		this.dxyfbzsjhour = dxyfbzsjhour;
		this.dxyfbzsjminute = dxyfbzsjminute;
		this.dxyfbzsjsec = dxyfbzsjsec;
		this.dyjzcsnum = dyjzcsnum;
		this.exyfbzsjhour = exyfbzsjhour;
		this.exyfbzsjminute = exyfbzsjminute;
		this.exyfbzsjsec = exyfbzsjsec;
		this.eyjzcsnum = eyjzcsnum;
		this.fxyfbzsjhour = fxyfbzsjhour;
		this.fxyfbzsjminute = fxyfbzsjminute;
		this.fxyfbzsjsec = fxyfbzsjsec;
		this.fyjzcsnum = fyjzcsnum;
		this.udegree = udegree;
		this.maxyjcxhour = maxyjcxhour;
		this.maxyjcxminute = maxyjcxminute;
		this.maxyjcxsec = maxyjcxsec;
		this.maxyjfshour = maxyjfshour;
		this.maxyjfsminute = maxyjfsminute;
		this.maxyjfssec = maxyjfssec;
		this.longyjcxhour = longyjcxhour;
		this.longyjcxminute = longyjcxminute;
		this.longyjcxsec = longyjcxsec;
		this.longyjfshour = longyjfshour;
		this.longyjfsminute = longyjfsminute;
		this.longyjfssec = longyjfssec;
		this.xywhzsIndex = xywhzsIndex;
		this.uuid = uuid;
		this.upload = upload;
		this.maxyjcxtimes = maxyjcxtimes;
		this.maxyjfstimes = maxyjfstimes;
		this.longyjcxtimes = longyjcxtimes;
		this.longyjfstimes = longyjfstimes;
		this.zchxzttimes = zchxzttimes;
		this.hxhappertimes = hxhappertimes;
		this.hxztzsjtimes = hxztzsjtimes;
		this.zcdtqtimes = zcdtqtimes;
		this.dtqhappertimes = dtqhappertimes;
		this.dtqzsjtimes = dtqzsjtimes;
		this.axyfbzsjtimes = axyfbzsjtimes;
		this.bxyfbzsjtimes = bxyfbzsjtimes;
		this.cxyfbzsjtimes = cxyfbzsjtimes;
		this.dxyfbzsjtimes = dxyfbzsjtimes;
		this.exyfbzsjtimes = exyfbzsjtimes;
		this.fxyfbzsjtimes = fxyfbzsjtimes;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getUpload() {
		return upload;
	}

	public void setUpload(int upload) {
		this.upload = upload;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAhiIndex() {
		return ahiIndex;
	}

	public void setAhiIndex(String ahiIndex) {
		this.ahiIndex = ahiIndex;
	}

	public String getHxztIndex() {
		return hxztIndex;
	}

	public void setHxztIndex(String hxztIndex) {
		this.hxztIndex = hxztIndex;
	}

	public String getDtqIndex() {
		return dtqIndex;
	}

	public void setDtqIndex(String dtqIndex) {
		this.dtqIndex = dtqIndex;
	}

	public String getPjxybhdIndex() {
		return pjxybhdIndex;
	}

	public void setPjxybhdIndex(String pjxybhdIndex) {
		this.pjxybhdIndex = pjxybhdIndex;
	}

	public String getZdxybhdIndex() {
		return zdxybhdIndex;
	}

	public void setZdxybhdIndex(String zdxybhdIndex) {
		this.zdxybhdIndex = zdxybhdIndex;
	}

	public String getXybhdzbIndex() {
		return xybhdzbIndex;
	}

	public void setXybhdzbIndex(String xybhdzbIndex) {
		this.xybhdzbIndex = xybhdzbIndex;
	}

	public int getScoreHxzb() {
		return scoreHxzb;
	}

	public void setScoreHxzb(int scoreHxzb) {
		this.scoreHxzb = scoreHxzb;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getYjzcsIndex() {
		return yjzcsIndex;
	}

	public void setYjzcsIndex(String yjzcsIndex) {
		this.yjzcsIndex = yjzcsIndex;
	}

	public int getZchxzthour() {
		return zchxzthour;
	}

	public void setZchxzthour(int zchxzthour) {
		this.zchxzthour = zchxzthour;
	}

	public int getZchxztminute() {
		return zchxztminute;
	}

	public void setZchxztminute(int zchxztminute) {
		this.zchxztminute = zchxztminute;
	}

	public int getZchxztsec() {
		return zchxztsec;
	}

	public void setZchxztsec(int zchxztsec) {
		this.zchxztsec = zchxztsec;
	}

	public int getHxhapperhour() {
		return hxhapperhour;
	}

	public void setHxhapperhour(int hxhapperhour) {
		this.hxhapperhour = hxhapperhour;
	}

	public int getHxhapperminute() {
		return hxhapperminute;
	}

	public void setHxhapperminute(int hxhapperminute) {
		this.hxhapperminute = hxhapperminute;
	}

	public int getHxhappersec() {
		return hxhappersec;
	}

	public void setHxhappersec(int hxhappersec) {
		this.hxhappersec = hxhappersec;
	}

	public int getHxztzsjhour() {
		return hxztzsjhour;
	}

	public void setHxztzsjhour(int hxztzsjhour) {
		this.hxztzsjhour = hxztzsjhour;
	}

	public int getHxztzsjminute() {
		return hxztzsjminute;
	}

	public void setHxztzsjminute(int hxztzsjminute) {
		this.hxztzsjminute = hxztzsjminute;
	}

	public int getHxztzsjsec() {
		return hxztzsjsec;
	}

	public void setHxztzsjsec(int hxztzsjsec) {
		this.hxztzsjsec = hxztzsjsec;
	}

	public int getZcdtqhour() {
		return zcdtqhour;
	}

	public void setZcdtqhour(int zcdtqhour) {
		this.zcdtqhour = zcdtqhour;
	}

	public int getZcdtqminute() {
		return zcdtqminute;
	}

	public void setZcdtqminute(int zcdtqminute) {
		this.zcdtqminute = zcdtqminute;
	}

	public int getZcdtqsec() {
		return zcdtqsec;
	}

	public void setZcdtqsec(int zcdtqsec) {
		this.zcdtqsec = zcdtqsec;
	}

	public int getDtqhapperhour() {
		return dtqhapperhour;
	}

	public void setDtqhapperhour(int dtqhapperhour) {
		this.dtqhapperhour = dtqhapperhour;
	}

	public int getDtqhapperminute() {
		return dtqhapperminute;
	}

	public void setDtqhapperminute(int dtqhapperminute) {
		this.dtqhapperminute = dtqhapperminute;
	}

	public int getDtqhappersec() {
		return dtqhappersec;
	}

	public void setDtqhappersec(int dtqhappersec) {
		this.dtqhappersec = dtqhappersec;
	}

	public int getDtqzsjhour() {
		return dtqzsjhour;
	}

	public void setDtqzsjhour(int dtqzsjhour) {
		this.dtqzsjhour = dtqzsjhour;
	}

	public int getDtqzsjminute() {
		return dtqzsjminute;
	}

	public void setDtqzsjminute(int dtqzsjminute) {
		this.dtqzsjminute = dtqzsjminute;
	}

	public int getDtqzsjsec() {
		return dtqzsjsec;
	}

	public void setDtqzsjsec(int dtqzsjsec) {
		this.dtqzsjsec = dtqzsjsec;
	}

	public String getSmsober() {
		return smsober;
	}

	public void setSmsober(String smsober) {
		this.smsober = smsober;
	}

	public String getSmmax() {
		return smmax;
	}

	public void setSmmax(String smmax) {
		this.smmax = smmax;
	}

	public int getAxyfbzsjhour() {
		return axyfbzsjhour;
	}

	public void setAxyfbzsjhour(int axyfbzsjhour) {
		this.axyfbzsjhour = axyfbzsjhour;
	}

	public int getAxyfbzsjminute() {
		return axyfbzsjminute;
	}

	public void setAxyfbzsjminute(int axyfbzsjminute) {
		this.axyfbzsjminute = axyfbzsjminute;
	}

	public int getAxyfbzsjsec() {
		return axyfbzsjsec;
	}

	public void setAxyfbzsjsec(int axyfbzsjsec) {
		this.axyfbzsjsec = axyfbzsjsec;
	}

	public String getAyjzcsnum() {
		return ayjzcsnum;
	}

	public void setAyjzcsnum(String ayjzcsnum) {
		this.ayjzcsnum = ayjzcsnum;
	}

	public int getBxyfbzsjhour() {
		return bxyfbzsjhour;
	}

	public void setBxyfbzsjhour(int bxyfbzsjhour) {
		this.bxyfbzsjhour = bxyfbzsjhour;
	}

	public int getBxyfbzsjminute() {
		return bxyfbzsjminute;
	}

	public void setBxyfbzsjminute(int bxyfbzsjminute) {
		this.bxyfbzsjminute = bxyfbzsjminute;
	}

	public int getBxyfbzsjsec() {
		return bxyfbzsjsec;
	}

	public void setBxyfbzsjsec(int bxyfbzsjsec) {
		this.bxyfbzsjsec = bxyfbzsjsec;
	}

	public String getByjzcsnum() {
		return byjzcsnum;
	}

	public void setByjzcsnum(String byjzcsnum) {
		this.byjzcsnum = byjzcsnum;
	}

	public int getCxyfbzsjhour() {
		return cxyfbzsjhour;
	}

	public void setCxyfbzsjhour(int cxyfbzsjhour) {
		this.cxyfbzsjhour = cxyfbzsjhour;
	}

	public int getCxyfbzsjminute() {
		return cxyfbzsjminute;
	}

	public void setCxyfbzsjminute(int cxyfbzsjminute) {
		this.cxyfbzsjminute = cxyfbzsjminute;
	}

	public int getCxyfbzsjsec() {
		return cxyfbzsjsec;
	}

	public void setCxyfbzsjsec(int cxyfbzsjsec) {
		this.cxyfbzsjsec = cxyfbzsjsec;
	}

	public String getCyjzcsnum() {
		return cyjzcsnum;
	}

	public void setCyjzcsnum(String cyjzcsnum) {
		this.cyjzcsnum = cyjzcsnum;
	}

	public int getDxyfbzsjhour() {
		return dxyfbzsjhour;
	}

	public void setDxyfbzsjhour(int dxyfbzsjhour) {
		this.dxyfbzsjhour = dxyfbzsjhour;
	}

	public int getDxyfbzsjminute() {
		return dxyfbzsjminute;
	}

	public void setDxyfbzsjminute(int dxyfbzsjminute) {
		this.dxyfbzsjminute = dxyfbzsjminute;
	}

	public int getDxyfbzsjsec() {
		return dxyfbzsjsec;
	}

	public void setDxyfbzsjsec(int dxyfbzsjsec) {
		this.dxyfbzsjsec = dxyfbzsjsec;
	}

	public String getDyjzcsnum() {
		return dyjzcsnum;
	}

	public void setDyjzcsnum(String dyjzcsnum) {
		this.dyjzcsnum = dyjzcsnum;
	}

	public int getExyfbzsjhour() {
		return exyfbzsjhour;
	}

	public void setExyfbzsjhour(int exyfbzsjhour) {
		this.exyfbzsjhour = exyfbzsjhour;
	}

	public int getExyfbzsjminute() {
		return exyfbzsjminute;
	}

	public void setExyfbzsjminute(int exyfbzsjminute) {
		this.exyfbzsjminute = exyfbzsjminute;
	}

	public int getExyfbzsjsec() {
		return exyfbzsjsec;
	}

	public void setExyfbzsjsec(int exyfbzsjsec) {
		this.exyfbzsjsec = exyfbzsjsec;
	}

	public String getEyjzcsnum() {
		return eyjzcsnum;
	}

	public void setEyjzcsnum(String eyjzcsnum) {
		this.eyjzcsnum = eyjzcsnum;
	}

	public int getFxyfbzsjhour() {
		return fxyfbzsjhour;
	}

	public void setFxyfbzsjhour(int fxyfbzsjhour) {
		this.fxyfbzsjhour = fxyfbzsjhour;
	}

	public int getFxyfbzsjminute() {
		return fxyfbzsjminute;
	}

	public void setFxyfbzsjminute(int fxyfbzsjminute) {
		this.fxyfbzsjminute = fxyfbzsjminute;
	}

	public int getFxyfbzsjsec() {
		return fxyfbzsjsec;
	}

	public void setFxyfbzsjsec(int fxyfbzsjsec) {
		this.fxyfbzsjsec = fxyfbzsjsec;
	}

	public String getFyjzcsnum() {
		return fyjzcsnum;
	}

	public void setFyjzcsnum(String fyjzcsnum) {
		this.fyjzcsnum = fyjzcsnum;
	}

	public String getXyodi() {
		return xyodi;
	}

	public void setXyodi(String xyodi) {
		this.xyodi = xyodi;
	}

	public String getUdegree() {
		return udegree;
	}

	public void setUdegree(String udegree) {
		this.udegree = udegree;
	}

	public int getMaxyjcxhour() {
		return maxyjcxhour;
	}

	public void setMaxyjcxhour(int maxyjcxhour) {
		this.maxyjcxhour = maxyjcxhour;
	}

	public int getMaxyjcxminute() {
		return maxyjcxminute;
	}

	public void setMaxyjcxminute(int maxyjcxminute) {
		this.maxyjcxminute = maxyjcxminute;
	}

	public int getMaxyjcxsec() {
		return maxyjcxsec;
	}

	public void setMaxyjcxsec(int maxyjcxsec) {
		this.maxyjcxsec = maxyjcxsec;
	}

	public int getMaxyjfshour() {
		return maxyjfshour;
	}

	public void setMaxyjfshour(int maxyjfshour) {
		this.maxyjfshour = maxyjfshour;
	}

	public int getMaxyjfsminute() {
		return maxyjfsminute;
	}

	public void setMaxyjfsminute(int maxyjfsminute) {
		this.maxyjfsminute = maxyjfsminute;
	}

	public int getMaxyjfssec() {
		return maxyjfssec;
	}

	public void setMaxyjfssec(int maxyjfssec) {
		this.maxyjfssec = maxyjfssec;
	}

	public int getLongyjcxhour() {
		return longyjcxhour;
	}

	public void setLongyjcxhour(int longyjcxhour) {
		this.longyjcxhour = longyjcxhour;
	}

	public int getLongyjcxminute() {
		return longyjcxminute;
	}

	public void setLongyjcxminute(int longyjcxminute) {
		this.longyjcxminute = longyjcxminute;
	}

	public int getLongyjcxsec() {
		return longyjcxsec;
	}

	public void setLongyjcxsec(int longyjcxsec) {
		this.longyjcxsec = longyjcxsec;
	}

	public int getLongyjfshour() {
		return longyjfshour;
	}

	public void setLongyjfshour(int longyjfshour) {
		this.longyjfshour = longyjfshour;
	}

	public int getLongyjfsminute() {
		return longyjfsminute;
	}

	public void setLongyjfsminute(int longyjfsminute) {
		this.longyjfsminute = longyjfsminute;
	}

	public int getLongyjfssec() {
		return longyjfssec;
	}

	public void setLongyjfssec(int longyjfssec) {
		this.longyjfssec = longyjfssec;
	}

	public String getXywhzsIndex() {
		return xywhzsIndex;
	}

	public void setXywhzsIndex(String xywhzsIndex) {
		this.xywhzsIndex = xywhzsIndex;
	}

	public String getAvgxlgzd() {
		return avgxlgzd;
	}

	public void setAvgxlgzd(String avgxlgzd) {
		this.avgxlgzd = avgxlgzd;
	}

	public String getOsahsdegree() {
		return osahsdegree;
	}

	public void setOsahsdegree(String osahsdegree) {
		this.osahsdegree = osahsdegree;
	}

	public String getDyxzdegree() {
		return dyxzdegree;
	}

	public void setDyxzdegree(String dyxzdegree) {
		this.dyxzdegree = dyxzdegree;
	}

	public int getMaxyjcxtimes() {
		return maxyjcxtimes;
	}

	public void setMaxyjcxtimes(int maxyjcxtimes) {
		this.maxyjcxtimes = maxyjcxtimes;
	}

	public int getMaxyjfstimes() {
		return maxyjfstimes;
	}

	public void setMaxyjfstimes(int maxyjfstimes) {
		this.maxyjfstimes = maxyjfstimes;
	}

	public int getLongyjcxtimes() {
		return longyjcxtimes;
	}

	public void setLongyjcxtimes(int longyjcxtimes) {
		this.longyjcxtimes = longyjcxtimes;
	}

	public int getLongyjfstimes() {
		return longyjfstimes;
	}

	public void setLongyjfstimes(int longyjfstimes) {
		this.longyjfstimes = longyjfstimes;
	}

	public int getZchxzttimes() {
		return zchxzttimes;
	}

	public void setZchxzttimes(int zchxzttimes) {
		this.zchxzttimes = zchxzttimes;
	}

	public int getHxhappertimes() {
		return hxhappertimes;
	}

	public void setHxhappertimes(int hxhappertimes) {
		this.hxhappertimes = hxhappertimes;
	}

	public int getHxztzsjtimes() {
		return hxztzsjtimes;
	}

	public void setHxztzsjtimes(int hxztzsjtimes) {
		this.hxztzsjtimes = hxztzsjtimes;
	}

	public int getZcdtqtimes() {
		return zcdtqtimes;
	}

	public void setZcdtqtimes(int zcdtqtimes) {
		this.zcdtqtimes = zcdtqtimes;
	}

	public int getDtqhappertimes() {
		return dtqhappertimes;
	}

	public void setDtqhappertimes(int dtqhappertimes) {
		this.dtqhappertimes = dtqhappertimes;
	}

	public int getDtqzsjtimes() {
		return dtqzsjtimes;
	}

	public void setDtqzsjtimes(int dtqzsjtimes) {
		this.dtqzsjtimes = dtqzsjtimes;
	}

	public int getAxyfbzsjtimes() {
		return axyfbzsjtimes;
	}

	public void setAxyfbzsjtimes(int axyfbzsjtimes) {
		this.axyfbzsjtimes = axyfbzsjtimes;
	}

	public int getBxyfbzsjtimes() {
		return bxyfbzsjtimes;
	}

	public void setBxyfbzsjtimes(int bxyfbzsjtimes) {
		this.bxyfbzsjtimes = bxyfbzsjtimes;
	}

	public int getCxyfbzsjtimes() {
		return cxyfbzsjtimes;
	}

	public void setCxyfbzsjtimes(int cxyfbzsjtimes) {
		this.cxyfbzsjtimes = cxyfbzsjtimes;
	}

	public int getDxyfbzsjtimes() {
		return dxyfbzsjtimes;
	}

	public void setDxyfbzsjtimes(int dxyfbzsjtimes) {
		this.dxyfbzsjtimes = dxyfbzsjtimes;
	}

	public int getExyfbzsjtimes() {
		return exyfbzsjtimes;
	}

	public void setExyfbzsjtimes(int exyfbzsjtimes) {
		this.exyfbzsjtimes = exyfbzsjtimes;
	}

	public int getFxyfbzsjtimes() {
		return fxyfbzsjtimes;
	}

	public void setFxyfbzsjtimes(int fxyfbzsjtimes) {
		this.fxyfbzsjtimes = fxyfbzsjtimes;
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

	public int getOdi4largestrange() {
		return odi4largestrange;
	}

	public void setOdi4largestrange(int odi4largestrange) {
		this.odi4largestrange = odi4largestrange;
	}

}
