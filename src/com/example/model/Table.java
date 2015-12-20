package com.example.model;

import java.io.Serializable;

public class Table implements Serializable {
	private int id;
	private String name;
	private int area;
	private int type;
	private int status;
	private User waiter;

	public Table() {
	}

	public Table(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Table(int id, String name, int area, int type, int status) {
		this.id = id;
		this.name = name;
		this.area = area;
		this.type = type;
		this.status = status;
	}

	public Table(int id, String name, int area) {
		this.id = id;
		this.name = name;
		this.area = area;
	}

	public Table(int id, String name, int area, int type) {
		this.id = id;
		this.name = name;
		this.area = area;
		this.type = type;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getWaiter() {
		return waiter;
	}

	public void setWaiter(User waiter) {
		this.waiter = waiter;
	}

	@Override
	public String toString() {
		return "Table{" + "id=" + id + ", name=" + name + ", area=" + area
				+ ", type=" + type + '}';
	}
}
