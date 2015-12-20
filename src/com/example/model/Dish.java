package com.example.model;

public class Dish {
	private int id;
	private String name;
	private float price;
	private String unit;
	
	public Dish() {
		// TODO Auto-generated constructor stub
	}
	
	public Dish(int id, String name, float price, String unit) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.unit = unit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{DISH : "+id+" - "+name+"}";
	}
	
	
}
