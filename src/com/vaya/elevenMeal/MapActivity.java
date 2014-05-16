package com.vaya.elevenMeal;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class MapActivity extends FragmentActivity{

	// Google Map
		private GoogleMap googleMap;
		private final static String TAG = "MAP";

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_map);
			Log.d(TAG, "creat map");

			try {
				// Loading map
				initilizeMap();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/**
		 * function to load map. If map is not created it will create it for you
		 * */
		private void initilizeMap() {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();

				// check if map is created successfully or not
				if (googleMap == null) {
					Toast.makeText(getApplicationContext(),
							"Sorry! unable to create maps", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}

		@Override
		protected void onResume() {
			super.onResume();
			initilizeMap();
		}

	}
