package com.vaya.elevenMeal;

import java.util.UUID;

public class Meal {
	public static enum Type {
		SALAD,
		SPICY,
		SOUR,
	}

	// Attributes
	protected UUID mId;
	protected String mName;
	protected float mPrice;
	protected Type mType;

	// Getter/setters
	public String getName() {
		return mName;
	}

	public UUID getId() {
		return mId;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public float getPrice() {
		return mPrice;
	}

	public void setPrice(float price) {
		mPrice = price;
	}

	public Type getType() {
		return mType;
	}

	public void setType(Type type) {
		mType = type;
	}
}
