package com.example.server;

public class FingerPrinting {
	private double latitude;
	private double longitude;
	private long BS1,BS2,BS3,BS4,BS5,BS6,BS7,BS8;
	private int Rssi1,Rssi2,Rssi3,Rssi4,Rssi5,Rssi6,Rssi7,Rssi8;
	public FingerPrinting(){
		this.latitude=0.0;
		this.longitude=0.0;
		this.BS1=0;
		this.BS2=0;
		this.BS3=0;
		this.BS4=0;
		this.BS5=0;
		this.BS6=0;
		this.BS7=0;
		this.BS8=0;
		this.Rssi1=0;
		this.Rssi2=0;
		this.Rssi3=0;
		this.Rssi4=0;
		this.Rssi5=0;
		this.Rssi6=0;
		this.Rssi7=0;
		this.Rssi8=0;
		
		
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setBS1(long bS1) {
		BS1 = bS1;
	}
	public void setBS2(long bS2) {
		BS2 = bS2;
	}
	public void setBS3(long bS3) {
		BS3 = bS3;
	}
	public void setBS4(long bS4) {
		BS4 = bS4;
	}
	public void setBS5(long bS5) {
		BS5 = bS5;
	}
	public void setBS6(long bS6) {
		BS6 = bS6;
	}
	public void setBS7(long bS7) {
		BS7 = bS7;
	}
	public void setBS8(long bS8) {
		BS8 = bS8;
	}
	public void setRssi1(int rssi1) {
		Rssi1 = rssi1;
	}
	public void setRssi2(int rssi2) {
		Rssi2 = rssi2;
	}
	public void setRssi3(int rssi3) {
		Rssi3 = rssi3;
	}
	public void setRssi4(int rssi4) {
		Rssi4 = rssi4;
	}
	public void setRssi5(int rssi5) {
		Rssi5 = rssi5;
	}
	public void setRssi6(int rssi6) {
		Rssi6 = rssi6;
	}
	public void setRssi7(int rssi7) {
		Rssi7 = rssi7;
	}
	public void setRssi8(int rssi8) {
		Rssi8 = rssi8;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public long getBS1() {
		return BS1;
	}
	public long getBS2() {
		return BS2;
	}
	public long getBS3() {
		return BS3;
	}
	public long getBS4() {
		return BS4;
	}
	public long getBS5() {
		return BS5;
	}
	public long getBS6() {
		return BS6;
	}
	public long getBS7() {
		return BS7;
	}
	public long getBS8() {
		return BS8;
	}
	public int getRssi1() {
		return Rssi1;
	}
	public int getRssi2() {
		return Rssi2;
	}
	public int getRssi3() {
		return Rssi3;
	}
	public int getRssi4() {
		return Rssi4;
	}
	public int getRssi5() {
		return Rssi5;
	}
	public int getRssi6() {
		return Rssi6;
	}
	public int getRssi7() {
		return Rssi7;
	}
	public int getRssi8() {
		return Rssi8;
	}
    
}
