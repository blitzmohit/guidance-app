package org.geek90.guidance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;


public class TodayDisplay extends Activity implements OnClickListener{
	private String excerpt,date,html,title;
	private Button donate;
	private WebView today;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_display);
		Bundle extras = getIntent().getExtras();
		excerpt=extras.getString("excerpt");
		date=extras.getString("date");
		title=extras.getString("title");
		html="<b>"+date+"</b>"+excerpt; 
		System.out.println(date);
		initLayout();
	}
	private void initLayout(){
		setContentView(R.layout.activity_today_display);
		donate=(Button)findViewById(R.id.button1);
		donate.setOnClickListener((OnClickListener)this);
		if(title.equals("daily")){
			setTitle(R.string.go);
		}
		else{
			setTitle(R.string.prev);
		}
		today=(WebView)findViewById(R.id.textView1);
		today.loadDataWithBaseURL(null,html, "text/html", "utf-8", null);
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
	@Override
	public void onClick(View v) {
		Log.i("org.geek90.guidance", "Onclick");
		Intent donate=new Intent();
		donate.setClass(TodayDisplay.this,InAppBillingActivity.class);
		startActivity(donate);
	}
}
