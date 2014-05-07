package com.vaya.elevenMeal;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vaya.elevenMeal.dummy.DummyContent;
import com.vaya.elevenMeal.restaurant.Restaurant;

/**
 * A fragment representing a single Restaurant detail screen. This fragment is
 * either contained in a {@link RestaurantListActivity} in two-pane mode (on
 * tablets) or a {@link RestaurantDetailActivity} on handsets.
 */
public class RestaurantDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Restaurant mItem;

	private Button mBookButton;
	private TextView mMenu;
	private TextView mCall;
	private TextView mPosition;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RestaurantDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			int i = getArguments().getInt(ARG_ITEM_ID);
			mItem = DummyContent.ITEM_MAP.get(getArguments()
					.getInt(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_restaurant_detail,
				container, false);
		mBookButton = ((Button) rootView
				.findViewById(R.id.restaurantDetailsReservation));
		mMenu = (TextView) rootView.findViewById(R.id.restaurantDetailsMenu);
		mCall = (TextView) rootView.findViewById(R.id.restaurantDetailsCall);
		mPosition = (TextView) rootView.findViewById(R.id.restaurantDetailsPosition);
		// Show the dummy content as text in a TextView.
		// if (mItem != null) {
		/*
		 * ((ImageView) rootView.findViewById(R.id.restaurantDetailsPreview))
		 * .setImageDrawable(this.getResources().getDrawable(
		 * R.drawable.dummy));
		 */
		((TextView) rootView.findViewById(R.id.restaurantDetailsName))
				.setText(mItem.getName());
		((TextView) rootView.findViewById(R.id.restaurantDetailsText))
				.setText(mItem.getAddress());
		// }
		return rootView;
	}

	@Override
	public void onResume() {

		mMenu.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("Moi", "Click");
				return false;
			}
		});

		mCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(getActivity());
				dialog.setContentView(R.layout.dialog_restaurant_contact);
				dialog.setTitle(R.string.contact);
				dialog.findViewById(R.id.dialogDismiss).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								dialog.dismiss();
							}
						});
				dialog.show();
			}
		});
		
		mPosition.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
					    Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
					startActivity(intent);
			}
		});
		
		mBookButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),
						ReservationActivity.class));
			}
		});
		super.onResume();
	}

	@Override
	public void onPause() {
		if (mBookButton != null)
			mBookButton.setOnClickListener(null);
		if (mMenu != null)
			mMenu.setOnTouchListener(null);
		if (mCall != null)
			mCall.setOnTouchListener(null);
		super.onPause();
	}
}
