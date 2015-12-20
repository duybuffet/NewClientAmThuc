package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.connect.GLOBAL;
import com.example.model.OrderDetails;

public class CustomChefArrayAdapter extends ArrayAdapter<OrderDetails> {
	private List<OrderDetails> objects;
	public CustomChefArrayAdapter(Context context, int resource,
			List<OrderDetails> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.objects = objects;
	}
	
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		OrderDetails lineItem = objects.get(position);
		if (lineItem.getStatus() == GLOBAL.ORDER_DETAILS_STATUS.CANCEL
				|| lineItem.getStatus() == GLOBAL.ORDER_DETAILS_STATUS.FINISH) {
			return false;
		}
		return true;
	}

}
