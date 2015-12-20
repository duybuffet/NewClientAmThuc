package com.example.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ptit.amthuc.R;
import com.example.connect.GLOBAL;
import com.example.connect.ClientMessage;
import com.example.connect.TCPClient;
import com.example.model.User;
import com.google.gson.Gson;

public class MainActivity extends Activity implements OnClickListener {
	private Toast toast;
	private static final String TAG = "SERVER MESSAGE";
	private TCPClient mTcpClient;
	private EditText editUsername, editPass;
	private Button btnTest;
	private Handler handler;
	private ProgressDialog progress;
	private TextView textError;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "KHỞI TẠO ACTIVITY MAIN");
		setContentView(R.layout.activity_main_login);
		// intent = new Intent(this, BroadcastService.class);
		editUsername = (EditText) findViewById(R.id.editName);
		editPass = (EditText) findViewById(R.id.editPass);
		btnTest = (Button) findViewById(R.id.btnLogin);
		btnTest.setOnClickListener(this);
		textError = (TextView) findViewById(R.id.textError);
		progress = new ProgressDialog(this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		handler = new Handler();
		mTcpClient = ((MyApplication) getApplication()).mClient;
		mTcpClient.setOnMessageListener(new TCPClient.onMessageReceived() {
			@Override
			public void MessageReceived(String msg) {

				// TODO Auto-generated method stub
				processMessage(msg);
			}
		});
		((MyApplication) getApplication()).Connect();
		toast = Toast.makeText(this, "", 2000);
	}

	public void processMessage(String msg) {
		try {
			Gson gson = new Gson();
			ClientMessage message = gson.fromJson(msg, ClientMessage.class);
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
				break;
			case GLOBAL.FROM_SERVER.LOGIN:
				checkLogin(message);
				break;

			case GLOBAL.FROM_SERVER.UPDATE_USERS_LIST:
				((MyApplication) getApplication()).setUsersList(message
						.getArrUsers());
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Login() {
		User user = new User();
		String username = editUsername.getText().toString();
		String password = editPass.getText().toString();
		if (username.equals("") || password.equals("")) {
			toast.setText("Bạn chưa nhập đủ thông tin");
			toast.show();
		} else {
			user.setUsername(username);
			user.setPassword(password);
			ClientMessage message = new ClientMessage(GLOBAL.TO_SERVER.LOGIN,
					"Login", user);
			Log.i(TAG, "Đăng nhập");
			Log.i(TAG, (new Gson()).toJson(message));
			mTcpClient.sendMessage(message);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if (id == R.id.btnLogin) {
			Login();
		}
	}

	public void checkLogin(ClientMessage msg) {
		// openWelcomeActivity();

		Log.i(TAG, "CHECK LOGIN");
		if (msg.getUser() != null) {
			if (msg.getUser().getUserLevel() != GLOBAL.USER_LEVEL.ADMIN) {
			Log.i(TAG, "LOGIN SUCCESSFULLY");
			((MyApplication) getApplication()).user = msg.getUser();
			openWelcomeActivity();
			} else {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						textError.setText("Cần là tài khoản bếp hoặc bồi bàn");
					}
				});
			}
		} else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					textError.setText("Thông tin đăng nhập không chính xác");
				}
			});
		}
	}

	public void openWelcomeActivity() {
		Intent i = null;
		if (MyApplication.user.getUserLevel() == GLOBAL.USER_LEVEL.WAITER
				|| MyApplication.user.getUserLevel() == GLOBAL.USER_LEVEL.CHEF) {
			i = new Intent(this, ApplicationMain.class);
		}
		startActivity(i);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "Cài đặt lại Listener" + resultCode);
		mTcpClient.setOnMessageListener(new TCPClient.onMessageReceived() {
			@Override
			public void MessageReceived(String msg) {
				// TODO Auto-generated method stub
				processMessage(msg);
			}
		});
		if (resultCode == 1) {
			String username = data.getStringExtra("username");
			String password = data.getStringExtra("password");
			editUsername.setText(username);
			editPass.setText(password);
			Login();
		}
	}
}