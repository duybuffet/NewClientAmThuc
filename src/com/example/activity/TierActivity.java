package com.example.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.TableAdapter;
import com.example.connect.ClientMessage;
import com.example.connect.GLOBAL;
import com.example.connect.TCPClient;
import com.example.connect.TCPClient.onMessageReceived;
import com.example.main.ApplicationMain;
import com.example.main.MainActivity;
import com.example.main.MyApplication;
import com.example.model.OrderDetails;
import com.example.model.Table;
import com.google.gson.Gson;
import com.ptit.amthuc.NotificationView;
import com.ptit.amthuc.R;

public class TierActivity extends Activity {
	TableAdapter adapter;
	GridView grid;
	ArrayList<Table> data, listTable;
	int vitri = 1;
	int tier = 1;
	TextView textTier;
	private static TCPClient mClient;
	static final Handler handler = new Handler();	
	public NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
			this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tiergid);
		textTier = (TextView) findViewById(R.id.txtView);
		textTier.setText("Tầng 1");
		// tao danh sach ban cho tang 1
		data = new ArrayList<Table>();
		if (ApplicationMain.listTable != null) {
			listTable = new ArrayList<Table>(ApplicationMain.listTable);
		}
		init(1);
		grid = (GridView) findViewById(R.id.gridView);
		adapter = new TableAdapter(this, R.layout.customview, data);
		grid.setAdapter(adapter);
		// bat su kien khi nha vao tung ban
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// vitri = arg2;
				Intent intent = new Intent(TierActivity.this,
						OrderDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("table_id", data.get(position).getId());
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});

		// request lien tuc
		addEventsListener();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		data.clear();
		listTable.clear();
		listTable.addAll(ApplicationMain.listTable);
		vitri = 1;
		tier = 1;
		init(1);
		adapter.notifyDataSetChanged();
		// request lien tuc
		addEventsListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	// khoi tao ban , tham so truyen vao la tang muon xem
	public void init(int tier1) {
		data.clear();
		switch (tier1) {
		case GLOBAL.AREA.FLOOR_1:
			textTier.setText("Tầng 1");
			for (Table tbl : listTable) {
				if (tbl.getArea() == GLOBAL.AREA.FLOOR_1) {
					data.add(tbl);
				}
			}
			break;

		case GLOBAL.AREA.FLOOR_2:
			textTier.setText("Tầng 2");
			for (Table tbl : listTable) {
				if (tbl.getArea() == GLOBAL.AREA.FLOOR_2) {
					data.add(tbl);
				}
			}
			break;
		case GLOBAL.AREA.FLOOR_3:
			textTier.setText("Tầng 3");
			for (Table tbl : listTable) {
				if (tbl.getArea() == GLOBAL.AREA.FLOOR_3) {
					data.add(tbl);
				}
			}
			break;

		}

	}

	// bat su kien back va next
	public void back(View v) {

		switch (tier) {
		case 1:
			break;

		case 2:
			tier = 1;
			init(1);
			textTier.setText("Tầng 1");

			break;
		case 3:
			tier = 2;
			init(2);
			textTier.setText("Tầng 2");

			break;
		}
		adapter.notifyDataSetChanged();

	}

	public void next(View v) {

		switch (tier) {
		case 1:
			tier = 2;
			init(2);
			textTier.setText("Tầng 2");

			break;

		case 2:
			tier = 3;
			init(3);
			textTier.setText("Tầng 3");

			break;
		case 3:

			break;
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void addEventsListener() {
		// TODO Auto-generated method stub
		mClient = ((MyApplication) getApplication()).mClient;
		mClient.setOnMessageListener(new onMessageReceived() {

			@Override
			public void MessageReceived(String msg) {
				// TODO Auto-generated method stub
				ProcessMessage(msg);
			}

			private void ProcessMessage(String msg) {
				// TODO Auto-generated method stub
				try {

					Gson gson = new Gson();
					final ClientMessage message = gson.fromJson(msg,
							ClientMessage.class);
					Log.e("Chef Activity", "Nhận dữ liệu từ server");
					switch (message.getMsgID()) {
					case GLOBAL.FROM_SERVER.LOAD_ORDER_DETAILS:
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Log.e("TIER ACTIVITY",
										"load order details");
								// TODO Auto-generated method stub
								handleUpdateOrderDetails(message
										.getArrOrderDetails());
							}

							private void handleUpdateOrderDetails(
									List<OrderDetails> arrOrderDetails) {
								// TODO Auto-generated method stub
								List<OrderDetails> oldList = ApplicationMain.listOrderDetailsForChef;
								List<OrderDetails> newList = message
										.getArrOrderDetails();

//								 Log.e("OLD SIZE" , oldList.size() + "");
//								 Log.e("NEW SIZE" , newList.size() + "");
								if (newList != null && oldList != null && newList.size() == oldList.size()){
									for (int i = 0; i < oldList.size(); i++) {
										OrderDetails newItem = newList.get(i);
										 Log.e("old item",
										 oldList.get(i).getStatus() + "");
										 Log.e("new item", newItem.getStatus() +
										 "");
										if (oldList.get(i).getStatus() != newItem
												.getStatus()) {
											switch (newItem.getStatus()) {
											case GLOBAL.ORDER_DETAILS_STATUS.FINISH:
												showMessage("Món "
														+ newItem.getDish()
																.getName()
														+ " hoàn thành!");
												Notify("ClientAmThuc", "Món "
														+ newItem.getDish()
																.getName()
														+ " hoàn thành!");
												break;
	
											case GLOBAL.ORDER_DETAILS_STATUS.CANCEL:
												showMessage("Món "
														+ newItem.getDish()
																.getName()
														+ " bị hủy!");
												Notify("ClientAmThuc", "Món "
														+ newItem.getDish()
																.getName()
														+ " bị hủy!");
												break;
											}
										}
									}
								}
								ApplicationMain.listOrderDetailsForChef = newList;
							}
						});

						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			private void showMessage(String msg) {
				Toast.makeText(TierActivity.this, msg, Toast.LENGTH_LONG)
						.show();
			}
		});
		silentService();
	}

	public static void silentService() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(9000);
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// Write your code here to update the UI.
								Log.e("SERVICE TIER", "Executed");
								ClientMessage mes = new ClientMessage();
								mes.setMsgID(GLOBAL.TO_SERVER.LOAD_ORDER_DETAILS);
								mClient.sendMessage(mes);
							}
						});
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@SuppressWarnings("deprecation")
	private void Notify(String notificationTitle, String notificationMessage) {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		@SuppressWarnings("deprecation")
		Notification notification = new Notification(
				R.drawable.restaurant_icon, notificationMessage,
				System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, NotificationView.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(this, notificationTitle,
				notificationMessage, pendingIntent);
		notificationManager.notify(9999, notification);
	}
}
