package com.example.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTextArea;

public class DBRead {
	public JTextArea area;
	private ResultSet rs;
	private static final String url="jdbc:mysql://localhost:3306/locationdemo?autoReconnect=true&useSSL=false";
	private static final String name="root";
	private static final String password="920627";
	public DBRead(JTextArea area1){
		this.area=area1;
	}
	
	public ResultSet DatabaseRead(){
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
			
			String sql="select * from fingerprinting ";//读数据库
			Statement stmt=connect.createStatement();//创建Statement对象
			 rs=stmt.executeQuery(sql);
			
    	}catch(SQLException e)
    	{
    		e.printStackTrace();
    	}
		return rs;
		
	}
	

}
