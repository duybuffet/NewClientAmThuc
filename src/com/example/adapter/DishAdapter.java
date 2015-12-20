package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ptit.amthuc.R;
import com.example.connect.GLOBAL;
import com.example.model.Category;
import com.example.model.Dish;
import com.example.util.Helper;

public class DishAdapter extends BaseAdapter{
	private List<Dish> arrItems;
	private LayoutInflater mInflater;
	private Context context;
	
	public DishAdapter(Context activity, List<Dish> arrItems) {
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
			convertView = mInflater.inflate(R.layout.item_select_grv_layout,
					null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
			convertView.setTag(holder);
		}else {
			holder = (HolderView) convertView.getTag();
		}
		final Dish item = arrItems.get(position);
		
		holder.tvName.setText(item.getName());
		holder.tvPrice.setText(Helper.formatNumber(item.getPrice()) + GLOBAL.CONSTANTS.PRICE_UNIT + "/" + item.getUnit());
		
		return convertView;
	}
	
	public class HolderView {
		private TextView tvName;
		private TextView tvPrice;
	}

}
