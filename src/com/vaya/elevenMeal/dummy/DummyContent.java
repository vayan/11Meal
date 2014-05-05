package com.vaya.elevenMeal.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.vaya.elevenMeal.Restaurant;

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

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, Restaurant> ITEM_MAP = new HashMap<String, Restaurant>();

	static {
		// Add 3 sample items.
		addItem(new Restaurant(UUID.randomUUID(), "resto 1", "add"));
		addItem(new Restaurant(UUID.randomUUID(), "resto 2", "add"));
		addItem(new Restaurant(UUID.randomUUID(), "resto 3", "add"));
	}

	private static void addItem(Restaurant item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.getName(), item);
	}
}
