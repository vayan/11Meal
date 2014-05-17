package com.vaya.elevenMeal;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vaya.elevenMeal.restaurant.IRestaurantObject;
import com.vaya.elevenMeal.restaurant.Meal;
import com.vaya.elevenMeal.restaurant.Order;
import com.vaya.elevenMeal.restaurant.Reservation;
import com.vaya.elevenMeal.restaurant.Restaurant;
import com.vaya.elevenMeal.restaurant.User;

/**
 * A fragment representing a single Reservation detail screen. This fragment is
 * either contained in a {@link ReservationListActivity} in two-pane mode (on
 * tablets) or a {@link ReservationDetailActivity} on handsets.
 */
public class ReservationDetailFragment extends Fragment
implements OnTaskCompleted {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Reservation reservation = null;
	private TextView resName;
	private TextView dateHour;
	private TextView viewPeople;
	private TextView viewOrder;
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ReservationDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			new API(this).get(new Reservation(), "id",
					String.valueOf(getArguments().getInt(ARG_ITEM_ID)));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_reservation_detail,
				container, false);

		resName    = (TextView) rootView.findViewById(R.id.reservationResName);
		dateHour   = (TextView) rootView.findViewById(R.id.dateHour);
		viewPeople = (TextView) rootView.findViewById(R.id.viewPeople);
		viewOrder  = (TextView) rootView.findViewById(R.id.viewOrder);

		return rootView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onTaskCompleted(Object res, java.lang.reflect.Type type) {
		if (res == null)
			return ;
		IRestaurantObject result;
		try {
			result = ((List<IRestaurantObject>) res).get(0);
		} catch (IndexOutOfBoundsException e) {
			return ;
		}
		
		if (result.getClass().equals(Reservation.class)) {
			reservation = Reservation.class.cast(result);
			dateHour.setText(reservation.getDate().toString());
			viewPeople.setText("");
			for (User u: reservation.getGuests()) {
				viewPeople.setText(viewPeople.getText() + u.getLogin() + "\n");
			}

			new API(this).get(new Restaurant(), "id",
					String.valueOf(reservation.getRestaurantId()));
			new API(this).get(new Order(), "reservation",
					String.valueOf(reservation.getId()));
		}
		else if (result.getClass().equals(Restaurant.class)) {
			Restaurant resto = Restaurant.class.cast(result);
			resName.setText(resto.getName());
		}
		else if (result.getClass().equals(Order.class)) {
			Order order = Order.class.cast(result);
			viewOrder.setText("");
			for (Meal m: order.getMealList()) {
				viewOrder.setText(m.getName() + "\t\t\t" + m.getPrice() + "\n");
			}
		}
	}
}
