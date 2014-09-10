package com.austinipm.highline;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener  {
	
	//private static final String LOGTAG = "HighLine";
	Button ns_button, sn_button, ilu_button, lost_button, exit_button, settings_button;
	MediaPlayer leftToRightPlayer, rightToLeftPlayer, ilu_player, lost_player;
	Handler seekHandler = new Handler();
	AudioManager am;
	SensorManager mSensorManager;
    Sensor mAccelerometer; 
	float curr_vol_left, curr_vol_right;
	float ALPHA = 0.03f;
    Float azimuth = null;
    Float azimuthRad = null;

	

	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		
		/* Sensor(compass) setup */

	    mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        if (mAccelerometer != null) {
        	mSensorManager.registerListener(mySensorEventListener, mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
            Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor");
          } else {
            Log.e("Compass MainActivity", "Registerered for ORIENTATION Sensor");
            Toast.makeText(this, "ORIENTATION Sensor not found",
                Toast.LENGTH_LONG).show();
            finish();
          }


		/* Initialize all views */
		
		ns_button = (Button) findViewById(R.id.ns_button);
		ns_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		sn_button = (Button) findViewById(R.id.sn_button);
		sn_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		//ilu_button = (Button) findViewById(R.id.ilu_button);
		//ilu_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		//lost_button = (Button) findViewById(R.id.lost_button);
		//lost_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		exit_button = (Button) findViewById(R.id.exit_button);
		exit_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		settings_button = (Button) findViewById(R.id.settings_button);
		settings_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		/* Initialize MediaPlayers */
		leftToRightPlayer = MediaPlayer.create(this, R.raw.trainlr);
		rightToLeftPlayer = MediaPlayer.create(this, R.raw.trainrl);

		
	};

	
	/* Method to control button interactions */
	public void onClick(View view){
		switch (view.getId()){
		case R.id.sn_button:
			if(azimuth >= 0 && azimuth <= 179){
				rightToLeftPlayer.start();
				
			}
			
			else if(azimuth >=180  && azimuth <= 360){
				leftToRightPlayer.start();
				
			}
			break;
			
		case R.id.ns_button:
			
			if(azimuth >= 0 && azimuth <= 179){
				leftToRightPlayer.start();
				
			}
			
			else if(azimuth >=180  && azimuth <= 360){
				rightToLeftPlayer.start();				
				
			}
			break;
			

		
		case R.id.settings_button:
			startSettingsActivity();
			break;
				
		case R.id.exit_button:
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


	protected float lowPass( Float input, Float output ) {
	    if ( output == null ) return input;     
	    output = output + ALPHA * (input - output);
	    return output;
	}
	
	  private SensorEventListener mySensorEventListener = new SensorEventListener() {



		    @Override
		    public void onAccuracyChanged(Sensor sensor, int accuracy) {
		    }

		    @Override
		    public void onSensorChanged(SensorEvent event) {
		      // angle between the magnetic north direction
		      // 0=North, 90=East, 180=South, 270=West
		      azimuthRad = lowPass(event.values[0], azimuthRad);
		      azimuth = ((float)Math.toDegrees(azimuthRad)+360)%360;

		    }
		  };

		  @Override
		  protected void onDestroy() {
		    super.onDestroy();
		    if (mAccelerometer != null) {
		    	mSensorManager.unregisterListener(mySensorEventListener);
		    }
		  }


		} 

