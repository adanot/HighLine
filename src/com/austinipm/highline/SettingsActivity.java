package com.austinipm.highline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingsActivity extends ActionBarActivity implements OnClickListener {
	
	Button back_button, map_button, about_button;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		back_button = (Button) findViewById(R.id.back_button);
		back_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		map_button = (Button) findViewById(R.id.map_button);
		map_button.setOnClickListener((android.view.View.OnClickListener) this);
		
		about_button = (Button) findViewById(R.id.about_button);
		about_button.setOnClickListener((android.view.View.OnClickListener) this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_settings,
					container, false);
			return rootView;
		}
	}
	
	/* Method to control button interactions */
	public void onClick(View view){
		switch (view.getId()){

		case R.id.back_button:
			startMainActivity();
			break;
			
		case R.id.map_button:
			startMapActivity();
			break;
			
		case R.id.about_button:
			startAboutActivity();
			break;
				
		case R.id.exit_button:
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
			
		}
	}
	
    public void startMainActivity(){
    	
    	Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    	
    }
    
    public void startMapActivity(){
    	
    	Intent intent = new Intent(SettingsActivity.this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    	
    }
    
    public void startAboutActivity(){
    	
    	Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    	
    }

}
