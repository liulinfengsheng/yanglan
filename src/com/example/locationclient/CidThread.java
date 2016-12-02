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

//Java���д����̵߳����ַ�����ʵ��Runnable�ӿڣ��̳�Thread��
//1.ʵ��Runnable�ӿ��е�run()��������run()�����м������Ĵ����߼�
//2.����Runnable�ӿ�ʵ����Ķ���Ҳ����CidThread�࣬���Ķ�����CidActivity.java�е�send�ӿ��д���
//3.����Thread�����start()�����������߳�

/**
 * @author ����
 * Handler����������������߳�������UI����ĸ��£�������⣬��ΪUI����ֻ�������߳��и���
 * CidActivity�߳��ж���Handler���������߳�CidThreadͨ��Message�������������ݣ�Handler����Message���뵽MessageQueue�У�Looper������
 * ��ȡ��Ҫ�����Message���󴫵ݸ�Handler�����handleMessage()�������д���
 */
public class CidThread implements Runnable {
	
	public Handler mHandler;
	private Socket s;
	BufferedReader br=null;//���ַ��������ж�ȡ�ı�����������ַ�
	OutputStream os=null;//�����

	
	@Override
	public void run() {
		
		try {
			s=new Socket("192.168.1.100",30005);
			os=s.getOutputStream();//���������os
						
			Looper.prepare();//1.Ϊ��ǰ�̴߳���Looper���󣬲���֮������MessageQueue,prepare()������֤ÿ���߳����ֻ��һ��Looper����
		    mHandler = new Handler() {//2.����Handler�����ʵ������дhandMessage()���������������������̵߳���Ϣ��revHandler�Ƕ����Handler���󣬸������Ϣ���͸�MessageQueue����������Looper�ָ�������Ϣ
				@Override
				public void handleMessage(Message msg) {
					if(msg.what==0x111){
					Log.d("handler", "handler"+msg.obj.toString());//�����������߳̽��յ������Ϣû��
					System.out.println("current thread:"+Thread.currentThread());//������					
						try {
						os.write((msg.obj.toString()+"\n").getBytes("utf-8"));//����Ϣд�뵽�����os������
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					
				}
					
				}

			};
			Looper.loop();//3.����Looper��loop��������������Looper.ÿ������MessageQueue�д���һ����Ϣ���ͻὫ��ȡ���������ݵ�handler��handleMeesage����������
			
		} catch (SocketTimeoutException e) {
			Log.d("out", "��ʱ����");
			System.out.println("�������ӳ�ʱ��");
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}

}
