package com.vaya.elevenMeal;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Restaurant {
	//Attributes
	protected UUID mId;
	protected String mName;
	protected String mAddress;
	protected String mPhone;
	protected String mPosition;
	protected List<Date> mTimeTable;
	
	// Getters/setters
	public UUID getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String address) {
		mAddress = address;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	public String getPosition() {
		return mPosition;
	}

	public void setPosition(String position) {
		mPosition = position;
	}

	public List<Date> getTimeTable() {
		return mTimeTable;
	}

	public void setTimeTable(List<Date> timeTable) {
		mTimeTable = timeTable;
	}
}
