package com.sleep.local.classs;

import java.io.Serializable;

/**
 * 用户
 * 
 * @author magic
 * 
 */
/**
 * @author magic
 *
 */
public class UserManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String username;
	private String userage;
	private String usersex;
	private int userweight;
	private int userheight;
	private int userbmi;
	private String uuid;
	private int upload;
	private String responsiblephysician;
	private String mobilenumber;
	private String useremail;
	private String uhospitalname;

	public UserManager() {
		super();
	}

	
	public UserManager(int id, String username, String userage, String usersex, int userweight, int userheight,
			int userbmi, String uuid, int upload, String responsiblephysician, String mobilenumber, String useremail,
			String uhospitalname) {
		super();
		this.id = id;
		this.username = username;
		this.userage = userage;
		this.usersex = usersex;
		this.userweight = userweight;
		this.userheight = userheight;
		this.userbmi = userbmi;
		this.uuid = uuid;
		this.upload = upload;
		this.responsiblephysician = responsiblephysician;
		this.mobilenumber = mobilenumber;
		this.useremail = useremail;
		this.uhospitalname = uhospitalname;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserage() {
		return userage;
	}

	public void setUserage(String userage) {
		this.userage = userage;
	}

	public String getUsersex() {
		return usersex;
	}

	public void setUsersex(String usersex) {
		this.usersex = usersex;
	}

	public int getUserweight() {
		return userweight;
	}
	public void setUserweight(int userweight) {
		this.userweight = userweight;
	}

	public int getUserheight() {
		return userheight;
	}

	public void setUserheight(int userheight) {
		this.userheight = userheight;
	}

	public int getUserbmi() {
		return userbmi;
	}

	public void setUserbmi(int userbmi) {
		this.userbmi = userbmi;
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

	public String getResponsiblephysician() {
		return responsiblephysician;
	}

	public void setResponsiblephysician(String responsiblephysician) {
		this.responsiblephysician = responsiblephysician;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}


	public String getUhospitalname() {
		return uhospitalname;
	}

	public void setUhospitalname(String uhospitalname) {
		this.uhospitalname = uhospitalname;
	}

}
