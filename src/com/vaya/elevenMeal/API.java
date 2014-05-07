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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaya.elevenMeal.restaurant.IRestaurantObject;
import com.vaya.elevenMeal.restaurant.User;

import android.os.AsyncTask;

public class API {

	// TODO: Put it in settings?
	protected String mUrl = "http://192.168.0.101:8181";

	public IRestaurantObject create(IRestaurantObject object) {
		String oClass  = object.getClass().getSimpleName();
		HttpPost request = new HttpPost("/" + oClass);
		request.setEntity(makeJSONObjectEntity(object));
		Type type = TypeToken.get(object.getClass().getDeclaringClass()).getType();
		new RequestTask(type).execute(request);
		return null;
	}

	public IRestaurantObject get(IRestaurantObject from, String column, int id) {
		String oClass  = from.getClass().toString();
		String sId     = String.valueOf(id);
		HttpGet request = new HttpGet("/" + oClass + "/" + column + "/" + sId);
		//Type type = TypeToken.get(ArrayList<from.getClass()>).getType();
		//new RequestTask(type).execute(request);
		return null; //TODO: complete stub
	}

	public IRestaurantObject update(IRestaurantObject object) {
		String oClass  = object.getClass().toString();
		HttpPut request = new HttpPut("/" + oClass);
		request.setEntity(makeJSONObjectEntity(object));
		//new RequestTask().execute(request);
		return null;
	}

	public boolean delete(IRestaurantObject object) {
		String oClass  = object.getClass().toString();
		String oId     = String.valueOf(object.getId());
		HttpDelete request = new HttpDelete(mUrl + "/" + oClass + "/" + oId);
		Type type = TypeToken.get(object.getClass()).getType();
		new RequestTask(type).execute(request);
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
			return new StringEntity("{TODO}");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private class RequestTask extends AsyncTask<HttpRequestBase, String, String>{
		Type mType;

		public RequestTask(Type type) {
			// TODO Auto-generated constructor stub
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
			new Gson().fromJson(result, mType);
		}
	}
}