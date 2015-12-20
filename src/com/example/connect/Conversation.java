package com.example.connect;

import java.util.ArrayList;

import com.example.model.User;

public class Conversation {
	private User target;
	private ArrayList<ClientMessage> messages;
	public User getTarget() {
		return target;
	}
	public void setTarget(User target) {
		this.target = target;
	}
	public ArrayList<ClientMessage> getMessages() {
		return messages;
	}
	public void setMessages(ArrayList<ClientMessage> messages) {
		this.messages = messages;
	}
	public Conversation(User target, ArrayList<ClientMessage> messages) {
		super();
		this.target = target;
		this.messages = messages;
	}
	public Conversation() {
		super();
		target = new User();
		messages = new ArrayList<ClientMessage>();
	}
	
	
	
}
