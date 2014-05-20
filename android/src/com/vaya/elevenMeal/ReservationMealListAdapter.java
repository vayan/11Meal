package com.vaya.elevenMeal;

import java.util.List;

import com.vaya.elevenMeal.ReservationActivity.OrderFragment;
import com.vaya.elevenMeal.restaurant.Meal;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ReservationMealListAdapter extends ArrayAdapter<Meal>{
	private Integer[] mQuantity;
	
	public Integer[] getQuantities()
	{
		return mQuantity;
	}
	
	public ReservationMealListAdapter(Context context, int resource,
			List<Meal> objects) {
		super(context, resource, objects);
		mQuantity = new Integer[objects.size()];
		for(int i = 0; i < objects.size(); i++){
			mQuantity[i] = 0;
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View rowView = inflater.inflate(R.layout.adapter_meal_list, null);
		TextView name = (TextView) rowView.findViewById(R.id.mealName);
		TextView price = (TextView) rowView.findViewById(R.id.mealPrice);
		EditText quantity = (EditText) rowView.findViewById(R.id.Mealquantity);
		ImageButton minus = (ImageButton) rowView.findViewById(R.id.mealMinus);
		
		//checkbox.setTag(Integer.valueOf(position));
		//checkbox.setOnCheckedChangeListener(mListener);

		name.setText(getItem(position).getName());
		price.setText(String.valueOf(getItem(position).getPrice()));
		quantity.setText(String.valueOf(mQuantity[position]));
		
		//MealRow = new MealRow(checkbox, price);
		
		rowView.setClickable(true);
		rowView.setTag(new MealRow(name, price, quantity, minus, position));
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
			mQuantity[row.position] += 1;
			row.quantity.setText(String.valueOf(mQuantity[row.position]));
			OrderFragment.setTotalOrder();
		}
	};
	
	
	private class MealRow
	{
		public TextView name;
		public TextView price;
		public EditText quantity;
		public ImageButton minus;
		public int position;
		
		public MealRow(TextView n, TextView p, EditText q, ImageButton m, int i)
		{
			name = n;
			price = p;
			quantity = q;
			minus = m;
			position = i;
			
			minus.setOnClickListener(mListener);
		}
		
		private OnClickListener mListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mQuantity[position] > 0)
					mQuantity[position] -= 1;
				quantity.setText(String.valueOf(mQuantity[position]));
				OrderFragment.setTotalOrder();
			}
		};
		
		
	}
}
