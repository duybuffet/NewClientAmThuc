package com.example.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.model.Dish;
import com.example.model.OrderDetails;

public class DBHelper extends SQLiteOpenHelper {

	public final static String DATABASE_NAME = "MyDBName.db";
	public final String ORDER_DETAILS_TABLE_NAME = "tbl_order_details";
	public final String ORDER_DETAILS_COLUMN_ID = "id";
	public final String ORDER_DETAILS_COLUMN_TABLE_ID = "table_id";
	public final String ORDER_DETAILS_COLUMN_FOOD_ID = "food_id";
	public final String ORDER_DETAILS_COLUMN_QUANTITY_ID = "quantity";
	private HashMap hp;
	private List<Dish> listDishes;

	public DBHelper(Context context, List<Dish> listDishes) {
		super(context, DATABASE_NAME, null, 1);
		this.listDishes = listDishes;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table tbl_order_details "
				+ "(id integer primary key, table_id integer, food_id integer, quantity integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS tbl_order_details");
		onCreate(db);
	}

	public List<OrderDetails> getOrderDetailsIdByTableId(int tableId) {
		List<OrderDetails> res = new ArrayList<OrderDetails>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM tbl_order_details WHERE table_id = " + tableId,
				null);
		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {
			int foodId = cursor.getInt(cursor.getColumnIndex(ORDER_DETAILS_COLUMN_FOOD_ID));
			int qtt = cursor.getInt(cursor.getColumnIndex(ORDER_DETAILS_COLUMN_QUANTITY_ID));
			
			OrderDetails details = new OrderDetails();			
			details.setQuantity(qtt);
			details.setDish(getDishByID(foodId));
			res.add(details);
			
			cursor.moveToNext();
		}
		return res;
	}
	
	private Dish getDishByID(int id) {
		Dish res = new Dish();
		for (Dish d : listDishes) {
			if (d.getId() == id) {
				res = d;
			}
		}
		return res; 
	}

	public long insertOrderDetails(int tableId, int foodId, int qtt) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(ORDER_DETAILS_COLUMN_TABLE_ID, tableId);
		contentValues.put(ORDER_DETAILS_COLUMN_FOOD_ID, foodId);
		contentValues.put(ORDER_DETAILS_COLUMN_QUANTITY_ID, qtt);
		Log.e("insertOrderDetails", "SUCCESS");
		return db.insert(ORDER_DETAILS_TABLE_NAME, null, contentValues);
	}
	
	public int updateOrderDetails(int tableId, int foodId, int qtt) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues newValues = new ContentValues();
		newValues.put(ORDER_DETAILS_COLUMN_QUANTITY_ID, qtt);
		return db.update(ORDER_DETAILS_TABLE_NAME, newValues, "table_id = " + tableId + " AND foodId = " + foodId, null);
	}
	
	public boolean deleteOrderLDetails(int tableId) {
		SQLiteDatabase db = this.getWritableDatabase();
		Log.e("DELETE ORDER OF TABLE ", tableId + "");
		return db.delete(ORDER_DETAILS_TABLE_NAME, ORDER_DETAILS_COLUMN_TABLE_ID + "=" + tableId, null) > 0;
	}
}
