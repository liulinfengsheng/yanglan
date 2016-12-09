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

//服务器端的连接线程类，执行定位功能
//多线程的方法实现
public class SocketReceive extends Thread {
	private Socket s=null;//定义当前线程所处理的socket
	private JTextArea area;
	private BufferedReader in=null;//输入流
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

	
	//构造函数
	public SocketReceive(Socket s,JTextArea area2) throws IOException{
		this.s=s;
		this.area=area2;
//		in=new BufferedReader(new InputStreamReader(s.getInputStream()));
	}

	@Override
	public void run() {
		
		
			try{
				//接收客户端发送的消息
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
					
					FingerPrinting refinger=new FingerPrinting();//定义待测点指纹
					HashMap<Long,Long> remap=new HashMap<Long,Long>();
					

					lac=receiveJson2.getString("lac");
					cid=receiveJson2.getString("cid");
					bs=lac+cid;
				fingerprinting2.append(bs+",");
					rssi=receiveJson2.getString("rssi");
					fingerprinting2.append(rssi+",");
					
					remap.put(Long.parseLong(bs), Long.parseLong(rssi));//将最强基站信息存入待测点指纹额
					refinger.setStrongBS(Long.parseLong(bs));
									
					neibor=receiveJson2.getJSONArray("neibor");
					
					for(int i=0;i<neibor.size();i++)
					{
						neibs=neibor.getJSONObject(i).getString("neiLac")+neibor.getJSONObject(i).getString("neiCid");				
	            	    fingerprinting2.append(neibs+",");
	            	    fingerprinting2.append(neibor.getJSONObject(i).getString("neiRssi")+",");	
	            	    remap.put(Long.parseLong(neibs), Long.parseLong(neibor.getJSONObject(i).getString("neiRssi")));//将邻近基站信息存入待测点指纹
					}
					fingerprinting2.deleteCharAt(fingerprinting2.length()-1);
					//打印待定位指纹信息
					area.append(fingerprinting2.toString()+"\n");
					
					
					//执行定位算法
					KNN location=new KNN(refinger,bs,area);
					Point result=location.locationCalulate();
					area.append("经度："+result.getX()+"   纬度："+result.getY());
					
//					dbRead=new DBRead(bs,area);
//					dbRead.DatabaseRead();//这两句是为了验证能否读数据库数据
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
					//ObjectOutputStream out =new ObjectOutputStream(s.getOutputStream());//os是服务器socket输出流
//	                 PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),true);
//					System.out.println(resultdata.toString()+"\n");
//					//out.writeUTF(resultdata.toString()+"\n");
////					out.println("good!");//验证
//					bufferWriter.println("test writer");
//				 
//					
//					out.flush();
//					out.close();//关闭输出流
//					in.close();//关闭输入流
//					area.append("\n"+"  "+"等待socket连接定位请求..."+"\n"+"\n");
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
//			FingerPrinting refinger=new FingerPrinting();//定义待测点指纹
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
//			remap.put(Long.parseLong(bs), Long.parseLong(rssi));//将最强基站信息存入待测点指纹额
//			refinger.setStrongBS(Long.parseLong(bs));
//							
//			neibor=receiveJson2.getJSONArray("neibor");
//			
//			for(int i=0;i<neibor.size();i++)
//			{
//				neibs=neibor.getJSONObject(i).getString("neiLac")+neibor.getJSONObject(i).getString("neiCid");				
//        	    fingerprinting2.append(neibs+",");
//        	    fingerprinting2.append(neibor.getJSONObject(i).getString("neiRssi")+",");	
//        	    remap.put(Long.parseLong(neibs), Long.parseLong(neibor.getJSONObject(i).getString("neiRssi")));//将邻近基站信息存入待测点指纹
//			}
//			fingerprinting2.deleteCharAt(fingerprinting2.length()-1);
//			//打印待定位指纹信息
//			area.append(fingerprinting2.toString()+"\n");
//			//执行定位算法
//			KNN location=new KNN(refinger,bs,area);
//			Point result=location.locationCalulate();
//			area.append("经度："+result.getX()+"   纬度："+result.getY());
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
