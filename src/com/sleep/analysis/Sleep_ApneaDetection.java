package com.sleep.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.sleep.analysis.Param_MODII;
import com.sleep.utils.Utils;

import android.R.integer;
import android.util.Log;
import android.widget.Toast;

/**
 * 通过分析睡眠过程中血氧饱和度值的变化，来检测呼吸暂停
 */
/**
 * @author hhh
 *
 */
/**
 * @author hhh
 *
 */
/**
 * @author hhh
 *
 */
/**
 * @author hhh
 *
 */
/**
 * @author hhh
 *
 */
/**
 * @author hhh
 *
 */
/**
 * @author hhh
 *
 */
/**
 * @author hhh
 *
 */
/**
 * @author hhh
 *
 */
/**
 * @author hhh
 *
 */
public class Sleep_ApneaDetection {

	// 计算所需的临时变量
	private DateFormat in = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
	private boolean flag_five = false; // 判断是否需要舍去前五分钟的数据，true为舍去，false为不舍去
	private boolean flag_time_dataType = false; // true:用long类型的数据计算时间，false:用String类型的数据计算时间
	private int[] temp_distribution_90_94 = new int[3]; // 血氧值在90~95之间的时间
	private int[] temp_distribution_85_90 = new int[3]; // 血氧值在85~90之间的时间
	private int[] temp_distribution_80_85 = new int[3]; // 血氧值在80~85之间的时间
	private int[] temp_distribution_75_80 = new int[3]; // 血氧值在75~80之间的时间
	private int[] temp_distribution_70_75 = new int[3]; // 血氧值在70~75之间的时间
	private int[] temp_distribution_65_70 = new int[3]; // 血氧值在65~70之间的时间
	private int[] temp_distribution_0_65 = new int[3]; // 血氧值在0~65之间的时间
	// private int[] temp_distribution_60_65 = new int[3];
	// //血氧值在60~65之间的时间
	// private int[] temp_distribution_0_60 = new int[3]; //血氧值在0~60之间的时间
	private long temp_distribution_90_942 = 0; // 血氧值在90~95之间的时间，值为总毫秒数
	private long temp_distribution_85_902 = 0; // 血氧值在85~90之间的时间，值为总毫秒数
	private long temp_distribution_80_852 = 0; // 血氧值在80~85之间的时间，值为总毫秒数
	private long temp_distribution_75_802 = 0; // 血氧值在75~80之间的时间，值为总毫秒数
	private long temp_distribution_70_752 = 0; // 血氧值在70~75之间的时间，值为总毫秒数
	private long temp_distribution_65_702 = 0; // 血氧值在65~70之间的时间，值为总毫秒数
	private long temp_distribution_0_652 = 0; // 血氧值在0~60之间的时间，值为总毫秒数
	// private long temp_distribution_60_652 = 0; //血氧值在60~65之间的时间，值为总毫秒数
	// private long temp_distribution_0_602 = 0; //血氧值在0~60之间的时间，值为总毫秒数

	// 计算所需的临时变量
	private int[] temp_bef_spo2; // 获取的SPO2数据
	private int[] temp_aft_spo2; // 初步去噪后的SPO2数据
	private int[] temp_aft2_spo2; // 2次去噪后的SPO2数据
	private int[] temp_data_signed; // 标记氧减的检测结果，用于分析
	private String[] temp_string_time; // 与SPO2值对应的时间
	private long[] temp_num_time; // 与SPO2值对应的时间
	private int[][] temp_extract_string_time; // 从字符串中提取的时间数据
	private float temp_st_ave = 0; // 血氧基准值(计算值可能地低于期望值)
	private int[][] temp_duration_AI; // 呼吸暂停的起止时间点
	private int[][] temp_duration_HI; // 低通气的起止时间点
	private int[][] temp_duration_noise; // 噪声点的起止时间点
	private int[][] temp_duration_ODI4; // 氧减的起止时间点
	private int[] temp_bef_pr; // 获取的脉率数据
	private int[] temp_aft_pr; // 获取的脉率数据
	private int[][] temp_waveData; // 脉率波峰的坐标，数据为：起点，峰值点，终点
	private int[] temp_PI; // 灌注指数
	private int[] press;//呼吸数据
	private int[] axis_x; //x轴加速度
	private int[] axis_y;
	private int[] axis_z;
	private int ai_count;//由呼吸数据得到的呼吸暂停次数


	private int hi_count;//由呼吸数据得到的低通气次数

	// 需要输出的结果
	private int[] time_wholeNight; // 整晚睡眠持续时间
	private int[] time_start; // 睡眠测量起始时间
	private int[] time_end; // 睡眠测量终止时间
	private float AHI = 0; // 呼吸暂停低通气指数
	private int AI_count = 0; // 呼吸暂停次数
	private float AI_index = 0; // 呼吸暂停指数
	private int HI_count = 0; // 低通气次数
	private float HI_index = 0; // 低通气指数
	private int ODI4_count = 0; // 氧减饱和度次数
	private float ODI4_index = 0; // 氧减饱和度指数
	private int HSPO2 = 0; // 最高血氧饱和度
	private int LSPO2 = 0; // 最低血氧饱和度
	private float MSPO2 = 0; // 平均血氧饱和度
	private float TS90 = 0; // 血氧饱和度低于90%占比
	private float ODHI = 0; // 氧减危害指数(显示时需要保留三位有效数字)
	private float PI = 0; // 平均灌注指数
	// 由long类型时间计算所得
	private long time_start2 = 0; // 睡眠测量起始时间
	private long time_end2 = 0; // 睡眠测量终止时间
	private long time_wholeNight2 = 0; // 整晚睡眠持续时间，值为总毫秒数

	// 需要输出的结果
	private int[] AI_longest_duration = new int[3]; // 最长呼吸暂停时长，数据格式为：时、分、秒
	private int[] AI_longest_timePoint = new int[3]; // 最长呼吸暂停发生时间，数据格式为：时、分、秒
	private int[] AI_time_sum = new int[3]; // 呼吸暂停总时间，时间的数据格式为：时、分、秒
	private float AI_time_percent = 0; // 呼吸暂停总时间占比
	private int AI_longest_spo2 = 0; // 最长呼吸暂停发生时的最低血氧值
	// 由long类型时间计算所得
	private long AI_longest_duration2 = 0; // 最长呼吸暂停时长，值为总毫秒数
	private long AI_longest_timePoint2 = 0; // 最长呼吸暂停发生时间
	private long AI_time_sum2 = 0; // 呼吸暂停总时间，值为总毫秒数
	private float AI_time_percent2 = 0; // 呼吸暂停总时间占比

	// 需要输出的结果
	private int[] HI_longest_duration = new int[3]; // 最长低通气时长，数据格式为：时、分、秒
	private int[] HI_longest_timePoint = new int[3]; // 最长低通气发生时间，数据格式为：时、分、秒
	private int[] HI_time_sum = new int[3]; // 低通气总时间，时间的数据格式为：时、分、秒
	private float HI_time_percent = 0; // 低通气总时间占比
	private int HI_longest_spo2 = 0; // 最长低通气发生时的最低血氧值
	// 由long类型时间计算所得
	private long HI_longest_duration2 = 0; // 最长低通气时长，值为总毫秒数
	private long HI_longest_timePoint2 = 0; // 最长低通气发生时间
	private long HI_time_sum2 = 0; // 低通气总时间，值为总毫秒数
	private float HI_time_percent2 = 0; // 低通气总时间占比

	// 需要输出的结果
	private int[] ODI4_longest_duration = new int[3]; // 最长氧减时长，数据格式为：时、分、秒
	private int[] ODI4_longest_timePoint = new int[3]; // 最长氧减发生时间，数据格式为：时、分、秒
	private int[] ODI4_largest_duration = new int[3]; // 最大氧减时长，数据格式为：时、分、秒
	private int[] ODI4_largest_timePoint = new int[3]; // 最大氧减发生时间，数据格式为：时、分、秒
	private int ODI4_largest_range = 0; // 最大氧减发生时的血氧下降幅度
	// 由long类型时间计算所得
	private long ODI4_longest_duration2 = 0; // 最长氧减时长，数据格式为：时、分、秒
	private long ODI4_longest_timePoint2 = 0; // 最长氧减发生时间，数据格式为：时、分、秒
	private long ODI4_largest_duration2 = 0; // 最大氧减时长，数据格式为：时、分、秒
	private long ODI4_largest_timePoint2 = 0; // 最大氧减发生时间，数据格式为：时、分、秒

	// 需要输出的结果
	private int[] distribution_90_100 = new int[4]; // 血氧值在90~100之间的参数统计，时间的数据格式为：时、分、秒，最后一位为氧减的次数
	private int[] distribution_80_90 = new int[4]; // 血氧值在80~90之间的参数统计，时间的数据格式为：时、分、秒，最后一位为氧减的次数
	private int[] distribution_70_80 = new int[4]; // 血氧值在70~80之间的参数统计，时间的数据格式为：时、分、秒，最后一位为氧减的次数
	private int[] distribution_60_70 = new int[4]; // 血氧值在60~70之间的参数统计，时间的数据格式为：时、分、秒，最后一位为氧减的次数
	private int[] distribution_50_60 = new int[4]; // 血氧值在50~60之间的参数统计，时间的数据格式为：时、分、秒，最后一位为氧减的次数
	private int[] distribution_0_50 = new int[4]; // 血氧值在0~50之间的参数统计，时间的数据格式为：时、分、秒，最后一位为氧减的次数
	// 由long类型时间计算所得
	private long[] distribution_90_1002 = new long[2]; // 血氧值在90~100之间的参数统计，第一个数据是时间，值为总毫秒数，第二个数据为氧减的次数
	private long[] distribution_80_902 = new long[2]; // 血氧值在80~90之间的参数统计，第一个数据是时间，值为总毫秒数，第二个数据为氧减的次数
	private long[] distribution_70_802 = new long[2]; // 血氧值在70~80之间的参数统计，第一个数据是时间，值为总毫秒数，第二个数据为氧减的次数
	private long[] distribution_60_702 = new long[2]; // 血氧值在60~70之间的参数统计，第一个数据是时间，值为总毫秒数，第二个数据为氧减的次数
	private long[] distribution_50_602 = new long[2]; // 血氧值在50~60之间的参数统计，第一个数据是时间，值为总毫秒数，第二个数据为氧减的次数
	private long[] distribution_0_502 = new long[2]; // 血氧值在0~50之间的参数统计，第一个数据是时间，值为总毫秒数，第二个数据为氧减的次数

	// 需要输出的结果
	private int[] distribution_ODI_0_85; // 以每15分钟为一区间，每区间内氧减低于85的次数
	private float SODHI = 0; // 睡眠氧减危险指数 sleep oxygen desaturation hazard
									// index
	private ArrayList<Param_MODII> MODII = new ArrayList<Param_MODII>(); // 最大氧减密集区间
																				// Maximum
																				// oxygen
																				// desaturation
																				// intensive
																				// interval

	/**
	 * 从一个文件中获取原始数据并完成计算
	 * 
	 * @param inputpath
	 *            数据文件所在文件夹的路径
	 * @param flag_five1
	 *            true：忽略开始的5分钟（300个点）数据和最后1分钟（60个点）数据，false：使用完整数据
	 * @param timeFlag1
	 *            true：使用long类型的数据计算时间，false：使用String类型的数据计算时间
	 * @return true：计算完毕
	 */
	public boolean calculate(String inputpath, boolean flag_five1, boolean flag_time_dataType1, String temp_seperator, int dataType) {
		flag_five = flag_five1;
		flag_time_dataType = flag_time_dataType1;
		// 从TXT文件中读取数据
		if(dataType == Utils.DATATYPE_ASCII){
			readMultiData_1_time_pr_spo2_5min_fromTempFileTotal(inputpath, temp_seperator);
		}else if(dataType == Utils.DATATYPE_BINARY){
			read_binary(inputpath, temp_seperator);
		}

		// 过滤脉率数据
//		temp_aft_pr = denoising_pr(temp_bef_pr);
		// 脉率波峰检测
//		temp_waveData = waveDetection(temp_aft_pr, 300);
		// 计算平均灌注指数
		PI = calAverage(temp_PI);

		// 过滤血氧数据
		temp_aft_spo2 = denoising_spo2(temp_bef_spo2);
		// 检测呼吸暂停、低通气、氧减事件以及异常噪声数据
		// detection_spo2_C_N(temp_aft_spo2);
		// detection_spo2_D_N(temp_aft_spo2);
		// detection_spo2_C_Y(temp_aft_spo2);
		detection_spo2_D_Y(temp_aft_spo2);
		ai_count=detection_press(press);
		hi_count=detection_press_HI(press);
		boolean[] state=silence_Detection(temp_num_time,axis_x, axis_y,axis_z);
		append_String(inputpath,"ai_count:"+ai_count+"   hi_count:"+hi_count+" ,        AI_count0:"+AI_count+" HI_count:"+HI_count);
		Log.e("apneadetection", "ai_count:"+ai_count+"   hi_count:"+hi_count+" ,        AI_count0:"+AI_count+" HI_count:"+HI_count);
		// detection_C(temp_aft_spo2);
		// detection_D(temp_aft_spo2);
		// 二次过滤，处理异常噪声数据
		temp_aft2_spo2 = denoising2(temp_aft_spo2);

		// if(flag_time_dataType){
		// 睡眠起止时间和持续时间
		time_wholeNight2 = calWholeNight(temp_num_time);
		// 计算AHI、AI、HI、MSPO2、LSPO2等指标
		para_HI2(temp_aft_spo2, AI_count, HI_count, ODI4_count, time_wholeNight2);
		// 计算最长呼吸暂停、血氧分布、ODHI等指标
		timeParaCalculate2(temp_aft_spo2, temp_num_time, time_wholeNight2, temp_duration_AI, temp_duration_HI, temp_duration_ODI4);
		// }else{
		// 睡眠起止时间和持续时间
		temp_extract_string_time = extractTime(temp_string_time);
		time_wholeNight = calLastTime(temp_extract_string_time);
		// 计算AHI、AI、HI、MSPO2、LSPO2等参数
		para_HI(temp_aft_spo2, AI_count, HI_count, ODI4_count, time_wholeNight);
		// 计算最长呼吸暂停、血氧分布、ODHI等指标
		timeParaCalculate(temp_aft_spo2, temp_extract_string_time, time_wholeNight, temp_duration_AI, temp_duration_HI, temp_duration_ODI4);
		// 计算氧减密集区间、氧减危险指数等指标
//		hazardIndex(temp_extract_string_time, time_wholeNight, temp_duration_ODI4, temp_aft2_spo2, temp_aft_pr);
		// }
		
		//清空临时变量
		temp_distribution_90_94 = null; // 血氧值在90~95之间的时间
		temp_distribution_85_90 = null; // 血氧值在85~90之间的时间
		temp_distribution_80_85 = null; // 血氧值在80~85之间的时间
		temp_distribution_75_80 = null; // 血氧值在75~80之间的时间
		temp_distribution_70_75 = null; // 血氧值在70~75之间的时间
		temp_distribution_65_70 = null; // 血氧值在65~70之间的时间
		temp_distribution_0_65 = null; // 血氧值在0~65之间的时间
		temp_distribution_90_942 = 0; // 血氧值在90~95之间的时间，值为总毫秒数
		temp_distribution_85_902 = 0; // 血氧值在85~90之间的时间，值为总毫秒数
		temp_distribution_80_852 = 0; // 血氧值在80~85之间的时间，值为总毫秒数
		temp_distribution_75_802 = 0; // 血氧值在75~80之间的时间，值为总毫秒数
		temp_distribution_70_752 = 0; // 血氧值在70~75之间的时间，值为总毫秒数
		temp_distribution_65_702 = 0; // 血氧值在65~70之间的时间，值为总毫秒数
		temp_distribution_0_652 = 0; // 血氧值在0~60之间的时间，值为总毫秒数
		
		temp_bef_spo2 = null; // 获取的SPO2数据
		temp_aft_spo2 = null; // 初步去噪后的SPO2数据
		temp_aft2_spo2 = null; // 2次去噪后的SPO2数据
		temp_data_signed = null; // 标记氧减的检测结果，用于分析
		temp_string_time = null; // 与SPO2值对应的时间
		temp_num_time = null; // 与SPO2值对应的时间
		temp_extract_string_time = null; // 从字符串中提取的时间数据
		temp_st_ave = 0; // 血氧基准值(计算值可能地低于期望值)
		temp_duration_AI = null; // 呼吸暂停的起止时间点
		temp_duration_HI = null; // 低通气的起止时间点
		temp_duration_noise = null; // 噪声点的起止时间点
		temp_duration_ODI4 = null; // 氧减的起止时间点
		temp_bef_pr = null; // 获取的脉率数据
		temp_aft_pr = null; // 获取的脉率数据
		temp_waveData = null; // 脉率波峰的坐标，数据为：起点，峰值点，终点
		temp_PI = null; // 灌注指数
		return true;
	}
	/**采集数据，获得正常呼吸幅值大小
	 * 
	 * @param press  原始数据
	 * @return		正常呼吸振幅
	 */
	private int getNormAmplitude(int[] press){
		int amplitude=40;					//初步设定正常值
		int sampletime=30;					//采样时间
		int span=10;						//每次采样的区间大小
		int min=0;							//
		int max=0;
		int temp_count=0;					//采样次数=采样时间/采样区间
		int count=(int)Math.floor(sampletime/span);//取整
		int[] sample=new int[count];		//采样值
		long sum=0;
		for (int i = 0; i < press.length-span; i=i+span) {
			for (int j = i; j <=i+span; j++) {
				if (press[j]<=min) {
					min=press[j];
				}
				if (press[j]>=max) {
					max=press[j];
				}
			}
			int temp=max-min;
			if (temp>30&&temp<200) {
				sample[temp_count++]=temp;
			}
			if (temp_count>=count) {
				break;
			}
		}
		if (sample.length==count) {
			for (int i = 0; i < sample.length; i++) {
				sum = sum + sample[i];
				amplitude = (int) (sum / sample.length);
				Log.e("sample", String.valueOf(sample[i]));
			} 
		}else {
			Log.e("getNormAmplitude(int[] press)", "采样不足");
		}
		return amplitude;
	}
	
	/**检测呼吸暂停次数
	 * 
	 * @param press[]
	 * @return呼吸暂停次数
	 */
	private int detection_press(int[] press){
		int count=0;                  //暂时存放呼吸暂停次数
		int amplitude=5;                 //限制振幅大小
		int press_deadline=10;                 //最大呼吸暂停持续时间
		int timeline=0;                 //实际呼吸暂停持续时间
		int min=0;                 //暂时存放最小点
		int max=0;                 //暂时存放最大点
		int margin1 = 0;
		int margin2=0;
		int middle=0;
		if (press!=null&&press.length>press_deadline) {
			for (int i = 2; i < press.length; i++) {
				if (press[i]<=min) {
					min=press[i];
				}
				if (press[i]>=max) {
					max=press[i];
				}
				if((max-min)<=amplitude){
					timeline++;
					middle=press[i];
				}else {
					Log.e("timeline", ""+timeline);
					if (timeline>=press_deadline&&timeline<=90) {    //呼吸暂停时间在deadline~90之间时，算一次呼吸暂停
						
						margin2=press[i];
						margin1=press[i-timeline-2];
						if (((margin1-amplitude>middle)&&(margin2+amplitude<middle))||((margin1-amplitude<middle)&&(margin2+amplitude>middle))) {
							count++;
							Log.e("呼吸暂停结束点", String.valueOf(i+2)+":"+String.valueOf(press[i]));
						}											//更严格的判断条件
//						count++;
					}
					timeline=0;
					min=press[i];
					max=press[i];
				}
			}
		}else {
			Log.e("detection_press(int[] press)", "数据不够");
		}
		return count;
	}
	
	/**检测低通气次数
	 * 
	 * @param press
	 * @return低通气次数
	 */
	private int detection_press_HI(int[] press){
		int count=0;                  //暂时存放呼吸暂停次数
		int norm=getNormAmplitude(press);
		int amplitude=(int)(norm*0.25);                 //限制振幅大小
		int press_deadline=10;                 //最大呼吸暂停持续时间
		int timeline=0;                 //实际呼吸暂停持续时间
		int min=0;                 //暂时存放最小点
		int max=0;                 //暂时存放最大点
		if (press!=null&&press.length>press_deadline) {
			for (int i = 0; i < press.length; i++) {
				if (press[i]<=min) {
					min=press[i];
				}
				if (press[i]>=max) {
					max=press[i];
				}
				if((max-min)<=amplitude){
					timeline++;
				}else {
					Log.e("timeline", ""+timeline);
					if (timeline>=press_deadline&&timeline<=90) {    
						count++;
						Log.e("低通气结束点", String.valueOf(i+2)+":"+String.valueOf(press[i]));
					}
					timeline=0;
					min=press[i];
					max=press[i];
				}
			}
		}else {
			Log.e("detection_press(int[] press)", "数据不够");
		}

		return count;
	}
	/**
	 * 检测运动事件
	 * @param temp_num_time
	 * @param axis_x
	 * @param axis_y
	 * @param axis_z
	 * @return
	 */
	private int[] move_Detection(long[] temp_num_time, int[] axis_x, int[] axis_y, int[] axis_z){
		long starttime=0;
		long endtime=0;
		int amplitude=100;
		int datawindow=5;
		int maxamptitude=40;
		int currentavg_x=0;
		int currentavg_y=0;
		int currentavg_z=0;
		int pre_avg_x=0;
		int pre_avg_y=0;
		int pre_avg_z=0;
		int sum_x=0;
		int sum_y=0;
		int sum_z=0;
		for (int i = 0; i < axis_x.length; i++) {
			for (int j = i; j < datawindow; j++) {
				sum_x = sum_x + axis_x[i];
			}
			currentavg_x=sum_x/datawindow;
			for (int j = i; j < datawindow; j++) {
				sum_y = sum_y + axis_y[i];
			}
			currentavg_y=sum_y/datawindow;
			for (int j = i; j < datawindow; j++) {
				sum_z = sum_z + axis_z[i];
			}
			currentavg_z=sum_z/datawindow;
			if(Math.abs(currentavg_x-pre_avg_x)>maxamptitude){
				starttime=temp_num_time[i];
			}
		}
		return null;
		
	}
	/**
	 * 检测静止事件
	 * @param temp_num_time
	 * @param axis_x
	 * @param axis_y
	 * @param axis_z
	 * @return
	 */
	private boolean[] silence_Detection(long[] temp_num_time, int[] axis_x, int[] axis_y, int[] axis_z){
		int datacount=temp_num_time.length;
		boolean[] state = new boolean[datacount];   //false:动         true:静止
		int max_x=0;
		int min_x=0;
		int max_y=0;
		int min_y=0;
		int max_z=0;
		int min_z=0;
		int amptitude=100;
		int datawindow=5;
		if (axis_x!=null&&axis_y!=null&&axis_z!=null&&axis_x.length>datawindow) {
			for (int i = 0; i < temp_num_time.length - datawindow; i++) {
				state[i] = false;
				min_x = axis_x[i];
				max_x = axis_x[i];
				for (int j = 0; j < datawindow; j++) {
					if (axis_x[i + j] < min_x) {
						min_x = axis_x[i + j];
					}
					if (axis_x[i + j] > max_x) {
						max_x = axis_x[i + j];
					}
				}
				min_y = axis_y[i];
				max_y = axis_y[i];
				for (int j = 0; j < datawindow; j++) {
					if (axis_y[i + j] < min_y) {
						min_y = axis_y[i + j];
					}
					if (axis_y[i + j] > max_y) {
						max_y = axis_y[i + j];
					}
				}
				min_z = axis_z[i];
				max_z = axis_z[i];
				for (int j = 0; j < datawindow; j++) {
					if (axis_z[i + j] < min_z) {
						min_z = axis_z[i + j];
					}
					if (axis_z[i + j] > max_z) {
						max_z = axis_z[i + j];
					}
				}
				if ((max_x - min_x) < amptitude && (max_y - min_y) < amptitude && (max_y - min_y) < amptitude) {
					for (int j = 0; j < datawindow; j++) {
						state[i + j] = true;
					}

				}
				Log.e("detection_silence", "" + i + ":" + state[i]);
			} 
		}
		return state;
		
	}
	/**
	 * 
	 * @param inputpath
	 * @param str
	 */
	public void append_String(String inputpath,String str){
		File file =new File(inputpath);
		try {
			Writer out = new FileWriter(file, true);
			out.write(str);
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/**
	 * 对SPO2数据初步去噪的方法；将大于100的值取为100，将小于70的值取为70，并将去噪后的数组返回
	 * 
	 * @param temp_bef_spo2
	 *            去噪前的血氧数据
	 * @return 去噪后的血氧数据
	 */
	public int[] denoising_spo2(int[] temp_bef_spo2) {
		int bef_len = temp_bef_spo2.length;
		int[] temp = new int[bef_len]; // 接收bef数组
		for (int i = 0; i < bef_len; i++)
			temp[i] = temp_bef_spo2[i];

		// Arrays.sort(temp);
		// for(int i = 0; i < 10; i++){
		// System.out.println(temp[i]);
		// }

		if (temp[0] > 100)
			temp[0] = 100; // 如果第一个SPO2的值大于100，则取为100
		if (temp[0] < 50)
			temp[0] = (int) (50 + Math.random() * 3); // 如果第一个SPO2值小于等于50，则取为50-53的随机数
		// 整体去噪
		for (int i = 0; i < bef_len; i++) {
			// 如果当前值与后一个值的差值大于15，或后一个SPO2值大于100，或后一个SPO2值小于50，或某点在前后两点值波动幅度小于等于2的情况下变化幅度大于等于5，则后一个值用前一个值代替
			if ((i + 1 < bef_len && (Math.abs(temp[i] - temp[i + 1]) > 15 || temp[i + 1] > 100 || temp[i + 1] < 50))
					|| (i + 2 < bef_len && Math.abs(temp[i] - temp[i + 2]) < 2
							&& (Math.abs(temp[i] - temp[i + 1]) >= 5 || Math.abs(temp[i + 2] - temp[i + 1]) >= 5))) {
				temp[i + 1] = temp[i];
			}
		}
		return temp;
	}

	/**
	 * 对PR数据初步去噪的方法；将大于100的值取为100，将小于25的值取为75，并将去噪后的数组返回
	 * 
	 * @param bef_pr去噪前的血氧数据
	 * @return 去噪后的血氧数据
	 */
	public int[] denoising_pr(int[] temp_bef_pr) {
		int bef_len = temp_bef_pr.length;
		int[] temp = new int[bef_len]; // 接收bef数组
		for (int i = 0; i < bef_len; i++)
			temp[i] = temp_bef_pr[i];

		if (temp[0] == 255 || temp[0] <= 25)
			temp[0] = 75; // 如果第一个PR的值等于255，或小于等于25，则取为75
		// 整体去噪
		for (int i = 0; i < bef_len; i++) {
			// 如果当前值与后一个值的差值大于40，或后一个PR值等于255，或后一个PR值小于等于25，则后一个值用前一个值代替
			if (i + 1 < bef_len && (Math.abs(temp[i] - temp[i + 1]) > 40 || temp[i + 1] == 255 || temp[i + 1] <= 25)) {
				temp[i + 1] = temp[i];
			}
		}
		return temp;
	}

	/**
	 * 对SPO2数据2次去噪的方法；对噪声数据均值化处理，即噪声区间内的值取首尾两个值的平均值
	 * 
	 * @param temp_aft_spo2
	 *            需要去噪的血氧数据
	 * @return 去噪后的血氧数据
	 */
	public int[] denoising2(int[] temp_aft_spo2) {
		int data_len = temp_aft_spo2.length;
		int[] temp = new int[data_len]; // 接收aft数组
		for (int i = 0; i < data_len; i++)
			temp[i] = temp_aft_spo2[i];

		// 局部去噪
		int ave = 0;
		int t = 0;
		for (int i = 0; i < temp_duration_noise.length & temp_duration_noise[i][1] > 0; i++) {
			ave = (temp[temp_duration_noise[i][0]] + temp[temp_duration_noise[i][3]]) / 2;
			t = temp_duration_noise[i][0] + 1;
			while (t < temp_duration_noise[i][3]) {
				temp[t] = ave;
				t++;
			}
		}
		return temp;
	}

	/**
	 * 根据SPO2值，检测呼吸暂停的起止时间并判断呼吸暂停次数
	 * 
	 * @param temp_aft_spo2
	 *            用于检测的SPO2数据
	 */
	public void detection_D(int[] aft) {
		int len = aft.length;
		int[] temp = new int[len]; // 接收SPO2的临时数组
		for (int i = 0; i < len; i++)
			temp[i] = aft[i];
		temp_duration_AI = new int[len][4]; // 呼吸暂停数据
		temp_duration_HI = new int[len][4]; // 低通气数据
		temp_duration_noise = new int[len][5]; // 噪声数据
		temp_duration_ODI4 = new int[len][5]; // 氧减数据
		int count = 0; // 呼吸暂停起止时间的计数
		int count2 = 0; // 低通气起止时间的计数
		int count3 = 0; // 噪声起止时间的计数
		int count4 = 0; // 氧减起止时间的计数
		int sum = 0;
		// for(int i = 180; i < 300; i++){
		// sum += temp[i];
		// }
		for (int i = 0; i < len; i++) {
			sum += temp[i];
		}
		temp_st_ave = sum / len; // temp_st_ave：基准值——————————————取值方式有待商议
//		System.out.println("temp_st_ave：" + temp_st_ave); // 测试输出
		int t_down = 0, t_up = 0; // t_down：血氧下降的时间，t_up：血氧上升的时间
		int valley = 0, last = 0; // valley：血氧在最低点维持的时间，last：一次血氧下降的总持续时间
		int[] t = { 0, 0, 0, 0, 0, 0 }; // 检测呼吸暂停所需的一个临时数组
										// t[0]:下降起点，t[1]:下降终点，t[2]:上升起点，t[3]:上升终点，t[4]:上升后的水平移动，t[5]:出现锯齿时的最低值
		boolean flag = false; // 开始检测的标志，true表示检测到第一个点
		int count_horizontal = 0; // 水平线的数据个数
		for (int i = 0; i < len - 2; i++) {
			if (flag == true) {
				if (temp[i] > temp[i + 1]) { // 检测到下降
					// 如果水平线的数据长度大于15，且血氧值与起始点血氧值差距小于4，则将起点调整为水平线末端
					if (count_horizontal > 15 && t[3] == 0 && temp[t[0]] - temp[i] < 4) {
						t[0] = i;
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
					}
					count_horizontal = 0;

					if (t[3] == 0) { // 正常下降
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
					} else { // 异常下降
						if (t[3] == i && temp[i - 1] == temp[i + 1]) { // 出现可忽略的小尖角(凸)
							if (t[2] == t[3] - 1) { // 下降过程中
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
							} else { // 上升过程中
								t[3] = i + 1;
								t[4] = 0;
							}
						} else if (t[4] == i && (temp[i] == temp[i + 2] || temp[t[3]] == temp[t[3] - 2])) { // 出现可忽略的小尖角(凹)
							if (temp[t[3]] == temp[t[3] - 2]) { // 下降过程中
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
							} else { // 上升过程中
								t[3] = i + 1;
								t[4] = 0;
							}
						} else { // 可能出现锯齿，可能氧降结束
							if ((temp[t[0]] - temp[t[3]] <= 2)) {
								// 一次下降过程结束
								t_down = t[1] - t[0];
								t_up = t[3] - t[2];
								valley = t[2] - t[1];
								last = t[3] - t[0];
								// System.out.println("t[0] = " + t[0] +"\tt[1]
								// = "+ t[1] +"\tt[2] = "+ t[2] +"\tt[3] = "+
								// t[3]); //测试输出
								// System.out.println("temp[t[0]] = " +
								// temp[t[0]] + "\ttemp[t[1]] = " + temp[t[1]] +
								// "\ttemp[t[2]] = " + temp[t[2]] +
								// "\ttemp[t[3]] = " + temp[t[3]]);
								// System.out.println("t_down = " + t_down +
								// "\tt_up = " + t_up + "\tvalley = " + valley +
								// "\tlast = " + last);
								// System.out.println("");

								if (temp[t[0]] - temp[t[1]] >= 3 && temp[t[3]] - temp[t[1]] >= 3 && last >= 7
										&& last < 210) { // 氧减的判断条件
									if (t_down >= 2 && t_up >= 2 && t[5] == 0) { // 呼吸暂停和低通气共同的判断条件
										if (temp[t[0]] - temp[t[1]] >= 5 && temp[t[3]] - temp[t[1]] >= 5 && t_down >= 3
												&& t_up >= 3) { // 呼吸暂停的判断条件
											temp_duration_AI[count][0] = t[0];
											temp_duration_AI[count][1] = t[1];
											temp_duration_AI[count][2] = t[2];
											temp_duration_AI[count][3] = t[3];
											count++;
										} else {
											temp_duration_HI[count2][0] = t[0];
											temp_duration_HI[count2][1] = t[1];
											temp_duration_HI[count2][2] = t[2];
											temp_duration_HI[count2][3] = t[3];
											count2++;
										}
									}
									temp_duration_ODI4[count4][0] = t[0];
									temp_duration_ODI4[count4][1] = t[1];
									temp_duration_ODI4[count4][2] = t[2];
									temp_duration_ODI4[count4][3] = t[3];
									temp_duration_ODI4[count4][4] = t[5];
									count4++;
								} else if (t[3] - t[0] < 300 && ((t[5] == 0
										&& (temp[t[0]] - temp[t[1]] >= 5 || temp[t[3]] - temp[t[1]] >= 5))
										|| (t[5] != 0
												&& (temp[t[0]] - temp[t[5]] >= 5 || temp[t[3]] - temp[t[5]] >= 5)))) {
									temp_duration_noise[count3][0] = t[0];
									temp_duration_noise[count3][1] = t[1];
									temp_duration_noise[count3][2] = t[2];
									temp_duration_noise[count3][3] = t[3];
									temp_duration_noise[count3][4] = t[5];
									count3++;
								}
								// 将当前点作为下个下降的起点，继续检测
								t[0] = i;
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
							} else {
								// 出现较大锯齿
								if (t[5] == 0) {
									t[5] = t[1];
								} else {
									t[5] = temp[t[1]] < temp[t[5]] ? t[1] : t[5]; // 将最低点保存在t[5]中
								}
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
							}
						}
					}
				} else if (temp[i] == temp[i + 1]) { // 检测到水平移动
					if (t[3] == 0) { // 下降中的水平移动
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
					}
					if (t[3] > 0)
						t[4] = i + 1; // 上升中的水平移动
					count_horizontal++;
				} else if (temp[i] < temp[i + 1]) { // 检测到上升
					// 如果水平线的数据长度大于15，且血氧值与起始点血氧值差距小于等于2，则判断一次氧降结束
					if ((count_horizontal > 15 && t[3] != 0 && t[4] != 0 && temp[t[0]] - temp[3] <= 2)
							|| (temp[t[0]] - temp[t[3]] <= 0 && t[3] != 0)) {
						// 一次下降过程结束，在血氧上升到较高水平的情况下平移一段时间后，再次上升
						t_down = t[1] - t[0];
						t_up = t[3] - t[2];
						valley = t[2] - t[1];
						last = t[3] - t[0];
						// System.out.println("t[0] = " + t[0] +"\tt[1] = "+
						// t[1] +"\tt[2] = "+ t[2] +"\tt[3] = "+ t[3]); //测试输出
						// System.out.println("temp[t[0]] = " + temp[t[0]] +
						// "\ttemp[t[1]] = " + temp[t[1]] + "\ttemp[t[2]] = " +
						// temp[t[2]] + "\ttemp[t[3]] = " + temp[t[3]]);
						// System.out.println("t_down = " + t_down + "\tt_up = "
						// + t_up + "\tvalley = " + valley + "\tlast = " +
						// last);
						// System.out.println("");
						if (temp[t[0]] - temp[t[1]] >= 3 && temp[t[3]] - temp[t[1]] >= 3 && last >= 7 && last < 210) { // 氧减的判断条件
							if (t_down >= 2 && t_up >= 2 && t[5] == 0) { // 呼吸暂停和低通气共同的判断条件
								if (temp[t[0]] - temp[t[1]] >= 5 && temp[t[3]] - temp[t[1]] >= 5 && t_down >= 3
										&& t_up >= 3) { // 呼吸暂停的判断条件
									temp_duration_AI[count][0] = t[0];
									temp_duration_AI[count][1] = t[1];
									temp_duration_AI[count][2] = t[2];
									temp_duration_AI[count][3] = t[3];
									count++;
								} else {
									temp_duration_HI[count2][0] = t[0];
									temp_duration_HI[count2][1] = t[1];
									temp_duration_HI[count2][2] = t[2];
									temp_duration_HI[count2][3] = t[3];
									count2++;
								}
							}
							temp_duration_ODI4[count4][0] = t[0];
							temp_duration_ODI4[count4][1] = t[1];
							temp_duration_ODI4[count4][2] = t[2];
							temp_duration_ODI4[count4][3] = t[3];
							temp_duration_ODI4[count4][4] = t[5];
							count4++;
						} else if (t[3] - t[0] < 300 && ((t[5] == 0
								&& (temp[t[0]] - temp[t[1]] >= 5 || temp[t[3]] - temp[t[1]] >= 5))
								|| (t[5] != 0 && (temp[t[0]] - temp[t[5]] >= 5 || temp[t[3]] - temp[t[5]] >= 5)))) {
							temp_duration_noise[count3][0] = t[0];
							temp_duration_noise[count3][1] = t[1];
							temp_duration_noise[count3][2] = t[2];
							temp_duration_noise[count3][3] = t[3];
							temp_duration_noise[count3][4] = t[5];
							count3++;
						}
						// 重置参数，准备检测下个下降的起点
						flag = false;
						t[0] = 0;
						t[1] = 0;
						t[2] = 0;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
					} else {
						// 正常上升
						count_horizontal = 0;
						t[3] = i + 1;
						t[4] = 0;
					}
				}

				// 一次下降过程结束，血氧值回升到下降的水平
				if (temp[t[0]] - temp[t[3]] <= 0 && t[3] != 0) {
					t_down = t[1] - t[0];
					t_up = t[3] - t[2];
					valley = t[2] - t[1];
					last = t[3] - t[0];
					// System.out.println("t[0] = " + t[0] +"\tt[1] = "+ t[1]
					// +"\tt[2] = "+ t[2] +"\tt[3] = "+ t[3]); //测试输出
					// System.out.println("temp[t[0]] = " + temp[t[0]] +
					// "\ttemp[t[1]] = " + temp[t[1]] + "\ttemp[t[2]] = " +
					// temp[t[2]] + "\ttemp[t[3]] = " + temp[t[3]]);
					// System.out.println("t_down = " + t_down + "\tt_up = " +
					// t_up + "\tvalley = " + valley + "\tlast = " + last);
					// System.out.println("");

					if (temp[t[0]] - temp[t[1]] >= 3 && temp[t[3]] - temp[t[1]] >= 3 && last >= 7 && last < 210) { // 氧减的判断条件
						if (t_down >= 2 && t_up >= 2 && t[5] == 0) { // 呼吸暂停和低通气共同的判断条件
							if (temp[t[0]] - temp[t[1]] >= 5 && temp[t[3]] - temp[t[1]] >= 5 && t_down >= 3
									&& t_up >= 3) { // 呼吸暂停的判断条件
								temp_duration_AI[count][0] = t[0];
								temp_duration_AI[count][1] = t[1];
								temp_duration_AI[count][2] = t[2];
								temp_duration_AI[count][3] = t[3];
								count++;
							} else {
								temp_duration_HI[count2][0] = t[0];
								temp_duration_HI[count2][1] = t[1];
								temp_duration_HI[count2][2] = t[2];
								temp_duration_HI[count2][3] = t[3];
								count2++;
							}
						}
						temp_duration_ODI4[count4][0] = t[0];
						temp_duration_ODI4[count4][1] = t[1];
						temp_duration_ODI4[count4][2] = t[2];
						temp_duration_ODI4[count4][3] = t[3];
						temp_duration_ODI4[count4][4] = t[5];
						count4++;
					} else if (t[3] - t[0] < 300
							&& ((t[5] == 0 && (temp[t[0]] - temp[t[1]] >= 5 || temp[t[3]] - temp[t[1]] >= 5))
									|| (t[5] != 0 && (temp[t[0]] - temp[t[5]] >= 5 || temp[t[3]] - temp[t[5]] >= 5)))) {
						temp_duration_noise[count3][0] = t[0];
						temp_duration_noise[count3][1] = t[1];
						temp_duration_noise[count3][2] = t[2];
						temp_duration_noise[count3][3] = t[3];
						temp_duration_noise[count3][4] = t[5];
						count3++;
					}
					// 重置参数，准备检测下个下降的起点
					flag = false;
					t[0] = 0;
					t[1] = 0;
					t[2] = 0;
					t[3] = 0;
					t[4] = 0;
					t[5] = 0;
				}
			} else if (temp[i] > temp[i + 1] && temp[i] > temp_st_ave - 5) {
				t[0] = i;
				t[1] = i + 1;
				t[2] = i + 1;
				flag = true;
			}
		}
		AI_count = count;
		HI_count = count2;
		ODI4_count = count4;
	}

	/**
	 * 根据SPO2值，检测呼吸暂停的起止时间并判断呼吸暂停次数
	 * 
	 * @param temp_aft_spo2
	 *            用于检测的SPO2数据
	 */
	public void detection_C(int[] temp_aft_spo2) {
		int len = temp_aft_spo2.length;
		int[] temp = new int[len]; // 接收SPO2的临时数组
		for (int i = 0; i < len; i++)
			temp[i] = temp_aft_spo2[i];
		temp_duration_AI = new int[len / 4][4]; // 呼吸暂停数据
		temp_duration_HI = new int[len / 4][4]; // 低通气数据
		temp_duration_noise = new int[len / 4][5]; // 噪声数据
		temp_duration_ODI4 = new int[len / 4][5]; // 氧减数据
		int count_ai = 0; // 呼吸暂停起止时间的计数
		int count_hi = 0; // 低通气起止时间的计数
		int count_noise = 0; // 噪声起止时间的计数
		int count_odi4 = 0; // 氧减起止时间的计数

		int sum_spo2 = 0;
		for (int i : temp)
			sum_spo2 += i;
		temp_st_ave = sum_spo2 / len; // temp_st_ave：基准值——————————————取值方式有待商议
//		System.out.println("temp_st_ave：" + temp_st_ave); // 测试输出

		int t_down = 0, t_up = 0; // t_down：血氧下降的时间，t_up：血氧上升的时间
		int valley = 0, last = 0; // valley：血氧在最低点维持的时间，last：一次血氧下降的总持续时间
		int[] t = { 0, 0, 0, 0, 0, 0 }; // 检测呼吸暂停所需的一个临时数组
										// t[0]:下降起点，t[1]:下降终点，t[2]:上升起点，t[3]:上升终点，t[4]:上升后的水平移动，t[5]:出现锯齿时的最低值
		boolean flag = false; // 开始检测的标志，true表示检测到起点，false表示检测到终点并判断完毕
		int count_horizontal = 0; // 水平线的数据个数

		int ch = 15; // count_horizontal的阈值（包含）

		for (int i = 0; i < len - 2; i++) {
			if (flag == true) {
				if (temp[i] > temp[i + 1]) { // 检测到下降
					// 如果水平线的数据长度大于等于阈值ch，且血氧值与起始点血氧值差距小于4，则将起点调整为水平线末端
					if (count_horizontal >= ch && t[3] == 0 && temp[t[0]] - temp[i] < 4) {
						t[0] = i;
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
					}
					count_horizontal = 0;

					if (t[3] == 0) { // 正常下降
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
					} else { // 异常下降
						if (t[3] == i && temp[i - 1] == temp[i + 1]) { // 出现可忽略的小尖角(凸)
							if (t[2] == t[3] - 1) { // 下降过程中
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
							} else { // 上升过程中
								t[3] = i + 1;
								t[4] = 0;
							}
						} else if (t[4] == i && (temp[i] == temp[i + 2] || temp[t[3]] == temp[t[3] - 2])) { // 出现可忽略的小尖角(凹)
							if (temp[t[3]] == temp[t[3] - 2]) { // 下降过程中
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
							} else { // 上升过程中
								t[3] = i + 1;
								t[4] = 0;
							}
						} else { // 可能出现锯齿，可能氧降结束
							if ((temp[t[0]] - temp[t[3]] <= 2)) {
								// 一次下降过程结束
								t_down = t[1] - t[0];
								t_up = t[3] - t[2];
								valley = t[2] - t[1];
								last = t[3] - t[0];

								if ((temp_st_ave - temp[t[1]] >= 3 || temp_st_ave - temp[t[5]] >= 3
										|| temp[t[0]] - temp[t[1]] >= 3 || temp[t[0]] - temp[t[5]] >= 3) && last >= 5
										&& last < 210) { // 氧减的判断条件 //temp[t[0]]
															// - temp[t[1]] >= 3
															// && temp[t[3]] -
															// temp[t[1]] >= 3
															// &&
									if (t[5] == 0) { // 呼吸暂停和低通气共同的判断条件 //t_down
														// >= 2 && t_up >= 2 &&
										if (temp[t[0]] - temp[t[1]] >= 3 && temp[t[3]] - temp[t[1]] >= 3) { // 呼吸暂停的判断条件
																											// //
																											// &&
																											// t_down
																											// >=
																											// 3
																											// &&
																											// t_up
																											// >=
																											// 3
											temp_duration_AI[count_ai][0] = t[0];
											temp_duration_AI[count_ai][1] = t[1];
											temp_duration_AI[count_ai][2] = t[2];
											temp_duration_AI[count_ai][3] = t[3];
											count_ai++;
										} else {
											temp_duration_HI[count_hi][0] = t[0];
											temp_duration_HI[count_hi][1] = t[1];
											temp_duration_HI[count_hi][2] = t[2];
											temp_duration_HI[count_hi][3] = t[3];
											count_hi++;
										}
									}
									temp_duration_ODI4[count_odi4][0] = t[0];
									temp_duration_ODI4[count_odi4][1] = t[1];
									temp_duration_ODI4[count_odi4][2] = t[2];
									temp_duration_ODI4[count_odi4][3] = t[3];
									temp_duration_ODI4[count_odi4][4] = t[5];

									count_odi4++;
								} else { // if(t[3] - t[0] < 300 && ((t[5] == 0
											// && (temp[t[0]] - temp[t[1]] >= 5
											// || temp[t[3]] - temp[t[1]] >= 5))
											// || (t[5] > 0 && (temp[t[0]] -
											// temp[t[5]] >= 5 || temp[t[3]] -
											// temp[t[5]] >= 5))))
									temp_duration_noise[count_noise][0] = t[0];
									temp_duration_noise[count_noise][1] = t[1];
									temp_duration_noise[count_noise][2] = t[2];
									temp_duration_noise[count_noise][3] = t[3];
									temp_duration_noise[count_noise][4] = t[5];
									count_noise++;
								}
								// 将当前点作为下个下降的起点，继续检测
								t[0] = i;
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
							} else {
								// 出现较大锯齿
								if (t[5] == 0) {
									t[5] = t[1];
								} else {
									t[5] = temp[t[1]] < temp[t[5]] ? t[1] : t[5]; // 将最低点保存在t[5]中
								}
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
							}
						}
					}
				} else if (temp[i] == temp[i + 1]) { // 检测到水平移动
					if (t[3] == 0) { // 下降中的水平移动
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
					} else {
						t[4] = i + 1; // 上升中的水平移动
					}
					count_horizontal++;
				} else if (temp[i] < temp[i + 1]) { // 检测到上升
					// 如果水平线的数据长度大于等于阈值ch，且当前血氧值与起始点血氧值的差值小于等于2，则判断一次氧降结束（终点不高于起点）
					if ((count_horizontal >= ch && t[3] != 0 && t[4] != 0 && temp[t[0]] - temp[t[3]] <= 2)
							|| (temp[t[0]] - temp[t[3]] <= 0 && t[3] != 0)) {
						// 一次下降过程结束，在血氧上升到较高水平的情况下平移一段时间后，再次上升
						t_down = t[1] - t[0];
						t_up = t[3] - t[2];
						valley = t[2] - t[1];
						last = t[3] - t[0];

						if ((temp_st_ave - temp[t[1]] >= 3 || temp_st_ave - temp[t[5]] >= 3
								|| temp[t[0]] - temp[t[1]] >= 3 || temp[t[0]] - temp[t[5]] >= 3) && last >= 5
								&& last < 210) { // 氧减的判断条件 //temp[t[0]] -
													// temp[t[1]] >= 3 &&
													// temp[t[3]] - temp[t[1]]
													// >= 3 &&
							if (t[5] == 0) { // 呼吸暂停和低通气共同的判断条件 //t_down >= 2 &&
												// t_up >= 2 &&
								if (temp[t[0]] - temp[t[1]] >= 3 && temp[t[3]] - temp[t[1]] >= 3) { // 呼吸暂停的判断条件
																									// //
																									// &&
																									// t_down
																									// >=
																									// 3
																									// &&
																									// t_up
																									// >=
																									// 3
									temp_duration_AI[count_ai][0] = t[0];
									temp_duration_AI[count_ai][1] = t[1];
									temp_duration_AI[count_ai][2] = t[2];
									temp_duration_AI[count_ai][3] = t[3];
									count_ai++;
								} else {
									temp_duration_HI[count_hi][0] = t[0];
									temp_duration_HI[count_hi][1] = t[1];
									temp_duration_HI[count_hi][2] = t[2];
									temp_duration_HI[count_hi][3] = t[3];
									count_hi++;
								}
							}
							temp_duration_ODI4[count_odi4][0] = t[0];
							temp_duration_ODI4[count_odi4][1] = t[1];
							temp_duration_ODI4[count_odi4][2] = t[2];
							temp_duration_ODI4[count_odi4][3] = t[3];
							temp_duration_ODI4[count_odi4][4] = t[5];

							count_odi4++;
						} else { // if(t[3] - t[0] < 300 && ((t[5] == 0 &&
									// (temp[t[0]] - temp[t[1]] >= 5 ||
									// temp[t[3]] - temp[t[1]] >= 5)) || (t[5] >
									// 0 && (temp[t[0]] - temp[t[5]] >= 5 ||
									// temp[t[3]] - temp[t[5]] >= 5))))
							temp_duration_noise[count_noise][0] = t[0];
							temp_duration_noise[count_noise][1] = t[1];
							temp_duration_noise[count_noise][2] = t[2];
							temp_duration_noise[count_noise][3] = t[3];
							temp_duration_noise[count_noise][4] = t[5];
							count_noise++;
						}
						// 重置参数，准备检测下个下降的起点
						flag = false;
						t[0] = 0;
						t[1] = 0;
						t[2] = 0;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
					} else {
						// 正常上升
						count_horizontal = 0;
						t[3] = i + 1;
						t[4] = 0;
					}
				}

				// 一次下降过程结束，血氧值回升到下降的水平
				if (temp[t[0]] - temp[t[3]] <= 0 && t[3] != 0) {
					t_down = t[1] - t[0];
					t_up = t[3] - t[2];
					valley = t[2] - t[1];
					last = t[3] - t[0];

					if ((temp_st_ave - temp[t[1]] >= 3 || temp_st_ave - temp[t[5]] >= 3 || temp[t[0]] - temp[t[1]] >= 3
							|| temp[t[0]] - temp[t[5]] >= 3) && last >= 5 && last < 210) { // 氧减的判断条件
																							// //temp[t[0]]
																							// -
																							// temp[t[1]]
																							// >=
																							// 3
																							// &&
																							// temp[t[3]]
																							// -
																							// temp[t[1]]
																							// >=
																							// 3
																							// &&
						if (t[5] == 0) { // 呼吸暂停和低通气共同的判断条件 //t_down >= 2 &&
											// t_up >= 2 &&
							if (temp[t[0]] - temp[t[1]] >= 3 && temp[t[3]] - temp[t[1]] >= 3) { // 呼吸暂停的判断条件
																								// //
																								// &&
																								// t_down
																								// >=
																								// 3
																								// &&
																								// t_up
																								// >=
																								// 3
								temp_duration_AI[count_ai][0] = t[0];
								temp_duration_AI[count_ai][1] = t[1];
								temp_duration_AI[count_ai][2] = t[2];
								temp_duration_AI[count_ai][3] = t[3];
								count_ai++;
							} else {
								temp_duration_HI[count_hi][0] = t[0];
								temp_duration_HI[count_hi][1] = t[1];
								temp_duration_HI[count_hi][2] = t[2];
								temp_duration_HI[count_hi][3] = t[3];
								count_hi++;
							}
						}
						temp_duration_ODI4[count_odi4][0] = t[0];
						temp_duration_ODI4[count_odi4][1] = t[1];
						temp_duration_ODI4[count_odi4][2] = t[2];
						temp_duration_ODI4[count_odi4][3] = t[3];
						temp_duration_ODI4[count_odi4][4] = t[5];

						count_odi4++;
					} else { // if(t[3] - t[0] < 300 && ((t[5] == 0 &&
								// (temp[t[0]] - temp[t[1]] >= 5 || temp[t[3]] -
								// temp[t[1]] >= 5)) || (t[5] > 0 && (temp[t[0]]
								// - temp[t[5]] >= 5 || temp[t[3]] - temp[t[5]]
								// >= 5))))
						temp_duration_noise[count_noise][0] = t[0];
						temp_duration_noise[count_noise][1] = t[1];
						temp_duration_noise[count_noise][2] = t[2];
						temp_duration_noise[count_noise][3] = t[3];
						temp_duration_noise[count_noise][4] = t[5];
						count_noise++;
					}
					// 重置参数，准备检测下个下降的起点
					flag = false;
					t[0] = 0;
					t[1] = 0;
					t[2] = 0;
					t[3] = 0;
					t[4] = 0;
					t[5] = 0;
				}
			} else if (temp[i] > temp[i + 1]) { // && temp[i] > temp_st_ave-5
				t[0] = i;
				t[1] = i + 1;
				t[2] = i + 1;
				t[3] = 0;
				t[4] = 0;
				t[5] = 0;
				flag = true;
			}
		}
		AI_count = count_ai;
		HI_count = count_hi;
		ODI4_count = count_odi4;
	}

	/**
	 * 根据SPO2值，检测呼吸暂停的起止时间并判断呼吸暂停次数(原始版本)
	 * 
	 * @param temp_aft_spo2
	 *            用于检测的SPO2数据
	 */
	public void detection_spo2_D_Y(int[] temp_aft_spo2) {
		int len = temp_aft_spo2.length;
		int[] temp = new int[len]; // 接收SPO2的临时数组
		for (int i = 0; i < len; i++)
			temp[i] = temp_aft_spo2[i];
		temp_duration_AI = new int[len][5]; // 呼吸暂停数据
		temp_duration_HI = new int[len][5]; // 低通气数据
		temp_duration_noise = new int[len][5]; // 噪声数据
		temp_duration_ODI4 = new int[len][6]; // 氧减数据
		int count = 0; // 呼吸暂停起止时间的计数
		int count2 = 0; // 低通气起止时间的计数
		int count3 = 0; // 噪声起止时间的计数
		int count4 = 0; // 氧减起止时间的计数

		int sum = 0;
		for (int i = 0; i < len; i++) {
			sum += temp[i];
		}
		temp_st_ave = sum / len; // temp_st_ave：基准值——————————————取值方式有待商议
//		System.out.println("temp_st_ave：" + temp_st_ave); // 测试输出

		int t_down = 0, t_up = 0; // t_down：血氧下降的时间，t_up：血氧上升的时间
		int spo2_down = 0, spo2_up = 0; // spo2_down：血氧下降的幅度，spo2_up：血氧上升的幅度
		int valley = 0, last = 0; // valley：血氧在最低点维持的时间，last：一次血氧下降的总持续时间
		int[] t = { 0, 0, 0, 0, 0, 0, 0 }; // 检测呼吸暂停所需的一个临时数组
											// t[0]:下降起点，t[1]:下降终点，t[2]:上升起点，t[3]:上升终点，t[4]:上升后的水平移动，t[5]:下降过程中的最低值，t[6]:上升过程中出现小尖角(凸)时用于找回t[3]的值
		boolean flag = false; // 开始检测的标志，true表示检测到第一个点
		int count_horizontal = 0; // 水平线的数据点数量
		for (int i = 0; i < len - 2; i++) {
			if (flag == true) {
				if (temp[i] > temp[i + 1]) { // 检测到下降
					// 下降过程中如果水平线的数据长度大于15，且血氧值与起始点血氧值差距小于4，则将起点调整为水平线末端
					if (count_horizontal > 15 && t[3] == 0 && temp[t[0]] - temp[i] < 4) {
						t[0] = i;
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
					}

					if (t[3] == 0) { // 正常下降
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
						count_horizontal = 0;
					} else { // 异常下降
						if (t[3] == i && temp[i - 1] == temp[i + 1]) { // 出现可忽略的小尖角(凸)
							if (t[2] == t[3] - 1) { // 下降过程中
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
								count_horizontal++;
							} else { // 上升过程中
								if ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
										|| (t[5] > 0
												&& 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))) { // temp[t[0]]-temp[t[3]]
																														// <=
																														// 1
									// 一次下降过程结束
									if (t[5] == 0) {
										t_down = t[2] - t[0];
										t_up = t[3] - t[2];
										spo2_down = temp[t[0]] - temp[t[2]];
										spo2_up = temp[t[3]] - temp[t[2]];
									} else {
										t_down = t[5] - t[0];
										t_up = t[3] - t[5];
										spo2_down = temp[t[0]] - temp[t[5]];
										spo2_up = temp[t[3]] - temp[t[5]];
									}
									valley = t[2] - t[1];
									last = t[3] - t[0];

									if (spo2_down >= 3 && spo2_up >= 3 && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
										if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
											// && spo2_down/t_down <
											// spo2_up/t_up
											if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5 && t_down > t_up) { // 呼吸暂停的判断条件
												temp_duration_AI[count][0] = t[0];
												temp_duration_AI[count][1] = t[1];
												temp_duration_AI[count][2] = t[2];
												temp_duration_AI[count][3] = t[3];
												temp_duration_AI[count][4] = t[5];
												count++;
											} else {
												temp_duration_HI[count2][0] = t[0];
												temp_duration_HI[count2][1] = t[1];
												temp_duration_HI[count2][2] = t[2];
												temp_duration_HI[count2][3] = t[3];
												temp_duration_HI[count2][4] = t[5];
												count2++;
											}
										}
										temp_duration_ODI4[count4][0] = t[0];
										temp_duration_ODI4[count4][1] = t[1];
										temp_duration_ODI4[count4][2] = t[2];
										temp_duration_ODI4[count4][3] = t[3];
										temp_duration_ODI4[count4][4] = t[5];
										if (t[5] > 0) {
											temp_duration_ODI4[count4][5] = temp_aft_spo2[t[1]] < temp_aft_spo2[t[5]]
													? t[1] : t[5];
										} else {
											temp_duration_ODI4[count4][5] = t[1];
										}
										count4++;
									} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
										temp_duration_noise[count3][0] = t[0];
										temp_duration_noise[count3][1] = t[1];
										temp_duration_noise[count3][2] = t[2];
										temp_duration_noise[count3][3] = t[3];
										temp_duration_noise[count3][4] = t[5];
										count3++;
									}
									// 将当前点作为下个下降的起点，继续检测
									t[0] = i;
									t[1] = i + 1;
									t[2] = i + 1;
									t[3] = 0;
									t[4] = 0;
									t[5] = 0;
									t[6] = 0;
									count_horizontal = 0;
								} else {
									if (t[6] != 0) {
										t[3] = t[6];
									} else {
										t[3] = i - 1;
									}
									t[4] = i + 1;
									count_horizontal++;
								}
							}
						} else if (t[4] == i && (temp[i] == temp[i + 2] || temp[t[3]] == temp[t[3] - 2])) { // 出现可忽略的小尖角(凹)
							if (temp[t[3]] == temp[t[3] - 2]) { // 下降过程中
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
								count_horizontal = 0;
							} else { // 上升过程中
								t[4] = i + 2;
								count_horizontal++;
							}
						} else { // 可能出现锯齿，可能氧降结束
							if ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
									|| (t[5] > 0 && 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))) { // temp[t[0]]-temp[t[3]]
																														// <=
																														// 2
								// 一次下降过程结束
								if (t[5] == 0) {
									t_down = t[2] - t[0];
									t_up = t[3] - t[2];
									spo2_down = temp[t[0]] - temp[t[2]];
									spo2_up = temp[t[3]] - temp[t[2]];
								} else {
									t_down = t[5] - t[0];
									t_up = t[3] - t[5];
									spo2_down = temp[t[0]] - temp[t[5]];
									spo2_up = temp[t[3]] - temp[t[5]];
								}
								valley = t[2] - t[1];
								last = t[3] - t[0];

								if (spo2_down >= 3 && spo2_up >= 3 && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
									if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
										// && spo2_down/t_down < spo2_up/t_up
										if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5 && t_down > t_up) { // 呼吸暂停的判断条件
											temp_duration_AI[count][0] = t[0];
											temp_duration_AI[count][1] = t[1];
											temp_duration_AI[count][2] = t[2];
											temp_duration_AI[count][3] = t[3];
											temp_duration_AI[count][4] = t[5];
											count++;
										} else {
											temp_duration_HI[count2][0] = t[0];
											temp_duration_HI[count2][1] = t[1];
											temp_duration_HI[count2][2] = t[2];
											temp_duration_HI[count2][3] = t[3];
											temp_duration_HI[count2][4] = t[5];
											count2++;
										}
									}
									temp_duration_ODI4[count4][0] = t[0];
									temp_duration_ODI4[count4][1] = t[1];
									temp_duration_ODI4[count4][2] = t[2];
									temp_duration_ODI4[count4][3] = t[3];
									temp_duration_ODI4[count4][4] = t[5];
									if (t[5] > 0) {
										temp_duration_ODI4[count4][5] = temp_aft_spo2[t[1]] < temp_aft_spo2[t[5]] ? t[1]
												: t[5];
									} else {
										temp_duration_ODI4[count4][5] = t[1];
									}
									count4++;
								} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
									temp_duration_noise[count3][0] = t[0];
									temp_duration_noise[count3][1] = t[1];
									temp_duration_noise[count3][2] = t[2];
									temp_duration_noise[count3][3] = t[3];
									temp_duration_noise[count3][4] = t[5];
									count3++;
								}
								// 将当前点作为下个下降的起点，继续检测
								t[0] = i;
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
							} else {
								// 出现较大锯齿
								if (t[5] == 0) {
									t[5] = t[2];
								} else {
									t[5] = temp[t[2]] < temp[t[5]] ? t[2] : t[5]; // 将最低点保存在t[5]中
								}
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[6] = 0;
							}
							count_horizontal = 0;
						}
					}
				} else if (temp[i] == temp[i + 1]) { // 检测到水平移动
					if (t[3] == 0) { // 下降中的水平移动
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
					} else if (t[3] > 0) {
						t[4] = i + 1; // 上升中的水平移动
						if (t[6] != t[3])
							t[6] = t[3]; // 临时存储t[3]值，用于找回
					}
					count_horizontal++;
				} else if (temp[i] < temp[i + 1]) { // 检测到上升
					// 如果水平线的数据长度大于15，且血氧值与起始点血氧值差距小于等于2，则判断一次氧降结束
					if ((t[3] != 0 && temp[t[0]] - temp[t[3]] <= 0) || (count_horizontal > 15 && t[3] != 0
							&& ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
									|| (t[5] > 0 && 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))))) { // temp[t[0]]
																															// -
																															// temp[t[3]]
																															// <=
																															// 2
						// 一次下降过程结束
						if (t[5] == 0) {
							t_down = t[2] - t[0];
							t_up = t[3] - t[2];
							spo2_down = temp[t[0]] - temp[t[2]];
							spo2_up = temp[t[3]] - temp[t[2]];
						} else {
							t_down = t[5] - t[0];
							t_up = t[3] - t[5];
							spo2_down = temp[t[0]] - temp[t[5]];
							spo2_up = temp[t[3]] - temp[t[5]];
						}
						valley = t[2] - t[1];
						last = t[3] - t[0];

						if (spo2_down >= 3 && spo2_up >= 3 && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
							if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
								// && spo2_down/t_down < spo2_up/t_up
								if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5 && t_down > t_up) { // 呼吸暂停的判断条件
									temp_duration_AI[count][0] = t[0];
									temp_duration_AI[count][1] = t[1];
									temp_duration_AI[count][2] = t[2];
									temp_duration_AI[count][3] = t[3];
									temp_duration_AI[count][4] = t[5];
									count++;
								} else {
									temp_duration_HI[count2][0] = t[0];
									temp_duration_HI[count2][1] = t[1];
									temp_duration_HI[count2][2] = t[2];
									temp_duration_HI[count2][3] = t[3];
									temp_duration_HI[count2][4] = t[5];
									count2++;
								}
							}
							temp_duration_ODI4[count4][0] = t[0];
							temp_duration_ODI4[count4][1] = t[1];
							temp_duration_ODI4[count4][2] = t[2];
							temp_duration_ODI4[count4][3] = t[3];
							temp_duration_ODI4[count4][4] = t[5];
							if (t[5] > 0) {
								temp_duration_ODI4[count4][5] = temp_aft_spo2[t[1]] < temp_aft_spo2[t[5]] ? t[1] : t[5];
							} else {
								temp_duration_ODI4[count4][5] = t[1];
							}
							count4++;
						} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
							temp_duration_noise[count3][0] = t[0];
							temp_duration_noise[count3][1] = t[1];
							temp_duration_noise[count3][2] = t[2];
							temp_duration_noise[count3][3] = t[3];
							temp_duration_noise[count3][4] = t[5];
							count3++;
						}
						// 重置参数，准备检测下个下降的起点
						flag = false;
						t[0] = 0;
						t[1] = 0;
						t[2] = 0;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
						count_horizontal = 0;
					} else {
						// 正常上升
						if (t[4] == 0 || t[4] != i + 1) {
							count_horizontal = 0;
							t[3] = i + 1;
							t[4] = 0;
							t[6] = 0;
						} else if (t[4] == i + 1) {
							count_horizontal++;
						}
					}
				}

				// 一次下降过程结束，血氧值回升到下降的水平
				if (temp[t[0]] - temp[t[3]] <= 0 && t[3] != 0) {
					// 一次下降过程结束
					if (t[5] == 0) {
						t_down = t[2] - t[0];
						t_up = t[3] - t[2];
						spo2_down = temp[t[0]] - temp[t[2]];
						spo2_up = temp[t[3]] - temp[t[2]];
					} else {
						t_down = t[5] - t[0];
						t_up = t[3] - t[5];
						spo2_down = temp[t[0]] - temp[t[5]];
						spo2_up = temp[t[3]] - temp[t[5]];
					}
					valley = t[2] - t[1];
					last = t[3] - t[0];

					if (spo2_down >= 3 && spo2_up >= 3 && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
						if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
							// && spo2_down/t_down < spo2_up/t_up
							if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5 && t_down > t_up) { // 呼吸暂停的判断条件
								temp_duration_AI[count][0] = t[0];
								temp_duration_AI[count][1] = t[1];
								temp_duration_AI[count][2] = t[2];
								temp_duration_AI[count][3] = t[3];
								temp_duration_AI[count][4] = t[5];
								count++;
							} else {
								temp_duration_HI[count2][0] = t[0];
								temp_duration_HI[count2][1] = t[1];
								temp_duration_HI[count2][2] = t[2];
								temp_duration_HI[count2][3] = t[3];
								temp_duration_HI[count2][4] = t[5];
								count2++;
							}
						}
						temp_duration_ODI4[count4][0] = t[0];
						temp_duration_ODI4[count4][1] = t[1];
						temp_duration_ODI4[count4][2] = t[2];
						temp_duration_ODI4[count4][3] = t[3];
						temp_duration_ODI4[count4][4] = t[5];
						if (t[5] > 0) {
							temp_duration_ODI4[count4][5] = temp_aft_spo2[t[1]] < temp_aft_spo2[t[5]] ? t[1] : t[5];
						} else {
							temp_duration_ODI4[count4][5] = t[1];
						}
						count4++;
					} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
						temp_duration_noise[count3][0] = t[0];
						temp_duration_noise[count3][1] = t[1];
						temp_duration_noise[count3][2] = t[2];
						temp_duration_noise[count3][3] = t[3];
						temp_duration_noise[count3][4] = t[5];
						count3++;
					}
					// 重置参数，准备检测下个下降的起点
					flag = false;
					t[0] = 0;
					t[1] = 0;
					t[2] = 0;
					t[3] = 0;
					t[4] = 0;
					t[5] = 0;
					t[6] = 0;
				}
			} else if (temp[i] > temp[i + 1] && temp[i] > temp_st_ave - 10) { //
				t[0] = i;
				t[1] = i + 1;
				t[2] = i + 1;
				t[3] = 0;
				t[4] = 0;
				t[5] = 0;
				t[6] = 0;
				flag = true;
			}
		}
		AI_count = count;
		HI_count = count2;
		ODI4_count = count4;
	}

	/**
	 * 根据SPO2值，检测呼吸暂停的起止时间并判断呼吸暂停次数(原始版本)
	 * 
	 * @param temp_aft_spo2
	 *            用于检测的SPO2数据
	 */
	public void detection_spo2_D_N(int[] temp_aft_spo2) {
		int len = temp_aft_spo2.length;
		int[] temp = new int[len]; // 接收SPO2的临时数组
		for (int i = 0; i < len; i++)
			temp[i] = temp_aft_spo2[i];
		temp_duration_AI = new int[len][5]; // 呼吸暂停数据
		temp_duration_HI = new int[len][5]; // 低通气数据
		temp_duration_noise = new int[len][5]; // 噪声数据
		temp_duration_ODI4 = new int[len][5]; // 氧减数据
		int count = 0; // 呼吸暂停起止时间的计数
		int count2 = 0; // 低通气起止时间的计数
		int count3 = 0; // 噪声起止时间的计数
		int count4 = 0; // 氧减起止时间的计数

		int sum = 0;
		for (int i = 0; i < len; i++) {
			sum += temp[i];
		}
		temp_st_ave = sum / len; // temp_st_ave：基准值——————————————取值方式有待商议
//		System.out.println("temp_st_ave：" + temp_st_ave); // 测试输出

		int t_down = 0, t_up = 0; // t_down：血氧下降的时间，t_up：血氧上升的时间
		int spo2_down = 0, spo2_up = 0; // spo2_down：血氧下降的幅度，spo2_up：血氧上升的幅度
		int valley = 0, last = 0; // valley：血氧在最低点维持的时间，last：一次血氧下降的总持续时间
		int[] t = { 0, 0, 0, 0, 0, 0, 0 }; // 检测呼吸暂停所需的一个临时数组
											// t[0]:下降起点，t[1]:下降终点，t[2]:上升起点，t[3]:上升终点，t[4]:上升后的水平移动，t[5]:下降过程中的最低值，t[6]:上升过程中出现小尖角(凸)时用于找回t[3]的值
		boolean flag = false; // 开始检测的标志，true表示检测到第一个点
		int count_horizontal = 0; // 水平线的数据点数量
		for (int i = 0; i < len - 2; i++) {
			if (flag == true) {
				if (temp[i] > temp[i + 1]) { // 检测到下降
					// 下降过程中如果水平线的数据长度大于15，且血氧值与起始点血氧值差距小于4，则将起点调整为水平线末端
					if (count_horizontal > 15 && t[3] == 0 && temp[t[0]] - temp[i] < 4) {
						t[0] = i;
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
					}

					if (t[3] == 0) { // 正常下降
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
						count_horizontal = 0;
					} else { // 异常下降
						if (t[3] == i && temp[i - 1] == temp[i + 1]) { // 出现可忽略的小尖角(凸)
							if (t[2] == t[3] - 1) { // 下降过程中
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
								count_horizontal++;
							} else { // 上升过程中
								if ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
										|| (t[5] > 0
												&& 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))) { // temp[t[0]]-temp[t[3]]
																														// <=
																														// 1
									// 一次下降过程结束
									if (t[5] == 0) {
										t_down = t[2] - t[0];
										t_up = t[3] - t[2];
										spo2_down = temp[t[0]] - temp[t[2]];
										spo2_up = temp[t[3]] - temp[t[2]];
									} else {
										t_down = t[5] - t[0];
										t_up = t[3] - t[5];
										spo2_down = temp[t[0]] - temp[t[5]];
										spo2_up = temp[t[3]] - temp[t[5]];
									}
									valley = t[2] - t[1];
									last = t[3] - t[0];

									if (spo2_down >= 3 && spo2_up >= 3 && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
										if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
											// && spo2_down/t_down <
											// spo2_up/t_up && t_down > t_up
											if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5) { // 呼吸暂停的判断条件
												temp_duration_AI[count][0] = t[0];
												temp_duration_AI[count][1] = t[1];
												temp_duration_AI[count][2] = t[2];
												temp_duration_AI[count][3] = t[3];
												count++;
											} else {
												temp_duration_HI[count2][0] = t[0];
												temp_duration_HI[count2][1] = t[1];
												temp_duration_HI[count2][2] = t[2];
												temp_duration_HI[count2][3] = t[3];
												count2++;
											}
										}
										temp_duration_ODI4[count4][0] = t[0];
										temp_duration_ODI4[count4][1] = t[1];
										temp_duration_ODI4[count4][2] = t[2];
										temp_duration_ODI4[count4][3] = t[3];
										temp_duration_ODI4[count4][4] = t[5];
										count4++;
									} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
										temp_duration_noise[count3][0] = t[0];
										temp_duration_noise[count3][1] = t[1];
										temp_duration_noise[count3][2] = t[2];
										temp_duration_noise[count3][3] = t[3];
										temp_duration_noise[count3][4] = t[5];
										count3++;
									}
									// 将当前点作为下个下降的起点，继续检测
									t[0] = i;
									t[1] = i + 1;
									t[2] = i + 1;
									t[3] = 0;
									t[4] = 0;
									t[5] = 0;
									t[6] = 0;
									count_horizontal = 0;
								} else {
									if (t[6] != 0) {
										t[3] = t[6];
									} else {
										t[3] = i - 1;
									}
									t[4] = i + 1;
									count_horizontal++;
								}
							}
						} else if (t[4] == i && (temp[i] == temp[i + 2] || temp[t[3]] == temp[t[3] - 2])) { // 出现可忽略的小尖角(凹)
							if (temp[t[3]] == temp[t[3] - 2]) { // 下降过程中
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
								count_horizontal = 0;
							} else { // 上升过程中
								t[4] = i + 2;
								count_horizontal++;
							}
						} else { // 可能出现锯齿，可能氧降结束
							if ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
									|| (t[5] > 0 && 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))) { // temp[t[0]]-temp[t[3]]
																														// <=
																														// 2
								// 一次下降过程结束
								if (t[5] == 0) {
									t_down = t[2] - t[0];
									t_up = t[3] - t[2];
									spo2_down = temp[t[0]] - temp[t[2]];
									spo2_up = temp[t[3]] - temp[t[2]];
								} else {
									t_down = t[5] - t[0];
									t_up = t[3] - t[5];
									spo2_down = temp[t[0]] - temp[t[5]];
									spo2_up = temp[t[3]] - temp[t[5]];
								}
								valley = t[2] - t[1];
								last = t[3] - t[0];

								if (spo2_down >= 3 && spo2_up >= 3 && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
									if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
										// && spo2_down/t_down < spo2_up/t_up &&
										// t_down > t_up
										if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5) { // 呼吸暂停的判断条件
											temp_duration_AI[count][0] = t[0];
											temp_duration_AI[count][1] = t[1];
											temp_duration_AI[count][2] = t[2];
											temp_duration_AI[count][3] = t[3];
											count++;
										} else {
											temp_duration_HI[count2][0] = t[0];
											temp_duration_HI[count2][1] = t[1];
											temp_duration_HI[count2][2] = t[2];
											temp_duration_HI[count2][3] = t[3];
											count2++;
										}
									}
									temp_duration_ODI4[count4][0] = t[0];
									temp_duration_ODI4[count4][1] = t[1];
									temp_duration_ODI4[count4][2] = t[2];
									temp_duration_ODI4[count4][3] = t[3];
									temp_duration_ODI4[count4][4] = t[5];
									count4++;
								} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
									temp_duration_noise[count3][0] = t[0];
									temp_duration_noise[count3][1] = t[1];
									temp_duration_noise[count3][2] = t[2];
									temp_duration_noise[count3][3] = t[3];
									temp_duration_noise[count3][4] = t[5];
									count3++;
								}
								// 将当前点作为下个下降的起点，继续检测
								t[0] = i;
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
							} else {
								// 出现较大锯齿
								if (t[5] == 0) {
									t[5] = t[2];
								} else {
									t[5] = temp[t[2]] < temp[t[5]] ? t[2] : t[5]; // 将最低点保存在t[5]中
								}
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[6] = 0;
							}
							count_horizontal = 0;
						}
					}
				} else if (temp[i] == temp[i + 1]) { // 检测到水平移动
					if (t[3] == 0) { // 下降中的水平移动
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
					} else if (t[3] > 0) {
						t[4] = i + 1; // 上升中的水平移动
						if (t[6] != t[3])
							t[6] = t[3]; // 临时存储t[3]值，用于找回
					}
					count_horizontal++;
				} else if (temp[i] < temp[i + 1]) { // 检测到上升
					// 如果水平线的数据长度大于15，且血氧值与起始点血氧值差距小于等于2，则判断一次氧降结束
					if ((t[3] != 0 && temp[t[0]] - temp[t[3]] <= 0) || (count_horizontal > 15 && t[3] != 0
							&& ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
									|| (t[5] > 0 && 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))))) { // temp[t[0]]
																															// -
																															// temp[t[3]]
																															// <=
																															// 2
						// 一次下降过程结束
						if (t[5] == 0) {
							t_down = t[2] - t[0];
							t_up = t[3] - t[2];
							spo2_down = temp[t[0]] - temp[t[2]];
							spo2_up = temp[t[3]] - temp[t[2]];
						} else {
							t_down = t[5] - t[0];
							t_up = t[3] - t[5];
							spo2_down = temp[t[0]] - temp[t[5]];
							spo2_up = temp[t[3]] - temp[t[5]];
						}
						valley = t[2] - t[1];
						last = t[3] - t[0];

						if (spo2_down >= 3 && spo2_up >= 3 && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
							if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
								// && spo2_down/t_down < spo2_up/t_up && t_down
								// > t_up
								if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5) { // 呼吸暂停的判断条件
									temp_duration_AI[count][0] = t[0];
									temp_duration_AI[count][1] = t[1];
									temp_duration_AI[count][2] = t[2];
									temp_duration_AI[count][3] = t[3];
									count++;
								} else {
									temp_duration_HI[count2][0] = t[0];
									temp_duration_HI[count2][1] = t[1];
									temp_duration_HI[count2][2] = t[2];
									temp_duration_HI[count2][3] = t[3];
									count2++;
								}
							}
							temp_duration_ODI4[count4][0] = t[0];
							temp_duration_ODI4[count4][1] = t[1];
							temp_duration_ODI4[count4][2] = t[2];
							temp_duration_ODI4[count4][3] = t[3];
							temp_duration_ODI4[count4][4] = t[5];
							count4++;
						} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
							temp_duration_noise[count3][0] = t[0];
							temp_duration_noise[count3][1] = t[1];
							temp_duration_noise[count3][2] = t[2];
							temp_duration_noise[count3][3] = t[3];
							temp_duration_noise[count3][4] = t[5];
							count3++;
						}
						// 重置参数，准备检测下个下降的起点
						flag = false;
						t[0] = 0;
						t[1] = 0;
						t[2] = 0;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
						count_horizontal = 0;
					} else {
						// 正常上升
						if (t[4] == 0 || t[4] != i + 1) {
							count_horizontal = 0;
							t[3] = i + 1;
							t[4] = 0;
							t[6] = 0;
						} else if (t[4] == i + 1) {
							count_horizontal++;
						}
					}
				}

				// 一次下降过程结束，血氧值回升到下降的水平
				if (temp[t[0]] - temp[t[3]] <= 0 && t[3] != 0) {
					// 一次下降过程结束
					if (t[5] == 0) {
						t_down = t[2] - t[0];
						t_up = t[3] - t[2];
						spo2_down = temp[t[0]] - temp[t[2]];
						spo2_up = temp[t[3]] - temp[t[2]];
					} else {
						t_down = t[5] - t[0];
						t_up = t[3] - t[5];
						spo2_down = temp[t[0]] - temp[t[5]];
						spo2_up = temp[t[3]] - temp[t[5]];
					}
					valley = t[2] - t[1];
					last = t[3] - t[0];

					if (spo2_down >= 3 && spo2_up >= 3 && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
						if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
							// && spo2_down/t_down < spo2_up/t_up && t_down >
							// t_up
							if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5) { // 呼吸暂停的判断条件
								temp_duration_AI[count][0] = t[0];
								temp_duration_AI[count][1] = t[1];
								temp_duration_AI[count][2] = t[2];
								temp_duration_AI[count][3] = t[3];
								count++;
							} else {
								temp_duration_HI[count2][0] = t[0];
								temp_duration_HI[count2][1] = t[1];
								temp_duration_HI[count2][2] = t[2];
								temp_duration_HI[count2][3] = t[3];
								count2++;
							}
						}
						temp_duration_ODI4[count4][0] = t[0];
						temp_duration_ODI4[count4][1] = t[1];
						temp_duration_ODI4[count4][2] = t[2];
						temp_duration_ODI4[count4][3] = t[3];
						temp_duration_ODI4[count4][4] = t[5];
						count4++;
					} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
						temp_duration_noise[count3][0] = t[0];
						temp_duration_noise[count3][1] = t[1];
						temp_duration_noise[count3][2] = t[2];
						temp_duration_noise[count3][3] = t[3];
						temp_duration_noise[count3][4] = t[5];
						count3++;
					}
					// 重置参数，准备检测下个下降的起点
					flag = false;
					t[0] = 0;
					t[1] = 0;
					t[2] = 0;
					t[3] = 0;
					t[4] = 0;
					t[5] = 0;
					t[6] = 0;
				}
			} else if (temp[i] > temp[i + 1] && temp[i] > temp_st_ave - 10) { //
				t[0] = i;
				t[1] = i + 1;
				t[2] = i + 1;
				t[3] = 0;
				t[4] = 0;
				t[5] = 0;
				t[6] = 0;
				flag = true;
			}
		}
		AI_count = count;
		HI_count = count2;
		ODI4_count = count4;
	}

	/**
	 * 根据SPO2值，检测呼吸暂停的起止时间并判断呼吸暂停次数(原始版本)
	 * 
	 * @param temp_aft_spo2
	 *            用于检测的SPO2数据
	 */
	public void detection_spo2_C_Y(int[] temp_aft_spo2) {
		int len = temp_aft_spo2.length;
		int[] temp = new int[len]; // 接收SPO2的临时数组
		for (int i = 0; i < len; i++)
			temp[i] = temp_aft_spo2[i];
		temp_duration_AI = new int[len][5]; // 呼吸暂停数据
		temp_duration_HI = new int[len][5]; // 低通气数据
		temp_duration_noise = new int[len][5]; // 噪声数据
		temp_duration_ODI4 = new int[len][5]; // 氧减数据
		int count = 0; // 呼吸暂停起止时间的计数
		int count2 = 0; // 低通气起止时间的计数
		int count3 = 0; // 噪声起止时间的计数
		int count4 = 0; // 氧减起止时间的计数

		int sum = 0;
		for (int i = 0; i < len; i++) {
			sum += temp[i];
		}
		temp_st_ave = sum / len; // temp_st_ave：基准值——————————————取值方式有待商议
//		System.out.println("temp_st_ave：" + temp_st_ave); // 测试输出

		int t_down = 0, t_up = 0; // t_down：血氧下降的时间，t_up：血氧上升的时间
		int spo2_down = 0, spo2_up = 0; // spo2_down：血氧下降的幅度，spo2_up：血氧上升的幅度
		int valley = 0, last = 0; // valley：血氧在最低点维持的时间，last：一次血氧下降的总持续时间
		int[] t = { 0, 0, 0, 0, 0, 0, 0 }; // 检测呼吸暂停所需的一个临时数组
											// t[0]:下降起点，t[1]:下降终点，t[2]:上升起点，t[3]:上升终点，t[4]:上升后的水平移动，t[5]:下降过程中的最低值，t[6]:上升过程中出现小尖角(凸)时用于找回t[3]的值
		boolean flag = false; // 开始检测的标志，true表示检测到第一个点
		int count_horizontal = 0; // 水平线的数据点数量
		for (int i = 0; i < len - 2; i++) {
			if (flag == true) {
				if (temp[i] > temp[i + 1]) { // 检测到下降
					// 下降过程中如果水平线的数据长度大于15，且血氧值与起始点血氧值差距小于4，则将起点调整为水平线末端
					if (count_horizontal > 15 && t[3] == 0 && temp[t[0]] - temp[i] < 4) {
						t[0] = i;
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
					}

					if (t[3] == 0) { // 正常下降
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
						count_horizontal = 0;
					} else { // 异常下降
						if (t[3] == i && temp[i - 1] == temp[i + 1]) { // 出现可忽略的小尖角(凸)
							if (t[2] == t[3] - 1) { // 下降过程中
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
								count_horizontal++;
							} else { // 上升过程中
								if ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
										|| (t[5] > 0
												&& 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))) { // temp[t[0]]-temp[t[3]]
																														// <=
																														// 1
									// 一次下降过程结束
									if (t[5] == 0) {
										t_down = t[2] - t[0];
										t_up = t[3] - t[2];
										spo2_down = temp[t[0]] - temp[t[2]];
										spo2_up = temp[t[3]] - temp[t[2]];
									} else {
										t_down = t[5] - t[0];
										t_up = t[3] - t[5];
										spo2_down = temp[t[0]] - temp[t[5]];
										spo2_up = temp[t[3]] - temp[t[5]];
									}
									valley = t[2] - t[1];
									last = t[3] - t[0];

									if (((t[5] > 0 && temp_st_ave - temp[t[5]] >= 3)
											|| (t[5] == 0 && temp_st_ave - temp[t[2]] >= 3)) && last >= 7
											&& last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
										if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
											// && spo2_down/t_down <
											// spo2_up/t_up
											if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5 && t_down > t_up) { // 呼吸暂停的判断条件
												temp_duration_AI[count][0] = t[0];
												temp_duration_AI[count][1] = t[1];
												temp_duration_AI[count][2] = t[2];
												temp_duration_AI[count][3] = t[3];
												count++;
											} else {
												temp_duration_HI[count2][0] = t[0];
												temp_duration_HI[count2][1] = t[1];
												temp_duration_HI[count2][2] = t[2];
												temp_duration_HI[count2][3] = t[3];
												count2++;
											}
										}
										temp_duration_ODI4[count4][0] = t[0];
										temp_duration_ODI4[count4][1] = t[1];
										temp_duration_ODI4[count4][2] = t[2];
										temp_duration_ODI4[count4][3] = t[3];
										temp_duration_ODI4[count4][4] = t[5];
										count4++;
									} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
										temp_duration_noise[count3][0] = t[0];
										temp_duration_noise[count3][1] = t[1];
										temp_duration_noise[count3][2] = t[2];
										temp_duration_noise[count3][3] = t[3];
										temp_duration_noise[count3][4] = t[5];
										count3++;
									}
									// 将当前点作为下个下降的起点，继续检测
									t[0] = i;
									t[1] = i + 1;
									t[2] = i + 1;
									t[3] = 0;
									t[4] = 0;
									t[5] = 0;
									t[6] = 0;
									count_horizontal = 0;
								} else {
									if (t[6] != 0) {
										t[3] = t[6];
									} else {
										t[3] = i - 1;
									}
									t[4] = i + 1;
									count_horizontal++;
								}
							}
						} else if (t[4] == i && (temp[i] == temp[i + 2] || temp[t[3]] == temp[t[3] - 2])) { // 出现可忽略的小尖角(凹)
							if (temp[t[3]] == temp[t[3] - 2]) { // 下降过程中
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
								count_horizontal = 0;
							} else { // 上升过程中
								t[4] = i + 2;
								count_horizontal++;
							}
						} else { // 可能出现锯齿，可能氧降结束
							if ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
									|| (t[5] > 0 && 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))) { // temp[t[0]]-temp[t[3]]
																														// <=
																														// 2
								// 一次下降过程结束
								if (t[5] == 0) {
									t_down = t[2] - t[0];
									t_up = t[3] - t[2];
									spo2_down = temp[t[0]] - temp[t[2]];
									spo2_up = temp[t[3]] - temp[t[2]];
								} else {
									t_down = t[5] - t[0];
									t_up = t[3] - t[5];
									spo2_down = temp[t[0]] - temp[t[5]];
									spo2_up = temp[t[3]] - temp[t[5]];
								}
								valley = t[2] - t[1];
								last = t[3] - t[0];

								if (((t[5] > 0 && temp_st_ave - temp[t[5]] >= 3)
										|| (t[5] == 0 && temp_st_ave - temp[t[2]] >= 3)) && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
									if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
										// && spo2_down/t_down < spo2_up/t_up
										if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5 && t_down > t_up) { // 呼吸暂停的判断条件
											temp_duration_AI[count][0] = t[0];
											temp_duration_AI[count][1] = t[1];
											temp_duration_AI[count][2] = t[2];
											temp_duration_AI[count][3] = t[3];
											count++;
										} else {
											temp_duration_HI[count2][0] = t[0];
											temp_duration_HI[count2][1] = t[1];
											temp_duration_HI[count2][2] = t[2];
											temp_duration_HI[count2][3] = t[3];
											count2++;
										}
									}
									temp_duration_ODI4[count4][0] = t[0];
									temp_duration_ODI4[count4][1] = t[1];
									temp_duration_ODI4[count4][2] = t[2];
									temp_duration_ODI4[count4][3] = t[3];
									temp_duration_ODI4[count4][4] = t[5];
									count4++;
								} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
									temp_duration_noise[count3][0] = t[0];
									temp_duration_noise[count3][1] = t[1];
									temp_duration_noise[count3][2] = t[2];
									temp_duration_noise[count3][3] = t[3];
									temp_duration_noise[count3][4] = t[5];
									count3++;
								}
								// 将当前点作为下个下降的起点，继续检测
								t[0] = i;
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
							} else {
								// 出现较大锯齿
								if (t[5] == 0) {
									t[5] = t[2];
								} else {
									t[5] = temp[t[2]] < temp[t[5]] ? t[2] : t[5]; // 将最低点保存在t[5]中
								}
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[6] = 0;
							}
							count_horizontal = 0;
						}
					}
				} else if (temp[i] == temp[i + 1]) { // 检测到水平移动
					if (t[3] == 0) { // 下降中的水平移动
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
					} else if (t[3] > 0) {
						t[4] = i + 1; // 上升中的水平移动
						if (t[6] != t[3])
							t[6] = t[3]; // 临时存储t[3]值，用于找回
					}
					count_horizontal++;
				} else if (temp[i] < temp[i + 1]) { // 检测到上升
					// 如果水平线的数据长度大于15，且血氧值与起始点血氧值差距小于等于2，则判断一次氧降结束
					if ((t[3] != 0 && temp[t[0]] - temp[t[3]] <= 0) || (count_horizontal > 15 && t[3] != 0
							&& ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
									|| (t[5] > 0 && 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))))) { // temp[t[0]]
																															// -
																															// temp[t[3]]
																															// <=
																															// 2
						// 一次下降过程结束
						if (t[5] == 0) {
							t_down = t[2] - t[0];
							t_up = t[3] - t[2];
							spo2_down = temp[t[0]] - temp[t[2]];
							spo2_up = temp[t[3]] - temp[t[2]];
						} else {
							t_down = t[5] - t[0];
							t_up = t[3] - t[5];
							spo2_down = temp[t[0]] - temp[t[5]];
							spo2_up = temp[t[3]] - temp[t[5]];
						}
						valley = t[2] - t[1];
						last = t[3] - t[0];

						if (((t[5] > 0 && temp_st_ave - temp[t[5]] >= 3)
								|| (t[5] == 0 && temp_st_ave - temp[t[2]] >= 3)) && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
							if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
								// && spo2_down/t_down < spo2_up/t_up
								if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5 && t_down > t_up) { // 呼吸暂停的判断条件
									temp_duration_AI[count][0] = t[0];
									temp_duration_AI[count][1] = t[1];
									temp_duration_AI[count][2] = t[2];
									temp_duration_AI[count][3] = t[3];
									count++;
								} else {
									temp_duration_HI[count2][0] = t[0];
									temp_duration_HI[count2][1] = t[1];
									temp_duration_HI[count2][2] = t[2];
									temp_duration_HI[count2][3] = t[3];
									count2++;
								}
							}
							temp_duration_ODI4[count4][0] = t[0];
							temp_duration_ODI4[count4][1] = t[1];
							temp_duration_ODI4[count4][2] = t[2];
							temp_duration_ODI4[count4][3] = t[3];
							temp_duration_ODI4[count4][4] = t[5];
							count4++;
						} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
							temp_duration_noise[count3][0] = t[0];
							temp_duration_noise[count3][1] = t[1];
							temp_duration_noise[count3][2] = t[2];
							temp_duration_noise[count3][3] = t[3];
							temp_duration_noise[count3][4] = t[5];
							count3++;
						}
						// 重置参数，准备检测下个下降的起点
						flag = false;
						t[0] = 0;
						t[1] = 0;
						t[2] = 0;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
						count_horizontal = 0;
					} else {
						// 正常上升
						if (t[4] == 0 || t[4] != i + 1) {
							count_horizontal = 0;
							t[3] = i + 1;
							t[4] = 0;
							t[6] = 0;
						} else if (t[4] == i + 1) {
							count_horizontal++;
						}
					}
				}

				// 一次下降过程结束，血氧值回升到下降的水平
				if (temp[t[0]] - temp[t[3]] <= 0 && t[3] != 0) {
					// 一次下降过程结束
					if (t[5] == 0) {
						t_down = t[2] - t[0];
						t_up = t[3] - t[2];
						spo2_down = temp[t[0]] - temp[t[2]];
						spo2_up = temp[t[3]] - temp[t[2]];
					} else {
						t_down = t[5] - t[0];
						t_up = t[3] - t[5];
						spo2_down = temp[t[0]] - temp[t[5]];
						spo2_up = temp[t[3]] - temp[t[5]];
					}
					valley = t[2] - t[1];
					last = t[3] - t[0];

					if (((t[5] > 0 && temp_st_ave - temp[t[5]] >= 3) || (t[5] == 0 && temp_st_ave - temp[t[2]] >= 3))
							&& last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
						if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
							// && spo2_down/t_down < spo2_up/t_up
							if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5 && t_down > t_up) { // 呼吸暂停的判断条件
								temp_duration_AI[count][0] = t[0];
								temp_duration_AI[count][1] = t[1];
								temp_duration_AI[count][2] = t[2];
								temp_duration_AI[count][3] = t[3];
								count++;
							} else {
								temp_duration_HI[count2][0] = t[0];
								temp_duration_HI[count2][1] = t[1];
								temp_duration_HI[count2][2] = t[2];
								temp_duration_HI[count2][3] = t[3];
								count2++;
							}
						}
						temp_duration_ODI4[count4][0] = t[0];
						temp_duration_ODI4[count4][1] = t[1];
						temp_duration_ODI4[count4][2] = t[2];
						temp_duration_ODI4[count4][3] = t[3];
						temp_duration_ODI4[count4][4] = t[5];
						count4++;
					} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
						temp_duration_noise[count3][0] = t[0];
						temp_duration_noise[count3][1] = t[1];
						temp_duration_noise[count3][2] = t[2];
						temp_duration_noise[count3][3] = t[3];
						temp_duration_noise[count3][4] = t[5];
						count3++;
					}
					// 重置参数，准备检测下个下降的起点
					flag = false;
					t[0] = 0;
					t[1] = 0;
					t[2] = 0;
					t[3] = 0;
					t[4] = 0;
					t[5] = 0;
					t[6] = 0;
				}
			} else if (temp[i] > temp[i + 1] && temp[i] > temp_st_ave - 10) { //
				t[0] = i;
				t[1] = i + 1;
				t[2] = i + 1;
				t[3] = 0;
				t[4] = 0;
				t[5] = 0;
				t[6] = 0;
				flag = true;
			}
		}
		AI_count = count;
		HI_count = count2;
		ODI4_count = count4;
	}

	/**
	 * 根据SPO2值，检测呼吸暂停的起止时间并判断呼吸暂停次数(原始版本)
	 * 
	 * @param temp_aft_spo2
	 *            用于检测的SPO2数据
	 */
	public void detection_spo2_C_N(int[] temp_aft_spo2) {
		int len = temp_aft_spo2.length;
		int[] temp = new int[len]; // 接收SPO2的临时数组
		for (int i = 0; i < len; i++)
			temp[i] = temp_aft_spo2[i];
		temp_duration_AI = new int[len][5]; // 呼吸暂停数据
		temp_duration_HI = new int[len][5]; // 低通气数据
		temp_duration_noise = new int[len][5]; // 噪声数据
		temp_duration_ODI4 = new int[len][5]; // 氧减数据
		int count = 0; // 呼吸暂停起止时间的计数
		int count2 = 0; // 低通气起止时间的计数
		int count3 = 0; // 噪声起止时间的计数
		int count4 = 0; // 氧减起止时间的计数

		int sum = 0;
		for (int i = 0; i < len; i++) {
			sum += temp[i];
		}
		temp_st_ave = sum / len; // temp_st_ave：基准值——————————————取值方式有待商议
//		System.out.println("temp_st_ave：" + temp_st_ave); // 测试输出

		int t_down = 0, t_up = 0; // t_down：血氧下降的时间，t_up：血氧上升的时间
		int spo2_down = 0, spo2_up = 0; // spo2_down：血氧下降的幅度，spo2_up：血氧上升的幅度
		int valley = 0, last = 0; // valley：血氧在最低点维持的时间，last：一次血氧下降的总持续时间
		int[] t = { 0, 0, 0, 0, 0, 0, 0 }; // 检测呼吸暂停所需的一个临时数组
											// t[0]:下降起点，t[1]:下降终点，t[2]:上升起点，t[3]:上升终点，t[4]:上升后的水平移动，t[5]:下降过程中的最低值，t[6]:上升过程中出现小尖角(凸)时用于找回t[3]的值
		boolean flag = false; // 开始检测的标志，true表示检测到第一个点
		int count_horizontal = 0; // 水平线的数据点数量
		for (int i = 0; i < len - 2; i++) {
			if (flag == true) {
				if (temp[i] > temp[i + 1]) { // 检测到下降
					// 下降过程中如果水平线的数据长度大于15，且血氧值与起始点血氧值差距小于4，则将起点调整为水平线末端
					if (count_horizontal > 15 && t[3] == 0 && temp[t[0]] - temp[i] < 4) {
						t[0] = i;
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
					}

					if (t[3] == 0) { // 正常下降
						t[1] = i + 1;
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
						count_horizontal = 0;
					} else { // 异常下降
						if (t[3] == i && temp[i - 1] == temp[i + 1]) { // 出现可忽略的小尖角(凸)
							if (t[2] == t[3] - 1) { // 下降过程中
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
								count_horizontal++;
							} else { // 上升过程中
								if ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
										|| (t[5] > 0
												&& 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))) { // temp[t[0]]-temp[t[3]]
																														// <=
																														// 1
									// 一次下降过程结束
									if (t[5] == 0) {
										t_down = t[2] - t[0];
										t_up = t[3] - t[2];
										spo2_down = temp[t[0]] - temp[t[2]];
										spo2_up = temp[t[3]] - temp[t[2]];
									} else {
										t_down = t[5] - t[0];
										t_up = t[3] - t[5];
										spo2_down = temp[t[0]] - temp[t[5]];
										spo2_up = temp[t[3]] - temp[t[5]];
									}
									valley = t[2] - t[1];
									last = t[3] - t[0];

									if (((t[5] > 0 && temp_st_ave - temp[t[5]] >= 3)
											|| (t[5] == 0 && temp_st_ave - temp[t[2]] >= 3)) && last >= 7
											&& last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
										if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
											// && spo2_down/t_down <
											// spo2_up/t_up && t_down > t_up
											if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5) { // 呼吸暂停的判断条件
												temp_duration_AI[count][0] = t[0];
												temp_duration_AI[count][1] = t[1];
												temp_duration_AI[count][2] = t[2];
												temp_duration_AI[count][3] = t[3];
												count++;
											} else {
												temp_duration_HI[count2][0] = t[0];
												temp_duration_HI[count2][1] = t[1];
												temp_duration_HI[count2][2] = t[2];
												temp_duration_HI[count2][3] = t[3];
												count2++;
											}
										}
										temp_duration_ODI4[count4][0] = t[0];
										temp_duration_ODI4[count4][1] = t[1];
										temp_duration_ODI4[count4][2] = t[2];
										temp_duration_ODI4[count4][3] = t[3];
										temp_duration_ODI4[count4][4] = t[5];
										count4++;
									} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
										temp_duration_noise[count3][0] = t[0];
										temp_duration_noise[count3][1] = t[1];
										temp_duration_noise[count3][2] = t[2];
										temp_duration_noise[count3][3] = t[3];
										temp_duration_noise[count3][4] = t[5];
										count3++;
									}
									// 将当前点作为下个下降的起点，继续检测
									t[0] = i;
									t[1] = i + 1;
									t[2] = i + 1;
									t[3] = 0;
									t[4] = 0;
									t[5] = 0;
									t[6] = 0;
									count_horizontal = 0;
								} else {
									if (t[6] != 0) {
										t[3] = t[6];
									} else {
										t[3] = i - 1;
									}
									t[4] = i + 1;
									count_horizontal++;
								}
							}
						} else if (t[4] == i && (temp[i] == temp[i + 2] || temp[t[3]] == temp[t[3] - 2])) { // 出现可忽略的小尖角(凹)
							if (temp[t[3]] == temp[t[3] - 2]) { // 下降过程中
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
								count_horizontal = 0;
							} else { // 上升过程中
								t[4] = i + 2;
								count_horizontal++;
							}
						} else { // 可能出现锯齿，可能氧降结束
							if ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
									|| (t[5] > 0 && 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))) { // temp[t[0]]-temp[t[3]]
																														// <=
																														// 2
								// 一次下降过程结束
								if (t[5] == 0) {
									t_down = t[2] - t[0];
									t_up = t[3] - t[2];
									spo2_down = temp[t[0]] - temp[t[2]];
									spo2_up = temp[t[3]] - temp[t[2]];
								} else {
									t_down = t[5] - t[0];
									t_up = t[3] - t[5];
									spo2_down = temp[t[0]] - temp[t[5]];
									spo2_up = temp[t[3]] - temp[t[5]];
								}
								valley = t[2] - t[1];
								last = t[3] - t[0];

								if (((t[5] > 0 && temp_st_ave - temp[t[5]] >= 3)
										|| (t[5] == 0 && temp_st_ave - temp[t[2]] >= 3)) && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
									if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
										// && spo2_down/t_down < spo2_up/t_up &&
										// t_down > t_up
										if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5) { // 呼吸暂停的判断条件
											temp_duration_AI[count][0] = t[0];
											temp_duration_AI[count][1] = t[1];
											temp_duration_AI[count][2] = t[2];
											temp_duration_AI[count][3] = t[3];
											count++;
										} else {
											temp_duration_HI[count2][0] = t[0];
											temp_duration_HI[count2][1] = t[1];
											temp_duration_HI[count2][2] = t[2];
											temp_duration_HI[count2][3] = t[3];
											count2++;
										}
									}
									temp_duration_ODI4[count4][0] = t[0];
									temp_duration_ODI4[count4][1] = t[1];
									temp_duration_ODI4[count4][2] = t[2];
									temp_duration_ODI4[count4][3] = t[3];
									temp_duration_ODI4[count4][4] = t[5];
									count4++;
								} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
									temp_duration_noise[count3][0] = t[0];
									temp_duration_noise[count3][1] = t[1];
									temp_duration_noise[count3][2] = t[2];
									temp_duration_noise[count3][3] = t[3];
									temp_duration_noise[count3][4] = t[5];
									count3++;
								}
								// 将当前点作为下个下降的起点，继续检测
								t[0] = i;
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[5] = 0;
								t[6] = 0;
							} else {
								// 出现较大锯齿
								if (t[5] == 0) {
									t[5] = t[2];
								} else {
									t[5] = temp[t[2]] < temp[t[5]] ? t[2] : t[5]; // 将最低点保存在t[5]中
								}
								t[1] = i + 1;
								t[2] = i + 1;
								t[3] = 0;
								t[4] = 0;
								t[6] = 0;
							}
							count_horizontal = 0;
						}
					}
				} else if (temp[i] == temp[i + 1]) { // 检测到水平移动
					if (t[3] == 0) { // 下降中的水平移动
						t[2] = i + 1;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
					} else if (t[3] > 0) {
						t[4] = i + 1; // 上升中的水平移动
						if (t[6] != t[3])
							t[6] = t[3]; // 临时存储t[3]值，用于找回
					}
					count_horizontal++;
				} else if (temp[i] < temp[i + 1]) { // 检测到上升
					// 如果水平线的数据长度大于15，且血氧值与起始点血氧值差距小于等于2，则判断一次氧降结束
					if ((t[3] != 0 && temp[t[0]] - temp[t[3]] <= 0) || (count_horizontal > 15 && t[3] != 0
							&& ((t[5] == 0 && 2 * (temp[t[0]] - temp[t[2]]) >= 5 * (temp[t[0]] - temp[t[3]]))
									|| (t[5] > 0 && 2 * (temp[t[0]] - temp[t[5]]) >= 5 * (temp[t[0]] - temp[t[3]]))))) { // temp[t[0]]
																															// -
																															// temp[t[3]]
																															// <=
																															// 2
						// 一次下降过程结束
						if (t[5] == 0) {
							t_down = t[2] - t[0];
							t_up = t[3] - t[2];
							spo2_down = temp[t[0]] - temp[t[2]];
							spo2_up = temp[t[3]] - temp[t[2]];
						} else {
							t_down = t[5] - t[0];
							t_up = t[3] - t[5];
							spo2_down = temp[t[0]] - temp[t[5]];
							spo2_up = temp[t[3]] - temp[t[5]];
						}
						valley = t[2] - t[1];
						last = t[3] - t[0];

						if (((t[5] > 0 && temp_st_ave - temp[t[5]] >= 3)
								|| (t[5] == 0 && temp_st_ave - temp[t[2]] >= 3)) && last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
							if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
								// && spo2_down/t_down < spo2_up/t_up && t_down
								// > t_up
								if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5) { // 呼吸暂停的判断条件
									temp_duration_AI[count][0] = t[0];
									temp_duration_AI[count][1] = t[1];
									temp_duration_AI[count][2] = t[2];
									temp_duration_AI[count][3] = t[3];
									count++;
								} else {
									temp_duration_HI[count2][0] = t[0];
									temp_duration_HI[count2][1] = t[1];
									temp_duration_HI[count2][2] = t[2];
									temp_duration_HI[count2][3] = t[3];
									count2++;
								}
							}
							temp_duration_ODI4[count4][0] = t[0];
							temp_duration_ODI4[count4][1] = t[1];
							temp_duration_ODI4[count4][2] = t[2];
							temp_duration_ODI4[count4][3] = t[3];
							temp_duration_ODI4[count4][4] = t[5];
							count4++;
						} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
							temp_duration_noise[count3][0] = t[0];
							temp_duration_noise[count3][1] = t[1];
							temp_duration_noise[count3][2] = t[2];
							temp_duration_noise[count3][3] = t[3];
							temp_duration_noise[count3][4] = t[5];
							count3++;
						}
						// 重置参数，准备检测下个下降的起点
						flag = false;
						t[0] = 0;
						t[1] = 0;
						t[2] = 0;
						t[3] = 0;
						t[4] = 0;
						t[5] = 0;
						t[6] = 0;
						count_horizontal = 0;
					} else {
						// 正常上升
						if (t[4] == 0 || t[4] != i + 1) {
							count_horizontal = 0;
							t[3] = i + 1;
							t[4] = 0;
							t[6] = 0;
						} else if (t[4] == i + 1) {
							count_horizontal++;
						}
					}
				}

				// 一次下降过程结束，血氧值回升到下降的水平
				if (temp[t[0]] - temp[t[3]] <= 0 && t[3] != 0) {
					// 一次下降过程结束
					if (t[5] == 0) {
						t_down = t[2] - t[0];
						t_up = t[3] - t[2];
						spo2_down = temp[t[0]] - temp[t[2]];
						spo2_up = temp[t[3]] - temp[t[2]];
					} else {
						t_down = t[5] - t[0];
						t_up = t[3] - t[5];
						spo2_down = temp[t[0]] - temp[t[5]];
						spo2_up = temp[t[3]] - temp[t[5]];
					}
					valley = t[2] - t[1];
					last = t[3] - t[0];

					if (((t[5] > 0 && temp_st_ave - temp[t[5]] >= 3) || (t[5] == 0 && temp_st_ave - temp[t[2]] >= 3))
							&& last >= 7 && last < 210) { // 氧减的判断条件，血氧下降和上升的幅度都大于等于3，且总持续时间大于等于7个点，小于210个点
						if (t_down >= 2 && t_up >= 2) { // 呼吸暂停和低通气共同的判断条件
							// && spo2_down/t_down < spo2_up/t_up && t_down >
							// t_up
							if (t_up >= 3 && spo2_down >= 5 && spo2_up >= 5) { // 呼吸暂停的判断条件
								temp_duration_AI[count][0] = t[0];
								temp_duration_AI[count][1] = t[1];
								temp_duration_AI[count][2] = t[2];
								temp_duration_AI[count][3] = t[3];
								count++;
							} else {
								temp_duration_HI[count2][0] = t[0];
								temp_duration_HI[count2][1] = t[1];
								temp_duration_HI[count2][2] = t[2];
								temp_duration_HI[count2][3] = t[3];
								count2++;
							}
						}
						temp_duration_ODI4[count4][0] = t[0];
						temp_duration_ODI4[count4][1] = t[1];
						temp_duration_ODI4[count4][2] = t[2];
						temp_duration_ODI4[count4][3] = t[3];
						temp_duration_ODI4[count4][4] = t[5];
						count4++;
					} else if (last < 300 && (spo2_down >= 5 || spo2_up >= 5)) {
						temp_duration_noise[count3][0] = t[0];
						temp_duration_noise[count3][1] = t[1];
						temp_duration_noise[count3][2] = t[2];
						temp_duration_noise[count3][3] = t[3];
						temp_duration_noise[count3][4] = t[5];
						count3++;
					}
					// 重置参数，准备检测下个下降的起点
					flag = false;
					t[0] = 0;
					t[1] = 0;
					t[2] = 0;
					t[3] = 0;
					t[4] = 0;
					t[5] = 0;
					t[6] = 0;
				}
			} else if (temp[i] > temp[i + 1] && temp[i] > temp_st_ave - 10) { //
				t[0] = i;
				t[1] = i + 1;
				t[2] = i + 1;
				t[3] = 0;
				t[4] = 0;
				t[5] = 0;
				t[6] = 0;
				flag = true;
			}
		}
		AI_count = count;
		HI_count = count2;
		ODI4_count = count4;
	}

	/**
	 * 检测脉率曲线的波峰区间，对窗口内的数据计算平均值作为基线值，阈值分别比基线值高5和8
	 * 
	 * @param pr
	 *            原始的脉率值
	 * @param pace
	 *            滑动检测窗口的大小
	 * @return 波峰区间数据，每组数据的内容为：起点，峰值点，终点
	 */
	public int[][] waveDetection(int[] pr, int pace) {
		int pr_len = pr.length;
		int[][] temp = new int[pr_len / 5][3]; // 返回的波峰数据，起点、峰值点、终点
		int count = 0; // 波峰数据的数组坐标

		for (int i = 0; i < pr_len - pace; i += (pace / 2)) {
			if (i < pr_len - pace) {
				float sum = 0;
				for (int j = i; j < i + pace; j++)
					sum += pr[j];
				float bline = sum / pace; // 基线值，窗口内脉率平均值
				float lline = bline + 5; // 低阈值，基线值+5
				float hline = bline + 8; // 高阈值，基线值+8

				int state = 0; // 0：表示未检测到上升沿；1：表示检测到第一个特征点，2：表示检测到第二个特征点，3：表示检测到第三个特征点，4：表示检测到第四个特征点
				int[] wave = new int[4]; // 临时存放波峰的四个特征点的坐标值
				for (int j = i; j < i + pace; j++) {
					if (state == 0) {
						if (pr[j] > lline) { // 检测到上升沿，检测到第一个特征点
							wave[0] = j;
							state = 1;
						}
					} else if (state == 1) {
						if (pr[j] > hline) { // 检测到第二个特征点
							wave[1] = j;
							state = 2;
						} else if (pr[j] < lline) { // 第二个特征点没有达到高阈值，而是降低到了低阈值以下
							wave[0] = 0;
							state = 0;
						}
					} else if (state == 2) {
						if (pr[j] < hline) { // 检测到第三个特征点
							wave[2] = j;
							state = 3;
						}
					} else if (state == 3) {
						if (pr[j] < lline) { // 检测到第四个特征点，检测出一个波峰
							wave[3] = j;
							state = 4;
						} else if (pr[j] > hline) { // 第四个特征点没有低于低阈值，而是上升到了高阈值以上
							state = 2;
						}
					} else if (state == 4) { // 检测出波峰，计算波峰的起点、终点和峰值的坐标，并存储
						state = 0;
						int k = wave[0], l = 0;
						// 检测波峰起点
						while ((k > 0 && pr[k - 1] <= pr[k]) || (k > 1 && pr[k - 2] <= pr[k])
								|| (k > 2 && pr[k - 3] <= pr[k])) {
							if (pr[k - 1] >= pr[k] || (pr[k] > pr[k + 1] && pr[k - 1] <= pr[k])) {
								l++;
							} else if (l > 0) {
								l = 0;
							}
							wave[0] = k - 1;
							k--;
							if (l == 5) {
								wave[0] = k + l;
								l = 0;
								break;
							}
						}
						if (l > 0) {
							wave[0] = k + l;
							l = 0;
						}

						k = wave[3];
						// 检测波峰终点
						while ((k + 1 < pr_len && pr[k] >= pr[k + 1]) || (k + 2 < pr_len && pr[k] >= pr[k + 2])
								|| (k + 3 < pr_len && pr[k] >= pr[k + 3])) {
							if (pr[k] <= pr[k + 1] || (pr[k - 1] < pr[k] && pr[k] >= pr[k + 1])) {
								l++;
							} else if (l > 0) {
								l = 0;
							}
							wave[3] = k + 1;
							k++;
							if (l == 5) {
								wave[3] = k - l;
								l = 0;
								break;
							}
						}
						if (l > 0) {
							wave[3] = k - l;
							l = 0;
						}

						int peak = pr[wave[1]]; // 波峰峰值
						k = wave[1];
						// 检测波峰峰值点
						while (k <= wave[2]) {
							if (pr[k] > peak) {
								peak = pr[k];
								wave[1] = k;
							}
							k++;
						}

						// 存储波峰数据
						if (count > 1 && temp[count - 2][0] != wave[0] && temp[count - 2][1] != wave[1]
								&& temp[count - 2][2] != wave[3] && temp[count - 1][0] != wave[0]
								&& temp[count - 1][1] != wave[1] && temp[count - 1][2] != wave[3]) {
							temp[count][0] = wave[0]; // 起点
							temp[count][1] = wave[1]; // 波峰
							temp[count][2] = wave[3]; // 终点
							count++;
						} else if (count == 1 && temp[count - 1][0] != wave[0] && temp[count - 1][1] != wave[1]
								&& temp[count - 1][2] != wave[3]) {
							temp[count][0] = wave[0]; // 起点
							temp[count][1] = wave[1]; // 波峰
							temp[count][2] = wave[3]; // 终点
							count++;
						} else if (count == 0) {
							temp[count][0] = wave[0]; // 起点
							temp[count][1] = wave[1]; // 波峰
							temp[count][2] = wave[3]; // 终点
							count++;
						}
					}
				}
			} else {
				i = pr_len - pace - 1;
				float sum = 0;
				for (int j = i; j < i + pace; j++)
					sum += pr[j];
				float bline = sum / pace;
				float lline = bline + 5;
				float hline = bline + 8;

				int state = 0; // 0：表示未检测到上升沿；1：表示检测到第一个特征点，2：表示检测到第二个特征点，3：表示检测到第三个特征点，4：表示检测到第四个特征点
				int[] wave = new int[4]; // 临时存放波峰的四个特征点的坐标值
				for (int j = i; j < i + pace; j++) {
					if (state == 0) {
						if (pr[j] > lline) { // 检测到上升沿，检测到第一个特征点
							wave[0] = j;
							state = 1;
						}
					} else if (state == 1) {
						if (pr[j] > hline) { // 检测到第二个特征点
							wave[1] = j;
							state = 2;
						} else if (pr[j] < lline) { // 第二个特征点没有达到高阈值，而是降低到了低阈值以下
							wave[0] = 0;
							state = 0;
						}
					} else if (state == 2) {
						if (pr[j] < hline) { // 检测到第三个特征点
							wave[2] = j;
							state = 3;
						}
					} else if (state == 3) {
						if (pr[j] < lline) { // 检测到第四个特征点，检测出一个波峰
							wave[3] = j;
							state = 4;
						} else if (pr[j] > hline) { // 第四个特征点没有低于低阈值，而是上升到了高阈值以上
							state = 2;
						}
					} else if (state == 4) { // 检测出波峰，计算波峰的起点、终点和峰值的坐标，并存储
						state = 0;
						int k = wave[0], l = 0;
						// 检测波峰起点
						while ((k > 0 && pr[k - 1] <= pr[k]) || (k > 1 && pr[k - 2] <= pr[k])
								|| (k > 2 && pr[k - 3] <= pr[k])) {
							if (pr[k - 1] >= pr[k] || (pr[k] > pr[k + 1] && pr[k - 1] <= pr[k])) {
								l++;
							} else if (l > 0) {
								l = 0;
							}
							wave[0] = k - 1;
							k--;
							if (l == 5) {
								wave[0] = k + l;
								l = 0;
								break;
							}
						}
						if (l > 0) {
							wave[0] = k + l;
							l = 0;
						}

						k = wave[3];
						// 检测波峰终点
						while ((k + 1 < pr_len && pr[k] >= pr[k + 1]) || (k + 2 < pr_len && pr[k] >= pr[k + 2])
								|| (k + 3 < pr_len && pr[k] >= pr[k + 3])) {
							if (pr[k] <= pr[k + 1] || (pr[k - 1] < pr[k] && pr[k] >= pr[k + 1])) {
								l++;
							} else if (l > 0) {
								l = 0;
							}
							wave[3] = k + 1;
							k++;
							if (l == 5) {
								wave[3] = k - l;
								l = 0;
								break;
							}
						}
						if (l > 0) {
							wave[3] = k - l;
							l = 0;
						}

						int peak = pr[wave[1]]; // 波峰峰值
						k = wave[1];
						// 检测波峰峰值点
						while (k <= wave[2]) {
							if (pr[k] > peak) {
								peak = pr[k];
								wave[1] = k;
							}
							k++;
						}

						// 存储波峰数据
						if (count > 1 && temp[count - 2][0] != wave[0] && temp[count - 2][1] != wave[1]
								&& temp[count - 2][2] != wave[3] && temp[count - 1][0] != wave[0]
								&& temp[count - 1][1] != wave[1] && temp[count - 1][2] != wave[3]) {
							temp[count][0] = wave[0]; // 起点
							temp[count][1] = wave[1]; // 波峰
							temp[count][2] = wave[3]; // 终点
							count++;
						} else if (count == 1 && temp[count - 1][0] != wave[0] && temp[count - 1][1] != wave[1]
								&& temp[count - 1][2] != wave[3]) {
							temp[count][0] = wave[0]; // 起点
							temp[count][1] = wave[1]; // 波峰
							temp[count][2] = wave[3]; // 终点
							count++;
						} else if (count == 0) {
							temp[count][0] = wave[0]; // 起点
							temp[count][1] = wave[1]; // 波峰
							temp[count][2] = wave[3]; // 终点
							count++;
						}
					}
				}
			}
		}
		return temp;
	}

	/**
	 * 计算其他血氧相关参数
	 * @param temp_aft2_spo2 去噪后的血氧数据
	 * @param AI_count 呼吸暂停次数
	 * @param HI_count 低通气次数
	 * @param ODI4_count 氧减次数
	 * @param time_wholeNight 睡眠持续时间
	 */
	public void para_HI(int[] temp_aft2_spo2, int AI_count, int HI_count, int ODI4_count, int[] time_wholeNight) {
		int aft2_len = temp_aft2_spo2.length;
		int[] temp = new int[aft2_len]; // 接收aft2数组
		for (int i = 0; i < aft2_len; i++)
			temp[i] = temp_aft2_spo2[i];

		LSPO2 = 100;
		HSPO2 = 0;
		float sum_mspo2 = 0, sum_ts90 = 0; // sum1用于计算mspo2，sum2用于计算ts90
		for (int i = 0; i < aft2_len; i++) {
			sum_mspo2 += temp[i];
			if (temp[i] < LSPO2)
				LSPO2 = temp[i]; // 计算最低血氧饱和度
			if (temp[i] > HSPO2)
				HSPO2 = temp[i]; // 计算最低血氧饱和度
			if (temp[i] < 90)
				sum_ts90++;
		}
		MSPO2 = sum_mspo2 / aft2_len; // 计算平均血氧饱和度
		TS90 = sum_ts90 * 100 / aft2_len; // 计算血氧饱和度低于90的占比(已计算为百分数)

		AHI = (AI_count + HI_count) / (time_wholeNight[0] + time_wholeNight[1] / 60f + time_wholeNight[2] / 3600f); // 计算AHI指数
		AI_index = AI_count / (time_wholeNight[0] + time_wholeNight[1] / 60f + time_wholeNight[2] / 3600f); // 计算呼吸暂停指数
		HI_index = HI_count / (time_wholeNight[0] + time_wholeNight[1] / 60f + time_wholeNight[2] / 3600f); // 计算低通气指数
		ODI4_index = ODI4_count / (time_wholeNight[0] + time_wholeNight[1] / 60f + time_wholeNight[2] / 3600f); // 计算氧减饱和度指数

	}

	/**
	 * 计算其他血氧相关参数
	 * @param temp_aft2_spo2 去噪后的血氧数据
	 * @param AI_count 呼吸暂停次数
	 * @param HI_count 低通气次数
	 * @param ODI4_count 氧减次数
	 * @param time_wholeNight2 睡眠持续时间
	 */
	public void para_HI2(int[] temp_aft2_spo2, int AI_count, int HI_count, int ODI4_count, long time_wholeNight2) {
		int aft2_len = temp_aft2_spo2.length;
		int[] temp = new int[aft2_len]; // 接收aft2数组
		for (int i = 0; i < aft2_len; i++)
			temp[i] = temp_aft2_spo2[i];

		LSPO2 = 100;
		HSPO2 = 0;
		float sum_mspo2 = 0, sum_ts90 = 0; // sum1用于计算mspo2，sum2用于计算ts90
		for (int i = 0; i < aft2_len; i++) {
			sum_mspo2 += temp[i];
			if (temp[i] < LSPO2)
				LSPO2 = temp[i]; // 计算最低血氧饱和度
			if (temp[i] > HSPO2)
				HSPO2 = temp[i]; // 计算最低血氧饱和度
			if (temp[i] < 90)
				sum_ts90++;
		}
		MSPO2 = sum_mspo2 / aft2_len; // 计算平均血氧饱和度
		TS90 = sum_ts90 * 100 / aft2_len; // 计算血氧饱和度低于90的占比(已计算为百分数)

		AHI = (AI_count + HI_count) / (time_wholeNight2 / 1000 / 3600f); // 计算AHI指数
		AI_index = AI_count / (time_wholeNight2 / 1000 / 3600f); // 计算呼吸暂停指数
		HI_index = HI_count / (time_wholeNight2 / 1000 / 3600f); // 计算低通气指数
		ODI4_index = ODI4_count / (time_wholeNight2 / 1000 / 3600f); // 计算氧减饱和度指数

	}

	/**
	 * 计算呼吸暂停和低通气相关的时间数据
	 * @param temp_aft2_spo2 去噪后的血氧数据
	 * @param temp_extract_string_time String类型的时间
	 * @param time_wholeNight 睡眠持续时间
	 * @param temp_duration_AI 呼吸暂停事件
	 * @param temp_duration_HI 低通气事件
	 * @param temp_duration_ODI4 氧减事件
	 */
	public void timeParaCalculate(int[] temp_aft2_spo2, int[][] temp_extract_string_time, int[] time_wholeNight, int[][] temp_duration_AI, int[][] temp_duration_HI, int[][] temp_duration_ODI4) {
		int aft2_len = temp_aft2_spo2.length;

		// 计算AI相关参数
		int temp_length_AI = 0;
		int[] temp_Interval_AI;
		for (int i = 0; i < temp_duration_AI.length & temp_duration_AI[i][1] != 0; i++) {
			if (temp_duration_AI[i][3] - temp_duration_AI[i][0] > temp_length_AI) { // 判断呼吸暂停的时间长度是否比前一次更长
				temp_length_AI = temp_duration_AI[i][3] - temp_duration_AI[i][0];
				AI_longest_duration = calTimeInterval(temp_extract_string_time[temp_duration_AI[i][0]],
						temp_extract_string_time[temp_duration_AI[i][3]]); // 呼吸暂停最长单次时长
				for (int j = 0; j < AI_longest_timePoint.length; j++) {
					AI_longest_timePoint[j] = temp_extract_string_time[temp_duration_AI[i][0]][j + 1]; // 呼吸暂停最长单次时刻
				}
				AI_longest_spo2 = temp_aft2_spo2[temp_duration_AI[i][1]]; // 最长呼吸暂停发生时的最低血氧值
			}
			temp_Interval_AI = calTimeInterval(temp_extract_string_time[temp_duration_AI[i][0]],
					temp_extract_string_time[temp_duration_AI[i][3]]);
			AI_time_sum = calTimeSum(AI_time_sum, temp_Interval_AI); // 呼吸暂停总时长
		}
		AI_time_percent = (float) (AI_time_sum[0] * 3600 + AI_time_sum[1] * 60 + AI_time_sum[2]) * 100f
				/ (time_wholeNight[0] * 3600 + time_wholeNight[1] * 60 + time_wholeNight[2]);

		// 计算HI相关参数ODI4_longest_duration
		int temp_length_HI = 0;
		int[] temp_Interval_HI;
		for (int i = 0; i < temp_duration_HI.length & temp_duration_HI[i][1] != 0; i++) {
			if (temp_duration_HI[i][3] - temp_duration_HI[i][0] > temp_length_HI) { // 判断低通气的时间长度是否比前一次更长
				temp_length_HI = temp_duration_HI[i][3] - temp_duration_HI[i][0];
				HI_longest_duration = calTimeInterval(temp_extract_string_time[temp_duration_HI[i][0]],
						temp_extract_string_time[temp_duration_HI[i][3]]); // 低通气最长单次时长
				for (int j = 0; j < HI_longest_timePoint.length; j++) {
					HI_longest_timePoint[j] = temp_extract_string_time[temp_duration_HI[i][0]][j + 1]; // 低通气最长单次时刻
				}
				HI_longest_spo2 = temp_aft2_spo2[temp_duration_HI[i][1]]; // 最长低通气发生时的最低血氧值
			}
			temp_Interval_HI = calTimeInterval(temp_extract_string_time[temp_duration_HI[i][0]],
					temp_extract_string_time[temp_duration_HI[i][3]]);
			HI_time_sum = calTimeSum(HI_time_sum, temp_Interval_HI); // 低通气总时长
		}
		HI_time_percent = (HI_time_sum[0] * 3600 + HI_time_sum[1] * 60 + HI_time_sum[2]) * 100f
				/ (time_wholeNight[0] * 3600 + time_wholeNight[1] * 60 + time_wholeNight[2]);

		// 计算ODI4相关参数ODI4_longest_duration
		int temp_length_ODI4 = 0, temp_high_spo2 = 0, temp_low_spo2 = 0;
		for (int i = 0; i < temp_duration_ODI4.length & temp_duration_ODI4[i][1] != 0; i++) {
			if (temp_duration_ODI4[i][3] - temp_duration_ODI4[i][0] > temp_length_ODI4) { // 判断氧降的时间长度是否比前一次更长
				temp_length_ODI4 = temp_duration_ODI4[i][3] - temp_duration_ODI4[i][0];
				ODI4_longest_duration = calTimeInterval(temp_extract_string_time[temp_duration_ODI4[i][0]],
						temp_extract_string_time[temp_duration_ODI4[i][3]]); // 氧降最长单次时长
				for (int j = 0; j < ODI4_longest_timePoint.length; j++) {
					ODI4_longest_timePoint[j] = temp_extract_string_time[temp_duration_ODI4[i][0]][j + 1]; // 氧降最长单次时刻
				}
			}
			temp_high_spo2 = temp_aft2_spo2[temp_duration_ODI4[i][0]] > temp_aft2_spo2[temp_duration_ODI4[i][3]]
					? temp_aft2_spo2[temp_duration_ODI4[i][0]] : temp_aft2_spo2[temp_duration_ODI4[i][3]];
			if (temp_aft2_spo2[temp_duration_ODI4[i][4]] == 0) {
				temp_low_spo2 = temp_aft2_spo2[temp_duration_ODI4[i][2]];
			} else {
				temp_low_spo2 = temp_aft2_spo2[temp_duration_ODI4[i][2]] < temp_aft2_spo2[temp_duration_ODI4[i][4]]
						? temp_aft2_spo2[temp_duration_ODI4[i][2]] : temp_aft2_spo2[temp_duration_ODI4[i][4]];
			}
			if (temp_high_spo2 - temp_low_spo2 > ODI4_largest_range) { // 判断氧减的下降幅度是否比前一次更大
				ODI4_largest_range = temp_high_spo2 - temp_low_spo2; // 最大氧减发生时的血氧下降幅度
				ODI4_largest_duration = calTimeInterval(temp_extract_string_time[temp_duration_ODI4[i][0]],
						temp_extract_string_time[temp_duration_ODI4[i][3]]); // 氧降最大单次时长
				for (int j = 0; j < ODI4_largest_timePoint.length; j++) {
					ODI4_largest_timePoint[j] = temp_extract_string_time[temp_duration_ODI4[i][0]][j + 1]; // 氧降最大单次时刻
				}
			}
		}

		// 计算血氧分布
		int xy = 0; // 时间区间的起点标志
		int[] time_temp; // 时间区间的临时存放数组
		for (int i = 0; i < aft2_len; i++) {
			// 计算血氧饱和度分布
			if (i == aft2_len - 1 || temp_aft2_spo2[i] != temp_aft2_spo2[xy]) {
				if (temp_aft2_spo2[xy] >= 90) {
					time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
					time_temp = calTimeSum(distribution_90_100, time_temp);
					for (int j = 0; j < 3; j++) {
						distribution_90_100[j] = time_temp[j];
					}

					if (temp_aft2_spo2[xy] < 94) {
						time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
						time_temp = calTimeSum(temp_distribution_90_94, time_temp);
						for (int j = 0; j < 3; j++) {
							temp_distribution_90_94[j] = time_temp[j];
						}
					}
				} else if (temp_aft2_spo2[xy] >= 80) {
					time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
					time_temp = calTimeSum(distribution_80_90, time_temp);
					for (int j = 0; j < 3; j++) {
						distribution_80_90[j] = time_temp[j];
					}

					if (temp_aft2_spo2[xy] < 85) {
						time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
						time_temp = calTimeSum(temp_distribution_80_85, time_temp);
						for (int j = 0; j < 3; j++) {
							temp_distribution_80_85[j] = time_temp[j];
						}
					} else {
						time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
						time_temp = calTimeSum(temp_distribution_85_90, time_temp);
						for (int j = 0; j < 3; j++) {
							temp_distribution_85_90[j] = time_temp[j];
						}
					}
				} else if (temp_aft2_spo2[xy] >= 70) {
					time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
					time_temp = calTimeSum(distribution_70_80, time_temp);
					for (int j = 0; j < 3; j++) {
						distribution_70_80[j] = time_temp[j];
					}

					if (temp_aft2_spo2[xy] < 75) {
						time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
						time_temp = calTimeSum(temp_distribution_70_75, time_temp);
						for (int j = 0; j < 3; j++) {
							temp_distribution_70_75[j] = time_temp[j];
						}
					} else {
						time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
						time_temp = calTimeSum(temp_distribution_75_80, time_temp);
						for (int j = 0; j < 3; j++) {
							temp_distribution_75_80[j] = time_temp[j];
						}
					}
				} else if (temp_aft2_spo2[xy] >= 60) {
					time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
					time_temp = calTimeSum(distribution_60_70, time_temp);
					for (int j = 0; j < 3; j++) {
						distribution_60_70[j] = time_temp[j];
					}

					if (temp_aft2_spo2[xy] < 65) {
						time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
						time_temp = calTimeSum(temp_distribution_0_65, time_temp);
						for (int j = 0; j < 3; j++) {
							temp_distribution_0_65[j] = time_temp[j];
						}
					} else {
						time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
						time_temp = calTimeSum(temp_distribution_65_70, time_temp);
						for (int j = 0; j < 3; j++) {
							temp_distribution_65_70[j] = time_temp[j];
						}
					}
				} else if (temp_aft2_spo2[xy] >= 50) {
					time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
					time_temp = calTimeSum(distribution_50_60, time_temp);
					for (int j = 0; j < 3; j++) {
						distribution_50_60[j] = time_temp[j];
					}

					// time_temp = calTimeInterval(temp_extract_string_time[xy],
					// temp_extract_string_time[i]);
					// time_temp = calTimeSum(temp_distribution_0_60,
					// time_temp);
					// for(int j = 0; j < 3; j++){
					// temp_distribution_0_60[j] = time_temp[j];
					// }
					time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
					time_temp = calTimeSum(temp_distribution_0_65, time_temp);
					for (int j = 0; j < 3; j++) {
						temp_distribution_0_65[j] = time_temp[j];
					}
				} else {
					time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
					time_temp = calTimeSum(distribution_0_50, time_temp);
					for (int j = 0; j < 3; j++) {
						distribution_0_50[j] = time_temp[j];
					}

					// time_temp = calTimeInterval(temp_extract_string_time[xy],
					// temp_extract_string_time[i]);
					// time_temp = calTimeSum(temp_distribution_0_60,
					// time_temp);
					// for(int j = 0; j < 3; j++){
					// temp_distribution_0_60[j] = time_temp[j];
					// }
					time_temp = calTimeInterval(temp_extract_string_time[xy], temp_extract_string_time[i]);
					time_temp = calTimeSum(temp_distribution_0_65, time_temp);
					for (int j = 0; j < 3; j++) {
						temp_distribution_0_65[j] = time_temp[j];
					}
				}
				xy = i;
			}
		}

		// int sum = (temp_distribution_90_94[0]*60 +
		// temp_distribution_90_94[1]) + (temp_distribution_85_90[0]*60 +
		// temp_distribution_85_90[1])*2
		// + (temp_distribution_80_85[0]*60 + temp_distribution_80_85[1])*3 +
		// (temp_distribution_75_80[0]*60 + temp_distribution_75_80[1])*4
		// + (temp_distribution_70_75[0]*60 + temp_distribution_70_75[1])*6 +
		// (temp_distribution_65_70[0]*60 + temp_distribution_65_70[1])*8
		// + (temp_distribution_60_65[0]*60 + temp_distribution_60_65[1])*10 +
		// (temp_distribution_0_60[0]*60 + temp_distribution_0_60[1])*12;
		int sum = //(temp_distribution_90_94[0] * 60 + temp_distribution_90_94[1])
				(temp_distribution_85_90[0] * 60 + temp_distribution_85_90[1]) * 1
				+ (temp_distribution_80_85[0] * 60 + temp_distribution_80_85[1]) * 2
				+ (temp_distribution_75_80[0] * 60 + temp_distribution_75_80[1]) * 4
				+ (temp_distribution_70_75[0] * 60 + temp_distribution_70_75[1]) * 6
				+ (temp_distribution_65_70[0] * 60 + temp_distribution_65_70[1]) * 8
				+ (temp_distribution_0_65[0] * 60 + temp_distribution_0_65[1]) * 10;
		ODHI = sum * 100f / (time_wholeNight[0] * 60 + time_wholeNight[1]);

		// 计算血氧分布
		for (int i = 0; i < temp_duration_ODI4.length & temp_duration_ODI4[i][1] != 0; i++) {
			if (temp_aft2_spo2[temp_duration_ODI4[i][1]] >= 90 && temp_aft2_spo2[temp_duration_ODI4[i][4]] >= 90) {
				distribution_90_100[3]++;
			} else if (temp_aft2_spo2[temp_duration_ODI4[i][1]] >= 80
					&& temp_aft2_spo2[temp_duration_ODI4[i][4]] >= 80) {
				distribution_80_90[3]++;
			} else if (temp_aft2_spo2[temp_duration_ODI4[i][1]] >= 70
					&& temp_aft2_spo2[temp_duration_ODI4[i][4]] >= 70) {
				distribution_70_80[3]++;
			} else if (temp_aft2_spo2[temp_duration_ODI4[i][1]] >= 60
					&& temp_aft2_spo2[temp_duration_ODI4[i][4]] >= 60) {
				distribution_60_70[3]++;
			} else if (temp_aft2_spo2[temp_duration_ODI4[i][1]] >= 50
					&& temp_aft2_spo2[temp_duration_ODI4[i][4]] >= 50) {
				distribution_50_60[3]++;
			} else {
				distribution_0_50[3]++;
			}
		}
	}

	/**
	 * 计算呼吸暂停和低通气相关的时间数据
	 * @param temp_aft2_spo2 去噪后的血氧数据
	 * @param temp_num_time long类型的时间
	 * @param time_wholeNight2 睡眠持续时间
	 * @param temp_duration_AI 呼吸暂停事件
	 * @param temp_duration_HI 低通气事件
	 * @param temp_duration_ODI4 氧减事件
	 */
	public void timeParaCalculate2(int[] temp_aft2_spo2, long[] temp_num_time, long time_wholeNight2, int[][] temp_duration_AI, int[][] temp_duration_HI, int[][] temp_duration_ODI4) {
		int aft2_len = temp_aft2_spo2.length;

		// 计算AI相关参数
		for (int i = 0; i < temp_duration_AI.length & temp_duration_AI[i][1] != 0; i++) {
			if ((temp_num_time[temp_duration_AI[i][3]]
					- temp_num_time[temp_duration_AI[i][0]]) > AI_longest_duration2) { // 判断呼吸暂停的时间长度是否比前一次更长
				AI_longest_duration2 = (temp_num_time[temp_duration_AI[i][3]] - temp_num_time[temp_duration_AI[i][0]]); // 呼吸暂停最长单次时长
				AI_longest_timePoint2 = temp_num_time[temp_duration_AI[i][0]]; // 呼吸暂停最长单次时刻
				AI_longest_spo2 = temp_aft2_spo2[temp_duration_AI[i][1]] < temp_aft2_spo2[temp_duration_AI[i][4]]
						? temp_aft2_spo2[temp_duration_AI[i][1]] : temp_aft2_spo2[temp_duration_AI[i][1]]; // 最长呼吸暂停发生时的最低血氧值
			}
			AI_time_sum2 = AI_time_sum2
					+ (temp_num_time[temp_duration_AI[i][3]] - temp_num_time[temp_duration_AI[i][0]]); // 呼吸暂停总时长
		}
		AI_time_percent2 = AI_time_sum2 * 100f / time_wholeNight2; // 呼吸暂停总时间占比

		// 计算HI相关参数
		for (int i = 0; i < temp_duration_HI.length & temp_duration_HI[i][1] != 0; i++) {
			if ((temp_num_time[temp_duration_HI[i][3]]
					- temp_num_time[temp_duration_HI[i][0]]) > HI_longest_duration2) { // 判断低通气的时间长度是否比前一次更长
				HI_longest_duration2 = (temp_num_time[temp_duration_HI[i][3]] - temp_num_time[temp_duration_HI[i][0]]); // 低通气最长单次时长
				HI_longest_timePoint2 = temp_num_time[temp_duration_HI[i][0]]; // 低通气最长单次时刻
				HI_longest_spo2 = temp_aft2_spo2[temp_duration_HI[i][1]] < temp_aft2_spo2[temp_duration_HI[i][1]]
						? temp_aft2_spo2[temp_duration_HI[i][1]] : temp_aft2_spo2[temp_duration_HI[i][1]]; // 最长低通气发生时的最低血氧值
			}
			HI_time_sum2 = HI_time_sum2
					+ (temp_num_time[temp_duration_HI[i][3]] - temp_num_time[temp_duration_HI[i][0]]); // 低通气总时长
		}
		HI_time_percent2 = HI_time_sum2 * 100f / time_wholeNight2; // 低通气总时间占比

		// 计算ODI4相关参数
		for (int i = 0; i < temp_duration_ODI4.length & temp_duration_ODI4[i][1] != 0; i++) {
			if ((temp_num_time[temp_duration_ODI4[i][3]]
					- temp_num_time[temp_duration_ODI4[i][0]]) > ODI4_longest_duration2) { // 判断氧减的时间长度是否比前一次更长
				ODI4_longest_duration2 = (temp_num_time[temp_duration_ODI4[i][3]]
						- temp_num_time[temp_duration_ODI4[i][0]]); // 氧减最长单次时长
				ODI4_longest_timePoint2 = temp_num_time[temp_duration_ODI4[i][0]]; // 氧减最长单次时刻
			}
			if (((temp_aft2_spo2[temp_duration_ODI4[i][0]]
					- temp_aft2_spo2[temp_duration_ODI4[i][2]] > temp_aft2_spo2[temp_duration_ODI4[i][3]]
							? temp_aft2_spo2[temp_duration_ODI4[i][0]] : temp_aft2_spo2[temp_duration_ODI4[i][3]])
					- temp_aft2_spo2[temp_duration_ODI4[i][2]]) > ODI4_largest_range) { // 判断氧减的下降幅度是否比前一次更大
				ODI4_largest_range = temp_aft2_spo2[temp_duration_ODI4[i][0]] > temp_aft2_spo2[temp_duration_ODI4[i][3]]
						? temp_aft2_spo2[temp_duration_ODI4[i][0]] : temp_aft2_spo2[temp_duration_ODI4[i][3]]; // 最大氧减发生时的血氧下降幅度
				ODI4_largest_duration2 = (temp_num_time[temp_duration_ODI4[i][3]]
						- temp_num_time[temp_duration_ODI4[i][0]]); // 氧减最大单次时长
				ODI4_largest_timePoint2 = temp_num_time[temp_duration_ODI4[i][0]]; // 氧减最大单次时刻
			}
		}

		// 计算血氧分布
		int xy = 0; // 时间区间的起点标志
		for (int i = 0; i < aft2_len; i++) {
			// 计算血氧饱和度分布
			if (i == aft2_len - 1 || temp_aft2_spo2[i] != temp_aft2_spo2[xy]) {
				if (temp_aft2_spo2[xy] >= 90) {
					distribution_90_1002[0] += (temp_num_time[i] - temp_num_time[xy]);

					if (temp_aft2_spo2[xy] < 94) {
						temp_distribution_90_942 += (temp_num_time[i] - temp_num_time[xy]);
					}
				} else if (temp_aft2_spo2[xy] >= 80) {
					distribution_80_902[0] += (temp_num_time[i] - temp_num_time[xy]);

					if (temp_aft2_spo2[xy] < 85) {
						temp_distribution_80_852 += (temp_num_time[i] - temp_num_time[xy]);
					} else {
						temp_distribution_85_902 += (temp_num_time[i] - temp_num_time[xy]);
					}
				} else if (temp_aft2_spo2[xy] >= 70) {
					distribution_70_802[0] += (temp_num_time[i] - temp_num_time[xy]);

					if (temp_aft2_spo2[xy] < 75) {
						temp_distribution_70_752 += (temp_num_time[i] - temp_num_time[xy]);
					} else {
						temp_distribution_75_802 += (temp_num_time[i] - temp_num_time[xy]);
					}
				} else if (temp_aft2_spo2[xy] >= 60) {
					distribution_60_702[0] += (temp_num_time[i] - temp_num_time[xy]);

					if (temp_aft2_spo2[xy] < 65) {
						// temp_distribution_60_652 += (temp_num_time[i] -
						// temp_num_time[xy]);
						temp_distribution_0_652 += (temp_num_time[i] - temp_num_time[xy]);
					} else {
						temp_distribution_65_702 += (temp_num_time[i] - temp_num_time[xy]);
					}
				} else if (temp_aft2_spo2[xy] >= 50) {
					distribution_50_602[0] += (temp_num_time[i] - temp_num_time[xy]);

					// temp_distribution_0_602 += (temp_num_time[i] -
					// temp_num_time[xy]);
					temp_distribution_0_652 += (temp_num_time[i] - temp_num_time[xy]);
				} else {
					distribution_0_502[0] += (temp_num_time[i] - temp_num_time[xy]);

					// temp_distribution_0_602 += (temp_num_time[i] -
					// temp_num_time[xy]);
					temp_distribution_0_652 += (temp_num_time[i] - temp_num_time[xy]);
				}
				xy = i;
			}
		}

		// ODHI = (temp_distribution_90_942 + temp_distribution_85_902 * 2 +
		// temp_distribution_80_852 * 3 + temp_distribution_75_802 * 4
		// + temp_distribution_70_752 * 6 + temp_distribution_65_702 * 8 +
		// temp_distribution_60_652 * 10 + temp_distribution_0_602 * 12) *100f /
		// time_wholeNight2;
		ODHI = (//temp_distribution_90_942 + 
				temp_distribution_85_902 * 1 + 
				temp_distribution_80_852 * 2 + 
				temp_distribution_75_802 * 4 + 
				temp_distribution_70_752 * 6 + 
				temp_distribution_65_702 * 8 + 
				temp_distribution_0_652 * 10) * 100f / time_wholeNight2;

		// 计算血氧分布
		for (int i = 0; i < temp_duration_ODI4.length & temp_duration_ODI4[i][1] != 0; i++) {
			if (temp_aft2_spo2[temp_duration_ODI4[i][1]] >= 90 && temp_aft2_spo2[temp_duration_ODI4[i][4]] >= 90) {
				distribution_90_1002[1]++;
			} else if (temp_aft2_spo2[temp_duration_ODI4[i][1]] >= 80
					&& temp_aft2_spo2[temp_duration_ODI4[i][4]] >= 80) {
				distribution_80_902[1]++;
			} else if (temp_aft2_spo2[temp_duration_ODI4[i][1]] >= 70
					&& temp_aft2_spo2[temp_duration_ODI4[i][4]] >= 70) {
				distribution_70_802[1]++;
			} else if (temp_aft2_spo2[temp_duration_ODI4[i][1]] >= 60
					&& temp_aft2_spo2[temp_duration_ODI4[i][4]] >= 60) {
				distribution_60_702[1]++;
			} else if (temp_aft2_spo2[temp_duration_ODI4[i][1]] >= 50
					&& temp_aft2_spo2[temp_duration_ODI4[i][4]] >= 50) {
				distribution_50_602[1]++;
			} else {
				distribution_0_502[1]++;
			}
		}

	}

	/**
	 * 计算氧减密集区间、氧减危险指数等指标
	 * @param temp_extract_string_time String类型的时间
	 * @param time_wholeNight long类型的时间
	 * @param temp_duration_ODI4 氧减事件
	 * @param temp_aft2_spo2 过滤后的血氧数据
	 * @param temp_aft_pr 过滤后的脉率数据
	 */
	private void hazardIndex(int[][] temp_extract_string_time, int[] time_wholeNight, int[][] temp_duration_ODI4, int[] temp_aft2_spo2,
			int[] temp_aft_pr) {
		int length = temp_aft2_spo2.length; // 原始数据的长度
		int len = (time_wholeNight[0] * 60 + time_wholeNight[1]) / 15; // 15分钟区间的个数
		distribution_ODI_0_85 = new int[len + 1];
		// System.out.println(temp_extract_string_time.length + "\n" +
		// temp_aft2_spo2.length + "\n" + temp_aft_pr.length);

		// 将15分钟区间的起止时间转化为起止点，并存储在index数组中
		int[] index = new int[len + 1]; // 存放15分钟区间的起止点
		int count_index = 0; // index数组的指针
		int[] temp_time; // 时间间隔的临时存放数组
		for (int i = 0; i < length; i++) {
			temp_time = calTimeInterval(temp_extract_string_time[0], temp_extract_string_time[i]);
			if (temp_time[0] * 60 + temp_time[1] >= 15 * count_index) {
				// System.out.println("与开始时间相差 " + (temp_time[0] * 60 +
				// temp_time[1]) + " 分钟\nindex[" + count_index + "] = " + i);
				index[count_index] = i;
				count_index++;
			}
		}

		// 统计每个15分钟区间内氧减低于85的次数，结果存储在distribution_ODI_0_85数组中
		int index_odi = 0; // 氧减数组的指针
		for (int i = 0; i < len; i++) {
			int count = 0; // 氧减低于85的计数
			while (temp_duration_ODI4[index_odi][5] >= index[i] && temp_duration_ODI4[index_odi][5] < index[i + 1]) {
				if (temp_aft2_spo2[temp_duration_ODI4[index_odi][5]] <= 85) {
					count++;
				}
				index_odi++;
			}
			distribution_ODI_0_85[i] = count;
//			System.out.println("distribution_ODI_0_85[" + i + "] = " + distribution_ODI_0_85[i] + " , index: "
//					+ index[i] + " ~ " + index[i + 1]);
		}

		// 计算睡眠氧减危险指数
		int sum = 0;
		for (int i : distribution_ODI_0_85)
			sum += i;
		distribution_ODI_0_85[len] = sum;
//		System.out.println("sum = " + sum);
		if(sum > 0){
			float average = (float) sum / len;
			float variance = 0, standard_deviation = 0;
			for (int i : distribution_ODI_0_85)
				variance += Math.pow(i - average, 2);
			standard_deviation = (float) Math.pow(variance, 0.5);
			SODHI = sum * standard_deviation / len;
//			System.out.println("variance = " + variance);
//			System.out.println("standard_deviation = " + standard_deviation);
		}else{
			SODHI = 0;
		}
//		System.out.println("SODHI = " + SODHI);
		
		// 计算最大氧减密集区间的指标
		int[] max3 = new int[5]; // max3数组保存氧减次数最多的5个区间在distribution_ODI_0_85中的指针，指针值越靠前，次数越多
		for (int i = 0; i < len; i++) {
			if (distribution_ODI_0_85[i] > distribution_ODI_0_85[max3[0]]) {
				max3[4] = max3[3];
				max3[3] = max3[2];
				max3[2] = max3[1];
				max3[1] = max3[0];
				max3[0] = i;
			} else if (distribution_ODI_0_85[i] > distribution_ODI_0_85[max3[1]]) {
				max3[4] = max3[3];
				max3[3] = max3[2];
				max3[2] = max3[1];
				max3[1] = i;
			} else if (distribution_ODI_0_85[i] > distribution_ODI_0_85[max3[2]]) {
				max3[4] = max3[3];
				max3[3] = max3[2];
				max3[2] = i;
			} else if (distribution_ODI_0_85[i] > distribution_ODI_0_85[max3[3]]) {
				max3[4] = max3[3];
				max3[3] = i;
			} else if (distribution_ODI_0_85[i] > distribution_ODI_0_85[max3[4]]) {
				max3[4] = i;
			}
		}
//		System.out.println(
//				max3[0] + "\t" + distribution_ODI_0_85[max3[0]] + "\t" + max3[1] + "\t" + distribution_ODI_0_85[max3[1]]
//						+ "\t" + max3[2] + "\t" + distribution_ODI_0_85[max3[2]] + "\t" + max3[3] + "\t"
//						+ distribution_ODI_0_85[max3[3]] + "\t" + max3[4] + "\t" + distribution_ODI_0_85[max3[4]]);

		for (int i = 0; i < max3.length; i++) {
			int[] time_interval_start = new int[3]; // 15分钟区间的开始时间
			long time_interval_start2 = 0; // 15分钟区间的开始时间
			int[] time_interval_end = new int[3]; // 15分钟区间的结束时间
			long time_interval_end2 = 0; // 15分钟区间的结束时间
			int count_odi = 0; // 低于85的氧减次数
			int[] time_odi = new int[3]; // 氧减总时间
			long time_odi2 = 0; // 氧减总时间
			float mspo2 = 0; // 平均血氧
			int pr_max = 0; // 最大心率
			float mpr = 0; // 平均心率
			float percentage_count_odi = 0; // 区间内恶性氧减次数与总恶性氧减次数百分比

			for (int j = 0; j < 3; j++) {
				time_interval_start[j] = temp_extract_string_time[index[max3[i]]][j + 1];
				// time_interval_start2
				time_interval_end[j] = temp_extract_string_time[index[max3[i] + 1]][j + 1];
				// time_interval_end2
			}
			count_odi = distribution_ODI_0_85[max3[i]];
			int[] time_temp;
			for (int j = 0; j < temp_duration_ODI4.length; j++) {
				if (temp_duration_ODI4[j][5] >= index[max3[i]] && temp_duration_ODI4[j][5] < index[max3[i] + 1]
						&& temp_aft2_spo2[temp_duration_ODI4[j][5]] <= 85) {
					time_temp = calTimeInterval(temp_extract_string_time[temp_duration_ODI4[j][0]],
							temp_extract_string_time[temp_duration_ODI4[j][3]]);
					time_odi = calTimeSum(time_odi, time_temp);
				}
			}
			// time_odi2
			for (int j = index[max3[i]]; j < index[max3[i] + 1]; j++) {
				mspo2 += temp_aft2_spo2[j];
				mpr += temp_aft_pr[j];
				if (temp_aft_pr[j] > pr_max)
					pr_max = temp_aft_pr[j];
			}
			mspo2 = mspo2 / (index[max3[i] + 1] - index[max3[i]]);
			mpr = mpr / (index[max3[i] + 1] - index[max3[i]]);
			percentage_count_odi = (float) count_odi / distribution_ODI_0_85[len];
			MODII.add(new Param_MODII(time_interval_start, time_interval_start2, time_interval_end, time_interval_end2,
					count_odi, time_odi, time_odi2, mspo2, pr_max, mpr, percentage_count_odi));

			// new Param_MODII(time_interval_start, time_interval_start2,
			// time_interval_end, time_interval_end2, count_odi, time_odi,
			// time_odi2, mspo2, pr_max, mpr, percentage_count_odi).printData();
		}

	}

	/**
	 * 求输入的数组内所有数值的均值
	 * 
	 * @param temp_PI2
	 *            输入数组
	 * @return 平均值
	 */
	private float calAverage(int[] temp_PI2) {
		float sum = 0;
		for (int i : temp_PI2)
			sum += i;
		return (sum / 10) / temp_PI2.length;
	}

	/**
	 * 计算两个时间点之间的时间间隔
	 * 
	 * @param time_former
	 *            前一个时间点，长度为4的数组，各元素的数据格式为：日、时、分、秒
	 * @param time_latter
	 *            后一个时间点，长度为4的数组，各元素的数据格式为：日、时、分、秒
	 * @return 时间间隔，长度为3的数组，各元素的数据格式为：时、分、秒
	 */
	public int[] calTimeInterval(int[] time_former, int[] time_latter) {
		int[] temp = new int[3];
		int[] t = new int[4];
		t[0] = time_latter[0] - time_former[0];
		t[1] = time_latter[1] - time_former[1];
		t[2] = time_latter[2] - time_former[2];
		t[3] = time_latter[3] - time_former[3];

		if (t[3] < 0) {
			t[2]--;
			temp[2] = t[3] + 60;
		} else {
			temp[2] = t[3];
		}

		if (t[2] < 0) {
			t[1]--;
			temp[1] = t[2] + 60;
		} else {
			temp[1] = t[2];
		}

		if (t[0] > 0) {
			temp[0] = 24 + t[1]; // 24小时制
		} else {
			temp[0] = t[1];
		}

		return temp;
	}

	/**
	 * 计算两个时间长度之和
	 * 
	 * @param time1
	 *            需要求和的时间1，长度为3的数组，各元素的数据格式为：时、分、秒
	 * @param time2
	 *            需要求和的时间2，长度为3的数组，各元素的数据格式为：时、分、秒
	 * @return 时间和，长度为3的数组，各元素的数据格式为：时、分、秒
	 */
	public int[] calTimeSum(int[] time1, int[] time2) {
		int[] temp = new int[3];
		temp[0] = time1[0] + time2[0];
		temp[1] = time1[1] + time2[1];
		temp[2] = time1[2] + time2[2];
		formTime(temp);

		return temp;
	}

	/**
	 * 从读取的时间字符串数组中抽取时间数据
	 * 
	 * @param temp_string_time
	 *            原始时间字符串数组
	 * @return 时间数组，各个元素对应的数据格式为：日、时、分、秒
	 */
	public int[][] extractTime(String[] temp_string_time){
		int[][] temp = new int[temp_string_time.length][4];
		for(int i = 0; i < temp_string_time.length; i++){
			String[] str = temp_string_time[i].split(" ");
			temp[i][0] = Integer.parseInt(str[2]);
			temp[i][1] = Integer.parseInt(str[3].split(":")[0]);
			temp[i][2] = Integer.parseInt(str[3].split(":")[1]);
			temp[i][3] = Integer.parseInt(str[3].split(":")[2]);
		}
		time_start = new int[3];
		for(int i = 0; i < 3; i++){
			time_start[i] = temp[0][i+1];
		}
		time_end = new int[3];
		for(int i = 0; i < 3; i++){
			time_end[i] = temp[temp_string_time.length-1][i+1];
		}
		return temp;
	}

	@SuppressWarnings("deprecation")
	public int[][] extractTimeTemp(long[] temp_string_time) {
		Date date;
		int[][] temp = new int[temp_string_time.length][4];
		for (int i = 0; i < temp_string_time.length; i++) {
			date = new Date(temp_string_time[i]);
			temp[i][0] = date.getDate();
			temp[i][1] = date.getHours();
			temp[i][2] = date.getMinutes();
			temp[i][3] = date.getSeconds();
		}
		return temp;
	}

	/**
	 * 判断时间值是否超过上限，并将时间调整为正确值
	 * 
	 * @param temp_string_time
	 *            需要调整的时间，长度为3的数组
	 */
	public void formTime(int[] temp_string_time) {
		if (temp_string_time[2] >= 60) {
			temp_string_time[1] += (temp_string_time[2] / 60);
			temp_string_time[2] = temp_string_time[2] % 60;
		}
		if (temp_string_time[1] >= 60) {
			temp_string_time[0] += (temp_string_time[1] / 60);
			temp_string_time[1] = temp_string_time[1] % 60;
		}
	}

	/**
	 * 计算整晚睡眠持续的时间
	 * 
	 * @param temp_extract_string_time
	 *            时间数组，各个元素对应的数据格式为：日、时、分、秒
	 * @return 睡眠时长，长度为2的数组，各元素的数据格式为：时、分、秒
	 */
	public int[] calLastTime(int[][] temp_extract_string_time) {
		int[] temp = new int[3];
		int[] t = new int[4];
		t[0] = temp_extract_string_time[temp_extract_string_time.length - 1][0] - temp_extract_string_time[0][0];
		t[1] = temp_extract_string_time[temp_extract_string_time.length - 1][1] - temp_extract_string_time[0][1];
		t[2] = temp_extract_string_time[temp_extract_string_time.length - 1][2] - temp_extract_string_time[0][2];
		t[3] = temp_extract_string_time[temp_extract_string_time.length - 1][3] - temp_extract_string_time[0][3];

		if (t[3] < 0) {  // 若保留，则精确到秒，若注释掉，则精确到分
			t[2]--;
			temp[2] = t[3] + 60;
		} else {
			temp[2] = t[3];
		}

		if (t[2] < 0) {
			t[1]--;
			temp[1] = t[2] + 60;
		} else {
			temp[1] = t[2];
		}

		if (t[0] > 0) {
			temp[0] = 24 + t[1]; // 24小时制
		} else {
			temp[0] = t[1];
		}

		return temp;
	}

	/**
	 * 计算整晚睡眠持续的时间
	 * 
	 * @param temp_num_time
	 *            时间数组
	 * @return 睡眠时长，返回值为毫秒数
	 */
	public long calWholeNight(long[] temp_num_time) {
		time_start2 = temp_num_time[0];
		time_end2 = temp_num_time[temp_num_time.length - 1];
		return (time_end2 - time_start2);  // 首尾时间直接相减，可能出现时间对不上的问题
//		return (time_end2/1000/60 - time_start2/1000/60)*1000*60;  // 持续时间等于结束时间减去开始时间，没有时间差，结果精确到分
	}

	/**
	 * 用于测试氧减的检测结果
	 * 
	 * @return
	 */
	public int[] sign_result() {
		int[] temp = new int[temp_bef_spo2.length];
		for (int i = 0; i < temp.length; i++)
			temp[i] = 100;
		for (int i = 0; i < temp_duration_ODI4.length && temp_duration_ODI4[i][1] != 0; i++) {
			for (int j = temp_duration_ODI4[i][0] + 1; j < temp_duration_ODI4[i][3]; j++) {
				temp[j] = 110;
			}
			temp[temp_duration_ODI4[i][1]] = 115;
			temp[temp_duration_ODI4[i][2]] = 115;
		}
		return temp;
	}

	/**
	 * 将多列数据写入到文件中
	 * 
	 * @param outputpath
	 *            输出文件路径
	 */
	public void writeData(String outputpath) {
		try {
			// 取得文件路径
			File f = new File(outputpath);
			// 将数组的数据写入到文件中
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("before" + "," + "after" + "," + "after2" + "," + "temp_data_signed" + "," + "temp_st_ave" + ","
					+ "AI_start" + "," + "AI_valley1" + "," + "AI_valley2" + "," + "AI_end"); // +
																								// ","
																								// +
																								// "apnea_count
																								// =
																								// "
																								// +
																								// AI_count);
			bw.newLine();// 换行
			for (int i = 0; i < temp_bef_spo2.length; i++) {
				bw.write(temp_bef_spo2[i] + "");
				bw.write("," + temp_aft_spo2[i]);
				bw.write("," + temp_aft2_spo2[i]);
				bw.write("," + temp_data_signed[i]);
				bw.write("," + temp_st_ave);
				if (i < temp_duration_AI.length) {
					if (temp_duration_AI[i][1] != 0) {
						bw.write("," + temp_duration_AI[i][0]);
						bw.write("," + temp_duration_AI[i][1]);
						bw.write("," + temp_duration_AI[i][2]);
						bw.write("," + temp_duration_AI[i][3]);
					}
				}
				bw.newLine();// 换行
			}
			bw.flush();
			fw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将多列数据写入到文件中
	 * 
	 * @param outputpath
	 *            输出文件路径
	 */
	public void writeDataTemp(String outputpath) {
		try {
			// 取得文件路径
			File f = new File(outputpath);
			// 将数组的数据写入到文件中
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			int count = 1;
			int temp[] = new int[3];
			bw.write("Number" + "," + "StartTime" + "," + "StopTime" + "," + "Duration" + "," + "Type" + "," + "LSPO2"
					+ "," + "temp_string_time" + "," + "SPO2-1" + "," + "SPO2-2" + "," + "PR-Low" + "," + "PR-1" + ","
					+ "PR-2");
			bw.newLine();// 换行
			for (int i = 0; i < temp_duration_AI.length && temp_duration_AI[i][1] != 0; i++) {
				bw.write(count + "");
				count++;
				bw.write("," + temp_extract_string_time[temp_duration_AI[i][0]][1] + ":"
						+ temp_extract_string_time[temp_duration_AI[i][0]][2] + ":"
						+ temp_extract_string_time[temp_duration_AI[i][0]][3]);
				bw.write("," + temp_extract_string_time[temp_duration_AI[i][3]][1] + ":"
						+ temp_extract_string_time[temp_duration_AI[i][3]][2] + ":"
						+ temp_extract_string_time[temp_duration_AI[i][3]][3]);
				temp = calTimeInterval(temp_extract_string_time[temp_duration_AI[i][0]],
						temp_extract_string_time[temp_duration_AI[i][3]]);
				bw.write("," + temp[0] + ":" + temp[1] + ":" + temp[2]);
				bw.write("," + "APN");
				bw.write("," + temp_aft_spo2[temp_duration_AI[i][1]]);
				bw.write("," + temp_extract_string_time[temp_duration_AI[i][1]][1] + ":"
						+ temp_extract_string_time[temp_duration_AI[i][1]][2] + ":"
						+ temp_extract_string_time[temp_duration_AI[i][1]][3]);
				bw.write("," + temp_aft_spo2[temp_duration_AI[i][0]]);
				bw.write("," + temp_aft_spo2[temp_duration_AI[i][3]]);
				bw.write("," + temp_bef_pr[temp_duration_AI[i][1]]);
				bw.write("," + temp_bef_pr[temp_duration_AI[i][0]]);
				bw.write("," + temp_bef_pr[temp_duration_AI[i][3]]);
				bw.newLine();// 换行
			}
			for (int i = 0; i < temp_duration_HI.length && temp_duration_HI[i][1] != 0; i++) {
				bw.write(count + "");
				count++;
				bw.write("," + temp_extract_string_time[temp_duration_HI[i][0]][1] + ":"
						+ temp_extract_string_time[temp_duration_HI[i][0]][2] + ":"
						+ temp_extract_string_time[temp_duration_HI[i][0]][3]);
				bw.write("," + temp_extract_string_time[temp_duration_HI[i][3]][1] + ":"
						+ temp_extract_string_time[temp_duration_HI[i][3]][2] + ":"
						+ temp_extract_string_time[temp_duration_HI[i][3]][3]);
				temp = calTimeInterval(temp_extract_string_time[temp_duration_HI[i][0]],
						temp_extract_string_time[temp_duration_HI[i][3]]);
				bw.write("," + temp[0] + ":" + temp[1] + ":" + temp[2]);
				bw.write("," + "HYP");
				bw.write("," + temp_aft_spo2[temp_duration_HI[i][1]]);
				bw.write("," + temp_extract_string_time[temp_duration_HI[i][1]][1] + ":"
						+ temp_extract_string_time[temp_duration_HI[i][1]][2] + ":"
						+ temp_extract_string_time[temp_duration_HI[i][1]][3]);
				bw.write("," + temp_aft_spo2[temp_duration_HI[i][0]]);
				bw.write("," + temp_aft_spo2[temp_duration_HI[i][3]]);
				bw.write("," + temp_bef_pr[temp_duration_HI[i][1]]);
				bw.write("," + temp_bef_pr[temp_duration_HI[i][0]]);
				bw.write("," + temp_bef_pr[temp_duration_HI[i][3]]);
				bw.newLine();// 换行
			}
			for (int i = 0; i < temp_duration_ODI4.length && temp_duration_ODI4[i][1] != 0; i++) {
				bw.write(count + "");
				count++;
				bw.write("," + temp_extract_string_time[temp_duration_ODI4[i][0]][1] + ":"
						+ temp_extract_string_time[temp_duration_ODI4[i][0]][2] + ":"
						+ temp_extract_string_time[temp_duration_ODI4[i][0]][3]);
				bw.write("," + temp_extract_string_time[temp_duration_ODI4[i][3]][1] + ":"
						+ temp_extract_string_time[temp_duration_ODI4[i][3]][2] + ":"
						+ temp_extract_string_time[temp_duration_ODI4[i][3]][3]);
				temp = calTimeInterval(temp_extract_string_time[temp_duration_ODI4[i][0]],
						temp_extract_string_time[temp_duration_ODI4[i][3]]);
				bw.write("," + temp[0] + ":" + temp[1] + ":" + temp[2]);
				bw.write("," + "ODI");
				bw.write("," + temp_aft_spo2[temp_duration_ODI4[i][1]]);
				bw.write("," + temp_extract_string_time[temp_duration_ODI4[i][1]][1] + ":"
						+ temp_extract_string_time[temp_duration_ODI4[i][1]][2] + ":"
						+ temp_extract_string_time[temp_duration_ODI4[i][1]][3]);
				bw.write("," + temp_aft_spo2[temp_duration_ODI4[i][0]]);
				bw.write("," + temp_aft_spo2[temp_duration_ODI4[i][3]]);
				bw.write("," + temp_bef_pr[temp_duration_ODI4[i][1]]);
				bw.write("," + temp_bef_pr[temp_duration_ODI4[i][0]]);
				bw.write("," + temp_bef_pr[temp_duration_ODI4[i][3]]);
				bw.newLine();// 换行
			}
			for (int i = 0; i < temp_waveData.length && temp_waveData[i][1] != 0; i++) {
				bw.write(count + "");
				count++;
				bw.write("," + temp_extract_string_time[temp_waveData[i][0]][1] + ":"
						+ temp_extract_string_time[temp_waveData[i][0]][2] + ":"
						+ temp_extract_string_time[temp_waveData[i][0]][3]);
				bw.write("," + temp_extract_string_time[temp_waveData[i][2]][1] + ":"
						+ temp_extract_string_time[temp_waveData[i][2]][2] + ":"
						+ temp_extract_string_time[temp_waveData[i][2]][3]);
				temp = calTimeInterval(temp_extract_string_time[temp_waveData[i][0]],
						temp_extract_string_time[temp_waveData[i][2]]);
				bw.write("," + temp[0] + ":" + temp[1] + ":" + temp[2]);
				bw.write("," + "PRA");
				bw.write("," + temp_aft_spo2[temp_waveData[i][1]]);
				bw.write("," + temp_extract_string_time[temp_waveData[i][1]][1] + ":"
						+ temp_extract_string_time[temp_waveData[i][1]][2] + ":"
						+ temp_extract_string_time[temp_waveData[i][1]][3]);
				bw.write("," + temp_aft_spo2[temp_waveData[i][0]]);
				bw.write("," + temp_aft_spo2[temp_waveData[i][2]]);
				bw.write("," + temp_bef_pr[temp_waveData[i][1]]);
				bw.write("," + temp_bef_pr[temp_waveData[i][0]]);
				bw.write("," + temp_bef_pr[temp_waveData[i][2]]);
				bw.newLine();// 换行
			}
			bw.flush();
			fw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// binary
	public void read_binary(String filepath, String temp_seperator){
		final int data_length = 21; //1+1+1+1+4+1+1+1+1+8+1 = 21
		RandomAccessFile raf;
		int len;
		try {
			raf = new RandomAccessFile(filepath, "r");
			len = (int) (raf.length() / data_length);

			if (flag_five == false) {
				temp_bef_pr = new int[len];
				temp_bef_spo2 = new int[len];
				temp_string_time = new String[len];
				temp_num_time = new long[len];
				temp_PI = new int[len];
				for (int i = 0; i < len; i++) {
					raf.seek(i * data_length);
					temp_bef_pr[i] = 0xFF & raf.readByte();
					temp_bef_spo2[i] = 0xFF & raf.readByte();
					temp_PI[i] = 0xFF & raf.readByte();
					raf.readByte();
					raf.readInt();
					raf.readByte();
					raf.readByte();
					raf.readByte();
					raf.readByte();
					temp_num_time[i] = raf.readLong();
//					System.out.println("temp_num_time[" + i + "] = " + temp_num_time[i]);
					raf.readByte();
					temp_string_time[i] = in.format(new Date(temp_num_time[i]));
//					System.out.println("temp_string_time[" + i + "] = " + temp_string_time[i]);
				}
			} else {
				temp_bef_pr = new int[len - 360];
				temp_bef_spo2 = new int[len - 360];
				temp_string_time = new String[len - 360];
				temp_num_time = new long[len - 360];
				temp_PI = new int[len - 360];
				for (int i = 0; i < len - 360; i++) {
					raf.seek((i + 300) * data_length);
					temp_bef_pr[i] = 0xFF & raf.readByte();
					temp_bef_spo2[i] = 0xFF & raf.readByte();
					temp_PI[i] = 0xFF & raf.readByte();
					raf.readByte();
					raf.readInt();
					raf.readByte();
					raf.readByte();
					raf.readByte();
					raf.readByte();
					temp_num_time[i] = raf.readLong();
//					System.out.println("temp_num_time[" + i + "] = " + temp_num_time[i]);
					raf.readByte();
					temp_string_time[i] = in.format(new Date(temp_num_time[i]));
//					System.out.println("temp_string_time[" + i + "] = " + temp_string_time[i]);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 读取多行数据，根据数据的行数定义接收数组的大小（一个文件多列数据）；
	 * 从一个文件中同时读取脉率值、血氧值和时间数据，如果测量时间大于10分钟，则计算时舍去前5分钟的数据(300个数据点)
	 * 
	 * @param filepath
	 *            目标文件的路径
	 */
	public void readMultiData_1_time_pr_spo2_5min_fromTempFile(String filepath, String temp_seperator) {
		// 读取数据
		int num = lineCountTotal(filepath, temp_seperator); // 所读取的数据的行数
		String strbuff;
		String[] strcol;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
			strbuff = br.readLine(); // 去掉第一行

			if (flag_five == false) {
				temp_bef_pr = new int[num];
				temp_bef_spo2 = new int[num];
				temp_string_time = new String[num];
				temp_num_time = new long[num];
				temp_PI = new int[num];
				for (int i = 0; i < num; i++) {
					strbuff = br.readLine();
					strcol = strbuff.split(",");
					temp_bef_pr[i] = Integer.valueOf(strcol[0]);
					temp_bef_spo2[i] = Integer.valueOf(strcol[1]);
					temp_num_time[i] = Long.valueOf(strcol[3]);
					temp_string_time[i] = in.format(new Date(temp_num_time[i]));
					temp_PI[i] = Integer.valueOf(strcol[4]);
				}
			} else {
				temp_bef_pr = new int[num - 360];
				temp_bef_spo2 = new int[num - 360];
				temp_string_time = new String[num - 360];
				temp_num_time = new long[num - 360];
				temp_PI = new int[num - 360];
				for (int i = 0; i < 300; i++) {
					br.readLine();
				}
				for (int i = 0; i < num - 360; i++) {
					strbuff = br.readLine();
					strcol = strbuff.split(",");
					temp_bef_pr[i] = Integer.valueOf(strcol[0]);
					temp_bef_spo2[i] = Integer.valueOf(strcol[1]);
					temp_num_time[i] = Long.valueOf(strcol[3]);
					temp_string_time[i] = in.format(new Date(temp_num_time[i]));
					temp_PI[i] = Integer.valueOf(strcol[4]);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取多行多列数据（一个文件多列数据）；
	 * 从一个文件中同时读取脉率值、血氧值和时间数据，如果测量时间大于10分钟，则计算时舍去前5分钟的数据(300个数据点)；
	 * 数据文件中存在分隔符，用于计算的数据为最后一个分隔符之后的数据
	 * @param filepath 目标文件的路径
	 * @param temp_seperator 分隔符
	 */
	public void readMultiData_1_time_pr_spo2_5min_fromTempFileNewest(String filepath, String temp_seperator){
        //读取数据
        int count_seperator = 0;  //统计分隔符的个数
        int count_newdata = 0;  //统计最新数据的行数
        String strbuff;
        String[] strcol;
        try {
	        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
	        strbuff = br.readLine();  //去掉第一行
	        while ((strbuff = br.readLine()) != null) { // readLine()方法是按行读的，返回值是这行的内容
	        	if(!strbuff.equals(temp_seperator)){
	        		count_newdata++;
	        	}else{
	        		count_newdata = 0;
	        		count_seperator++;
	        	}
	        }
	        br.close();
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
	        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
	        strbuff = br.readLine();  //去掉第一行
	        int temp_count_seperator = 0;
	        
	        while(temp_count_seperator < count_seperator){
				strbuff = br.readLine();
				if(strbuff.equals(temp_seperator)) temp_count_seperator++;
			}
	        if(flag_five == false){
		        temp_bef_pr = new int[count_newdata];
		        temp_bef_spo2 = new int[count_newdata];
				temp_string_time = new String[count_newdata];
				temp_num_time = new long[count_newdata];
				temp_PI = new int[count_newdata];
				for (int i = 0; i < count_newdata; i++) {
		            strbuff = br.readLine();
		            strcol = strbuff.split(",");
		            temp_bef_pr[i] = Integer.valueOf(strcol[0]);
		            temp_bef_spo2[i] = Integer.valueOf(strcol[1]);
					temp_num_time[i] = Long.valueOf(strcol[3]);
					temp_string_time[i] = in.format(new Date(temp_num_time[i]));
					temp_PI[i] = Integer.valueOf(strcol[4]);
		        }
			}else{
				temp_bef_pr = new int[count_newdata-360];
		        temp_bef_spo2 = new int[count_newdata-360];
				temp_string_time = new String[count_newdata-360];
				temp_num_time = new long[count_newdata-360];
				temp_PI = new int[count_newdata-360];
				for (int i = 0; i < 300; i++) {
					br.readLine();
		        }
				for (int i = 0; i < count_newdata-360; i++) {
		            strbuff = br.readLine();
		            strcol = strbuff.split(",");
		            temp_bef_pr[i] = Integer.valueOf(strcol[0]);
		            temp_bef_spo2[i] = Integer.valueOf(strcol[1]);
					temp_num_time[i] = Long.valueOf(strcol[3]);
					temp_string_time[i] = in.format(new Date(temp_num_time[i]));
					temp_PI[i] = Integer.valueOf(strcol[4]);
		        }
			}
	        br.close();
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 读取多行多列数据（一个文件多列数据）；
	 * 从一个文件中同时读取脉率值、血氧值和时间数据，如果测量时间大于10分钟，则计算时舍去前5分钟的数据(300个数据点)；
	 * 数据文件中存在分隔符，用于计算的数据为最后一个分隔符之后的数据
	 * @param filepath 目标文件的路径
	 * @param temp_seperator 分隔符
	 */
	public void readMultiData_1_time_pr_spo2_5min_fromTempFileTotal(String filepath, String temp_seperator){
        //读取数据
        int dataCount = lineCountTotal(filepath, temp_seperator); //所读取的数据的行数
        String strbuff;
        String[] strcol;
        int[] temp_dataCount = timeLengthCount(filepath, temp_seperator);
        
        try {
	        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
	        strbuff = br.readLine();  //去掉第一行
	        
	        if(temp_dataCount == null){
	        	temp_bef_pr = new int[dataCount];
				temp_bef_spo2 = new int[dataCount];
				temp_PI = new int[dataCount];
				temp_string_time = new String[dataCount];
				temp_num_time = new long[dataCount];
				press=new int[dataCount];
				axis_x=new int[dataCount];
				axis_y=new int[dataCount];
				axis_z=new int[dataCount];
		        int i = 0;
				while((strbuff = br.readLine()) != null && i < dataCount){
					if(!temp_seperator.equals(strbuff)){
						strcol = strbuff.split(",");
						temp_bef_pr[i] = Integer.valueOf(strcol[0]);
			            temp_bef_spo2[i] = Integer.valueOf(strcol[1]);
						temp_num_time[i] = Long.valueOf(strcol[9]);
						press[i]=Integer.valueOf(strcol[11]);
						axis_x[i]=Integer.valueOf(strcol[12]);
						axis_y[i]=Integer.valueOf(strcol[13]);
						axis_z[i]=Integer.valueOf(strcol[14]);
						temp_string_time[i] = in.format(new Date(temp_num_time[i]));

//						temp_string_time[i] = strcol[3];
//						try {
//							temp_num_time[i] = in.parse(strcol[3]).getTime();
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
						temp_PI[i] = Integer.valueOf(strcol[2]);
						i++;
					}
				}
			}else{
				int length = dataCount - temp_dataCount[0] - temp_dataCount[1];
				temp_bef_pr = new int[length];
				temp_bef_spo2 = new int[length];
				temp_PI = new int[length];
				temp_string_time = new String[length];
				temp_num_time = new long[length];
				press=new int[length];
				axis_x=new int[length];
				axis_y=new int[length];
				axis_z=new int[length];
				for (int i = 0; i < temp_dataCount[0]; i++) {
					br.readLine();
		        }
				int i = 0;
				while((strbuff = br.readLine()) != null && i < length){
					if(!temp_seperator.equals(strbuff)){  //写在while里会出错？？？
						strcol = strbuff.split(",");
						temp_bef_pr[i] = Integer.valueOf(strcol[0]);
			            temp_bef_spo2[i] = Integer.valueOf(strcol[1]);
						temp_num_time[i] = Long.valueOf(strcol[9]);
						press[i]=Integer.valueOf(strcol[11]);
						axis_x[i]=Integer.valueOf(strcol[12]);
						axis_y[i]=Integer.valueOf(strcol[13]);
						axis_z[i]=Integer.valueOf(strcol[14]);
						temp_string_time[i] = in.format(new Date(temp_num_time[i]));

//						temp_string_time[i] = strcol[3];
//						try {
//							temp_num_time[i] = in.parse(strcol[3]).getTime();
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
						temp_PI[i] = Integer.valueOf(strcol[2]);
						i++;
					}
				}
			}
	        br.close();
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 读取多行数据，根据数据的行数定义接收数组的大小（一个文件多列数据）；
	 * 从一个文件中同时读取脉率值、血氧值和时间数据，如果测量时间大于10分钟，则计算时舍去前5分钟的数据(300个数据点)
	 * 
	 * @param filepath
	 *            目标文件的路径
	 */
	public void readMultiData_1_time_pr_spo2_5min(String filepath, String temp_seperator) {
		// 读取数据
		int num = lineCountTotal(filepath, temp_seperator); // 所读取的数据的行数
		String strbuff;
		String[] strcol;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
			strbuff = br.readLine(); // 去掉第一行

			if (flag_five == false) {
				temp_bef_pr = new int[num];
				temp_bef_spo2 = new int[num];
				temp_PI = new int[num];
				temp_string_time = new String[num];
				temp_num_time = new long[num];
				for (int i = 0; i < num; i++) {
					strbuff = br.readLine();
					strcol = strbuff.split(",");
					temp_bef_pr[i] = Integer.valueOf(strcol[0]);
					temp_bef_spo2[i] = Integer.valueOf(strcol[2]);
					temp_PI[i] = Integer.valueOf(strcol[4]);
					temp_string_time[i] = strcol[7];
					if (strcol.length > 13) {
						temp_num_time[i] = Long.valueOf(strcol[13]);
					}
				}
			} else {
				temp_bef_pr = new int[num - 360];
				temp_bef_spo2 = new int[num - 360];
				temp_PI = new int[num - 360];
				temp_string_time = new String[num - 360];
				temp_num_time = new long[num - 360];
				for (int i = 0; i < 300; i++) {
					br.readLine();
				}
				for (int i = 0; i < num - 360; i++) {
					strbuff = br.readLine();
					strcol = strbuff.split(",");
					temp_bef_pr[i] = Integer.valueOf(strcol[0]);
					temp_bef_spo2[i] = Integer.valueOf(strcol[2]);
					temp_PI[i] = Integer.valueOf(strcol[4]);
					temp_string_time[i] = strcol[7];
					if (strcol.length > 13) {
						temp_num_time[i] = Long.valueOf(strcol[13]);
					}
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 计算文件内最新数据的行数，并返回该数值
	 * 
	 * @param filepath
	 *            文件的路径
	 * @return 最新数据的行数
	 */
	public static int lineCountNewest(String filepath, String temp_seperator) {
		int count = 0; // 用于统计行数，从0开始
		String strbuff = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
			br.readLine(); // 去掉第一行
			while ((strbuff = br.readLine()) != null) { // readLine()方法是按行读的，返回值是这行的内容
				count++; // 每读一行，则变量累加1
				if (temp_seperator.equals(strbuff))
					count = 0; // 如果读到分隔符，则重置计数
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 计算文件内数据的行数，并返回该数值
	 * @param filepath 目标文件的路径
	 * @return 文件内数据的行数
	 */
	public int lineCountTotal(String filepath, String temp_seperator){
		int count = 0; // 用于统计行数，从0开始
		String strbuff = null;
        try {
        	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
	        br.readLine();  //去掉第一行
			while ((strbuff = br.readLine()) != null) { // readLine()方法是按行读的，返回值是这行的内容
				if(!temp_seperator.equals(strbuff)) count++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 计算测量总时间，如果大于10分钟，则计算开始5分钟和最后1分钟对应的数据个数
	 * @param filepath 文件的路径
	 * @param temp_seperator 分隔符
	 * @return 长度位2的数组
	 */
	public int[] timeLengthCount(String filepath, String temp_seperator){
		int[] result = new int[2];  //返回的结果，如果测量时间大于10分钟，结果为开头需要舍去的数据个数和结尾需要舍去的数据个数，否则为空
		int lineCount = lineCountTotal(filepath, temp_seperator);
		long tempTimeData[] = new long[lineCount];
		int index = 0;
		String strbuff = null;
        try {
        	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
	        br.readLine();  //去掉第一行
			while ((strbuff = br.readLine()) != null) { // readLine()方法是按行读的，返回值是这行的内容
				if(!temp_seperator.equals(strbuff)) {
					tempTimeData[index] = Long.valueOf(strbuff.split(",")[0]);
					index++;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        // 判断测量时间是否大于10分钟（600000毫秒）
        if((tempTimeData[lineCount-1] - tempTimeData[0]) > 600000){
        	//计算开始5分钟的数据行数
        	index = 1;
        	while(tempTimeData[index] - tempTimeData[0] < 300000){
        		index++;
        	}
        	result[0] = index;
        	//计算最后1分钟的数据行数
        	index = lineCount - 2;
        	while(tempTimeData[lineCount-1] - tempTimeData[index] < 60000){
        		index--;
        	}
        	result[1] = lineCount - 1 - index;
        }else{
        	result = null;
        }
		return result;
	}

	public int[] getTime_wholeNight() {
		return time_wholeNight;
	}

	public int[] getTime_start() {
		return time_start;
	}

	public int[] getTime_end() {
		return time_end;
	}

	public float getAHI() {
		return AHI;
	}

	public int getAI_count() {
		return AI_count;
	}

	public float getAI_index() {
		return AI_index;
	}

	public int getHI_count() {
		return HI_count;
	}

	public float getHI_index() {
		return HI_index;
	}

	public int getODI4_count() {
		return ODI4_count;
	}

	public float getODI4_index() {
		return ODI4_index;
	}

	public int getHSPO2() {
		return HSPO2;
	}

	public int getLSPO2() {
		return LSPO2;
	}

	public float getMSPO2() {
		return MSPO2;
	}

	public float getTS90() {
		return TS90;
	}

	public float getODHI() {
		return ODHI;
	}

	public float getPI() {
		return PI;
	}

	public long getTime_start2() {
		return time_start2;
	}

	public long getTime_end2() {
		return time_end2;
	}

	public long getTime_wholeNight2() {
		return time_wholeNight2;
	}

	public int[] getAI_longest_duration() {
		return AI_longest_duration;
	}

	public int[] getAI_longest_timePoint() {
		return AI_longest_timePoint;
	}

	public int[] getAI_time_sum() {
		return AI_time_sum;
	}

	public float getAI_time_percent() {
		return AI_time_percent;
	}

	public int getAI_longest_spo2() {
		return AI_longest_spo2;
	}

	public long getAI_longest_duration2() {
		return AI_longest_duration2;
	}

	public long getAI_longest_timePoint2() {
		return AI_longest_timePoint2;
	}

	public long getAI_time_sum2() {
		return AI_time_sum2;
	}

	public float getAI_time_percent2() {
		return AI_time_percent2;
	}

	public int[] getHI_longest_duration() {
		return HI_longest_duration;
	}

	public int[] getHI_longest_timePoint() {
		return HI_longest_timePoint;
	}

	public int[] getHI_time_sum() {
		return HI_time_sum;
	}

	public float getHI_time_percent() {
		return HI_time_percent;
	}

	public int getHI_longest_spo2() {
		return HI_longest_spo2;
	}

	public long getHI_longest_duration2() {
		return HI_longest_duration2;
	}

	public long getHI_longest_timePoint2() {
		return HI_longest_timePoint2;
	}

	public long getHI_time_sum2() {
		return HI_time_sum2;
	}

	public float getHI_time_percent2() {
		return HI_time_percent2;
	}

	public int[] getODI4_longest_duration() {
		return ODI4_longest_duration;
	}

	public int[] getODI4_longest_timePoint() {
		return ODI4_longest_timePoint;
	}

	public int[] getODI4_largest_duration() {
		return ODI4_largest_duration;
	}

	public int[] getODI4_largest_timePoint() {
		return ODI4_largest_timePoint;
	}

	public int getODI4_largest_range() {
		return ODI4_largest_range;
	}

	public long getODI4_longest_duration2() {
		return ODI4_longest_duration2;
	}

	public long getODI4_longest_timePoint2() {
		return ODI4_longest_timePoint2;
	}

	public long getODI4_largest_duration2() {
		return ODI4_largest_duration2;
	}

	public long getODI4_largest_timePoint2() {
		return ODI4_largest_timePoint2;
	}

	public int[] getDistribution_90_100() {
		return distribution_90_100;
	}

	public int[] getDistribution_80_90() {
		return distribution_80_90;
	}

	public int[] getDistribution_70_80() {
		return distribution_70_80;
	}

	public int[] getDistribution_60_70() {
		return distribution_60_70;
	}

	public int[] getDistribution_50_60() {
		return distribution_50_60;
	}

	public int[] getDistribution_0_50() {
		return distribution_0_50;
	}

	public long[] getDistribution_90_1002() {
		return distribution_90_1002;
	}

	public long[] getDistribution_80_902() {
		return distribution_80_902;
	}

	public long[] getDistribution_70_802() {
		return distribution_70_802;
	}

	public long[] getDistribution_60_702() {
		return distribution_60_702;
	}

	public long[] getDistribution_50_602() {
		return distribution_50_602;
	}

	public long[] getDistribution_0_502() {
		return distribution_0_502;
	}

	public int[] getDistribution_ODI_0_85() {
		return distribution_ODI_0_85;
	}

	public float getSODHI() {
		return SODHI;
	}

	public ArrayList<Param_MODII> getMODII() {
		return MODII;
	}
	public int getAi_count() {
		return ai_count;
	}
	public int getHi_count() {
		return hi_count;
	}
}

