package com.vaya.elevenMeal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyActivity extends Activity 
{
	public static Context mContext;
	Button button;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		addListenerOnButton();
    }
    
    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
    	MyActivity.mContext = mContext;
    }
    
    public void addListenerOnButton() {
    	 
		button = (Button) findViewById(R.id.button_nfc);
 
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
				Log.d("Main","Click bouton");
				setContentView(R.layout.nfctag);
			}
		});	 
	}
		 
}
