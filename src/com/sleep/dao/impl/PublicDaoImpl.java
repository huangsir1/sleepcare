package com.sleep.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.sleep.dao.PublicDao;
import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.Hospitals;
import com.sleep.local.classs.UserHistory;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserNumber;
import com.sleep.local.db.DbOpenHelper;
import com.sleep.utils.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PublicDaoImpl implements PublicDao {
	private DbOpenHelper dbOpenHelper;

	public PublicDaoImpl(Context context) {
		super();
		this.dbOpenHelper = new DbOpenHelper(context);
	}

	/**
	 * 注册手机号
	 */
	@Override
	public boolean addUserLogin(UserNumber sleep) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("usertel", sleep.getUsertel());
		values.put("emails", sleep.getEmails());
		values.put("password", sleep.getPassword());
		values.put("token", sleep.getToken());
		values.put("sleepcareversion", sleep.getSleepcareversion());
		long id1 = db.insert("userlogin", null, values);
		if (id1 != 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * 增加用户
	 */
	@Override
	public boolean addUser(UserManager sleep) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", sleep.getUsername());
		values.put("userage", sleep.getUserage());
		values.put("usersex", sleep.getUsersex());

		values.put("responsiblephysician", sleep.getResponsiblephysician());
		values.put("mobilenumber", sleep.getMobilenumber());
		values.put("useremail", sleep.getUseremail());
		values.put("uhospitalname", sleep.getUhospitalname());

		values.put("userweight", sleep.getUserweight());
		values.put("userheight", sleep.getUserheight());
		values.put("userbmi", sleep.getUserbmi());
		values.put("useruuid", sleep.getUuid());
		values.put("userupload", sleep.getUpload());
		long id1 = db.insert("usermanager", null, values);
		if (id1 != 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * 修改账户
	 */
	@Override
	public boolean modifyusernumber(UserNumber sleep) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("usertel", sleep.getUsertel());
		values.put("emails", sleep.getEmails());

		values.put("password", sleep.getPassword());
		values.put("token", sleep.getToken());
		values.put("sleepcareversion", sleep.getSleepcareversion());

		int row = db.update("userlogin", values, "usertel=?", new String[] { sleep.getUsertel() });
		if (row > 0) {
			System.out.println("成功修改了" + row + "条数据");
			flag = true;
		} else {
			System.out.println("修改失败!");
		}
		db.close();
		return flag;
	}

	// 查询usernumber
	@Override
	public UserNumber findTelorEmail() {
		UserNumber un = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from userlogin";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			un = new UserNumber();
			un.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			un.setUsertel(cursor.getString(cursor.getColumnIndex("usertel")));
			un.setEmails(cursor.getString(cursor.getColumnIndex("emails")));
			un.setPassword(cursor.getString(cursor.getColumnIndex("password")));
			un.setToken(cursor.getString(cursor.getColumnIndex("token")));
			un.setSleepcareversion(cursor.getString(cursor.getColumnIndex("sleepcareversion")));
		}
		db.close();
		return un;
	}

	/**
	 * 修改用户信息
	 */
	@Override
	public boolean modifyusermanager(UserManager sleep) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("username", sleep.getUsername());
		values.put("userage", sleep.getUserage());
		values.put("usersex", sleep.getUsersex());
		values.put("responsiblephysician", sleep.getResponsiblephysician());
		values.put("mobilenumber", sleep.getMobilenumber());
		values.put("useremail", sleep.getUseremail());
		values.put("userweight", sleep.getUserweight());
		values.put("userheight", sleep.getUserheight());
		values.put("userbmi", sleep.getUserbmi());
		values.put("uhospitalname", sleep.getUhospitalname());

		values.put("useruuid", sleep.getUuid());
		values.put("userupload", sleep.getUpload());

		int row = db.update("usermanager", values, "_id=?", new String[] { String.valueOf(sleep.getId()) });
		if (row > 0) {
			System.out.println("成功修改了" + row + "条数据");
			flag = true;
		} else {
			System.out.println("修改失败!");
		}
		db.close();
		return flag;
	}

	/**
	 * 删除用户
	 */
	@Override
	public boolean removeUser(int id) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int row = db.delete("usermanager", "_id=?", new String[] { String.valueOf(id) });
		if (row > 0) {
			System.out.println("成功删除了" + row + "条数据");
			Log.i("TAG", "成功删除了" + row + "条数据");
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * 用户查询
	 */
	@Override
	public List<UserManager> findAllUsers() {
		List<UserManager> all = new ArrayList<UserManager>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from usermanager order by _id desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			UserManager sleep = new UserManager();
			sleep.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			sleep.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			sleep.setUserage(cursor.getString(cursor.getColumnIndex("userage")));
			sleep.setUsersex(cursor.getString(cursor.getColumnIndex("usersex")));
			sleep.setResponsiblephysician(cursor.getString(cursor.getColumnIndex("responsiblephysician")));
			sleep.setMobilenumber(cursor.getString(cursor.getColumnIndex("mobilenumber")));
			sleep.setUseremail(cursor.getString(cursor.getColumnIndex("useremail")));
			sleep.setUserweight(cursor.getInt(cursor.getColumnIndex("userweight")));
			sleep.setUserheight(cursor.getInt(cursor.getColumnIndex("userheight")));
			sleep.setUhospitalname(cursor.getString(cursor.getColumnIndex("uhospitalname")));

			sleep.setUserbmi(cursor.getInt(cursor.getColumnIndex("userbmi")));
			sleep.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			sleep.setUpload(cursor.getInt(cursor.getColumnIndex("userupload")));

			all.add(sleep);
		}
		db.close();
		return all;
	}

	/**
	 * 用户id查询
	 */
	@Override
	public UserManager findAllUserById(int id) {
		UserManager sleep = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from usermanager where _id=?";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(id) });
		while (cursor.moveToNext()) {
			sleep = new UserManager();
			sleep.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			sleep.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			sleep.setUserage(cursor.getString(cursor.getColumnIndex("userage")));
			sleep.setUsersex(cursor.getString(cursor.getColumnIndex("usersex")));
			sleep.setUserweight(cursor.getInt(cursor.getColumnIndex("userweight")));
			sleep.setResponsiblephysician(cursor.getString(cursor.getColumnIndex("responsiblephysician")));
			sleep.setMobilenumber(cursor.getString(cursor.getColumnIndex("mobilenumber")));
			sleep.setUseremail(cursor.getString(cursor.getColumnIndex("useremail")));
			sleep.setUserheight(cursor.getInt(cursor.getColumnIndex("userheight")));
			sleep.setUhospitalname(cursor.getString(cursor.getColumnIndex("uhospitalname")));
			sleep.setUserbmi(cursor.getInt(cursor.getColumnIndex("userbmi")));
			sleep.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			sleep.setUpload(cursor.getInt(cursor.getColumnIndex("userupload")));
		}
		db.close();
		return sleep;
	}

	/**
	 * 用户name查询
	 */
	@Override
	public UserManager findAllUserByName(String name) {
		UserManager sleep = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from usermanager where username=?";
		Cursor cursor = db.rawQuery(sql, new String[] { name });
		while (cursor.moveToNext()) {
			sleep = new UserManager();
			sleep.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			sleep.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			sleep.setUserage(cursor.getString(cursor.getColumnIndex("userage")));
			sleep.setUsersex(cursor.getString(cursor.getColumnIndex("usersex")));
			sleep.setUserweight(cursor.getInt(cursor.getColumnIndex("userweight")));
			sleep.setResponsiblephysician(cursor.getString(cursor.getColumnIndex("responsiblephysician")));
			sleep.setMobilenumber(cursor.getString(cursor.getColumnIndex("mobilenumber")));
			sleep.setUseremail(cursor.getString(cursor.getColumnIndex("useremail")));
			sleep.setUserheight(cursor.getInt(cursor.getColumnIndex("userheight")));
			sleep.setUhospitalname(cursor.getString(cursor.getColumnIndex("uhospitalname")));
			sleep.setUserbmi(cursor.getInt(cursor.getColumnIndex("userbmi")));
			sleep.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			sleep.setUpload(cursor.getInt(cursor.getColumnIndex("userupload")));
		}
		db.close();
		return sleep;
	}

	/**
	 * 增加问卷调查
	 */
	@Override
	public boolean addEpworth(Epworth epworth) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		// 阅读
		values.put("satreading", epworth.getSatreading());
		// 看电视
		values.put("watchtv", epworth.getWatchtv());
		// 不动
		values.put("satnotmove", epworth.getSatnotmove());
		// 长时间不休息
		values.put("longnotrest", epworth.getLongnotrest());
		// 坐着与人谈话
		values.put("satconversation", epworth.getSatconversation());
		// 饭后休息
		values.put("afterdinnerrest", epworth.getAfterdinnerrest());
		// 开车等红绿灯
		values.put("trafficlights", epworth.getTrafficlights());
		// 静卧休息
		values.put("jingworest", epworth.getJingworest());
		// 总分
		values.put("sumscore", epworth.getSumscore());

		values.put("useruuid", epworth.getUuid());
		values.put("userupload", epworth.getUpload());

		long id1 = db.insert("epworth", null, values);
		if (id1 != 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * 修改问卷调查
	 */
	@Override
	public boolean modifyepworth(Epworth epworth) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		// 阅读
		values.put("satreading", epworth.getSatreading());
		// 看电视
		values.put("watchtv", epworth.getWatchtv());
		// 不动
		values.put("satnotmove", epworth.getSatnotmove());
		// 长时间不休息
		values.put("longnotrest", epworth.getLongnotrest());
		// 坐着与人谈话
		values.put("satconversation", epworth.getSatconversation());
		// 饭后休息
		values.put("afterdinnerrest", epworth.getAfterdinnerrest());
		// 开车等红绿灯
		values.put("trafficlights", epworth.getTrafficlights());
		// 静卧休息
		values.put("jingworest", epworth.getJingworest());
		// 总分
		values.put("sumscore", epworth.getSumscore());

		values.put("useruuid", epworth.getUuid());
		values.put("userupload", epworth.getUpload());

		int row = db.update("epworth", values, "_id=?", new String[] { String.valueOf(epworth.getId()) });
		if (row > 0) {
			System.out.println("成功修改了" + row + "条数据");
			flag = true;
		} else {
			System.out.println("修改失败!");
		}
		db.close();
		return flag;
	}

	/**
	 * 查询问卷调查集合
	 */
	@Override
	public List<Epworth> findAllEpworth() {
		List<Epworth> all = new ArrayList<Epworth>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from epworth";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			Epworth epworth = new Epworth();
			epworth.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			epworth.setSatreading(cursor.getInt(cursor.getColumnIndex("satreading")));
			epworth.setWatchtv(cursor.getInt(cursor.getColumnIndex("watchtv")));
			epworth.setSatnotmove(cursor.getInt(cursor.getColumnIndex("satnotmove")));
			epworth.setLongnotrest(cursor.getInt(cursor.getColumnIndex("longnotrest")));
			epworth.setSatconversation(cursor.getInt(cursor.getColumnIndex("satconversation")));
			epworth.setAfterdinnerrest(cursor.getInt(cursor.getColumnIndex("afterdinnerrest")));
			epworth.setTrafficlights(cursor.getInt(cursor.getColumnIndex("trafficlights")));
			epworth.setJingworest(cursor.getInt(cursor.getColumnIndex("jingworest")));
			epworth.setSumscore(cursor.getInt(cursor.getColumnIndex("sumscore")));

			epworth.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			epworth.setUpload(cursor.getInt(cursor.getColumnIndex("userupload")));

			all.add(epworth);
		}
		db.close();
		return all;
	}

	/**
	 * 按id查询问卷调查
	 */

	@Override
	public Epworth findAllEpworthById(int id) {
		Epworth epworth = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from epworth where _id=?";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(id) });
		while (cursor.moveToNext()) {
			epworth = new Epworth();
			epworth.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			epworth.setSatreading(cursor.getInt(cursor.getColumnIndex("satreading")));
			epworth.setWatchtv(cursor.getInt(cursor.getColumnIndex("watchtv")));
			epworth.setSatnotmove(cursor.getInt(cursor.getColumnIndex("satnotmove")));
			epworth.setLongnotrest(cursor.getInt(cursor.getColumnIndex("longnotrest")));
			epworth.setSatconversation(cursor.getInt(cursor.getColumnIndex("satconversation")));
			epworth.setAfterdinnerrest(cursor.getInt(cursor.getColumnIndex("afterdinnerrest")));
			epworth.setTrafficlights(cursor.getInt(cursor.getColumnIndex("trafficlights")));
			epworth.setJingworest(cursor.getInt(cursor.getColumnIndex("jingworest")));
			epworth.setSumscore(cursor.getInt(cursor.getColumnIndex("sumscore")));

			epworth.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			epworth.setUpload(cursor.getInt(cursor.getColumnIndex("userupload")));
		}
		db.close();
		return epworth;
	}

	// 增加病史
	@Override
	public boolean addDiabetesHy(DiabetesHy diabeteshy) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		/*
		 * 病史
		 */
		// 失眠
		values.put("losesleep", diabeteshy.getLosesleep());
		// 糖尿病
		values.put("diabetes", diabeteshy.getDiabetes());
		// 高血压
		values.put("hypertension", diabeteshy.getHypertension());
		// 冠心病
		values.put("coronaryheartdisease", diabeteshy.getCoronaryheartdisease());
		// 心力衰竭
		values.put("heartfailure", diabeteshy.getHeartfailure());
		// 心律失常
		values.put("arrhythmia", diabeteshy.getArrhythmia());
		// 鼻腔阻塞
		values.put("congestion", diabeteshy.getCongestion());
		// 长期吸烟
		values.put("longsmoking", diabeteshy.getLongsmoking());
		// 脑血管疾病
		values.put("cerebrovasculardisease", diabeteshy.getCerebrovasculardisease());
		// 肾功能损害
		values.put("renalfailure", diabeteshy.getRenalfailure());
		// 服用镇静剂
		values.put("takesedatives", diabeteshy.getTakesedatives());
		// 长期大量饮酒
		values.put("longdrinking", diabeteshy.getLongdrinking());
		// 是否绝经
		values.put("whetherjjm", diabeteshy.getWhetherjjm());
		// 是否有OSAHS的家族史
		values.put("whetherfmhy", diabeteshy.getWhetherfmhy());
		// 是否悬雍垂粗大
		values.put("whetherxyccd", diabeteshy.getWhetherxyccd());

		values.put("useruuid", diabeteshy.getUuid());
		values.put("userupload", diabeteshy.getUpload());

		long id1 = db.insert("diabeteshy", null, values);
		if (id1 != 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	// 修改病史
	@Override
	public boolean modifyDiabetesHy(DiabetesHy diabeteshy) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		/*
		 * 病史
		 */
		// 失眠
		values.put("losesleep", diabeteshy.getLosesleep());
		// 糖尿病
		values.put("diabetes", diabeteshy.getDiabetes());
		// 高血压
		values.put("hypertension", diabeteshy.getHypertension());
		// 冠心病
		values.put("coronaryheartdisease", diabeteshy.getCoronaryheartdisease());
		// 心力衰竭
		values.put("heartfailure", diabeteshy.getHeartfailure());
		// 心律失常
		values.put("arrhythmia", diabeteshy.getArrhythmia());
		// 鼻腔阻塞
		values.put("congestion", diabeteshy.getCongestion());
		// 长期吸烟
		values.put("longsmoking", diabeteshy.getLongsmoking());
		// 脑血管疾病
		values.put("cerebrovasculardisease", diabeteshy.getCerebrovasculardisease());
		// 肾功能损害
		values.put("renalfailure", diabeteshy.getRenalfailure());
		// 服用镇静剂
		values.put("takesedatives", diabeteshy.getTakesedatives());
		// 长期大量饮酒
		values.put("longdrinking", diabeteshy.getLongdrinking());
		// 是否绝经
		values.put("whetherjjm", diabeteshy.getWhetherjjm());
		// 是否有OSAHS的家族史
		values.put("whetherfmhy", diabeteshy.getWhetherfmhy());
		// 是否悬雍垂粗大
		values.put("whetherxyccd", diabeteshy.getWhetherxyccd());

		values.put("useruuid", diabeteshy.getUuid());
		values.put("userupload", diabeteshy.getUpload());

		int row = db.update("diabeteshy", values, "_id=?", new String[] { String.valueOf(diabeteshy.getId()) });
		if (row > 0) {
			System.out.println("成功修改了" + row + "条数据");
			flag = true;
		} else {
			System.out.println("修改失败!");
		}
		db.close();
		return flag;
	}

	// 病史集合
	@Override
	public List<DiabetesHy> findAllDiabetesHy() {
		List<DiabetesHy> all = new ArrayList<DiabetesHy>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from diabeteshy";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			DiabetesHy diabeteshy = new DiabetesHy();
			/*
			 * 病史
			 */
			diabeteshy.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			// 失眠
			diabeteshy.setLosesleep(cursor.getString(cursor.getColumnIndex("losesleep")));
			// 糖尿病
			diabeteshy.setDiabetes(cursor.getString(cursor.getColumnIndex("diabetes")));
			// 高血压
			diabeteshy.setHypertension(cursor.getString(cursor.getColumnIndex("hypertension")));
			// 冠心病
			diabeteshy.setCoronaryheartdisease(cursor.getString(cursor.getColumnIndex("coronaryheartdisease")));
			// 心力衰竭
			diabeteshy.setHeartfailure(cursor.getString(cursor.getColumnIndex("heartfailure")));
			// 心律失常
			diabeteshy.setArrhythmia(cursor.getString(cursor.getColumnIndex("arrhythmia")));
			// 鼻腔阻塞
			diabeteshy.setCongestion(cursor.getString(cursor.getColumnIndex("congestion")));
			// 长期吸烟
			diabeteshy.setLongsmoking(cursor.getString(cursor.getColumnIndex("longsmoking")));
			// 脑血管疾病
			diabeteshy.setCerebrovasculardisease(cursor.getString(cursor.getColumnIndex("cerebrovasculardisease")));
			// 肾功能损害
			diabeteshy.setRenalfailure(cursor.getString(cursor.getColumnIndex("renalfailure")));
			// 服用镇静剂
			diabeteshy.setTakesedatives(cursor.getString(cursor.getColumnIndex("takesedatives")));
			// 长期大量饮酒
			diabeteshy.setLongdrinking(cursor.getString(cursor.getColumnIndex("longdrinking")));
			// 是否绝经
			diabeteshy.setWhetherjjm(cursor.getString(cursor.getColumnIndex("whetherjjm")));
			// 是否有OSAHS的家族史
			diabeteshy.setWhetherfmhy(cursor.getString(cursor.getColumnIndex("whetherfmhy")));
			// 是否悬雍垂粗大
			diabeteshy.setWhetherxyccd(cursor.getString(cursor.getColumnIndex("whetherxyccd")));

			diabeteshy.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			diabeteshy.setUpload(cursor.getInt(cursor.getColumnIndex("userupload")));

			all.add(diabeteshy);
		}
		db.close();
		return all;
	}

	// 按id查询病史
	@Override
	public DiabetesHy findAllDiabetesHyById(int id) {
		DiabetesHy diabeteshy = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from diabeteshy where _id=?";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(id) });
		while (cursor.moveToNext()) {
			diabeteshy = new DiabetesHy();
			/*
			 * 病史
			 */
			diabeteshy.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			// 失眠
			diabeteshy.setLosesleep(cursor.getString(cursor.getColumnIndex("losesleep")));
			// 糖尿病
			diabeteshy.setDiabetes(cursor.getString(cursor.getColumnIndex("diabetes")));
			// 高血压
			diabeteshy.setHypertension(cursor.getString(cursor.getColumnIndex("hypertension")));
			// 冠心病
			diabeteshy.setCoronaryheartdisease(cursor.getString(cursor.getColumnIndex("coronaryheartdisease")));
			// 心力衰竭
			diabeteshy.setHeartfailure(cursor.getString(cursor.getColumnIndex("heartfailure")));
			// 心律失常
			diabeteshy.setArrhythmia(cursor.getString(cursor.getColumnIndex("arrhythmia")));
			// 鼻腔阻塞
			diabeteshy.setCongestion(cursor.getString(cursor.getColumnIndex("congestion")));
			// 长期吸烟
			diabeteshy.setLongsmoking(cursor.getString(cursor.getColumnIndex("longsmoking")));
			// 脑血管疾病
			diabeteshy.setCerebrovasculardisease(cursor.getString(cursor.getColumnIndex("cerebrovasculardisease")));
			// 肾功能损害
			diabeteshy.setRenalfailure(cursor.getString(cursor.getColumnIndex("renalfailure")));
			// 服用镇静剂
			diabeteshy.setTakesedatives(cursor.getString(cursor.getColumnIndex("takesedatives")));
			// 长期大量饮酒
			diabeteshy.setLongdrinking(cursor.getString(cursor.getColumnIndex("longdrinking")));
			// 是否绝经
			diabeteshy.setWhetherjjm(cursor.getString(cursor.getColumnIndex("whetherjjm")));
			// 是否有OSAHS的家族史
			diabeteshy.setWhetherfmhy(cursor.getString(cursor.getColumnIndex("whetherfmhy")));
			// 是否悬雍垂粗大
			diabeteshy.setWhetherxyccd(cursor.getString(cursor.getColumnIndex("whetherxyccd")));

			diabeteshy.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			diabeteshy.setUpload(cursor.getInt(cursor.getColumnIndex("userupload")));
		}
		db.close();
		return diabeteshy;
	}

	/**
	 * 判定上传
	 */
	// 用户
	@Override
	public List<UserManager> findAllWebUploadUser() {
		List<UserManager> all = new ArrayList<UserManager>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from usermanager where userupload=0";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			UserManager user = new UserManager();
			user.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			user.setUserage(cursor.getString(cursor.getColumnIndex("userage")));
			user.setUsersex(cursor.getString(cursor.getColumnIndex("usersex")));
			user.setUserweight(cursor.getInt(cursor.getColumnIndex("userweight")));
			user.setResponsiblephysician(cursor.getString(cursor.getColumnIndex("responsiblephysician")));
			user.setMobilenumber(cursor.getString(cursor.getColumnIndex("mobilenumber")));
			user.setUseremail(cursor.getString(cursor.getColumnIndex("useremail")));
			user.setUhospitalname(cursor.getString(cursor.getColumnIndex("uhospitalname")));
			user.setUserheight(cursor.getInt(cursor.getColumnIndex("userheight")));
			user.setUserbmi(cursor.getInt(cursor.getColumnIndex("userbmi")));
			user.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			user.setUpload(cursor.getInt(cursor.getColumnIndex("userupload")));
			all.add(user);
		}
		db.close();
		return all;
	}

	/**
	 * 疾病
	 */
	@Override
	public List<DiabetesHy> findAllWebUploadeDiabetesHy() {
		List<DiabetesHy> all = new ArrayList<DiabetesHy>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from diabeteshy where userupload=0";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			DiabetesHy diabeteshy = new DiabetesHy();
			/*
			 * 病史
			 */
			diabeteshy.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			// 失眠
			diabeteshy.setLosesleep(cursor.getString(cursor.getColumnIndex("losesleep")));
			// 糖尿病
			diabeteshy.setDiabetes(cursor.getString(cursor.getColumnIndex("diabetes")));
			// 高血压
			diabeteshy.setHypertension(cursor.getString(cursor.getColumnIndex("hypertension")));
			// 冠心病
			diabeteshy.setCoronaryheartdisease(cursor.getString(cursor.getColumnIndex("coronaryheartdisease")));
			// 心力衰竭
			diabeteshy.setHeartfailure(cursor.getString(cursor.getColumnIndex("heartfailure")));
			// 心律失常
			diabeteshy.setArrhythmia(cursor.getString(cursor.getColumnIndex("arrhythmia")));
			// 鼻腔阻塞
			diabeteshy.setCongestion(cursor.getString(cursor.getColumnIndex("congestion")));
			// 长期吸烟
			diabeteshy.setLongsmoking(cursor.getString(cursor.getColumnIndex("longsmoking")));
			// 脑血管疾病
			diabeteshy.setCerebrovasculardisease(cursor.getString(cursor.getColumnIndex("cerebrovasculardisease")));
			// 肾功能损害
			diabeteshy.setRenalfailure(cursor.getString(cursor.getColumnIndex("renalfailure")));
			// 服用镇静剂
			diabeteshy.setTakesedatives(cursor.getString(cursor.getColumnIndex("takesedatives")));
			// 长期大量饮酒
			diabeteshy.setLongdrinking(cursor.getString(cursor.getColumnIndex("longdrinking")));
			// 是否绝经
			diabeteshy.setWhetherjjm(cursor.getString(cursor.getColumnIndex("whetherjjm")));
			// 是否有OSAHS的家族史
			diabeteshy.setWhetherfmhy(cursor.getString(cursor.getColumnIndex("whetherfmhy")));
			// 是否悬雍垂粗大
			diabeteshy.setWhetherxyccd(cursor.getString(cursor.getColumnIndex("whetherxyccd")));

			diabeteshy.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			diabeteshy.setUpload(cursor.getInt(cursor.getColumnIndex("userupload")));

			all.add(diabeteshy);
		}
		db.close();
		return all;
	}

	/**
	 * ESS
	 */
	@Override
	public List<Epworth> findAllWebUploadeEpworth() {
		List<Epworth> all = new ArrayList<Epworth>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from epworth where userupload=0";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			Epworth epworth = new Epworth();
			/**
			 * ESS
			 */
			epworth.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			epworth.setSatreading(cursor.getInt(cursor.getColumnIndex("satreading")));
			epworth.setWatchtv(cursor.getInt(cursor.getColumnIndex("watchtv")));
			epworth.setSatnotmove(cursor.getInt(cursor.getColumnIndex("satnotmove")));
			epworth.setLongnotrest(cursor.getInt(cursor.getColumnIndex("longnotrest")));
			epworth.setSatconversation(cursor.getInt(cursor.getColumnIndex("satconversation")));
			epworth.setAfterdinnerrest(cursor.getInt(cursor.getColumnIndex("afterdinnerrest")));
			epworth.setTrafficlights(cursor.getInt(cursor.getColumnIndex("trafficlights")));
			epworth.setJingworest(cursor.getInt(cursor.getColumnIndex("jingworest")));
			epworth.setSumscore(cursor.getInt(cursor.getColumnIndex("sumscore")));

			epworth.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			epworth.setUpload(cursor.getInt(cursor.getColumnIndex("userupload")));

			all.add(epworth);
		}
		db.close();
		return all;
	}

	/**
	 * pad设置
	 */
	// 增加
	@Override
	public boolean addHospitals(Hospitals hospitals) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("hospitalname", hospitals.getHospitalname());
		values.put("departmentname", hospitals.getDepartmentname());
		values.put("doctorname", hospitals.getDoctorname());
		values.put("printerinformation", hospitals.getPrinterinformation());
		values.put("useruuid", hospitals.getUseruuid());
		values.put("userid", hospitals.getUserid());
		long id1 = db.insert("hospitals", null, values);
		if (id1 != 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	// 修改
	@Override
	public boolean modifyhospitals(Hospitals hospitals) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("hospitalname", hospitals.getHospitalname());
		values.put("departmentname", hospitals.getDepartmentname());
		values.put("doctorname", hospitals.getDoctorname());
		values.put("printerinformation", hospitals.getPrinterinformation());
		values.put("useruuid", hospitals.getUseruuid());
		values.put("userid", hospitals.getUserid());
		int row = db.update("hospitals", values, "_id=?", new String[] { String.valueOf(hospitals.getId()) });
		if (row > 0) {
			System.out.println("成功修改了" + row + "条数据");
			flag = true;
		} else {
			System.out.println("修改失败!");
		}
		db.close();
		return flag;
	}

	// 查询集合
	@Override
	public List<Hospitals> findAllHospitals() {
		List<Hospitals> all = new ArrayList<Hospitals>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from hospitals";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			Hospitals hospitals = new Hospitals();
			hospitals.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			hospitals.setHospitalname(cursor.getString(cursor.getColumnIndex("hospitalname")));
			hospitals.setDepartmentname(cursor.getString(cursor.getColumnIndex("departmentname")));
			hospitals.setDoctorname(cursor.getString(cursor.getColumnIndex("doctorname")));
			hospitals.setUseruuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			hospitals.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));

			hospitals.setPrinterinformation(cursor.getString(cursor.getColumnIndex("printerinformation")));
			all.add(hospitals);
		}
		db.close();
		return all;
	}

	// id查询
	@Override
	public Hospitals findAllHospitalsById(int hid) {
		Hospitals hospitals = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from hospitals where _id=?";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(hid) });
		while (cursor.moveToNext()) {
			hospitals = new Hospitals();
			hospitals.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			hospitals.setHospitalname(cursor.getString(cursor.getColumnIndex("hospitalname")));
			hospitals.setDepartmentname(cursor.getString(cursor.getColumnIndex("departmentname")));
			hospitals.setDoctorname(cursor.getString(cursor.getColumnIndex("doctorname")));
			hospitals.setUseruuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			hospitals.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
			hospitals.setPrinterinformation(cursor.getString(cursor.getColumnIndex("printerinformation")));
		}
		db.close();
		return hospitals;
	}

	/**
	 * 历史记录
	 */
	// 增加
	@Override
	public boolean addHistorys(UserHistory history) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("hstarthour", history.getHstarthour());
		values.put("hstartminute", history.getHstartminute());
		values.put("hstarttimes", history.getHstarttimes());
		values.put("hendhour", history.getHendhour());
		values.put("hendminute", history.getHendminute());
		values.put("hendtimes", history.getHendtimes());
		values.put("hsmschour", history.getHsmschour());
		values.put("hsmscminute", history.getHsmscminute());
		values.put("hsmsctimes", history.getHsmsctimes());
		values.put("hqshour", history.getHqshour());
		values.put("hqsminute", history.getHqsminute());
		values.put("hqssctimes", history.getHqssctimes());
		values.put("hsshour", history.getHsshour());
		values.put("hssminute", history.getHssminute());
		values.put("hsssctimes", history.getHsssctimes());
		values.put("hqxhour", history.getHqxhour());
		values.put("hqxminute", history.getHqxminute());
		values.put("hqxsctimes", history.getHqxsctimes());
		values.put("hmaxml", history.getHmaxml());
		values.put("hmaxmlhour", history.getHmaxmlhour());
		values.put("hmaxmlminute", history.getHmaxmlminute());
		values.put("hmaxmlsec", history.getHmaxmlsec());
		values.put("hmaxmltimes", history.getHmaxmltimes());

		values.put("hminml", history.getHminml());
		values.put("hminmlhour", history.getHminmlhour());
		values.put("hminmlminute", history.getHminmlminute());
		values.put("hminmlsec", history.getHminmlsec());
		values.put("hminmltimes", history.getHminmltimes());
		values.put("havgml", history.getHavgml());
		values.put("hsleepscore", history.getHsleepscore());
		values.put("hahiIndex", history.getHahiIndex());
		values.put("hhxztIndex", history.getHhxztIndex());
		values.put("hdtqIndex", history.getHdtqIndex());
		values.put("hyjzcsIndex", history.getHyjzcsIndex());
		values.put("hpjxybhdIndex", history.getHpjxybhdIndex());
		values.put("hzdxybhdIndex", history.getHzdxybhdIndex());
		values.put("hxybhdzbIndex", history.getHxybhdzbIndex());
		values.put("hmaxyjcxhour", history.getHmaxyjcxhour());
		values.put("hmaxyjcxminute", history.getHmaxyjcxminute());
		values.put("hmaxyjcxsec", history.getHmaxyjcxsec());
		values.put("hmaxyjcxtimes", history.getHmaxyjcxtimes());
		values.put("hmaxyjfshour", history.getHmaxyjfshour());
		values.put("hmaxyjfsminute", history.getHmaxyjfsminute());
		values.put("hmaxyjfssec", history.getHmaxyjfssec());
		values.put("hmaxyjfstimes", history.getHmaxyjfstimes());
		values.put("hlongyjcxhour", history.getHlongyjcxhour());
		values.put("hlongyjcxminute", history.getHlongyjcxminute());
		values.put("hlongyjcxsec", history.getHlongyjcxsec());
		values.put("hlongyjcxtimes", history.getHlongyjcxtimes());
		values.put("hlongyjfshour", history.getHlongyjfshour());
		values.put("hlongyjfsminute", history.getHlongyjfsminute());
		values.put("hlongyjfssec", history.getHlongyjfssec());
		values.put("hlongyjfstimes", history.getHlongyjfstimes());
		values.put("hxywhzsIndex", history.getHxywhzsIndex());
		values.put("havgxlgzd", history.getHavgxlgzd());
		values.put("hosahsdegree", history.getHosahsdegree());
		values.put("hdyxzdegree", history.getHdyxzdegree());
		values.put("hyjzsIndex", history.getHyjzsIndex());
		values.put("hzchxzthour", history.getHzchxzthour());
		values.put("hzchxztminute", history.getHzchxztminute());
		values.put("hzchxztsec", history.getHzchxztsec());
		values.put("hzchxzttimes", history.getHzchxzttimes());
		values.put("hhxhapperhour", history.getHhxhapperhour());
		values.put("hhxhapperminute", history.getHhxhapperminute());
		values.put("hhxhappersec", history.getHhxhappersec());
		values.put("hhxhappertimes", history.getHhxhappertimes());
		values.put("hhxztzsjhour", history.getHhxztzsjhour());
		values.put("hhxztzsjminute", history.getHhxztzsjminute());
		values.put("hhxztzsjsec", history.getHhxztzsjsec());
		values.put("hhxztzsjtimes", history.getHhxztzsjtimes());
		values.put("hzcdtqhour", history.getHzcdtqhour());
		values.put("hzcdtqminute", history.getHzcdtqminute());
		values.put("hzcdtqsec", history.getHzcdtqsec());
		values.put("hzcdtqtimes", history.getHzcdtqtimes());
		values.put("hdtqhapperhour", history.getHdtqhapperhour());
		values.put("hdtqhapperminute", history.getHdtqhapperminute());
		values.put("hdtqhappersec", history.getHdtqhappersec());
		values.put("hdtqhappertimes", history.getHdtqhappertimes());
		values.put("hdtqzsjhour", history.getHdtqzsjhour());
		values.put("hdtqzsjminute", history.getHdtqzsjminute());
		values.put("hdtqzsjsec", history.getHdtqzsjsec());
		values.put("hdtqzsjtimes", history.getHdtqzsjtimes());
		values.put("hxyodi", history.getHxyodi());
		values.put("hsmsober", history.getHsmsober());
		values.put("hsmmax", history.getHsmmax());
		// a
		values.put("haxyfbzsjhour", history.getHaxyfbzsjhour());
		values.put("haxyfbzsjminute", history.getHaxyfbzsjminute());
		values.put("haxyfbzsjsec", history.getHaxyfbzsjsec());
		values.put("hayjzcsnum", history.getHayjzcsnum());
		values.put("haxyfbzsjtimes", history.getHaxyfbzsjtimes());
		// b
		values.put("hbxyfbzsjhour", history.getHbxyfbzsjhour());
		values.put("hbxyfbzsjminute", history.getHbxyfbzsjminute());
		values.put("hbxyfbzsjsec", history.getHbxyfbzsjsec());
		values.put("hbyjzcsnum", history.getHbyjzcsnum());
		values.put("hbxyfbzsjtimes", history.getHbxyfbzsjtimes());
		// c
		values.put("hcxyfbzsjhour", history.getHcxyfbzsjhour());
		values.put("hcxyfbzsjminute", history.getHcxyfbzsjminute());
		values.put("hcxyfbzsjsec", history.getHcxyfbzsjsec());
		values.put("hcyjzcsnum", history.getHcyjzcsnum());
		values.put("hcxyfbzsjtimes", history.getHcxyfbzsjtimes());
		// d
		values.put("hdxyfbzsjhour", history.getHdxyfbzsjhour());
		values.put("hdxyfbzsjminute", history.getHdxyfbzsjminute());
		values.put("hdxyfbzsjsec", history.getHdxyfbzsjsec());
		values.put("hdyjzcsnum", history.getHdyjzcsnum());
		values.put("hdxyfbzsjtimes", history.getHdxyfbzsjtimes());
		// e
		values.put("hexyfbzsjhour", history.getHexyfbzsjhour());
		values.put("hexyfbzsjminute", history.getHexyfbzsjminute());
		values.put("hexyfbzsjsec", history.getHexyfbzsjsec());
		values.put("heyjzcsnum", history.getHeyjzcsnum());
		values.put("hexyfbzsjtimes", history.getHexyfbzsjtimes());
		// f
		values.put("hfxyfbzsjhour", history.getHfxyfbzsjhour());
		values.put("hfxyfbzsjminute", history.getHfxyfbzsjminute());
		values.put("hfxyfbzsjsec", history.getHfxyfbzsjsec());
		values.put("hfyjzcsnum", history.getHfyjzcsnum());
		values.put("hfxyfbzsjtimes", history.getHfxyfbzsjtimes());

		values.put("hudegree", history.getHudegree());
		values.put("hscoreHxzb", history.getHscoreHxzb());
		values.put("testdate", history.getTestdate());
		values.put("reportuuid", history.getReportuuid());
		// 是否计算
		values.put("calculationflag", history.getCalculationflag());
		// 文件路径
		values.put("filepaths", history.getFilepaths());
		// 文件上传
		values.put("uploadfile", history.getUploadfile());
		// mac地址
		values.put("macaddress", history.getMacaddress());
		// pdf
		values.put("reportfilepath", history.getReportfilepath());
		// 用户ID
		values.put("userid", history.getUserid());
		// 用户uuid
		values.put("useruuid", history.getUuid());
		// 历史报告上传
		values.put("historyupload", history.getHistoryupload());
		// 是否为最终报告
		values.put("isFinalReport", history.getIsFinalReport());
		values.put("iscomputed", history.getIsComputed());

		long id1 = db.insert("historys", null, values);
		if (id1 != 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	// 修改
	@Override
	public boolean modifyHistorys(UserHistory history) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("hstarthour", history.getHstarthour());
		values.put("hstartminute", history.getHstartminute());
		values.put("hstarttimes", history.getHstarttimes());
		values.put("hendhour", history.getHendhour());
		values.put("hendminute", history.getHendminute());
		values.put("hendtimes", history.getHendtimes());
		values.put("hsmschour", history.getHsmschour());
		values.put("hsmscminute", history.getHsmscminute());
		values.put("hsmsctimes", history.getHsmsctimes());
		values.put("hqshour", history.getHqshour());
		values.put("hqsminute", history.getHqsminute());
		values.put("hqssctimes", history.getHqssctimes());
		values.put("hsshour", history.getHsshour());
		values.put("hssminute", history.getHssminute());
		values.put("hsssctimes", history.getHsssctimes());
		values.put("hqxhour", history.getHqxhour());
		values.put("hqxminute", history.getHqxminute());
		values.put("hqxsctimes", history.getHqxsctimes());
		values.put("hmaxml", history.getHmaxml());
		values.put("hmaxmlhour", history.getHmaxmlhour());
		values.put("hmaxmlminute", history.getHmaxmlminute());
		values.put("hmaxmlsec", history.getHmaxmlsec());
		values.put("hmaxmltimes", history.getHmaxmltimes());

		values.put("hminml", history.getHminml());
		values.put("hminmlhour", history.getHminmlhour());
		values.put("hminmlminute", history.getHminmlminute());
		values.put("hminmlsec", history.getHminmlsec());
		values.put("hminmltimes", history.getHminmltimes());
		values.put("havgml", history.getHavgml());
		values.put("hsleepscore", history.getHsleepscore());
		values.put("hahiIndex", history.getHahiIndex());
		values.put("hhxztIndex", history.getHhxztIndex());
		values.put("hdtqIndex", history.getHdtqIndex());
		values.put("hyjzcsIndex", history.getHyjzcsIndex());
		values.put("hpjxybhdIndex", history.getHpjxybhdIndex());
		values.put("hzdxybhdIndex", history.getHzdxybhdIndex());
		values.put("hxybhdzbIndex", history.getHxybhdzbIndex());
		values.put("hmaxyjcxhour", history.getHmaxyjcxhour());
		values.put("hmaxyjcxminute", history.getHmaxyjcxminute());
		values.put("hmaxyjcxsec", history.getHmaxyjcxsec());
		values.put("hmaxyjcxtimes", history.getHmaxyjcxtimes());
		values.put("hmaxyjfshour", history.getHmaxyjfshour());
		values.put("hmaxyjfsminute", history.getHmaxyjfsminute());
		values.put("hmaxyjfssec", history.getHmaxyjfssec());
		values.put("hmaxyjfstimes", history.getHmaxyjfstimes());
		values.put("hlongyjcxhour", history.getHlongyjcxhour());
		values.put("hlongyjcxminute", history.getHlongyjcxminute());
		values.put("hlongyjcxsec", history.getHlongyjcxsec());
		values.put("hlongyjcxtimes", history.getHlongyjcxtimes());
		values.put("hlongyjfshour", history.getHlongyjfshour());
		values.put("hlongyjfsminute", history.getHlongyjfsminute());
		values.put("hlongyjfssec", history.getHlongyjfssec());
		values.put("hlongyjfstimes", history.getHlongyjfstimes());
		values.put("hxywhzsIndex", history.getHxywhzsIndex());
		values.put("havgxlgzd", history.getHavgxlgzd());
		values.put("hosahsdegree", history.getHosahsdegree());
		values.put("hdyxzdegree", history.getHdyxzdegree());
		values.put("hyjzsIndex", history.getHyjzsIndex());
		values.put("hzchxzthour", history.getHzchxzthour());
		values.put("hzchxztminute", history.getHzchxztminute());
		values.put("hzchxztsec", history.getHzchxztsec());
		values.put("hzchxzttimes", history.getHzchxzttimes());
		values.put("hhxhapperhour", history.getHhxhapperhour());
		values.put("hhxhapperminute", history.getHhxhapperminute());
		values.put("hhxhappersec", history.getHhxhappersec());
		values.put("hhxhappertimes", history.getHhxhappertimes());
		values.put("hhxztzsjhour", history.getHhxztzsjhour());
		values.put("hhxztzsjminute", history.getHhxztzsjminute());
		values.put("hhxztzsjsec", history.getHhxztzsjsec());
		values.put("hhxztzsjtimes", history.getHhxztzsjtimes());
		values.put("hzcdtqhour", history.getHzcdtqhour());
		values.put("hzcdtqminute", history.getHzcdtqminute());
		values.put("hzcdtqsec", history.getHzcdtqsec());
		values.put("hzcdtqtimes", history.getHzcdtqtimes());
		values.put("hdtqhapperhour", history.getHdtqhapperhour());
		values.put("hdtqhapperminute", history.getHdtqhapperminute());
		values.put("hdtqhappersec", history.getHdtqhappersec());
		values.put("hdtqhappertimes", history.getHdtqhappertimes());
		values.put("hdtqzsjhour", history.getHdtqzsjhour());
		values.put("hdtqzsjminute", history.getHdtqzsjminute());
		values.put("hdtqzsjsec", history.getHdtqzsjsec());
		values.put("hdtqzsjtimes", history.getHdtqzsjtimes());
		values.put("hxyodi", history.getHxyodi());
		values.put("hsmsober", history.getHsmsober());
		values.put("hsmmax", history.getHsmmax());
		// a
		values.put("haxyfbzsjhour", history.getHaxyfbzsjhour());
		values.put("haxyfbzsjminute", history.getHaxyfbzsjminute());
		values.put("haxyfbzsjsec", history.getHaxyfbzsjsec());
		values.put("hayjzcsnum", history.getHayjzcsnum());
		values.put("haxyfbzsjtimes", history.getHaxyfbzsjtimes());
		// b
		values.put("hbxyfbzsjhour", history.getHbxyfbzsjhour());
		values.put("hbxyfbzsjminute", history.getHbxyfbzsjminute());
		values.put("hbxyfbzsjsec", history.getHbxyfbzsjsec());
		values.put("hbyjzcsnum", history.getHbyjzcsnum());
		values.put("hbxyfbzsjtimes", history.getHbxyfbzsjtimes());
		// c
		values.put("hcxyfbzsjhour", history.getHcxyfbzsjhour());
		values.put("hcxyfbzsjminute", history.getHcxyfbzsjminute());
		values.put("hcxyfbzsjsec", history.getHcxyfbzsjsec());
		values.put("hcyjzcsnum", history.getHcyjzcsnum());
		values.put("hcxyfbzsjtimes", history.getHcxyfbzsjtimes());
		// d
		values.put("hdxyfbzsjhour", history.getHdxyfbzsjhour());
		values.put("hdxyfbzsjminute", history.getHdxyfbzsjminute());
		values.put("hdxyfbzsjsec", history.getHdxyfbzsjsec());
		values.put("hdyjzcsnum", history.getHdyjzcsnum());
		values.put("hdxyfbzsjtimes", history.getHdxyfbzsjtimes());
		// e
		values.put("hexyfbzsjhour", history.getHexyfbzsjhour());
		values.put("hexyfbzsjminute", history.getHexyfbzsjminute());
		values.put("hexyfbzsjsec", history.getHexyfbzsjsec());
		values.put("heyjzcsnum", history.getHeyjzcsnum());
		values.put("hexyfbzsjtimes", history.getHexyfbzsjtimes());
		// f
		values.put("hfxyfbzsjhour", history.getHfxyfbzsjhour());
		values.put("hfxyfbzsjminute", history.getHfxyfbzsjminute());
		values.put("hfxyfbzsjsec", history.getHfxyfbzsjsec());
		values.put("hfyjzcsnum", history.getHfyjzcsnum());
		values.put("hfxyfbzsjtimes", history.getHfxyfbzsjtimes());

		values.put("hudegree", history.getHudegree());
		values.put("hscoreHxzb", history.getHscoreHxzb());
		values.put("calculationflag", history.getCalculationflag());
		// 文件路径
		values.put("filepaths", history.getFilepaths());
		// 文件上传
		values.put("uploadfile", history.getUploadfile());
		// mac地址
		values.put("macaddress", history.getMacaddress());
		// pdf
		values.put("reportfilepath", history.getReportfilepath());
		// 用户ID
		values.put("userid", history.getUserid());
		// 用户uuid
		values.put("useruuid", history.getUuid());
		// 历史报告上传
		values.put("historyupload", history.getHistoryupload());
		// 是否为最终报告
		values.put("isFinalReport", history.getIsFinalReport());

		values.put("testdate", history.getTestdate());
		values.put("reportuuid", history.getReportuuid());
		values.put("iscomputed", history.getIsComputed());

		int row = db.update("historys", values, "_id=?", new String[] { String.valueOf(history.getId()) });
		if (row > 0) {
			System.out.println("成功修改了" + row + "条数据");
			flag = true;
		} else {
			System.out.println("修改失败!");
		}
		db.close();
		return flag;
	}

	// 删除
	@Override
	public boolean removeHistory(int id) {
		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int row = db.delete("historys", "_id=?", new String[] { String.valueOf(id) });
		if (row > 0) {
			System.out.println("成功删除了" + row + "条数据");
			Log.i("TAG", "成功删除了" + row + "条数据");
			flag = true;
		}
		db.close();
		return flag;
	}

	// 集合查询
	@Override
	public List<UserHistory> findAllHistorys() {
		List<UserHistory> all = new ArrayList<UserHistory>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from historys";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			UserHistory history = new UserHistory();
			history.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			history.setHstarthour(cursor.getInt(cursor.getColumnIndex("hstarthour")));
			history.setHstartminute(cursor.getInt(cursor.getColumnIndex("hstartminute")));
			history.setHstarttimes(cursor.getInt(cursor.getColumnIndex("hstarttimes")));
			history.setHendhour(cursor.getInt(cursor.getColumnIndex("hendhour")));
			history.setHendminute(cursor.getInt(cursor.getColumnIndex("hendminute")));
			history.setHendtimes(cursor.getInt(cursor.getColumnIndex("hendtimes")));
			history.setHsmschour(cursor.getInt(cursor.getColumnIndex("hsmschour")));
			history.setHsmscminute(cursor.getInt(cursor.getColumnIndex("hsmscminute")));
			history.setHsmsctimes(cursor.getInt(cursor.getColumnIndex("hsmsctimes")));
			history.setHqshour(cursor.getInt(cursor.getColumnIndex("hqshour")));
			history.setHqsminute(cursor.getInt(cursor.getColumnIndex("hqsminute")));
			history.setHqssctimes(cursor.getInt(cursor.getColumnIndex("hqssctimes")));
			history.setHsshour(cursor.getInt(cursor.getColumnIndex("hsshour")));
			history.setHssminute(cursor.getInt(cursor.getColumnIndex("hssminute")));
			history.setHsssctimes(cursor.getInt(cursor.getColumnIndex("hsssctimes")));
			history.setHqxhour(cursor.getInt(cursor.getColumnIndex("hqxhour")));
			history.setHqxminute(cursor.getInt(cursor.getColumnIndex("hqxminute")));
			history.setHqxsctimes(cursor.getInt(cursor.getColumnIndex("hqxsctimes")));

			history.setHmaxml(cursor.getString(cursor.getColumnIndex("hmaxml")));
			history.setHmaxmlhour(cursor.getInt(cursor.getColumnIndex("hmaxmlhour")));
			history.setHmaxmlminute(cursor.getInt(cursor.getColumnIndex("hmaxmlminute")));
			history.setHmaxmlsec(cursor.getInt(cursor.getColumnIndex("hmaxmlsec")));
			history.setHmaxmltimes(cursor.getInt(cursor.getColumnIndex("hmaxmltimes")));

			history.setHminml(cursor.getString(cursor.getColumnIndex("hminml")));
			history.setHminmlhour(cursor.getInt(cursor.getColumnIndex("hminmlhour")));
			history.setHminmlminute(cursor.getInt(cursor.getColumnIndex("hminmlminute")));
			history.setHminmlsec(cursor.getInt(cursor.getColumnIndex("hminmlsec")));
			history.setHminmltimes(cursor.getInt(cursor.getColumnIndex("hminmltimes")));

			history.setHavgml(cursor.getString(cursor.getColumnIndex("havgml")));
			history.setHsleepscore(cursor.getInt(cursor.getColumnIndex("hsleepscore")));

			history.setHahiIndex(cursor.getString(cursor.getColumnIndex("hahiIndex")));
			history.setHhxztIndex(cursor.getString(cursor.getColumnIndex("hhxztIndex")));
			history.setHdtqIndex(cursor.getString(cursor.getColumnIndex("hdtqIndex")));
			history.setHyjzcsIndex(cursor.getString(cursor.getColumnIndex("hyjzcsIndex")));
			history.setHpjxybhdIndex(cursor.getString(cursor.getColumnIndex("hpjxybhdIndex")));
			history.setHzdxybhdIndex(cursor.getString(cursor.getColumnIndex("hzdxybhdIndex")));
			history.setHxybhdzbIndex(cursor.getString(cursor.getColumnIndex("hxybhdzbIndex")));

			history.setHmaxyjcxhour(cursor.getInt(cursor.getColumnIndex("hmaxyjcxhour")));
			history.setHmaxyjcxminute(cursor.getInt(cursor.getColumnIndex("hmaxyjcxminute")));
			history.setHmaxyjcxsec(cursor.getInt(cursor.getColumnIndex("hmaxyjcxsec")));
			history.setHmaxyjcxtimes(cursor.getInt(cursor.getColumnIndex("hmaxyjcxtimes")));
			history.setHmaxyjfshour(cursor.getInt(cursor.getColumnIndex("hmaxyjfshour")));
			history.setHmaxyjfsminute(cursor.getInt(cursor.getColumnIndex("hmaxyjfsminute")));
			history.setHmaxyjfssec(cursor.getInt(cursor.getColumnIndex("hmaxyjfssec")));
			history.setHmaxyjfstimes(cursor.getInt(cursor.getColumnIndex("hmaxyjfstimes")));
			history.setHlongyjcxhour(cursor.getInt(cursor.getColumnIndex("hlongyjcxhour")));
			history.setHlongyjcxminute(cursor.getInt(cursor.getColumnIndex("hlongyjcxminute")));
			history.setHlongyjcxsec(cursor.getInt(cursor.getColumnIndex("hlongyjcxsec")));
			history.setHlongyjcxtimes(cursor.getInt(cursor.getColumnIndex("hlongyjcxtimes")));
			history.setHlongyjfshour(cursor.getInt(cursor.getColumnIndex("hlongyjfshour")));
			history.setHlongyjfsminute(cursor.getInt(cursor.getColumnIndex("hlongyjfsminute")));
			history.setHlongyjfssec(cursor.getInt(cursor.getColumnIndex("hlongyjfssec")));
			history.setHlongyjfstimes(cursor.getInt(cursor.getColumnIndex("hlongyjfstimes")));

			history.setHxywhzsIndex(cursor.getString(cursor.getColumnIndex("hxywhzsIndex")));
			history.setHavgxlgzd(cursor.getString(cursor.getColumnIndex("havgxlgzd")));
			history.setHosahsdegree(cursor.getString(cursor.getColumnIndex("hosahsdegree")));
			history.setHdyxzdegree(cursor.getString(cursor.getColumnIndex("hdyxzdegree")));
			history.setHyjzsIndex(cursor.getString(cursor.getColumnIndex("hyjzsIndex")));

			history.setHzchxzthour(cursor.getInt(cursor.getColumnIndex("hzchxzthour")));
			history.setHzchxztminute(cursor.getInt(cursor.getColumnIndex("hzchxztminute")));
			history.setHzchxztsec(cursor.getInt(cursor.getColumnIndex("hzchxztsec")));
			history.setHzchxzttimes(cursor.getInt(cursor.getColumnIndex("hzchxzttimes")));
			history.setHhxhapperhour(cursor.getInt(cursor.getColumnIndex("hhxhapperhour")));
			history.setHhxhapperminute(cursor.getInt(cursor.getColumnIndex("hhxhapperminute")));
			history.setHhxhappersec(cursor.getInt(cursor.getColumnIndex("hhxhappersec")));
			history.setHhxhappertimes(cursor.getInt(cursor.getColumnIndex("hhxhappertimes")));
			history.setHhxztzsjhour(cursor.getInt(cursor.getColumnIndex("hhxztzsjhour")));
			history.setHhxztzsjminute(cursor.getInt(cursor.getColumnIndex("hhxztzsjminute")));
			history.setHhxztzsjsec(cursor.getInt(cursor.getColumnIndex("hhxztzsjsec")));
			history.setHhxztzsjtimes(cursor.getInt(cursor.getColumnIndex("hhxztzsjtimes")));
			history.setHzcdtqhour(cursor.getInt(cursor.getColumnIndex("hzcdtqhour")));
			history.setHzcdtqminute(cursor.getInt(cursor.getColumnIndex("hzcdtqminute")));
			history.setHzcdtqsec(cursor.getInt(cursor.getColumnIndex("hzcdtqsec")));
			history.setHzcdtqtimes(cursor.getInt(cursor.getColumnIndex("hzcdtqtimes")));
			history.setHdtqhapperhour(cursor.getInt(cursor.getColumnIndex("hdtqhapperhour")));
			history.setHdtqhapperminute(cursor.getInt(cursor.getColumnIndex("hdtqhapperminute")));
			history.setHdtqhappersec(cursor.getInt(cursor.getColumnIndex("hdtqhappersec")));
			history.setHdtqhappertimes(cursor.getInt(cursor.getColumnIndex("hdtqhappertimes")));
			history.setHdtqzsjhour(cursor.getInt(cursor.getColumnIndex("hdtqzsjhour")));
			history.setHdtqzsjminute(cursor.getInt(cursor.getColumnIndex("hdtqzsjminute")));
			history.setHdtqzsjsec(cursor.getInt(cursor.getColumnIndex("hdtqzsjsec")));
			history.setHdtqzsjtimes(cursor.getInt(cursor.getColumnIndex("hdtqzsjtimes")));

			history.setHxyodi(cursor.getString(cursor.getColumnIndex("hxyodi")));
			history.setHsmsober(cursor.getString(cursor.getColumnIndex("hsmsober")));
			history.setHsmmax(cursor.getString(cursor.getColumnIndex("hsmmax")));
			// a
			history.setHaxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("haxyfbzsjhour")));
			history.setHaxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("haxyfbzsjminute")));
			history.setHaxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("haxyfbzsjsec")));
			history.setHayjzcsnum(cursor.getString(cursor.getColumnIndex("hayjzcsnum")));
			history.setHaxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("haxyfbzsjtimes")));
			// b
			history.setHbxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjhour")));
			history.setHbxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjminute")));
			history.setHbxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjsec")));
			history.setHbyjzcsnum(cursor.getString(cursor.getColumnIndex("hbyjzcsnum")));
			history.setHbxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjtimes")));
			// c
			history.setHcxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjhour")));
			history.setHcxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjminute")));
			history.setHcxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjsec")));
			history.setHcyjzcsnum(cursor.getString(cursor.getColumnIndex("hcyjzcsnum")));
			history.setHcxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjtimes")));
			// d
			history.setHdxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjhour")));
			history.setHdxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjminute")));
			history.setHdxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjsec")));
			history.setHdyjzcsnum(cursor.getString(cursor.getColumnIndex("hdyjzcsnum")));
			history.setHdxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjtimes")));
			// e
			history.setHexyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hexyfbzsjhour")));
			history.setHexyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hexyfbzsjminute")));
			history.setHexyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hexyfbzsjsec")));
			history.setHeyjzcsnum(cursor.getString(cursor.getColumnIndex("heyjzcsnum")));
			history.setHexyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hexyfbzsjtimes")));
			// f
			history.setHfxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjhour")));
			history.setHfxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjminute")));
			history.setHfxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjsec")));
			history.setHfyjzcsnum(cursor.getString(cursor.getColumnIndex("hfyjzcsnum")));
			history.setHfxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjtimes")));

			history.setFilepaths(cursor.getString(cursor.getColumnIndex("filepaths")));
			history.setUploadfile(cursor.getInt(cursor.getColumnIndex("uploadfile")));
			history.setMacaddress(cursor.getString(cursor.getColumnIndex("macaddress")));
			history.setReportfilepath(cursor.getString(cursor.getColumnIndex("reportfilepath")));
			history.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
			history.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			history.setHistoryupload(cursor.getInt(cursor.getColumnIndex("historyupload")));
			history.setIsFinalReport(cursor.getInt(cursor.getColumnIndex("isFinalReport")));

			history.setHudegree(cursor.getString(cursor.getColumnIndex("hudegree")));
			history.setHscoreHxzb(cursor.getInt(cursor.getColumnIndex("hscoreHxzb")));
			history.setTestdate(cursor.getInt(cursor.getColumnIndex("testdate")));
			history.setReportuuid(cursor.getString(cursor.getColumnIndex("reportuuid")));
			history.setIsComputed(cursor.getInt(cursor.getColumnIndex("iscomputed")));
			history.setCalculationflag(cursor.getInt(cursor.getColumnIndex("calculationflag")));

			all.add(history);
		}
		db.close();
		return all;
	}

	// 按id查询
	@Override
	public UserHistory findMaxHistoryIdByUserId(int userid) {

		UserHistory history = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from historys where userid=? order by _id desc";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(userid) });
		Log.w(Utils.TAG, "findMaxHistoryIdByUserId, cursor.getCount() = " + cursor.getCount());
		if (cursor.getCount() > 0) {
			cursor.moveToNext();
			history = new UserHistory();
			history.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			history.setHstarthour(cursor.getInt(cursor.getColumnIndex("hstarthour")));
			history.setHstartminute(cursor.getInt(cursor.getColumnIndex("hstartminute")));
			history.setHstarttimes(cursor.getInt(cursor.getColumnIndex("hstarttimes")));
			history.setHendhour(cursor.getInt(cursor.getColumnIndex("hendhour")));
			history.setHendminute(cursor.getInt(cursor.getColumnIndex("hendminute")));
			history.setHendtimes(cursor.getInt(cursor.getColumnIndex("hendtimes")));
			history.setHsmschour(cursor.getInt(cursor.getColumnIndex("hsmschour")));
			history.setHsmscminute(cursor.getInt(cursor.getColumnIndex("hsmscminute")));
			history.setHsmsctimes(cursor.getInt(cursor.getColumnIndex("hsmsctimes")));
			history.setHqshour(cursor.getInt(cursor.getColumnIndex("hqshour")));
			history.setHqsminute(cursor.getInt(cursor.getColumnIndex("hqsminute")));
			history.setHqssctimes(cursor.getInt(cursor.getColumnIndex("hqssctimes")));
			history.setHsshour(cursor.getInt(cursor.getColumnIndex("hsshour")));
			history.setHssminute(cursor.getInt(cursor.getColumnIndex("hssminute")));
			history.setHsssctimes(cursor.getInt(cursor.getColumnIndex("hsssctimes")));
			history.setHqxhour(cursor.getInt(cursor.getColumnIndex("hqxhour")));
			history.setHqxminute(cursor.getInt(cursor.getColumnIndex("hqxminute")));
			history.setHqxsctimes(cursor.getInt(cursor.getColumnIndex("hqxsctimes")));

			history.setHmaxml(cursor.getString(cursor.getColumnIndex("hmaxml")));
			history.setHmaxmlhour(cursor.getInt(cursor.getColumnIndex("hmaxmlhour")));
			history.setHmaxmlminute(cursor.getInt(cursor.getColumnIndex("hmaxmlminute")));
			history.setHmaxmlsec(cursor.getInt(cursor.getColumnIndex("hmaxmlsec")));
			history.setHmaxmltimes(cursor.getInt(cursor.getColumnIndex("hmaxmltimes")));

			history.setHminml(cursor.getString(cursor.getColumnIndex("hminml")));
			history.setHminmlhour(cursor.getInt(cursor.getColumnIndex("hminmlhour")));
			history.setHminmlminute(cursor.getInt(cursor.getColumnIndex("hminmlminute")));
			history.setHminmlsec(cursor.getInt(cursor.getColumnIndex("hminmlsec")));
			history.setHminmltimes(cursor.getInt(cursor.getColumnIndex("hminmltimes")));

			history.setHavgml(cursor.getString(cursor.getColumnIndex("havgml")));
			history.setHsleepscore(cursor.getInt(cursor.getColumnIndex("hsleepscore")));

			history.setHahiIndex(cursor.getString(cursor.getColumnIndex("hahiIndex")));
			history.setHhxztIndex(cursor.getString(cursor.getColumnIndex("hhxztIndex")));
			history.setHdtqIndex(cursor.getString(cursor.getColumnIndex("hdtqIndex")));
			history.setHyjzcsIndex(cursor.getString(cursor.getColumnIndex("hyjzcsIndex")));
			history.setHpjxybhdIndex(cursor.getString(cursor.getColumnIndex("hpjxybhdIndex")));
			history.setHzdxybhdIndex(cursor.getString(cursor.getColumnIndex("hzdxybhdIndex")));
			history.setHxybhdzbIndex(cursor.getString(cursor.getColumnIndex("hxybhdzbIndex")));

			history.setHmaxyjcxhour(cursor.getInt(cursor.getColumnIndex("hmaxyjcxhour")));
			history.setHmaxyjcxminute(cursor.getInt(cursor.getColumnIndex("hmaxyjcxminute")));
			history.setHmaxyjcxsec(cursor.getInt(cursor.getColumnIndex("hmaxyjcxsec")));
			history.setHmaxyjcxtimes(cursor.getInt(cursor.getColumnIndex("hmaxyjcxtimes")));
			history.setHmaxyjfshour(cursor.getInt(cursor.getColumnIndex("hmaxyjfshour")));
			history.setHmaxyjfsminute(cursor.getInt(cursor.getColumnIndex("hmaxyjfsminute")));
			history.setHmaxyjfssec(cursor.getInt(cursor.getColumnIndex("hmaxyjfssec")));
			history.setHmaxyjfstimes(cursor.getInt(cursor.getColumnIndex("hmaxyjfstimes")));
			history.setHlongyjcxhour(cursor.getInt(cursor.getColumnIndex("hlongyjcxhour")));
			history.setHlongyjcxminute(cursor.getInt(cursor.getColumnIndex("hlongyjcxminute")));
			history.setHlongyjcxsec(cursor.getInt(cursor.getColumnIndex("hlongyjcxsec")));
			history.setHlongyjcxtimes(cursor.getInt(cursor.getColumnIndex("hlongyjcxtimes")));
			history.setHlongyjfshour(cursor.getInt(cursor.getColumnIndex("hlongyjfshour")));
			history.setHlongyjfsminute(cursor.getInt(cursor.getColumnIndex("hlongyjfsminute")));
			history.setHlongyjfssec(cursor.getInt(cursor.getColumnIndex("hlongyjfssec")));
			history.setHlongyjfstimes(cursor.getInt(cursor.getColumnIndex("hlongyjfstimes")));

			history.setHxywhzsIndex(cursor.getString(cursor.getColumnIndex("hxywhzsIndex")));
			history.setHavgxlgzd(cursor.getString(cursor.getColumnIndex("havgxlgzd")));
			history.setHosahsdegree(cursor.getString(cursor.getColumnIndex("hosahsdegree")));
			history.setHdyxzdegree(cursor.getString(cursor.getColumnIndex("hdyxzdegree")));
			history.setHyjzsIndex(cursor.getString(cursor.getColumnIndex("hyjzsIndex")));

			history.setHzchxzthour(cursor.getInt(cursor.getColumnIndex("hzchxzthour")));
			history.setHzchxztminute(cursor.getInt(cursor.getColumnIndex("hzchxztminute")));
			history.setHzchxztsec(cursor.getInt(cursor.getColumnIndex("hzchxztsec")));
			history.setHzchxzttimes(cursor.getInt(cursor.getColumnIndex("hzchxzttimes")));
			history.setHhxhapperhour(cursor.getInt(cursor.getColumnIndex("hhxhapperhour")));
			history.setHhxhapperminute(cursor.getInt(cursor.getColumnIndex("hhxhapperminute")));
			history.setHhxhappersec(cursor.getInt(cursor.getColumnIndex("hhxhappersec")));
			history.setHhxhappertimes(cursor.getInt(cursor.getColumnIndex("hhxhappertimes")));
			history.setHhxztzsjhour(cursor.getInt(cursor.getColumnIndex("hhxztzsjhour")));
			history.setHhxztzsjminute(cursor.getInt(cursor.getColumnIndex("hhxztzsjminute")));
			history.setHhxztzsjsec(cursor.getInt(cursor.getColumnIndex("hhxztzsjsec")));
			history.setHhxztzsjtimes(cursor.getInt(cursor.getColumnIndex("hhxztzsjtimes")));
			history.setHzcdtqhour(cursor.getInt(cursor.getColumnIndex("hzcdtqhour")));
			history.setHzcdtqminute(cursor.getInt(cursor.getColumnIndex("hzcdtqminute")));
			history.setHzcdtqsec(cursor.getInt(cursor.getColumnIndex("hzcdtqsec")));
			history.setHzcdtqtimes(cursor.getInt(cursor.getColumnIndex("hzcdtqtimes")));
			history.setHdtqhapperhour(cursor.getInt(cursor.getColumnIndex("hdtqhapperhour")));
			history.setHdtqhapperminute(cursor.getInt(cursor.getColumnIndex("hdtqhapperminute")));
			history.setHdtqhappersec(cursor.getInt(cursor.getColumnIndex("hdtqhappersec")));
			history.setHdtqhappertimes(cursor.getInt(cursor.getColumnIndex("hdtqhappertimes")));
			history.setHdtqzsjhour(cursor.getInt(cursor.getColumnIndex("hdtqzsjhour")));
			history.setHdtqzsjminute(cursor.getInt(cursor.getColumnIndex("hdtqzsjminute")));
			history.setHdtqzsjsec(cursor.getInt(cursor.getColumnIndex("hdtqzsjsec")));
			history.setHdtqzsjtimes(cursor.getInt(cursor.getColumnIndex("hdtqzsjtimes")));

			history.setHxyodi(cursor.getString(cursor.getColumnIndex("hxyodi")));
			history.setHsmsober(cursor.getString(cursor.getColumnIndex("hsmsober")));
			history.setHsmmax(cursor.getString(cursor.getColumnIndex("hsmmax")));
			// a
			history.setHaxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("haxyfbzsjhour")));
			history.setHaxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("haxyfbzsjminute")));
			history.setHaxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("haxyfbzsjsec")));
			history.setHayjzcsnum(cursor.getString(cursor.getColumnIndex("hayjzcsnum")));
			history.setHaxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("haxyfbzsjtimes")));
			// b
			history.setHbxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjhour")));
			history.setHbxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjminute")));
			history.setHbxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjsec")));
			history.setHbyjzcsnum(cursor.getString(cursor.getColumnIndex("hbyjzcsnum")));
			history.setHbxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjtimes")));
			// c
			history.setHcxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjhour")));
			history.setHcxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjminute")));
			history.setHcxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjsec")));
			history.setHcyjzcsnum(cursor.getString(cursor.getColumnIndex("hcyjzcsnum")));
			history.setHcxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjtimes")));
			// d
			history.setHdxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjhour")));
			history.setHdxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjminute")));
			history.setHdxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjsec")));
			history.setHdyjzcsnum(cursor.getString(cursor.getColumnIndex("hdyjzcsnum")));
			history.setHdxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjtimes")));
			// e
			history.setHexyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hexyfbzsjhour")));
			history.setHexyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hexyfbzsjminute")));
			history.setHexyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hexyfbzsjsec")));
			history.setHeyjzcsnum(cursor.getString(cursor.getColumnIndex("heyjzcsnum")));
			history.setHexyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hexyfbzsjtimes")));
			// f
			history.setHfxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjhour")));
			history.setHfxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjminute")));
			history.setHfxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjsec")));
			history.setHfyjzcsnum(cursor.getString(cursor.getColumnIndex("hfyjzcsnum")));
			history.setHfxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjtimes")));

			history.setFilepaths(cursor.getString(cursor.getColumnIndex("filepaths")));
			history.setUploadfile(cursor.getInt(cursor.getColumnIndex("uploadfile")));
			history.setMacaddress(cursor.getString(cursor.getColumnIndex("macaddress")));
			history.setReportfilepath(cursor.getString(cursor.getColumnIndex("reportfilepath")));
			history.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
			history.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			history.setHistoryupload(cursor.getInt(cursor.getColumnIndex("historyupload")));
			history.setIsFinalReport(cursor.getInt(cursor.getColumnIndex("isFinalReport")));

			history.setHudegree(cursor.getString(cursor.getColumnIndex("hudegree")));
			history.setHscoreHxzb(cursor.getInt(cursor.getColumnIndex("hscoreHxzb")));
			history.setTestdate(cursor.getInt(cursor.getColumnIndex("testdate")));
			history.setReportuuid(cursor.getString(cursor.getColumnIndex("reportuuid")));
			history.setIsComputed(cursor.getInt(cursor.getColumnIndex("iscomputed")));
			history.setCalculationflag(cursor.getInt(cursor.getColumnIndex("calculationflag")));
		}
		db.close();
		return history;

	}

	// 按id查询
	@Override
	public UserHistory findMinHistoryIdByUserId(int userid) {
		UserHistory history = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from historys where userid=? order by _id asc";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(userid) });
		if (cursor.getCount() > 0) {
			cursor.moveToNext();
			history = new UserHistory();
			history.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			history.setHstarthour(cursor.getInt(cursor.getColumnIndex("hstarthour")));
			history.setHstartminute(cursor.getInt(cursor.getColumnIndex("hstartminute")));
			history.setHstarttimes(cursor.getInt(cursor.getColumnIndex("hstarttimes")));
			history.setHendhour(cursor.getInt(cursor.getColumnIndex("hendhour")));
			history.setHendminute(cursor.getInt(cursor.getColumnIndex("hendminute")));
			history.setHendtimes(cursor.getInt(cursor.getColumnIndex("hendtimes")));
			history.setHsmschour(cursor.getInt(cursor.getColumnIndex("hsmschour")));
			history.setHsmscminute(cursor.getInt(cursor.getColumnIndex("hsmscminute")));
			history.setHsmsctimes(cursor.getInt(cursor.getColumnIndex("hsmsctimes")));
			history.setHqshour(cursor.getInt(cursor.getColumnIndex("hqshour")));
			history.setHqsminute(cursor.getInt(cursor.getColumnIndex("hqsminute")));
			history.setHqssctimes(cursor.getInt(cursor.getColumnIndex("hqssctimes")));
			history.setHsshour(cursor.getInt(cursor.getColumnIndex("hsshour")));
			history.setHssminute(cursor.getInt(cursor.getColumnIndex("hssminute")));
			history.setHsssctimes(cursor.getInt(cursor.getColumnIndex("hsssctimes")));
			history.setHqxhour(cursor.getInt(cursor.getColumnIndex("hqxhour")));
			history.setHqxminute(cursor.getInt(cursor.getColumnIndex("hqxminute")));
			history.setHqxsctimes(cursor.getInt(cursor.getColumnIndex("hqxsctimes")));

			history.setHmaxml(cursor.getString(cursor.getColumnIndex("hmaxml")));
			history.setHmaxmlhour(cursor.getInt(cursor.getColumnIndex("hmaxmlhour")));
			history.setHmaxmlminute(cursor.getInt(cursor.getColumnIndex("hmaxmlminute")));
			history.setHmaxmlsec(cursor.getInt(cursor.getColumnIndex("hmaxmlsec")));
			history.setHmaxmltimes(cursor.getInt(cursor.getColumnIndex("hmaxmltimes")));

			history.setHminml(cursor.getString(cursor.getColumnIndex("hminml")));
			history.setHminmlhour(cursor.getInt(cursor.getColumnIndex("hminmlhour")));
			history.setHminmlminute(cursor.getInt(cursor.getColumnIndex("hminmlminute")));
			history.setHminmlsec(cursor.getInt(cursor.getColumnIndex("hminmlsec")));
			history.setHminmltimes(cursor.getInt(cursor.getColumnIndex("hminmltimes")));

			history.setHavgml(cursor.getString(cursor.getColumnIndex("havgml")));
			history.setHsleepscore(cursor.getInt(cursor.getColumnIndex("hsleepscore")));

			history.setHahiIndex(cursor.getString(cursor.getColumnIndex("hahiIndex")));
			history.setHhxztIndex(cursor.getString(cursor.getColumnIndex("hhxztIndex")));
			history.setHdtqIndex(cursor.getString(cursor.getColumnIndex("hdtqIndex")));
			history.setHyjzcsIndex(cursor.getString(cursor.getColumnIndex("hyjzcsIndex")));
			history.setHpjxybhdIndex(cursor.getString(cursor.getColumnIndex("hpjxybhdIndex")));
			history.setHzdxybhdIndex(cursor.getString(cursor.getColumnIndex("hzdxybhdIndex")));
			history.setHxybhdzbIndex(cursor.getString(cursor.getColumnIndex("hxybhdzbIndex")));

			history.setHmaxyjcxhour(cursor.getInt(cursor.getColumnIndex("hmaxyjcxhour")));
			history.setHmaxyjcxminute(cursor.getInt(cursor.getColumnIndex("hmaxyjcxminute")));
			history.setHmaxyjcxsec(cursor.getInt(cursor.getColumnIndex("hmaxyjcxsec")));
			history.setHmaxyjcxtimes(cursor.getInt(cursor.getColumnIndex("hmaxyjcxtimes")));
			history.setHmaxyjfshour(cursor.getInt(cursor.getColumnIndex("hmaxyjfshour")));
			history.setHmaxyjfsminute(cursor.getInt(cursor.getColumnIndex("hmaxyjfsminute")));
			history.setHmaxyjfssec(cursor.getInt(cursor.getColumnIndex("hmaxyjfssec")));
			history.setHmaxyjfstimes(cursor.getInt(cursor.getColumnIndex("hmaxyjfstimes")));
			history.setHlongyjcxhour(cursor.getInt(cursor.getColumnIndex("hlongyjcxhour")));
			history.setHlongyjcxminute(cursor.getInt(cursor.getColumnIndex("hlongyjcxminute")));
			history.setHlongyjcxsec(cursor.getInt(cursor.getColumnIndex("hlongyjcxsec")));
			history.setHlongyjcxtimes(cursor.getInt(cursor.getColumnIndex("hlongyjcxtimes")));
			history.setHlongyjfshour(cursor.getInt(cursor.getColumnIndex("hlongyjfshour")));
			history.setHlongyjfsminute(cursor.getInt(cursor.getColumnIndex("hlongyjfsminute")));
			history.setHlongyjfssec(cursor.getInt(cursor.getColumnIndex("hlongyjfssec")));
			history.setHlongyjfstimes(cursor.getInt(cursor.getColumnIndex("hlongyjfstimes")));

			history.setHxywhzsIndex(cursor.getString(cursor.getColumnIndex("hxywhzsIndex")));
			history.setHavgxlgzd(cursor.getString(cursor.getColumnIndex("havgxlgzd")));
			history.setHosahsdegree(cursor.getString(cursor.getColumnIndex("hosahsdegree")));
			history.setHdyxzdegree(cursor.getString(cursor.getColumnIndex("hdyxzdegree")));
			history.setHyjzsIndex(cursor.getString(cursor.getColumnIndex("hyjzsIndex")));

			history.setHzchxzthour(cursor.getInt(cursor.getColumnIndex("hzchxzthour")));
			history.setHzchxztminute(cursor.getInt(cursor.getColumnIndex("hzchxztminute")));
			history.setHzchxztsec(cursor.getInt(cursor.getColumnIndex("hzchxztsec")));
			history.setHzchxzttimes(cursor.getInt(cursor.getColumnIndex("hzchxzttimes")));
			history.setHhxhapperhour(cursor.getInt(cursor.getColumnIndex("hhxhapperhour")));
			history.setHhxhapperminute(cursor.getInt(cursor.getColumnIndex("hhxhapperminute")));
			history.setHhxhappersec(cursor.getInt(cursor.getColumnIndex("hhxhappersec")));
			history.setHhxhappertimes(cursor.getInt(cursor.getColumnIndex("hhxhappertimes")));
			history.setHhxztzsjhour(cursor.getInt(cursor.getColumnIndex("hhxztzsjhour")));
			history.setHhxztzsjminute(cursor.getInt(cursor.getColumnIndex("hhxztzsjminute")));
			history.setHhxztzsjsec(cursor.getInt(cursor.getColumnIndex("hhxztzsjsec")));
			history.setHhxztzsjtimes(cursor.getInt(cursor.getColumnIndex("hhxztzsjtimes")));
			history.setHzcdtqhour(cursor.getInt(cursor.getColumnIndex("hzcdtqhour")));
			history.setHzcdtqminute(cursor.getInt(cursor.getColumnIndex("hzcdtqminute")));
			history.setHzcdtqsec(cursor.getInt(cursor.getColumnIndex("hzcdtqsec")));
			history.setHzcdtqtimes(cursor.getInt(cursor.getColumnIndex("hzcdtqtimes")));
			history.setHdtqhapperhour(cursor.getInt(cursor.getColumnIndex("hdtqhapperhour")));
			history.setHdtqhapperminute(cursor.getInt(cursor.getColumnIndex("hdtqhapperminute")));
			history.setHdtqhappersec(cursor.getInt(cursor.getColumnIndex("hdtqhappersec")));
			history.setHdtqhappertimes(cursor.getInt(cursor.getColumnIndex("hdtqhappertimes")));
			history.setHdtqzsjhour(cursor.getInt(cursor.getColumnIndex("hdtqzsjhour")));
			history.setHdtqzsjminute(cursor.getInt(cursor.getColumnIndex("hdtqzsjminute")));
			history.setHdtqzsjsec(cursor.getInt(cursor.getColumnIndex("hdtqzsjsec")));
			history.setHdtqzsjtimes(cursor.getInt(cursor.getColumnIndex("hdtqzsjtimes")));

			history.setHxyodi(cursor.getString(cursor.getColumnIndex("hxyodi")));
			history.setHsmsober(cursor.getString(cursor.getColumnIndex("hsmsober")));
			history.setHsmmax(cursor.getString(cursor.getColumnIndex("hsmmax")));
			// a
			history.setHaxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("haxyfbzsjhour")));
			history.setHaxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("haxyfbzsjminute")));
			history.setHaxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("haxyfbzsjsec")));
			history.setHayjzcsnum(cursor.getString(cursor.getColumnIndex("hayjzcsnum")));
			history.setHaxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("haxyfbzsjtimes")));
			// b
			history.setHbxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjhour")));
			history.setHbxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjminute")));
			history.setHbxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjsec")));
			history.setHbyjzcsnum(cursor.getString(cursor.getColumnIndex("hbyjzcsnum")));
			history.setHbxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjtimes")));
			// c
			history.setHcxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjhour")));
			history.setHcxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjminute")));
			history.setHcxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjsec")));
			history.setHcyjzcsnum(cursor.getString(cursor.getColumnIndex("hcyjzcsnum")));
			history.setHcxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjtimes")));
			// d
			history.setHdxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjhour")));
			history.setHdxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjminute")));
			history.setHdxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjsec")));
			history.setHdyjzcsnum(cursor.getString(cursor.getColumnIndex("hdyjzcsnum")));
			history.setHdxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjtimes")));
			// e
			history.setHexyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hexyfbzsjhour")));
			history.setHexyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hexyfbzsjminute")));
			history.setHexyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hexyfbzsjsec")));
			history.setHeyjzcsnum(cursor.getString(cursor.getColumnIndex("heyjzcsnum")));
			history.setHexyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hexyfbzsjtimes")));
			// f
			history.setHfxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjhour")));
			history.setHfxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjminute")));
			history.setHfxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjsec")));
			history.setHfyjzcsnum(cursor.getString(cursor.getColumnIndex("hfyjzcsnum")));
			history.setHfxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjtimes")));

			history.setFilepaths(cursor.getString(cursor.getColumnIndex("filepaths")));
			history.setUploadfile(cursor.getInt(cursor.getColumnIndex("uploadfile")));
			history.setMacaddress(cursor.getString(cursor.getColumnIndex("macaddress")));
			history.setReportfilepath(cursor.getString(cursor.getColumnIndex("reportfilepath")));
			history.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
			history.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			history.setHistoryupload(cursor.getInt(cursor.getColumnIndex("historyupload")));
			history.setIsFinalReport(cursor.getInt(cursor.getColumnIndex("isFinalReport")));

			history.setHudegree(cursor.getString(cursor.getColumnIndex("hudegree")));
			history.setHscoreHxzb(cursor.getInt(cursor.getColumnIndex("hscoreHxzb")));
			history.setTestdate(cursor.getInt(cursor.getColumnIndex("testdate")));
			history.setReportuuid(cursor.getString(cursor.getColumnIndex("reportuuid")));
			history.setIsComputed(cursor.getInt(cursor.getColumnIndex("iscomputed")));
			history.setCalculationflag(cursor.getInt(cursor.getColumnIndex("calculationflag")));
		}
		db.close();
		return history;
	}

	// 删除最小的
	@Override
	public void deletes(int userid) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 删除条件
		String whereClause = "userid=? and _id =(select min(_id) from historys)";
		// 删除条件参数
		String[] whereArgs = { String.valueOf(userid) };
		// 执行删除
		db.delete("historys", whereClause, whereArgs);
		db.close();
		// // 删除SQL语句
		// String sql = "delete from historys where _id =(select min(_id) from
		// historys)";
		// // 执行SQL语句
		// db.execSQL(sql);

	}

	@Override
	public List<UserHistory> findAllHistorysById(int hid) {

		List<UserHistory> all = new ArrayList<UserHistory>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from historys where _id=?";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(hid) });
		while (cursor.moveToNext()) {
			UserHistory history = new UserHistory();
			history.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			history.setHstarthour(cursor.getInt(cursor.getColumnIndex("hstarthour")));
			history.setHstartminute(cursor.getInt(cursor.getColumnIndex("hstartminute")));
			history.setHstarttimes(cursor.getInt(cursor.getColumnIndex("hstarttimes")));
			history.setHendhour(cursor.getInt(cursor.getColumnIndex("hendhour")));
			history.setHendminute(cursor.getInt(cursor.getColumnIndex("hendminute")));
			history.setHendtimes(cursor.getInt(cursor.getColumnIndex("hendtimes")));
			history.setHsmschour(cursor.getInt(cursor.getColumnIndex("hsmschour")));
			history.setHsmscminute(cursor.getInt(cursor.getColumnIndex("hsmscminute")));
			history.setHsmsctimes(cursor.getInt(cursor.getColumnIndex("hsmsctimes")));
			history.setHqshour(cursor.getInt(cursor.getColumnIndex("hqshour")));
			history.setHqsminute(cursor.getInt(cursor.getColumnIndex("hqsminute")));
			history.setHqssctimes(cursor.getInt(cursor.getColumnIndex("hqssctimes")));
			history.setHsshour(cursor.getInt(cursor.getColumnIndex("hsshour")));
			history.setHssminute(cursor.getInt(cursor.getColumnIndex("hssminute")));
			history.setHsssctimes(cursor.getInt(cursor.getColumnIndex("hsssctimes")));
			history.setHqxhour(cursor.getInt(cursor.getColumnIndex("hqxhour")));
			history.setHqxminute(cursor.getInt(cursor.getColumnIndex("hqxminute")));
			history.setHqxsctimes(cursor.getInt(cursor.getColumnIndex("hqxsctimes")));

			history.setHmaxml(cursor.getString(cursor.getColumnIndex("hmaxml")));
			history.setHmaxmlhour(cursor.getInt(cursor.getColumnIndex("hmaxmlhour")));
			history.setHmaxmlminute(cursor.getInt(cursor.getColumnIndex("hmaxmlminute")));
			history.setHmaxmlsec(cursor.getInt(cursor.getColumnIndex("hmaxmlsec")));
			history.setHmaxmltimes(cursor.getInt(cursor.getColumnIndex("hmaxmltimes")));

			history.setHminml(cursor.getString(cursor.getColumnIndex("hminml")));
			history.setHminmlhour(cursor.getInt(cursor.getColumnIndex("hminmlhour")));
			history.setHminmlminute(cursor.getInt(cursor.getColumnIndex("hminmlminute")));
			history.setHminmlsec(cursor.getInt(cursor.getColumnIndex("hminmlsec")));
			history.setHminmltimes(cursor.getInt(cursor.getColumnIndex("hminmltimes")));

			history.setHavgml(cursor.getString(cursor.getColumnIndex("havgml")));
			history.setHsleepscore(cursor.getInt(cursor.getColumnIndex("hsleepscore")));

			history.setHahiIndex(cursor.getString(cursor.getColumnIndex("hahiIndex")));
			history.setHhxztIndex(cursor.getString(cursor.getColumnIndex("hhxztIndex")));
			history.setHdtqIndex(cursor.getString(cursor.getColumnIndex("hdtqIndex")));
			history.setHyjzcsIndex(cursor.getString(cursor.getColumnIndex("hyjzcsIndex")));
			history.setHpjxybhdIndex(cursor.getString(cursor.getColumnIndex("hpjxybhdIndex")));
			history.setHzdxybhdIndex(cursor.getString(cursor.getColumnIndex("hzdxybhdIndex")));
			history.setHxybhdzbIndex(cursor.getString(cursor.getColumnIndex("hxybhdzbIndex")));

			history.setHmaxyjcxhour(cursor.getInt(cursor.getColumnIndex("hmaxyjcxhour")));
			history.setHmaxyjcxminute(cursor.getInt(cursor.getColumnIndex("hmaxyjcxminute")));
			history.setHmaxyjcxsec(cursor.getInt(cursor.getColumnIndex("hmaxyjcxsec")));
			history.setHmaxyjcxtimes(cursor.getInt(cursor.getColumnIndex("hmaxyjcxtimes")));
			history.setHmaxyjfshour(cursor.getInt(cursor.getColumnIndex("hmaxyjfshour")));
			history.setHmaxyjfsminute(cursor.getInt(cursor.getColumnIndex("hmaxyjfsminute")));
			history.setHmaxyjfssec(cursor.getInt(cursor.getColumnIndex("hmaxyjfssec")));
			history.setHmaxyjfstimes(cursor.getInt(cursor.getColumnIndex("hmaxyjfstimes")));
			history.setHlongyjcxhour(cursor.getInt(cursor.getColumnIndex("hlongyjcxhour")));
			history.setHlongyjcxminute(cursor.getInt(cursor.getColumnIndex("hlongyjcxminute")));
			history.setHlongyjcxsec(cursor.getInt(cursor.getColumnIndex("hlongyjcxsec")));
			history.setHlongyjcxtimes(cursor.getInt(cursor.getColumnIndex("hlongyjcxtimes")));
			history.setHlongyjfshour(cursor.getInt(cursor.getColumnIndex("hlongyjfshour")));
			history.setHlongyjfsminute(cursor.getInt(cursor.getColumnIndex("hlongyjfsminute")));
			history.setHlongyjfssec(cursor.getInt(cursor.getColumnIndex("hlongyjfssec")));
			history.setHlongyjfstimes(cursor.getInt(cursor.getColumnIndex("hlongyjfstimes")));

			history.setHxywhzsIndex(cursor.getString(cursor.getColumnIndex("hxywhzsIndex")));
			history.setHavgxlgzd(cursor.getString(cursor.getColumnIndex("havgxlgzd")));
			history.setHosahsdegree(cursor.getString(cursor.getColumnIndex("hosahsdegree")));
			history.setHdyxzdegree(cursor.getString(cursor.getColumnIndex("hdyxzdegree")));
			history.setHyjzsIndex(cursor.getString(cursor.getColumnIndex("hyjzsIndex")));

			history.setHzchxzthour(cursor.getInt(cursor.getColumnIndex("hzchxzthour")));
			history.setHzchxztminute(cursor.getInt(cursor.getColumnIndex("hzchxztminute")));
			history.setHzchxztsec(cursor.getInt(cursor.getColumnIndex("hzchxztsec")));
			history.setHzchxzttimes(cursor.getInt(cursor.getColumnIndex("hzchxzttimes")));
			history.setHhxhapperhour(cursor.getInt(cursor.getColumnIndex("hhxhapperhour")));
			history.setHhxhapperminute(cursor.getInt(cursor.getColumnIndex("hhxhapperminute")));
			history.setHhxhappersec(cursor.getInt(cursor.getColumnIndex("hhxhappersec")));
			history.setHhxhappertimes(cursor.getInt(cursor.getColumnIndex("hhxhappertimes")));
			history.setHhxztzsjhour(cursor.getInt(cursor.getColumnIndex("hhxztzsjhour")));
			history.setHhxztzsjminute(cursor.getInt(cursor.getColumnIndex("hhxztzsjminute")));
			history.setHhxztzsjsec(cursor.getInt(cursor.getColumnIndex("hhxztzsjsec")));
			history.setHhxztzsjtimes(cursor.getInt(cursor.getColumnIndex("hhxztzsjtimes")));
			history.setHzcdtqhour(cursor.getInt(cursor.getColumnIndex("hzcdtqhour")));
			history.setHzcdtqminute(cursor.getInt(cursor.getColumnIndex("hzcdtqminute")));
			history.setHzcdtqsec(cursor.getInt(cursor.getColumnIndex("hzcdtqsec")));
			history.setHzcdtqtimes(cursor.getInt(cursor.getColumnIndex("hzcdtqtimes")));
			history.setHdtqhapperhour(cursor.getInt(cursor.getColumnIndex("hdtqhapperhour")));
			history.setHdtqhapperminute(cursor.getInt(cursor.getColumnIndex("hdtqhapperminute")));
			history.setHdtqhappersec(cursor.getInt(cursor.getColumnIndex("hdtqhappersec")));
			history.setHdtqhappertimes(cursor.getInt(cursor.getColumnIndex("hdtqhappertimes")));
			history.setHdtqzsjhour(cursor.getInt(cursor.getColumnIndex("hdtqzsjhour")));
			history.setHdtqzsjminute(cursor.getInt(cursor.getColumnIndex("hdtqzsjminute")));
			history.setHdtqzsjsec(cursor.getInt(cursor.getColumnIndex("hdtqzsjsec")));
			history.setHdtqzsjtimes(cursor.getInt(cursor.getColumnIndex("hdtqzsjtimes")));

			history.setHxyodi(cursor.getString(cursor.getColumnIndex("hxyodi")));
			history.setHsmsober(cursor.getString(cursor.getColumnIndex("hsmsober")));
			history.setHsmmax(cursor.getString(cursor.getColumnIndex("hsmmax")));
			// a
			history.setHaxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("haxyfbzsjhour")));
			history.setHaxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("haxyfbzsjminute")));
			history.setHaxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("haxyfbzsjsec")));
			history.setHayjzcsnum(cursor.getString(cursor.getColumnIndex("hayjzcsnum")));
			history.setHaxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("haxyfbzsjtimes")));
			// b
			history.setHbxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjhour")));
			history.setHbxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjminute")));
			history.setHbxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjsec")));
			history.setHbyjzcsnum(cursor.getString(cursor.getColumnIndex("hbyjzcsnum")));
			history.setHbxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjtimes")));
			// c
			history.setHcxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjhour")));
			history.setHcxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjminute")));
			history.setHcxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjsec")));
			history.setHcyjzcsnum(cursor.getString(cursor.getColumnIndex("hcyjzcsnum")));
			history.setHcxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjtimes")));
			// d
			history.setHdxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjhour")));
			history.setHdxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjminute")));
			history.setHdxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjsec")));
			history.setHdyjzcsnum(cursor.getString(cursor.getColumnIndex("hdyjzcsnum")));
			history.setHdxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjtimes")));
			// e
			history.setHexyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hexyfbzsjhour")));
			history.setHexyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hexyfbzsjminute")));
			history.setHexyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hexyfbzsjsec")));
			history.setHeyjzcsnum(cursor.getString(cursor.getColumnIndex("heyjzcsnum")));
			history.setHexyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hexyfbzsjtimes")));
			// f
			history.setHfxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjhour")));
			history.setHfxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjminute")));
			history.setHfxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjsec")));
			history.setHfyjzcsnum(cursor.getString(cursor.getColumnIndex("hfyjzcsnum")));
			history.setHfxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjtimes")));

			history.setFilepaths(cursor.getString(cursor.getColumnIndex("filepaths")));
			history.setUploadfile(cursor.getInt(cursor.getColumnIndex("uploadfile")));
			history.setMacaddress(cursor.getString(cursor.getColumnIndex("macaddress")));
			history.setReportfilepath(cursor.getString(cursor.getColumnIndex("reportfilepath")));
			history.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
			history.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			history.setHistoryupload(cursor.getInt(cursor.getColumnIndex("historyupload")));
			history.setIsFinalReport(cursor.getInt(cursor.getColumnIndex("isFinalReport")));

			history.setHudegree(cursor.getString(cursor.getColumnIndex("hudegree")));
			history.setHscoreHxzb(cursor.getInt(cursor.getColumnIndex("hscoreHxzb")));
			history.setTestdate(cursor.getInt(cursor.getColumnIndex("testdate")));
			history.setReportuuid(cursor.getString(cursor.getColumnIndex("reportuuid")));
			history.setIsComputed(cursor.getInt(cursor.getColumnIndex("iscomputed")));
			history.setCalculationflag(cursor.getInt(cursor.getColumnIndex("calculationflag")));
			all.add(history);
		}
		db.close();
		return all;

	}

	@Override
	public List<UserHistory> findAllUnuploadFile() {
		List<UserHistory> all = new ArrayList<UserHistory>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from historys where uploadfile=0";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				UserHistory history = new UserHistory();
				history.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				history.setHstarthour(cursor.getInt(cursor.getColumnIndex("hstarthour")));
				history.setHstartminute(cursor.getInt(cursor.getColumnIndex("hstartminute")));
				history.setHstarttimes(cursor.getInt(cursor.getColumnIndex("hstarttimes")));
				history.setHendhour(cursor.getInt(cursor.getColumnIndex("hendhour")));
				history.setHendminute(cursor.getInt(cursor.getColumnIndex("hendminute")));
				history.setHendtimes(cursor.getInt(cursor.getColumnIndex("hendtimes")));
				history.setHsmschour(cursor.getInt(cursor.getColumnIndex("hsmschour")));
				history.setHsmscminute(cursor.getInt(cursor.getColumnIndex("hsmscminute")));
				history.setHsmsctimes(cursor.getInt(cursor.getColumnIndex("hsmsctimes")));
				history.setHqshour(cursor.getInt(cursor.getColumnIndex("hqshour")));
				history.setHqsminute(cursor.getInt(cursor.getColumnIndex("hqsminute")));
				history.setHqssctimes(cursor.getInt(cursor.getColumnIndex("hqssctimes")));
				history.setHsshour(cursor.getInt(cursor.getColumnIndex("hsshour")));
				history.setHssminute(cursor.getInt(cursor.getColumnIndex("hssminute")));
				history.setHsssctimes(cursor.getInt(cursor.getColumnIndex("hsssctimes")));
				history.setHqxhour(cursor.getInt(cursor.getColumnIndex("hqxhour")));
				history.setHqxminute(cursor.getInt(cursor.getColumnIndex("hqxminute")));
				history.setHqxsctimes(cursor.getInt(cursor.getColumnIndex("hqxsctimes")));

				history.setHmaxml(cursor.getString(cursor.getColumnIndex("hmaxml")));
				history.setHmaxmlhour(cursor.getInt(cursor.getColumnIndex("hmaxmlhour")));
				history.setHmaxmlminute(cursor.getInt(cursor.getColumnIndex("hmaxmlminute")));
				history.setHmaxmlsec(cursor.getInt(cursor.getColumnIndex("hmaxmlsec")));
				history.setHmaxmltimes(cursor.getInt(cursor.getColumnIndex("hmaxmltimes")));

				history.setHminml(cursor.getString(cursor.getColumnIndex("hminml")));
				history.setHminmlhour(cursor.getInt(cursor.getColumnIndex("hminmlhour")));
				history.setHminmlminute(cursor.getInt(cursor.getColumnIndex("hminmlminute")));
				history.setHminmlsec(cursor.getInt(cursor.getColumnIndex("hminmlsec")));
				history.setHminmltimes(cursor.getInt(cursor.getColumnIndex("hminmltimes")));

				history.setHavgml(cursor.getString(cursor.getColumnIndex("havgml")));
				history.setHsleepscore(cursor.getInt(cursor.getColumnIndex("hsleepscore")));

				history.setHahiIndex(cursor.getString(cursor.getColumnIndex("hahiIndex")));
				history.setHhxztIndex(cursor.getString(cursor.getColumnIndex("hhxztIndex")));
				history.setHdtqIndex(cursor.getString(cursor.getColumnIndex("hdtqIndex")));
				history.setHyjzcsIndex(cursor.getString(cursor.getColumnIndex("hyjzcsIndex")));
				history.setHpjxybhdIndex(cursor.getString(cursor.getColumnIndex("hpjxybhdIndex")));
				history.setHzdxybhdIndex(cursor.getString(cursor.getColumnIndex("hzdxybhdIndex")));
				history.setHxybhdzbIndex(cursor.getString(cursor.getColumnIndex("hxybhdzbIndex")));

				history.setHmaxyjcxhour(cursor.getInt(cursor.getColumnIndex("hmaxyjcxhour")));
				history.setHmaxyjcxminute(cursor.getInt(cursor.getColumnIndex("hmaxyjcxminute")));
				history.setHmaxyjcxsec(cursor.getInt(cursor.getColumnIndex("hmaxyjcxsec")));
				history.setHmaxyjcxtimes(cursor.getInt(cursor.getColumnIndex("hmaxyjcxtimes")));
				history.setHmaxyjfshour(cursor.getInt(cursor.getColumnIndex("hmaxyjfshour")));
				history.setHmaxyjfsminute(cursor.getInt(cursor.getColumnIndex("hmaxyjfsminute")));
				history.setHmaxyjfssec(cursor.getInt(cursor.getColumnIndex("hmaxyjfssec")));
				history.setHmaxyjfstimes(cursor.getInt(cursor.getColumnIndex("hmaxyjfstimes")));
				history.setHlongyjcxhour(cursor.getInt(cursor.getColumnIndex("hlongyjcxhour")));
				history.setHlongyjcxminute(cursor.getInt(cursor.getColumnIndex("hlongyjcxminute")));
				history.setHlongyjcxsec(cursor.getInt(cursor.getColumnIndex("hlongyjcxsec")));
				history.setHlongyjcxtimes(cursor.getInt(cursor.getColumnIndex("hlongyjcxtimes")));
				history.setHlongyjfshour(cursor.getInt(cursor.getColumnIndex("hlongyjfshour")));
				history.setHlongyjfsminute(cursor.getInt(cursor.getColumnIndex("hlongyjfsminute")));
				history.setHlongyjfssec(cursor.getInt(cursor.getColumnIndex("hlongyjfssec")));
				history.setHlongyjfstimes(cursor.getInt(cursor.getColumnIndex("hlongyjfstimes")));

				history.setHxywhzsIndex(cursor.getString(cursor.getColumnIndex("hxywhzsIndex")));
				history.setHavgxlgzd(cursor.getString(cursor.getColumnIndex("havgxlgzd")));
				history.setHosahsdegree(cursor.getString(cursor.getColumnIndex("hosahsdegree")));
				history.setHdyxzdegree(cursor.getString(cursor.getColumnIndex("hdyxzdegree")));
				history.setHyjzsIndex(cursor.getString(cursor.getColumnIndex("hyjzsIndex")));

				history.setHzchxzthour(cursor.getInt(cursor.getColumnIndex("hzchxzthour")));
				history.setHzchxztminute(cursor.getInt(cursor.getColumnIndex("hzchxztminute")));
				history.setHzchxztsec(cursor.getInt(cursor.getColumnIndex("hzchxztsec")));
				history.setHzchxzttimes(cursor.getInt(cursor.getColumnIndex("hzchxzttimes")));
				history.setHhxhapperhour(cursor.getInt(cursor.getColumnIndex("hhxhapperhour")));
				history.setHhxhapperminute(cursor.getInt(cursor.getColumnIndex("hhxhapperminute")));
				history.setHhxhappersec(cursor.getInt(cursor.getColumnIndex("hhxhappersec")));
				history.setHhxhappertimes(cursor.getInt(cursor.getColumnIndex("hhxhappertimes")));
				history.setHhxztzsjhour(cursor.getInt(cursor.getColumnIndex("hhxztzsjhour")));
				history.setHhxztzsjminute(cursor.getInt(cursor.getColumnIndex("hhxztzsjminute")));
				history.setHhxztzsjsec(cursor.getInt(cursor.getColumnIndex("hhxztzsjsec")));
				history.setHhxztzsjtimes(cursor.getInt(cursor.getColumnIndex("hhxztzsjtimes")));
				history.setHzcdtqhour(cursor.getInt(cursor.getColumnIndex("hzcdtqhour")));
				history.setHzcdtqminute(cursor.getInt(cursor.getColumnIndex("hzcdtqminute")));
				history.setHzcdtqsec(cursor.getInt(cursor.getColumnIndex("hzcdtqsec")));
				history.setHzcdtqtimes(cursor.getInt(cursor.getColumnIndex("hzcdtqtimes")));
				history.setHdtqhapperhour(cursor.getInt(cursor.getColumnIndex("hdtqhapperhour")));
				history.setHdtqhapperminute(cursor.getInt(cursor.getColumnIndex("hdtqhapperminute")));
				history.setHdtqhappersec(cursor.getInt(cursor.getColumnIndex("hdtqhappersec")));
				history.setHdtqhappertimes(cursor.getInt(cursor.getColumnIndex("hdtqhappertimes")));
				history.setHdtqzsjhour(cursor.getInt(cursor.getColumnIndex("hdtqzsjhour")));
				history.setHdtqzsjminute(cursor.getInt(cursor.getColumnIndex("hdtqzsjminute")));
				history.setHdtqzsjsec(cursor.getInt(cursor.getColumnIndex("hdtqzsjsec")));
				history.setHdtqzsjtimes(cursor.getInt(cursor.getColumnIndex("hdtqzsjtimes")));

				history.setHxyodi(cursor.getString(cursor.getColumnIndex("hxyodi")));
				history.setHsmsober(cursor.getString(cursor.getColumnIndex("hsmsober")));
				history.setHsmmax(cursor.getString(cursor.getColumnIndex("hsmmax")));
				// a
				history.setHaxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("haxyfbzsjhour")));
				history.setHaxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("haxyfbzsjminute")));
				history.setHaxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("haxyfbzsjsec")));
				history.setHayjzcsnum(cursor.getString(cursor.getColumnIndex("hayjzcsnum")));
				history.setHaxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("haxyfbzsjtimes")));
				// b
				history.setHbxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjhour")));
				history.setHbxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjminute")));
				history.setHbxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjsec")));
				history.setHbyjzcsnum(cursor.getString(cursor.getColumnIndex("hbyjzcsnum")));
				history.setHbxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjtimes")));
				// c
				history.setHcxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjhour")));
				history.setHcxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjminute")));
				history.setHcxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjsec")));
				history.setHcyjzcsnum(cursor.getString(cursor.getColumnIndex("hcyjzcsnum")));
				history.setHcxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjtimes")));
				// d
				history.setHdxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjhour")));
				history.setHdxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjminute")));
				history.setHdxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjsec")));
				history.setHdyjzcsnum(cursor.getString(cursor.getColumnIndex("hdyjzcsnum")));
				history.setHdxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjtimes")));
				// e
				history.setHexyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hexyfbzsjhour")));
				history.setHexyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hexyfbzsjminute")));
				history.setHexyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hexyfbzsjsec")));
				history.setHeyjzcsnum(cursor.getString(cursor.getColumnIndex("heyjzcsnum")));
				history.setHexyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hexyfbzsjtimes")));
				// f
				history.setHfxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjhour")));
				history.setHfxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjminute")));
				history.setHfxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjsec")));
				history.setHfyjzcsnum(cursor.getString(cursor.getColumnIndex("hfyjzcsnum")));
				history.setHfxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjtimes")));

				history.setFilepaths(cursor.getString(cursor.getColumnIndex("filepaths")));
				history.setUploadfile(cursor.getInt(cursor.getColumnIndex("uploadfile")));
				history.setMacaddress(cursor.getString(cursor.getColumnIndex("macaddress")));
				history.setReportfilepath(cursor.getString(cursor.getColumnIndex("reportfilepath")));
				history.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
				history.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
				history.setHistoryupload(cursor.getInt(cursor.getColumnIndex("historyupload")));
				history.setIsFinalReport(cursor.getInt(cursor.getColumnIndex("isFinalReport")));

				history.setHudegree(cursor.getString(cursor.getColumnIndex("hudegree")));
				history.setHscoreHxzb(cursor.getInt(cursor.getColumnIndex("hscoreHxzb")));
				history.setTestdate(cursor.getInt(cursor.getColumnIndex("testdate")));
				history.setReportuuid(cursor.getString(cursor.getColumnIndex("reportuuid")));
				history.setIsComputed(cursor.getInt(cursor.getColumnIndex("iscomputed")));
				history.setCalculationflag(cursor.getInt(cursor.getColumnIndex("calculationflag")));
				all.add(history);
			}
		}
		db.close();
		return all;
	}

	@Override
	public List<UserHistory> findAllWebUploadUserHistory() {
		List<UserHistory> all = new ArrayList<UserHistory>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from historys where historyupload=0";
		Cursor cursor = db.rawQuery(sql, null);
		Log.d(Utils.TAG, "findAllWebUploadUserHistory, cursor.getColumnCount() = " + cursor.getColumnCount());
		Log.d(Utils.TAG, "findAllWebUploadUserHistory, cursor.getCount() = " + cursor.getCount());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				UserHistory history = new UserHistory();
				history.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				history.setHstarthour(cursor.getInt(cursor.getColumnIndex("hstarthour")));
				history.setHstartminute(cursor.getInt(cursor.getColumnIndex("hstartminute")));
				history.setHstarttimes(cursor.getInt(cursor.getColumnIndex("hstarttimes")));
				history.setHendhour(cursor.getInt(cursor.getColumnIndex("hendhour")));
				history.setHendminute(cursor.getInt(cursor.getColumnIndex("hendminute")));
				history.setHendtimes(cursor.getInt(cursor.getColumnIndex("hendtimes")));
				history.setHsmschour(cursor.getInt(cursor.getColumnIndex("hsmschour")));
				history.setHsmscminute(cursor.getInt(cursor.getColumnIndex("hsmscminute")));
				history.setHsmsctimes(cursor.getInt(cursor.getColumnIndex("hsmsctimes")));
				history.setHqshour(cursor.getInt(cursor.getColumnIndex("hqshour")));
				history.setHqsminute(cursor.getInt(cursor.getColumnIndex("hqsminute")));
				history.setHqssctimes(cursor.getInt(cursor.getColumnIndex("hqssctimes")));
				history.setHsshour(cursor.getInt(cursor.getColumnIndex("hsshour")));
				history.setHssminute(cursor.getInt(cursor.getColumnIndex("hssminute")));
				history.setHsssctimes(cursor.getInt(cursor.getColumnIndex("hsssctimes")));
				history.setHqxhour(cursor.getInt(cursor.getColumnIndex("hqxhour")));
				history.setHqxminute(cursor.getInt(cursor.getColumnIndex("hqxminute")));
				history.setHqxsctimes(cursor.getInt(cursor.getColumnIndex("hqxsctimes")));

				history.setHmaxml(cursor.getString(cursor.getColumnIndex("hmaxml")));
				history.setHmaxmlhour(cursor.getInt(cursor.getColumnIndex("hmaxmlhour")));
				history.setHmaxmlminute(cursor.getInt(cursor.getColumnIndex("hmaxmlminute")));
				history.setHmaxmlsec(cursor.getInt(cursor.getColumnIndex("hmaxmlsec")));
				history.setHmaxmltimes(cursor.getInt(cursor.getColumnIndex("hmaxmltimes")));

				history.setHminml(cursor.getString(cursor.getColumnIndex("hminml")));
				history.setHminmlhour(cursor.getInt(cursor.getColumnIndex("hminmlhour")));
				history.setHminmlminute(cursor.getInt(cursor.getColumnIndex("hminmlminute")));
				history.setHminmlsec(cursor.getInt(cursor.getColumnIndex("hminmlsec")));
				history.setHminmltimes(cursor.getInt(cursor.getColumnIndex("hminmltimes")));

				history.setHavgml(cursor.getString(cursor.getColumnIndex("havgml")));
				history.setHsleepscore(cursor.getInt(cursor.getColumnIndex("hsleepscore")));

				history.setHahiIndex(cursor.getString(cursor.getColumnIndex("hahiIndex")));
				history.setHhxztIndex(cursor.getString(cursor.getColumnIndex("hhxztIndex")));
				history.setHdtqIndex(cursor.getString(cursor.getColumnIndex("hdtqIndex")));
				history.setHyjzcsIndex(cursor.getString(cursor.getColumnIndex("hyjzcsIndex")));
				history.setHpjxybhdIndex(cursor.getString(cursor.getColumnIndex("hpjxybhdIndex")));
				history.setHzdxybhdIndex(cursor.getString(cursor.getColumnIndex("hzdxybhdIndex")));
				history.setHxybhdzbIndex(cursor.getString(cursor.getColumnIndex("hxybhdzbIndex")));

				history.setHmaxyjcxhour(cursor.getInt(cursor.getColumnIndex("hmaxyjcxhour")));
				history.setHmaxyjcxminute(cursor.getInt(cursor.getColumnIndex("hmaxyjcxminute")));
				history.setHmaxyjcxsec(cursor.getInt(cursor.getColumnIndex("hmaxyjcxsec")));
				history.setHmaxyjcxtimes(cursor.getInt(cursor.getColumnIndex("hmaxyjcxtimes")));
				history.setHmaxyjfshour(cursor.getInt(cursor.getColumnIndex("hmaxyjfshour")));
				history.setHmaxyjfsminute(cursor.getInt(cursor.getColumnIndex("hmaxyjfsminute")));
				history.setHmaxyjfssec(cursor.getInt(cursor.getColumnIndex("hmaxyjfssec")));
				history.setHmaxyjfstimes(cursor.getInt(cursor.getColumnIndex("hmaxyjfstimes")));
				history.setHlongyjcxhour(cursor.getInt(cursor.getColumnIndex("hlongyjcxhour")));
				history.setHlongyjcxminute(cursor.getInt(cursor.getColumnIndex("hlongyjcxminute")));
				history.setHlongyjcxsec(cursor.getInt(cursor.getColumnIndex("hlongyjcxsec")));
				history.setHlongyjcxtimes(cursor.getInt(cursor.getColumnIndex("hlongyjcxtimes")));
				history.setHlongyjfshour(cursor.getInt(cursor.getColumnIndex("hlongyjfshour")));
				history.setHlongyjfsminute(cursor.getInt(cursor.getColumnIndex("hlongyjfsminute")));
				history.setHlongyjfssec(cursor.getInt(cursor.getColumnIndex("hlongyjfssec")));
				history.setHlongyjfstimes(cursor.getInt(cursor.getColumnIndex("hlongyjfstimes")));

				history.setHxywhzsIndex(cursor.getString(cursor.getColumnIndex("hxywhzsIndex")));
				history.setHavgxlgzd(cursor.getString(cursor.getColumnIndex("havgxlgzd")));
				history.setHosahsdegree(cursor.getString(cursor.getColumnIndex("hosahsdegree")));
				history.setHdyxzdegree(cursor.getString(cursor.getColumnIndex("hdyxzdegree")));
				history.setHyjzsIndex(cursor.getString(cursor.getColumnIndex("hyjzsIndex")));

				history.setHzchxzthour(cursor.getInt(cursor.getColumnIndex("hzchxzthour")));
				history.setHzchxztminute(cursor.getInt(cursor.getColumnIndex("hzchxztminute")));
				history.setHzchxztsec(cursor.getInt(cursor.getColumnIndex("hzchxztsec")));
				history.setHzchxzttimes(cursor.getInt(cursor.getColumnIndex("hzchxzttimes")));
				history.setHhxhapperhour(cursor.getInt(cursor.getColumnIndex("hhxhapperhour")));
				history.setHhxhapperminute(cursor.getInt(cursor.getColumnIndex("hhxhapperminute")));
				history.setHhxhappersec(cursor.getInt(cursor.getColumnIndex("hhxhappersec")));
				history.setHhxhappertimes(cursor.getInt(cursor.getColumnIndex("hhxhappertimes")));
				history.setHhxztzsjhour(cursor.getInt(cursor.getColumnIndex("hhxztzsjhour")));
				history.setHhxztzsjminute(cursor.getInt(cursor.getColumnIndex("hhxztzsjminute")));
				history.setHhxztzsjsec(cursor.getInt(cursor.getColumnIndex("hhxztzsjsec")));
				history.setHhxztzsjtimes(cursor.getInt(cursor.getColumnIndex("hhxztzsjtimes")));
				history.setHzcdtqhour(cursor.getInt(cursor.getColumnIndex("hzcdtqhour")));
				history.setHzcdtqminute(cursor.getInt(cursor.getColumnIndex("hzcdtqminute")));
				history.setHzcdtqsec(cursor.getInt(cursor.getColumnIndex("hzcdtqsec")));
				history.setHzcdtqtimes(cursor.getInt(cursor.getColumnIndex("hzcdtqtimes")));
				history.setHdtqhapperhour(cursor.getInt(cursor.getColumnIndex("hdtqhapperhour")));
				history.setHdtqhapperminute(cursor.getInt(cursor.getColumnIndex("hdtqhapperminute")));
				history.setHdtqhappersec(cursor.getInt(cursor.getColumnIndex("hdtqhappersec")));
				history.setHdtqhappertimes(cursor.getInt(cursor.getColumnIndex("hdtqhappertimes")));
				history.setHdtqzsjhour(cursor.getInt(cursor.getColumnIndex("hdtqzsjhour")));
				history.setHdtqzsjminute(cursor.getInt(cursor.getColumnIndex("hdtqzsjminute")));
				history.setHdtqzsjsec(cursor.getInt(cursor.getColumnIndex("hdtqzsjsec")));
				history.setHdtqzsjtimes(cursor.getInt(cursor.getColumnIndex("hdtqzsjtimes")));

				history.setHxyodi(cursor.getString(cursor.getColumnIndex("hxyodi")));
				history.setHsmsober(cursor.getString(cursor.getColumnIndex("hsmsober")));
				history.setHsmmax(cursor.getString(cursor.getColumnIndex("hsmmax")));
				// a
				history.setHaxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("haxyfbzsjhour")));
				history.setHaxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("haxyfbzsjminute")));
				history.setHaxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("haxyfbzsjsec")));
				history.setHayjzcsnum(cursor.getString(cursor.getColumnIndex("hayjzcsnum")));
				history.setHaxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("haxyfbzsjtimes")));
				// b
				history.setHbxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjhour")));
				history.setHbxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjminute")));
				history.setHbxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjsec")));
				history.setHbyjzcsnum(cursor.getString(cursor.getColumnIndex("hbyjzcsnum")));
				history.setHbxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjtimes")));
				// c
				history.setHcxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjhour")));
				history.setHcxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjminute")));
				history.setHcxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjsec")));
				history.setHcyjzcsnum(cursor.getString(cursor.getColumnIndex("hcyjzcsnum")));
				history.setHcxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjtimes")));
				// d
				history.setHdxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjhour")));
				history.setHdxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjminute")));
				history.setHdxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjsec")));
				history.setHdyjzcsnum(cursor.getString(cursor.getColumnIndex("hdyjzcsnum")));
				history.setHdxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjtimes")));
				// e
				history.setHexyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hexyfbzsjhour")));
				history.setHexyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hexyfbzsjminute")));
				history.setHexyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hexyfbzsjsec")));
				history.setHeyjzcsnum(cursor.getString(cursor.getColumnIndex("heyjzcsnum")));
				history.setHexyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hexyfbzsjtimes")));
				// f
				history.setHfxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjhour")));
				history.setHfxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjminute")));
				history.setHfxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjsec")));
				history.setHfyjzcsnum(cursor.getString(cursor.getColumnIndex("hfyjzcsnum")));
				history.setHfxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjtimes")));

				history.setFilepaths(cursor.getString(cursor.getColumnIndex("filepaths")));
				history.setUploadfile(cursor.getInt(cursor.getColumnIndex("uploadfile")));
				history.setMacaddress(cursor.getString(cursor.getColumnIndex("macaddress")));
				history.setReportfilepath(cursor.getString(cursor.getColumnIndex("reportfilepath")));
				history.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
				history.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
				history.setHistoryupload(cursor.getInt(cursor.getColumnIndex("historyupload")));
				history.setIsFinalReport(cursor.getInt(cursor.getColumnIndex("isFinalReport")));

				history.setHudegree(cursor.getString(cursor.getColumnIndex("hudegree")));
				history.setHscoreHxzb(cursor.getInt(cursor.getColumnIndex("hscoreHxzb")));
				history.setTestdate(cursor.getInt(cursor.getColumnIndex("testdate")));
				history.setReportuuid(cursor.getString(cursor.getColumnIndex("reportuuid")));
				history.setIsComputed(cursor.getInt(cursor.getColumnIndex("iscomputed")));
				history.setCalculationflag(cursor.getInt(cursor.getColumnIndex("calculationflag")));
				all.add(history);
			}
		}
		db.close();
		return all;
	}

	@Override
	public List<UserHistory> findAllHistoryByUserId(int userid) {

		List<UserHistory> all = new ArrayList<UserHistory>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from historys where userid=?";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(userid) });
		while (cursor.moveToNext()) {
			UserHistory history = new UserHistory();
			history.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			history.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
			history.setHstarthour(cursor.getInt(cursor.getColumnIndex("hstarthour")));
			history.setHstartminute(cursor.getInt(cursor.getColumnIndex("hstartminute")));
			history.setHstarttimes(cursor.getInt(cursor.getColumnIndex("hstarttimes")));
			history.setHendhour(cursor.getInt(cursor.getColumnIndex("hendhour")));
			history.setHendminute(cursor.getInt(cursor.getColumnIndex("hendminute")));
			history.setHendtimes(cursor.getInt(cursor.getColumnIndex("hendtimes")));
			history.setHsmschour(cursor.getInt(cursor.getColumnIndex("hsmschour")));
			history.setHsmscminute(cursor.getInt(cursor.getColumnIndex("hsmscminute")));
			history.setHsmsctimes(cursor.getInt(cursor.getColumnIndex("hsmsctimes")));
			history.setHqshour(cursor.getInt(cursor.getColumnIndex("hqshour")));
			history.setHqsminute(cursor.getInt(cursor.getColumnIndex("hqsminute")));
			history.setHqssctimes(cursor.getInt(cursor.getColumnIndex("hqssctimes")));
			history.setHsshour(cursor.getInt(cursor.getColumnIndex("hsshour")));
			history.setHssminute(cursor.getInt(cursor.getColumnIndex("hssminute")));
			history.setHsssctimes(cursor.getInt(cursor.getColumnIndex("hsssctimes")));
			history.setHqxhour(cursor.getInt(cursor.getColumnIndex("hqxhour")));
			history.setHqxminute(cursor.getInt(cursor.getColumnIndex("hqxminute")));
			history.setHqxsctimes(cursor.getInt(cursor.getColumnIndex("hqxsctimes")));

			history.setHmaxml(cursor.getString(cursor.getColumnIndex("hmaxml")));
			history.setHmaxmlhour(cursor.getInt(cursor.getColumnIndex("hmaxmlhour")));
			history.setHmaxmlminute(cursor.getInt(cursor.getColumnIndex("hmaxmlminute")));
			history.setHmaxmlsec(cursor.getInt(cursor.getColumnIndex("hmaxmlsec")));
			history.setHmaxmltimes(cursor.getInt(cursor.getColumnIndex("hmaxmltimes")));

			history.setHminml(cursor.getString(cursor.getColumnIndex("hminml")));
			history.setHminmlhour(cursor.getInt(cursor.getColumnIndex("hminmlhour")));
			history.setHminmlminute(cursor.getInt(cursor.getColumnIndex("hminmlminute")));
			history.setHminmlsec(cursor.getInt(cursor.getColumnIndex("hminmlsec")));
			history.setHminmltimes(cursor.getInt(cursor.getColumnIndex("hminmltimes")));

			history.setHavgml(cursor.getString(cursor.getColumnIndex("havgml")));
			history.setHsleepscore(cursor.getInt(cursor.getColumnIndex("hsleepscore")));

			history.setHahiIndex(cursor.getString(cursor.getColumnIndex("hahiIndex")));
			history.setHhxztIndex(cursor.getString(cursor.getColumnIndex("hhxztIndex")));
			history.setHdtqIndex(cursor.getString(cursor.getColumnIndex("hdtqIndex")));
			history.setHyjzcsIndex(cursor.getString(cursor.getColumnIndex("hyjzcsIndex")));
			history.setHpjxybhdIndex(cursor.getString(cursor.getColumnIndex("hpjxybhdIndex")));
			history.setHzdxybhdIndex(cursor.getString(cursor.getColumnIndex("hzdxybhdIndex")));
			history.setHxybhdzbIndex(cursor.getString(cursor.getColumnIndex("hxybhdzbIndex")));

			history.setHmaxyjcxhour(cursor.getInt(cursor.getColumnIndex("hmaxyjcxhour")));
			history.setHmaxyjcxminute(cursor.getInt(cursor.getColumnIndex("hmaxyjcxminute")));
			history.setHmaxyjcxsec(cursor.getInt(cursor.getColumnIndex("hmaxyjcxsec")));
			history.setHmaxyjcxtimes(cursor.getInt(cursor.getColumnIndex("hmaxyjcxtimes")));
			history.setHmaxyjfshour(cursor.getInt(cursor.getColumnIndex("hmaxyjfshour")));
			history.setHmaxyjfsminute(cursor.getInt(cursor.getColumnIndex("hmaxyjfsminute")));
			history.setHmaxyjfssec(cursor.getInt(cursor.getColumnIndex("hmaxyjfssec")));
			history.setHmaxyjfstimes(cursor.getInt(cursor.getColumnIndex("hmaxyjfstimes")));
			history.setHlongyjcxhour(cursor.getInt(cursor.getColumnIndex("hlongyjcxhour")));
			history.setHlongyjcxminute(cursor.getInt(cursor.getColumnIndex("hlongyjcxminute")));
			history.setHlongyjcxsec(cursor.getInt(cursor.getColumnIndex("hlongyjcxsec")));
			history.setHlongyjcxtimes(cursor.getInt(cursor.getColumnIndex("hlongyjcxtimes")));
			history.setHlongyjfshour(cursor.getInt(cursor.getColumnIndex("hlongyjfshour")));
			history.setHlongyjfsminute(cursor.getInt(cursor.getColumnIndex("hlongyjfsminute")));
			history.setHlongyjfssec(cursor.getInt(cursor.getColumnIndex("hlongyjfssec")));
			history.setHlongyjfstimes(cursor.getInt(cursor.getColumnIndex("hlongyjfstimes")));

			history.setHxywhzsIndex(cursor.getString(cursor.getColumnIndex("hxywhzsIndex")));
			history.setHavgxlgzd(cursor.getString(cursor.getColumnIndex("havgxlgzd")));
			history.setHosahsdegree(cursor.getString(cursor.getColumnIndex("hosahsdegree")));
			history.setHdyxzdegree(cursor.getString(cursor.getColumnIndex("hdyxzdegree")));
			history.setHyjzsIndex(cursor.getString(cursor.getColumnIndex("hyjzsIndex")));

			history.setHzchxzthour(cursor.getInt(cursor.getColumnIndex("hzchxzthour")));
			history.setHzchxztminute(cursor.getInt(cursor.getColumnIndex("hzchxztminute")));
			history.setHzchxztsec(cursor.getInt(cursor.getColumnIndex("hzchxztsec")));
			history.setHzchxzttimes(cursor.getInt(cursor.getColumnIndex("hzchxzttimes")));
			history.setHhxhapperhour(cursor.getInt(cursor.getColumnIndex("hhxhapperhour")));
			history.setHhxhapperminute(cursor.getInt(cursor.getColumnIndex("hhxhapperminute")));
			history.setHhxhappersec(cursor.getInt(cursor.getColumnIndex("hhxhappersec")));
			history.setHhxhappertimes(cursor.getInt(cursor.getColumnIndex("hhxhappertimes")));
			history.setHhxztzsjhour(cursor.getInt(cursor.getColumnIndex("hhxztzsjhour")));
			history.setHhxztzsjminute(cursor.getInt(cursor.getColumnIndex("hhxztzsjminute")));
			history.setHhxztzsjsec(cursor.getInt(cursor.getColumnIndex("hhxztzsjsec")));
			history.setHhxztzsjtimes(cursor.getInt(cursor.getColumnIndex("hhxztzsjtimes")));
			history.setHzcdtqhour(cursor.getInt(cursor.getColumnIndex("hzcdtqhour")));
			history.setHzcdtqminute(cursor.getInt(cursor.getColumnIndex("hzcdtqminute")));
			history.setHzcdtqsec(cursor.getInt(cursor.getColumnIndex("hzcdtqsec")));
			history.setHzcdtqtimes(cursor.getInt(cursor.getColumnIndex("hzcdtqtimes")));
			history.setHdtqhapperhour(cursor.getInt(cursor.getColumnIndex("hdtqhapperhour")));
			history.setHdtqhapperminute(cursor.getInt(cursor.getColumnIndex("hdtqhapperminute")));
			history.setHdtqhappersec(cursor.getInt(cursor.getColumnIndex("hdtqhappersec")));
			history.setHdtqhappertimes(cursor.getInt(cursor.getColumnIndex("hdtqhappertimes")));
			history.setHdtqzsjhour(cursor.getInt(cursor.getColumnIndex("hdtqzsjhour")));
			history.setHdtqzsjminute(cursor.getInt(cursor.getColumnIndex("hdtqzsjminute")));
			history.setHdtqzsjsec(cursor.getInt(cursor.getColumnIndex("hdtqzsjsec")));
			history.setHdtqzsjtimes(cursor.getInt(cursor.getColumnIndex("hdtqzsjtimes")));

			history.setHxyodi(cursor.getString(cursor.getColumnIndex("hxyodi")));
			history.setHsmsober(cursor.getString(cursor.getColumnIndex("hsmsober")));
			history.setHsmmax(cursor.getString(cursor.getColumnIndex("hsmmax")));
			// a
			history.setHaxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("haxyfbzsjhour")));
			history.setHaxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("haxyfbzsjminute")));
			history.setHaxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("haxyfbzsjsec")));
			history.setHayjzcsnum(cursor.getString(cursor.getColumnIndex("hayjzcsnum")));
			history.setHaxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("haxyfbzsjtimes")));
			// b
			history.setHbxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjhour")));
			history.setHbxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjminute")));
			history.setHbxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjsec")));
			history.setHbyjzcsnum(cursor.getString(cursor.getColumnIndex("hbyjzcsnum")));
			history.setHbxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjtimes")));
			// c
			history.setHcxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjhour")));
			history.setHcxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjminute")));
			history.setHcxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjsec")));
			history.setHcyjzcsnum(cursor.getString(cursor.getColumnIndex("hcyjzcsnum")));
			history.setHcxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjtimes")));
			// d
			history.setHdxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjhour")));
			history.setHdxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjminute")));
			history.setHdxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjsec")));
			history.setHdyjzcsnum(cursor.getString(cursor.getColumnIndex("hdyjzcsnum")));
			history.setHdxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjtimes")));
			// e
			history.setHexyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hexyfbzsjhour")));
			history.setHexyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hexyfbzsjminute")));
			history.setHexyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hexyfbzsjsec")));
			history.setHeyjzcsnum(cursor.getString(cursor.getColumnIndex("heyjzcsnum")));
			history.setHexyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hexyfbzsjtimes")));
			// f
			history.setHfxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjhour")));
			history.setHfxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjminute")));
			history.setHfxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjsec")));
			history.setHfyjzcsnum(cursor.getString(cursor.getColumnIndex("hfyjzcsnum")));
			history.setHfxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjtimes")));

			history.setFilepaths(cursor.getString(cursor.getColumnIndex("filepaths")));
			history.setUploadfile(cursor.getInt(cursor.getColumnIndex("uploadfile")));
			history.setMacaddress(cursor.getString(cursor.getColumnIndex("macaddress")));
			history.setReportfilepath(cursor.getString(cursor.getColumnIndex("reportfilepath")));
			history.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			history.setHistoryupload(cursor.getInt(cursor.getColumnIndex("historyupload")));
			history.setIsFinalReport(cursor.getInt(cursor.getColumnIndex("isFinalReport")));

			history.setHudegree(cursor.getString(cursor.getColumnIndex("hudegree")));
			history.setHscoreHxzb(cursor.getInt(cursor.getColumnIndex("hscoreHxzb")));
			history.setTestdate(cursor.getInt(cursor.getColumnIndex("testdate")));
			history.setReportuuid(cursor.getString(cursor.getColumnIndex("reportuuid")));
			history.setIsComputed(cursor.getInt(cursor.getColumnIndex("iscomputed")));
			history.setCalculationflag(cursor.getInt(cursor.getColumnIndex("calculationflag")));
			all.add(history);
		}
		db.close();
		return all;

	}

	@Override
	public UserHistory findGivenHistory(int userid, int hid) {

		UserHistory history = null;
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from historys where userid=? and _id=?";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(userid), String.valueOf(hid) });
		if (cursor.getCount() > 0) {
			cursor.moveToNext();
			history = new UserHistory();
			history.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			history.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
			history.setHstarthour(cursor.getInt(cursor.getColumnIndex("hstarthour")));
			history.setHstartminute(cursor.getInt(cursor.getColumnIndex("hstartminute")));
			history.setHstarttimes(cursor.getInt(cursor.getColumnIndex("hstarttimes")));
			history.setHendhour(cursor.getInt(cursor.getColumnIndex("hendhour")));
			history.setHendminute(cursor.getInt(cursor.getColumnIndex("hendminute")));
			history.setHendtimes(cursor.getInt(cursor.getColumnIndex("hendtimes")));
			history.setHsmschour(cursor.getInt(cursor.getColumnIndex("hsmschour")));
			history.setHsmscminute(cursor.getInt(cursor.getColumnIndex("hsmscminute")));
			history.setHsmsctimes(cursor.getInt(cursor.getColumnIndex("hsmsctimes")));
			history.setHqshour(cursor.getInt(cursor.getColumnIndex("hqshour")));
			history.setHqsminute(cursor.getInt(cursor.getColumnIndex("hqsminute")));
			history.setHqssctimes(cursor.getInt(cursor.getColumnIndex("hqssctimes")));
			history.setHsshour(cursor.getInt(cursor.getColumnIndex("hsshour")));
			history.setHssminute(cursor.getInt(cursor.getColumnIndex("hssminute")));
			history.setHsssctimes(cursor.getInt(cursor.getColumnIndex("hsssctimes")));
			history.setHqxhour(cursor.getInt(cursor.getColumnIndex("hqxhour")));
			history.setHqxminute(cursor.getInt(cursor.getColumnIndex("hqxminute")));
			history.setHqxsctimes(cursor.getInt(cursor.getColumnIndex("hqxsctimes")));

			history.setHmaxml(cursor.getString(cursor.getColumnIndex("hmaxml")));
			history.setHmaxmlhour(cursor.getInt(cursor.getColumnIndex("hmaxmlhour")));
			history.setHmaxmlminute(cursor.getInt(cursor.getColumnIndex("hmaxmlminute")));
			history.setHmaxmlsec(cursor.getInt(cursor.getColumnIndex("hmaxmlsec")));
			history.setHmaxmltimes(cursor.getInt(cursor.getColumnIndex("hmaxmltimes")));

			history.setHminml(cursor.getString(cursor.getColumnIndex("hminml")));
			history.setHminmlhour(cursor.getInt(cursor.getColumnIndex("hminmlhour")));
			history.setHminmlminute(cursor.getInt(cursor.getColumnIndex("hminmlminute")));
			history.setHminmlsec(cursor.getInt(cursor.getColumnIndex("hminmlsec")));
			history.setHminmltimes(cursor.getInt(cursor.getColumnIndex("hminmltimes")));

			history.setHavgml(cursor.getString(cursor.getColumnIndex("havgml")));
			history.setHsleepscore(cursor.getInt(cursor.getColumnIndex("hsleepscore")));

			history.setHahiIndex(cursor.getString(cursor.getColumnIndex("hahiIndex")));
			history.setHhxztIndex(cursor.getString(cursor.getColumnIndex("hhxztIndex")));
			history.setHdtqIndex(cursor.getString(cursor.getColumnIndex("hdtqIndex")));
			history.setHyjzcsIndex(cursor.getString(cursor.getColumnIndex("hyjzcsIndex")));
			history.setHpjxybhdIndex(cursor.getString(cursor.getColumnIndex("hpjxybhdIndex")));
			history.setHzdxybhdIndex(cursor.getString(cursor.getColumnIndex("hzdxybhdIndex")));
			history.setHxybhdzbIndex(cursor.getString(cursor.getColumnIndex("hxybhdzbIndex")));

			history.setHmaxyjcxhour(cursor.getInt(cursor.getColumnIndex("hmaxyjcxhour")));
			history.setHmaxyjcxminute(cursor.getInt(cursor.getColumnIndex("hmaxyjcxminute")));
			history.setHmaxyjcxsec(cursor.getInt(cursor.getColumnIndex("hmaxyjcxsec")));
			history.setHmaxyjcxtimes(cursor.getInt(cursor.getColumnIndex("hmaxyjcxtimes")));
			history.setHmaxyjfshour(cursor.getInt(cursor.getColumnIndex("hmaxyjfshour")));
			history.setHmaxyjfsminute(cursor.getInt(cursor.getColumnIndex("hmaxyjfsminute")));
			history.setHmaxyjfssec(cursor.getInt(cursor.getColumnIndex("hmaxyjfssec")));
			history.setHmaxyjfstimes(cursor.getInt(cursor.getColumnIndex("hmaxyjfstimes")));
			history.setHlongyjcxhour(cursor.getInt(cursor.getColumnIndex("hlongyjcxhour")));
			history.setHlongyjcxminute(cursor.getInt(cursor.getColumnIndex("hlongyjcxminute")));
			history.setHlongyjcxsec(cursor.getInt(cursor.getColumnIndex("hlongyjcxsec")));
			history.setHlongyjcxtimes(cursor.getInt(cursor.getColumnIndex("hlongyjcxtimes")));
			history.setHlongyjfshour(cursor.getInt(cursor.getColumnIndex("hlongyjfshour")));
			history.setHlongyjfsminute(cursor.getInt(cursor.getColumnIndex("hlongyjfsminute")));
			history.setHlongyjfssec(cursor.getInt(cursor.getColumnIndex("hlongyjfssec")));
			history.setHlongyjfstimes(cursor.getInt(cursor.getColumnIndex("hlongyjfstimes")));

			history.setHxywhzsIndex(cursor.getString(cursor.getColumnIndex("hxywhzsIndex")));
			history.setHavgxlgzd(cursor.getString(cursor.getColumnIndex("havgxlgzd")));
			history.setHosahsdegree(cursor.getString(cursor.getColumnIndex("hosahsdegree")));
			history.setHdyxzdegree(cursor.getString(cursor.getColumnIndex("hdyxzdegree")));
			history.setHyjzsIndex(cursor.getString(cursor.getColumnIndex("hyjzsIndex")));

			history.setHzchxzthour(cursor.getInt(cursor.getColumnIndex("hzchxzthour")));
			history.setHzchxztminute(cursor.getInt(cursor.getColumnIndex("hzchxztminute")));
			history.setHzchxztsec(cursor.getInt(cursor.getColumnIndex("hzchxztsec")));
			history.setHzchxzttimes(cursor.getInt(cursor.getColumnIndex("hzchxzttimes")));
			history.setHhxhapperhour(cursor.getInt(cursor.getColumnIndex("hhxhapperhour")));
			history.setHhxhapperminute(cursor.getInt(cursor.getColumnIndex("hhxhapperminute")));
			history.setHhxhappersec(cursor.getInt(cursor.getColumnIndex("hhxhappersec")));
			history.setHhxhappertimes(cursor.getInt(cursor.getColumnIndex("hhxhappertimes")));
			history.setHhxztzsjhour(cursor.getInt(cursor.getColumnIndex("hhxztzsjhour")));
			history.setHhxztzsjminute(cursor.getInt(cursor.getColumnIndex("hhxztzsjminute")));
			history.setHhxztzsjsec(cursor.getInt(cursor.getColumnIndex("hhxztzsjsec")));
			history.setHhxztzsjtimes(cursor.getInt(cursor.getColumnIndex("hhxztzsjtimes")));
			history.setHzcdtqhour(cursor.getInt(cursor.getColumnIndex("hzcdtqhour")));
			history.setHzcdtqminute(cursor.getInt(cursor.getColumnIndex("hzcdtqminute")));
			history.setHzcdtqsec(cursor.getInt(cursor.getColumnIndex("hzcdtqsec")));
			history.setHzcdtqtimes(cursor.getInt(cursor.getColumnIndex("hzcdtqtimes")));
			history.setHdtqhapperhour(cursor.getInt(cursor.getColumnIndex("hdtqhapperhour")));
			history.setHdtqhapperminute(cursor.getInt(cursor.getColumnIndex("hdtqhapperminute")));
			history.setHdtqhappersec(cursor.getInt(cursor.getColumnIndex("hdtqhappersec")));
			history.setHdtqhappertimes(cursor.getInt(cursor.getColumnIndex("hdtqhappertimes")));
			history.setHdtqzsjhour(cursor.getInt(cursor.getColumnIndex("hdtqzsjhour")));
			history.setHdtqzsjminute(cursor.getInt(cursor.getColumnIndex("hdtqzsjminute")));
			history.setHdtqzsjsec(cursor.getInt(cursor.getColumnIndex("hdtqzsjsec")));
			history.setHdtqzsjtimes(cursor.getInt(cursor.getColumnIndex("hdtqzsjtimes")));

			history.setHxyodi(cursor.getString(cursor.getColumnIndex("hxyodi")));
			history.setHsmsober(cursor.getString(cursor.getColumnIndex("hsmsober")));
			history.setHsmmax(cursor.getString(cursor.getColumnIndex("hsmmax")));
			// a
			history.setHaxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("haxyfbzsjhour")));
			history.setHaxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("haxyfbzsjminute")));
			history.setHaxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("haxyfbzsjsec")));
			history.setHayjzcsnum(cursor.getString(cursor.getColumnIndex("hayjzcsnum")));
			history.setHaxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("haxyfbzsjtimes")));
			// b
			history.setHbxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjhour")));
			history.setHbxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjminute")));
			history.setHbxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjsec")));
			history.setHbyjzcsnum(cursor.getString(cursor.getColumnIndex("hbyjzcsnum")));
			history.setHbxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjtimes")));
			// c
			history.setHcxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjhour")));
			history.setHcxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjminute")));
			history.setHcxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjsec")));
			history.setHcyjzcsnum(cursor.getString(cursor.getColumnIndex("hcyjzcsnum")));
			history.setHcxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjtimes")));
			// d
			history.setHdxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjhour")));
			history.setHdxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjminute")));
			history.setHdxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjsec")));
			history.setHdyjzcsnum(cursor.getString(cursor.getColumnIndex("hdyjzcsnum")));
			history.setHdxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjtimes")));
			// e
			history.setHexyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hexyfbzsjhour")));
			history.setHexyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hexyfbzsjminute")));
			history.setHexyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hexyfbzsjsec")));
			history.setHeyjzcsnum(cursor.getString(cursor.getColumnIndex("heyjzcsnum")));
			history.setHexyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hexyfbzsjtimes")));
			// f
			history.setHfxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjhour")));
			history.setHfxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjminute")));
			history.setHfxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjsec")));
			history.setHfyjzcsnum(cursor.getString(cursor.getColumnIndex("hfyjzcsnum")));
			history.setHfxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjtimes")));

			history.setFilepaths(cursor.getString(cursor.getColumnIndex("filepaths")));
			history.setUploadfile(cursor.getInt(cursor.getColumnIndex("uploadfile")));
			history.setMacaddress(cursor.getString(cursor.getColumnIndex("macaddress")));
			history.setReportfilepath(cursor.getString(cursor.getColumnIndex("reportfilepath")));
			history.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
			history.setHistoryupload(cursor.getInt(cursor.getColumnIndex("historyupload")));
			history.setIsFinalReport(cursor.getInt(cursor.getColumnIndex("isFinalReport")));

			history.setHudegree(cursor.getString(cursor.getColumnIndex("hudegree")));
			history.setHscoreHxzb(cursor.getInt(cursor.getColumnIndex("hscoreHxzb")));
			history.setTestdate(cursor.getInt(cursor.getColumnIndex("testdate")));
			history.setReportuuid(cursor.getString(cursor.getColumnIndex("reportuuid")));
			history.setIsComputed(cursor.getInt(cursor.getColumnIndex("iscomputed")));
			history.setCalculationflag(cursor.getInt(cursor.getColumnIndex("calculationflag")));
		}
		db.close();
		return history;

	}

	@Override
	public boolean deleteSpecificHistory(int hid, int userid) {

		boolean flag = false;
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int row = db.delete("historys", "_id=? and userid=?",
				new String[] { String.valueOf(hid), String.valueOf(userid) });
		if (row > 0) {
			System.out.println("成功删除了" + row + "条数据");
			Log.i("TAG", "成功删除了" + row + "条数据");
			flag = true;
		}
		db.close();
		return flag;

	}

	@Override
	public List<UserHistory> findUnUploadUserHistory() {
		List<UserHistory> all = new ArrayList<UserHistory>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from historys where historyupload=0 or uploadfile=0";
		Cursor cursor = db.rawQuery(sql, null);
		Log.d(Utils.TAG, "findAllWebUploadUserHistory, cursor.getColumnCount() = " + cursor.getColumnCount());
		Log.d(Utils.TAG, "findAllWebUploadUserHistory, cursor.getCount() = " + cursor.getCount());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				UserHistory history = new UserHistory();
				history.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				history.setHstarthour(cursor.getInt(cursor.getColumnIndex("hstarthour")));
				history.setHstartminute(cursor.getInt(cursor.getColumnIndex("hstartminute")));
				history.setHstarttimes(cursor.getInt(cursor.getColumnIndex("hstarttimes")));
				history.setHendhour(cursor.getInt(cursor.getColumnIndex("hendhour")));
				history.setHendminute(cursor.getInt(cursor.getColumnIndex("hendminute")));
				history.setHendtimes(cursor.getInt(cursor.getColumnIndex("hendtimes")));
				history.setHsmschour(cursor.getInt(cursor.getColumnIndex("hsmschour")));
				history.setHsmscminute(cursor.getInt(cursor.getColumnIndex("hsmscminute")));
				history.setHsmsctimes(cursor.getInt(cursor.getColumnIndex("hsmsctimes")));
				history.setHqshour(cursor.getInt(cursor.getColumnIndex("hqshour")));
				history.setHqsminute(cursor.getInt(cursor.getColumnIndex("hqsminute")));
				history.setHqssctimes(cursor.getInt(cursor.getColumnIndex("hqssctimes")));
				history.setHsshour(cursor.getInt(cursor.getColumnIndex("hsshour")));
				history.setHssminute(cursor.getInt(cursor.getColumnIndex("hssminute")));
				history.setHsssctimes(cursor.getInt(cursor.getColumnIndex("hsssctimes")));
				history.setHqxhour(cursor.getInt(cursor.getColumnIndex("hqxhour")));
				history.setHqxminute(cursor.getInt(cursor.getColumnIndex("hqxminute")));
				history.setHqxsctimes(cursor.getInt(cursor.getColumnIndex("hqxsctimes")));

				history.setHmaxml(cursor.getString(cursor.getColumnIndex("hmaxml")));
				history.setHmaxmlhour(cursor.getInt(cursor.getColumnIndex("hmaxmlhour")));
				history.setHmaxmlminute(cursor.getInt(cursor.getColumnIndex("hmaxmlminute")));
				history.setHmaxmlsec(cursor.getInt(cursor.getColumnIndex("hmaxmlsec")));
				history.setHmaxmltimes(cursor.getInt(cursor.getColumnIndex("hmaxmltimes")));

				history.setHminml(cursor.getString(cursor.getColumnIndex("hminml")));
				history.setHminmlhour(cursor.getInt(cursor.getColumnIndex("hminmlhour")));
				history.setHminmlminute(cursor.getInt(cursor.getColumnIndex("hminmlminute")));
				history.setHminmlsec(cursor.getInt(cursor.getColumnIndex("hminmlsec")));
				history.setHminmltimes(cursor.getInt(cursor.getColumnIndex("hminmltimes")));

				history.setHavgml(cursor.getString(cursor.getColumnIndex("havgml")));
				history.setHsleepscore(cursor.getInt(cursor.getColumnIndex("hsleepscore")));

				history.setHahiIndex(cursor.getString(cursor.getColumnIndex("hahiIndex")));
				history.setHhxztIndex(cursor.getString(cursor.getColumnIndex("hhxztIndex")));
				history.setHdtqIndex(cursor.getString(cursor.getColumnIndex("hdtqIndex")));
				history.setHyjzcsIndex(cursor.getString(cursor.getColumnIndex("hyjzcsIndex")));
				history.setHpjxybhdIndex(cursor.getString(cursor.getColumnIndex("hpjxybhdIndex")));
				history.setHzdxybhdIndex(cursor.getString(cursor.getColumnIndex("hzdxybhdIndex")));
				history.setHxybhdzbIndex(cursor.getString(cursor.getColumnIndex("hxybhdzbIndex")));

				history.setHmaxyjcxhour(cursor.getInt(cursor.getColumnIndex("hmaxyjcxhour")));
				history.setHmaxyjcxminute(cursor.getInt(cursor.getColumnIndex("hmaxyjcxminute")));
				history.setHmaxyjcxsec(cursor.getInt(cursor.getColumnIndex("hmaxyjcxsec")));
				history.setHmaxyjcxtimes(cursor.getInt(cursor.getColumnIndex("hmaxyjcxtimes")));
				history.setHmaxyjfshour(cursor.getInt(cursor.getColumnIndex("hmaxyjfshour")));
				history.setHmaxyjfsminute(cursor.getInt(cursor.getColumnIndex("hmaxyjfsminute")));
				history.setHmaxyjfssec(cursor.getInt(cursor.getColumnIndex("hmaxyjfssec")));
				history.setHmaxyjfstimes(cursor.getInt(cursor.getColumnIndex("hmaxyjfstimes")));
				history.setHlongyjcxhour(cursor.getInt(cursor.getColumnIndex("hlongyjcxhour")));
				history.setHlongyjcxminute(cursor.getInt(cursor.getColumnIndex("hlongyjcxminute")));
				history.setHlongyjcxsec(cursor.getInt(cursor.getColumnIndex("hlongyjcxsec")));
				history.setHlongyjcxtimes(cursor.getInt(cursor.getColumnIndex("hlongyjcxtimes")));
				history.setHlongyjfshour(cursor.getInt(cursor.getColumnIndex("hlongyjfshour")));
				history.setHlongyjfsminute(cursor.getInt(cursor.getColumnIndex("hlongyjfsminute")));
				history.setHlongyjfssec(cursor.getInt(cursor.getColumnIndex("hlongyjfssec")));
				history.setHlongyjfstimes(cursor.getInt(cursor.getColumnIndex("hlongyjfstimes")));

				history.setHxywhzsIndex(cursor.getString(cursor.getColumnIndex("hxywhzsIndex")));
				history.setHavgxlgzd(cursor.getString(cursor.getColumnIndex("havgxlgzd")));
				history.setHosahsdegree(cursor.getString(cursor.getColumnIndex("hosahsdegree")));
				history.setHdyxzdegree(cursor.getString(cursor.getColumnIndex("hdyxzdegree")));
				history.setHyjzsIndex(cursor.getString(cursor.getColumnIndex("hyjzsIndex")));

				history.setHzchxzthour(cursor.getInt(cursor.getColumnIndex("hzchxzthour")));
				history.setHzchxztminute(cursor.getInt(cursor.getColumnIndex("hzchxztminute")));
				history.setHzchxztsec(cursor.getInt(cursor.getColumnIndex("hzchxztsec")));
				history.setHzchxzttimes(cursor.getInt(cursor.getColumnIndex("hzchxzttimes")));
				history.setHhxhapperhour(cursor.getInt(cursor.getColumnIndex("hhxhapperhour")));
				history.setHhxhapperminute(cursor.getInt(cursor.getColumnIndex("hhxhapperminute")));
				history.setHhxhappersec(cursor.getInt(cursor.getColumnIndex("hhxhappersec")));
				history.setHhxhappertimes(cursor.getInt(cursor.getColumnIndex("hhxhappertimes")));
				history.setHhxztzsjhour(cursor.getInt(cursor.getColumnIndex("hhxztzsjhour")));
				history.setHhxztzsjminute(cursor.getInt(cursor.getColumnIndex("hhxztzsjminute")));
				history.setHhxztzsjsec(cursor.getInt(cursor.getColumnIndex("hhxztzsjsec")));
				history.setHhxztzsjtimes(cursor.getInt(cursor.getColumnIndex("hhxztzsjtimes")));
				history.setHzcdtqhour(cursor.getInt(cursor.getColumnIndex("hzcdtqhour")));
				history.setHzcdtqminute(cursor.getInt(cursor.getColumnIndex("hzcdtqminute")));
				history.setHzcdtqsec(cursor.getInt(cursor.getColumnIndex("hzcdtqsec")));
				history.setHzcdtqtimes(cursor.getInt(cursor.getColumnIndex("hzcdtqtimes")));
				history.setHdtqhapperhour(cursor.getInt(cursor.getColumnIndex("hdtqhapperhour")));
				history.setHdtqhapperminute(cursor.getInt(cursor.getColumnIndex("hdtqhapperminute")));
				history.setHdtqhappersec(cursor.getInt(cursor.getColumnIndex("hdtqhappersec")));
				history.setHdtqhappertimes(cursor.getInt(cursor.getColumnIndex("hdtqhappertimes")));
				history.setHdtqzsjhour(cursor.getInt(cursor.getColumnIndex("hdtqzsjhour")));
				history.setHdtqzsjminute(cursor.getInt(cursor.getColumnIndex("hdtqzsjminute")));
				history.setHdtqzsjsec(cursor.getInt(cursor.getColumnIndex("hdtqzsjsec")));
				history.setHdtqzsjtimes(cursor.getInt(cursor.getColumnIndex("hdtqzsjtimes")));

				history.setHxyodi(cursor.getString(cursor.getColumnIndex("hxyodi")));
				history.setHsmsober(cursor.getString(cursor.getColumnIndex("hsmsober")));
				history.setHsmmax(cursor.getString(cursor.getColumnIndex("hsmmax")));
				// a
				history.setHaxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("haxyfbzsjhour")));
				history.setHaxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("haxyfbzsjminute")));
				history.setHaxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("haxyfbzsjsec")));
				history.setHayjzcsnum(cursor.getString(cursor.getColumnIndex("hayjzcsnum")));
				history.setHaxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("haxyfbzsjtimes")));
				// b
				history.setHbxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjhour")));
				history.setHbxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjminute")));
				history.setHbxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjsec")));
				history.setHbyjzcsnum(cursor.getString(cursor.getColumnIndex("hbyjzcsnum")));
				history.setHbxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjtimes")));
				// c
				history.setHcxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjhour")));
				history.setHcxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjminute")));
				history.setHcxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjsec")));
				history.setHcyjzcsnum(cursor.getString(cursor.getColumnIndex("hcyjzcsnum")));
				history.setHcxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjtimes")));
				// d
				history.setHdxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjhour")));
				history.setHdxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjminute")));
				history.setHdxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjsec")));
				history.setHdyjzcsnum(cursor.getString(cursor.getColumnIndex("hdyjzcsnum")));
				history.setHdxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjtimes")));
				// e
				history.setHexyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hexyfbzsjhour")));
				history.setHexyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hexyfbzsjminute")));
				history.setHexyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hexyfbzsjsec")));
				history.setHeyjzcsnum(cursor.getString(cursor.getColumnIndex("heyjzcsnum")));
				history.setHexyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hexyfbzsjtimes")));
				// f
				history.setHfxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjhour")));
				history.setHfxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjminute")));
				history.setHfxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjsec")));
				history.setHfyjzcsnum(cursor.getString(cursor.getColumnIndex("hfyjzcsnum")));
				history.setHfxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjtimes")));

				history.setFilepaths(cursor.getString(cursor.getColumnIndex("filepaths")));
				history.setUploadfile(cursor.getInt(cursor.getColumnIndex("uploadfile")));
				history.setMacaddress(cursor.getString(cursor.getColumnIndex("macaddress")));
				history.setReportfilepath(cursor.getString(cursor.getColumnIndex("reportfilepath")));
				history.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
				history.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
				history.setHistoryupload(cursor.getInt(cursor.getColumnIndex("historyupload")));
				history.setIsFinalReport(cursor.getInt(cursor.getColumnIndex("isFinalReport")));

				history.setHudegree(cursor.getString(cursor.getColumnIndex("hudegree")));
				history.setHscoreHxzb(cursor.getInt(cursor.getColumnIndex("hscoreHxzb")));
				history.setTestdate(cursor.getInt(cursor.getColumnIndex("testdate")));
				history.setReportuuid(cursor.getString(cursor.getColumnIndex("reportuuid")));
				history.setIsComputed(cursor.getInt(cursor.getColumnIndex("iscomputed")));
				history.setCalculationflag(cursor.getInt(cursor.getColumnIndex("calculationflag")));
				all.add(history);
			}
		}
		db.close();
		return all;
	}

	@Override
	public List<UserHistory> findUnUploadUserHistoryByUserId(int userid) {
		List<UserHistory> all = new ArrayList<UserHistory>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sql = "select * from historys where (historyupload=0 or uploadfile=0) and userid=?";
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(userid) });
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				UserHistory history = new UserHistory();
				history.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				history.setHstarthour(cursor.getInt(cursor.getColumnIndex("hstarthour")));
				history.setHstartminute(cursor.getInt(cursor.getColumnIndex("hstartminute")));
				history.setHstarttimes(cursor.getInt(cursor.getColumnIndex("hstarttimes")));
				history.setHendhour(cursor.getInt(cursor.getColumnIndex("hendhour")));
				history.setHendminute(cursor.getInt(cursor.getColumnIndex("hendminute")));
				history.setHendtimes(cursor.getInt(cursor.getColumnIndex("hendtimes")));
				history.setHsmschour(cursor.getInt(cursor.getColumnIndex("hsmschour")));
				history.setHsmscminute(cursor.getInt(cursor.getColumnIndex("hsmscminute")));
				history.setHsmsctimes(cursor.getInt(cursor.getColumnIndex("hsmsctimes")));
				history.setHqshour(cursor.getInt(cursor.getColumnIndex("hqshour")));
				history.setHqsminute(cursor.getInt(cursor.getColumnIndex("hqsminute")));
				history.setHqssctimes(cursor.getInt(cursor.getColumnIndex("hqssctimes")));
				history.setHsshour(cursor.getInt(cursor.getColumnIndex("hsshour")));
				history.setHssminute(cursor.getInt(cursor.getColumnIndex("hssminute")));
				history.setHsssctimes(cursor.getInt(cursor.getColumnIndex("hsssctimes")));
				history.setHqxhour(cursor.getInt(cursor.getColumnIndex("hqxhour")));
				history.setHqxminute(cursor.getInt(cursor.getColumnIndex("hqxminute")));
				history.setHqxsctimes(cursor.getInt(cursor.getColumnIndex("hqxsctimes")));

				history.setHmaxml(cursor.getString(cursor.getColumnIndex("hmaxml")));
				history.setHmaxmlhour(cursor.getInt(cursor.getColumnIndex("hmaxmlhour")));
				history.setHmaxmlminute(cursor.getInt(cursor.getColumnIndex("hmaxmlminute")));
				history.setHmaxmlsec(cursor.getInt(cursor.getColumnIndex("hmaxmlsec")));
				history.setHmaxmltimes(cursor.getInt(cursor.getColumnIndex("hmaxmltimes")));

				history.setHminml(cursor.getString(cursor.getColumnIndex("hminml")));
				history.setHminmlhour(cursor.getInt(cursor.getColumnIndex("hminmlhour")));
				history.setHminmlminute(cursor.getInt(cursor.getColumnIndex("hminmlminute")));
				history.setHminmlsec(cursor.getInt(cursor.getColumnIndex("hminmlsec")));
				history.setHminmltimes(cursor.getInt(cursor.getColumnIndex("hminmltimes")));

				history.setHavgml(cursor.getString(cursor.getColumnIndex("havgml")));
				history.setHsleepscore(cursor.getInt(cursor.getColumnIndex("hsleepscore")));

				history.setHahiIndex(cursor.getString(cursor.getColumnIndex("hahiIndex")));
				history.setHhxztIndex(cursor.getString(cursor.getColumnIndex("hhxztIndex")));
				history.setHdtqIndex(cursor.getString(cursor.getColumnIndex("hdtqIndex")));
				history.setHyjzcsIndex(cursor.getString(cursor.getColumnIndex("hyjzcsIndex")));
				history.setHpjxybhdIndex(cursor.getString(cursor.getColumnIndex("hpjxybhdIndex")));
				history.setHzdxybhdIndex(cursor.getString(cursor.getColumnIndex("hzdxybhdIndex")));
				history.setHxybhdzbIndex(cursor.getString(cursor.getColumnIndex("hxybhdzbIndex")));

				history.setHmaxyjcxhour(cursor.getInt(cursor.getColumnIndex("hmaxyjcxhour")));
				history.setHmaxyjcxminute(cursor.getInt(cursor.getColumnIndex("hmaxyjcxminute")));
				history.setHmaxyjcxsec(cursor.getInt(cursor.getColumnIndex("hmaxyjcxsec")));
				history.setHmaxyjcxtimes(cursor.getInt(cursor.getColumnIndex("hmaxyjcxtimes")));
				history.setHmaxyjfshour(cursor.getInt(cursor.getColumnIndex("hmaxyjfshour")));
				history.setHmaxyjfsminute(cursor.getInt(cursor.getColumnIndex("hmaxyjfsminute")));
				history.setHmaxyjfssec(cursor.getInt(cursor.getColumnIndex("hmaxyjfssec")));
				history.setHmaxyjfstimes(cursor.getInt(cursor.getColumnIndex("hmaxyjfstimes")));
				history.setHlongyjcxhour(cursor.getInt(cursor.getColumnIndex("hlongyjcxhour")));
				history.setHlongyjcxminute(cursor.getInt(cursor.getColumnIndex("hlongyjcxminute")));
				history.setHlongyjcxsec(cursor.getInt(cursor.getColumnIndex("hlongyjcxsec")));
				history.setHlongyjcxtimes(cursor.getInt(cursor.getColumnIndex("hlongyjcxtimes")));
				history.setHlongyjfshour(cursor.getInt(cursor.getColumnIndex("hlongyjfshour")));
				history.setHlongyjfsminute(cursor.getInt(cursor.getColumnIndex("hlongyjfsminute")));
				history.setHlongyjfssec(cursor.getInt(cursor.getColumnIndex("hlongyjfssec")));
				history.setHlongyjfstimes(cursor.getInt(cursor.getColumnIndex("hlongyjfstimes")));

				history.setHxywhzsIndex(cursor.getString(cursor.getColumnIndex("hxywhzsIndex")));
				history.setHavgxlgzd(cursor.getString(cursor.getColumnIndex("havgxlgzd")));
				history.setHosahsdegree(cursor.getString(cursor.getColumnIndex("hosahsdegree")));
				history.setHdyxzdegree(cursor.getString(cursor.getColumnIndex("hdyxzdegree")));
				history.setHyjzsIndex(cursor.getString(cursor.getColumnIndex("hyjzsIndex")));

				history.setHzchxzthour(cursor.getInt(cursor.getColumnIndex("hzchxzthour")));
				history.setHzchxztminute(cursor.getInt(cursor.getColumnIndex("hzchxztminute")));
				history.setHzchxztsec(cursor.getInt(cursor.getColumnIndex("hzchxztsec")));
				history.setHzchxzttimes(cursor.getInt(cursor.getColumnIndex("hzchxzttimes")));
				history.setHhxhapperhour(cursor.getInt(cursor.getColumnIndex("hhxhapperhour")));
				history.setHhxhapperminute(cursor.getInt(cursor.getColumnIndex("hhxhapperminute")));
				history.setHhxhappersec(cursor.getInt(cursor.getColumnIndex("hhxhappersec")));
				history.setHhxhappertimes(cursor.getInt(cursor.getColumnIndex("hhxhappertimes")));
				history.setHhxztzsjhour(cursor.getInt(cursor.getColumnIndex("hhxztzsjhour")));
				history.setHhxztzsjminute(cursor.getInt(cursor.getColumnIndex("hhxztzsjminute")));
				history.setHhxztzsjsec(cursor.getInt(cursor.getColumnIndex("hhxztzsjsec")));
				history.setHhxztzsjtimes(cursor.getInt(cursor.getColumnIndex("hhxztzsjtimes")));
				history.setHzcdtqhour(cursor.getInt(cursor.getColumnIndex("hzcdtqhour")));
				history.setHzcdtqminute(cursor.getInt(cursor.getColumnIndex("hzcdtqminute")));
				history.setHzcdtqsec(cursor.getInt(cursor.getColumnIndex("hzcdtqsec")));
				history.setHzcdtqtimes(cursor.getInt(cursor.getColumnIndex("hzcdtqtimes")));
				history.setHdtqhapperhour(cursor.getInt(cursor.getColumnIndex("hdtqhapperhour")));
				history.setHdtqhapperminute(cursor.getInt(cursor.getColumnIndex("hdtqhapperminute")));
				history.setHdtqhappersec(cursor.getInt(cursor.getColumnIndex("hdtqhappersec")));
				history.setHdtqhappertimes(cursor.getInt(cursor.getColumnIndex("hdtqhappertimes")));
				history.setHdtqzsjhour(cursor.getInt(cursor.getColumnIndex("hdtqzsjhour")));
				history.setHdtqzsjminute(cursor.getInt(cursor.getColumnIndex("hdtqzsjminute")));
				history.setHdtqzsjsec(cursor.getInt(cursor.getColumnIndex("hdtqzsjsec")));
				history.setHdtqzsjtimes(cursor.getInt(cursor.getColumnIndex("hdtqzsjtimes")));

				history.setHxyodi(cursor.getString(cursor.getColumnIndex("hxyodi")));
				history.setHsmsober(cursor.getString(cursor.getColumnIndex("hsmsober")));
				history.setHsmmax(cursor.getString(cursor.getColumnIndex("hsmmax")));
				// a
				history.setHaxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("haxyfbzsjhour")));
				history.setHaxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("haxyfbzsjminute")));
				history.setHaxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("haxyfbzsjsec")));
				history.setHayjzcsnum(cursor.getString(cursor.getColumnIndex("hayjzcsnum")));
				history.setHaxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("haxyfbzsjtimes")));
				// b
				history.setHbxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjhour")));
				history.setHbxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjminute")));
				history.setHbxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjsec")));
				history.setHbyjzcsnum(cursor.getString(cursor.getColumnIndex("hbyjzcsnum")));
				history.setHbxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hbxyfbzsjtimes")));
				// c
				history.setHcxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjhour")));
				history.setHcxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjminute")));
				history.setHcxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjsec")));
				history.setHcyjzcsnum(cursor.getString(cursor.getColumnIndex("hcyjzcsnum")));
				history.setHcxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hcxyfbzsjtimes")));
				// d
				history.setHdxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjhour")));
				history.setHdxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjminute")));
				history.setHdxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjsec")));
				history.setHdyjzcsnum(cursor.getString(cursor.getColumnIndex("hdyjzcsnum")));
				history.setHdxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hdxyfbzsjtimes")));
				// e
				history.setHexyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hexyfbzsjhour")));
				history.setHexyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hexyfbzsjminute")));
				history.setHexyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hexyfbzsjsec")));
				history.setHeyjzcsnum(cursor.getString(cursor.getColumnIndex("heyjzcsnum")));
				history.setHexyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hexyfbzsjtimes")));
				// f
				history.setHfxyfbzsjhour(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjhour")));
				history.setHfxyfbzsjminute(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjminute")));
				history.setHfxyfbzsjsec(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjsec")));
				history.setHfyjzcsnum(cursor.getString(cursor.getColumnIndex("hfyjzcsnum")));
				history.setHfxyfbzsjtimes(cursor.getInt(cursor.getColumnIndex("hfxyfbzsjtimes")));

				history.setFilepaths(cursor.getString(cursor.getColumnIndex("filepaths")));
				history.setUploadfile(cursor.getInt(cursor.getColumnIndex("uploadfile")));
				history.setMacaddress(cursor.getString(cursor.getColumnIndex("macaddress")));
				history.setReportfilepath(cursor.getString(cursor.getColumnIndex("reportfilepath")));
				history.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
				history.setUuid(cursor.getString(cursor.getColumnIndex("useruuid")));
				history.setHistoryupload(cursor.getInt(cursor.getColumnIndex("historyupload")));
				history.setIsFinalReport(cursor.getInt(cursor.getColumnIndex("isFinalReport")));

				history.setHudegree(cursor.getString(cursor.getColumnIndex("hudegree")));
				history.setHscoreHxzb(cursor.getInt(cursor.getColumnIndex("hscoreHxzb")));
				history.setTestdate(cursor.getInt(cursor.getColumnIndex("testdate")));
				history.setReportuuid(cursor.getString(cursor.getColumnIndex("reportuuid")));
				history.setIsComputed(cursor.getInt(cursor.getColumnIndex("iscomputed")));
				history.setCalculationflag(cursor.getInt(cursor.getColumnIndex("calculationflag")));
				all.add(history);
			}
		}
		db.close();
		return all;
	}

	@Override
	public void truncateUserLogin() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 删除SQL语句
		String sql = "delete from userlogin";
		// 执行SQL语句
		db.execSQL(sql);
		db.close();
	}

	@Override
	public void truncateUserManager() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 删除SQL语句
		String sql = "delete from usermanager";
		// 执行SQL语句
		db.execSQL(sql);
		db.close();
	}

	@Override
	public void truncateDiabetesHy() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 删除SQL语句
		String sql = "delete from diabeteshy";
		// 执行SQL语句
		db.execSQL(sql);
		db.close();
	}

	@Override
	public void truncateEpworth() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 删除SQL语句
		String sql = "delete from epworth";
		// 执行SQL语句
		db.execSQL(sql);
		db.close();
	}

	@Override
	public void truncateHospitals() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 删除SQL语句
		String sql = "delete from hospitals";
		// 执行SQL语句
		db.execSQL(sql);
		db.close();
	}

	@Override
	public void truncateUserHistory() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// 删除SQL语句
		String sql = "delete from historys";
		// 执行SQL语句
		db.execSQL(sql);
		db.close();
	}

}
