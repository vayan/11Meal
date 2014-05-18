package com.vaya.elevenMeal.restaurant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Order implements IRestaurantObject {
	// Attributes
	protected int id;
	protected int user;
	protected int total_price;
	protected int restaurant;
	protected int reservation;
	protected List<Meal> meals;

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public int getTotal_price() {
		return total_price;
	}

	public void setTotal_price(int total_price) {
		this.total_price = total_price;
	}

	public int getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(int restaurant) {
		this.restaurant = restaurant;
	}

	public int getReservation() {
		return reservation;
	}

	public void setReservation(int reservation) {
		this.reservation = reservation;
	}
	
	public Order() {
		meals = new ArrayList<Meal>();
	}

	public List<Meal> getMealList() {
		return meals;
	}

	public void setMealList(List<Meal> mealList) {
		meals = mealList;
	}

	// Methods
	public void addMeal(Meal meal) {
		meals.add(meal);
	}

	public void removeMeal(Meal meal) {
		Iterator<Meal> it = meals.iterator();
		while (it.hasNext()) {
			if (it.next().getId() == meal.getId()) {
				it.remove();
				break;
			}
		}
	}

	public float getTotalPrice() {
		float totalPrice = 0;
		for (Meal m: meals)
			totalPrice += m.getPrice();
		return totalPrice;
	}

	@Override
	public int getId() {
		return id;
	}
}
