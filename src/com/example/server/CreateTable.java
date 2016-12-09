package com.example.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private String[][]data_array2;
	private double lat,longi;
	private JTextArea area;
	private ResultSet rs;
	private boolean yes;
	private int valueid;
	private boolean tempyes;
	private int numbervalue;
   
     
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
		 longi=Double.parseDouble(array[0]);
		 lat=Double.parseDouble(array[1]);
		 
		 data_array=new long[16];
		 for(int i=0;(i+2)<array.length;i++)
		 {
			data_array[i]=Long.parseLong(array[i+2]);
			
					
		 }		
//		 int N=data_array.length/2;
//		 int M=5;
//		 data_array2=new String[N][M];
//		 int m=0;
//		 for (int i=0;i<N;i++)
//		 {
//			 data_array2[i][0]=Long.toString(data_array[m++]);
//			 data_array2[i][1]=data_array2[0][0];//这个地方别改了，是对的
//			 data_array2[i][2]=Long.toString(data_array[m++]);
//			 data_array2[i][3]=Double.toString(longi);
//			 data_array2[i][4]=Double.toString(lat);	
//		 }
		 
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
//			System.out.println(data_array2.length);
//			String sql="insert into fingerprinting(BaseID,Strong_BaseID,RSRP,Longitude,Latitude) values(?,?,?,?,?)";
			String sql="insert into fingerprinting(lat,longi,BS_1,rssi_1,BS_2,rssi_2,BS_3,rssi_3,BS_4,rssi_4,BS_5,rssi_5,BS_6,rssi_6,BS_7,rssi_7,BS_8,rssi_8) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//			PreparedStatement pstmt,pstmt2,pstmt3,pstmt4;
			PreparedStatement pstmt;
			pstmt=(PreparedStatement)connect.prepareStatement(sql);		
			
//            for(int i=0;i<data_array.length;i++)
//            {
////            	if(Long.parseLong(data_array2[i][0])==0)
//                    break;
//            	pstmt.setLong(1, Long.parseLong(data_array2[i][0]));
//            	pstmt.setLong(2, Long.parseLong(data_array2[i][1]));
//            	pstmt.setLong(3, Long.parseLong(data_array2[i][2]));        	
//            	pstmt.setDouble(4, Double.parseDouble(data_array2[i][3]));
//            	pstmt.setDouble(5, Double.parseDouble(data_array2[i][4]));
			    pstmt.setDouble(1, lat);
			    pstmt.setDouble(2, longi);
            	pstmt.setLong(3, data_array[0]);
            	pstmt.setLong(4, data_array[1]);
            	pstmt.setLong(5, data_array[2]);
            	pstmt.setLong(6, data_array[3]);
            	pstmt.setLong(7, data_array[4]);
            	pstmt.setLong(8, data_array[5]);
            	pstmt.setLong(9, data_array[6]);
            	pstmt.setLong(10, data_array[7]);
            	pstmt.setLong(11, data_array[8]);
            	pstmt.setLong(12, data_array[9]);
            	pstmt.setLong(13, data_array[10]);
            	pstmt.setLong(14, data_array[11]);
            	pstmt.setLong(15, data_array[12]);
            	pstmt.setLong(16, data_array[13]);
            	pstmt.setLong(17, data_array[14]);
            	pstmt.setLong(18, data_array[15]);
//            	        	
//           }	
            pstmt.executeUpdate();  
//            String sql2="select * from fingerprinting";
//            pstmt2=(PreparedStatement)connect.prepareStatement(sql2);
//            rs=pstmt2.executeQuery(sql2);
//            yes=rs.last();
////            System.out.println(yes);//调试是否指向每个指纹的最后一行
//            if(yes==true){
//            	valueid=rs.getInt(1);//取出指纹中最后一个id号
//            	numbervalue=valueid/8;
//            	String sql3="update fingerprinting set Number="+numbervalue+" where id="+valueid;
//            	  pstmt3=(PreparedStatement)connect.prepareStatement(sql3);
//                  pstmt3.executeUpdate();
//            	for(int i=0;i<7;i++)
//            	{
//            		if(rs.absolute(valueid-i-1)==true){
//            			String sql4="update fingerprinting set Number="+numbervalue+" where id="+(valueid-i-1);
//            			pstmt4=(PreparedStatement)connect.prepareStatement(sql4);
//            			pstmt4.executeUpdate();
//            		}
//            	}
//            }
   
            
			area.append("success insert data!\n");
			pstmt.close();
			connect.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
 	 
     }
	 
}
