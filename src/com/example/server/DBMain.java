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
 * @author ����
 * DBMain��һ���߳��࣬��
 */
public class DBMain extends Thread {
	private BufferedReader is = null;//Socket�������ֽ���
	
	private JTextArea area = new JTextArea();//�ı���
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


	// ���캯��
	public DBMain(JTextArea area2) {
		this.area = area2;
	}
    
	//��дrun()����
	public void run() {

		try {
			ServerSocket ss = new ServerSocket(30005); // ����������׽��֣����ڼ����ͻ���Socket����������
			//����ѭ�����ϵؽ������Կͻ��˵�����
			while (true) {
				area.append("\n" + "        " + "�ȴ�Android�ͻ��˴���ָ����Ϣ....." + "\n");
				Socket s = ss.accept();// ���д������������һֱ�ȴ����˵����ӣ�accept()�����᷵����������������Ŀͻ��˵�Socketʵ��
				area.append("\n" +"ָ��socket���ӳɹ���"+"\n" );
				creat(s);//creat()���������������ӳɹ���Socket����
			} // �ȴ��ͻ��˵���������
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void creat(Socket s)
	{
		try {
			//��socket��Ӧ����������װ��BufferedReader
			is=new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8"));//s.getInputStream()���ظ�Socket�����Ӧ��������
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
		        //��ӡ��ش�����Ϣ    
               area.append(fingerprinting.toString()+"\n");
               //��CID��Ϣ�������ݿ�
				table = new CreateTable(fingerprinting,area);
                table.DatabaseCreat();

		            
		            
			}
			 
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
	}


	private String readFromClient() {
		try {
			return is.readLine();//������ͨ��IO����
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
}
