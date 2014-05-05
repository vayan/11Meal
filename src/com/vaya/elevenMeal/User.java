package com.vaya.elevenMeal;

import java.util.List;

public class User {
	// Attributes
	protected int mId;
	protected String mLogin;
	protected String mFirstName;
	protected String mLastName;
	protected String mEmail;
	protected String mPhone;
	
	// Getters/setters
	public int getId() {
		return mId;
	}

	public String getLogin() {
		return mLogin;
	}

	public void setLogin(String login) {
		mLogin = login;
	}

	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String lastName) {
		mLastName = lastName;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	// Methods
	public List<Reservation> getReservations() {
		return null; //TODO: complete stub
	}
}
