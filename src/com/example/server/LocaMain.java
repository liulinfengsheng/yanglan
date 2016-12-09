package com.example.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JTextArea;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author ����
 *  �� ��λ�������˵�������
 */
public class LocaMain extends Thread {
	private JTextArea area=new JTextArea();
	private Socket s;
	private BufferedReader in;
	private StringBuffer fingerprinting2;
	private String lac;
	private String cid;
	private String bs;
	private String rssi;
	private JSONArray neibor;
	private String neibs;
	private PrintStream out;
	private JSONObject resultdata;

	//���캯��
	public LocaMain(JTextArea area1)  {
		this.area = area1;
		
	}

	public void run() {
		
		
			
			 try {
				 ServerSocket ss = new ServerSocket(30006);//����serversocket
				while (true) {
					area.append("\n" +"�ȴ�Socket���Ӷ�λ����....." + "\n" + "\n");
					Socket s = ss.accept(); // �ȴ��ͻ��˵���������
					area.append("\n" +"��λsocket���ӳɹ���" + "\n");
//					SocketReceive socketRece=new SocketReceive(s,area);
//					socketRece.start(); //�յ��������������һ��������̴߳��������
					loc(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}		
		
		}

	private void loc(Socket s) {
		try {
			in=new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8"));
			String content=null;
			while((content=readFromClient())!=null){
				JSONObject receiveJson2=JSONObject.fromObject(content);
				fingerprinting2=new StringBuffer();
				
				FingerPrinting refinger=new FingerPrinting();//��������ָ��
				HashMap<Long,Long> remap=new HashMap<Long,Long>();
				

				lac=receiveJson2.getString("lac");
				cid=receiveJson2.getString("cid");
				bs=lac+cid;
			fingerprinting2.append(bs+",");
				rssi=receiveJson2.getString("rssi");
				fingerprinting2.append(rssi+",");
				
				remap.put(Long.parseLong(bs), Long.parseLong(rssi));//����ǿ��վ��Ϣ��������ָ�ƶ�
				refinger.setStrongBS(Long.parseLong(bs));
								
				neibor=receiveJson2.getJSONArray("neibor");
				
				for(int i=0;i<neibor.size();i++)
				{
					neibs=neibor.getJSONObject(i).getString("neiLac")+neibor.getJSONObject(i).getString("neiCid");				
            	    fingerprinting2.append(neibs+",");
            	    fingerprinting2.append(neibor.getJSONObject(i).getString("neiRssi")+",");	
            	    remap.put(Long.parseLong(neibs), Long.parseLong(neibor.getJSONObject(i).getString("neiRssi")));//���ڽ���վ��Ϣ��������ָ��
				}
				fingerprinting2.deleteCharAt(fingerprinting2.length()-1);
				//��ӡ����λָ����Ϣ
				area.append(fingerprinting2.toString()+"\n");
				
				
				//ִ�ж�λ�㷨
				KNN location=new KNN(refinger,bs,area);
				Point result=location.locationCalulate();
				area.append("���ȣ�"+result.getX()+"   γ�ȣ�"+result.getY());
				
				resultdata=new JSONObject();  
                resultdata.put("longi", result.getX());
                resultdata.put("lat", result.getY());
                
				out=new PrintStream(s.getOutputStream());
				out.println(resultdata.toString()+"\n");
				
			}
			
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
		}
		
	}

	private String readFromClient() {
		try {
			return in.readLine();//������ͨ��IO����
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
}