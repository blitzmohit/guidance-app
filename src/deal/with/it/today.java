package deal.with.it;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

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
	private Button bt1,bt2,bt3;
	static String[] jsoncat={"status","count","count_total","pages","posts"};
	private String feed[]=new String[2],date[]=new String[2];
	private int no;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		feed=extras.getStringArray("feed");
		date=extras.getStringArray("date");
		Log.i("deal.with.it","----");
		//    Log.i("deal.with.it",feed);
		//    Log.i("deal.with.it",date);
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
				tv1.setVisibility(0);
			}
		}
	}
	@Override
	public void onClick(View v){  
		switch(v.getId()){
		case R.id.Button01:
			System.out.println("months");
			String url="http://www.google.com";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
			break;
		case R.id.Button02:
			//DO something
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