package org.geek90.guidance;

import android.accounts.Account;
import android.accounts.AccountManager;
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

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Welcome extends Activity{
	private static final String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
    private final String TAG = "org.geek90.guidance";
    private GoogleCloudMessaging gcm;
    private TextView mDisplay;
    private Context context;
    private String regid;
//    final private String[] postdate=new String[2];
    private final String[] month_titles=new String[2];
    private final String[] month_content=new String[2];
    private String[] slugs=new String[2];
    private final String[] titles=new String[2];
    private final String[] content=new String[2];
    private ProgressBar mProgress;
    private Boolean flag=false;
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.home);
		flag=checkPlayServices();
		if(flag){		
			context = getApplicationContext();
			gcm = GoogleCloudMessaging.getInstance(this);
		}
        TextView error_message = (TextView) findViewById(R.id.textView1);
		mProgress = (ProgressBar) findViewById(R.id.progress_bar);
		new PrefetchData().execute();
	}
	@Override
	protected void onResume() {
		super.onResume();

		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(context);
	}
	@Override
	protected void onPause() {
		super.onPause();

//		 Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(context);
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
		protected Boolean doInBackground(Void... arg0) {
			if(flag){
				regid = getRegistrationId(context);
				if (TextUtils.isEmpty(regid)) {
					registerInBackground();
				}
			}
			publishProgress(true);
            String readTwitterFeed = readFeed("http://lotusmeditationgroup.com/?cat=4&count=2&json=1");
			try{
				JSONObject jsonObject = new JSONObject(readTwitterFeed);
				JSONArray posts = jsonObject.getJSONArray("posts");
				for(int j=0;j<2;j++){
					JSONObject x=posts.getJSONObject(j);
					titles[j]=x.getString("title_plain");
					content[j]=x.getString("content");
					//					System.out.println(slugs[j]);
				}
			}
			catch(Exception e){
				readTwitterFeed =null;
//				postdate[0]=null;
				e.printStackTrace();
				return false;
			}
			Log.i("deal.with.it", readTwitterFeed);
            String monthFeed = readFeed("http://lotusmeditationgroup.com/?cat=14&count=2&json=1");
			try{
				JSONObject jsonObject = new JSONObject(monthFeed);
				JSONArray posts_y = jsonObject.getJSONArray("posts");
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
			String msg;
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(context);
				}
                String SENDER_ID = "145463378681";
                regid = gcm.register(SENDER_ID);
				msg = "Device registered, registration ID=" + regid;
				boolean sendSuccess=sendRegistrationIdToBackend(regid);
				if(sendSuccess){
					storeRegistrationId(context, regid);
				}
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
				// If there is an error, don't just keep trying to register.
				// Require the user to click a button again, or perform
				// exponential back-off.
			}
			Log.e(TAG,msg);
		}
		private void storeRegistrationId(Context context, String regId) {
			final SharedPreferences prefs = getGCMPreferences();
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
		private String readFeed(String url) {
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			Log.i("deal.with.it","Read feed");
			HttpGet httpGet = new HttpGet(url);
            InputStream content=null;
			try {
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					content = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(content));
					String line;
					Log.i(MenuDisplay.class.toString(),"Here inside the status code");
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
				} else {
					Log.e(MenuDisplay.class.toString(), "Failed to download file");
				}
			} catch (ClientProtocolException e) {
				Log.e(MenuDisplay.class.toString(), "ClientProtocolException");

			} catch (IOException e) {
				Log.e(MenuDisplay.class.toString(), "IOException");
			}finally {
                try {
                    content.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.i(MenuDisplay.class.toString(),"Returning string");
			return builder.toString();
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
//			publishProgress(false);
			if (result) {
				Intent lIntent = new Intent();
				lIntent.setClass(Welcome.this, MenuDisplay.class);
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
			final SharedPreferences prefs = getGCMPreferences();
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
		private SharedPreferences getGCMPreferences() {
			return getSharedPreferences(Welcome.class.getSimpleName(),
					Context.MODE_PRIVATE);
		}
		private boolean sendRegistrationIdToBackend(String regid) {
			Log.i(TAG,regid);
			// this code will send registration id of a device to our own server.
			String url = "http://www.geek90.net/lmg_php/getdevice.php";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("regid", regid));
			params.add(new BasicNameValuePair("email", getGoogAccount()));
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				return false;
			}
			try {
				HttpResponse httpResponse = httpClient.execute(httpPost);
				String responseBody="news";
				int responseCode = httpResponse.getStatusLine().getStatusCode();
				switch(responseCode) {
				case 200:
					HttpEntity entity = httpResponse.getEntity();
					if(entity != null) {
						responseBody = EntityUtils.toString(entity);
						if(responseBody.contains("successs")){
							Log.i(TAG,"true");
							return true;
						}
						else{
							Log.i(TAG,responseBody);
							return false;
						}
					}
					break;
				}
				Log.i(TAG,responseBody);
				return false;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}         
		}
		private String getGoogAccount(){
			AccountManager accountManager = AccountManager.get(getBaseContext()); 
			Account[] accounts = accountManager.getAccountsByType("com.google");
			Account account;
			if (accounts.length > 0) {
				account = accounts[0];      
			} else {
				return "null";
			}
			return account.name;
		}

	}

}
