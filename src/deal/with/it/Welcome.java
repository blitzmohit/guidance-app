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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class Welcome extends Activity{

	String readTwitterFeed, postdate[]=new String[2],excerpts[]=new String[4],dates[]=new String[4];
	String[] slugs=new String[4],titles=new String[4];
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.home);
		new PrefetchData().execute();
	}

	private class PrefetchData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			readTwitterFeed = readTwitterFeed();
			try{
				JSONObject jsonObject = new JSONObject(readTwitterFeed);
				JSONArray posts =(JSONArray)jsonObject.getJSONArray("posts");
				for(int j=0;j<4;j++){
					JSONObject x=posts.getJSONObject(j);
					excerpts[j]=x.getString("excerpt").split("<div class=\"sharedaddy")[0].split("</div><br/><p>")[1];
					dates[j]=x.getString("date").split(" ")[0];
					slugs[j]=x.getString("slug");
					titles[j]=x.getString("title_plain");
					System.out.println(slugs[j]);
				}
			}
			catch(Exception e){
				readTwitterFeed=null;
				postdate[0]=null;
				e.printStackTrace();
			}
			Log.i("deal.with.it",readTwitterFeed);
			return null;    

		}
		private String readTwitterFeed() {
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			Log.i("deal.with.it","Read twitter feed");
			HttpGet httpGet = new HttpGet("http://lotusmeditationgroup.com/?cat=4&json=1");
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
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i(today.class.toString(),"Returning string");
			return builder.toString();
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Intent lIntent = new Intent();
			lIntent.setClass(Welcome.this, today.class);
			lIntent.putExtra("feed",excerpts);
			lIntent.putExtra("date", titles);
			startActivity(lIntent);
			Log.i("deal.with.it","Done");
			finish();

		}
	}

}
