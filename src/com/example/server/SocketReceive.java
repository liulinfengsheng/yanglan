package com.example.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JTextArea;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//�������˵������߳��ִ࣬�ж�λ����
//���̵߳ķ���ʵ��
public class SocketReceive extends Thread {
	private Socket s=null;//���嵱ǰ�߳��������socket
	private JTextArea area;
	private BufferedReader in=null;//������
	private StringBuffer fingerprinting2;
	private String lac,cid,bs,rssi;
	private JSONArray neibor;
	private String neibs;
	private Point locaResultPoint;
	double locationX,locationY;
	private DBRead dbRead;
	private JSONObject resultdata;
	private JSONObject resultMsg;
	private BufferedWriter Writer;

	
	//���캯��
	public SocketReceive(Socket s,JTextArea area2) throws IOException{
		this.s=s;
		this.area=area2;
//		in=new BufferedReader(new InputStreamReader(s.getInputStream()));
	}

	@Override
	public void run() {
		
		
			try{
				//���տͻ��˷��͵���Ϣ
				in=new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8"));
//                while(true){
//                	resultMsg=ReceiveMsg(s);
//                	
//                	SendMsg(s,resultMsg);
//                	if(true)
//                		break;
//                }
                   
//                CloseSocket(s);
				String content2=in.readLine();
				while(content2!=null){
					JSONObject receiveJson2=JSONObject.fromObject(content2);
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
					
//					dbRead=new DBRead(bs,area);
//					dbRead.DatabaseRead();//��������Ϊ����֤�ܷ�����ݿ�����
	                 resultdata=new JSONObject();  
	                 resultdata.put("longi", result.getX());
	                 resultdata.put("lat", result.getY());
//	                 for(Socket s:LocaMain.socketList){
	                	 PrintStream ps=new PrintStream(s.getOutputStream());
	                	 ps.println(resultdata.toString()+"\n");
//	                	 ps.println("nihaoya"+"\n");
//	                 }
//	                 OutputStream out = s.getOutputStream();
//	                 PrintWriter bufferWriter = new PrintWriter(out, true);
//	                
					//ObjectOutputStream out =new ObjectOutputStream(s.getOutputStream());//os�Ƿ�����socket�����
//	                 PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),true);
//					System.out.println(resultdata.toString()+"\n");
//					//out.writeUTF(resultdata.toString()+"\n");
////					out.println("good!");//��֤
//					bufferWriter.println("test writer");
//				 
//					
//					out.flush();
//					out.close();//�ر������
//					in.close();//�ر�������
//					area.append("\n"+"  "+"�ȴ�socket���Ӷ�λ����..."+"\n"+"\n");
//					}
//			    }
//				catch(Exception e)
//				{	
//					System.out.println("error happends");
//				}
//				finally
//				{
//					try {
//						s.close();
//					} catch (IOException e) {
//						
//						e.printStackTrace();
//					}
//					System.out.println("socket close");
				}
			
		    }
			catch(IOException e){
				System.out.println(e);				
			}
	}

//	private void CloseSocket(Socket s2) throws IOException {
//		in.close();
//		Writer.close();
//		s.close();
//		System.out.println("socket close ...");
//		
//	}
//
//	private void SendMsg(Socket s2, JSONObject resultdata2) throws IOException {
//		Writer =new BufferedWriter(new OutputStreamWriter(s2.getOutputStream()));
//		Writer.write(resultdata2+"\n");
//		Writer.flush();
//		
//	}
//
//	private JSONObject ReceiveMsg(Socket s2) throws IOException {
//		in=new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8"));
//		String content2=in.readLine();
//		while(content2!=null){
//			JSONObject receiveJson2=JSONObject.fromObject(content2);
//			fingerprinting2=new StringBuffer();
//			
//			FingerPrinting refinger=new FingerPrinting();//��������ָ��
//			HashMap<Long,Long> remap=new HashMap<Long,Long>();
//			
//
//			lac=receiveJson2.getString("lac");
//			cid=receiveJson2.getString("cid");
//			bs=lac+cid;
//			fingerprinting2.append(bs+",");
//			rssi=receiveJson2.getString("rssi");
//			fingerprinting2.append(rssi+",");
//			
//			remap.put(Long.parseLong(bs), Long.parseLong(rssi));//����ǿ��վ��Ϣ��������ָ�ƶ�
//			refinger.setStrongBS(Long.parseLong(bs));
//							
//			neibor=receiveJson2.getJSONArray("neibor");
//			
//			for(int i=0;i<neibor.size();i++)
//			{
//				neibs=neibor.getJSONObject(i).getString("neiLac")+neibor.getJSONObject(i).getString("neiCid");				
//        	    fingerprinting2.append(neibs+",");
//        	    fingerprinting2.append(neibor.getJSONObject(i).getString("neiRssi")+",");	
//        	    remap.put(Long.parseLong(neibs), Long.parseLong(neibor.getJSONObject(i).getString("neiRssi")));//���ڽ���վ��Ϣ��������ָ��
//			}
//			fingerprinting2.deleteCharAt(fingerprinting2.length()-1);
//			//��ӡ����λָ����Ϣ
//			area.append(fingerprinting2.toString()+"\n");
//			//ִ�ж�λ�㷨
//			KNN location=new KNN(refinger,bs,area);
//			Point result=location.locationCalulate();
//			area.append("���ȣ�"+result.getX()+"   γ�ȣ�"+result.getY());
//			
//			  resultdata=new JSONObject();  
//              resultdata.put("longi", result.getX());
//              resultdata.put("lat", result.getY());
////             
//		
//	}
//		
//		 return resultdata;
//	}
//

    
  }
