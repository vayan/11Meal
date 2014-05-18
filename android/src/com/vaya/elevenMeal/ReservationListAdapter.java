package com.vaya.elevenMeal;

import java.util.List;

import com.vaya.elevenMeal.ReservationListFragment.ResView;
import com.vaya.elevenMeal.restaurant.Reservation.State;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReservationListAdapter extends ArrayAdapter<ResView> {
	private TextView nameView;
	private TextView creatorView;

	public ReservationListAdapter(Context context, int resource,
			List<ResView> resViewList) {
		super(context, resource, resViewList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View rowView = inflater.inflate(R.layout.adapter_reservation_list, null);
		nameView = (TextView) rowView.findViewById(R.id.reservationListName);
		creatorView = (TextView) rowView.findViewById(R.id.reservationListCreator);
		
		//FIXME: Use @strings resources
		nameView.setText(getItem(position).mRestaurantName);
		creatorView.setText(getItem(position).mOwnerName);
		
		View dot = ((View) rowView.findViewById(R.id.doabarrelroll));
		if (getItem(position).mState == State.OPENED)
			dot.setBackground(getContext().getResources().getDrawable(R.drawable.circle_orange));
		else if (getItem(position).mState == State.CONFIRMED)
			dot.setBackground(getContext().getResources().getDrawable(R.drawable.circle_green));
		else if (getItem(position).mState == State.CANCELED)
			dot.setBackground(getContext().getResources().getDrawable(R.drawable.circle_red));
		else
			dot.setBackground(getContext().getResources().getDrawable(R.drawable.circle_blue));

		return rowView;
	}
}
