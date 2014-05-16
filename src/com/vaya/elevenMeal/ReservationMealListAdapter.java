package com.vaya.elevenMeal;

import java.util.List;

import com.vaya.elevenMeal.restaurant.Meal;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ReservationMealListAdapter extends ArrayAdapter<Meal>{
	private Boolean[] mChecked;
	
	public Boolean[] getChecked()
	{
		return mChecked;
	}
	
	public ReservationMealListAdapter(Context context, int resource,
			List<Meal> objects) {
		super(context, resource, objects);
		mChecked = new Boolean[objects.size()];
		for(int i = 0; i < objects.size(); i++){
			mChecked[i] = false;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View rowView = inflater.inflate(R.layout.adapter_meal_list, null);
		CheckBox checkbox = (CheckBox) rowView.findViewById(R.id.mealName);
		TextView price = (TextView) rowView.findViewById(R.id.mealPrice);
		
		checkbox.setFocusable(false);
		checkbox.setFocusableInTouchMode(false);
		checkbox.setClickable(false);
		//checkbox.setTag(Integer.valueOf(position));
		checkbox.setChecked(mChecked[position]);
		//checkbox.setOnCheckedChangeListener(mListener);
		
		checkbox.setText(getItem(position).getName());
		price.setText(String.valueOf(getItem(position).getPrice()));
		
		//MealRow = new MealRow(checkbox, price);
		
		rowView.setClickable(true);
		rowView.setTag(new MealRow(checkbox, price, position));
		rowView.setOnClickListener(mListener);
		
		return rowView;
	}
	
//	OnCheckedChangeListener mListener = new OnCheckedChangeListener() {
//		
//		@Override
//		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//			mChecked[(Integer)buttonView.getTag()] = isChecked;
//			
//		}
//	};
	
	OnClickListener mListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			MealRow row = ((MealRow) v.getTag());
			row.checkbox.setChecked(!row.checkbox.isChecked());
			mChecked[row.position] = row.checkbox.isChecked();
		}
	};
	
	
	private class MealRow
	{
		public CheckBox checkbox;
		public TextView textView;
		public int position;
		
		public MealRow(CheckBox c, TextView t, int i)
		{
			checkbox = c;
			textView = t;
			position = i;
		}
	}
}
