package com.vaya.elevenMeal;

import java.util.UUID;

public class Meal {
	public static enum Type {
		SALAD,
		SPICY,
		SOUR,
	}

	protected UUID mId;
	protected String mName;
	protected float mPrice;
	protected Type mType;
}
