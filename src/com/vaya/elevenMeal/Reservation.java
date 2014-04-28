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

	protected UUID mId;
	protected Date mDate;
	protected State mState;
	protected Payment mPayMethod;
	
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
