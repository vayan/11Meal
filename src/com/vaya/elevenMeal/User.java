package com.vaya.elevenMeal;

import java.util.List;
import java.util.UUID;

public class User {
	protected UUID mId;
	protected String mLogin;
	protected String mFirstName;
	protected String mLastName;
	protected String mEmail;
	protected String mPhone;
	
	public List<Reservation> getReservations() {
		return null; //TODO: complete stub
	}
}
