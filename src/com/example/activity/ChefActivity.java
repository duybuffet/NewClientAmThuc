package com.example.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ptit.amthuc.R;
import com.example.adapter.CustomChefArrayAdapter;
import com.example.connect.GLOBAL;
import com.example.connect.ClientMessage;
import com.example.connect.TCPClient;
import com.example.connect.TCPClient.onMessageReceived;
import com.example.main.ApplicationMain;
import com.example.main.MyApplication;
import com.example.model.OrderDetails;
import com.google.gson.Gson;

public class ChefActivity extends Activity implements OnItemClickListener {
	private ListView lvOrderDetails;
	private TextView tvDate;
	private Button btnReturn;
	private CustomChefArrayAdapter adapter;
	private TCPClient mClient;
	private List<OrderDetails> data;
	final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chef);

		initUI();
		initData();
		addEventsListener();
	}

	private void silentService() {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(3000);
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// Write your code here to update the UI.
								Log.e("SILENT SERVICE", "Executed");
								ClientMessage mes = new ClientMessage();
								mes.setMsgID(GLOBAL.TO_SERVER.LOAD_ORDER_DETAILS);
								mClient.sendMessage(mes);
							}
						});
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start();

	}

	private void addEventsListener() {
		// TODO Auto-generated method stub
		btnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChefActivity.this.finish();
			}
		});
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
					Log.i("Chef Activity", "Nhận dữ liệu từ server");
					switch (message.getMsgID()) {
					case GLOBAL.FROM_SERVER.LOAD_ORDER_DETAILS:
						Log.e("IN HERE", "HERE HERE ");
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								ApplicationMain.listOrderDetailsForChef = message
										.getArrOrderDetails();
								Log.e("Reload order Details",
										ApplicationMain.listOrderDetailsForChef
												.toString());
								initOrderDetailsData();
								lvOrderDetails.invalidate();
								((BaseAdapter) lvOrderDetails.getAdapter())
										.notifyDataSetChanged();
								// adapter.notifyDataSetChanged();
							}
						});

						break;

					case GLOBAL.FROM_SERVER.UPDATE_ORDER_DETAILS_STATUS:
						if (message.getMsg().equals(GLOBAL.CONFIG.SUCCESS)) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(ChefActivity.this,
											"Cập nhật thành công!",
											Toast.LENGTH_SHORT).show();
									ClientMessage mes = new ClientMessage();
									mes.setMsgID(GLOBAL.TO_SERVER.RELOAD_ORDER_DETAILS);
									mClient.sendMessage(mes);
								}
							});
						}
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		silentService();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		lvOrderDetails = (ListView) findViewById(R.id.lvChefListOrderDetails);
		registerForContextMenu(lvOrderDetails);
		tvDate = (TextView) findViewById(R.id.tvChefDate);
		SimpleDateFormat sdf = new SimpleDateFormat(
				GLOBAL.CONFIG.DATE_DISPLAY_FORMAT);
		tvDate.setText(sdf.format(new Date()));
		btnReturn = (Button) findViewById(R.id.btnChefReturn);
	}

	private void initData() {
		// TODO Auto-generated method stub
		data = new ArrayList<OrderDetails>();

		initOrderDetailsData();
		// adapter = new ChefAdapter(this, android.R.layout.simple_list_item_1,
		// data);
		adapter = new CustomChefArrayAdapter(this,
				android.R.layout.simple_list_item_1, data);
		lvOrderDetails.setAdapter(adapter);
		lvOrderDetails.setOnItemClickListener(this);
	}

	private void initOrderDetailsData() {
		// TODO Auto-generated method stub
		data.clear();
		for (OrderDetails od : ApplicationMain.listOrderDetailsForChef) {
			if (od.getStatus() == GLOBAL.ORDER_DETAILS_STATUS.WAITING) {
				data.add(od);
			}
		}
//		data.addAll(ApplicationMain.listOrderDetailsForChef);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.chef_update_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		OrderDetails ordDetail = (OrderDetails) lvOrderDetails
				.getItemAtPosition(info.position);
		ClientMessage mes = new ClientMessage();

		switch (item.getItemId()) {
		case R.id.itemCancel:
			if (ordDetail.getStatus() == GLOBAL.ORDER_DETAILS_STATUS.WAITING)
				ordDetail.setStatus(GLOBAL.ORDER_DETAILS_STATUS.CANCEL);
			break;

		case R.id.itemFinish:
			if (ordDetail.getStatus() == GLOBAL.ORDER_DETAILS_STATUS.WAITING)
				ordDetail.setStatus(GLOBAL.ORDER_DETAILS_STATUS.FINISH);
			break;

		default:
			break;
		}

		mes.setMsgID(GLOBAL.TO_SERVER.UPDATE_ORDER_DETAILS_STATUS);
		mes.setOrderDetails(ordDetail);
		mClient.sendMessage(mes);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		this.openContextMenu(arg1);
	}

}
