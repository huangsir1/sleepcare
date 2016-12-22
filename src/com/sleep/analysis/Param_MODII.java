package com.sleep.analysis;
/**
 * 保存最大氧减密集区间相关的参数
 */
public class Param_MODII {
	public Param_MODII(int[] time_interval_start, long time_interval_start2, int[] time_interval_end,
			long time_interval_end2, int count_odi, int[] time_odi, long time_odi2, float mspo2, int pr_max, float mpr,
			float percentage_count_odi) {
		super();
		this.time_interval_start = time_interval_start;
		this.time_interval_start2 = time_interval_start2;
		this.time_interval_end = time_interval_end;
		this.time_interval_end2 = time_interval_end2;
		this.count_odi = count_odi;
		this.time_odi = time_odi;
		this.time_odi2 = time_odi2;
		this.mspo2 = mspo2;
		this.pr_max = pr_max;
		this.mpr = mpr;
		this.percentage_count_odi = percentage_count_odi;
	}

	private int[] time_interval_start = new int[3]; // 当前的15分钟区间的开始时间
	private long time_interval_start2 = 0; // 当前的15分钟区间的开始时间
	private int[] time_interval_end = new int[3]; // 当前的15分钟区间的结束时间
	private long time_interval_end2 = 0; // 当前的15分钟区间的结束时间
	private int count_odi = 0; // 低于85的氧减次数
	private int[] time_odi = new int[3]; // 区间内氧减总时间
	private long time_odi2 = 0; // 区间内氧减总时间
	private float mspo2 = 0; // 平均血氧
	private int pr_max = 0; // 最大心率
	private float mpr = 0; // 平均心率
	private float percentage_count_odi = 0; // 区间内恶性氧减次数与总恶性氧减次数百分比

	public void printData() {
		System.out.println("区间起点时间: " + time_interval_start[0] + "时" + time_interval_start[1] + "分" + time_interval_start[2] + "秒" + "\n" 
				+ "区间终点时间: " + time_interval_end[0] + "时" + time_interval_end[1] + "分" + time_interval_end[2] + "秒" + "\n" 
				+ "氧减低于85的次数: " + count_odi + "\n"
				+ "区间内氧减总时间: " + time_odi[0] + "时" + time_odi[1] + "分" + time_odi[2] + "秒" + "\n" 
				+ "平均血氧: " + mspo2 + "\n" 
				+ "最大心率: " + pr_max + "\n" 
				+ "平均心率: " + mpr + "\n" 
				+ "区间内恶性氧减次数与总恶性氧减次数百分比: " + percentage_count_odi + "\n");
	}
	
	public int[] getTime_interval_start() {
		return time_interval_start;
	}

	public long getTime_interval_start2() {
		return time_interval_start2;
	}

	public int[] getTime_interval_end() {
		return time_interval_end;
	}

	public long getTime_interval_end2() {
		return time_interval_end2;
	}

	public int getCount_odi() {
		return count_odi;
	}

	public int[] getTime_odi() {
		return time_odi;
	}

	public long getTime_odi2() {
		return time_odi2;
	}

	public float getMspo2() {
		return mspo2;
	}

	public int getPr_max() {
		return pr_max;
	}

	public float getMpr() {
		return mpr;
	}

	public float getPercentage_count_odi() {
		return percentage_count_odi;
	}

}
