package com.vaya.elevenMeal.restaurant;

public class Meal implements IRestaurantObject {
	public static enum Type {
		STARTER,
		MAIN,
		DESSERT,
		DRINK,
		SOUP,
		UNDEFINED,
	}

	// Attributes
	protected int id;
	protected int restaurant;
	protected String name;
	protected int price;
	protected int type;

	// Getter/setters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setIdRestaurant(int id) {
		restaurant = id;
	}
	
	public Type getType() {
		return Type.values()[type];
	}

	public void setType(Type type) {
		this.type = type.ordinal();
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
