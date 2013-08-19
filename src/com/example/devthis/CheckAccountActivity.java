package com.example.devthis;

import org.json.JSONArray;

import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.TextView;

public class CheckAccountActivity extends Activity {

	private IntentFilter[] _mIntentFilters;
	private PendingIntent _mPendingIntent;
	private NfcAdapter _mNfcAdapter;
	private String[][] _mNFCTechLists;
	private TextView _creditTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_account);
		
		_mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
 
        // create an intent with tag data and deliver to this activity
        _mPendingIntent = PendingIntent.getActivity(this, 0,
            new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
 
        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        
        try {
            ndefIntent.addDataType("*/*");
            _mIntentFilters = new IntentFilter[] { ndefIntent };
        } catch (Exception e) {
            Log.e("LOG", e.toString());
        }
        
        _creditTV = (TextView)findViewById(R.id.account_credit);
        
        _mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };
        
        
        RequestInvoker invoker = new RequestInvoker();
        invoker.setCommand( new SetCreditsCommand(this) );
        invoker.executeCommand();
	}
	
	public void setCredits(String credits) {
		_creditTV.setText( credits );
	}

	@Override
	protected void onResume() {
		if (_mNfcAdapter != null)
            _mNfcAdapter.enableForegroundDispatch(this, _mPendingIntent, _mIntentFilters, _mNFCTechLists);
		
		super.onResume();
	}

	@Override
	protected void onPause() {
		_creditTV.setText( getString(R.string.credit_placeholder) );
		super.onPause();
	}

	@Override
    public void onNewIntent(Intent intent) {
//		startActivity( new Intent( this, MainActivity.class ) );
    }

}
