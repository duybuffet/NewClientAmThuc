package com.example.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptit.amthuc.R;
import com.example.connect.GLOBAL;
import com.example.model.Table;

public class TableAdapter extends ArrayAdapter<Table> {
	Activity context = null;
	int layoutId;
	ArrayList<Table> myArray = null;
	public int vitri = 1;

	public TableAdapter(Activity context, int layoutId, ArrayList<Table> arr) {
		super(context, layoutId, arr);
		this.context = context;
		this.layoutId = layoutId;
		this.myArray = arr;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Table table = myArray.get(position);
		// LayoutInflater inflater = context.getLayoutInflater();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(layoutId, null);

		// ImageButton table = (ImageButton) convertView
		// .findViewById(R.id.tb);
		ImageView ivTable = (ImageView) convertView.findViewById(R.id.tb);
		TextView txtName = (TextView) convertView.findViewById(R.id.txtname);
		// cai dat anh cho ban
		switch (table.getType()) {
		case GLOBAL.TABLE_TYPE.TABLE_2:
			switch (table.getStatus()) {
			case GLOBAL.ORDER_AND_TABLE_STATUS.TABLE_FREE:
				ivTable.setImageResource(R.drawable.t10);
				break;
			
			case GLOBAL.ORDER_AND_TABLE_STATUS.NOT_YET_ORDER:
				ivTable.setImageResource(R.drawable.t11);
				break;
				
			case GLOBAL.ORDER_AND_TABLE_STATUS.ORDERED:
				ivTable.setImageResource(R.drawable.t12);
			}
			
			break;
			
		case GLOBAL.TABLE_TYPE.TABLE_4:
			switch (table.getStatus()) {
			case GLOBAL.ORDER_AND_TABLE_STATUS.TABLE_FREE:
				ivTable.setImageResource(R.drawable.t20);
				break;
			
			case GLOBAL.ORDER_AND_TABLE_STATUS.NOT_YET_ORDER:
				ivTable.setImageResource(R.drawable.t21);
				break;
				
			case GLOBAL.ORDER_AND_TABLE_STATUS.ORDERED:
				ivTable.setImageResource(R.drawable.t22);
			}
			break;
			
		case GLOBAL.TABLE_TYPE.TABLE_8:
			switch (table.getStatus()) {
			case GLOBAL.ORDER_AND_TABLE_STATUS.TABLE_FREE:
				ivTable.setImageResource(R.drawable.t30);
				break;
			
			case GLOBAL.ORDER_AND_TABLE_STATUS.NOT_YET_ORDER:
				ivTable.setImageResource(R.drawable.t31);
				break;
				
			case GLOBAL.ORDER_AND_TABLE_STATUS.ORDERED:
				ivTable.setImageResource(R.drawable.t32);
			}
			break;

		default:
			break;
		}
		// cai dat ten ban
		txtName.setText(table.getName());

		return convertView;
	}
}
