//package com.example.locationclient;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.PrintStream;
//import java.io.UnsupportedEncodingException;
//import java.net.Socket;
//import java.net.SocketTimeoutException;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//
//class LocThread implements Runnable {
//	private Socket s2;
//	private BufferedReader br2=null;
//	private OutputStream os2=null;
//	private double longi,lat;
//	private StringBuilder sBuilder;
//	public static  Handler reHandler;
//	private BufferedReader bReader=null;
//
//	@Override
//	public void run() {
//      try {
//		s2=new Socket("192.168.1.101",30006);		
//		
//		//********���մӷ���������Ϣ*********************************
//		 sBuilder=new StringBuilder();//���JSON�ַ���
//		 bReader=new BufferedReader(new InputStreamReader(s2.getInputStream()));//��ȡ��socket��Ӧ��������
//		for(String s=bReader.readLine();s!=null;s=bReader.readLine()){
//			sBuilder.append(s);
//		}
////		Message msg2=new Message();
////		msg2.what=0x222;
////		msg2.obj=sBuilder.toString();
////		LocActivity.this.
//		
//		
//		//******************************************************
//		os2=s2.getOutputStream();//��ȡ��socket��Ӧ�������
//		Looper.prepare();
//		reHandler=new Handler(){
//			@Override
//			public void handleMessage(Message msg) {
//				if(msg.what==0x123)
//				{
//					try {
//						os2.write((msg.obj.toString()+"\n").getBytes("utf-8"));
//						
//					}catch(UnsupportedEncodingException e ){
//						e.printStackTrace();
//					}
//					catch (IOException e) {
//						e.printStackTrace();
//					}
//					
//				}
//				
//			}
//			
//		};
//		Looper.loop();
//		
//		
//		
//
//		
////		try {
////			JSONObject jsonObject=new JSONObject(sBuilder.toString());
////			longi=jsonObject.getDouble("longi");
////			lat=jsonObject.getDouble("lat");
////		} catch (JSONException e) {
////			
////			e.printStackTrace();
////		}
//		//*****************************************************
//		 
//		
//		
//	} catch(SocketTimeoutException e){
//		System.out.println("�������ӳ�ʱ��");
//	}
//      catch (IOException e) {
//		e.printStackTrace();
//	}
//
//	}
//
//
//
//}
