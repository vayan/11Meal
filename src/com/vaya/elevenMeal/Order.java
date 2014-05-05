package com.vaya.elevenMeal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Order {
	// Attributes
	protected UUID mId;
	protected List<Meal> mMealList;

	public Order() {
		mMealList = new ArrayList<Meal>();
	}

	// Getters/setters
	public UUID getId() {
		return mId;
	}

	public List<Meal> getMealList() {
		return mMealList;
	}

	public void setMealList(List<Meal> mealList) {
		mMealList = mealList;
	}

	// Methods
	public void addMeal(Meal meal) {
		mMealList.add(meal);
	}

	public void removeMeal(Meal meal) {
		Iterator<Meal> it = mMealList.iterator();
		while (it.hasNext()) {
			if (it.next().getId().equals(meal.getId())) {
				it.remove();
				break;
			}
		}
	}

	public float getTotalPrice() {
		float totalPrice = 0;
		for (Meal m: mMealList)
			totalPrice += m.getPrice();
		return totalPrice;
	}
}
