package com.sleep.bluetooth;

public class EquipmentData {

	private Byte Head1; 		// 数据包头1
	private Byte Head2; 		// 数据包头2
	private Byte ml; 			// 平均后的脉率
//	private Byte mlr; 			// 实时脉率
	private Byte xy; 			// 平均后的血氧
//	private Byte xyr; 			// 实时血氧
//	private Integer PPG; 		// PPG波
	private Byte PI; 			// PI强度
	private Byte Status; 		// 血氧状态位
	private Byte Battery; 		// 电池剩余量
	private Integer RR; 		// RR间期
	private Byte Index; 		// 数据包目录
	private Byte checkSum;		// 校验字节
//	private Date time; 			// 当前数据解析出来的时间
	private long numTime; 		// 当前数据解析出来的时间
	private boolean isValid;	// 当前数据是否有效
	private Byte press;
	private short axis_x;	
	private short axis_y;
	private short axis_z;
	public short getAxis_x() {
		return axis_x;
	}
	public void setAxis_x(short axis_x) {
		this.axis_x = axis_x;
	}
	public short getAxis_y() {
		return axis_y;
	}
	public void setAxis_y(short axis_y) {
		this.axis_y = axis_y;
	}
	public short getAxis_z() {
		return axis_z;
	}
	public void setAxis_z(short axis_z) {
		this.axis_z = axis_z;
	}

	public Byte getHead1() {
		return Head1;
	}
	public void setHead1(Byte head1) {
		Head1 = head1;
	}
	public Byte getHead2() {
		return Head2;
	}
	public void setHead2(Byte head2) {
		Head2 = head2;
	}
	public Byte getMl() {
		return ml;
	}
	public void setMl(Byte ml) {
		this.ml = ml;
	}
	public Byte getXy() {
		return xy;
	}
	public void setXy(Byte xy) {
		this.xy = xy;
	}
	public Byte getPI() {
		return PI;
	}
	public void setPI(Byte pI) {
		PI = pI;
	}
	public Byte getStatus() {
		return Status;
	}
	public void setStatus(Byte status) {
		Status = status;
	}
	public Byte getBattery() {
		return Battery;
	}
	public void setBattery(Byte battery) {
		Battery = battery;
	}
	public Integer getRR() {
		return RR;
	}
	public void setRR(Integer rR) {
		RR = rR;
	}
	public Byte getIndex() {
		return Index;
	}
	public void setIndex(Byte index) {
		Index = index;
	}
	public Byte getCheckSum() {
		return checkSum;
	}
	public void setCheckSum(Byte checkSum) {
		this.checkSum = checkSum;
	}
	public long getNumTime() {
		return numTime;
	}
	public void setNumTime(long numTime) {
		this.numTime = numTime;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public Byte getPress() {
		return press;
	}
	public void setPress(Byte press) {
		this.press = press;
	}
}
