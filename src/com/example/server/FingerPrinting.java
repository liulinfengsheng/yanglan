package com.example.server;

import java.util.HashMap;

public class FingerPrinting {
	private Point point;
	private Long StrongBS;
	private HashMap<Long,Long> map=new HashMap<Long,Long>();
	private int fingerId;
	//¹¹Ôìº¯Êý
	public FingerPrinting(Point point,Long StringBS,HashMap<Long,Long> map,int fingerId){
		this.point=point;
		this.StrongBS=StringBS;
		this.setMap(map);
		this.fingerId=fingerId;
	}
	public FingerPrinting()
	{
		this.point=null;
		this.StrongBS=(long) 0;
//		this.setMap(map);
		this.fingerId=0;
		
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public Long getStrongBS() {
		return StrongBS;
	}
	public void setStrongBS(Long strongBS) {
		StrongBS = strongBS;
	}
	public HashMap<Long,Long> getMap() {
		return map;
	}
	public void setMap(HashMap<Long,Long> map) {
		this.map = map;
	}
	public boolean isEmpty(){
		if(this.point==null) return true;
		else return false;
	}
	public int getFingerId() {
		return fingerId;
	}
	public void setFingerId(int fingerId) {
		this.fingerId = fingerId;
	}
	
}
