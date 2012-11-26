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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class today extends Activity implements OnClickListener{
	private TextView tv1;
	private Button bt1;
/*	private static final String TAG_CONTACTS = "contacts";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_GENDER = "gender";
	private static final String TAG_PHONE = "phone";
	private static final String TAG_PHONE_MOBILE = "mobile";
	private static final String TAG_PHONE_HOME = "home";
	private static final String TAG_PHONE_OFFICE = "office";*/
	static String[] jsoncat={"status","count","count_total","pages","posts"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initLayout();
	}
	private void initLayout(){
		setContentView(R.layout.today);
		bt1 =(Button)findViewById(R.id.bt1);
		tv1=(TextView)findViewById(R.id.tv1);
	    bt1.setOnClickListener((OnClickListener) this);	
	}
	@Override
	public void onClick(View v){  
    Log.i("webtry","layout now trying");
    tv1.setVisibility(0);
    tv1.append("Sometext");
    String readTwitterFeed = readTwitterFeed();
    Log.i("deal.with.it",readTwitterFeed);
    try {
      JSONObject jsonObject = new JSONObject(readTwitterFeed);
      JSONArray posts =(JSONArray)jsonObject.getJSONArray("posts");
      for (int i = 0; i < posts.length(); i++) {
   // 	      final View row = createRow(posts.getJSONObject(i));
      }
      //Log.i("deal.with.it",posts.getString());
     /*Log.i("deal.with.it","Number of entries "+jsonObject.length());
      for (int i = 0; i < jsonObject.length(); i++) {
        Log.i("deal.with.it",jsonObject.getString(jsoncat[i]));
      }*/
    } catch (Exception e) {
      e.printStackTrace();
    }
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
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	

	
	
	
	
	/*try {
    	Log.i("webtry","URL enter try block");
    	  URL url = new URL("http://www.google.com");
    	  Log.i("webtry","URL created");
    	  HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	  readStream(con.getInputStream());
    	  Log.i("webtry","URL try code exit");	  
    	  }
    catch (Exception e) {
    	e.printStackTrace();
    	Log.i("webtry","URL catch code exit");
    	}
	}
    private void readStream(InputStream in) {
    	Log.i("webtry","URL now in readstream");
    	  BufferedReader reader = null;
    	  try {
    	    reader = new BufferedReader(new InputStreamReader(in));
    	    String line = "";
    	    while ((line = reader.readLine()) != null) {
    	      //System.out.println(line);
    	    	Log.i("webtry", line);
    	    }
    	  } catch (IOException e) {
    	    e.printStackTrace();
    	  } finally {
    	    if (reader != null) {
    	      try {
    	    	  Log.i("webtry","URL null reader check");
    	        reader.close();
    	      } catch (IOException e) {
    	    	  Log.i("webtry","URL catch null reader");
    	        e.printStackTrace();
    	        }
    	    }
    	  }
    	}*/
    /*public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) 
          getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }*/
    





	
	
	
	
	
	
	
	
	
	
	
    //tv1=(TextView)findViewById(R.id.tv1);
    