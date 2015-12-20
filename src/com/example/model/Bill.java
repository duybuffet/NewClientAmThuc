package com.example.model;

public class Bill {
	private int id;
	private String time;
	private Float customerMoney;
	private Float customerChange;
	private Order order;

	public Bill() {
	}

	public Bill(int id, String time, Float customerMoney, Float customerChange,
			Order order) {
		this.id = id;
		this.time = time;
		this.customerMoney = customerMoney;
		this.customerChange = customerChange;
		this.order = order;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Float getCustomerMoney() {
		return customerMoney;
	}

	public void setCustomerMoney(Float customerMoney) {
		this.customerMoney = customerMoney;
	}

	public Float getCustomerChange() {
		return customerChange;
	}

	public void setCustomerChange(Float customerChange) {
		this.customerChange = customerChange;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
