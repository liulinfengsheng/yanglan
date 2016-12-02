package com.example.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTextArea;
//import com.example.locationclient.CidInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author 杨兰
 * DBMain是一个线程类，③
 */
public class DBMain extends Thread {
	private BufferedReader is = null;//Socket的输入字节流
	
	private JTextArea area = new JTextArea();//文本区
	private BufferedReader br;
	private CreateTable table;
	private String operator,lac,cid,rssi,lat,longi;
    private int lac1,cid1,rssi1;

	private JSONArray neibor;
	private String bs;
	private int bs1;
	
	private String neibs;
	private StringBuffer fingerprinting;
	private double latitude,longitude;


	// 构造函数
	public DBMain(JTextArea area2) {
		this.area = area2;
	}
    
	//重写run()方法
	public void run() {

		try {
			ServerSocket ss = new ServerSocket(30005); // 创建服务端套接字，用于监听客户端Socket的连接请求
			//采用循环不断地接收来自客户端的请求
			while (true) {
				area.append("\n" + "        " + "等待Android客户端传送指纹信息....." + "\n");
				Socket s = ss.accept();// 此行代码会阻塞，将一直等待别人的连接，accept()方法会返回请求这个服务器的客户端的Socket实例
				area.append("\n" +"指纹socket连接成功！"+"\n" );
				creat(s);//creat()方法用来处理连接成功的Socket对象
			} // 等待客户端的连接请求
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void creat(Socket s)
	{
		try {
			//将socket对应的输入流包装成BufferedReader
			is=new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8"));//s.getInputStream()返回该Socket对象对应的输入流
			String content=null;
			
			while((content=readFromClient())!=null)
			{
				 JSONObject receiveJson=JSONObject.fromObject(content);           
				 fingerprinting=new StringBuffer();
				     
				     
		             lat=receiveJson.getString("lat");
		             longi=receiveJson.getString("longi");
		             fingerprinting.append(lat+",");
		             fingerprinting.append(longi+",");
		             
		             lac=receiveJson.getString("lac");
		             cid=receiveJson.getString("cid"); 
		             bs=lac+cid;
		             fingerprinting.append(bs+",");
		             
		             rssi=receiveJson.getString("rssi");
		             fingerprinting.append(rssi+",");
		             
		             neibor = receiveJson.getJSONArray("neibor");
		            for(int i=0;i<neibor.size();i++)
		            {
		            	neibs=neibor.getJSONObject(i).getString("neiLac")+neibor.getJSONObject(i).getString("neiCid");
	            	    fingerprinting.append(neibs+",");
	            	    fingerprinting.append(neibor.getJSONObject(i).getString("neiRssi")+",");	            	            	
		            }
		            fingerprinting.deleteCharAt(fingerprinting.length()-1);
		        //打印相关传送信息    
               area.append(fingerprinting.toString()+"\n");
               //将CID信息存入数据库
				table = new CreateTable(fingerprinting,area);
                table.DatabaseCreat();

		            
		            
			}
			 
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
	}


	private String readFromClient() {
		try {
			return is.readLine();//进行普通的IO操作
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
}
