package org.geek90.guidance;

import org.geek90.guidance.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class TodayDisplay extends Activity{
	private String excerpt,date,html,title;
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
}