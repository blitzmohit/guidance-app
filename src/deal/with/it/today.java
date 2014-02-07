package deal.with.it;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class today extends Activity implements OnClickListener{
	private TextView tv1;
	private Button bt1;
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
//    tv1.append("Sometext");
    String readTwitterFeed = readTwitterFeed();
    //Log.i("deal.with.it",readTwitterFeed);
    try {
      JSONObject jsonObject = new JSONObject(readTwitterFeed);
      JSONArray posts =(JSONArray)jsonObject.getJSONArray("posts");
//      for (int j = 0; j < posts.length(); j++) {
      	Log.i("deal.with.it","Starting posts to string");
      	Calendar c = Calendar.getInstance();
      	System.out.println("Current time => " + c.getTime());

      	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      	String formattedDate = df.format(c.getTime());
   	       //Log.i("deal.with.it",posts.toString(2));
      	JSONObject x=posts.getJSONObject(0);
      	System.out.println(x);
      	String[] postdate=x.getString("date").split(" ");
        Log.i("deal.with.it",formattedDate+" "+postdate[0]);
      	Log.i("deal.with.it",x.getString("id"));
   	    if(postdate[0].equals(formattedDate))
   	    {
      	Log.i("deal.with.it","Todays post");
      	//tv1.append(x.getString("url"));
      	String url=x.getString("url");
      	Intent i = new Intent(Intent.ACTION_VIEW);
      	i.setData(Uri.parse(url));
      	startActivity(i);
   	    }
   	    else
   	    {
   	    	Log.i("deal.with.it","Should I show some old posts");
//   	    	tv1.append(x.getString("content"));
   	    	String url=x.getString("url");
   	      	Intent i = new Intent(Intent.ACTION_VIEW);
   	      	i.setData(Uri.parse(url));
   	      	startActivity(i);
   	    }
//      }
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