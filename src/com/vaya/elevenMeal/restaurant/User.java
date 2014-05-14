package com.vaya.elevenMeal.restaurant;

import java.util.List;

public class User implements IRestaurantObject {
	// Attributes
	protected int id;
	protected String login;
	protected String first_Name;
	protected String last_Name;
	protected String email;
	protected String phone;
	protected String phoneUID;
	protected String gCM_ID;
	
	// Getters/setters
	public int getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return first_Name;
	}

	public void setFirstName(String firstName) {
		first_Name = firstName;
	}

	public String getLastName() {
		return last_Name;
	}

	public void setLastName(String lastName) {
		last_Name = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGcmId() {
		return gCM_ID;
	}

	public void setGcmId(String gcmId) {
		this.gCM_ID = gcmId;
	}

	// Methods
	public List<Reservation> getReservations() {
		return null; //TODO: complete stub
	}

	public String getPhoneUID() {
		return phoneUID;
	}

	public void setPhoneUID(String phoneUID) {
		this.phoneUID = phoneUID;
	}
	
	@Override
	public String toString() {
		return getLogin();
	}
}
