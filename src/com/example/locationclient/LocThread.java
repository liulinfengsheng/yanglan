package com.example.locationclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class LocThread implements Runnable {
	private Socket s2;
	private BufferedReader br2=null;
	private OutputStream os2=null;
	public static  Handler reHandler;

	@Override
	public void run() {
      try {
		s2=new Socket("192.168.1.100",30006);
		br2=new BufferedReader(new InputStreamReader(s2.getInputStream()));
		os2=s2.getOutputStream();
		Looper.prepare();
		reHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0x123)
				{
					try {
						os2.write((msg.obj.toString()+"\n").getBytes("utf-8"));
					}catch(UnsupportedEncodingException e ){
						e.printStackTrace();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
			
		};
		Looper.loop();
		
	} catch(SocketTimeoutException e){
		System.out.println("ÍøÂçÁ¬½Ó³¬Ê±£¡");
	}
      catch (IOException e) {
		e.printStackTrace();
	}

	}

}
