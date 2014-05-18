package com.vaya.elevenMeal.test;

import java.util.List;

import com.vaya.elevenMeal.API;
import com.vaya.elevenMeal.restaurant.Reservation;
import com.vaya.elevenMeal.restaurant.User;

import junit.framework.TestCase;

public class APITest extends TestCase {
	private API  api;

	protected void setUp() throws Exception {
		super.setUp();
		
		api = new API();
		api.setAsync(false);
	}
	
	@SuppressWarnings("unchecked")
	public void testCreate() {
		// Full user
		User user1 = new User();
		user1.setFirstName("Toto");
		user1.setLastName("DUPONT");
		user1.setEmail("toto@example.com");
		user1.setLogin("toto42");
		user1.setPassword("01011970");
		user1.setPhone("+33 6 00 13 37 42");
		user1.setGcmId("foo");
		user1.setPhoneUID("foo");
		user1.setLocation("0.1337,0.0042");
		api.create(user1);
		
		// Partial users
		User user2 = new User();
		user2.setLogin("tata");
		api.create(user2);
		
		User user3 = new User();
		user3.setLogin("titi");
		api.create(user3);

		// Get back users from server
		api.get(new User(), "login", "toto42");
		user1 = ((List<User>) api.getLastResult()).get(0);
		api.get(new User(), "login", "tata");
		user2 = ((List<User>) api.getLastResult()).get(0);
		api.get(new User(), "login", "titi");
		user3 = ((List<User>) api.getLastResult()).get(0);

		// Reservation with theses users
		Reservation reservation = new Reservation();
		reservation.setOwner(user1);
		reservation.addGuest(user2);
		reservation.addGuest(user3);
		api.create(reservation);
		api.getLastResult();
	}

	@SuppressWarnings("unchecked")
	public void testGet() {
		api.get(new User(), "login", "toto42");
		User user = ((List<User>) api.getLastResult()).get(0);
		assertEquals("toto42", user.getLogin());
	}

	@SuppressWarnings("unchecked")
	public void testDelete() {
		api.get(new User(), "login", "tata");
		List<User> userList = (List<User>) api.getLastResult();
		assertNotNull(userList);
		User user = userList.get(0);
		api.delete(user);
	}
}
