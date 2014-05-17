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
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.vaya.elevenMeal.OnTaskCompleted;
import com.vaya.elevenMeal.restaurant.*;

import android.os.AsyncTask;
import android.util.Log;

public class API {
	private String mUrl;
	private Gson mGson;
	private boolean mAsync;
	private Object mLastResult;
	private OnTaskCompleted mListener;

	public API() {
		init();
	}

	public API(OnTaskCompleted listener) {
		init();
		this.mListener = listener;
	}

	private void init() {
		mUrl = "http://192.168.0.107:8181";
		mGson = new GsonBuilder()
		.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
		mAsync = true;
		mListener = null;
	}

	// Methods
	public void create(IRestaurantObject object) {
		String oClass  = object.getClass().getSimpleName();
		HttpPost request = new HttpPost(mUrl + "/" + oClass);
		request.setEntity(makeJSONObjectEntity(object));
		request(object.getClass(), request);
	}

	public void get(IRestaurantObject from, String column, String search) {
		String oClass  = from.getClass().getSimpleName();
		HttpGet request = new HttpGet(mUrl + "/" + oClass + "/" + column + "/" + search);
		request(getType(oClass), request);
	}

	public void getAll(IRestaurantObject from) {
		String oClass  = from.getClass().getSimpleName();
		HttpGet request = new HttpGet(mUrl + "/" + oClass);
		request(getType(oClass), request);
	}

	public void update(IRestaurantObject object) {
		String oClass  = object.getClass().getSimpleName();
		String oId     = String.valueOf(object.getId());
		HttpPut request = new HttpPut("/" + oClass + "/" + oId);
		request.setEntity(makeJSONObjectEntity(object));
		request(getType(oClass), request);
	}

	public void delete(IRestaurantObject object) {
		String oClass  = object.getClass().getSimpleName();
		String oId     = String.valueOf(object.getId());
		HttpDelete request = new HttpDelete(mUrl + "/" + oClass + "/" + oId);
		request(getType(oClass), request);
	}

	// Getters / setters
	public Object getLastResult() {
		return mLastResult;
	}

	public void setAsync(boolean async) {
		mAsync = async;
	}

	public void setUrl(String url) {
		mUrl = url;
	}
	// Private methods
	private void request(Type objectType, HttpRequestBase request) {
		if (mAsync) {
			new RequestTask(objectType).execute(request);
			return ;
		} 
		String result = makeHttpRequest(request);
		Log.d("API.request", result);
		mLastResult = mGson.fromJson(result, objectType);
		if (result != null && mListener != null)
			mListener.onTaskCompleted(mLastResult, objectType);
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

	private String makeHttpRequest(HttpRequestBase request) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try {
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			Log.d("API.makeHttpRequest", request.getRequestLine().toString() );
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
			return makeHttpRequest(requests[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == null)
				return ;
			try {
				mLastResult = mGson.fromJson(result, mType);
			}
			catch (JsonSyntaxException e) {
				Log.e("API.onPostExecute", e.getMessage());
				mLastResult = mType.getClass();
			}
			if (mListener != null)
				mListener.onTaskCompleted(mLastResult, mType);
		}
	}
}
