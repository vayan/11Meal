package com.vaya.elevenMeal;

import java.util.List;

import com.vaya.elevenMeal.restaurant.IRestaurantObject;
import com.vaya.elevenMeal.restaurant.Reservation;
import com.vaya.elevenMeal.restaurant.Restaurant;
import com.vaya.elevenMeal.restaurant.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReservationListAdapter extends ArrayAdapter<Reservation>
implements OnTaskCompleted {
	private TextView nameView;
	private TextView creatorView;

	public ReservationListAdapter(Context context, int resource,
			List<Reservation> objects) {
		super(context, resource, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View rowView = inflater.inflate(R.layout.adapter_reservation_list, null);
		nameView = (TextView) rowView.findViewById(R.id.reservationListName);
		creatorView = (TextView) rowView.findViewById(R.id.reservationListCreator);
		
		//FIXME: Use @strings resources
		nameView.setText("Unknown Restaurant");
		creatorView.setText("(unknown creator)");

		Reservation reservation = getItem(position);
		API api = new API(this);
		api.get(new User(), "id",
				String.valueOf(reservation.getOwnerId()));
		api.get(new Restaurant(), "id",
				String.valueOf(reservation.getRestaurantId())); 
		return rowView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onTaskCompleted(Object results, java.lang.reflect.Type type) {
		if (results == null)
			return ;
		IRestaurantObject result;
		try {
			result = ((List<IRestaurantObject>) results).get(0);
		} catch (IndexOutOfBoundsException e) {
			return ;
		}
		
		if (result.getClass().equals(User.class)) {
			User res = User.class.cast(result);
			creatorView.setText(res.getLogin());
			return ;
		}
		if (result.getClass().equals(Restaurant.class)) {
			Restaurant res = Restaurant.class.cast(result);
			nameView.setText(res.getName());
		}
	}
}
