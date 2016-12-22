package com.sleep.local.classs;

import java.io.Serializable;
/**
 * 问卷调查
 */
public class Epworth implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	//阅读
	private int satreading;
	//看电视
	private int watchtv;
	//不动
	private int satnotmove;
	//长时间不休息
	private int longnotrest;
	//坐着与人谈话
	private int satconversation;
	//饭后休息
	private int afterdinnerrest;
	//开车等红绿灯
	private int trafficlights;
	//静卧休息
	private int jingworest;
	//总分
	private int sumscore;
	private String uuid;
	private int upload;
	public Epworth() {
		super();
	}
	
	public Epworth(int id, int satreading, int watchtv, int satnotmove,
			int longnotrest, int satconversation, int afterdinnerrest,
			int trafficlights, int jingworest, int sumscore) {
		super();
		this.id = id;
		this.satreading = satreading;
		this.watchtv = watchtv;
		this.satnotmove = satnotmove;
		this.longnotrest = longnotrest;
		this.satconversation = satconversation;
		this.afterdinnerrest = afterdinnerrest;
		this.trafficlights = trafficlights;
		this.jingworest = jingworest;
		this.sumscore = sumscore;
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
	public int getSatreading() {
		return satreading;
	}
	public void setSatreading(int satreading) {
		this.satreading = satreading;
	}
	public int getWatchtv() {
		return watchtv;
	}
	public void setWatchtv(int watchtv) {
		this.watchtv = watchtv;
	}
	public int getSatnotmove() {
		return satnotmove;
	}
	public void setSatnotmove(int satnotmove) {
		this.satnotmove = satnotmove;
	}
	public int getLongnotrest() {
		return longnotrest;
	}
	public void setLongnotrest(int longnotrest) {
		this.longnotrest = longnotrest;
	}
	public int getSatconversation() {
		return satconversation;
	}
	public void setSatconversation(int satconversation) {
		this.satconversation = satconversation;
	}
	public int getTrafficlights() {
		return trafficlights;
	}
	public void setTrafficlights(int trafficlights) {
		this.trafficlights = trafficlights;
	}
	public int getJingworest() {
		return jingworest;
	}
	public void setJingworest(int jingworest) {
		this.jingworest = jingworest;
	}
	public int getAfterdinnerrest() {
		return afterdinnerrest;
	}
	public void setAfterdinnerrest(int afterdinnerrest) {
		this.afterdinnerrest = afterdinnerrest;
	}

	public int getSumscore() {
		return sumscore;
	}

	public void setSumscore(int sumscore) {
		this.sumscore = sumscore;
	}
	
	
}






























