package com.austinipm.highline;

import android.app.Activity;
import android.content.Context;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends Activity implements OnClickListener, OnSeekBarChangeListener  {
	
	//private static final String LOGTAG = "HighLine";
	Button money_button, ilu_button, lost_button, exit_button, settings_button;
	MediaPlayer money_player, ilu_player, lost_player;
	Handler seekHandler = new Handler();
	AudioManager am;
	SeekBar volume_L, volume_R;
	SensorManager mSensorManager;
    Sensor mAccelerometer; 
	float curr_vol_left, curr_vol_right;
	float ALPHA = 0.03f;

	

	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/* AudioManager setup */
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);		
		int maxV = 100;
		//am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int startV = 100;
	    //am.getStreamVolume(AudioManager.STREAM_MUSIC);
		
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
		
		money_button = (Button) findViewById(R.id.money_button);
		money_button.setOnClickListener((android.view.View.OnClickListener) this);

		//ilu_button = (Button) findViewById(R.id.ilu_button);
		//ilu_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		//lost_button = (Button) findViewById(R.id.lost_button);
		//lost_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		exit_button = (Button) findViewById(R.id.exit_button);
		exit_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		settings_button = (Button) findViewById(R.id.settings_button);
		settings_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		/* Initialize MediaPlayers */
		money_player = MediaPlayer.create(this, R.raw.money);
		//ilu_player = MediaPlayer.create(this, R.raw.ilu);
		//lost_player = MediaPlayer.create(this, R.raw.lost);
		
		/* Initialize volume SeekBar */
		volume_L = (SeekBar) findViewById(R.id.left_sb);
		volume_L.setMax(maxV);
		volume_L.setProgress(startV);
		volume_L.setOnSeekBarChangeListener((android.widget.SeekBar.OnSeekBarChangeListener) this);
		
		volume_R = (SeekBar) findViewById(R.id.right_sb);
		volume_R.setMax(maxV);
		volume_R.setProgress(startV);
		volume_R.setOnSeekBarChangeListener((android.widget.SeekBar.OnSeekBarChangeListener) this);

		
	};
	
	/* Method to update system volume on SeekBar change */
	public void onProgressChanged(SeekBar bar, int progress, boolean arg2){
		
		if(bar.equals(volume_L)){
			curr_vol_left = (float)progress/100;			
			money_player.setVolume(curr_vol_left , curr_vol_right);
		}
		else if(bar.equals(volume_R)){
			curr_vol_right = (float)progress/100;
			money_player.setVolume(curr_vol_left, curr_vol_right);
		}
		
		//am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
		
		
	}
	
	/* Method to update Volume SeekBar on hardware volume keys 
	   @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){ 
	    	 int index = volume.getProgress(); 
	         volume.setProgress(index + 1); 
	         return true; 
	     }else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
	         int index = volume.getProgress(); 
	         volume.setProgress(index - 1); 
	         return true; 
	     }
	      return super.onKeyDown(keyCode, event); 
	   }*/
	

	/* Method to control button interactions */
	public void onClick(View view){
		switch (view.getId()){
		case R.id.money_button:
			if(((ToggleButton) money_button).isChecked()){
				money_player.start();
				break;
			}
			else{
				money_player.pause();
				break;
			}
		case R.id.ilu_button:
			if(((ToggleButton) ilu_button).isChecked()){
				ilu_player.start();
				break;
				}
				else{
				ilu_player.pause();
				break;
				}
		case R.id.lost_button:
			if(((ToggleButton) lost_button).isChecked()){
				lost_player.start();
				break;
				}
				else{
				lost_player.pause();
				break;
				}
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

/* SeekBar methods */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	protected float lowPass( Float input, Float output ) {
	    if ( output == null ) return input;     
	    output = output + ALPHA * (input - output);
	    return output;
	}
	
	  private SensorEventListener mySensorEventListener = new SensorEventListener() {
	      Float azimuth = null;

		    @Override
		    public void onAccuracyChanged(Sensor sensor, int accuracy) {
		    }

		    @Override
		    public void onSensorChanged(SensorEvent event) {
		      // angle between the magnetic north direction
		      // 0=North, 90=East, 180=South, 270=West
		      azimuth = lowPass(event.values[0], azimuth);
		      float azimuthInDegrees = ((float)Math.toDegrees(azimuth)+360)%360;
		      Log.i("Azimuth", Float.toString(azimuthInDegrees) + "Degrees");
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

