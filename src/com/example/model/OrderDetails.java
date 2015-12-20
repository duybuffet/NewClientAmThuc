package com.example.model;

import java.io.Serializable;

import com.example.connect.GLOBAL;

public class OrderDetails implements Serializable {

	private int id;
	private float displayPrice;
	private int quantity;
	private int status;
	private Order order;
	private Dish dish;
	private User chef;

	public OrderDetails() {
	}

	public OrderDetails(int id) {
		this.id = id;
	}

	public OrderDetails(int id, int quantity) {
		this.id = id;
		this.quantity = quantity;
	}

	public OrderDetails(float displayPrice, int quantity, Dish dish) {
		this.displayPrice = displayPrice;
		this.quantity = quantity;
		this.dish = dish;
	}

	public OrderDetails(float displayPrice, int quantity, Order order, Dish dish) {
		this.displayPrice = displayPrice;
		this.quantity = quantity;
		this.order = order;
		this.dish = dish;
	}

	public OrderDetails(int id, int quantity, int status, Dish dish) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.status = status;
		this.dish = dish;
	}

	public OrderDetails(int id, int quantity, int status) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getDisplayPrice() {
		return displayPrice;
	}

	public void setDisplayPrice(float displayPrice) {
		this.displayPrice = displayPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Dish getDish() {
		return dish;
	}

	public void setDish(Dish dish) {
		this.dish = dish;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getChef() {
		return chef;
	}

	public void setChef(User chef) {
		this.chef = chef;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getDish().getName() + " - số lượng: ("
				+ this.getQuantity() + ") - "
				+ GLOBAL.ORDER_DETAILS_STATUS.DISPLAY[this.getStatus()];
	}

}
