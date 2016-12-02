package com.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JTextArea;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//服务器端的连接线程类，执行定位功能
//多线程的方法实现
public class SocketReceive extends Thread {
	private Socket s;
	private JTextArea area;
	private BufferedReader is;
	private StringBuffer fingerprinting2;
	private String lac,cid,bs,rssi;
	private JSONArray neibor;
	private String neibs;
	

	
	//构造函数
	public SocketReceive(Socket s,JTextArea area2){
		this.s=s;
		this.area=area2;
	}

	@Override
	public void run() {
		//接收客户端发送的消息
		try {
			is=new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8"));
			String content2=null;
			while((content2=readFromClient())!=null){
				JSONObject receiveJson2=JSONObject.fromObject(content2);
				fingerprinting2=new StringBuffer();

				lac=receiveJson2.getString("lac");
				cid=receiveJson2.getString("cid");
				bs=lac+cid;
				fingerprinting2.append(bs+",");
				rssi=receiveJson2.getString("rssi");
				fingerprinting2.append(rssi+",");
								
				neibor=receiveJson2.getJSONArray("neibor");
				
				for(int i=0;i<neibor.size();i++)
				{
					neibs=neibor.getJSONObject(i).getString("neiLac")+neibor.getJSONObject(i).getString("neiCid");
            	    fingerprinting2.append(neibs+",");
            	    fingerprinting2.append(neibor.getJSONObject(i).getString("neiRssi")+",");	           	    
				}
				fingerprinting2.deleteCharAt(fingerprinting2.length()-1);
				area.append(fingerprinting2.toString()+"\n");
				
				
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}

	private void splitData() {
		// TODO 自动生成的方法存根
		
	}

	private String readFromClient() {
		try {
			return is.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
