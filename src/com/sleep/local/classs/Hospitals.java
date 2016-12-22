package com.sleep.local.classs;

import java.io.Serializable;


public class Hospitals implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String hospitalname;
	private String departmentname;
	private String doctorname;
	private String printerinformation;
	private String useruuid;
	private int userid;
	public Hospitals() {
		super();
	}
	
	public Hospitals(int id, String hospitalname, String departmentname,
			String doctorname, String printerinformation, String useruuid) {
		super();
		this.id = id;
		this.hospitalname = hospitalname;
		this.departmentname = departmentname;
		this.doctorname = doctorname;
		this.printerinformation = printerinformation;
		this.useruuid = useruuid;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}
	public String getDepartmentname() {
		return departmentname;
	}
	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}
	public String getDoctorname() {
		return doctorname;
	}
	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}
	public String getPrinterinformation() {
		return printerinformation;
	}
	public void setPrinterinformation(String printerinformation) {
		this.printerinformation = printerinformation;
	}
	public String getUseruuid() {
		return useruuid;
	}
	public void setUseruuid(String useruuid) {
		this.useruuid = useruuid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}
	
}
