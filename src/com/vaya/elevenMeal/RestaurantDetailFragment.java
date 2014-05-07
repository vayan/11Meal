package com.vaya.elevenMeal;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
			mItem = DummyContent.ITEM_MAP.get(getArguments().getInt(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_restaurant_detail,
				container, false);
		mBookButton = ((Button) rootView.findViewById(R.id.restaurantDetailsReservation));
		// Show the dummy content as text in a TextView.
		// if (mItem != null) {
		/*((ImageView) rootView.findViewById(R.id.restaurantDetailsPreview))
				.setImageDrawable(this.getResources().getDrawable(
						R.drawable.dummy));*/
		((TextView) rootView.findViewById(R.id.restaurantDetailsName))
				.setText(mItem.getName());
		((TextView) rootView.findViewById(R.id.restaurantDetailsText))
				.setText(mItem.getAddress());
		// }
		return rootView;
	}
	
	@Override
	public void onResume() {
		mBookButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), ReservationActivity.class));
			}
		});
		super.onResume();
	}
	
	@Override
	public void onPause() {
		if (mBookButton != null)
			mBookButton.setOnClickListener(null);
		super.onPause();
	}
}
