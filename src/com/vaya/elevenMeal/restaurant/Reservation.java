package com.vaya.elevenMeal.restaurant;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Reservation implements IRestaurantObject {
	public static enum State {
		OPENED, // Reservation can be modified by users
		CONFIRMED, // Confirmed by restaurant, read-only for user
		QUEUED,
		DONE,
		CANCELED, // Canceled, read-only
	}
	public static enum Payment {
		CASH,
		CHECK,
		CREDIT_CARD,
	}

	// Attributes
	protected int id;
	protected User user;
	protected ArrayList<User> guests;
	protected int restaurant;
	protected Date date;
	protected State state;
	protected Payment payementMethod;
	
	public Reservation(int id)
	{
		this.id = id;
	}
	
	// Getters/setters
	public int getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public User getOwner() {
		return user;
	}
	
	public ArrayList<User> getGuests() {
		return guests;
	}

	public void setOwner(User owner) {
		this.user = owner;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Payment getPayMethod() {
		return payementMethod;
	}

	public void setPayMethod(Payment payMethod) {
		this.payementMethod = payMethod;
	}

	// Methods
	public void addGuest(User user) {
		guests.add(user);
	}
	
	public void removeGuest(User user) {
		Iterator<User> it = guests.iterator();
		while (it.hasNext()) {
			if (it.next().getId() == user.getId()) {
				it.remove();
				break;
			}
		}
	}

	// FIXME: Do we really need theses methods?
	public boolean delete() {
		return false; //TODO: complete stub
	}

	public boolean update() {
		return false; //TODO: complete stub
	}
}
