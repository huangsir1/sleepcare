package com.sleep.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper {
	/**
	 * 构造方法四个参数的含义 第一个参数:context上下文 第二个参数:数据库的名称 第三个参数：游标工厂 第四个参数：数据库版本
	 */
	private static final String BDNAME = "sleep.db";
	private static final int DBVERSION = 2;

	public DbOpenHelper(Context context) {
		super(context, BDNAME, null, DBVERSION);

	}

	/**
	 * 创建数据库表时调用
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		//账号密码
		db.execSQL("create table userlogin("
				+ "_id integer primary key autoincrement,"
				+ "usertel varchar(20)," 
				+ "emails varchar(20)," 
				+ "password varchar(20),"
				+ "sleepcareversion varchar(20),"
				+ "token varchar(64))");

		//用户信息
		db.execSQL("create table usermanager("
				+ "_id integer primary key autoincrement,"
				+ "username varchar(20)," 
				+ "userage varchar(32),"
				//责任医师
				+ "responsiblephysician varchar(32),"
				//手机号码
				+ "mobilenumber varchar(20),"
				+ "useremail varchar(20),"
				+ "uhospitalname varchar(32),"
				+ "usersex varchar(3),"
				+ "userweight integer,"
				+ "userheight integer," 
				+ "userbmi integer," 
				+ "useruuid varchar(36)," 				
				+ "userupload integer default 0)");

		// 嗜睡程度表
		db.execSQL("create table epworth("
				+ "_id integer primary key autoincrement," +
				// 阅读
				"satreading integer," +
				// 看电视
				"watchtv integer," +
				// 不动
				"satnotmove integer," +
				// 长时间不休息
				"longnotrest integer," +
				// 坐着与人谈话
				"satconversation integer," +
				// 饭后休息
				"afterdinnerrest integer," +
				// 开车等红绿灯
				"trafficlights integer," +
				// 静卧休息
				"jingworest integer," +
				// 总分
				"sumscore integer," +
				"useruuid varchar(36),"+
				"userupload integer default 0)");

		//病史表
		db.execSQL("create table diabeteshy("
				+ "_id integer primary key autoincrement," +
				/*
				 * 病史
				 */
				// 失眠
				"losesleep varchar(20)," +
				// 糖尿病
				"diabetes varchar(20)," +
				// 高血压
				"hypertension varchar(20)," +
				// 冠心病
				"coronaryheartdisease varchar(20)," +
				// 心力衰竭
				"heartfailure varchar(20)," +
				// 心律失常
				"arrhythmia varchar(20)," +
				// 鼻腔阻塞
				"congestion varchar(20)," +
				// 长期吸烟
				"longsmoking varchar(20)," +
				// 脑血管疾病
				"cerebrovasculardisease varchar(20)," +
				// 肾功能损害
				"renalfailure varchar(20)," +
				// 服用镇静剂
				"takesedatives varchar(20)," +
				// 长期大量饮酒
				"longdrinking varchar(20)," +
				// 是否OSAHS的家族史
				"whetherfmhy varchar(20)," +
				// 是否悬雍垂粗大
				"whetherxyccd varchar(20)," +
				/*
				 * 女性可选
				 */
				// 是否绝经
				"whetherjjm varchar(20)," +
				
				"useruuid varchar(36),"+
				"userupload integer default 0)");
		
		//pad设置
		db.execSQL("create table hospitals("
				+ "_id integer primary key autoincrement,"
				+ "hospitalname varchar(32)," 
				+ "departmentname varchar(32),"
				+ "doctorname varchar(32),"
				+ "useruuid varchar(36),"
				+ "userid integer," 
				+ "printerinformation varchar(32))");
		
		/**
		 * history
		 */
		db.execSQL("create table historys("
				+ "_id integer primary key autoincrement,"
				//用户ID
				+ "userid integer,"
				//用户uuid
				+ "useruuid varchar(36),"
				//mac地址
				+ "macaddress varchar(32),"
				//报告的UUID
				+"reportuuid varchar(36),"
				//判定上传
				+"historyupload integer default 0,"
				//文件路径
				+ "filepaths varchar(64),"
				//文件上传
				+ "uploadfile integer default 0," 
				//pdf
				+ "reportfilepath varchar(64),"
				//是否为最终报告
				+ "isFinalReport integer default 0," 
				//是否计算出结果
				+ "iscomputed integer default 0," 
				//是否计算
				+ "calculationflag integer default 0," 

				
				// 开始睡眠时间
				+ "hstarthour integer," 
				+ "hstartminute integer,"
				
				+ "hstarttimes integer,"
				
				// 结束睡眠时间
				+ "hendhour integer," 
				+ "hendminute integer,"
				
				+ "hendtimes integer,"
				
				// 睡眠时长
				+ "hsmschour integer,"
				+ "hsmscminute integer," 
				
				+ "hsmsctimes integer,"
				
				// 浅睡时长
				+ "hqshour integer,"
				+ "hqsminute integer,"
				
				+ "hqssctimes integer,"
				
				// 深睡时长
				+ "hsshour integer," 
				+ "hssminute integer,"
				
				+ "hsssctimes integer,"
				
				// 清醒时长
				+ "hqxhour integer,"
				+ "hqxminute integer," 
				
				+ "hqxsctimes integer,"
				
				/**脉率***************/
				//最高脉率 	次
				+ "hmaxml varchar(20),"
				//发生于    	时
				+ "hmaxmlhour integer,"
				//发生于 	 分
				+ "hmaxmlminute integer,"
				//发生于  	秒
				+ "hmaxmlsec integer,"
				
				+ "hmaxmltimes integer,"
				
				//最低脉率 	次
				+ "hminml varchar(20),"
				//发生于    	时
				+ "hminmlhour integer,"
				//发生于 	 分
				+ "hminmlminute integer,"
				//发生于  	秒
				+ "hminmlsec integer,"
				+ "hminmltimes integer,"
				//平均脉率
				+ "havgml varchar(20),"
				//  睡眠打分
				+ "hsleepscore integer,"
				+ "hahiIndex varchar(20)," 
				+ "hhxztIndex varchar(20),"
				+ "hdtqIndex varchar(20),"
				+ "hyjzcsIndex varchar(20),"
				+ "hpjxybhdIndex varchar(20),"
				+ "hzdxybhdIndex varchar(20)," 
				+ "hxybhdzbIndex varchar(20),"
				//最大氧降  	次
				//持续    		时
				+ "hmaxyjcxhour integer,"
				//持续  		 分
				+ "hmaxyjcxminute integer,"
				//持续   		秒
				+ "hmaxyjcxsec integer,"
				
				+ "hmaxyjcxtimes integer,"				
				
				//发生于    	时
				+ "hmaxyjfshour integer,"
				//发生于 	 分
				+ "hmaxyjfsminute integer,"
				//发生于  	秒
				+ "hmaxyjfssec integer,"
				
				+ "hmaxyjfstimes integer,"	
				
				//最长氧降时长	  次
				//持续  		时
				+ "hlongyjcxhour integer,"
				//持续		 分
				+ "hlongyjcxminute integer,"
				//持续	 	秒
				+ "hlongyjcxsec integer,"
				
				+ "hlongyjcxtimes integer,"	
				
				//发生于  	时
				+ "hlongyjfshour integer,"
				//发生于 	 分
				+ "hlongyjfsminute integer,"
				//发生于 	 秒
				+ "hlongyjfssec integer,"
				
				+ "hlongyjfstimes integer,"	
				
				//氧减危害指数
				+ "hxywhzsIndex varchar(20),"
				//血流灌注度
				+ "havgxlgzd varchar(20),"
				//OSAHS
				+ "hosahsdegree varchar(20),"
				//低氧血症
				+ "hdyxzdegree varchar(20),"
				//氧减指数
				+ "hyjzsIndex varchar(20),"
				
				/**
				 * 新增呼吸内容
				 */
				// 最长呼吸暂停时
				+ "hzchxzthour integer," +
				// 最长呼吸暂停分
				"hzchxztminute integer," +
				// 最长呼吸暂停秒
				"hzchxztsec integer," +
				
				"hzchxzttimes integer,"+ 
				
				// 发生于时
				"hhxhapperhour integer," +
				// 发生于分
				"hhxhapperminute integer," +
				// 发生于秒
				"hhxhappersec integer," +
				
				"hhxhappertimes integer,"+ 
				
				// "hxSpO2 integer," +
				// 呼吸暂停总时间时
				"hhxztzsjhour integer," +
				// 呼吸暂停总时间分
				"hhxztzsjminute integer," +
				// 呼吸暂停总时间秒
				"hhxztzsjsec integer," +
				
				"hhxztzsjtimes integer,"+ 
				
				/**
				 * 新增低通气内容
				 */
				// 最长低通气时
				"hzcdtqhour integer," +
				// 最长低通气分
				"hzcdtqminute integer," +
				// 最长低通气秒
				"hzcdtqsec integer," +
				
				"hzcdtqtimes integer,"+ 
				
				// 发生于时
				"hdtqhapperhour integer," +
				// 发生于分
				"hdtqhapperminute integer," +
				// 发生于秒
				"hdtqhappersec integer," +
				
				"hdtqhappertimes integer,"+ 
				
				// "dtqSpO2 integer," +
				// 低通气总时间时
				"hdtqzsjhour integer," +
				// 低通气总时间分
				"hdtqzsjminute integer," +
				// 低通气总时间秒
				"hdtqzsjsec integer," +

				"hdtqzsjtimes integer,"+ 
				
				/**
				 * 新增ODI内容
				 */
				// odi
				"hxyodi varchar(20)," +
				// 醒时
				"hsmsober varchar(20)," +
				// 最高
				"hsmmax varchar(20)," +

				/**
				 * 血氧分布总时间
				 */
				/*
				 * a-代表(90%-100%)
				 */
				// a总时间时
				"haxyfbzsjhour integer," +
				// a总时间分
				"haxyfbzsjminute integer," +
				// a总时间秒
				"haxyfbzsjsec integer," +
				// a-氧减总次数
				"hayjzcsnum varchar(20)," +
				
				"haxyfbzsjtimes integer," +
				
				/*
				 * b-代表(80%-89%)
				 */
				// b总时间时
				"hbxyfbzsjhour integer," +
				// b总时间分
				"hbxyfbzsjminute integer," +
				// b总时间秒
				"hbxyfbzsjsec integer," +
				// b-氧减总次数
				"hbyjzcsnum varchar(20)," +
				
				"hbxyfbzsjtimes integer," +
				
				/*
				 * c-代表(70%-79%)
				 */
				// c总时间时
				"hcxyfbzsjhour integer," +
				// c总时间分
				"hcxyfbzsjminute integer," +
				// c总时间秒
				"hcxyfbzsjsec integer," +
				// c-氧减总次数
				"hcyjzcsnum varchar(20)," +
				
				"hcxyfbzsjtimes integer," +

				/*
				 * d-代表(60%-69%)
				 */
				// d总时间时
				"hdxyfbzsjhour integer," +
				// d总时间分
				"hdxyfbzsjminute integer," +
				// d总时间秒
				"hdxyfbzsjsec integer," +
				// d-氧减总次数
				"hdyjzcsnum varchar(20)," +

				"hdxyfbzsjtimes integer," +

				/*
				 * e-代表(50%-59%)
				 */
				// e总时间时
				"hexyfbzsjhour integer," +
				// e总时间分
				"hexyfbzsjminute integer," +
				// e总时间秒
				"hexyfbzsjsec integer," +
				// e-氧减总次数
				"heyjzcsnum varchar(20)," +

				"hexyfbzsjtimes integer," +

				/*
				 * f-代表(<50%)
				 */
				// f总时间时
				"hfxyfbzsjhour integer," +
				// f总时间分
				"hfxyfbzsjminute integer," +
				// f总时间秒
				"hfxyfbzsjsec integer," +
				// f-氧减总次数
				"hfyjzcsnum varchar(20)," +

				"hfxyfbzsjtimes integer," +

				// osahs病情程度
				"hudegree varchar(20)," +
				// 评分
				"hscoreHxzb integer," + "testdate integer)");

	}

	/**
	 * 修改数据库表时调用
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("TAG", "onUpgrade-执行了");
		switch (oldVersion) {
		case 1:
			db.execSQL("alter table userlogin add token varchar(64)");
			db.execSQL("alter table userlogin add sleepcareversion varchar(20)");
			// 最大氧减发生时的血氧下降幅度
			db.execSQL("alter table historys add odi4largestrange integer");
			// 删除表
			db.execSQL("drop table userindex");
			db.execSQL("drop table usersleep");
			break;
		default:
			break;
		}
	}

}
