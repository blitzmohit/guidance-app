package deal.with.it;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;

public class Welcome extends Activity{
	/*
	private Button okButton;
	private Button prevButton;
	private ImageView imgView;
	private Intent intent;
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		initializeLayout();
	}

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
	        finish();
	    }

}
