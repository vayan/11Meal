package com.vaya.elevenMeal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An activity representing a single Restaurant detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a
 * {@link RestaurantListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link RestaurantDetailFragment}.
 */
public class RestaurantDetailActivity extends FragmentActivity {
	private TextView mMenu;
	private TextView mCall;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putInt(
					RestaurantDetailFragment.ARG_ITEM_ID,
					getIntent().getIntExtra(
							RestaurantDetailFragment.ARG_ITEM_ID, 0));
			RestaurantDetailFragment fragment = new RestaurantDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.restaurant_detail_container, fragment).commit();
		}
	}
	
	@Override
	protected void onResume() {
		mMenu = (TextView) findViewById(R.id.restaurantDetailsMenu);
		mCall = (TextView) findViewById(R.id.restaurantDetailsCall);
		
		mMenu.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("Moi", "Click");
				return false;
			}
		});
		
		mCall.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				final AlertDialog.Builder dialog = new DiaAlertDialog.Builderlog(getApplicationContext());
//				dialog.setView(R.layout.dialog_restaurant_contact);
//				dialog.setTitle(R.string.contact);
//				dialog.findViewById(R.id.dialogDismiss).setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						dialog.dismiss();
//					}
//				});
//				dialog.show();
				return false;
			}
		});
		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		if (mMenu != null)
			mMenu.setOnTouchListener(null);
		if (mCall != null)
			mCall.setOnTouchListener(null);
		mMenu = null;
		mCall = null;
		super.onPause();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this,
					RestaurantListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
