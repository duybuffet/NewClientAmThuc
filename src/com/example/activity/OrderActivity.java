package com.example.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AsyncPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ptit.amthuc.R;
import com.example.adapter.CategoryAdapter;
import com.example.adapter.DishAdapter;
import com.example.connect.GLOBAL;
import com.example.connect.ClientMessage;
import com.example.connect.TCPClient;
import com.example.model.Dish;
import com.example.model.Order;
import com.example.model.OrderDetails;
import com.example.model.Table;
import com.example.main.ApplicationMain;
import com.example.main.MainActivity;
import com.example.main.MyApplication;
import com.example.util.DBHelper;
import com.google.gson.Gson;

public class OrderActivity extends Activity {
	private ListView lvCategory;
	private TextView tvTableName;
	private GridView grvDish;
	private Button btnCloseMenu;
	private CategoryAdapter adapter;
	private DishAdapter dishAdapter;
	private List<Dish> listDish;	
	private Button btnDestroyTable, btnChangeTable;
	private TCPClient mClient;
	private int tableId;
	private DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_food_layout);
		
		// dbHelp init
		dbHelper = new DBHelper(OrderActivity.this, ApplicationMain.listDishes);
		// get table id
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey("table_id")) {
			tableId = extras.getInt("table_id");
			Log.e("TABLE ID", tableId + "");
		}

		initUI();
		initData();
		mClient = ((MyApplication) getApplication()).mClient;		
		if (ApplicationMain.listCategory != null
				&& ApplicationMain.listCategory.size() > 0) {
			listDish = ApplicationMain.listCategory.get(0).getListDishes();

			dishAdapter = new DishAdapter(OrderActivity.this, listDish);
			grvDish.setAdapter(dishAdapter);
			grvDish.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Order order = new Order();
					Dish dish = new Dish();
					dish.setId(listDish.get(position).getId());
					if (dbHelper.insertOrderDetails(tableId, dish.getId(), 1) != -1) {
						final Toast toast = Toast.makeText(OrderActivity.this, "Thêm món thành công", Toast.LENGTH_SHORT);
						toast.show();
						Handler handler = new Handler();
			            handler.postDelayed(new Runnable() {
			               @Override
			               public void run() {
			                   toast.cancel(); 
			               }
			        }, 500);
					}
				}
			});
		}
		
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
					Log.i("ORDER ACTIVITY", "Nhận dữ liệu từ server");
					switch (message.getMsgID()) {

					case GLOBAL.FROM_SERVER.RELOAD_TABLES:
						Log.e("Order Activity", "Reload table");
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}

	private void initUI() {
		tvTableName = (TextView) findViewById(R.id.tvOrderTableName);
		lvCategory = (ListView) findViewById(R.id.lvCategory);
		grvDish = (GridView) findViewById(R.id.grvDish);		
		btnCloseMenu = (Button) findViewById(R.id.btnCloseMenu);
		btnChangeTable = (Button) findViewById(R.id.btn_change_table);
		btnDestroyTable = (Button) findViewById(R.id.btn_destroy_table);
		btnCloseMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderActivity.this.finish();

			}
		});
		btnDestroyTable.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						OrderActivity.this);
				builder.setTitle("?????????????????????")
						.setMessage("??????????????????????")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// do something
										Toast.makeText(getApplicationContext(),
												"You clicked on YES",
												Toast.LENGTH_SHORT).show();
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
				builder.show();

			}
		});

		btnChangeTable.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						OrderActivity.this);
				builder.setTitle("?????????????????????")
						.setMessage("??????????????????????")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// do something
										Toast.makeText(getApplicationContext(),
												"You clicked on YES",
												Toast.LENGTH_SHORT).show();
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
				builder.show();

			}
		});
	}

	private void initData() {	
		for (Table t : ApplicationMain.listTable) {
			if (t.getId() == tableId) {
				tvTableName.setText(t.getName());				
				break;
			}
		}
		listDish = new ArrayList<Dish>();
		adapter = new CategoryAdapter(this, ApplicationMain.listCategory);
		lvCategory.setAdapter(adapter);
		lvCategory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (listDish != null) {
					listDish = null;
				}
				listDish = ApplicationMain.listCategory.get(position)
						.getListDishes();
				dishAdapter = new DishAdapter(OrderActivity.this, listDish);
				grvDish.setAdapter(dishAdapter);
				dishAdapter.notifyDataSetChanged();

			}
		});

	}

	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (Integer.valueOf(android.os.Build.VERSION.SDK) < 7 // Instead use
																// android.os.Build.VERSION.SDK_INT
																// <
																// android.os.Build.VERSION_CODES.ECLAIR
				&& keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			onBackPressed();
		}

		return super.onKeyDown(keyCode, event);
	}

}
