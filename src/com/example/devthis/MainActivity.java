package com.example.devthis;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	public static final String API_URL = "http://devthis.cvprojecten.net/";
	
	private TextView _mTextView;
	private NfcAdapter _mNfcAdapter;
	private String[][] _mNFCTechLists;
	private IntentFilter[] _mIntentFilters;
	private PendingIntent _mPendingIntent;
	
	private Button _btn1, _btn2, _btn3;

	private JSONObject _capturedJSONObject;
	private LinearLayout _containerBtns;
	
	private static String _idTag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_mTextView = (TextView)findViewById(R.id.tv);
        _mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
 
        if (_mNfcAdapter != null) {
            _mTextView.setText( getString(R.string.instruction_main) );
        } else {
            _mTextView.setText( getString(R.string.nfc_not_supported) );
        }
 
        // create an intent with tag data and deliver to this activity
        _mPendingIntent = PendingIntent.getActivity(this, 0,
            new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
 
        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            _mIntentFilters = new IntentFilter[] { ndefIntent };
        } catch (Exception e) {
            Log.e("LOG", e.toString());
        }
 
        _mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };
        
        _containerBtns = (LinearLayout)findViewById(R.id.container_buttons);
        _btn1 = (Button)findViewById(R.id.button1);
        _btn2 = (Button)findViewById(R.id.button2);
        _btn3 = (Button)findViewById(R.id.button3);
        _btn1.setOnClickListener( this );
        _btn2.setOnClickListener( this );
        _btn3.setOnClickListener( this );
        
        _capturedJSONObject = new JSONObject();
	}

    @Override
	protected void onPause() {
    	this._containerBtns.setVisibility(-1);
		_mTextView.setText( getString(R.string.instruction_main) );
		super.onPause();
	}

	@Override
    public void onNewIntent(Intent intent) {
        //String action = intent.getAction();
        //Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        // parse through all NDEF messages and their records and pick text type only // ACTION_NDEF_DISCOVERED
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord [] recs = ((NdefMessage)data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                            Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;
 
                            _capturedJSONObject = new JSONObject( new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1, textEncoding) );
                            _idTag = _capturedJSONObject.get("id").toString();
                            _showTagInfo();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("LOG", e.toString());
            }
        }
    }
	
    private void _showTagInfo() {
    	_mTextView.setText( "Dit is de tag van "+this._getJSONValue(this._capturedJSONObject, "name") );
    	this._containerBtns.setVisibility(1);
	}

	private Object _getJSONValue( JSONObject jsonObject, String name ) {
        try {
        	return jsonObject.get(name);
        } catch (JSONException e) {
        	Log.d("LOG", e.toString());
        	return null;
        }
    }
    
	@Override
	protected void onResume() {
		if (_mNfcAdapter != null)
            _mNfcAdapter.enableForegroundDispatch(this, _mPendingIntent, _mIntentFilters, _mNFCTechLists);
		
		super.onResume();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

	@Override
	public void onClick(View v) {
		this._containerBtns.setVisibility(-1);
		_mTextView.setText( getString(R.string.instruction_main) );
		
		switch ( v.getId() ) {
			case R.id.button1:
				this.startActivity( new Intent( this, CheckAccountActivity.class ) );
				break;
			case R.id.button2:
				this.startActivity( new Intent( this, UpgradeCreditActivity.class ) );
				break;
		}
	}
	
	public static String getIdTag() {
		return _idTag;
	}

}
