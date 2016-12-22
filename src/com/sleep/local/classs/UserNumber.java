package com.sleep.local.classs;

import java.io.Serializable;

/**
 * 账户
 * 
 * @author magic
 * 
 */
public class UserNumber implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 个人信息
	private int id;
	private String usertel;
	private String password;
	private String uuid;
	private int upload;
	private String emails;
	private String token;
	private String sleepcareversion;

	public UserNumber() {
		super();
	}

	public UserNumber(int id, String usertel, String password, String uuid, int upload, String emails, String token,
			String sleepcareversion) {
		super();
		this.id = id;
		this.usertel = usertel;
		this.password = password;
		this.uuid = uuid;
		this.upload = upload;
		this.emails = emails;
		this.token = token;
		this.sleepcareversion = sleepcareversion;
	}

	public String getEmails() {
		return emails;
	}



	public void setEmails(String emails) {
		this.emails = emails;
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

	public String getUsertel() {
		return usertel;
	}

	public void setUsertel(String usertel) {
		this.usertel = usertel;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSleepcareversion() {
		return sleepcareversion;
	}

	public void setSleepcareversion(String sleepcareversion) {
		this.sleepcareversion = sleepcareversion;
	}

}
