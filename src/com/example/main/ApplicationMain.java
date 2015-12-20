package com.example.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;

import com.ptit.amthuc.R;
import com.example.activity.ChefActivity;
import com.example.activity.TierActivity;
import com.example.adapter.UserAdapter;
import com.example.connect.GLOBAL;
import com.example.connect.ClientMessage;
import com.example.connect.TCPClient;
import com.example.model.Category;
import com.example.model.Dish;
import com.example.model.OrderDetails;
import com.example.model.Table;
import com.example.model.User;
import com.example.service.ServiceManager;
import com.google.gson.Gson;

public class ApplicationMain extends Activity implements OnClickListener {
	private TabHost tab;
	private GestureDetector gDetect;
	private ListView lvFriend, lvUsers;
	private Button btnProfile, btnOrder, btnLogout, btnHistory;
	private SearchView svFriend, svPlayers;
	private UserAdapter userAdapter;
	private TCPClient mClient;
	private String TAG = "Application Main Activity";
	private Handler handler;
	private ProgressDialog progress;
	private ServiceManager mServiceManager;
	public static int ACTIVITY_RESULT = 11;
	public static List<Category> listCategory;
	public static List<Table> listTable;
	public static List<Dish> listDishes;
	public static List<OrderDetails> listOrderDetailsForChef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_application_main);

		loadTabs();
		getFormWidgets();
		progress = new ProgressDialog(this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		handler = new Handler();
		gDetect = new GestureDetector(this, new GestureListener());
	}

	public void getFormWidgets() {
		mClient = ((MyApplication) getApplication()).mClient;
		mClient.setOnMessageListener(new TCPClient.onMessageReceived() {

			@Override
			public void MessageReceived(String msg) {
				// TODO Auto-generated method stub
				ProcessMessage(msg);

			}
		});
		lvUsers = (ListView) findViewById(R.id.listViewPlayers);
		btnProfile = (Button) findViewById(R.id.btnProfile);
		btnOrder = (Button) findViewById(R.id.btnOrder);
		if (MyApplication.user.getUserLevel() == GLOBAL.USER_LEVEL.CHEF) {
			btnOrder.setText("Xử lý order");
		}
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnHistory = (Button) findViewById(R.id.btnHistory);
		svPlayers = (SearchView) findViewById(R.id.searchViewPlayers);
		btnProfile.setOnClickListener(this);
		btnOrder.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		btnHistory.setOnClickListener(this);
		/*
		 * svFriend.setOnClickListener(this);
		 * svPlayers.setOnClickListener(this);
		 */
		userAdapter = new UserAdapter(this, R.layout.adapter_player_array,
				((MyApplication) getApplication()).arrUsers);
		lvUsers.setAdapter(userAdapter);
		registerForContextMenu(lvUsers);

		listCategory = new ArrayList<Category>();
		listTable = new ArrayList<Table>();

		ClientMessage message = new ClientMessage();

		message.setMsgID(GLOBAL.TO_SERVER.LOAD_TABLES);
		mClient.sendMessage(message);

		message.setMsgID(GLOBAL.TO_SERVER.LIST_CATEGORY);
		mClient.sendMessage(message);
		
		message.setMsgID(GLOBAL.TO_SERVER.LOAD_ORDER_DETAILS);
		mClient.sendMessage(message);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		User user = (User) lvUsers.getItemAtPosition(info.position);
		switch (item.getItemId()) {
		case R.id.itemProfile:
			Log.i(TAG, "Xem hồ sơ");
			break;
		case R.id.itemChat:
			Log.i(TAG, "Chat với người dùng");
			Intent i = new Intent(this, Chat.class);
			Bundle bundle = new Bundle();
			ClientMessage message = new ClientMessage();
			message.setTarget(user);
			message.setMsgID(GLOBAL.TO_SERVER.CHAT);
			message.setMsg("");
			bundle.putSerializable("message", message);
			i.putExtra("data", bundle);
			startActivityForResult(i, this.ACTIVITY_RESULT);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.gDetect.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo info) {
		super.onCreateContextMenu(menu, view, info);
		getMenuInflater().inflate(R.menu.player_context_menu, menu);
	}

	public class GestureListener extends
			GestureDetector.SimpleOnGestureListener {

		private float flingMin = 50;
		private float velocityMin = 100;

		@Override
		public boolean onDown(MotionEvent event) {
			return true;
		}

		public boolean onFling(MotionEvent event1, MotionEvent event2,
				float velocityX, float velocityY) {
			float horizontalDiff = event2.getX() - event1.getX();
			float verticalDiff = event2.getY() - event1.getY();
			float absVelocityX = Math.abs(velocityX);
			if (horizontalDiff > flingMin && absVelocityX > velocityMin) {
				int currentTab = tab.getCurrentTab() + 1;
				if (currentTab > 2)
					currentTab = 0;
				tab.setCurrentTab(currentTab);
			} else if (horizontalDiff < -1 * flingMin
					&& absVelocityX > velocityMin) {
				int currentTab = tab.getCurrentTab() - 1;
				if (currentTab < 0)
					currentTab = 2;
				tab.setCurrentTab(currentTab);
			}
			return false;
			// determine what happens on fling events
		}
	}

	public void loadTabs() {
		// Lấy Tabhost id ra trước (cái này của built - in android
		tab = (TabHost) findViewById(android.R.id.tabhost);
		// gọi lệnh setup
		tab.setup();
		TabHost.TabSpec spec;
		// Tạo tab1
		spec = tab.newTabSpec("t1");
		spec.setContent(R.id.person);
		spec.setIndicator("Cá nhân");
		tab.addTab(spec);

		// Tạo tab3
		spec = tab.newTabSpec("t2");
		spec.setContent(R.id.players);
		spec.setIndicator("Danh sách người dùng online");
		tab.addTab(spec);
		// Thiết lập tab mặc định được chọn ban đầu là tab 0
		tab.setCurrentTab(0);
		// Ở đây Tôi để sự kiện này để các bạn tùy xử lý
		// Ví dụ tab1 chưa nhập thông tin xong mà lại qua tab 2 thì báo...
		tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String arg0) {

			}
		});
	}

	public void ProcessMessage(String msg) {
		try {

			Gson gson = new Gson();
			ClientMessage message = gson.fromJson(msg, ClientMessage.class);
			Log.i(TAG, "Nhận dữ liệu từ server");
			switch (message.getMsgID()) {

			case GLOBAL.FROM_SERVER.TIMEOUT:

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progress.setMessage("Không thể kết nối đến máy chủ");
						progress.setTitle("Lỗi kết nối");
						progress.show();
					}
				});
				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						Log.i(TAG, "Thử kết nối lại đến máy chủ");
						// TODO Auto-generated method stub
						((MyApplication) getApplication()).Connect();
					}
				};
				handler.postDelayed(runnable, 2000);

				break;
			case GLOBAL.FROM_SERVER.CONNECTED:

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progress.dismiss();
					}
				});
				Intent i = new Intent(this, MainActivity.class);
				((MyApplication) getApplication()).resetData();
				startActivity(i);
				finish();
				break;
			case GLOBAL.FROM_SERVER.UPDATE_USERS_LIST:
				updatePlayersList(message);
				break;
			case GLOBAL.FROM_SERVER.RECEIVED_CHAT_MESSAGE:
				receivedChatMessage(message);

			case GLOBAL.FROM_SERVER.LIST_CATEGORY:
				listCategory = message.getArrCategories();
				listDishes = new ArrayList();
				for (Category cat : listCategory) {
					listDishes.addAll(cat.getListDishes());
				}
				Log.e("LIST DISH", listDishes.toString());
				break;

			case GLOBAL.FROM_SERVER.LOAD_TABLES:
				listTable = message.getArrTables();
				break;
				
			case GLOBAL.FROM_SERVER.LOAD_ORDER_DETAILS:
				listOrderDetailsForChef = message.getArrOrderDetails();
				Log.e("LIST ORDER DETAILS", listOrderDetailsForChef.toString());
				break;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receivedChatMessage(ClientMessage message) {
		Intent i = new Intent(this, Chat.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("message", message);
		i.putExtra("data", bundle);
		startActivityForResult(i, 0);
	}

	public void updatePlayersList(ClientMessage msg) {
		Log.i(TAG, "Cập nhật danh sách người chơi");
		((MyApplication) getApplication()).setUsersList(msg.getArrUsers());
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				userAdapter.clear();
				userAdapter.addAll(((MyApplication) getApplication()).arrUsers);
				userAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.btnLogout:
			Logout();
			break;
		case R.id.btnOrder:
			if (MyApplication.user.getUserLevel() == GLOBAL.USER_LEVEL.WAITER) {
				order();
			} else {
				handleOrder();
			}
			break;
		}
	}

	private void handleOrder() {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, ChefActivity.class);
		startActivity(i);
	}

	private void order() {
		Intent i = new Intent(this, TierActivity.class);
		startActivity(i);

	}

	public void Logout() {
		Intent i = new Intent(this, MainActivity.class);
		((MyApplication) getApplication()).resetData();
		ClientMessage message = new ClientMessage();
		message.setMsgID(GLOBAL.TO_SERVER.LOGOUT);
		mClient.sendMessage(message);
		startActivity(i);
		finish();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "Thoát khỏi cửa sổ chat trở lại Application Main");
		mClient.setOnMessageListener(new TCPClient.onMessageReceived() {

			@Override
			public void MessageReceived(String msg) {
				// TODO Auto-generated method stub
				ProcessMessage(msg);

			}
		});
	}
}
