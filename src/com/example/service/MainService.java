package com.example.service;

import com.example.connect.GLOBAL;
import com.example.connect.ClientMessage;
import com.example.connect.TCPClient;
import com.example.main.MainActivity;
import com.example.main.MyApplication;
import com.google.gson.Gson;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MainService extends Service {
	private TCPClient mClient;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Toast.makeText(this, "In Service", Toast.LENGTH_LONG).show();
//		mClient = ((MyApplication) getApplication()).mClient;
		mClient.setOnMessageListener(new TCPClient.onMessageReceived() {

			@Override
			public void MessageReceived(String msg) {
				// TODO Auto-generated method stub
				ProcessMessage(msg);
			}

			private void ProcessMessage(String msg) {
				// TODO Auto-generated method stub
				try {

					Gson gson = new Gson();
					ClientMessage message = gson.fromJson(msg, ClientMessage.class);
					Log.i("MAIN SERVICE", "Nhận dữ liệu từ server");
					switch (message.getMsgID()) {
	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
//		
		return START_STICKY;
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
