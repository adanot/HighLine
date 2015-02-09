package com.austinipm.highline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements SensorEventListener, View.OnClickListener{
	
	public static final String PREFS_NAME = "MyPrefsFile";

	Button ns_button, sn_button, lost_button, exit_button, settings_button;
	ToggleButton harbor_button;
	MediaPlayer northPlayer, southPlayer, eastPlayer, westPlayer;
		

    float degree, degree_scaled;

	// define the display assembly compass picture
	private ImageView image;

	// record the compass picture angle turned
	private float currentDegree = 0f;

	// device sensor manager
	private SensorManager mSensorManager;

	TextView tvHeading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		   boolean dialogShown = settings.getBoolean("dialogShown", false);
		   


		   if (!dialogShown) {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
			    alert.setTitle("Calibration");
			    alert.setMessage("In order to fully enjoy your HighLine experience you "
			    		+ "must first calibrate your compass.\n"
			    		+ "This can be done by holding your device flat and moving "
			    		+ "it in a figure eight pattern a few times. \n"
			    		+ "Also, since this is a stereophonic experience, make sure your headphones are "
			    		+ "on the correct sides.");
			    
			    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			          // Canceled

			        }
			        
			      });

			    alert.show();
			    SharedPreferences.Editor editor = settings.edit();
			     editor.putBoolean("dialogShown", true);
			     editor.commit();    

		   }
		
	    
		// our compass image
		image = (ImageView) findViewById(R.id.imageViewCompass);

		// TextView that will tell the user what degree is he heading
		tvHeading = (TextView) findViewById(R.id.tvHeading);

		// initialize your android device sensor capabilities
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
/* Initialize all views */
		
		ns_button = (Button) findViewById(R.id.ns_button);
		ns_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		sn_button = (Button) findViewById(R.id.sn_button);
		sn_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		harbor_button = (ToggleButton) findViewById(R.id.harbor_button);
		harbor_button.setOnClickListener((android.view.View.OnClickListener) this);
		harbor_button.setChecked(true);
		
		//lost_button = (Button) findViewById(R.id.lost_button);
		//lost_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		exit_button = (Button) findViewById(R.id.exit_button);
		exit_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		settings_button = (Button) findViewById(R.id.settings_button);
		settings_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		/* Initialize MediaPlayers */
		northPlayer = MediaPlayer.create(this, R.raw.trainlr);
        eastPlayer = MediaPlayer.create(this, R.raw.trainrl);
        southPlayer = MediaPlayer.create(this, R.raw.trainlr);
        westPlayer = MediaPlayer.create(this, R.raw.trainrl);

        /* Set player looping */
        northPlayer.setLooping(true);
        eastPlayer.setLooping(true);
        southPlayer.setLooping(true);
        westPlayer.setLooping(true);

        northPlayer.start();
        eastPlayer.start();
        southPlayer.start();
        westPlayer.start();


	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		
		// for the system's orientation sensor registered listeners
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
        northPlayer.start();
        eastPlayer.start();
        southPlayer.start();
        westPlayer.start();

	}

	@Override
	protected void onPause() {
		super.onPause();
		
		// to stop the listener and save battery
		mSensorManager.unregisterListener(this);
        northPlayer.pause();
        eastPlayer.pause();
        southPlayer.pause();
        westPlayer.pause();

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// get the angle around the z-axis rotated
		degree = Math.round(event.values[0]);
		degree_scaled = degree * (1f/360f);
		Log.i("Scaled Value: ", Float.toString(degree_scaled));

		tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

		// create a rotation animation (reverse turn degree degrees)
		RotateAnimation ra = new RotateAnimation(
				currentDegree, 
				-degree,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF,
				0.5f);

		// how long the animation will take place
		ra.setDuration(210);

		// set the animation after the end of the reservation status
		ra.setFillAfter(true);

		// Start the animation
		image.startAnimation(ra);
		currentDegree = -degree;
		
		if(0 <= degree_scaled && degree_scaled < .5){
		
			harborPlayer.setVolume(1 - degree_scaled, 0 + degree_scaled);
			
		}
		if(.5 <= degree_scaled && degree_scaled <= 1){
			
			harborPlayer.setVolume(1 + degree_scaled, 0 - degree_scaled);
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not in use
	}
	
	/* Method to control button interactions */
	public void onClick(View view){
		switch (view.getId()){
		case R.id.sn_button:
			if(degree >= 0 && degree <= 179){
				rightToLeftPlayer.start();
				
			}
			
			else if(degree >=180  && degree <= 360){
				leftToRightPlayer.start();
				
			}
			break;
			
		case R.id.ns_button:
			
			if(degree >= 0 && degree <= 179){
				leftToRightPlayer.start();
				
			}
			
			else if(degree >=180  && degree <= 360){
				rightToLeftPlayer.start();				
				
			}
			break;
			
		case R.id.harbor_button:
			if(harborPlayer.isPlaying())
			{
				harborPlayer.pause();
			}
			else
			{
				harborPlayer.start();
			}
			
			break;
			

		
		case R.id.settings_button:
			startSettingsActivity();
			break;
				
		case R.id.exit_button:
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
		     editor.putBoolean("dialogShown", false);
		     editor.commit();  
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
			
		}
	}
	
public void startSettingsActivity(){
    	
    	Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);      
    	
    }

 
}
	




