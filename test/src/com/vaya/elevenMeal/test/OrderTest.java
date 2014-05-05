package com.vaya.elevenMeal.test;

import java.util.ArrayList;

import com.vaya.elevenMeal.Meal;
import com.vaya.elevenMeal.Order;

import junit.framework.TestCase;

public class OrderTest extends TestCase {
	protected Meal mMeal1;
	protected Meal mMeal2;
	protected Order mOrder;

	protected void setUp() throws Exception {
		super.setUp();
		mMeal1 = new Meal();
		mMeal2 = new Meal();
		mOrder = new Order();
		
		mMeal1.setName("Tomato Salad");
		mMeal1.setPrice(10);
		mMeal2.setName("Beef Steak");
		mMeal2.setPrice(42);
	}

	public void testGetTotalPrice() {
		mOrder.addMeal(mMeal1);
		mOrder.addMeal(mMeal2);
		float price = mMeal1.getPrice() + mMeal2.getPrice();
		assertEquals(price, mOrder.getTotalPrice());
	}

	public void testAddMeal() {
		mOrder.addMeal(mMeal1);
		mOrder.addMeal(mMeal2);
		ArrayList<Meal> meals = (ArrayList<Meal>) mOrder.getMealList();
		assertTrue(
				meals.get(0).getName().equals(mMeal1.getName())
			 && meals.get(1).getName().equals(mMeal2.getName()));
	}

	public void testRemoveMeal() {
		mOrder.addMeal(mMeal1);
		mOrder.addMeal(mMeal2);
		mOrder.removeMeal(mMeal2);
		ArrayList<Meal> meals = (ArrayList<Meal>) mOrder.getMealList();
		if (meals.size() != 1
		 || meals.get(0).getName().equals(mMeal1.getName()))
			fail();
		mOrder.removeMeal(mMeal1);
		meals = (ArrayList<Meal>) mOrder.getMealList();
		assertEquals(0, meals.size());
	}
}
