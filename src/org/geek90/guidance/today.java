package org.geek90.guidance;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import org.geek90.guidance.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class today extends Activity implements OnClickListener{
	private TextView tv1;
	private Button bt1,bt2,bt3;
	static String[] jsoncat={"status","count","count_total","pages","posts"};
	private String feed[]=new String[2],date[]=new String[2],recv_month_content[]=new String[2], recv_month_titles[]=new String[2];
	private int no,month_no;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		feed=extras.getStringArray("feed");
		date=extras.getStringArray("date");
		recv_month_content=extras.getStringArray("month_feed");
		recv_month_titles=extras.getStringArray("month_titles");
		Log.i("deal.with.it","----");
		initLayout();
	}

	private void initLayout(){
		setContentView(R.layout.today);
		Log.i("deal.with.it","Done again");
		bt1 =(Button)findViewById(R.id.Button01);
		tv1=(TextView)findViewById(R.id.TextView02);
		bt1.setOnClickListener((OnClickListener) this);	
		bt2=(Button)findViewById(R.id.Button02);
		bt3=(Button)findViewById(R.id.bt1);
		bt1.setOnClickListener((OnClickListener) this);	
		bt2.setOnClickListener((OnClickListener) this);	
		bt3.setOnClickListener((OnClickListener) this);	
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
				bt2.setVisibility(0);
				tv1.setVisibility(4);
				Log.i("deal.with.it",feed[i]);
				break;
			}
			else
			{
				Log.i("deal.with.it","date was not equal");
				Log.i("deal.with.it",new_date);
				tv1.setVisibility(0);
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
			todaysII.setClass(today.this,TodayDisplay.class);
			todaysII.putExtra("excerpt", recv_month_content[month_no]);
			todaysII.putExtra("date", recv_month_titles[month_no]);
			startActivity(todaysII);
			break;
		case R.id.Button02:
			System.out.println("todays");
			System.out.println("this is the day"+date[no]);
			System.out.println("this is the message");
			System.out.println(feed[no]);
			Intent todaysI=new Intent();
			todaysI.setClass(today.this,TodayDisplay.class);
			todaysI.putExtra("excerpt", feed[no]);
			todaysI.putExtra("date", date[no]);
			startActivity(todaysI);
			break;
		case R.id.bt1:
			//DO something
			System.out.println("request");
			Intent requestI=new Intent();
			requestI.setClass(today.this,RequestActivity.class);
			startActivity(requestI);
			break;
		}
	}
}