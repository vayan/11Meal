package com.vaya.elevenMeal.restaurant;

public class Restaurant implements IRestaurantObject {
	//Attributes
	protected int id;
	protected String name;
	protected String address;
	protected String phone;
	protected String position;
	protected String schedule;
	protected String description;
	
	
	public Restaurant() {
		
	}

	public Restaurant(int id, String name, String address)
	{
		this.id = id;
		this.name = name;
		this.address = address;
	}
	
	// Getters/setters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
