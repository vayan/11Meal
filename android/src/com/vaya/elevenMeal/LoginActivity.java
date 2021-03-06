package com.vaya.elevenMeal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.*;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.vaya.elevenMeal.API;
import com.vaya.elevenMeal.restaurant.User;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity implements OnTaskCompleted{ /*  GooglePlayServicesClient.ConnectionCallbacks,
														GooglePlayServicesClient.OnConnectionFailedListener{*/

	// for GCM
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	String SENDER_ID = "992584978430";
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	String regid;
	Intent intentRestaurant;

	Location mCurrentLocation;
	LocationClient mLocationClient;
	Location myLocation;
	double latitude;
	double longitude;

	private LocationRequest mLocationRequest;

	private OnSharedPreferenceChangeListener listener = null;

	static final String TAG = "Mainacti";

	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
		"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	// private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private Button mRegisterbut;
	private Button mLoginbut;
	Context context;

	private EditText mPassRepeatView;
	private EditText mUsernameView;
	private EditText mFirstnameView;
	private EditText mLastnameView;
	private EditText mPhoneView;

	private Boolean mSwitch = false;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mLoginbut.setEnabled(true);
			mRegisterbut.setEnabled(true);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_login);

		/*mLocationRequest = LocationRequest.create();
		 mLocationRequest.setInterval(1000*60);

	        // Use high accuracy
	        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	        // Set the interval ceiling to one minute
	        mLocationRequest.setFastestInterval(1000);*/

		context = getApplicationContext();

		UpdatePref();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs,
					String key) {
				UpdatePref();
			}
		};
		prefs.registerOnSharedPreferenceChangeListener(listener);
		prefs = getSharedPreferences("com.vaya.elevenMeal.location.SHARED_PREFERENCES", Context.MODE_PRIVATE);
		// mLocationClient = new LocationClient(this, this, this);
		/*
		 * if (savedInstanceState == null) {
		 * getFragmentManager().beginTransaction() .add(R.id.container, new
		 * PlaceholderFragment()).commit(); }
		 */

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);


		mLoginbut = (Button) findViewById(R.id.sign_in_button);
		mRegisterbut = (Button) findViewById(R.id.register_button);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
		.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});

		findViewById(R.id.register_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						register();
					}
				});



		mLoginbut.setEnabled(false);
		mRegisterbut.setEnabled(false);

		mPassRepeatView = (EditText) findViewById(R.id.passwordConfirm);
		mUsernameView = (EditText) findViewById(R.id.login);
		mFirstnameView = (EditText) findViewById(R.id.firstName);
		mLastnameView = (EditText) findViewById(R.id.lastName);
		mPhoneView = (EditText) findViewById(R.id.phoneNumber);

		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);

			if (regid.isEmpty()) {
				new registerInBackground().execute();
			} else {
				mLoginbut.setEnabled(true);
				mRegisterbut.setEnabled(true);
			}
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}

		// If the user is already logged in
		intentRestaurant = new Intent(this, RestaurantListActivity.class);
		SharedPreferences settings = getSharedPreferences("11Meal",
				MODE_PRIVATE);
		int id = settings.getInt("user_id", -1);
		Log.d("LOGIN", String.valueOf(id));
		if (id != -1) {
			startActivity(intentRestaurant);
			finish();
		}
	}

	public Boolean register() {
		GPSTracker gps = new GPSTracker(this);

		if(gps.canGetLocation()){
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		}
		/*Location mCurrentLocation;
	    mCurrentLocation = mLocationClient.getLastLocation();*/
		if (mSwitch) {
			if (!(mPasswordView.getText().toString().equals(mPassRepeatView
					.getText().toString()))) {
				mPasswordView
				.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
				return false;
			}
			User user = new User();
			user.setEmail(mEmailView.getText().toString());
			user.setPassword(mPasswordView.getText().toString());
			user.setLogin(mPasswordView.getText().toString());
			user.setFirstName(mFirstnameView.getText().toString());
			user.setLastName(mLastnameView.getText().toString());
			user.setLogin(mUsernameView.getText().toString());
			user.setPhone(mPhoneView.getText().toString());
			user.setGcmId(getRegistrationId(this));
			user.setLocation(
					String.valueOf(latitude) + "," + String.valueOf(longitude));
			new API(this).create(user);
			showProgress(true);
			return true;
		}
		mSwitch = !mSwitch;
		mPassRepeatView.setVisibility(android.view.View.VISIBLE);
		mUsernameView.setVisibility(android.view.View.VISIBLE);
		mFirstnameView.setVisibility(android.view.View.VISIBLE);
		mLastnameView.setVisibility(android.view.View.VISIBLE);
		mPhoneView.setVisibility(android.view.View.VISIBLE);
		return false;
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		return registrationId;
	}

	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(LoginActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i("GPLAY_SERVICE", "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private void sendRegistrationIdToBackend() {
		Message msg = new Message();
		msg.obj = "done";
		mHandler.sendMessage(msg);
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private class registerInBackground extends AsyncTask<Void, Void, String> {
		protected String doInBackground(Void... params) {
			String msg = "";
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(context);
				}
				regid = gcm.register(SENDER_ID);
				msg = "Device registered, registration ID=" + regid;

				// You should send the registration ID to your server over HTTP,
				// so it can use GCM/HTTP or CCS to send messages to your app.
				// The request to your server should be authenticated if your
				// app
				// is using accounts.
				sendRegistrationIdToBackend();

				// For this demo: we don't need to send it because the device
				// will send upstream messages to a server that echo back the
				// message using the 'from' address in the message.

				// Persist the regID - no need to register again.
				storeRegistrationId(context, regid);
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
				new registerInBackground().execute();
			}
			return msg;
		}

		protected void onPostExecute(String msg) {
			Log.i("ASYNC", msg);
		}
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		intentRestaurant = new Intent(this, RestaurantListActivity.class);
		// startActivity(intent);
		// finish();

		/*
		 * if (mAuthTask != null) { return; }
		 */

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			new API(this).get(new User(), "email", mEmail);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginStatusView.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginFormView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	/*
	 * public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
	 *
	 * @Override protected Boolean doInBackground(Void... params) { // TODO:
	 * attempt authentication against a network service.
	 *
	 * try { // Simulate network access. Thread.sleep(2000); } catch
	 * (InterruptedException e) { return false; }
	 *
	 * for (String credential : DUMMY_CREDENTIALS) { String[] pieces =
	 * credential.split(":"); if (pieces[0].equals(mEmail)) { // Account exists,
	 * return true if the password matches. return pieces[1].equals(mPassword);
	 * } }
	 *
	 * // TODO: register the new account here. return true; }
	 *
	 * @Override protected void onPostExecute(final Boolean success) { mAuthTask
	 * = null; showProgress(false);
	 *
	 * if (success) { startActivity(intentRestaurant); finish(); } else {
	 * mPasswordView .setError(getString(R.string.error_incorrect_password));
	 * mPasswordView.requestFocus(); } }
	 *
	 * @Override protected void onCancelled() { mAuthTask = null;
	 * showProgress(false); } }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.settings) {
			showUserSettings();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showUserSettings() {
		startActivity(new Intent(LoginActivity.this, UserSettingsActivity.class));
		Log.d(TAG, "Open settings");
	}

	public void UpdatePref() {
		// get latest settings from the xml config file d
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
	}

	@Override
	public void onTaskCompleted(Object res, java.lang.reflect.Type type) {
		showProgress(false);
		intentRestaurant = new Intent(this, RestaurantListActivity.class);

		if ((type.toString())
				.equals("class com.vaya.elevenMeal.restaurant.User")) {
			User user = (User) res;
			startActivity(intentRestaurant);
			SharedPreferences settings = getSharedPreferences("11Meal",
					MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("user_id", user.getId());
			finish();
			return;
		}

		List<User> user = (List<User>) res;
		if (user.size() == 0)
		{
			mEmailView.setError(getString(R.string.error_invalid_email));
			mEmailView.requestFocus();
			return;
		}
		if (mEmailView.getText().toString()
				.equalsIgnoreCase(user.get(0).getEmail())
				&& mPasswordView.getText().toString()
				.equals((user.get(0)).getPassword())) {
			Log.d("LOGIN", mEmail + "||" + mPassword + ">>"
					+ user.get(0).getEmail() + "||" + user.get(0).getPassword());
			startActivity(intentRestaurant);

			// We need an Editor object to make preference changes.
			// All objects are from android.context.Context
			SharedPreferences settings = getSharedPreferences("11Meal",
					MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("user_id", user.get(0).getId());
			// Commit the edits!
			editor.commit();

			finish();
		} else {
			mPasswordView
			.setError(getString(R.string.error_incorrect_password));
			mPasswordView.requestFocus();
		}

	}

	/*@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		mLocationClient.connect();

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		mLocationClient.disconnect();
	}*/

}
