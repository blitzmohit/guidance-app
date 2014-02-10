package deal.with.it;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TodayDisplay extends Activity implements OnClickListener{
	private String excerpt,date,url;
	private TextView today;
	private Button read_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_display);
		Bundle extras = getIntent().getExtras();
		excerpt=extras.getString("excerpt");
		date=extras.getString("date");
		initLayout();
	}
	private void initLayout(){
		setContentView(R.layout.activity_today_display);
		today=(TextView)findViewById(R.id.textView1);
		read_button=(Button)findViewById(R.id.button1);
		String part[]=excerpt.split("<a href=\"");
		today.setText(part[0].replace("<br />", ""));
		url=part[1].split("\">Read more...")[0];
		System.out.println(url);
		read_button.setOnClickListener((OnClickListener) this);

	}
	@Override
	public void onClick(View v){ 
		System.out.println("button clicked");
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
}
