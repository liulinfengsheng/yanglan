package com.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

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
	private BufferedReader is;
	private StringBuffer fingerprinting2;
	private String lac,cid,bs,rssi;
	private JSONArray neibor;
	private String neibs;

	//���캯��
	public LocaMain(JTextArea area1) {
		this.area = area1;
	}

	public void run() {
		try {
			ServerSocket ss = new ServerSocket(30006);
			
			while (true) {
				area.append("\n" +"�ȴ�Socket���Ӷ�λ����....." + "\n" + "\n");
				Socket s = ss.accept(); // �ȴ��ͻ��˵���������
				area.append("\n" +"��λsocket���ӳɹ���" + "\n");
				 SocketReceive socketRece=new SocketReceive(s,area);
				 socketRece.start(); //�յ��������������һ��������̴߳��������
//				process(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private void process(Socket s) {
//		try {
//			is=new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8"));
//			String content2=null;
//			while((content2=readFromClient())!=null){
//				JSONObject receiveJson2=JSONObject.fromObject(content2);
//				fingerprinting2=new StringBuffer();
//				lac=receiveJson2.getString("lac");
//				cid=receiveJson2.getString("cid");
//				bs=lac+cid;
//				fingerprinting2.append(bs+",");
//				
//				rssi=receiveJson2.getString("rssi");
//				fingerprinting2.append(rssi+",");
//				
//				neibor=receiveJson2.getJSONArray("neibor");
//				
//				for(int i=0;i<neibor.size();i++)
//				{
//					neibs=neibor.getJSONObject(i).getString("neiLac")+neibor.getJSONObject(i).getString("neiCid");
//            	    fingerprinting2.append(neibs+",");
//            	    fingerprinting2.append(neibor.getJSONObject(i).getString("neiRssi")+",");	
//            	    
//				}
//				fingerprinting2.deleteCharAt(fingerprinting2.length()-1);
//				area.append(fingerprinting2.toString()+"\n");
//				
//				
//			}
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
//
//	private String readFromClient() {
//		try {
//			return is.readLine();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return null;
//	}
}
