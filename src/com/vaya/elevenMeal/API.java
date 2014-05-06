package com.vaya.elevenMeal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;

import com.vaya.elevenMeal.restaurant.User;

import android.os.AsyncTask;

public class API {
	public static enum RequestType {
		GET,
		PUT,
		POST,
		DELETE,
	}

	protected String mUrl;

	public Object create(Object object) {
		//TODO: use from.getClass()
		return null; //TODO: complete stub
	}
	public Object get(Object from, String column, int id) {
		return null; //TODO: complete stub
	}
	
	public Object update(Object object) {
		return null; //TODO: complete stub
	}

	public boolean delete(Object object) {
		String oClass  = object.getClass().toString();
		String oId     = object.getId();
		String request = "/" + oClass + "/" + oId;
		new RequestTask(RequestType.DELETE).execute(request);
		return false; //TODO: complete stub
	}

	public boolean createUser(User user, String password) {
		return false; //TODO: complete stub
	}

	public boolean authenticate(String login, String password) {
		return false; //TODO: complete stub
	}
	
	private class RequestTask extends AsyncTask<String, String, String>{
		private URL mURL;
		private HttpRequestBase mRequest;
		
		public RequestTask(RequestType type) {
			HttpRequestBase request[] = {
					new HttpGet(),
					new HttpPut(),
					new HttpPost(),
					new HttpDelete(),
			};
			mRequest = request[type.ordinal()];
			
		}

		@Override
		protected String doInBackground(String... path) {
			try {
				mURL = new URL(mUrl + path[0]);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				mRequest.setURI(mURL.toURI());
				response = httpclient.execute(mRequest);
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
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//Do anything with response..
		}
	}
}
