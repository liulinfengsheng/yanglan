package com.example.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTextArea;

import com.mysql.jdbc.Driver;

/**
 * @author 杨兰
 *创建数据表
 */
public class CreateTable {

    private StringBuffer finprint;
    private String fingerprinting;
    private String[] array;
    private long[] data_array;
	private double lat,longi;
	private JTextArea area;
   
     
	
	private static final String url="jdbc:mysql://localhost:3306/locationdemo?autoReconnect=true&useSSL=false";
	private static final String name="root";
	private static final String password="920627";
	 
	  
	
	 
	 public CreateTable(StringBuffer fingerprinting2, JTextArea area2) {
		 this.finprint=fingerprinting2;
         this.fingerprinting=finprint.toString();
         this.area=area2;
	}

	public void splitData(){
		 array=fingerprinting.split(",");
		 lat=Double.parseDouble(array[0]);
		 longi=Double.parseDouble(array[1]);
		 
		 data_array=new long[16];
		 for(int i=0;(i+2)<array.length;i++)
		 {
			data_array[i]=Long.parseLong(array[i+2]);
			
					
		 }	
		 
	 }	  
	 
     public void DatabaseCreat(){
    	try {
			Class.forName("com.mysql.jdbc.Driver");//加载Mysql jdbc驱动程序
			area.append("Success loading Mysql Driver!\n");

		} catch (ClassNotFoundException e) {	
			area.append("Error loading Mysql Driver!\n");
			e.printStackTrace();
		}
    	try {
    		
			Connection connect =DriverManager.getConnection(url,name,password);//建立连接对象
			area.append("Success connect Mysql server!\n");
			splitData();
			String sql="insert into fingerprinting(lat,longi,BS_1,rssi_1,BS_2,rssi_2,BS_3,rssi_3,BS_4,rssi_4,BS_5,rssi_5,BS_6,rssi_6,BS_7,rssi_7,BS_8,rssi_8) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt;
			pstmt=(PreparedStatement)connect.prepareStatement(sql);	
			pstmt.setDouble(1, lat);
			pstmt.setDouble(2, longi);
			for(int i=0;i<16;i++)
			{
				pstmt.setLong(i+3, data_array[i]);
			}		
			pstmt.executeUpdate();	
			area.append("success insert data!\n");
			pstmt.close();
			connect.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
 	 
     }
	 
}
