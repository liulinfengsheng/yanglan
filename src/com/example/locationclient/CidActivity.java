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
	protected JSONObject locationdata;//JSON对象
	protected StringBuffer neighborInfoStr;
	private LocationClient locationClient;
	boolean isFirstLoc=true;
	
	private MapView mapView;
	private BaiduMap baiduMap;
	
	private CidThread cidthread;	
	private Handler mHander;

	//百度地图的位置监听接口
	public BDLocationListener myListener=new BDLocationListener(){

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null )
				return;
			
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();			
			longi=location.getLongitude();//经度
			lat=location.getLatitude();//纬度
			coordinate.setText("经度:"+String.valueOf(longi)+"\n"+"纬度:"+String.valueOf(lat));//经纬度显示在手机界面上
			//1.1JSONObject对象locationdata封装经纬度信息
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
		
		Log.v("cidActivity", Thread.currentThread().getName()+"开始执行");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cid_main);		
		coordinate=(TextView)findViewById(R.id.coordinate);//物理坐标		
		cid_textview=(TextView)findViewById(R.id.cid);//基站信息
//***********************************百度地图获取物理坐标***************************************		
        //	百度地图获取位置	
		locationClient=new LocationClient(getApplicationContext());//声明LocationClient类,用getApplicationContext获取全进程有效的Context；
		locationClient.registerLocationListener(myListener);//注册监听函数	
		
          //LocationClientOption类用来设置定位SDK的定位方式
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true); // 打开GPS
			option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式,后期注意修改成仅设备定位模式，高精度定位模式是同时使用网络定位和GPS定位
			option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
			option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
			option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
			option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向					
			locationClient.setLocOption(option);
//*****************************************************************************************    
		back = (Button) findViewById(R.id.back);
		refresh=(Button)findViewById(R.id.refresh);
		send = (Button) findViewById(R.id.send);
		
		//使用实现Runnable接口的方式定义线程的启动线程的方法,不能移动到send()方法中的onclick()中来启动
         cidthread = new CidThread();//创建子线程
         new Thread(cidthread).start();//启动子线程
		
		 locationdata=new JSONObject();//JSON对象封装指纹信息	
		 
		 
	     
	     //三个按钮的监听	
	     back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent_cid1 = new Intent(CidActivity.this, ClientMainActivity.class);//第一个参数Context要求提供一个启动活动的上下文，第二个参数Class指定想要启动的目标活动
				startActivity(intent_cid1);//这个方法用于启动活动				
			}
	    	 
	     });
         refresh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//1.获取经纬度
				locationClient.start();//locationClient是百度地图对象，来获得经纬度信息
				
                //2.获取基站信息的准备工作              
				telmanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				cidInfo = new StringBuffer();//用来存储基站信息，字符串形式
				//2.1 获取运营商信息
				String operator = telmanager.getNetworkOperator();//获得运营商信息										
				//mcc移动国家码 mnc移动网络码
				mcc = Integer.parseInt(operator.substring(0, 3));
				mnc = Integer.parseInt(operator.substring(3));
				if (mcc == 460) {
					if (mnc == 0) {
						cidInfo.append("中国移动");
						try {
							locationdata.put("operator", "中国移动");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else if (mnc == 1) {
						cidInfo.append("中国联通");
						try {
							locationdata.put("operator", "中国联通");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else if (mnc == 2) {
						cidInfo.append("中国电信");
						try {
							locationdata.put("operator", "中国电信");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				//2.2获取服务基站号，Lac地区区域码 Cid一个移动基站
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
//******************************手机状态监听，用来获取服务基站的信号强度******************************888
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
				//2.3获取服务基站信号强度，RSSI
				telmanager.listen(listen, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
				cidInfo.append(" " + "rssi:" + signalstrengthvalue);
				try {
					locationdata.put("rssi", signalstrengthvalue);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				//2.4获取邻近基站基站号，信号强度
				List<NeighboringCellInfo> infos = telmanager.getNeighboringCellInfo();
				neighborInfo = new StringBuffer();//用来存储邻近基站信息，字符串形式
				neighborInfo.append("\n" + "邻区基站总数 : " + String.valueOf(infos.size()) + "\n");
				JSONArray jsonarray = new JSONArray();//JSON数组形式打包邻近基站信息
		
				if (!infos.isEmpty()) {

					for (NeighboringCellInfo info1 : infos) { // 根据邻区总数进行循环
						neiLac = info1.getLac();
						neiCid = info1.getCid();
						neiRssi = -113 + 2 * info1.getRssi();
						
													
						JSONObject neiborinfo = new JSONObject();//非常关键，不能乱放
						try {
							neiborinfo.put("neiLac", neiLac);
							neiborinfo.put("neiCid", neiCid);
							neiborinfo.put("neiRssi", neiRssi);

						} catch (JSONException e) {
							e.printStackTrace();
						}
						jsonarray.put(neiborinfo);//将neiborinfo对象放入jsonarray数组中
						
						neighborInfo.append("Lac: " + Integer.toString(neiLac)); // 取出当前邻区的LAC
						neighborInfo.append(" Cid: " + Integer.toString(neiCid)); // 取出当前邻区的CID
						neighborInfo.append(" Rssi: " + Integer.toString(neiRssi) + "\n"); // 获取邻区基站信号强度

					}
				}
				cidInfo.append("\n" + neighborInfo);
				try {
					locationdata.put("neibor", jsonarray);//将jsonarray数组放入 locationdata对象中
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				cid_textview.setText(cidInfo);//将基站信息显示在屏幕中
				
			}
        	 
         });
	     send.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
				
					//**创建消息****
				    Message msg=new Message();
				    msg.what=0x111;
					msg.obj = locationdata;//用来存放对象
					Log.d("DD", locationdata.toString());//用来测试JSON数据打包好了没有
					//*****向子线程中的Handler发送消息******
					cidthread.mHandler.handleMessage(msg);//让子线程处理主线程cidActivity发送的消息
					coordinate.setText(" ");
					cid_textview.setText("指纹发送成功！");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
	    	 
	     });
	     Log.d("cidActivity", Thread.currentThread().getName()+"结束执行");
    }



	// 三个状态实现地图生命周期管理
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
		


	


	

	


