package com.example.server;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JTextArea;
public class KNN {
    private FingerPrinting refinger;
    private String bs;
	private JTextArea area;
	private DBRead dbRead;
	private Vector vector;
	private HashMap<Long, Long> remap;
	//构造函数，获取定位所需各种信息
	public KNN(FingerPrinting refinger,String bs,JTextArea area1)
	{
		this.refinger=refinger;
		this.bs=bs;
		this.area=area1;
		dbRead =new DBRead(bs,area);
		vector=dbRead.DatabaseRead();
	}
	
	public Point locationCalulate(){
		
		Iterator ite=vector.iterator();
		HashMap<Double,Point>disrsrp=new HashMap<Double,Point>();
		Vector<Double> sortdisrsrp=new Vector<Double>();
		
		while(ite.hasNext()){
			FingerPrinting temfinger=new FingerPrinting();
			temfinger=(FingerPrinting) ite.next();//数据库中指纹信息
			
			 remap=refinger.getMap();//待测点的指纹信息
			 for(Long key:remap.keySet())
			 {
				 if(temfinger.getMap().containsKey(key)==true)//如果数据库中指纹包含某个基站
					 continue;
				 else//如果数据库中指纹不包含某个基站
				 {
					 
					 temfinger.getMap().put(key, (long) -110);//将这个基站的信号强度赋值为-110存入数据库中指纹
				 }
			 
		   }
			 //求欧氏距离*************************************
			long distance=0;
			for(Long key2:remap.keySet()){
				long m=remap.get(key2)-temfinger.getMap().get(key2);//距离之差
				distance=m*m+distance;
			}
			double dis=Math.sqrt(distance);
			disrsrp.put(dis, temfinger.getPoint());
			sortdisrsrp.addElement(dis);
			//*********************************************			 
		}
		Collections.sort(sortdisrsrp);
		
		
		Point []sum=new Point[3];
		sum[0]=disrsrp.get(sortdisrsrp.elementAt(0));
		sum[1]=disrsrp.get(sortdisrsrp.elementAt(1));
		sum[2]=disrsrp.get(sortdisrsrp.elementAt(2));
		
		double x,y;
		x=(sum[0].getX()+sum[1].getX()+sum[2].getX())/3;
		y=(sum[0].getY()+sum[1].getY()+sum[2].getY())/3;
	
		
		Point resultPoint=new Point();
		resultPoint.setX(x);
		resultPoint.setY(y);
		
		
		return resultPoint;
		
	}
	

}
