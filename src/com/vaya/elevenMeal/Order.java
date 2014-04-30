package com.vaya.elevenMeal;

import java.util.List;
import java.util.UUID;

public class Order {
	// Attributes
	protected UUID mId;
	protected List<Meal> mMealList;

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
		//TODO: complete stub
	}

	public void removeMeal(Meal meal) {
		//TODO: complete stub
	}

	public float getTotalPrice() {
		return 0; //TODO: complete stub
	}
}
