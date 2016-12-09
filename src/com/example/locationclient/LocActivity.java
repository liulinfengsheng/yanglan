package com.example.locationclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LocActivity extends Activity {

	private Button locback,update,send,locate;
	private MapView mapView=null;//地图主控件
	private BaiduMap baiduMap=null;
	
	public LocationClient locationClient=null;
	BitmapDescriptor mCurrentMarker=null;
	boolean isFirstLoc=true;
	
	private TelephonyManager telephonymanager;
	private StringBuffer locInfo;
	protected int lac;
	protected int cid;
	
	private int signalstrengthvalue2;
	protected StringBuffer neighborInfo2;
	protected int neiLac2;
	protected int neiCid2;
	protected int neiRssi2;
	private TextView text;
	protected JSONObject neiborinfo2;
	protected JSONObject messJson;
	private Handler reHandler;
	private String locdata;
	private double jingdu,weidu;
	private Socket s2;
	private OutputStream out;
	private BufferedReader in;
	private double getlongi,getlat;
	private JSONObject locdata2;
	private MapStatusUpdate update22;//MapStatusUpdate类描述地图状态将要发生的变化
    private BMapManager bMapManager;//加载地图引擎
	@Override
 	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());//百度地图第一步，进行初始化操作，它一定要在setContentView前面
		setContentView(R.layout.loc_main);
		
		mapView=(MapView)this.findViewById(R.id.mapView);
		baiduMap=mapView.getMap();//BaiduMap类是地图的总控制器
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//设置百度地图的类型
		baiduMap.setMyLocationEnabled(true);
		
		locback=(Button)findViewById(R.id.locback);
		update=(Button)findViewById(R.id.update);
		send=(Button)findViewById(R.id.send);
		locate=(Button)findViewById(R.id.locate);
		
		text=(TextView)findViewById(R.id.text);
		messJson=new JSONObject();
				
		locback.setOnClickListener(new LocListener());
		update.setOnClickListener(new LocListener());
		send.setOnClickListener(new LocListener());
		locate.setOnClickListener(new LocListener());
	
	
	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==0x222)
			{
				locdata=msg.obj.toString();
				try {
					navigatoTo(locdata);
				} catch (JSONException e) {
					
					e.printStackTrace();
				}				 
                }			
		}
		
	};


	
	
	private class LocListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.locback:
				displaylocback();
				break;
			case R.id.update:
				displayupdate();
				break;
			case R.id.send:
				displaysend();
			case R.id.locate:
				displaylocate();
				break;
			default:
				break;
			}
			
		}

		
	}
	
	public void displaylocback() {
		Intent intent_loc1=new Intent(LocActivity.this,ClientMainActivity.class);
		startActivity(intent_loc1);	
		
	}
	public void displaysend() {
		new Thread(){

			@Override
			public void run() {

				try {
					s2=new Socket("192.168.1.103",30006);
					out=s2.getOutputStream();			
					out.write((messJson.toString()+"\n").getBytes("utf-8"));
					in=new BufferedReader(new InputStreamReader(s2.getInputStream()));
					String result=in.readLine();
					Message msg=new Message();
					msg.what=0x222;
					msg.obj=result;
					handler.sendMessage(msg);
					in.close();
					out.close();
					s2.close();
				} catch (UnknownHostException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
			
		}.start();
	}
	public void displaylocate() {
		System.out.println(getlongi+"  "+getlat);
		
	}
	public void displayupdate() {

		telephonymanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation gsmlocation = (GsmCellLocation) telephonymanager.getCellLocation();
		locInfo=new StringBuffer();
		
		 lac = gsmlocation.getLac();
	     cid = gsmlocation.getCid();
	     locInfo.append("\n"+"Lac:"+String.valueOf(lac)+" "+"Cid:"+String.valueOf(cid));         
    try {
    	messJson.put("lac",lac);
    	messJson.put("cid", cid);				
	} catch (JSONException e) {
		e.printStackTrace();
	}
     
     PhoneStateListener listener = new PhoneStateListener() {
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
				super.onSignalStrengthsChanged(signalStrength);
				int asu = signalStrength.getGsmSignalStrength(); 				
				if (asu != 99) {
					signalstrengthvalue2 = asu * 2 - 113;
				} else {
					signalstrengthvalue2 = asu;
				}
				locInfo.append(" "+"rssi:"+signalstrengthvalue2);
				try {
					messJson.put("rssi",signalstrengthvalue2);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			}

		};		         
     telephonymanager.listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);		     	
     locInfo.append(" "+"rssi:"+signalstrengthvalue2);	
     try {
    	 messJson.put("rssi",signalstrengthvalue2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
   
     List<NeighboringCellInfo> infos2 = telephonymanager.getNeighboringCellInfo();			         
     neighborInfo2=new StringBuffer();
		 neighborInfo2.append("\n"+"邻区基站总数 : " + String.valueOf(infos2.size()) + "\n");		 		 		 
		JSONArray jsonarray2 = new JSONArray();
		 if (!infos2.isEmpty()) {
			
			for (NeighboringCellInfo info1 : infos2) { // 根据邻区总数进行循环
				neiLac2=info1.getLac();
				neiCid2=info1.getCid();
				neiRssi2=-113+2*info1.getRssi();
				neiborinfo2=new JSONObject();
				try {
				neiborinfo2.put("neiLac", neiLac2);
				neiborinfo2.put("neiCid", neiCid2);
				neiborinfo2.put("neiRssi", neiRssi2);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}			 				
				jsonarray2.put(neiborinfo2);
		
				neighborInfo2.append("Lac: " + Integer.toString(neiLac2)); // 取出当前邻区的LAC		 				
				neighborInfo2.append(" Cid: " + Integer.toString(neiCid2)); // 取出当前邻区的CID		 				
				neighborInfo2.append(" Rssi: " + Integer.toString(neiRssi2) + "\n"); // 获取邻区基站信号强度		 							
				
			
			}
		}
		locInfo.append("\n"+neighborInfo2);
		try {	 			
		messJson.put("neibor", jsonarray2);
		System.err.println(messJson.toString());
	} catch (JSONException e) {
		e.printStackTrace();
	}		 				 				 			 			 		
       text.setText(locInfo);     
	}
	private void navigatoTo(String locdata) throws JSONException{		            
            
            	


			text.setText(" ");
			locdata2=new JSONObject(locdata);
			getlongi=locdata2.getDouble("longi");
			getlat=locdata2.getDouble("lat");
		
	//************************这一段代码是从BaiduMapsApiDemo上的baidumapsdk.demo中的LocationDemo.java里找到的****************	
            MyLocationData locData=new MyLocationData.Builder().latitude(getlat).longitude(getlongi).build();
            baiduMap.setMyLocationData(locData);
            if(isFirstLoc){
            	isFirstLoc=false;
            	LatLng ll=new LatLng(getlongi,getlat);
            	MapStatus.Builder builder=new MapStatus.Builder();
            	builder.target(ll).zoom(18.0f);
            	baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
             
  //*************************************************************************************************************

				
			
	}
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		baiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}
	

}
