package com.example.weightmeter;

import com.example.weightmeter.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Fs_mainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fs_main);
		
		User actvUser = new User();
		actvUser = (User) getIntent().getSerializableExtra("activeUser");
		
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
