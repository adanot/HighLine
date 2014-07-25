package com.austinipm.highline;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;


public class MainActivity extends Activity implements OnClickListener, OnSeekBarChangeListener, SensorEventListener  {
	
	Button money_button, ilu_button, lost_button, exit_button;
	MediaPlayer money_player, ilu_player, lost_player;
	Handler seekHandler = new Handler();
	AudioManager am;
	SeekBar volume;

	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/* AudioManager setup */
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);		
		int maxV = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int startV = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		/* Sensor(compass) setup */
		SensorManager mSensorManager;
	    Sensor mAccelerometer; 
	    mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


		/* Initialize all views */
		
		money_button = (Button) findViewById(R.id.money_button);
		money_button.setOnClickListener((android.view.View.OnClickListener) this);

		ilu_button = (Button) findViewById(R.id.ilu_button);
		ilu_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		lost_button = (Button) findViewById(R.id.lost_button);
		lost_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		exit_button = (Button) findViewById(R.id.exit_button);
		exit_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		/* Initialize MediaPlayers */
		money_player = MediaPlayer.create(this, R.raw.money);
		ilu_player = MediaPlayer.create(this, R.raw.ilu);
		lost_player = MediaPlayer.create(this, R.raw.lost);
		
		/* Initialize volume SeekBar */
		volume = (SeekBar) findViewById(R.id.money_sb);
		volume.setMax(maxV);
		volume.setProgress(startV);
		volume.setOnSeekBarChangeListener((android.widget.SeekBar.OnSeekBarChangeListener) this);

		
	};
	
	/* Method to update system volume on SeekBar change */
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2){
		
		am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
		
		
	}
	
	/* Method to update Volume SeekBar on hardware volume keys */
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
	   }
	

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
		case R.id.exit_button:
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
			
		}
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
	
	

	
	/* Sensor methods */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}

