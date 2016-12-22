package com.sleep.local.classs;

import java.io.Serializable;

/**
 * 睡眠
 * 
 * @author magic
 * 
 */
public class UserSleep implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	// 睡眠开始时长
	private int starthour, startminute;

	// 睡眠结束时长
	private int endhour, endminute;

	// 睡眠时长
	private int smschour, smscminute;

	// 浅睡时长
	private int qshour, qsminute;

	// 深睡时长
	private int sshour, ssminute;

	// 清醒时长
	private int qxhour, qxminute;

	// 最高脉率 次/分钟
	private String maxml;
	// 发生于 时 分 秒
	private int maxmlhour, maxmlminute, maxmlsec;

	// 最低脉率 次/分钟
	private String minml;
	// 发生于 时 分 秒
	private int minmlhour, minmlminute, minmlsec;
	// 平均脉率 次/分钟
	private String avgml;

	// 睡眠打分
	private int scoreDate;

	private String uuid;
	private int upload;
	private String doctoropinion;
	private String submintdate;

	private int userid;
	private String filepaths;
	private int uploadfile;
	private String macaddress;
	private String reportfilepath;

	/**
	 * Long类型时间
	 */
	private int starttimes;
	private int endtimes;
	private int smsctimes;
	private int qssctimes;
	private int sssctimes;
	private int qxsctimes;
	private int maxmltimes;
	private int minmltimes;

	public UserSleep() {
		super();
	}

	public UserSleep(int id, int starthour, int startminute, int endhour, int endminute, int smschour, int smscminute,
			int qshour, int qsminute, int sshour, int ssminute, int qxhour, int qxminute, String maxml, int maxmlhour,
			int maxmlminute, int maxmlsec, String minml, int minmlhour, int minmlminute, int minmlsec, String avgml,
			int scoreDate, String uuid, int upload, String doctoropinion, String submintdate, int userid,
			String filepaths, int uploadfile, String macaddress, String reportfilepath, int starttimes, int endtimes,
			int smsctimes, int qssctimes, int sssctimes, int qxsctimes, int maxmltimes, int minmltimes) {
		super();
		this.id = id;
		this.starthour = starthour;
		this.startminute = startminute;
		this.endhour = endhour;
		this.endminute = endminute;
		this.smschour = smschour;
		this.smscminute = smscminute;
		this.qshour = qshour;
		this.qsminute = qsminute;
		this.sshour = sshour;
		this.ssminute = ssminute;
		this.qxhour = qxhour;
		this.qxminute = qxminute;
		this.maxml = maxml;
		this.maxmlhour = maxmlhour;
		this.maxmlminute = maxmlminute;
		this.maxmlsec = maxmlsec;
		this.minml = minml;
		this.minmlhour = minmlhour;
		this.minmlminute = minmlminute;
		this.minmlsec = minmlsec;
		this.avgml = avgml;
		this.scoreDate = scoreDate;
		this.uuid = uuid;
		this.upload = upload;
		this.doctoropinion = doctoropinion;
		this.submintdate = submintdate;
		this.userid = userid;
		this.filepaths = filepaths;
		this.uploadfile = uploadfile;
		this.macaddress = macaddress;
		this.reportfilepath = reportfilepath;
		this.starttimes = starttimes;
		this.endtimes = endtimes;
		this.smsctimes = smsctimes;
		this.qssctimes = qssctimes;
		this.sssctimes = sssctimes;
		this.qxsctimes = qxsctimes;
		this.maxmltimes = maxmltimes;
		this.minmltimes = minmltimes;
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

	public int getStarthour() {
		return starthour;
	}

	public void setStarthour(int starthour) {
		this.starthour = starthour;
	}

	public int getStartminute() {
		return startminute;
	}

	public void setStartminute(int startminute) {
		this.startminute = startminute;
	}

	public int getEndhour() {
		return endhour;
	}

	public void setEndhour(int endhour) {
		this.endhour = endhour;
	}

	public int getEndminute() {
		return endminute;
	}

	public void setEndminute(int endminute) {
		this.endminute = endminute;
	}

	public int getSmschour() {
		return smschour;
	}

	public void setSmschour(int smschour) {
		this.smschour = smschour;
	}

	public int getSmscminute() {
		return smscminute;
	}

	public void setSmscminute(int smscminute) {
		this.smscminute = smscminute;
	}

	public int getQshour() {
		return qshour;
	}

	public void setQshour(int qshour) {
		this.qshour = qshour;
	}

	public int getQsminute() {
		return qsminute;
	}

	public void setQsminute(int qsminute) {
		this.qsminute = qsminute;
	}

	public int getSshour() {
		return sshour;
	}

	public void setSshour(int sshour) {
		this.sshour = sshour;
	}

	public int getSsminute() {
		return ssminute;
	}

	public void setSsminute(int ssminute) {
		this.ssminute = ssminute;
	}

	public int getQxhour() {
		return qxhour;
	}

	public void setQxhour(int qxhour) {
		this.qxhour = qxhour;
	}

	public int getQxminute() {
		return qxminute;
	}

	public void setQxminute(int qxminute) {
		this.qxminute = qxminute;
	}

	public int getScoreDate() {
		return scoreDate;
	}

	public void setScoreDate(int scoreDate) {
		this.scoreDate = scoreDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getMaxmlhour() {
		return maxmlhour;
	}

	public void setMaxmlhour(int maxmlhour) {
		this.maxmlhour = maxmlhour;
	}

	public int getMaxmlminute() {
		return maxmlminute;
	}

	public void setMaxmlminute(int maxmlminute) {
		this.maxmlminute = maxmlminute;
	}

	public int getMaxmlsec() {
		return maxmlsec;
	}

	public void setMaxmlsec(int maxmlsec) {
		this.maxmlsec = maxmlsec;
	}

	public String getAvgml() {
		return avgml;
	}

	public void setAvgml(String avgml) {
		this.avgml = avgml;
	}

	public String getMaxml() {
		return maxml;
	}

	public void setMaxml(String maxml) {
		this.maxml = maxml;
	}

	public String getMinml() {
		return minml;
	}

	public void setMinml(String minml) {
		this.minml = minml;
	}

	public int getMinmlhour() {
		return minmlhour;
	}

	public void setMinmlhour(int minmlhour) {
		this.minmlhour = minmlhour;
	}

	public int getMinmlminute() {
		return minmlminute;
	}

	public void setMinmlminute(int minmlminute) {
		this.minmlminute = minmlminute;
	}

	public int getMinmlsec() {
		return minmlsec;
	}

	public void setMinmlsec(int minmlsec) {
		this.minmlsec = minmlsec;
	}

	public int getStarttimes() {
		return starttimes;
	}

	public void setStarttimes(int starttimes) {
		this.starttimes = starttimes;
	}

	public int getEndtimes() {
		return endtimes;
	}

	public void setEndtimes(int endtimes) {
		this.endtimes = endtimes;
	}

	public int getSmsctimes() {
		return smsctimes;
	}

	public void setSmsctimes(int smsctimes) {
		this.smsctimes = smsctimes;
	}

	public int getQssctimes() {
		return qssctimes;
	}

	public void setQssctimes(int qssctimes) {
		this.qssctimes = qssctimes;
	}

	public int getSssctimes() {
		return sssctimes;
	}

	public void setSssctimes(int sssctimes) {
		this.sssctimes = sssctimes;
	}

	public int getQxsctimes() {
		return qxsctimes;
	}

	public void setQxsctimes(int qxsctimes) {
		this.qxsctimes = qxsctimes;
	}

	public int getMaxmltimes() {
		return maxmltimes;
	}

	public void setMaxmltimes(int maxmltimes) {
		this.maxmltimes = maxmltimes;
	}

	public int getMinmltimes() {
		return minmltimes;
	}

	public void setMinmltimes(int minmltimes) {
		this.minmltimes = minmltimes;
	}

	public String getDoctoropinion() {
		return doctoropinion;
	}

	public void setDoctoropinion(String doctoropinion) {
		this.doctoropinion = doctoropinion;
	}

	public String getSubmintdate() {
		return submintdate;
	}

	public void setSubmintdate(String submintdate) {
		this.submintdate = submintdate;
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

}
