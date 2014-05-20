package com.vaya.elevenMeal;

import java.util.List;

import com.vaya.elevenMeal.restaurant.Restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantListAdapter extends ArrayAdapter<Restaurant>{

	public RestaurantListAdapter(Context context, int resource,
			List<Restaurant> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View rowView = inflater.inflate(R.layout.adapter_restaurant_list, null);
		ImageView imageView = (ImageView) rowView.findViewById(
				R.id.restaurantPreview);
		TextView titleView = (TextView) rowView.findViewById(
				R.id.restaurantTitle);
		TextView detailsView = (TextView) rowView.findViewById(
				R.id.restaurantDetails);


		imageView.setImageDrawable(getContext().getResources().getDrawable(
				R.drawable.ic_launcher));
		titleView.setText(getItem(position).getName());
		detailsView.setText(getItem(position).getAddress());

		return rowView;
	}
}
