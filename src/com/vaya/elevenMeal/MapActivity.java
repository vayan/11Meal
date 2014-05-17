package com.vaya.elevenMeal;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class MapActivity extends FragmentActivity implements LocationListener, LocationSource{

	/**
     * Crash is if the Google Play services APK is not available or null.
     */
    private final static String TAG = "MAP";
    private GoogleMap mMap;
     
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;
    
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // in meter
    
   
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // in ms here 1 minute
  
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.activity_map);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
 
            if(locationManager != null)
            {
                boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if(networkIsEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d(TAG, "Network");
                }
                else if(gpsIsEnabled)
                {
                	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d(TAG, "GPS");

                }
                else
                {
                	Log.d(TAG, "Error network or gps");
                	 Toast.makeText(getApplicationContext(),
                             "Sorry! No localisation find....", Toast.LENGTH_SHORT)
                             .show();
                }
            }
            else
            {
                Log.d(TAG, "Error localisation manager");
            }
         
        setUpMapIfNeeded();
    }
 
    @Override
    public void onPause()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(this);
        }
         
        super.onPause();
    }
     
    @Override
    public void onResume()
    {
        super.onResume();
         
        setUpMapIfNeeded();
         
        if(locationManager != null)
        {
            mMap.setMyLocationEnabled(true);
        }
    }
     
 
    private void setUpMapIfNeeded() {
       
        if (mMap == null) 
        {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap(); //map from fragment
            Log.d(TAG, "create map succeed");

            if (mMap != null) 
            {
            	
                setUpMap();
            }
 
            //This is how you register the LocationSource
            mMap.setLocationSource(this);
        }
    }
     
    
    private void setUpMap() // add setup link location or camera move
    {
    	mMap.setMyLocationEnabled(true);
    	mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }
     
    @Override
    public void activate(OnLocationChangedListener listener) 
    {
        mListener = listener;
    }
     
    @Override
    public void deactivate() 
    {
        mListener = null;
    }
 
    @Override
    public void onLocationChanged(Location location) //call when user move
    {
        if( mListener != null )
        {
            mListener.onLocationChanged( location );
 
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        }
    }
 
    @Override
    public void onProviderDisabled(String provider) 
    {
        // TODO Auto-generated method stub
        Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    public void onProviderEnabled(String provider) 
    {
        // TODO Auto-generated method stub
        Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) 
    {
        // TODO Auto-generated method stub
        Toast.makeText(this, "status changed", Toast.LENGTH_SHORT).show();
    }
}
