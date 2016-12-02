package com.example.locationclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CidActivity extends Activity {

	private TextView coordinate,cid_textview;
	private Button back,refresh,send;
	
	private TelephonyManager telmanager;
	private LocationManager locationManager;
	private String provider;
	private Location location;
	
	private int signalstrengthvalue;
	

	private StringBuffer neighborInfo;
	private StringBuffer cidInfo;
	protected int mcc;
	protected int mnc;
	protected int lac;
	protected int cid;
	private double lat;
	private double longi;
	
	private String MCC;
	private String MNC;
	private int LAC;
	private int CID;
	private String LAT;
	private String LONGI;
	private int RSSI;

	private PrintStream osss;
	FileOutputStream fos;
	private BufferedWriter writer;
	protected int neiLac;
	protected int neiCid;
	protected int neiRssi;
	protected JSONObject locationdata;//JSON����
	protected StringBuffer neighborInfoStr;
	private LocationClient locationClient;
	boolean isFirstLoc=true;
	
	private MapView mapView;
	private BaiduMap baiduMap;
	
	private CidThread cidthread;	
	private Handler mHander;

	//�ٶȵ�ͼ��λ�ü����ӿ�
	public BDLocationListener myListener=new BDLocationListener(){

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null )
				return;
			
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();			
			longi=location.getLongitude();//����
			lat=location.getLatitude();//γ��
			coordinate.setText("����:"+String.valueOf(longi)+"\n"+"γ��:"+String.valueOf(lat));//��γ����ʾ���ֻ�������
			//1.1JSONObject����locationdata��װ��γ����Ϣ
			try {				
				locationdata.put("longi", longi);
				locationdata.put("lat", lat);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (isFirstLoc) {
				isFirstLoc = false;									
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());	
			}
			
		}
		
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.v("cidActivity", Thread.currentThread().getName()+"��ʼִ��");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cid_main);		
		coordinate=(TextView)findViewById(R.id.coordinate);//��������		
		cid_textview=(TextView)findViewById(R.id.cid);//��վ��Ϣ
//***********************************�ٶȵ�ͼ��ȡ��������***************************************		
        //	�ٶȵ�ͼ��ȡλ��	
		locationClient=new LocationClient(getApplicationContext());//����LocationClient��,��getApplicationContext��ȡȫ������Ч��Context��
		locationClient.registerLocationListener(myListener);//ע���������	
		
          //LocationClientOption���������ö�λSDK�Ķ�λ��ʽ
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true); // ��GPS
			option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// ���ö�λģʽ,����ע���޸ĳɽ��豸��λģʽ���߾��ȶ�λģʽ��ͬʱʹ�����綨λ��GPS��λ
			option.setCoorType("bd09ll"); // ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
			option.setScanSpan(5000); // ���÷���λ����ļ��ʱ��Ϊ5000ms
			option.setIsNeedAddress(true); // ���صĶ�λ���������ַ��Ϣ
			option.setNeedDeviceDirect(true); // ���صĶ�λ��������ֻ���ͷ�ķ���					
			locationClient.setLocOption(option);
//*****************************************************************************************    
		back = (Button) findViewById(R.id.back);
		refresh=(Button)findViewById(R.id.refresh);
		send = (Button) findViewById(R.id.send);
		
		//ʹ��ʵ��Runnable�ӿڵķ�ʽ�����̵߳������̵߳ķ���,�����ƶ���send()�����е�onclick()��������
         cidthread = new CidThread();//�������߳�
         new Thread(cidthread).start();//�������߳�
		
		 locationdata=new JSONObject();//JSON�����װָ����Ϣ	
		 
		 
	     
	     //������ť�ļ���	
	     back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent_cid1 = new Intent(CidActivity.this, ClientMainActivity.class);//��һ������ContextҪ���ṩһ��������������ģ��ڶ�������Classָ����Ҫ������Ŀ��
				startActivity(intent_cid1);//����������������				
			}
	    	 
	     });
         refresh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//1.��ȡ��γ��
				locationClient.start();//locationClient�ǰٶȵ�ͼ��������þ�γ����Ϣ
				
                //2.��ȡ��վ��Ϣ��׼������              
				telmanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				cidInfo = new StringBuffer();//�����洢��վ��Ϣ���ַ�����ʽ
				//2.1 ��ȡ��Ӫ����Ϣ
				String operator = telmanager.getNetworkOperator();//�����Ӫ����Ϣ										
				//mcc�ƶ������� mnc�ƶ�������
				mcc = Integer.parseInt(operator.substring(0, 3));
				mnc = Integer.parseInt(operator.substring(3));
				if (mcc == 460) {
					if (mnc == 0) {
						cidInfo.append("�й��ƶ�");
						try {
							locationdata.put("operator", "�й��ƶ�");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else if (mnc == 1) {
						cidInfo.append("�й���ͨ");
						try {
							locationdata.put("operator", "�й���ͨ");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else if (mnc == 2) {
						cidInfo.append("�й�����");
						try {
							locationdata.put("operator", "�й�����");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				//2.2��ȡ�����վ�ţ�Lac���������� Cidһ���ƶ���վ
				GsmCellLocation gsmlocation = (GsmCellLocation) telmanager.getCellLocation();
				lac = gsmlocation.getLac();
				cid = gsmlocation.getCid();		      
				cidInfo.append("\n" + "Lac:" + String.valueOf(lac) + " " + "Cid:" + String.valueOf(cid));
		        
				try {
					locationdata.put("lac", lac);
					locationdata.put("cid", cid);
				} catch (JSONException e) {
					e.printStackTrace();
				}
//******************************�ֻ�״̬������������ȡ�����վ���ź�ǿ��******************************888
				PhoneStateListener listen = new PhoneStateListener() {
					public void onSignalStrengthsChanged(SignalStrength signalStrength) {
						super.onSignalStrengthsChanged(signalStrength);						
						int asu = signalStrength.getGsmSignalStrength();
						if (asu != 99) {
							signalstrengthvalue = asu * 2 - 113;
						} else {
							signalstrengthvalue = asu;
						}						
						cidInfo.append(" " + "rssi:" + signalstrengthvalue);						
						try {
							locationdata.put("rssi", signalstrengthvalue);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				};
				//2.3��ȡ�����վ�ź�ǿ�ȣ�RSSI
				telmanager.listen(listen, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
				cidInfo.append(" " + "rssi:" + signalstrengthvalue);
				try {
					locationdata.put("rssi", signalstrengthvalue);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				//2.4��ȡ�ڽ���վ��վ�ţ��ź�ǿ��
				List<NeighboringCellInfo> infos = telmanager.getNeighboringCellInfo();
				neighborInfo = new StringBuffer();//�����洢�ڽ���վ��Ϣ���ַ�����ʽ
				neighborInfo.append("\n" + "������վ���� : " + String.valueOf(infos.size()) + "\n");
				JSONArray jsonarray = new JSONArray();//JSON������ʽ����ڽ���վ��Ϣ
		
				if (!infos.isEmpty()) {

					for (NeighboringCellInfo info1 : infos) { // ����������������ѭ��
						neiLac = info1.getLac();
						neiCid = info1.getCid();
						neiRssi = -113 + 2 * info1.getRssi();
						
													
						JSONObject neiborinfo = new JSONObject();//�ǳ��ؼ��������ҷ�
						try {
							neiborinfo.put("neiLac", neiLac);
							neiborinfo.put("neiCid", neiCid);
							neiborinfo.put("neiRssi", neiRssi);

						} catch (JSONException e) {
							e.printStackTrace();
						}
						jsonarray.put(neiborinfo);//��neiborinfo�������jsonarray������
						
						neighborInfo.append("Lac: " + Integer.toString(neiLac)); // ȡ����ǰ������LAC
						neighborInfo.append(" Cid: " + Integer.toString(neiCid)); // ȡ����ǰ������CID
						neighborInfo.append(" Rssi: " + Integer.toString(neiRssi) + "\n"); // ��ȡ������վ�ź�ǿ��

					}
				}
				cidInfo.append("\n" + neighborInfo);
				try {
					locationdata.put("neibor", jsonarray);//��jsonarray������� locationdata������
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				cid_textview.setText(cidInfo);//����վ��Ϣ��ʾ����Ļ��
				
			}
        	 
         });
	     send.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
				
					//**������Ϣ****
				    Message msg=new Message();
				    msg.what=0x111;
					msg.obj = locationdata;//������Ŷ���
					Log.d("DD", locationdata.toString());//��������JSON���ݴ������û��
					//*****�����߳��е�Handler������Ϣ******
					cidthread.mHandler.handleMessage(msg);//�����̴߳������߳�cidActivity���͵���Ϣ
					coordinate.setText(" ");
					cid_textview.setText("ָ�Ʒ��ͳɹ���");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
	    	 
	     });
	     Log.d("cidActivity", Thread.currentThread().getName()+"����ִ��");
    }



	// ����״̬ʵ�ֵ�ͼ�������ڹ���
	@Override
	protected void onDestroy() {
		locationClient.stop();
		super.onDestroy();

	}

	@Override
	protected void onResume() {

		super.onResume();

	}

	@Override
	protected void onPause() {

		super.onPause();

	}

	
	
}
		


	


	

	


