package com.vaya.elevenMeal;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Reservation {
	public static enum State {
		OPENED, // Reservation can be modified by users
		CONFIRMED, // Confirmed by restaurant, read-only for user
		CANCELED, // Canceled, read-only
	}
	public static enum Payment {
		CASH,
		CHECK,
		CREDIT_CARD,
	}

	// Attributes
	protected int mId;
	protected User mOwner;
	protected ArrayList<User> mGuests;
	protected Date mDate;
	protected State mState;
	protected Payment mPayMethod;
	
	// Getters/setters
	public int getId() {
		return mId;
	}

	public Date getDate() {
		return mDate;
	}

	public User getOwner() {
		return mOwner;
	}
	
	public ArrayList<User> getGuests() {
		return mGuests;
	}

	public void setOwner(User owner) {
		mOwner = owner;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public State getState() {
		return mState;
	}

	public void setState(State state) {
		mState = state;
	}

	public Payment getPayMethod() {
		return mPayMethod;
	}

	public void setPayMethod(Payment payMethod) {
		mPayMethod = payMethod;
	}

	// Methods
	public void addGuest(User user) {
		mGuests.add(user);
	}
	
	public void removeGuest(User user) {
		Iterator<User> it = mGuests.iterator();
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
