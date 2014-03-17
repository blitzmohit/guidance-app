package org.geek90.guidance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabHelper.OnIabPurchaseFinishedListener;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
public class DonateActivity extends Activity {

	private static final String TAG = "org.geek90.guidance";
	IabHelper mHelper;

	private Button clickButton;
	private Button buyButton;
	private Button consumeButton;
	static final String ITEM_SKU = "android.test.purchased";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_app_billing);

		buyButton = (Button)findViewById(R.id.buyButton);
		clickButton = (Button)findViewById(R.id.clickButton);
		consumeButton=(Button)findViewById(R.id.consumeButton);
		clickButton.setEnabled(false);

		String base64EncodedPublicKey = 
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjAad6tuyGrFTjS28fogu7wKGP2bwHhQ9wHteZF3G8qpbXTOmlQ5jMmOiMY9eZql0nwXwkERA6vMj6c+RnCApLDj9ggd+VbXrYoAPM3v3Z+ysYdPQtaPnLFobueJQZp/EglFE3mhd9hbNJ68XGEGfI5Oh48lRkj00F4YMaE+ox0NPfuZHZWs2Kvt1kHLJXlwRalxBhmR4N7KJY/ZBRdnPV1BZxy8d8VajNuoZ/yltipUNtktxD6LJuBZvMHSSxLO5qkkcz+VEOdeByChBxesfYpkKLKcap5Li4DzrlJx57ckh+zsxx+IoldtUznU4QfDyzhXKSlwuzYVDIuDcy1nggwIDAQAB";

		mHelper = new IabHelper(this, base64EncodedPublicKey);

		mHelper.startSetup(new 
				IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) 
			{
				if (!result.isSuccess()) {
					Log.d(TAG, "In-app Billing setup failed: " + 
							result);
				} else {             
					Log.d(TAG, "In-app Billing is set up OK");
				}
			}
		});
	} 
	public void buyClick(View view) {
//		consumeItem();
		buyButton.setEnabled(false);
		OnIabPurchaseFinishedListener mPurchaseFinishedListener = null;
		mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,   
				mPurchaseFinishedListener, "mypurchasetoken");
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, 
			Intent data) 
	{
		Log.i(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
		if (!mHelper.handleActivityResult(requestCode, 
				resultCode, data)) {     
			super.onActivityResult(requestCode, resultCode, data);
		}
		 else {
		        Log.i(TAG, "onActivityResult handled by IABUtil.");
		    }
	}
	
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener 
	= new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, 
				Purchase purchase) 
		{
			if (result.isFailure()) {
				// Handle error
				Log.e(TAG,"Failed OnIABPurchaseFinishedListener");
				Log.e(TAG,result.getMessage());
				return;
			}      
			else if (purchase.getSku().equals(ITEM_SKU)) {
				consumeItem();
				buyButton.setEnabled(false);
			}

		}
	};
	public void consumeItem() {
		mHelper.queryInventoryAsync(mReceivedInventoryListener);
	}

	IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener 
	= new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			int flag=0;

			if (result.isFailure()) {
				// Handle failure
				Log.i(TAG,"QueryInventoryFinishedListener");
			} else {
				mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU), 
						mConsumeFinishedListener);
				flag=1;
			}
			 if (inventory.hasPurchase(ITEM_SKU) && flag==0) {

		            mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU), mConsumeFinishedListener);
		        }
		}
	};
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
			new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, 
				IabResult result) {

			if (result.isSuccess()) {		    	 
				clickButton.setEnabled(true);
			} else {
				// handle error
				Log.i(TAG, "Failed onconsumefinished");
			}
		}
	};
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null) mHelper.dispose();
		mHelper = null;
	}
}
