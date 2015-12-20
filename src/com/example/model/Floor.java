package com.example.model;

public class Floor {
	private int id;
	private String name;
	private String status;
//	private Table table;
	
	public Floor() {
		// TODO Auto-generated constructor stub
	}
	

	public Floor(int id, String name, String status) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
//		this.table = table;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


//	public Table getTable() {
//		return table;
//	}
//
//
//	public void setTable(Table table) {
//		this.table = table;
//	}
	
	
}
