package com.vaya.elevenMeal;

import java.util.List;

import com.vaya.elevenMeal.restaurant.Reservation;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReservationListAdapter extends ArrayAdapter<Reservation>{

	public ReservationListAdapter(Context context, int resource,
			List<Reservation> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View rowView = inflater.inflate(R.layout.adapter_reservation_list, null);
		TextView nameView = (TextView) rowView.findViewById(R.id.reservationListName);
		TextView creatorView = (TextView) rowView.findViewById(R.id.reservationListCreator);
		
		nameView.setText(getItem(position).getOwner().getLogin());
		creatorView.setText(getItem(position).getOwner().getLogin());
		
		return rowView;
	}
}
