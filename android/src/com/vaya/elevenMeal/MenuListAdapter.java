package com.vaya.elevenMeal;

import java.util.List;

import com.vaya.elevenMeal.restaurant.Meal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MenuListAdapter extends ArrayAdapter<Meal>{

	public MenuListAdapter(Context context, int resource,
			List<Meal> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View rowView = inflater.inflate(R.layout.adapter_menu_list, null);
		TextView name = (TextView) rowView.findViewById(R.id.dialogMenuName);
		TextView price = (TextView) rowView.findViewById(R.id.dialogMenuPrice);
		
		
		name.setText(getItem(position).getName());
		price.setText(String.valueOf(getItem(position).getPrice()));
		
		return rowView;
	}
}
