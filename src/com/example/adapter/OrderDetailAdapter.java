package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ptit.amthuc.R;
import com.example.activity.OrderDetailActivity;
import com.example.connect.GLOBAL;
import com.example.model.Dish;
import com.example.model.OrderDetails;
import com.example.util.Helper;

public class OrderDetailAdapter extends BaseAdapter {
	private List<OrderDetails> arrItems;
	private LayoutInflater mInflater;
	private Context context;
	private Spinner spnQuantity;
	private ArrayList<String> arrData;
	private ArrayAdapter<String> spnAdapter;

	public OrderDetailAdapter(Context activity, List<OrderDetails> arrItems) {
		this.arrItems = arrItems;
		this.context = activity;
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final HolderView holder;
		if (convertView == null) {
			holder = new HolderView();
			convertView = mInflater.inflate(R.layout.item_order_detail_layout,
					null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
			holder.tvUnit = (TextView) convertView.findViewById(R.id.tvUnit);
			holder.tvType = (TextView) convertView.findViewById(R.id.tvType);
			holder.spnQuantity = (Spinner) convertView
					.findViewById(R.id.spnQuantity);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		final OrderDetails item = arrItems.get(position);

		holder.tvName.setText(item.getDish().getName());
		holder.tvPrice.setText(Helper.formatNumber(item.getDish().getPrice())
				+ GLOBAL.CONSTANTS.PRICE_UNIT + "/" + item.getDish().getUnit());
		holder.tvUnit.setText(item.getDish().getUnit());
		holder.tvType.setText("Bình thường");
		arrData = new ArrayList<String>();
		for (int i = 1; i <= 30; i++) {
			arrData.add(i + "");
		}
		spnAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, arrData);

		holder.spnQuantity.setAdapter(spnAdapter);

		holder.spnQuantity
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
//						Toast.makeText(context,
//								"Ban da chon : " + arrData.get(position),
//								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		return convertView;
	}

	public class HolderView {
		private TextView tvName;
		private TextView tvPrice;
		private TextView tvUnit;
		private TextView tvType;
		private Spinner spnQuantity;
	}

}
