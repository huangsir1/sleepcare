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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.sleep.utils.Utils;

import android.util.Log;

/**
 * 采用标准差的方法，通过计算脉率的标准差，结合脉率的变化信息，计算睡眠分期
 */
public class Sleep_stageSeperate {

	//计算所需的临时变量
	private DateFormat in = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
	private boolean flag_five = false;  //判断是否需要舍去前五分钟的数据，true为舍去，false为不舍去
	private boolean flag_time_dataType = false;  //true:用long类型的数据计算时间，false:用String类型的数据计算时间
	private float packagelength = 100;  //设备发送的数据包的长度
	private int p = 180;    //去噪的窗口大小
	private int p2 = 20;    //去噪的窗口大小
	private int pace = 60;    //计算标准差的窗口大小
	private String[] temp_string_time;    //与RR间期值对应的时间
	private int[][] temp_extract_string_time;    //从字符串中提取的时间数据，格式为：日、时、分、秒
	private long[] temp_num_time;    //与RR间期值对应的时间
	private int[][] temp_stdev_string_time;    //与每个标准差对应的时间，格式为：时、分、秒
	private long[] temp_stdev_num_time;    //与每个标准差对应的时间
	private float[] temp_bef_RR;    //获取的RR间期
	private float[] temp_aft_ML;    //RR间期去噪后转化为脉率
	private float[] temp_stdev;    //标准差
	private float temp_stdev_average = 0;    //数组stdev的均值
	private float[] temp_stdev_pr_ave;    //与标准差对应的脉率均值
	private float temp_ave_average = 0;    //数组ave中所有元素的均值
	private float temp_ave_max = 0;    //数组ave中最大的元素
	private float temp_ave_min = 0;    //数组ave中最小的元素
	private int[] temp_stage;    //数字化的分期，清醒为3，浅睡为2，深睡为1
	
	//需要输出的结果
	private int[] time_start;    //睡眠测量起始时间
	private int[] time_end;    //睡眠测量终止时间
	private int[] time_last;    //整晚睡眠持续时间，格式为：时、分、秒
	private int[] time_wake;    //清醒时间，格式为：时、分、秒
	private int[] time_light;    //浅睡时间，格式为：时、分、秒
	private int[] time_deep;    //深睡时间，格式为：时、分、秒
	private float pr_average = 0;    //脉率平均值
	private int pr_max = 0;    //脉率最大值
	private int[] pr_max_timePoint = new int[3];    //脉率最大值对应的时间点，时间格式为：时、分、秒
	private int pr_min = 0;    //脉率最小值
	private int[] pr_min_timePoint = new int[3];    //脉率最小值对应的时间点，时间格式为：时、分、秒
	//由long类型时间计算所得
	private long time_start2 = 0;    //睡眠测量起始时间
	private long time_end2 = 0;    //睡眠测量终止时间
	private long time_last2 = 0;    //整晚睡眠持续时间，值为总毫秒数
	private long time_wake2 = 0;    //清醒时间，值为总毫秒数
	private long time_light2 = 0;    //浅睡时间，值为总毫秒数
	private long time_deep2 = 0;    //深睡时间，值为总毫秒数
	private long pr_max_timePoint2 = 0;    //脉率最大值对应的时间点
	private long pr_min_timePoint2 = 0;    //脉率最小值对应的时间点
	
	/**
	 * 从一个文件中获取原始数据并完成计算
	 * @param inputpath 数据文件所在文件夹的路径
	 * @param flag_five1 true：忽略开始的5分钟（300个点）数据和最后1分钟（60个点）数据，false：使用完整数据
	 * @param timeFlag1 true：使用long类型的数据计算时间，false：使用String类型的数据计算时间
	 * @return true：计算完毕
	 */
	public boolean calculate(String inputpath, boolean flag_five1, boolean flag_time_dataType1, String temp_seperator, int dataType){
		flag_five = flag_five1;
		flag_time_dataType = flag_time_dataType1;
		// 从TXT文件中读取数据
		if(dataType == Utils.DATATYPE_ASCII){
			readMultiData_1_time_pr_spo2_5min_fromTempFileTotal(inputpath, temp_seperator);
		}else if(dataType == Utils.DATATYPE_BINARY){
			read_binary(inputpath, temp_seperator);
		}
		Log.i("stage", "finish");
//		if(flag_time_dataType){
			//计算睡眠起止时间和持续时间
			calLastTime2(temp_num_time);
			//过滤血氧数据
			temp_aft_ML = denoisingRR(temp_bef_RR, p);
			//计算标准差、脉率最大值、最小值、平均值等数据
			temp_stdev = cal_stdev2(temp_aft_ML, temp_num_time, pace);
			//计算睡眠分期
			temp_stage = cal_stage2(temp_stdev, temp_stdev_average, temp_stdev_pr_ave, temp_ave_average, temp_stdev_num_time, temp_ave_min, temp_ave_max);
//		}else{
			//计算睡眠起止时间和持续时间
			temp_extract_string_time = extractTime(temp_string_time);
			time_last = calLastTime(temp_extract_string_time);
			//过滤血氧数据
			temp_aft_ML = denoisingRR(temp_bef_RR, p);
			//计算标准差、脉率最大值、最小值、平均值等数据
			temp_stdev = cal_stdev(temp_aft_ML, temp_extract_string_time, pace);
			//计算睡眠分期
			temp_stage = cal_stage(temp_stdev, temp_stdev_average, temp_stdev_pr_ave, temp_ave_average, temp_stdev_string_time, temp_ave_min, temp_ave_max);
//		}
		
//		formTimeSecond(time_start);
//		formTimeSecond(time_end);
//		formTimeSecond(time_last);
//		formTimeSecond(time_wake);
//		formTimeSecond(time_light);
//		formTimeSecond(time_deep);
			
		//清空临时变量
		temp_string_time = null;    //与RR间期值对应的时间
		temp_extract_string_time = null;    //从字符串中提取的时间数据，格式为：日、时、分、秒
		temp_num_time = null;    //与RR间期值对应的时间
		temp_stdev_string_time = null;    //与每个标准差对应的时间，格式为：时、分、秒
		temp_stdev_num_time = null;    //与每个标准差对应的时间
		temp_bef_RR = null;    //获取的RR间期
		temp_aft_ML = null;    //RR间期去噪后转化为脉率
		temp_stdev = null;    //标准差
		temp_stdev_average = 0;    //数组stdev的均值
		temp_stdev_pr_ave = null;    //与标准差对应的脉率均值
		temp_ave_average = 0;    //数组ave中所有元素的均值
		temp_ave_max = 0;    //数组ave中最大的元素
		temp_ave_min = 0;    //数组ave中最小的元素
		temp_stage = null;    //数字化的分期，清醒为3，浅睡为2，深睡为1
		return true;
	}
	
	/**
	 * 对脉率数据去噪的方法(设定脉率数值的整体上下限，以及局部波动上下限，不符合条件的数据取均值)
	 * @param temp_bef_RR 需要去噪的数据
	 * @param p 每次去噪截取的区间大小
	 * @return 去噪后的脉率数据
	 */
	public float[] denoisingML(float[] temp_bef_RR, int p){
		int bef_len = temp_bef_RR.length;
		float[] temp = new float[bef_len];    //接收bef数组
		for(int i = 0; i < bef_len; i++){
			temp[i] = temp_bef_RR[i];
		}
		//整体去噪
		float sum = 0;
		float temp_stdev_pr_ave = 0;
		for(float i : temp){
			sum += i;
		}
		temp_stdev_pr_ave = sum / bef_len;
		for(int i = 0; i <bef_len; i++){
			if(temp[i] < 40 || temp[i]> 180){
				temp[i] = temp_stdev_pr_ave;
			}
		}
		sum = 0;
		temp_stdev_pr_ave = 0;
		int pace = p;    //去噪计算的数据长度
		int pace2 = bef_len%pace;
		for(int i = 0; i <bef_len; i++){
			sum += temp[i];
			if((i+1)%pace == 0){
				temp_stdev_pr_ave = sum/pace;
				for(int j = 0; j < pace; j++){
					if(temp[i - j] - temp_stdev_pr_ave > 25 || temp[i - j] - temp_stdev_pr_ave < -25){
						temp[i - j] = temp_stdev_pr_ave;
					}
				}
			sum = 0;
			}else if((i+1) == bef_len){
				temp_stdev_pr_ave = sum/(pace2);
				for(int j = 0; j < pace2; j++){
					if(temp[i - j] - temp_stdev_pr_ave > 25 || temp[i - j] - temp_stdev_pr_ave < -25){
						temp[i - j] = temp_stdev_pr_ave;
					}
				}
				sum = 0;
			}
		}
		return temp;
	}
	
	/**
	 * 对RR间期数据去噪的方法(设定RR间期数值的整体上下限，以及局部波动上下限，不符合条件的数据取均值)，并将去噪后的RR间期转换为脉率值
	 * @param temp_bef_RR 需要去噪的数据
	 * @param p 每次去噪截取的区间大小
	 * @return 去噪后的脉率数据
	 */
	public float[] denoisingRR(float[] temp_bef_RR, int p){
		int bef_len = temp_bef_RR.length;
		float[] temp = new float[bef_len];    //接收数据的临时数组
		for(int i = 0; i < bef_len; i++){
			temp[i] = temp_bef_RR[i];
		}
		//整体去噪
		float sum = 0;
		float temp_stdev_pr_ave = 0;
		for(float i : temp){
			sum += i;
		}
		temp_stdev_pr_ave = sum / bef_len;
		for(int i = 0; i <bef_len; i++){
			if(temp[i] < 0.33 || temp[i]> 1.5){
				temp[i] = temp_stdev_pr_ave;
			}
		}
		//局部去噪
		sum = 0;
		temp_stdev_pr_ave = 0;
		int pace = p;    //去噪计算的数据长度
		int pace2 = bef_len%pace;    //剩余的，不足窗口大小的数据
		for(int i = 0; i <bef_len; i++){
			sum += temp[i];
			if((i+1)%pace == 0){
				temp_stdev_pr_ave = sum/pace;
				for(int j = 0; j < pace; j++){
					if(temp[i - j] - temp_stdev_pr_ave > 0.33 || temp[i - j] - temp_stdev_pr_ave < -0.33){
						temp[i - j] = temp_stdev_pr_ave;
					}
				}
			sum = 0;
			}else if((i+1) == bef_len){
				temp_stdev_pr_ave = sum/(pace2);
				for(int j = 0; j < pace2; j++){
					if(temp[i - j] - temp_stdev_pr_ave > 0.33 || temp[i - j] - temp_stdev_pr_ave < -0.33){
						temp[i - j] = temp_stdev_pr_ave;
					}
				}
				sum = 0;
			}
		}
		//将RR间期转化为脉率
		for(int i = 0; i < temp.length; i++){
			if(temp[i] > 0){
				temp[i] = 60 / temp[i];
			}else{
				temp[i] = 255;
			}
		}
		return temp;
	}

	/**
	 * 对RR间期数据去噪的方法(设定RR间期数值的整体上下限，以及局部波动上下限，不符合条件的数据取均值)，并将去噪后的RR间期转换为脉率值
	 * @param temp_bef_RR 需要去噪的数据
	 * @param p 每次去噪截取的区间大小
	 * @return 去噪后的脉率数据
	 */
	public float[] denoisingRR2(float[] temp_bef_RR, int p){
		int bef_len = temp_bef_RR.length;
		float[] temp = new float[bef_len];    //接收数据的临时数组
		for(int i = 0; i < bef_len; i++){
			temp[i] = temp_bef_RR[i];
		}
		//整体去噪
		float sum = 0;
		float temp_stdev_pr_ave = 0;
		for(float i : temp){
			sum += i;
		}
		temp_stdev_pr_ave = sum / bef_len;
		for(int i = 0; i <bef_len; i++){
			if(temp[i] < 0.33 || temp[i]> 1.5){
				temp[i] = temp_stdev_pr_ave;
			}
		}
		//局部去噪
		sum = 0;
		temp_stdev_pr_ave = 0;
		int pace = p;    //去噪计算的数据长度
		int pace2 = bef_len%pace;    //剩余的，不足窗口大小的数据
		for(int i = 0; i <bef_len; i++){
			sum += temp[i];
			if((i+1)%pace == 0){
				temp_stdev_pr_ave = sum/pace;
				for(int j = 0; j < pace; j++){
					if(temp[i - j] - temp_stdev_pr_ave > 0.33 || temp[i - j] - temp_stdev_pr_ave < -0.33){
						temp[i - j] = temp_stdev_pr_ave;
					}
				}
			sum = 0;
			}else if((i+1) == bef_len){
				temp_stdev_pr_ave = sum/(pace2);
				for(int j = 0; j < pace2; j++){
					if(temp[i - j] - temp_stdev_pr_ave > 0.33 || temp[i - j] - temp_stdev_pr_ave < -0.33){
						temp[i - j] = temp_stdev_pr_ave;
					}
				}
				sum = 0;
			}
		}
		//将RR间期转化为脉率
		for(int i = 0; i < temp.length; i++){
			if(temp[i] > 0){
				temp[i] = 60 / temp[i];
			}else{
				temp[i] = 255;
			}
		}
		return temp;
	}
	
	/**
	 * 对RR间期数据去噪的方法，先转换为脉率值，然后通过比较相邻数值的差值，去掉突变的值(采用移动窗口的方式去噪，每次移动半个窗口)
	 * @param temp_bef_RR 需要去噪的数据
	 * @param p 每次去噪截取的区间大小(窗口大小)
	 * @return  
	 */
	public float[] denoisingRR_v2(float[] temp_bef_RR, int p){
		int bef_len = temp_bef_RR.length;
		float[] temp = new float[bef_len];    //接收数据的临时数组
		
		for(int i = 0; i < bef_len; i++) temp[i] = temp_bef_RR[i];
		
	    //将RR间期转化为脉率，若RR间期为小于等于0，脉率取255
		for(int i = 0; i < bef_len; i++){
			if(temp_bef_RR[i] > 0){
				temp[i] = 60 / temp_bef_RR[i];
			}else{
				temp[i] = 255;
			}
		}
		
//		//过滤单点突变的值
//		for(int i = 0; i < bef_len; i++){
//			if(temp[i] < 60){  // 判断条件：当前点的值是否小于60
//				if(i+1 < bef_len && Math.abs(temp[i] - temp[i+1]) > 10){  // 当前点与后一点的差值的绝对值大于10
//					float peak = 0;  // 上升或下降到顶点时的幅度
//					float t1 = temp[i] - temp[i+1];  // 累计差值
//					int c1 = i+1;  // 数组的临时标志
//					if(t1 > 0){  // 如果差值大于0
//						while(c1 + 1 < bef_len && temp[c1] - temp[c1 + 1] > 0){
//							t1 += (temp[c1] - temp[c1 + 1]);
//							c1++;
//						}
//						peak = t1;
//					}else if(t1 < 0){  // 如果差值小于0
//						while(c1 + 1 < bef_len && temp[c1] - temp[c1 + 1] < 0){
//							t1 += (temp[c1] - temp[c1 + 1]);
//							c1++;
//						}
//						peak = t1;
//					}
//				}
//			}else{
//				if(Math.abs(temp[i] - temp[i-1]) > 30 && Math.abs(temp[i] - temp[i-2]) < 20){
//					temp[i-1] = (temp[i-2] + temp[i]) / 2;
//				}
//			}
//		}
		
		
		float[] window = new float[p];    //定义去噪窗口大小的临时数据
		float[] fd = new float[p-1];    //差分数组
//		int wIndex = 0;    //window数组的index
		for(int i = 0; i < temp.length; i++){
			if(((i+1)%(p/2) == 0 && i >= p-1) || i == temp.length-1){
				for(int j = 0; j < p; j++){
//					window[j] = temp[i-(p-1)+j];    //给窗口数组赋值
//					if(j > 0) fd[j-1] = window[j] - window[j-1];    //计算差分值(finite difference)(后项减前项)
					if(j > 0) fd[j-1] = temp[i-(p-1)+j] - temp[i-(p-1)+j-1];
				}
				//去噪处理
				for(int j = 0; j < p-1; j++){    //从左向右检测
					if(fd[j] > 45 && j+4 < p-1){    //上升幅度大于45的突变
						if(Math.abs(fd[j] + fd[j+1]) < 40){    //如果出现一个点发生跳变的情况，将该点的值取为前后两点的均值
							temp[i-(p-2)+j] = (temp[i-(p-2)+j-1] + temp[i-(p-2)+j+1])/2;
						}
						else if(Math.abs(fd[j] + fd[j+1] + fd[j+2]) < 40){    //两个点发生跳变
							temp[i-(p-2)+j] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2])*1/3;
							temp[i-(p-2)+j+1] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2])*2/3;
						}
						else if(Math.abs(fd[j] + fd[j+1] + fd[j+2] + fd[j+3]) < 40){    //三个点发生跳变
							temp[i-(p-2)+j] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3])*1/4;
							temp[i-(p-2)+j+1] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3])*2/4;
							temp[i-(p-2)+j+2] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3])*3/4;
						}
						else if(Math.abs(fd[j] + fd[j+1] + fd[j+2] + fd[j+3] + fd[j+4]) < 40){    //四个点发生跳变
							temp[i-(p-2)+j] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3] + fd[j+4])*1/5;
							temp[i-(p-2)+j+1] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3] + fd[j+4])*2/5;
							temp[i-(p-2)+j+2] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3] + fd[j+4])*3/5;
							temp[i-(p-2)+j+3] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3] + fd[j+4])*4/5;
						}
					}else if(fd[j] < -45 && j+4 < p-1){    //下降幅度大于45的突变
						if(Math.abs(fd[j] + fd[j+1]) < 40){    //如果出现一个点发生跳变的情况，将该点的值取为前后两点的均值
							temp[i-(p-2)+j] = (temp[i-(p-2)+j-1] + temp[i-(p-2)+j+1])/2;
						}
						else if(Math.abs(fd[j] + fd[j+1] + fd[j+2]) < 40){    //两个点发生跳变
							temp[i-(p-2)+j] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2])*1/3;
							temp[i-(p-2)+j+1] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2])*2/3;
						}
						else if(Math.abs(fd[j] + fd[j+1] + fd[j+2] + fd[j+3]) < 40){    //三个点发生跳变
							temp[i-(p-2)+j] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3])*1/4;
							temp[i-(p-2)+j+1] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3])*2/4;
							temp[i-(p-2)+j+2] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3])*3/4;
						}
						else if(Math.abs(fd[j] + fd[j+1] + fd[j+2] + fd[j+3] + fd[j+4]) < 40){    //四个点发生跳变
							temp[i-(p-2)+j] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3] + fd[j+4])*1/5;
							temp[i-(p-2)+j+1] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3] + fd[j+4])*2/5;
							temp[i-(p-2)+j+2] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3] + fd[j+4])*3/5;
							temp[i-(p-2)+j+3] = temp[i-(p-2)+j-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3] + fd[j+4])*4/5;
						}
					}
				}
				for(int j = p-2; j >=0; j--){    //从右向左检测
					if(fd[j] > 45 && j-3 > 0){
						if(Math.abs(fd[j] + fd[j-1]) < 40){    //如果出现一个点发生跳变的情况，将该点的值取为前后两点的均值
							temp[i-p+j] = (temp[i-p+j+1] + temp[i-p+j-1])/2;
						}
//						else if((fd[j] + fd[j-1] + fd[j-2]) > -40 && (fd[j] + fd[j-1] + fd[j-2]) < 40){    //两个点发生跳变
//							temp[i-p+j] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2])*1/3;
//							temp[i-p+j-1] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2])*2/3;
//						}else if((fd[j] + fd[j-1] + fd[j-2] + fd[j-3]) > -40 && (fd[j] + fd[j-1] + fd[j-2] + fd[j-3]) < 40){    //三个点发生跳变
//							temp[i-p+j] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3])*1/4;
//							temp[i-p+j-1] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3])*2/4;
//							temp[i-p+j-2] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3])*3/4;
//						}else if((fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4]) > -40 && (fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4]) < 40){    //四个点发生跳变
//							temp[i-p+j] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4])*1/5;
//							temp[i-p+j-1] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4])*2/5;
//							temp[i-p+j-2] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4])*3/5;
//							temp[i-p+j-3] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4])*4/5;
//						}
					}else if(fd[j] < -45 && j-3 > 0){
						if(Math.abs(fd[j] + fd[j-1]) < 40){    //如果出现一个点发生跳变的情况，将该点的值取为前后两点的均值
							temp[i-p+j] = (temp[i-p+j+1] + temp[i-p+j-1])/2;
						}
//						else if((fd[j] + fd[j-1] + fd[j-2]) > -40 && (fd[j] + fd[j-1] + fd[j-2]) < 40){    //两个点发生跳变
//							temp[i-p+j] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2])*1/3;
//							temp[i-p+j-1] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2])*2/3;
//						}else if((fd[j] + fd[j-1] + fd[j-2] + fd[j-3]) > -40 && (fd[j] + fd[j-1] + fd[j-2] + fd[j-3]) < 40){    //三个点发生跳变
//							temp[i-p+j] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3])*1/4;
//							temp[i-p+j-1] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3])*2/4;
//							temp[i-p+j-2] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3])*3/4;
//						}else if((fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4]) > -40 && (fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4]) < 40){    //四个点发生跳变
//							temp[i-p+j] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4])*1/5;
//							temp[i-p+j-1] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4])*2/5;
//							temp[i-p+j-2] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4])*3/5;
//							temp[i-p+j-3] = temp[i-p+j+1] - (fd[j] + fd[j-1] + fd[j-2] + fd[j-3] + fd[j-4])*4/5;
//						}
					}
				}
			}
			
			
//			window[wIndex] = temp[i];
//			//计算差分值
//			if(wIndex > 0){
//				fd[wIndex-1] = window[wIndex] - window[wIndex-1];
//			}
//			wIndex++;
			//去噪处理
//			if(wIndex == p-1){
//				wIndex = 0;
//				for(int j = 0; j < p-1; j++){
//					if(fd[j] > 50 && j < p-1-3){    //差分的阈值为50
//						if((fd[j] + fd[j+1]) > -10 && (fd[j] + fd[j+1]) < 10){    //如果出现一个点发生跳变的情况，将该点的值取为前后两点的均值
//							temp[i-(p-1-j)] = (temp[i-(p-1-j)-1] + temp[i-(p-1-j)+1])/2;
//						}else if((fd[j] + fd[j+1] + fd[j+2]) > -10 && (fd[j] + fd[j+1] + fd[j+2]) < 10){    //两个点发生跳变
//							temp[i-(p-1-j)] = temp[i-(p-1-j)-1] + (fd[j] + fd[j+1] + fd[j+2])*1/3;
//							temp[i-(p-1-j)+1] = temp[i-(p-1-j)-1] + (fd[j] + fd[j+1] + fd[j+2])*2/3;
//						}else if((fd[j] + fd[j+1] + fd[j+2] + fd[j+3]) > -10 && (fd[j] + fd[j+1] + fd[j+2] + fd[j+3]) < 10){    //三个点发生跳变
//							temp[i-(p-1-j)] = temp[i-(p-1-j)-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3])*1/4;
//							temp[i-(p-1-j)+1] = temp[i-(p-1-j)-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3])*2/4;
//							temp[i-(p-1-j)+1] = temp[i-(p-1-j)-1] + (fd[j] + fd[j+1] + fd[j+2] + fd[j+3])*3/4;
//						}
//					}
//				}
//			}
		}
		
		
		return temp;
	}
	
	/**
	 * 根据划分的窗口大小，计算每段数据对应的标准差，并计算对应的时间长度
	 * @param temp_aft_ML 用于计算的脉率数据
	 * @param temp_extract_string_time String类型的时间
	 * @param p 窗口大小，标准差计算的数据长度
	 * @return 标准差数组
	 */
	public float[] cal_stdev(float[] temp_aft_ML, int[][] temp_extract_string_time, int p){
		float[] temp_stdev;
		int aft_len = temp_aft_ML.length;    //aft数组的长度
		int pace = p;    //标准差计算的数据长度
		float[] temp = new float[aft_len];    //接收aft数组
		for(int i = 0; i < aft_len; i++){
			temp[i] = temp_aft_ML[i];
		}
		int len = 0;    //标准差数组的大小
		
//		//剩余的数据也用于计算标准差
//		int pace2 = aft_len%pace;    //不足窗口长度的数据长度
//		if(pace2 != 0){
//			len = aft_len/pace + 1;
//		}else{
//			len = aft_len/pace;
//		}
//		temp_stdev = new float[len];    //标准差数组
//		temp_stdev_pr_ave = new float[len];
//		int count = 0;    //标准差数组的元素序号
//		float sum = 0;
//		for(int i = 0; i < aft_len; i++){
//			sum += temp[i];
//			if((i+1)%pace == 0){
//				temp_stdev_pr_ave[count] = sum/pace;
//				float sum2 = 0;
//				for(int j = 0; j < pace; j++){
//					sum2 += Math.pow(temp[i-j] - temp_stdev_pr_ave[count], 2);
//					if(j == pace -1){
//						temp_stdev[count] = (float) Math.sqrt(sum2/pace);
//						count++;
//					}
//				}
//				sum = 0;
//			}else if((i+1) == aft_len){
//				temp_stdev_pr_ave[count] = sum/(pace2);
//				float sum2 = 0;
//				for(int j = 0; j < pace2; j++){
//					sum2 += Math.pow(temp[i-j] - temp_stdev_pr_ave[count], 2);
//					if(j == pace2 - 1){
//						temp_stdev[count] = (float) Math.sqrt(sum2/pace2);
//						count++;
//					}
//				}
//				sum = 0;
//			}
//		}
		
		//剩余的数据直接舍去
		len = aft_len/pace; 
		temp_stdev = new float[len];    //标准差数组
		temp_stdev_string_time = new int[len][3];
		int[] start_time = new int[4];    //标准差计算区间起点对应的时间
		int[] end_time = new int[4];    //标准差计算区间终点对应的时间
		temp_stdev_pr_ave = new float[len];
		int count = 0;    //标准差数组的元素序号
		float sum = 0;
		float sum_pr = 0;
		pr_max = (int)temp[0];    //初始化
		pr_min = (int)temp[0];    //初始化
		for(int i = 0; i < aft_len; i++){
			sum_pr += temp[i];
			sum += temp[i];
			if((i+1)%pace == 0){
				for(int t = 0; t < start_time.length; t++){
					start_time[t] = temp_extract_string_time[i+1-pace][t];
					end_time[t] = temp_extract_string_time[i][t];
				}
				temp_stdev_string_time[count] = calStdevTime(start_time, end_time);
				temp_stdev_pr_ave[count] = sum/pace;
				float sum2 = 0;
				for(int j = 0; j < pace; j++){
					sum2 += Math.pow(temp[i-j] - temp_stdev_pr_ave[count], 2);
					if(j == pace -1){
						temp_stdev[count] = (float) Math.sqrt(sum2/pace);
						count++;
					}
				}
				sum = 0;
			}
			if(temp[i] > pr_max){
				pr_max = (int)temp[i];    //计算最大脉率值
				for(int j = 0; j < 3; j++){
					pr_max_timePoint[j] = temp_extract_string_time[i][j+1];    //计算最大脉率值对应的时间
				}
			}
			if(temp[i] < pr_min){
				pr_min = (int)temp[i];    //计算最小脉率值
				for(int j = 0; j < 3; j++){
					pr_min_timePoint[j] = temp_extract_string_time[i][j+1];    //计算最小脉率值对应的时间
				}
			}
		}
		pr_average = sum_pr / aft_len;    //计算脉率平均值
		
		float sum_ave = 0;
		temp_ave_max = temp_stdev_pr_ave[0];
		temp_ave_min = temp_stdev_pr_ave[0];
		for(int i = 0; i < temp_stdev_pr_ave.length; i++){
			sum_ave += temp_stdev_pr_ave[i];
			if(temp_ave_max < temp_stdev_pr_ave[i]) temp_ave_max = temp_stdev_pr_ave[i];
			if(temp_ave_min > temp_stdev_pr_ave[i]) temp_ave_min = temp_stdev_pr_ave[i];
		}
		temp_ave_average = sum_ave / len;
//		System.out.println("temp_ave_average: " + temp_ave_average);
		
		sum_ave = 0;
		for(float i : temp_stdev) sum_ave += i;
		temp_stdev_average = sum_ave / len;
//		System.out.println("temp_stdev_average: " + temp_stdev_average);
		return temp_stdev;
	}
	
	/**
	 * 根据划分的窗口大小，计算每段数据对应的标准差，并计算对应的时间长度
	 * @param temp_aft_ML 用于计算的脉率数据
	 * @param temp_num_time long类型的时间
	 * @param p 窗口大小，标准差计算的数据长度
	 * @return 标准差数组
	 */
	public float[] cal_stdev2(float[] temp_aft_ML, long[] temp_num_time, int p){
		float[] temp_stdev;
		int aft_len = temp_aft_ML.length;    //aft数组的长度
		int pace = p;    //标准差计算的数据长度
		float[] temp = new float[aft_len];    //接收aft数组
		for(int i = 0; i < aft_len; i++){
			temp[i] = temp_aft_ML[i];
		}
		int len = 0;    //标准差数组的大小
		
//		//剩余的数据也用于计算标准差
//		int pace2 = aft_len%pace;    //不足窗口长度的数据长度
//		if(pace2 != 0){
//			len = aft_len/pace + 1;
//		}else{
//			len = aft_len/pace;
//		}
//		temp_stdev = new float[len];    //标准差数组
//		temp_stdev_pr_ave = new float[len];
//		int count = 0;    //标准差数组的元素序号
//		float sum = 0;
//		for(int i = 0; i < aft_len; i++){
//			sum += temp[i];
//			if((i+1)%pace == 0){
//				temp_stdev_pr_ave[count] = sum/pace;
//				float sum2 = 0;
//				for(int j = 0; j < pace; j++){
//					sum2 += Math.pow(temp[i-j] - temp_stdev_pr_ave[count], 2);
//					if(j == pace -1){
//						temp_stdev[count] = (float) Math.sqrt(sum2/pace);
//						count++;
//					}
//				}
//				sum = 0;
//			}else if((i+1) == aft_len){
//				temp_stdev_pr_ave[count] = sum/(pace2);
//				float sum2 = 0;
//				for(int j = 0; j < pace2; j++){
//					sum2 += Math.pow(temp[i-j] - temp_stdev_pr_ave[count], 2);
//					if(j == pace2 - 1){
//						temp_stdev[count] = (float) Math.sqrt(sum2/pace2);
//						count++;
//					}
//				}
//				sum = 0;
//			}
//		}
		
		//剩余的数据直接舍去
		len = aft_len/pace; 
		temp_stdev = new float[len];    //标准差数组
		temp_stdev_num_time = new long[len];
		temp_stdev_pr_ave = new float[len];
		int count = 0;    //标准差数组的元素序号
		float sum = 0;
		float sum_pr = 0;
		pr_max = (int)temp[0];    //初始化
		pr_min = (int)temp[0];    //初始化
		for(int i = 0; i < aft_len; i++){
			sum_pr += temp[i];
			sum += temp[i];
			if((i+1)%pace == 0){
				temp_stdev_num_time[count] = temp_num_time[i] - temp_num_time[i+1-pace];
				temp_stdev_pr_ave[count] = sum/pace;
				float sum2 = 0;
				for(int j = 0; j < pace; j++){
					sum2 += Math.pow(temp[i-j] - temp_stdev_pr_ave[count], 2);
				}
				temp_stdev[count] = (float) Math.sqrt(sum2/pace);
				count++;
				sum = 0;
			}
			if(temp[i] > pr_max){
				pr_max = (int)temp[i];    //计算最大脉率值
				pr_max_timePoint2 = temp_num_time[i];    //计算最大脉率值对应的时间
			}
			if(temp[i] < pr_min){
				pr_min = (int)temp[i];    //计算最小脉率值
				pr_min_timePoint2 = temp_num_time[i];    //计算最小脉率值对应的时间
			}
		}
		pr_average = sum_pr / aft_len;    //计算脉率平均值
		
		float sum_ave = 0;
		temp_ave_max = temp_stdev_pr_ave[0];
		temp_ave_min = temp_stdev_pr_ave[0];
		for(int i = 0; i < temp_stdev_pr_ave.length; i++){
			sum_ave += temp_stdev_pr_ave[i];
			if(temp_ave_max < temp_stdev_pr_ave[i]) temp_ave_max = temp_stdev_pr_ave[i];
			if(temp_ave_min > temp_stdev_pr_ave[i]) temp_ave_min = temp_stdev_pr_ave[i];
		}
		temp_ave_average = sum_ave / len;
//		System.out.println("temp_ave_average: " + temp_ave_average);
		
		sum_ave = 0;
		for(float i : temp_stdev) sum_ave += i;
		temp_stdev_average = sum_ave / len;
//		System.out.println("temp_stdev_average: " + temp_stdev_average);
		return temp_stdev;
	}
	
	/**
	 * 计算整晚的睡眠分期，根据标准差值的变化，以及脉率平均值的变化
	 * @param temp_stdev 根据窗口大小计算所得的标准差的值
	 * @param temp_stdev_average 标准差均值
	 * @param temp_stdev_pr_ave 与标准差对应的脉率均值
	 * @param temp_ave_average 各窗口中脉率总均值
	 * @param temp_stdev_string_time 与每个标准差对应的时间
	 * @param temp_ave_min 各窗口中脉率均值最小值
	 * @param temp_ave_max 各窗口中脉率均值最大值
	 * @return 数字化的睡眠分期，1：深睡期，2：浅睡期，3：清醒期
	 */
	public int[] cal_stage(float[] temp_stdev, float temp_stdev_average, float[] temp_stdev_pr_ave, float temp_ave_average, int[][] temp_stdev_string_time, float temp_ave_min, float temp_ave_max){
		time_wake = new int[3];
		time_light = new int[3];
		time_deep = new int[3];
		int[] temp_stage;
		int stdev_len = temp_stdev.length;
		temp_stage = new int[stdev_len];
		for(int i = 0; i < stdev_len; i++){
			if(temp_stdev[i] < temp_stdev_average / 2){
				if(temp_stdev_pr_ave[i] < temp_ave_average){
					temp_stage[i] = 1;
					time_deep[0] += temp_stdev_string_time[i][0];
					time_deep[1] += temp_stdev_string_time[i][1];
					time_deep[2] += temp_stdev_string_time[i][2];
				}
				else{
					temp_stage[i] = 2;
					time_light[0] += temp_stdev_string_time[i][0];
					time_light[1] += temp_stdev_string_time[i][1];
					time_light[2] += temp_stdev_string_time[i][2];
				}
			}else if(temp_stdev[i] < temp_stdev_average){
				if(temp_stdev_pr_ave[i] < (temp_ave_average + temp_ave_min) / 2){
					temp_stage[i] = 1;
					time_deep[0] += temp_stdev_string_time[i][0];
					time_deep[1] += temp_stdev_string_time[i][1];
					time_deep[2] += temp_stdev_string_time[i][2];
				} 
				else{
					temp_stage[i] = 2;
					time_light[0] += temp_stdev_string_time[i][0];
					time_light[1] += temp_stdev_string_time[i][1];
					time_light[2] += temp_stdev_string_time[i][2];
				}
			}else if(temp_stdev[i] < temp_stdev_average * 2){
				if(temp_stdev_pr_ave[i] < (temp_ave_average + temp_ave_max) / 2){
					temp_stage[i] = 2;
					time_light[0] += temp_stdev_string_time[i][0];
					time_light[1] += temp_stdev_string_time[i][1];
					time_light[2] += temp_stdev_string_time[i][2];
				} 
				else{
					temp_stage[i] = 3;
					time_wake[0] += temp_stdev_string_time[i][0];
					time_wake[1] += temp_stdev_string_time[i][1];
					time_wake[2] += temp_stdev_string_time[i][2];
				}
			}else{
				if(temp_stdev_pr_ave[i] < temp_ave_average){
					temp_stage[i] = 2;
					time_light[0] += temp_stdev_string_time[i][0];
					time_light[1] += temp_stdev_string_time[i][1];
					time_light[2] += temp_stdev_string_time[i][2];
				} 
				else{
					temp_stage[i] = 3;
					time_wake[0] += temp_stdev_string_time[i][0];
					time_wake[1] += temp_stdev_string_time[i][1];
					time_wake[2] += temp_stdev_string_time[i][2];
				}
			}
		}
		calStageTime(time_deep, time_light, time_wake);
		return temp_stage;
	}
	
	/**
	 * 计算整晚的睡眠分期，根据标准差值的变化，以及脉率平均值的变化
	 * @param temp_stdev 根据窗口大小计算所得的标准差的值
	 * @param temp_stdev_average 标准差均值
	 * @param temp_stdev_pr_ave 与标准差对应的脉率均值
	 * @param temp_ave_average 各窗口中脉率总均值
	 * @param temp_stdev_num_time 与每个标准差对应的时间
	 * @param temp_ave_min 各窗口中脉率均值最小值
	 * @param temp_ave_max 各窗口中脉率均值最大值
	 * @return 数字化的睡眠分期，1：深睡期，2：浅睡期，3：清醒期
	 */
	public int[] cal_stage2(float[] temp_stdev, float temp_stdev_average, float[] temp_stdev_pr_ave, float temp_ave_average, long[] temp_stdev_num_time, float temp_ave_min, float temp_ave_max){
		time_wake2 = 0;
		time_light2 = 0;
		time_deep2 = 0;
		int stdev_len = temp_stdev.length;
		int[] temp_stage = new int[stdev_len];
		for(int i = 0; i < stdev_len; i++){
			if(temp_stdev[i] < temp_stdev_average / 2){
				if(temp_stdev_pr_ave[i] < temp_ave_average){
					temp_stage[i] = 1;
					time_deep2 += temp_stdev_num_time[i];
				}
				else{
					temp_stage[i] = 2;
					time_light2 += temp_stdev_num_time[i];
				}
			}else if(temp_stdev[i] < temp_stdev_average){
				if(temp_stdev_pr_ave[i] < (temp_ave_average + temp_ave_min) / 2){
					temp_stage[i] = 1;
					time_deep2 += temp_stdev_num_time[i];
				} 
				else{
					temp_stage[i] = 2;
					time_light2 += temp_stdev_num_time[i];
				}
			}else if(temp_stdev[i] < temp_stdev_average * 2){
				if(temp_stdev_pr_ave[i] < (temp_ave_average + temp_ave_max) / 2){
					temp_stage[i] = 2;
					time_light2 += temp_stdev_num_time[i];
				} 
				else{
					temp_stage[i] = 3;
					time_wake2 += temp_stdev_num_time[i];
				}
			}else{
				if(temp_stdev_pr_ave[i] < temp_ave_average){
					temp_stage[i] = 2;
					time_light2 += temp_stdev_num_time[i];
				} 
				else{
					temp_stage[i] = 3;
					time_wake2 += temp_stdev_num_time[i];
				}
			}
		}
		calStageTime2(time_deep2, time_light2, time_wake2);
		return temp_stage;
	}
	
	/**
	 * 从读取的时间字符串数组中抽取时间数据
	 * @param temp_string_time 原始时间字符串数组
	 * @return 返回的时间数组，各个元素对应的数据是：日、时、分、秒
	 */
	public int[][] extractTime(String[] temp_string_time){
		int[][] temp = new int[temp_string_time.length][4];
		for(int i = 0; i < temp_string_time.length; i++){
//			System.out.println("i = " + i);
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
	
	/**
	 * 计算整晚睡眠持续的时间
	 * @param temp_extract_string_time 时间数组，各个元素对应的数据是：日、时、分、秒
	 * @return 返回的时间数组，各元素的数据为：时、分、秒
	 */
	public int[] calLastTime(int[][] temp_extract_string_time){
		int[] temp = new int[3];
		int[] t = new int[4];
		t[0] = temp_extract_string_time[temp_extract_string_time.length-1][0] - temp_extract_string_time[0][0];
		t[1] = temp_extract_string_time[temp_extract_string_time.length-1][1] - temp_extract_string_time[0][1];
		t[2] = temp_extract_string_time[temp_extract_string_time.length-1][2] - temp_extract_string_time[0][2];
		t[3] = temp_extract_string_time[temp_extract_string_time.length-1][3] - temp_extract_string_time[0][3];
		
		if(t[3] < 0){  // 若保留，则精确到秒，若注释掉，则精确到分
			t[2]--;
			temp[2] = t[3] + 60;
		}else{
			temp[2] = t[3];
		}
		
		if(t[2] < 0){
			t[1]--;
			temp[1] = t[2] + 60;
		}else{
			temp[1] = t[2];
		}
		
		if(t[0] > 0){
			temp[0] = 24 + t[1];        //24小时制
		}else{
			temp[0] = t[1];
		}
		
		return temp;
	}
	
	/**
	 * 计算整晚睡眠的开始时间、结束时间、持续时间
	 * @param temp_num_time long类型的时间
	 */
	public void calLastTime2(long[] temp_num_time){
		time_start2 = temp_num_time[0];
		time_end2 = temp_num_time[temp_num_time.length - 1];
		time_last2 = (time_end2 - time_start2);  // 首尾时间直接相减，可能出现时间对不上的问题，结果精确到秒
//		time_last2 = (time_end2/1000/60 - time_start2/1000/60)*1000*60;  // 持续时间等于结束时间减去开始时间，没有时间差，结果精确到分
	}
	
	/**
	 * 计算每个标准差值对应的时间窗口长度
	 * @param start_time 时间窗口的起点，各个元素对应的数据是：日、时、分、秒
	 * @param end_time 时间窗口的终点，各个元素对应的数据是：日、时、分、秒
	 * @return 返回的时间间隔，各个元素对应的数据是：时、分、秒
	 */
	public int[] calStdevTime(int[] start_time, int[] end_time){
		int[] temp = new int[3];
		int[] t = new int[4];
		t[0] = end_time[0] - start_time[0];
		t[1] = end_time[1] - start_time[1];
		t[2] = end_time[2] - start_time[2];
		t[3] = end_time[3] - start_time[3];
		
		if(t[3] < 0){
			t[2]--;
			temp[2] = t[3] + 60;
		}else{
			temp[2] = t[3];
		}
		if(t[2] < 0){
			t[1]--;
			temp[1] = t[2] + 60;
		}else{
			temp[1] = t[2];
		}
		if(t[0] > 0){
			temp[0] = 24 + t[1];        //24小时制
		}else{
			temp[0] = t[1];
		}
		
		return temp;
	}
	
	/**
	 * 计算每个睡眠阶段的持续时间长度
	 * @param deep 深睡期持续时间，结果格式为：时、分、秒
	 * @param light 浅睡期持续时间，结果格式为：时、分、秒
	 * @param wake 清醒期持续时间，结果格式为：时、分、秒
	 */
	public void calStageTime(int[] deep, int[] light, int[] wake){
		int[] temp = new int[3];
		for(int i = 0; i < temp.length; i++){
			temp[i] = deep[i] + light[i] + wake[i];
		}
		formTime(temp);
		
		temp[0] = time_last[0] - temp[0];
		
		if(time_last[1] - temp[1] < 0){
			temp[0]--;
			temp[1] = time_last[1] - temp[1] + 60;
		}else{
			temp[1] = time_last[1] - temp[1];
		}
		
		if(time_last[2] - temp[2] < 0){
			temp[1]--;
			temp[2] = time_last[2] - temp[2] + 60;
		}else{
			temp[2] = time_last[2] - temp[2];
		}
		
		if(temp[0]*60 + temp[1] > 20){
			for(int j = 0; j < temp.length; j++){
				deep[j] += temp[j] / 2;
				light[j] += temp[j] / 2;
			}
		}else{
			for(int j = 0; j < temp.length; j++){
				light[j] += temp[j];
			}
		}
		
		formTime(deep);
		formTime(light);
		formTime(wake);
	}
	
	/**
	 * 计算每个睡眠阶段的持续时间长度
	 * @param deep 深睡期持续时间，结果格式为：时、分、秒
	 * @param light 浅睡期持续时间，结果格式为：时、分、秒
	 * @param wake 清醒期持续时间，结果格式为：时、分、秒
	 */
	public void calStageTime2(long deep, long light, long wake){
		long temp = time_last2 - deep - light - wake;
		//如果睡眠总持续时间与三个睡眠期的时间和的差值大于20分钟，则差值由浅睡期和深睡期平分，反之，全给浅睡期
		if(temp/1000/60 > 20){
			deep += temp / 2;
			light += temp / 2;
		}else{
			light += temp;
		}
		
//		//调整三个睡眠时期的时间，消除与总睡眠时间之间的时间差，精确到分
//		long temp = time_last2/1000/60 - deep/1000/60 - light/1000/60 - wake/1000/60;
//		//如果睡眠总持续时间与三个睡眠期的时间和的差值大于20分钟，则差值由浅睡期和深睡期平分，反之，全给浅睡期
//		if(temp > 20){
//			deep += (temp / 2 * 1000 * 60);
//			light += (temp / 2 * 1000 * 60);
//		}else{
//			light += (temp * 1000 * 60);
//		}
	}
	
	/**
	 * 判断时间值是否超过上限，并将时间调整为正确值
	 * @param temp_string_time 需要调整的时间数组，各个元素对应的数据是：时、分、秒
	 */
	public void formTime(int[] temp_string_time){
		if(temp_string_time[2] >= 60){
			temp_string_time[1] += temp_string_time[2] / 60;
			temp_string_time[2] = temp_string_time[2] % 60;
		}
		if(temp_string_time[1] >= 60){
			temp_string_time[0] += temp_string_time[1] / 60;
			temp_string_time[1] = temp_string_time[1] % 60;
		}
	}
	
	/**
	 * 根据秒数的大小，调整时间的值（小于30则舍去，大于30则进位）
	 * @param temp_string_time 传入长度为3的数组，时间数值必须正确，各个元素对应的数据是：时、分、秒
	 */
	public void formTimeSecond(int[] temp_string_time){
		if(temp_string_time[2] >= 30){
			temp_string_time[1] ++;
		}
		if(temp_string_time[1] >= 60){
			temp_string_time[0] += temp_string_time[1] / 60;
			temp_string_time[1] = temp_string_time[1] % 60;
		}
	}
	
	/**
	 * 将多列数据写入到文件中
	 * @param outputpath 输出文件路径
	 */
	public void writeData(String outputpath){
		try{
			// 取得文件路径
			File f = new File(outputpath);
			// 将数组的数据写入到文件中
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("before" + "," + "after" + "," + "temp_ave_average" + ","  + "temp_stdev_pr_ave" + "," 
					+ "temp_stdev" + "," + "temp_stdev_average" + "," + "temp_stage");
			bw.newLine();//换行
			for (int i = 0; i < temp_bef_RR.length; i++) {
				bw.write(temp_bef_RR[i] + "");
				bw.write("," + temp_aft_ML[i]);
				bw.write("," + temp_ave_average);
				if(i < temp_stdev_pr_ave.length) bw.write("," + temp_stdev_pr_ave[i]);
				if(i < temp_stdev.length) bw.write("," + temp_stdev[i]);
				if(i < temp_stdev.length) bw.write("," + temp_stdev_average);
				if(i < temp_stage.length) bw.write("," + temp_stage[i]);
				bw.newLine();//换行
			}
			
			bw.flush();
			fw.flush();
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// binary
	public void read_binary(String filepath, String temp_seperator){
		final int data_length = 21; //1+1+1+1+4+1+1+1+1+8+1 = 21
		RandomAccessFile raf;
		int len;
		try {
			raf = new RandomAccessFile(filepath,"r");
			len = (int)(raf.length()/data_length);
			
			if(flag_five == false){
		        temp_bef_RR = new float[len];
				temp_string_time = new String[len];
				temp_num_time = new long[len];
				for (int i = 0; i < len; i++) {
					raf.seek(i*data_length);
					raf.readByte();
					raf.readByte();
					raf.readByte();
					raf.readByte();
					temp_bef_RR[i] = Float.valueOf(raf.readInt()) / packagelength;
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
			}else{
		        temp_bef_RR = new float[len-360];
				temp_string_time = new String[len-360];
				temp_num_time = new long[len-360];
				for (int i = 0; i < len-360; i++) {
					raf.seek((i + 300)*data_length);
					raf.readByte();
					raf.readByte();
					raf.readByte();
					raf.readByte();
					temp_bef_RR[i] = Float.valueOf(raf.readInt()) / packagelength;
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
				if(temp_seperator.equals(strbuff)) temp_count_seperator++;
			}
	        if(flag_five == false){
		        temp_bef_RR = new float[count_newdata];
				temp_string_time = new String[count_newdata];
				temp_num_time = new long[count_newdata];
				for (int i = 0; i < count_newdata; i++) {
		            strbuff = br.readLine();
		            strcol = strbuff.split(",");
		            temp_bef_RR[i] = Float.valueOf(strcol[2]) / packagelength;
					temp_num_time[i] = Long.valueOf(strcol[3]);
					temp_string_time[i] = in.format(new Date(temp_num_time[i]));
		        }
			}else{
		        temp_bef_RR = new float[count_newdata-360];
				temp_string_time = new String[count_newdata-360];
				temp_num_time = new long[count_newdata-360];
				for (int i = 0; i < 300; i++) {
					br.readLine();
		        }
				for (int i = 0; i < count_newdata-360; i++) {
		            strbuff = br.readLine();
		            strcol = strbuff.split(",");
		            temp_bef_RR[i] = Float.valueOf(strcol[2]) / packagelength;
					temp_num_time[i] = Long.valueOf(strcol[3]);
					temp_string_time[i] = in.format(new Date(temp_num_time[i]));
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
		        temp_bef_RR = new float[dataCount];
				temp_string_time = new String[dataCount];
				temp_num_time = new long[dataCount];
		        int i = 0;
				while((strbuff = br.readLine()) != null && i < dataCount){
					if(!temp_seperator.equals(strbuff)){
			            strcol = strbuff.split(",");
			            temp_bef_RR[i] = Float.valueOf(strcol[4]) / packagelength;
						temp_num_time[i] = Long.valueOf(strcol[9]);
						temp_string_time[i] = in.format(new Date(temp_num_time[i]));
						
//						temp_string_time[i] = strcol[3];
//						try {
//							temp_num_time[i] = in.parse(strcol[3]).getTime();
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						i++;
					}
				}
			}else{
				int length = dataCount - temp_dataCount[0] - temp_dataCount[1];
				Log.e("stage", "length"+length);
		        temp_bef_RR = new float[length];
				temp_string_time = new String[length];
				temp_num_time = new long[length];
				for (int i = 0; i < temp_dataCount[0]; i++) {
					br.readLine();
		        }
				int i = 0;
				while((strbuff = br.readLine()) != null && i < length){
					Log.e("stage", "");
					if(!temp_seperator.equals(strbuff)){
			            strcol = strbuff.split(",");
			            temp_bef_RR[i] = Float.valueOf(strcol[4]) / packagelength;
						temp_num_time[i] = Long.valueOf(strcol[9]);
						temp_string_time[i] = in.format(new Date(temp_num_time[i]));
						Log.e("stage", temp_string_time[i]);
//						temp_string_time[i] = strcol[3];
//						try {
//							temp_num_time[i] = in.parse(strcol[3]).getTime();
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
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
	 * 从一个文件中同时读取RR间期和时间数据，如果测量时间大于10分钟，则计算时舍去前5分钟(300个数据点)和后1分钟(60个数据点)的数据
	 * @param filepath 目标文件的路径
	 */
	public void readMultiData_1_time_RR_5min(String filepath, String temp_seperator){
        //读取数据
        int num = lineCountTotal(filepath, temp_seperator); //所读取的数据的行数
        String strbuff;
        String[] strcol;
        try {
	        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
	        strbuff = br.readLine();  //去掉第一行
	        
	        if(flag_five == false){
	        	temp_bef_RR = new float[num];
				temp_string_time = new String[num];
				temp_num_time = new long[num];
				for (int i = 0; i < num; i++) {
		            strbuff = br.readLine();
		            strcol = strbuff.split(",");
		            temp_bef_RR[i] = Float.valueOf(strcol[6]) / packagelength;  // 转换为标准RR间期
					temp_string_time[i] = strcol[7];
					if(strcol.length > 13){
						temp_num_time[i] = Long.valueOf(strcol[13]);
					}
		        }
			}else{
				temp_bef_RR = new float[num-360];
				temp_string_time = new String[num-360];
				temp_num_time = new long[num-360];
				for (int i = 0; i < 300; i++) {
					br.readLine();
		        }
				for (int i = 0; i < num-360; i++) {
		            strbuff = br.readLine();
		            strcol = strbuff.split(",");
		            temp_bef_RR[i] = Float.valueOf(strcol[6]) / packagelength;  // 转换为标准RR间期
					temp_string_time[i] = strcol[7];
					if(strcol.length > 13){
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
					tempTimeData[index] = Long.valueOf(strbuff.split(",")[9]);
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

	public int[] getTime_start() {
		return time_start;
	}

	public int[] getTime_end() {
		return time_end;
	}

	public int[] getTime_last() {
		return time_last;
	}

	public int[] getTime_wake() {
		return time_wake;
	}

	public int[] getTime_light() {
		return time_light;
	}

	public int[] getTime_deep() {
		return time_deep;
	}

	public float getPr_average() {
		return pr_average;
	}

	public int getPr_max() {
		return pr_max;
	}

	public int[] getPr_max_timePoint() {
		return pr_max_timePoint;
	}

	public int getPr_min() {
		return pr_min;
	}

	public int[] getPr_min_timePoint() {
		return pr_min_timePoint;
	}

	public long getTime_start2() {
		return time_start2;
	}

	public long getTime_end2() {
		return time_end2;
	}

	public long getTime_last2() {
		return time_last2;
	}

	public long getTime_wake2() {
		return time_wake2;
	}

	public long getTime_light2() {
		return time_light2;
	}

	public long getTime_deep2() {
		return time_deep2;
	}

	public long getPr_max_timePoint2() {
		return pr_max_timePoint2;
	}

	public long getPr_min_timePoint2() {
		return pr_min_timePoint2;
	}
}
