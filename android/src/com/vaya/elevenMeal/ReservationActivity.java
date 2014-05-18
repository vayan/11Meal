package com.vaya.elevenMeal;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.internal.ch;
import com.vaya.elevenMeal.restaurant.Meal;
import com.vaya.elevenMeal.restaurant.Order;
import com.vaya.elevenMeal.restaurant.Reservation;
import com.vaya.elevenMeal.restaurant.Reservation.Payment;
import com.vaya.elevenMeal.restaurant.Reservation.State;
import com.vaya.elevenMeal.restaurant.Restaurant;
import com.vaya.elevenMeal.restaurant.User;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v13.app.FragmentPagerAdapter;
import android.net.NetworkInfo.DetailedState;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

// Trop de static, c'est caca
public class ReservationActivity extends Activity implements
		ActionBar.TabListener, OnTaskCompleted {
	
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private static int mIdRestaurant;
	
	private static ReservationMealListAdapter mReservationAdapter;
	
	private static int mIdUser;
	
	private Boolean mSwitch = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservation);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		mIdRestaurant = getIntent().getExtras().getInt(ARG_ITEM_ID);
		
		SharedPreferences preferences = getSharedPreferences("11Meal", MODE_PRIVATE);
		mIdUser = preferences.getInt("user_id", -1);
	}
	
	@Override
	protected void onDestroy() {
		//mSectionsPagerAdapter.destroyItem(mViewPager, 2, mSectionsPagerAdapter.getItem(2));
		//mSectionsPagerAdapter.destroyItem(mViewPager, 1, mSectionsPagerAdapter.getItem(1));
		//mSectionsPagerAdapter.destroyItem(mViewPager, 0, mSectionsPagerAdapter.getItem(0));
		//mSectionsPagerAdapter = null;
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reservation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_validate) {
			Reservation reservation = new Reservation();
			
			reservation.setOwnerId(mIdUser);
			reservation.setListGuest((ArrayList<User>) PeopleFragment.getUsers());
			reservation.setDate(new Date(HourDateFragment.getDateHour()));
			reservation.setPayMethod(HourDateFragment.getPaymentMethod());
			reservation.setState(State.OPENED);
			reservation.setRestaurantId(mIdRestaurant);
			
			new API(this).create(reservation);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return HourDateFragment.newInstance(position + 1);
			case 1:
				return PeopleFragment.newInstance(position + 1);
			case 2:
				return OrderFragment.newInstance(position + 1);
			default:
				return HourDateFragment.newInstance(position + 1);
			}
		}
		
		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.information).toUpperCase(l);
			case 1:
				return getString(R.string.friends).toUpperCase(l);
			case 2:
				return getString(R.string.order).toUpperCase(l);
			}
			return null;
		}
		
	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        super.destroyItem(container, position, object);

	        if (position <= getCount()) {
	            FragmentManager manager = ((Fragment) object).getFragmentManager();
	            FragmentTransaction trans = manager.beginTransaction();
	            trans.remove((Fragment) object);
	            trans.commit();
	        }
	    }
	}

	public static class HourDateFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		private static DatePicker mDatePicker;
		private static TimePicker mTimePicker;
		private static Spinner mSpinner;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static HourDateFragment newInstance(int sectionNumber) {
			HourDateFragment fragment = new HourDateFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public HourDateFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_reservation_hourdate, container, false);
			mDatePicker = ((DatePicker) rootView.findViewById(R.id.datePicker1));
			mTimePicker = ((TimePicker) rootView.findViewById(R.id.timePicker1));
			mSpinner = ((Spinner) rootView.findViewById(R.id.spinner1));

			mSpinner.setAdapter(new ArrayAdapter<Payment>(getActivity(),
					android.R.layout.simple_list_item_1, Payment.values()));
			return rootView;
		}

		public static Long getDateHour() {
			Calendar calendar = Calendar.getInstance(TimeZone
					.getTimeZone("UTC"));
			calendar.set(Calendar.YEAR, mDatePicker.getYear());
			calendar.set(Calendar.MONTH, mDatePicker.getMonth());
			calendar.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
			calendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
			calendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
			calendar.set(Calendar.SECOND, 0);

			return calendar.getTimeInMillis();
		}

		public static Payment getPaymentMethod() {
			return (Payment) mSpinner.getSelectedItem();
		}
	}

	public static class PeopleFragment extends Fragment implements
			OnTaskCompleted {
		private static List<UserCheckable> mUsers;

		private static ListView mPeople;
		private static ArrayAdapter<UserCheckable> mAdapter;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PeopleFragment newInstance(int sectionNumber) {
			PeopleFragment fragment = new PeopleFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			if (mAdapter == null)
				new API(fragment).getAll(new User());
			return fragment;
		}

		public PeopleFragment() {
			// new API().getAll(new User());
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_reservation_people, container, false);
			mPeople = (ListView) rootView
					.findViewById(R.id.newReservationListPeople);
			
			if (mAdapter != null)
				mPeople.setAdapter(mAdapter);
			mPeople.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					mUsers.get(arg2).checked = !mUsers.get(arg2).checked;
				}
			});
			
			return rootView;
		}

		@Override
		public void onTaskCompleted(Object res, java.lang.reflect.Type type) {
			List<User> users = (List<User>) res;
			mUsers = new ArrayList<UserCheckable>();
			for(User user:users)
				if (user.getId() != mIdUser)
				mUsers.add(new UserCheckable(user, false));
			if (mUsers != null)
			{
				mAdapter = new ArrayAdapter<UserCheckable>(getActivity(), android.R.layout.simple_list_item_checked, mUsers);
				mPeople.setAdapter(mAdapter);
			}
		}
		
		public static List<User> getUsers() {
			List<User> users = new ArrayList<User>();
			for (UserCheckable user:mUsers)
				if (user.checked)
					users.add(user.user);
			return users;
		}
		
		
		private class UserCheckable {
			public Boolean checked;
			public User user;
			
			public UserCheckable(User u, Boolean b)
			{
				user = u;
				checked = b;
			}
			
			@Override
			public String toString() {
				return user.getLogin();
			}
		}
	}

	public static class OrderFragment extends Fragment implements
			OnTaskCompleted {
		private static List<Meal> mOrders;
		
		private ListView mMeal;
		private static OrderFragment mFragment;
		private static TextView mTotal;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static OrderFragment newInstance(int sectionNumber) {
			OrderFragment fragment = new OrderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			mFragment = fragment;
			if (mReservationAdapter == null)
				new API(mFragment).get(new Meal(), "Restaurant",
						String.valueOf(mIdRestaurant));
			return fragment;
		}

		public OrderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_reservation_order, container, false);
			mTotal = (TextView) rootView.findViewById(R.id.newReservationOrderTotal);
			setTotalOrder();
				mMeal = (ListView) rootView
					.findViewById(R.id.newReservationListOrder);
			if (mReservationAdapter != null)
				 mMeal.setAdapter(mReservationAdapter);
			return rootView;
		}

		@Override
		public void onTaskCompleted(Object res, java.lang.reflect.Type type) {
			mOrders = (List<Meal>) res;
			if (mOrders != null)
			{
				mReservationAdapter = new ReservationMealListAdapter(getActivity(), R.layout.adapter_meal_list, mOrders);
				mMeal.setAdapter(mReservationAdapter);
			}
		}
		
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
		}
		
		public static List<Meal> getMealList()
		{

			List<Meal> choosen = new ArrayList<Meal>();
			List<Integer> quantities;
			
			quantities = Arrays.asList(mReservationAdapter.getQuantities());
			for (int i = 0; i < quantities.size(); i++){
				for (int j = 0; j < quantities.get(i); j++)
					choosen.add(mOrders.get(i));
			}
			return choosen;
		}
		
		public static void setTotalOrder()
		{
			float total = 0;
			Integer[] quantities;
			
			if (mReservationAdapter == null)
			{
				mTotal.setText(String.valueOf(0));
				return;
			}
			quantities = mReservationAdapter.getQuantities();
			for(int i = 0; i < quantities.length; i++)
				total += mOrders.get(i).getPrice() * quantities[i];
			mTotal.setText(String.valueOf(total));
		}
		
		private static float getTotalPrice()
		{
			float total = 0;
			Integer[] quantities;
			
			quantities = mReservationAdapter.getQuantities();
			for(int i = 0; i < quantities.length; i++)
				total += mOrders.get(i).getPrice() * quantities[i];
			return total;
		}
	}

	@Override
	public void onTaskCompleted(Object res, Type type) {
		if (!mSwitch)
		{
			Reservation reservation = (Reservation) res;
			Order order = new Order();
			order.setRestaurant(mIdRestaurant);
			order.setUser(mIdUser);
			order.setTotal_price((int) OrderFragment.getTotalPrice());
			order.setReservation(reservation.getId());
			order.setMealList(OrderFragment.getMealList());
			new API(this).create(order);
			mSwitch = !mSwitch;
		}
		else
			this.finish();
	}
}
