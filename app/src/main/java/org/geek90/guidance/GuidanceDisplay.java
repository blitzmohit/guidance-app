package org.geek90.guidance;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.facebook.FacebookSdk;

public class GuidanceDisplay extends Activity{
    private String html;
    private String title;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.guidancedisplay);
        Bundle extras = getIntent().getExtras();
        String excerpt = extras.getString("excerpt");
        String date = extras.getString("date");
		title=extras.getString("title");
		html="<b>"+ date +"</b>"+ excerpt;
		System.out.println(date);
		initLayout();
	}
	private void initLayout(){
		if(title.equals("daily")){
			setTitle(R.string.go);
		}
		else{
			setTitle(R.string.prev);
		}
        WebView today = (WebView) findViewById(R.id.textView1);
        WebSettings webSettings = today.getSettings();
        webSettings.setTextZoom(150);
        today.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) { 
	        switch (item.getItemId()) {
	        case android.R.id.home: 
	            onBackPressed();
	            return true;
	        }

	    return super.onOptionsItemSelected(item);
	}
}
