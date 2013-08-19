package com.example.devthis;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class UpgradeCreditActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upgrade_credit);
		
		RequestInvoker invoker = new RequestInvoker();
        invoker.setCommand( new UpgradeCreditCommand(this) );
        invoker.executeCommand();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upgrade_credit, menu);
		return true;
	}

}
