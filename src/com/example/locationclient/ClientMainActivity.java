package com.example.locationclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ClientMainActivity extends Activity {

	private Button login,create,location,exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_main);
		create = (Button) findViewById(R.id.create);// cid…®√Ë
		location=(Button)findViewById(R.id.location);//location…®√Ë
		exit=(Button)findViewById(R.id.exit);//exit…®√Ë
		

		create.setOnClickListener(new MyListener());// cidº‡Ã˝
		location.setOnClickListener(new MyListener());
		exit.setOnClickListener(new MyListener());

	}

	private class MyListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {			
			case R.id.create:
				displayCidInfo();
				break;
			case R.id.location:
				displayLocInfo();
				break;
			case R.id.exit:
			    displayExit();
			    break;
			default:
				break;
			}

		}

	}

	public void displayLoginInfo() {

	}

	public void displayCidInfo() {

		Intent intent_main1 = new Intent(ClientMainActivity.this, CidActivity.class);
		startActivity(intent_main1);

	}

	public void displayLocInfo() {
		Intent intent_main2 = new Intent(ClientMainActivity.this, LocActivity.class);
		startActivity(intent_main2);
	}

	public void displayExit() {

		System.exit(0);
	}
}
