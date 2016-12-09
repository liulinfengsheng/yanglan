package com.example.server;

import java.io.DataOutputStream;
import java.io.IOException;

public class SocketSend implements Runnable {
	
	private DataOutputStream dout;
	//¹¹Ôìº¯Êý
	public SocketSend(DataOutputStream dout){
		super();
		this.dout=dout;
	}

	@Override
	public void run() {
		while(true){
			String message="good";
			try {
				dout.writeUTF(message);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}

	}

}
