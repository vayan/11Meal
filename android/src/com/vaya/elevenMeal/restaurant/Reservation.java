package com.vaya.elevenMeal.restaurant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.util.Log;

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
	protected int user;
	protected ArrayList<User> guests;
	protected int restaurant;
	protected String date;
	protected int state;
	protected int payementMethod;

	public Reservation() {
		guests = new ArrayList<User>();
	}
	
	public Reservation(int id)
	{
		this.id = id;
		guests = new ArrayList<User>();
	}
	
	// Getters/setters
	public int getId() {
		return id;
	}

	public Date getDate() {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
		} catch (ParseException e) {
			Log.w("Reservation.getdate", e.getMessage());
		}
		return null;
	}

	public int getOwnerId() {
		return user;
	}
	
	public ArrayList<User> getGuests() {
		return guests;
	}

	public void setOwner(User owner) {
		this.user = owner.getId();
	}

	public int getRestaurantId() {
		return restaurant;
	}
	
	public void setRestaurantId(int id) {
		this.restaurant = id;
	}

	public void setDate(Date date) {
		this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public State getState() {
		return State.values()[state];
	}

	public void setState(State state) {
		this.state = state.ordinal();
	}

	public Payment getPayMethod() {
		return Payment.values()[payementMethod];
	}

	public void setPayMethod(Payment payMethod) {
		this.payementMethod = payMethod.ordinal();
	}

	// Methods
	public void addGuest(User user) {
		guests.add(user);
	}
	
	public void removeGuest(int user) {
		Iterator<User> it = guests.iterator();
		while (it.hasNext()) {
			if (it.next().getId() == user) {
				it.remove();
				break;
			}
		}
	}
	
	public void setListGuest(ArrayList<User> users)
	{
		this.guests = users;
	}

	// FIXME: Do we really need theses methods?
	public boolean delete() {
		return false; //TODO: complete stub
	}

	public boolean update() {
		return false; //TODO: complete stub
	}

	public void setOwnerId(int id) {
		this.user = id; // TODO Auto-generated method stub
		
	}
}
