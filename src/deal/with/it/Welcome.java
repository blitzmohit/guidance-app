package deal.with.it;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Welcome extends Activity{

	String readTwitterFeed, postdate[]=new String[2],excerpts[]=new String[2],dates[]=new String[2];
	String[] slugs=new String[2],titles=new String[2],content=new String[2];
	TextView error_message;
	ProgressBar mProgress;
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.home);
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
	private class PrefetchData extends AsyncTask<Void, Boolean, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(Void... arg0) {
			publishProgress(true);
			readTwitterFeed = readTwitterFeed();
			try{
				JSONObject jsonObject = new JSONObject(readTwitterFeed);
				JSONArray posts =(JSONArray)jsonObject.getJSONArray("posts");
				for(int j=0;j<2;j++){
					JSONObject x=posts.getJSONObject(j);
					excerpts[j]=x.getString("excerpt").split("<div class=\"sharedaddy")[0].split("</div><br/><p>")[1];
					dates[j]=x.getString("date").split(" ")[0];
					slugs[j]=x.getString("slug");
					titles[j]=x.getString("title_plain");
					content[j]=x.getString("content");
					System.out.println(slugs[j]);
				}
			}
			catch(Exception e){
				readTwitterFeed=null;
				postdate[0]=null;
				e.printStackTrace();
				return false;
			}
			Log.i("deal.with.it",readTwitterFeed);
			return true;    

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
		private String readTwitterFeed() {
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			Log.i("deal.with.it","Read twitter feed");
			HttpGet httpGet = new HttpGet("http://lotusmeditationgroup.com/?cat=4&count=2&json=1");
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
			startActivity(lIntent);
			Log.i("deal.with.it","Done");
			finish();
			 }
			 else{
				 init();
			 }

		}
	}

}
