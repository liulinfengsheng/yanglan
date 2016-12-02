package com.example.locationclient;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LocActivity extends Activity {

	private Button locback,update,locate;
	private MapView mapView=null;
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
	private LocThread locThread;
	protected JSONObject neiborinfo2;
	protected JSONObject messJson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.loc_main);
		
		mapView=(MapView)this.findViewById(R.id.mapView);
		baiduMap=mapView.getMap();
		baiduMap.setMyLocationEnabled(true);
		
		locback=(Button)findViewById(R.id.locback);
		update=(Button)findViewById(R.id.update);
		locate=(Button)findViewById(R.id.locate);
		
		text=(TextView)findViewById(R.id.text);
		messJson=new JSONObject();
		
		
		locThread=new LocThread();
		new Thread(locThread).start();
		
		locback.setOnClickListener(new LocListener());
		update.setOnClickListener(new LocListener());
		locate.setOnClickListener(new LocListener());	

	}
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
	public void displaylocate() {
		try {
			Message msg = new Message();
			msg.what=0x123;
			msg.obj=messJson;
			LocThread.reHandler.sendMessage(msg);
			text.setText("指纹发送成功！");
		} catch (Exception e) {					
			e.printStackTrace();
		}
		
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
	

}
