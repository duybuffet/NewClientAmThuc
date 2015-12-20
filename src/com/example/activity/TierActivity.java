package com.example.activity;

import java.util.ArrayList;

import com.ptit.amthuc.R;
import com.example.adapter.CategoryAdapter;
import com.example.adapter.TableAdapter;
import com.example.connect.GLOBAL;
import com.example.main.ApplicationMain;
import com.example.model.Category;
import com.example.model.Table;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class TierActivity extends Activity {
	TableAdapter adapter;
	GridView grid;
	ArrayList<Table> data, listTable;
	int vitri = 1;
	int tier = 1;
	TextView textTier;

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
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
//				vitri = arg2;
				Intent intent = new Intent(TierActivity.this, OrderDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("table_id", data.get(position).getId());
				intent.putExtras(bundle);
				startActivity(intent);
				
//				Table table = data.get(vitri);
				// trang thai cua ban
//				int status = table.getStatus();
//
//				switch (status) {
//				case 1:
//					Log.i("message", "ban dang co trang thai 1 o vi tri "
//							+ vitri);
//
//					break;
//
//				case 2:
//
//					break;
//				case 3:
//
//					break;
//				case 4:
//
//					break;
//				}
			}

		});

	}
	
	

//	@Override
//	protected void onRestart() {
//		// TODO Auto-generated method stub
//		super.onRestart();
////		data.clear();
////		listTable.clear();
////		listTable.addAll(ApplicationMain.listTable);
////		vitri = 1;
////		tier = 1;
////		init(1);
////		adapter.notifyDataSetChanged();
//	}



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
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.tier, menu);
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
	
	

}
