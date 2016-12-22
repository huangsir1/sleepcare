package com.sleep.utils;
public class Statuss {
	private String message = "no info";
	public final static Statuss SUCCESSED = new Statuss(2000, "成功");
	public final static Statuss FAILED = new Statuss(5000, "未知错误");
	
	public final static int SUCCESSED_CODE = 2000;
	public final static int FAILED_CODE = 5000;
	
	public final static Statuss USER_UNREGISTER = new Statuss(5001, "用户未注册");
	public final static Statuss USER_EXIST= new Statuss(5002, "用户已存在");
	public final static Statuss PARAM_ERROR= new Statuss(5003, "参数错误");
	public final static Statuss USER_REGISTER_SUCCESS = new Statuss(5200, "用户注册成功");
	public final static Statuss REPORT_EXSIT = new Statuss(6001, "报告已存在");

	public int code;

	public Statuss() {

	}

	public Statuss(int code) {
		this.code = code;
	}

	public Statuss(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	/* 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Statuss [message=" + message + ", code=" + code + "]";
	}

	
}
