package deal.with.it;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class RequestActivity extends Activity implements OnClickListener {
	private Button button1;
	private EditText name,msg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
		
		initLayout();
	}
	private void initLayout(){
		button1=(Button)findViewById(R.id.button1);
		button1.setOnClickListener((OnClickListener) this);
		name=(EditText)findViewById(R.id.editText1);
		msg=(EditText)findViewById(R.id.editText2);
		
	}
	@Override
	public void onClick(View v) {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
	            "mailto","lotusmeditationgroup@gmail.com", null));
	emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Request for Personalized Guidance");
	emailIntent.putExtra(Intent.EXTRA_TEXT, "Please send me the details on personalized guidance service."+"\n"+msg.getText()+"\n"+name.getText());
	startActivity(Intent.createChooser(emailIntent, "Send email..."));
	}

}
