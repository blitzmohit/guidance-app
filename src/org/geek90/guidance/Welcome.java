package org.geek90.guidance;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class Welcome extends Activity{
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	final String SENDER_ID = "145463378681";
//	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	final String TAG = "org.geek90.guidance";
	GoogleCloudMessaging gcm;
	TextView mDisplay;
	Context context;
	String regid;
	String readTwitterFeed, postdate[]=new String[2],month_titles[]=new String[2],month_content[]=new String[2], monthFeed;
	String[] slugs=new String[2],titles=new String[2],content=new String[2];
	TextView error_message;
	ProgressBar mProgress;
	Boolean flag=false;
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.home);
		flag=checkPlayServices();
		if(flag){		
			context = getApplicationContext();
			gcm = GoogleCloudMessaging.getInstance(this);
		}
		error_message=(TextView)findViewById(R.id.textView1);
		mProgress = (ProgressBar) findViewById(R.id.progress_bar);
		new PrefetchData().execute();
	}
	private void init(){
		new AlertDialog.Builder(this)
		.setTitle("Error")
		.setMessage("Could not connect to website, kindly check internet connectivity and try again")
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				// continue with delete
				finish();
			}
		})
		.show();
	}
	private boolean checkPlayServices() {
		try{			
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//						PLAY_SERVICES_RESOLUTION_REQUEST).show();
				System.out.println("User can install Play services");
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
		}
		catch(Exception e){
			return false;
		}
	}
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	private class PrefetchData extends AsyncTask<Void, Boolean, Boolean> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(Void... arg0) {
			if(flag){
				 regid = getRegistrationId(context);
		            if (TextUtils.isEmpty(regid)) {
		                registerInBackground();
		            }
			}
			publishProgress(true);
			readTwitterFeed = readTwitterFeed("http://lotusmeditationgroup.com/?cat=4&count=2&json=1");
			try{
				JSONObject jsonObject = new JSONObject(readTwitterFeed);
				JSONArray posts =(JSONArray)jsonObject.getJSONArray("posts");
				for(int j=0;j<2;j++){
					JSONObject x=posts.getJSONObject(j);
					titles[j]=x.getString("title_plain");
					content[j]=x.getString("content");
					//					System.out.println(slugs[j]);
				}
			}
			catch(Exception e){
				readTwitterFeed=null;
				postdate[0]=null;
				e.printStackTrace();
				return false;
			}
			Log.i("deal.with.it",readTwitterFeed);
			monthFeed= readTwitterFeed("http://lotusmeditationgroup.com/?cat=14&count=2&json=1");
			try{
				JSONObject jsonObject = new JSONObject(monthFeed);
				JSONArray posts_y =(JSONArray)jsonObject.getJSONArray("posts");
				//				JSONObject y=posts_y.getJSONObject(0);
				for(int j=0;j<2 && j<posts_y.length();j++){
					JSONObject y=posts_y.getJSONObject(j);
					month_titles[j]=y.getString("title_plain");
					month_content[j]=y.getString("content");
				}
			}
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		private void registerInBackground() {
			String msg = "";
			try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;
                sendRegistrationIdToBackend(regid);
                storeRegistrationId(context, regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            Log.e(TAG,msg);
//            return msg;
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
		protected void onProgressUpdate(boolean val) {
			// setting progress percentage
			if(val){
				mProgress.setVisibility(View.VISIBLE);
			}
			else{
				mProgress.setVisibility(View.INVISIBLE);
			}
		}
		private String readTwitterFeed(String url) {
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			Log.i("deal.with.it","Read twitter feed");
			HttpGet httpGet = new HttpGet(url);
			try {
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;
					Log.i(today.class.toString(),"Here inside the status code");
					while ((line = reader.readLine()) != null) {
						builder.append(line);

					}
				} else {
					Log.e(today.class.toString(), "Failed to download file");
				}
			} catch (ClientProtocolException e) {
				Log.e(today.class.toString(), "ClientProtocolException");

			} catch (IOException e) {
				Log.e(today.class.toString(), "IOException");
			}
			Log.i(today.class.toString(),"Returning string");
			return builder.toString();
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			publishProgress(false);
			if (result) {
				Intent lIntent = new Intent();
				lIntent.setClass(Welcome.this, today.class);
				lIntent.putExtra("feed",content);
				lIntent.putExtra("date", titles);
				lIntent.putExtra("month_feed",month_content);
				lIntent.putExtra("month_titles", month_titles);
				//			System.out.println(month_content[0]);
				//			System.out.println(month_titles[0]);
				startActivity(lIntent);
				Log.i("deal.with.it","Done");
				finish();
			}
			else{
				init();
			}

		}
		private String getRegistrationId(Context context) {
		    final SharedPreferences prefs = getGCMPreferences(context);
		    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		    if (TextUtils.isEmpty(registrationId)) {
		        Log.i(TAG, "Registration not found.");
		        return "";
		    }
		    // Check if app was updated; if so, it must clear the registration ID
		    // since the existing regID is not guaranteed to work with the new
		    // app version.
		    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		    int currentVersion = getAppVersion(context);
		    if (registeredVersion != currentVersion) {
		        Log.i(TAG, "App version changed.");
		        return "";
		    }
		    return registrationId;
		}
		private SharedPreferences getGCMPreferences(Context context) {
		    // This sample app persists the registration ID in shared preferences, but
		    // how you store the regID in your app is up to you.
		    return getSharedPreferences(Welcome.class.getSimpleName(),
		            Context.MODE_PRIVATE);
		}
		
		private void sendRegistrationIdToBackend(String regid) {
			// this code will send registration id of a device to our own server.
			String url = "http://www.geek90.net/lmg_php/getdevice.php";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("regid", regid));
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				HttpResponse httpResponse = httpClient.execute(httpPost);
				String responseBody="abc";
				int responseCode = httpResponse.getStatusLine().getStatusCode();
				switch(responseCode) {
				case 200:
					HttpEntity entity = httpResponse.getEntity();
					if(entity != null) {
						responseBody = EntityUtils.toString(entity);
						String filename = "Lmgguidance";
						FileOutputStream outputStream;

						try {
							outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
							outputStream.write(responseBody.getBytes());
							outputStream.close();
						} catch (Exception e) {
							Log.e(TAG, "File write failed");
						}

					}
					break;
				}
				Log.i(TAG,responseBody);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}         

		}

	}

}
