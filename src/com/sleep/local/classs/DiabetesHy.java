package com.sleep.local.classs;

import java.io.Serializable;

public class DiabetesHy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 病史
	 */
	private int id;
	//失眠
	private String losesleep;
	//糖尿病
	private String diabetes;
	//高血压
	private String hypertension;
	//冠心病
	private String coronaryheartdisease;
	//心力衰竭
	private String heartfailure;
	//心律失常
	private String arrhythmia;
	//鼻腔阻塞
	private String congestion;
	//长期吸烟
	private String longsmoking;
	//脑血管疾病
	private String cerebrovasculardisease;
	//肾功能损害
	private String renalfailure;
	//服用镇静剂
	private String takesedatives;
	//长期大量饮酒
	private String longdrinking;
	//是否绝经
	private String whetherjjm;
	//是否有OSAHS的家族史
	private String whetherfmhy;
	// 是否悬雍垂粗大
	private String whetherxyccd;
	
	private String uuid;
	private int upload;
	public DiabetesHy() {
		super();
	}



	public DiabetesHy(int id, String losesleep, String diabetes,
			String hypertension, String coronaryheartdisease,
			String heartfailure, String arrhythmia, String congestion,
			String longsmoking, String cerebrovasculardisease,
			String renalfailure, String takesedatives, String longdrinking,
			String whetherjjm, String whetherfmhy, String whetherxyccd) {
		super();
		this.id = id;
		this.losesleep = losesleep;
		this.diabetes = diabetes;
		this.hypertension = hypertension;
		this.coronaryheartdisease = coronaryheartdisease;
		this.heartfailure = heartfailure;
		this.arrhythmia = arrhythmia;
		this.congestion = congestion;
		this.longsmoking = longsmoking;
		this.cerebrovasculardisease = cerebrovasculardisease;
		this.renalfailure = renalfailure;
		this.takesedatives = takesedatives;
		this.longdrinking = longdrinking;
		this.whetherjjm = whetherjjm;
		this.whetherfmhy = whetherfmhy;
		this.whetherxyccd = whetherxyccd;
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

	public String getLosesleep() {
		return losesleep;
	}
	public void setLosesleep(String losesleep) {
		this.losesleep = losesleep;
	}
	public String getDiabetes() {
		return diabetes;
	}
	public void setDiabetes(String diabetes) {
		this.diabetes = diabetes;
	}
	public String getHypertension() {
		return hypertension;
	}
	public void setHypertension(String hypertension) {
		this.hypertension = hypertension;
	}
	public String getCoronaryheartdisease() {
		return coronaryheartdisease;
	}
	public void setCoronaryheartdisease(String coronaryheartdisease) {
		this.coronaryheartdisease = coronaryheartdisease;
	}
	public String getHeartfailure() {
		return heartfailure;
	}
	public void setHeartfailure(String heartfailure) {
		this.heartfailure = heartfailure;
	}
	public String getArrhythmia() {
		return arrhythmia;
	}
	public void setArrhythmia(String arrhythmia) {
		this.arrhythmia = arrhythmia;
	}
	public String getCongestion() {
		return congestion;
	}
	public void setCongestion(String congestion) {
		this.congestion = congestion;
	}
	public String getLongsmoking() {
		return longsmoking;
	}
	public void setLongsmoking(String longsmoking) {
		this.longsmoking = longsmoking;
	}
	public String getCerebrovasculardisease() {
		return cerebrovasculardisease;
	}
	public void setCerebrovasculardisease(String cerebrovasculardisease) {
		this.cerebrovasculardisease = cerebrovasculardisease;
	}
	public String getRenalfailure() {
		return renalfailure;
	}
	public void setRenalfailure(String renalfailure) {
		this.renalfailure = renalfailure;
	}
	public String getTakesedatives() {
		return takesedatives;
	}
	public void setTakesedatives(String takesedatives) {
		this.takesedatives = takesedatives;
	}
	public String getLongdrinking() {
		return longdrinking;
	}
	public void setLongdrinking(String longdrinking) {
		this.longdrinking = longdrinking;
	}


	public String getWhetherjjm() {
		return whetherjjm;
	}


	public void setWhetherjjm(String whetherjjm) {
		this.whetherjjm = whetherjjm;
	}


	public String getWhetherfmhy() {
		return whetherfmhy;
	}


	public void setWhetherfmhy(String whetherfmhy) {
		this.whetherfmhy = whetherfmhy;
	}



	public String getWhetherxyccd() {
		return whetherxyccd;
	}



	public void setWhetherxyccd(String whetherxyccd) {
		this.whetherxyccd = whetherxyccd;
	}


	
	
}
