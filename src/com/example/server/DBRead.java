package com.example.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JTextArea;
//��ȡ�������ݿ��е�ָ����Ϣ
public class DBRead {
	public String StrongBS;
	public JTextArea area;
	private ResultSet rs;
	private Vector vector;
	private static final String url="jdbc:mysql://localhost:3306/locationdemo?autoReconnect=true&useSSL=false";
	private static final String name="root";
	private static final String password="920627";
//	
//	private double []datax=new double[100];
//	private double []datay=new double[100];
//	private long []datab=new long[100];
//	private long []datas=new long[100];
//	private long []datar=new long[100];
//	
	
	
	
	public DBRead(String Strong_BS, JTextArea area2) {
		this.StrongBS=Strong_BS;
		this.area=area2;
	}

	public Vector DatabaseRead(){
		try {
			Class.forName("com.mysql.jdbc.Driver");//����Mysql jdbc��������
			area.append("Success loading Mysql Driver!\n");

		} catch (ClassNotFoundException e) {	
			area.append("Error loading Mysql Driver!\n");
			e.printStackTrace();
		}
    	try {
    		
			Connection connect =DriverManager.getConnection(url,name,password);//�������Ӷ���
			area.append("Success connect Mysql server!\n");
//			String sql="select * from fingerprinting where Strong_BaseID="+StrongBS;//�����ݿ�
			String sql="select * from fingerprinting where BS_1="+StrongBS;
			Statement stmt=connect.createStatement();//����Statement����
			rs=stmt.executeQuery(sql);// ����SQL����ѯ�����(����)
			 vector=new Vector();
			area.append("id"+"    "+"longi"+"     "+"lat"+"          "+"BS1"+"     "+"rssi1"+"    "+"BS2"+"    "+"rssi2"+"    "+"BS3"+"       "+"rssi3"+"  "+"BS4"+"       "+"rssi4"+"    "
					+"BS5"+"      "+"rssi5"+"  "+"BS6"+"      "+"rssi6"+"       "+"BS7"+"    "+"rssi7"+"    "+"BS8"+"    "+"rssi8"+"  "+"\n");
			while(rs.next()){
				//***�洢��ָ�ƶ�����*********************************
				FingerPrinting fingerprint=new FingerPrinting();
				fingerprint.setFingerId(rs.getInt("id"));			
				Point pointxy=new Point();
				pointxy.setX(rs.getDouble("Longi"));
				pointxy.setY(rs.getDouble("lat"));
				fingerprint.setPoint(pointxy);
				fingerprint.setStrongBS(rs.getLong("BS_1"));
				HashMap<Long,Long> map = new HashMap<Long,Long>();
				map.put(rs.getLong("BS_1"), rs.getLong("rssi_1"));
				map.put(rs.getLong("BS_2"), rs.getLong("rssi_2"));
				map.put(rs.getLong("BS_3"), rs.getLong("rssi_3"));
				map.put(rs.getLong("BS_4"), rs.getLong("rssi_4"));
				map.put(rs.getLong("BS_5"), rs.getLong("rssi_5"));
				map.put(rs.getLong("BS_6"), rs.getLong("rssi_6"));
				map.put(rs.getLong("BS_7"), rs.getLong("rssi_7"));
				map.put(rs.getLong("BS_8"), rs.getLong("rssi_8"));
				fingerprint.setMap(map);
				vector.addElement(fingerprint);
				//**********************************************
				//**************************�ڷ�������������ʾ������ǿ��վɸѡ��ָ��*************************
				String f1=rs.getInt("id")+"  "+rs.getDouble("longi")+"  "+rs.getDouble("lat")+"   "
                         +rs.getLong("BS_1")+"  "+rs.getLong("rssi_1")+"  "+rs.getLong("BS_2")+"  "+rs.getLong("rssi_2")+"   "
                         +rs.getLong("BS_3")+"  "+rs.getLong("rssi_3")+"  "+rs.getLong("BS_4")+"  "+rs.getLong("rssi_4")+"   "
                         +rs.getLong("BS_5")+"  "+rs.getLong("rssi_5")+"   "+rs.getLong("BS_6")+"  "+rs.getLong("rssi_6")+"   "
                         +rs.getLong("BS_7")+"  "+rs.getLong("rssi_7")+"  "+rs.getLong("BS_8")+"  "+rs.getLong("rssi_8")+"\n";
				area.append(f1);
				//*******************************************************************************
				
			}
		
			//**************���ڼ��fingerprintingָ�����Ƿ���������***************
//			Iterator ite =vector.iterator();
//			Long temp=3283835804L;
//			while(ite.hasNext())
//			{
//				FingerPrinting finger1=new FingerPrinting();
//				finger1=(FingerPrinting) ite.next();
//				String f1=finger1.getFingerId()+"  "+String.valueOf(finger1.getPoint().getX())+"     "+String.valueOf(finger1.getPoint().getY())+"    "
//				+String.valueOf(finger1.getMap().get(temp));
//				System.out.println(f1);
//			}
			//**********************************************************
				
				
				
				
				
				
//			 int i=0;
////			 int p=1;
//			 long number=0;
//			 Vector vector=new Vector();
//			 FingerPrinting fingerprint=new FingerPrinting();
//			 while(rs.next()){	
//				 if(number!=rs.getLong("number")){
//					 if(fingerprint.isEmpty()==false){
//						 vector.addElement(fingerprint);
////						 fingerprint=new FingerPrinting();
//						 System.out.println(fingerprint.getPoint().getX());
//
//					 }
//					 number = rs.getLong("number");
//					 Point pointxy=new Point();
//					 pointxy.setX(rs.getDouble("Longitude"));
//					 pointxy.setY(rs.getDouble("Latitude"));
//					 fingerprint.setPoint(pointxy);
//					 fingerprint.setStrongBS(rs.getLong("Strong_BaseID"));
//					 HashMap<Long,Long> map=new HashMap<Long,Long>();
//					 map.put(rs.getLong("BaseID"), rs.getLong("RSRP"));
//					 fingerprint.setMap(map);
//				 }
//			 
//				 while(rs.getLong("Number")==p)
//				 {
//				 else{					
//					HashMap<Long,Long> map=new HashMap<Long,Long>();
//					map.put(rs.getLong("BaseID"), rs.getLong("RSRP"));
//					fingerprint.setMap(map);
//				 }
//				 }

//				 datab[i]=rs.getLong("BaseID");
//				 datas[i]=rs.getLong("Strong_BaseID");
//				 datar[i]=rs.getLong("RSRP");
//				 datax[i]=rs.getDouble("Longitude");
//				 datay[i]=rs.getDouble("Latitude");
//				 i++;
//			 }
//			 Iterator ite = vector.iterator();
//			 while(ite.hasNext()){  
//				 FingerPrinting fingerp = (FingerPrinting) ite.next();
//				 System.out.println(fingerp.getMap().);
//		            //do something  
//		        }  
			 
			 
//			area.append("         "+"��վ��"+"    "+"��ǿ��վ��    "+"   "+"�ź�ǿ��  "+"��������"+"  "+"γ������   "+"\n"+"\n");
//			for(int j=0;j<20;j++){
//				area.append("    "+"    "+datab[j]+"   "+datas[j]+"   "+datar[j]+"    "+datax[j]+"   "+datay[j]+"\n"+"\n");
//			}
			 
			stmt.close();//�ر������������
			connect.close();
    	}catch(SQLException e)
    	{
    		e.printStackTrace();
    		area.append("���ݿ����Ӵ���");
    		System.exit(0);
    	}
	
		return vector;
	}
	

}
