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
import com.example.model.Category;

public class CategoryAdapter extends BaseAdapter{
	private List<Category> arrItems;
	private LayoutInflater mInflater;
	private Context context;
	
	public CategoryAdapter(Context activity, List<Category> arrItems) {
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
			convertView = mInflater.inflate(R.layout.item_select_lv_layout,
					null);
			holder.tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);
			holder.bgCategory = (LinearLayout) convertView.findViewById(R.id.bgCategory);
			convertView.setTag(holder);
		}else {
			holder = (HolderView) convertView.getTag();
		}
		final Category item = arrItems.get(position);
		
		holder.tvCategory.setText(item.getName());
		int i = position%2;
		switch(i){
			case 0:
				holder.bgCategory.setBackgroundColor(Color.parseColor("#D8B889"));
				break;
			case 1:
				holder.bgCategory.setBackgroundColor(Color.parseColor("#DB800E"));
				break;
		}
		
		
		return convertView;
	}
	
	public class HolderView {
		private TextView tvCategory;
		private LinearLayout bgCategory;
	}

}
