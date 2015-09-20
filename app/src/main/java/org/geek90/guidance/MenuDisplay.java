package org.geek90.guidance;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.widget.LikeView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;


public class MenuDisplay extends Activity implements OnClickListener{
    static String[] jsoncat={"status","count","count_total","pages","posts"};
	private String feed[]=new String[2],date[]=new String[2],recv_month_content[]=new String[2], recv_month_titles[]=new String[2];
	private int no,month_no;
    private CallbackManager callbackManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
		Bundle extras = getIntent().getExtras();
		feed=extras.getStringArray("feed");
		date=extras.getStringArray("date");
		recv_month_content=extras.getStringArray("month_feed");
		recv_month_titles=extras.getStringArray("month_titles");
		Log.i("deal.with.it","----");
		initLayout();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

	}

	private void initLayout(){
		setContentView(R.layout.today);
		LikeView likeView = (LikeView) findViewById(R.id.likeView);
		likeView.setLikeViewStyle(LikeView.Style.STANDARD);
		likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
		likeView.setObjectIdAndType("https://www.facebook.com/lotusmeditationgroup",
                LikeView.ObjectType.OPEN_GRAPH);
		Log.i("deal.with.it", "Done again");
//        likeView.
        Button bt1 = (Button) findViewById(R.id.Button01);
        TextView tv1 = (TextView) findViewById(R.id.TextView02);
		bt1.setOnClickListener(this);
        Button bt2 = (Button) findViewById(R.id.Button02);
        Button bt3 = (Button) findViewById(R.id.bt1);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sample_date = new SimpleDateFormat("dd MMMM",Locale.US);
		String new_date=sample_date.format(cal.getTime());
		String[] month_date=new_date.split(" ");
		if(new_date.charAt(0)=='0'){
			new_date=new_date.substring(1);
		}
		for(int i=0;i<2;i++){
			if(Pattern.compile(Pattern.quote(new_date), Pattern.CASE_INSENSITIVE).matcher(date[i]).find())
			{
				Log.i("deal.with.it","date was equal");
				Log.i("deal.with.it",date[i]);
				no=i;
				bt2.setVisibility(View.VISIBLE); // 0---
				tv1.setVisibility(View.GONE);
				Log.i("deal.with.it",feed[i]);
				break;
			}
			else
			{
				Log.i("deal.with.it","date was not equal");
				Log.i("deal.with.it",new_date);
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    llp.setMargins(50, 0, 0, 0); // llp.setMargins(left, top, right, bottom);
			    tv1.setLayoutParams(llp);
				tv1.setVisibility(View.VISIBLE);
				bt2.setVisibility(View.GONE);
			}
		}
		for(int i=0;i<2;i++){
			if(Pattern.compile(Pattern.quote(month_date[1]), Pattern.CASE_INSENSITIVE).matcher(recv_month_titles[i]).find())
			{
				Log.i("deal.with.it","month was equal");
				month_no=i;
				break;
			}
			else
			{
				Log.i("deal.with.it","month was not equal");
			}
		}
	}
	@Override
	public void onClick(View v){  
		switch(v.getId()){
		case R.id.Button01:
			System.out.println("months");
			Intent todaysII=new Intent();
			todaysII.setClass(MenuDisplay.this,GuidanceDisplay.class);
			todaysII.putExtra("excerpt", recv_month_content[month_no]);
			todaysII.putExtra("date", recv_month_titles[month_no]);
			todaysII.putExtra("title", "month");
			startActivity(todaysII);
			break;
		case R.id.Button02:
			System.out.println("todays");
			System.out.println("this is the day"+date[no]);
			System.out.println("this is the message");
			System.out.println(feed[no]);
			Intent todaysI=new Intent();
			todaysI.setClass(MenuDisplay.this,GuidanceDisplay.class);
			todaysI.putExtra("excerpt", feed[no]);
			todaysI.putExtra("date", date[no]);
			todaysI.putExtra("title", "daily");
			startActivity(todaysI);
			break;
		case R.id.bt1:
			//DO something
			System.out.println("request");
			Intent requestI=new Intent();
			requestI.setClass(MenuDisplay.this,RequestActivity.class);
			startActivity(requestI);
			break;
		}
	}
}