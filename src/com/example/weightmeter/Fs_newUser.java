package com.example.weightmeter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.weightmeter.model.User;
import com.example.weightmeter.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Fs_newUser extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	/**
	 * The flag which determines does application start from user creation or jump right to data viewing/input.
	 */
	private Boolean anyUser = false;

	private ArrayList<User> users;
	private User newUser;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fs_newuser);

		final View controlsView = findViewById(R.id.lytNewUserView);
		final View contentView = findViewById(R.id.lytMainActivity);
		
		users = null;
		newUser = new User();
		
		//Here we are gonna start our parsing of our basic app configuration from xml
				XmlPullParserFactory pullParserFactory;
				
				try {
					pullParserFactory = XmlPullParserFactory.newInstance();
					XmlPullParser parser = pullParserFactory.newPullParser();
					
					InputStream inputStream = getApplicationContext().getAssets().open("lclData.xml");
					parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
					parser.setInput(inputStream, null);
					
					parseXML(parser);
					
					if (!users.isEmpty())
						newUser = users.get(0);
					
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (anyUser)
				{					
					Intent goToMainActivity = new Intent(getApplicationContext(), Fs_mainActivity.class);
					startActivity(goToMainActivity);
				}
				else
				{

				}
				
		String[] npNumbers = new String[40];
		 
		for(int i=0; i<npNumbers.length; i++)
		   npNumbers[i] = Integer.toString(8+i);
				
		NumberPicker npAge = (NumberPicker) findViewById(R.id.npAge);
		npAge.setMaxValue(100);
		npAge.setMinValue(0);
		npAge.setValue(30);
		npAge.setWrapSelectorWheel(false);
		npAge.setDisplayedValues(npNumbers);
		
		Button newUsrOk = (Button)findViewById(R.id.btnNewUserOK);
		
		
		newUsrOk.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) {
				EditText userName = (EditText)findViewById(R.id.txtUserName);
				EditText gender = (EditText)findViewById(R.id.txtGender);
				NumberPicker age = (NumberPicker)findViewById(R.id.npAge);
				
				newUser.setName(userName.toString());
				newUser.setSex(gender.toString());
				newUser.setAge(age.getValue());
				
				Intent goToMainActivity = new Intent(getApplicationContext(), Fs_mainActivity.class);
				startActivity(goToMainActivity);
				
				goToMainActivity.putExtra("lastUser", true);
			}
		});

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

	}
	
	private void parseXML(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		int eventType = parser.getEventType();
		User currentUser = null;
		
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			String name = null;
			String sex = null;
			String age = null;
			
			switch (eventType)
			{
				case XmlPullParser.START_DOCUMENT:
					users = new ArrayList<User>();
				break;
				
				case XmlPullParser.START_TAG:
					name = parser.getAttributeValue(null, "name");
					sex = parser.getAttributeValue(null, "age");
					age = parser.getAttributeValue(null, "sex");
					
					if (name != "" && name != null)
					{
						currentUser = new User();
						
						currentUser.setName(name);
						currentUser.setSex(sex);
						currentUser.setAge(Integer.parseInt(age));
						
						anyUser = true;
					}
					else
					{
						//sanity check
						if (anyUser != true)
						anyUser = false;
					}
				break;
				
				case XmlPullParser.END_TAG:
					users.add(currentUser);
				break;
			}
			
			eventType = parser.next();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
