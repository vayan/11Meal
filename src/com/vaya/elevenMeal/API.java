package com.vaya.elevenMeal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vaya.elevenMeal.dummy.OnTaskCompleted;
import com.vaya.elevenMeal.restaurant.*;

import android.os.AsyncTask;
import android.util.Log;

public class API {

	// TODO: Put it in settings?
	protected String mUrl;
	protected Gson mGson;
	protected OnTaskCompleted mListener;

	public API() {
		init();
	}
	
	public API(OnTaskCompleted listener) {
		init();
		this.mListener = listener;
	}
	
	private void init() {
		mUrl = "http://galan.im:8181";
		mGson = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
		mListener = null;
	}

	public IRestaurantObject create(IRestaurantObject object) {
		String oClass  = object.getClass().getSimpleName();
		HttpPost request = new HttpPost(mUrl + "/" + oClass);
		request.setEntity(makeJSONObjectEntity(object));
		new RequestTask(object.getClass()).execute(request);
		return null;
	}

	public IRestaurantObject get(IRestaurantObject from, String column, String search) {
		String oClass  = from.getClass().getSimpleName();
		HttpGet request = new HttpGet(mUrl + "/" + oClass + "/" + column + "/" + search);
		new RequestTask(getType(oClass)).execute(request);
		return null; //TODO: complete stub
	}

	public IRestaurantObject getAll(IRestaurantObject from) {
		String oClass  = from.getClass().getSimpleName();
		HttpGet request = new HttpGet(mUrl + "/" + oClass);
		new RequestTask(getType(oClass)).execute(request);
		return null; //TODO: complete stub
	}

	public IRestaurantObject update(IRestaurantObject object) {
		String oClass  = object.getClass().getSimpleName();
		HttpPut request = new HttpPut("/" + oClass);
		request.setEntity(makeJSONObjectEntity(object));
		new RequestTask(getType(oClass)).execute(request);
		return null;
	}

	public boolean delete(IRestaurantObject object) {
		String oClass  = object.getClass().getSimpleName();
		String oId     = String.valueOf(object.getId());
		HttpDelete request = new HttpDelete(mUrl + "/" + oClass + "/" + oId);
		new RequestTask(getType(oClass)).execute(request);
		return false;
	}

	public boolean createUser(User user, String password) {
		return false; //TODO: complete stub
	}

	public boolean authenticate(String login, String password) {
		return false; //TODO: complete stub
	}

	private HttpEntity makeJSONObjectEntity(IRestaurantObject object) {
		try {
			String json = mGson.toJson(object);
			Log.d("API.makeJSONObjectEntity", json);
			return new StringEntity(json);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private Type getType(String objectName) {
		Type type = null;
		switch (objectName) {
		case "Meal":
			type = new TypeToken<ArrayList<Meal>>(){}.getType();
			break ;
		case "Order":
			type = new TypeToken<ArrayList<Order>>(){}.getType();
			break ;
		case "Reservation":
			type = new TypeToken<ArrayList<Reservation>>(){}.getType();
			break ;
		case "Restaurant":
			type = new TypeToken<ArrayList<Restaurant>>(){}.getType();
			break ;
		case "User":
			type = new TypeToken<ArrayList<User>>(){}.getType();
			break ;
		}
		return type;
	}

	private class RequestTask extends AsyncTask<HttpRequestBase, String, String>{
		Type mType;

		public RequestTask(Type type) {
			mType = type;
		}

		@Override
		protected String doInBackground(HttpRequestBase... requests) {
			HttpRequestBase request = requests[0];
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				request.addHeader("Accept", "application/json");
				request.addHeader("Content-type", "application/json");
				response = httpclient.execute(request);
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else{
					//Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				//TODO Handle problems..
			} catch (IOException e) {
				//TODO Handle problems..
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == null)
				return ;
			Log.d("API.onPostExecute", result);
			if (mListener != null)
				mListener.onTaskCompleted(mGson.fromJson(result, mType));
		}
	}
}
