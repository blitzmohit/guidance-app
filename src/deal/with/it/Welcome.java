package deal.with.it;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Welcome extends Activity implements OnClickListener{
	
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
		okButton=(Button)findViewById(R.id.ok);
		prevButton=(Button)findViewById(R.id.prev);
		imgView=(ImageView)findViewById(R.id.welcome);
		okButton.setOnClickListener((OnClickListener) this);
		prevButton.setOnClickListener((OnClickListener) this);
	}
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.ok:
			intent = new Intent(this, today.class);
			startActivity(intent);
			break;
		case R.id.prev:
			
			break;
		}
	}
	
	

}
