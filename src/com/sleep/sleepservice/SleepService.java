package com.sleep.sleepservice;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sleep.local.classs.UserNumber;
import com.sleep.local.db.DbOpenHelper;

public class SleepService {
	private DbOpenHelper dbHelper;

	public SleepService(Context context) {
		super();
		this.dbHelper = new DbOpenHelper(context);
	}
	//登录
	public boolean login(String tel,String password){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from userlogin where usertel=? and password=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{tel,password});
		if (cursor.moveToFirst()==true) {
			cursor.close();
			return true;
		}
		return false;
	}
	
	//注册
	public boolean register(UserNumber se){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="insert into userlogin(usertel,password)values(?,?) ";
		Object[] obj={se.getUsertel(),se.getPassword()};
		sdb.execSQL(sql, obj);
		return true;
	}
	/**
	 * 注册时确定是否是新用户
	 * @param tel
	 * @return
	 */
	public boolean newregistersleep(String tel){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from userlogin where usertel="+tel;
		Cursor cursor=sdb.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			cursor.moveToFirst();
			cursor.close();
			return true;
		}
		cursor.moveToFirst();
		cursor.close();
		return false;
	}
	/***
	 *登录时确定是否是新用户
	 * 
	 * @param id
	 * @return
	 */
	public boolean newloginsleep(String usertel, String password) {
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from userlogin where usertel="+usertel+"password"+password;
		Cursor c =sdb.rawQuery(sql, null);
		if (c.moveToNext()) {
			c.moveToFirst();
			c.close();
			return true;
		}
		c.moveToFirst();
		c.close();
		return false;
	}
	/**
	 * 关闭数据库
	 */
	public void dbclose() {
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		sdb.close();
		dbHelper.close();
	}
	
}
























