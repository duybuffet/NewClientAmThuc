package com.example.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ptit.amthuc.R;
import com.example.adapter.OrderDetailAdapter;
import com.example.connect.GLOBAL;
import com.example.connect.ClientMessage;
import com.example.connect.TCPClient;
import com.example.main.ApplicationMain;
import com.example.main.MyApplication;
import com.example.model.Bill;
import com.example.model.Dish;
import com.example.model.Order;
import com.example.model.OrderDetails;
import com.example.model.Table;
import com.example.util.DBHelper;
import com.example.util.Helper;
import com.google.gson.Gson;

public class OrderDetailActivity extends Activity implements OnClickListener {
	private ListView lvOrderDetail;
	private OrderDetailAdapter adapter;
	private List<OrderDetails> listOrderDetails;
	private Button btnOpenMenu;
	private Button btnPay;
	private Button btnBack;
	private Context context;
	private Button btnOrder, btnDestroyOrder;
	private int tableId;
	private TextView tvTableName, tvTotal, tvItemsTotal, tvQtt;
	private TCPClient mClient;
	private Table tbl;
	private Order ord;
	private DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.order_layout);
		// OrderDetailActivity.this.getActionBar().hide();
		mClient = ((MyApplication) getApplication()).mClient;

		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey("table_id")) {
			tableId = extras.getInt("table_id");
		}

		initUI();
		initEventListener();
		initData();

	}

	private void initLvOrderDetails() {
		// TODO Auto-generated method stub
		dbHelper = new DBHelper(OrderDetailActivity.this,
				ApplicationMain.listDishes);
		listOrderDetails = new ArrayList<OrderDetails>();
		if (tbl.getStatus() == GLOBAL.ORDER_AND_TABLE_STATUS.NOT_YET_ORDER) {
			listOrderDetails = dbHelper.getOrderDetailsIdByTableId(tbl.getId());
			Log.e("listOrderDetails : ", listOrderDetails.toString());

			if (adapter == null) {
				adapter = new OrderDetailAdapter(OrderDetailActivity.this,
						listOrderDetails);
				lvOrderDetail.setAdapter(adapter);
			} else {
				Log.e("NOTIFY SET DATA CHANGED", "CHANGED");
				adapter = new OrderDetailAdapter(OrderDetailActivity.this,
						listOrderDetails);
				adapter.notifyDataSetChanged();
				lvOrderDetail.setAdapter(adapter);
			}
		}

	}

	private void initEventListener() {
		// TODO Auto-generated method stub
		btnOpenMenu.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnPay.setOnClickListener(this);
		btnOrder.setOnClickListener(this);
		btnDestroyOrder.setOnClickListener(this);

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
					ClientMessage message = gson.fromJson(msg,
							ClientMessage.class);
					Log.e("ORDER DETAILS ACTIVITY", "Nhận dữ liệu từ server");
					switch (message.getMsgID()) {

					case GLOBAL.FROM_SERVER.RELOAD_TABLES:
						ApplicationMain.listTable = message.getArrTables();
						break;

					case GLOBAL.FROM_SERVER.SHOW_ORDERS:
						Log.e("ORDER DETAILS",
								"RECEIVE MSG FROM SERVER BEFORE CHECK");
						if (message.getMsg().equals(GLOBAL.CONFIG.SUCCESS)) {
							Log.e("ORDER DETAILS",
									"RECEIVE MSG FROM SERVER AFTER CHECK MSG SUCCESS");
							if ((ord = message.getOrder()) != null
									&& (ord.getStatus() == GLOBAL.ORDER_AND_TABLE_STATUS.ORDERED
											|| ord.getStatus() == GLOBAL.ORDER_AND_TABLE_STATUS.ORDER_READY_TO_SERVED || ord
											.getStatus() == GLOBAL.ORDER_AND_TABLE_STATUS.ORDER_SERVED)) {
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Log.e("ORDER DETAILS",
												"RECEIVE MSG FROM SERVER _ ORDER CHECKED");
										tvTotal.setText(GLOBAL.CONSTANTS.TOTAL_TITLE
												+ ord.getTotalCost()
												+ GLOBAL.CONSTANTS.PRICE_UNIT);
										tvItemsTotal
												.setText(GLOBAL.CONSTANTS.ITEMS_TOTAL_TITLE
														+ ord.getTotalCost()
														+ GLOBAL.CONSTANTS.PRICE_UNIT);
										tvQtt.setText(GLOBAL.CONSTANTS.ORDER_ITEMS_QTT
												+ ord.getItems().size());
										listOrderDetails = ord.getItems();
										Log.e("ORDER DETAILS LIST ITEMS", ord
												.getItems().toString());
										adapter = new OrderDetailAdapter(
												OrderDetailActivity.this,
												listOrderDetails);
										adapter.notifyDataSetChanged();
										lvOrderDetail.setAdapter(adapter);
									}
								});
								
							}
						} else {
							showMessage("Không tìm thấy order",
									Toast.LENGTH_LONG);
						}
						break;

					case GLOBAL.FROM_SERVER.CANCEL_ORDER:
						if (message.getMsg().equals(GLOBAL.CONFIG.SUCCESS)) {
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									// reset view
									tvTotal.setText(GLOBAL.CONSTANTS.TOTAL_TITLE + 0
											+ GLOBAL.CONSTANTS.PRICE_UNIT);
									tvItemsTotal
											.setText(GLOBAL.CONSTANTS.ITEMS_TOTAL_TITLE
													+ 0 + GLOBAL.CONSTANTS.PRICE_UNIT);
									tvQtt.setText(GLOBAL.CONSTANTS.ORDER_ITEMS_QTT + 0);
									showMessage("Hủy thành công", Toast.LENGTH_LONG);

									// reload table
									ClientMessage mes = new ClientMessage();
									mes.setMsgID(GLOBAL.TO_SERVER.RELOAD_TABLES);
									mes.setArrTables(ApplicationMain.listTable);
									mClient.sendMessage(mes);

									startActivity(new Intent(OrderDetailActivity.this,
											TierActivity.class));
									finish();
								}
							});
						}
						break;

					case GLOBAL.FROM_SERVER.BILL:
						if (message.getMsg().equals(GLOBAL.CONFIG.SUCCESS)) {
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// reset view
									// reload table
									tvTotal.setText(GLOBAL.CONSTANTS.TOTAL_TITLE + 0
											+ GLOBAL.CONSTANTS.PRICE_UNIT);
									tvItemsTotal
											.setText(GLOBAL.CONSTANTS.ITEMS_TOTAL_TITLE
													+ 0 + GLOBAL.CONSTANTS.PRICE_UNIT);
									tvQtt.setText(GLOBAL.CONSTANTS.ORDER_ITEMS_QTT + 0);

									showMessage("Thanh toán thành công",
											Toast.LENGTH_LONG);

									ClientMessage mes = new ClientMessage();
									mes.setMsgID(GLOBAL.TO_SERVER.RELOAD_TABLES);
									mes.setArrTables(ApplicationMain.listTable);
									mClient.sendMessage(mes);
									startActivity(new Intent(OrderDetailActivity.this,
											TierActivity.class));
								}
							});
						}
						break;

					case GLOBAL.FROM_SERVER.ADD_ORDER:
						if (message.getMsg().equalsIgnoreCase("SUCCESS")) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											showMessage(
													"Order đã được chuyển vào bếp thành công!",
													Toast.LENGTH_LONG);
											ClientMessage mes = new ClientMessage();
											mes.setMsgID(GLOBAL.TO_SERVER.RELOAD_TABLES);
											mes.setArrTables(ApplicationMain.listTable);
											mClient.sendMessage(mes);
											mes.setMsgID(GLOBAL.TO_SERVER.SHOW_ORDER);
											mes.setMsg(tbl.getId() + "");
											mClient.sendMessage(mes);
											dbHelper.deleteOrderLDetails(tableId);
										}
									});
								}
							});

						} else {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(getApplicationContext(),
											"Có lỗi xảy ra!",
											Toast.LENGTH_SHORT).show();
									tbl.setStatus(GLOBAL.ORDER_AND_TABLE_STATUS.NOT_YET_ORDER);
									for (int i = 0; i < ApplicationMain.listTable
											.size(); i++) {
										if (ApplicationMain.listTable.get(i)
												.getId() == tableId) {
											ApplicationMain.listTable.set(i,
													tbl);
											break;
										}
									}
								}
							});

						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initUI() {
		tvQtt = (TextView) findViewById(R.id.tvOrderItemQtt);
		tvItemsTotal = (TextView) findViewById(R.id.tvItemsTotalPrice);
		tvTotal = (TextView) findViewById(R.id.tvOrderTotal);
		tvTableName = (TextView) findViewById(R.id.tvOrderDetailsTableName);
		lvOrderDetail = (ListView) findViewById(R.id.lvOrderDetail);
		btnOpenMenu = (Button) findViewById(R.id.btnOpenMenu);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnPay = (Button) findViewById(R.id.btnPay);
		btnOrder = (Button) findViewById(R.id.btn_order);
		btnDestroyOrder = (Button) findViewById(R.id.btn_destroy_order);
	}

	private void initData() {
		for (Table t : ApplicationMain.listTable) {
			if (t.getId() == tableId) {
				tvTableName.setText(t.getName());
				tbl = t;
				initLvOrderDetails();
				if (tbl.getStatus() >= GLOBAL.ORDER_AND_TABLE_STATUS.ORDERED
						&& tbl.getStatus() <= GLOBAL.ORDER_AND_TABLE_STATUS.ORDER_SERVED) {
					Log.e("ORDER DETAILS", "TABLE ORDER STATUS : ORDERED");
					ClientMessage mes = new ClientMessage();
					mes.setMsgID(GLOBAL.TO_SERVER.SHOW_ORDER);
					mes.setMsg(tbl.getId() + "");
					mClient.sendMessage(mes);
				} else if (tbl.getStatus() == GLOBAL.ORDER_AND_TABLE_STATUS.NOT_YET_ORDER) {
					double total = 0;
					for (OrderDetails dtail : listOrderDetails) {
						total += dtail.getDish().getPrice();
					}
					tvTotal.setText(GLOBAL.CONSTANTS.TOTAL_TITLE + Helper.formatNumber(total)
							+ GLOBAL.CONSTANTS.PRICE_UNIT);

					tvItemsTotal.setText(GLOBAL.CONSTANTS.ITEMS_TOTAL_TITLE
							+ Helper.formatNumber(total) + GLOBAL.CONSTANTS.PRICE_UNIT);

					tvQtt.setText(GLOBAL.CONSTANTS.ORDER_ITEMS_QTT
							+ listOrderDetails.size());
				} else if (tbl.getStatus() == GLOBAL.ORDER_AND_TABLE_STATUS.TABLE_FREE) {
					tvTotal.setText(GLOBAL.CONSTANTS.TOTAL_TITLE + 0
							+ GLOBAL.CONSTANTS.PRICE_UNIT);
				}
				break;
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnOpenMenu:
			openMenu();
			break;

		case R.id.btnPay:
			pay();
			break;
		case R.id.btnBack:
			back();
			break;
		case R.id.btn_order:
			switch (tbl.getStatus()) {
			case GLOBAL.ORDER_AND_TABLE_STATUS.NOT_YET_ORDER:
				order();
				break;

			case GLOBAL.ORDER_AND_TABLE_STATUS.TABLE_FREE:
				Toast.makeText(OrderDetailActivity.this, "Chưa chọn món ăn",
						Toast.LENGTH_SHORT).show();
				break;

			case GLOBAL.ORDER_AND_TABLE_STATUS.ORDERED:
				Toast.makeText(OrderDetailActivity.this, "Cập nhật order",
						Toast.LENGTH_SHORT).show();
				break;
			}
			break;
		case R.id.btn_destroy_order:
			destroyOrder();
			break;

		default:
			break;
		}

	}

	private void order() {
		if (tbl.getStatus() == GLOBAL.ORDER_AND_TABLE_STATUS.NOT_YET_ORDER) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Xác nhận order")
					.setMessage("Gửi order cho bếp ?")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// do something
									Order order = new Order();
									order.setStatus(GLOBAL.ORDER_AND_TABLE_STATUS.ORDERED);
									order.setWaiter(MyApplication.user);
									SimpleDateFormat sdf = new SimpleDateFormat(
											GLOBAL.CONFIG.TIME_FORMAT);
									order.setOrderTime(sdf.format(new Date()));

									for (int i = 0; i < ApplicationMain.listTable
											.size(); i++) {
										if (ApplicationMain.listTable.get(i)
												.getId() == tableId) {
											tbl.setStatus(GLOBAL.ORDER_AND_TABLE_STATUS.ORDERED);
											ApplicationMain.listTable.set(i,
													tbl);
											break;
										}
									}
									order.setOrderTable(tbl);
									order.setItems(listOrderDetails);
									double total = 0;
									for (OrderDetails dtail : listOrderDetails) {
										total += dtail.getDish().getPrice();
									}
									order.setTotalCost(total);

									ClientMessage mes = new ClientMessage();
									mes.setOrder(order);
									mes.setMsgID(GLOBAL.TO_SERVER.ADD_ORDER);
									mClient.sendMessage(mes);
									Log.e("ORDER OBJECT",
											new Gson().toJson(mes));
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
	}

	private void destroyOrder() {
		if (tbl != null
				&& tbl.getStatus() == GLOBAL.ORDER_AND_TABLE_STATUS.ORDERED
				&& ord != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Hủy order")
					.setMessage("Bạn có chắc muốn hủy order này ?")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// do something
									ClientMessage mes = new ClientMessage();
									mes.setOrder(ord);
									mes.setMsgID(GLOBAL.TO_SERVER.CANCEL_ORDER);
									mClient.sendMessage(mes);
									// reload static var listTable
									for (int i = 0; i < ApplicationMain.listTable
											.size(); i++) {
										if (ApplicationMain.listTable.get(i)
												.getId() == tableId) {
											tbl.setStatus(GLOBAL.ORDER_AND_TABLE_STATUS.TABLE_FREE);
											ApplicationMain.listTable.set(i,
													tbl);
											break;
										}
									}

									//
									tvTotal.setText(GLOBAL.CONSTANTS.TOTAL_TITLE
											+ 0 + GLOBAL.CONSTANTS.PRICE_UNIT);
									tvItemsTotal
											.setText(GLOBAL.CONSTANTS.ITEMS_TOTAL_TITLE
													+ 0
													+ GLOBAL.CONSTANTS.PRICE_UNIT);
									tvQtt.setText(GLOBAL.CONSTANTS.ORDER_ITEMS_QTT + 0);
									listOrderDetails = new ArrayList<OrderDetails>();
									adapter = new OrderDetailAdapter(
											OrderDetailActivity.this,
											listOrderDetails);
									adapter.notifyDataSetChanged();
									lvOrderDetail.setAdapter(adapter);

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
	}

	private void back() {
		this.finish();

	}

	private void pay() {
		if (tbl != null
				&& ord != null
				&& tbl.getStatus() >= GLOBAL.ORDER_AND_TABLE_STATUS.ORDERED
				&& tbl.getStatus() <= GLOBAL.ORDER_AND_TABLE_STATUS.ORDER_SERVED) {
			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_dialog);

			// set the custom dialog components - text, image and button
			TextView tvAmount = (TextView) dialog
					.findViewById(R.id.tvPayDialogTotalToPay);
			final TextView tvChange = (TextView) dialog
					.findViewById(R.id.tvPayDialogChange);
			final EditText edCustomerMoney = (EditText) dialog
					.findViewById(R.id.edPayDialogCustomerMoney);
			tvAmount.setText(Helper.formatNumber(ord.getTotalCost()) + GLOBAL.CONSTANTS.PRICE_UNIT);

			Button btnPay = (Button) dialog.findViewById(R.id.btnPayDialogPay);
			Button btnCancel = (Button) dialog
					.findViewById(R.id.btnPayDialogCancel);
			// if button is clicked, close the custom dialog
			edCustomerMoney.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
					float cusMoney = 0;
					try {
						cusMoney = Float.parseFloat(s.toString());
					} catch (Exception e) {
						cusMoney = 0;
					}
					if (cusMoney < ord.getTotalCost()) {
						showMessage("Chưa đủ tiền", Toast.LENGTH_LONG);
						tvChange.setText("Chưa đủ tiền");
					} else {
						tvChange.setText(Helper.formatNumber((cusMoney - ord.getTotalCost()))
								+ GLOBAL.CONSTANTS.PRICE_UNIT);
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});

			btnCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			btnPay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (edCustomerMoney.getText().toString().equals("")) {
						showMessage("Cần nhập tiền khách đưa",
								Toast.LENGTH_LONG);
					} else {
						Bill bill = new Bill();
						SimpleDateFormat sdf = new SimpleDateFormat(
								GLOBAL.CONFIG.TIME_FORMAT);
						bill.setTime(sdf.format(new Date()));
						bill.setOrder(ord);
						float cusMoney = Float.parseFloat(edCustomerMoney
								.getText().toString());
						bill.setCustomerMoney(Float.parseFloat(edCustomerMoney
								.getText().toString()));
						bill.setCustomerChange(cusMoney
								- Float.parseFloat(ord.getTotalCost()
										.toString()));

						// reload static var listTable
						for (int i = 0; i < ApplicationMain.listTable.size(); i++) {
							if (ApplicationMain.listTable.get(i).getId() == tableId) {
								tbl.setStatus(GLOBAL.ORDER_AND_TABLE_STATUS.TABLE_FREE);
								ApplicationMain.listTable.set(i, tbl);
								break;
							}
						}

						ClientMessage mes = new ClientMessage();
						mes.setMsgID(GLOBAL.TO_SERVER.BILL);
						mes.setBill(bill);
						mClient.sendMessage(mes);

					}
				}
			});

			dialog.show();
		} else {
			showMessage("Bạn cần thực hiện order trước", Toast.LENGTH_LONG);
		}
	}

	private void openMenu() {
		// if table free now, set status NOT_YET_ORDER, and send to server to
		// update in all client
		if (tbl != null) {
			if (tbl.getStatus() == GLOBAL.ORDER_AND_TABLE_STATUS.TABLE_FREE) {
				tbl.setWaiter(MyApplication.user);
				tbl.setStatus(GLOBAL.ORDER_AND_TABLE_STATUS.NOT_YET_ORDER);
				for (int i = 0; i < ApplicationMain.listTable.size(); i++) {
					if (ApplicationMain.listTable.get(i).getId() == tableId) {
						ApplicationMain.listTable.set(i, tbl);
						break;
					}
				}

				ClientMessage mes = new ClientMessage();
				mes.setMsgID(GLOBAL.TO_SERVER.RELOAD_TABLES);
				mes.setArrTables(ApplicationMain.listTable);
				mClient.sendMessage(mes);
			}

			// send table position to OrderActivity
			Bundle bundle = new Bundle();
			bundle.putInt("table_id", tableId);
			Intent i = new Intent(OrderDetailActivity.this, OrderActivity.class);
			i.putExtras(bundle);
			startActivity(i);
		}
	}

	private void reloadListTable() {
		// TODO Auto-generated method stub
		for (int i = 0; i < ApplicationMain.listTable.size(); i++) {
			if (ApplicationMain.listTable.get(i).getId() == tableId) {
				ApplicationMain.listTable.set(i, tbl);
				break;
			}
		}
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

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		initData();
		// initLvOrderDetails();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
		// initLvOrderDetails();
	}

	private void showMessage(String string, int length) {
		// TODO Auto-generated method stub
		Toast.makeText(this, string, length).show();
	}

}
