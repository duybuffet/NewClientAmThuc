package com.example.connect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Bill;
import com.example.model.Category;
import com.example.model.Dish;
import com.example.model.Order;
import com.example.model.OrderDetails;
import com.example.model.Table;
import com.example.model.User;

public class ClientMessage implements Serializable {
	private int msgID;
	private String msg;
	private User user;
	private ArrayList<User> arrUsers;
	private List<Category> arrCategories;
	private List<Dish> arrDishes;
	private List<Table> arrTables;
	private List<OrderDetails> arrOrderDetails;
	private Order order;
	private Bill bill;
	private OrderDetails orderDetails;
	private User target;

	public User getTarget() {
		return target;
	}

	public void setTarget(User target) {
		this.target = target;
	}

	public ClientMessage() {
		super();
	}

	public ArrayList<User> getArrUsers() {
		return arrUsers;
	}

	public void setArrUsers(ArrayList<User> arrUsers) {
		this.arrUsers = arrUsers;
	}

	public ClientMessage(int msgID, String msg, User user,
			ArrayList<User> arrUsers) {
		super();
		this.msgID = msgID;
		this.msg = msg;
		this.user = user;
		this.arrUsers = arrUsers;
	}

	public ClientMessage(int msgID, String msg, ArrayList<User> arrUsers) {
		super();
		this.msgID = msgID;
		this.msg = msg;
		this.arrUsers = arrUsers;
	}

	public ClientMessage(int msgID, String msg) {
		super();
		this.msgID = msgID;
		this.msg = msg;
	}

	public ClientMessage(int msgID, String msg, User user) {
		super();
		this.msgID = msgID;
		this.msg = msg;
		this.user = user;
	}

	public int getMsgID() {
		return msgID;
	}

	public void setMsgID(int msgID) {
		this.msgID = msgID;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Category> getArrCategories() {
		return arrCategories;
	}

	public void setArrCategories(List<Category> arrCategories) {
		this.arrCategories = arrCategories;
	}

	public List<Dish> getArrDishes() {
		return arrDishes;
	}

	public void setArrDishes(List<Dish> arrDishes) {
		this.arrDishes = arrDishes;
	}

	public List<Table> getArrTables() {
		return arrTables;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setArrTables(List<Table> arrTables) {
		this.arrTables = arrTables;
	}

	public OrderDetails getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(OrderDetails orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public List<OrderDetails> getArrOrderDetails() {
		return arrOrderDetails;
	}

	public void setArrOrderDetails(List<OrderDetails> arrOrderDetails) {
		this.arrOrderDetails = arrOrderDetails;
	}
	
	

}
