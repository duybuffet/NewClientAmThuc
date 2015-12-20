package com.example.connect;

import java.net.Socket;

import com.example.model.User;

public class Client {
	private User user;
	private Socket socket;
	
	public Client(Socket socket){
		this.socket = socket;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	
}
