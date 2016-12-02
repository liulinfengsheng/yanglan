package com.example.locationclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

//Java当中创建线程的两种方法：实现Runnable接口，继承Thread类
//1.实现Runnable接口中的run()方法，在run()方法中加入具体的处理逻辑
//2.创建Runnable接口实现类的对象，也就是CidThread类，它的对象在CidActivity.java中的send接口中创建
//3.调用Thread对象的start()方法，启动线程

/**
 * @author 杨兰
 * Handler对象用来解决（子线程来更新UI界面的更新）这个问题，因为UI界面只能在主线程中更新
 * CidActivity线程中定义Handler对象，与子线程CidThread通过Message对象来传递数据，Handler对象将Message加入到MessageQueue中，Looper对象负责
 * 抽取出要处理的Message对象传递给Handler对象的handleMessage()方法进行处理。
 */
public class CidThread implements Runnable {
	
	public Handler mHandler;
	private Socket s;
	BufferedReader br=null;//从字符输入流中读取文本，缓冲各个字符
	OutputStream os=null;//输出流

	
	@Override
	public void run() {
		
		try {
			s=new Socket("192.168.1.100",30005);
			os=s.getOutputStream();//输出流对象os
						
			Looper.prepare();//1.为当前线程创建Looper对象，并随之配套有MessageQueue,prepare()方法保证每个线程最多只有一个Looper对象，
		    mHandler = new Handler() {//2.创建Handler子类的实例，重写handMessage()方法，负责处理来自其他线程的消息。revHandler是定义的Handler对象，负责把消息发送给MessageQueue，并负责处理Looper分给它的消息
				@Override
				public void handleMessage(Message msg) {
					if(msg.what==0x111){
					Log.d("handler", "handler"+msg.obj.toString());//用来测试子线程接收到这个消息没有
					System.out.println("current thread:"+Thread.currentThread());//测试用					
						try {
						os.write((msg.obj.toString()+"\n").getBytes("utf-8"));//将消息写入到输出流os对象中
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					
				}
					
				}

			};
			Looper.loop();//3.调用Looper的loop（）方法，启动Looper.每当发现MessageQueue中存在一条消息，就会将它取出，并传递到handler的handleMeesage（）方法中
			
		} catch (SocketTimeoutException e) {
			Log.d("out", "超时连接");
			System.out.println("网络连接超时！");
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}

}
