package com.vaya.elevenMeal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.vaya.elevenMeal.restaurant.IRestaurantObject;
import com.vaya.elevenMeal.restaurant.Meal;
import com.vaya.elevenMeal.restaurant.Reservation;
import com.vaya.elevenMeal.restaurant.Reservation.State;
import com.vaya.elevenMeal.restaurant.Restaurant;
import com.vaya.elevenMeal.restaurant.User;

/**
 * A list fragment representing a list of Reservations. This fragment also
 * supports tablet devices by allowing list items to be given an 'activated'
 * state upon selection. This helps indicate which item is currently being
 * viewed in a {@link ReservationDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ReservationListFragment extends ListFragment
	implements OnTaskCompleted {

	private List<Reservation> reservationList;
	private int userId;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(int id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(int id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ReservationListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getActivity().getSharedPreferences("11Meal",
				Context.MODE_PRIVATE);
		userId = settings.getInt("user_id", 0);
		new API(this).get(new Reservation(),
				//guest` LIKE "%<userId>%" OR `user
				"guest%60%20LIKE%20%22%25" + userId + "%25%22%20OR%20%60user",
				String.valueOf(userId));

	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(reservationList.get(position).getId());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
	public class ResView {
		public String mRestaurantName;
		public String mOwnerName;
		public State mState;

		public ResView(String restaurantName, String ownerName, State state) {
			mRestaurantName = restaurantName;
			mOwnerName = ownerName;
			mState = state;
		}
	}

	private class BuildResViewList extends AsyncTask<List<Reservation>, List<ResView>, List<ResView>> {

		@SuppressWarnings("unchecked")
		@Override
		protected List<ResView> doInBackground(List<Reservation>... list) {
			List<ResView> resViewList = new ArrayList<ResView>();
			API api = new API();
			api.setAsync(false);
			for (Reservation r: reservationList) {
				List<IRestaurantObject> resultList;
				User owner = new User();
				Restaurant restaurant = new Restaurant();

				owner.setLogin(getString(R.string.unk_user));
				restaurant.setName(getString(R.string.unk_restaurant));

				api.get(new User(), "id", String.valueOf(r.getOwnerId()));
				resultList = 
						(List<IRestaurantObject> ) api.getLastResult();
				if (resultList != null)
					owner = User.class.cast(resultList.get(0));

				api.get(new Restaurant(), "id", String.valueOf(r.getRestaurantId()));
				resultList = 
						(List<IRestaurantObject> ) api.getLastResult();
				if (resultList != null)
					restaurant = Restaurant.class.cast(resultList.get(0));

				resViewList.add(new ResView(restaurant.getName(), owner.getLogin(), r.getState()));
			}

			return resViewList;
		}

		@Override
		protected void onPostExecute(List<ResView> result) {
			super.onPostExecute(result);
			setListAdapter(new ReservationListAdapter(
					getActivity(), R.layout.adapter_reservation_list, result));
		}
		
	}
	
	private List<Reservation> filterUser(List<Reservation> resList) {
		Iterator<Reservation> it = resList.iterator();
		while (it.hasNext()) {
			Reservation r = it.next();
			boolean toDelete = true;
			if (r.getOwnerId() != userId) {
				Iterator<User> uIt = r.getGuests().iterator();
				while(uIt.hasNext()) {
					if (uIt.next().getId() == userId) {
						toDelete = false;
					}
				}
			}
			else
				continue ;
			if (toDelete)
				it.remove();
		}
		return resList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onTaskCompleted(Object res, java.lang.reflect.Type type) {
		if (res == null) {
			//TODO: Show error to user
			return ;
		}
		reservationList = (List<Reservation>) res;
		reservationList = filterUser(reservationList);
		new BuildResViewList().execute(reservationList);
	}
}
