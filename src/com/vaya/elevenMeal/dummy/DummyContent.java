package com.vaya.elevenMeal.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaya.elevenMeal.restaurant.Reservation;
import com.vaya.elevenMeal.restaurant.Restaurant;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {
	
	/**
	 * An array of sample (dummy) items.
	 */
	public static List<Restaurant> ITEMS = new ArrayList<Restaurant>();
	public static List<Reservation> ITEMS_BOOKS = new ArrayList<Reservation>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<Integer , Restaurant> ITEM_MAP = new HashMap<Integer , Restaurant>();
	public static Map<Integer , Reservation> ITEM_MAP_BOOKS = new HashMap<Integer , Reservation>();

	static {
		// Add 3 sample items.
		addItem(new Restaurant(0, "asiate", "Asiates contemporary American cuisine is creatively accented with modern, artistic touches, presenting one of the most unique dining experiences in Manhattan. A combination of gracious service, inventive cuisine, stylish décor and stunning views ensure that the restaurant is a hit with both guests and locals alike. Using Central Park views for inspiration, the focal point of the restaurant is an incredible tree-branch sculpture symbolizing trees in winter. Hanging from the ceiling, it is breathtaking in both scope and style. Add to this a huge wall of wine housing over 1,300 bottles and a seasonal menu featuring fresh, local produce, and you have the perfect environment in which to enjoy delectable cuisine."));
		addItem(new Restaurant(1, "resto 2", "add"));
		addItem(new Restaurant(2, "resto 3", "add"));
	}

	private static void addItem(Restaurant item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.getName(), item);
	}
}
