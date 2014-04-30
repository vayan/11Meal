package com.vaya.elevenMeal;

import java.util.Date;
import java.util.UUID;

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
	protected UUID mId;
	protected Date mDate;
	protected State mState;
	protected Payment mPayMethod;
	
	// Getters/setters
	public UUID getId() {
		return mId;
	}

	public Date getDate() {
		return mDate;
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
	public void sendInvitation(User user) {
		//TODO: complete stub
	}

	public boolean delete() {
		return false; //TODO: complete stub
	}

	public boolean update() {
		return false; //TODO: complete stub
	}
}
