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
	/*
	private Button okButton;
	private Button prevButton;
	private ImageView imgView;
	private Intent intent;
	*/
	  String readTwitterFeed, postdate[]=new String[2],excerpts[]=new String[4],dates[]=new String[4];
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.home);
		new PrefetchData().execute();
		}
	
//		initializeLayout();
//	}
/*
	private void initializeLayout() {
		setContentView(R.layout.home);
//		okButton=(Button)findViewById(R.id.ok);
//		prevButton=(Button)findViewById(R.id.prev);
		imgView=(ImageView)findViewById(R.id.welcome);
//		okButton.setOnClickListener((OnClickListener) this);
//		prevButton.setOnClickListener((OnClickListener) this);
	}*/
//	@Override
	/*public void onClick(View v){
		switch(v.getId()){
		case R.id.ok:
			intent = new Intent(this, today.class);
			startActivity(intent);
			break;
		case R.id.prev:
			
			break;
		}
	}*/
	/*
	 private CountDownTimer lTimer;

	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.home); // Contains only an LinearLayout with backround as image drawable

	        lTimer = new CountDownTimer(4000, 1000) {
	            public void onFinish() {
	                closeScreen();
	            }
	            @Override
	            public void onTick(long millisUntilFinished) {
	                // TODO Auto-generated method stub
	            }
	        }.start();
	    }

	    private void closeScreen() {
	        Intent lIntent = new Intent();
	        lIntent.setClass(this, today.class);
	        startActivity(lIntent);
	        Log.i("deal.with.it","Done");
	        finish();
	    }
*/
	private class PrefetchData extends AsyncTask<Void, Void, Void> {
		 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls         
 
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
        			postdate=x.getString("date").split(" ");
//        			System.out.println(postdate[0]);
        			dates[j]=postdate[0];
        			System.out.println(dates[j]);
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
	        lIntent.putExtra("date", dates);
	        startActivity(lIntent);
	        Log.i("deal.with.it","Done");
	        finish();
            
        }
	}
	
}
