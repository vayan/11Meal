package com.vaya.elevenMeal;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.vaya.elevenMeal.restaurant.Meal;
import com.vaya.elevenMeal.restaurant.Restaurant;

/**
 * A fragment representing a single Restaurant detail screen. This fragment is
 * either contained in a {@link RestaurantListActivity} in two-pane mode (on
 * tablets) or a {@link RestaurantDetailActivity} on handsets.
 */
public class RestaurantDetailFragment extends Fragment implements
		OnTaskCompleted {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Restaurant mItem;
	private List<Meal> mMeals = new ArrayList<Meal>();

	private Button mBookButton;
	private TextView mMenu;
	private TextView mCall;
	private TextView mPosition;
	private TextView mName;
	private TextView mText;

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
			new API(this).get(new Restaurant(), "Id", String.valueOf(i));
			new API(this).get(new Meal(), "Restaurant", String.valueOf(i));
			/*
			 * mItem = DummyContent.ITEM_MAP.get(getArguments()
			 * .getInt(ARG_ITEM_ID));
			 */
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
		mCall = (TextView) rootView.findViewById(R.id.restaurantDetailsContact);
		mPosition = (TextView) rootView
				.findViewById(R.id.restaurantDetailsPosition);
		mName = ((TextView) rootView.findViewById(R.id.restaurantDetailsName));
		mText = ((TextView) rootView.findViewById(R.id.restaurantDetailsText));
		// }
		return rootView;
	}

	@Override
	public void onResume() {
		if (mItem != null)
			setButtons();
		super.onResume();
	}

	@Override
	public void onPause() {
		// if (mBookButton != null)
		// mBookButton.setOnClickListener(null);
		// if (mMenu != null)
		// mMenu.setOnTouchListener(null);
		// if (mCall != null)
		// mCall.setOnTouchListener(null);
		super.onPause();
	}

	@Override
	public void onTaskCompleted(Object res, java.lang.reflect.Type type) {
		switch (type.toString()) {
		case "java.util.ArrayList<com.vaya.elevenMeal.restaurant.Restaurant>":
			mItem = ((List<Restaurant>) res).get(0);
			mName.setText(mItem.getName());
			mText.setText(mItem.getDescription());
			setButtons();
			break;
		case "java.util.ArrayList<com.vaya.elevenMeal.restaurant.Meal>":
			mMeals = (List<Meal>) res;
			break;
		}
	}

	private void setButtons() {
		mMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMeals != null) {
					final Dialog dialog = new Dialog(getActivity());
					dialog.setContentView(R.layout.dialog_restaurant_menu);
					dialog.setTitle(R.string.menu);
					((ListView) dialog.findViewById(R.id.dialogMenuList))
							.setAdapter(new MenuListAdapter(getActivity(),
									R.layout.adapter_menu_list, mMeals));
					dialog.findViewById(R.id.dialogMenuDismiss)
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									dialog.dismiss();
								}
							});
					dialog.show();
				}
			}
		});

		mCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mItem != null) {
					final Dialog dialog = new Dialog(getActivity());
					dialog.setContentView(R.layout.dialog_restaurant_contact);
					dialog.setTitle(R.string.contact);
					((TextView) dialog
							.findViewById(R.id.dialogRestaurantAddress))
							.setText(mItem.getAddress());
					((TextView) dialog.findViewById(R.id.dialogRestaurantTel))
							.setText(mItem.getPhone());
					dialog.findViewById(R.id.dialogDismiss).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									dialog.dismiss();
								}
							});
					dialog.show();
				}
			}
		});

		mPosition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						android.content.Intent.ACTION_VIEW,
						Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
				startActivity(intent);
			}
		});

		mBookButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ReservationActivity.class);
				intent.putExtra(ReservationActivity.ARG_ITEM_ID, mItem.getId());
				startActivity(intent);
			}
		});
	}
}
