package com.gcioropina.jocrazboi;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * New game activity.
 * @author gcioropina
 * @created 1/13/15
 */
public class NewGameActivity extends ActionBarActivity {

	private Database db;
	ArrayList<String> profiles;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        
        db = new Database(this);
        
        profiles = db.getAllProfiles();
        if(profiles.size() > 0) {
        	showProfilesSpinner();
        }
    }
    
    /**
     * Show spinner and text for profiles.
     */
    private void showProfilesSpinner()
    {
    	TextView textView = (TextView) findViewById(R.id.savedProfilesText);
    	textView.setVisibility(TextView.VISIBLE);
    	
    	Spinner spinner = (Spinner) findViewById(R.id.profilesSpinner);
		spinner.setVisibility(Spinner.VISIBLE);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, profiles) {
		        	@Override
		        	public View getView(int position, View convertView, ViewGroup parent) {
		        	    View view = super.getView(position, convertView, parent);
		        	    TextView text = (TextView) view.findViewById(android.R.id.text1);
		        	    text.setTextColor(getResources().getColor(R.color.spinner_profiles));
		        	    return view;
		        	  }
		        	};
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
		    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				EditText username = (EditText) findViewById(R.id.editText);
				
				username.setText(adapterView.getSelectedItem().toString());
		    } 

			@Override
		    public void onNothingSelected(AdapterView<?> adapterView) {
		        return;
		    } 
		}); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_new_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get username and go to the game activity.
     * @param v
     */
    public void onBeginGameBtnClick(View v) {
        String username = ((EditText) findViewById(R.id.editText)).getText().toString();
//        Log.i("username", username.toString());

        if (username.isEmpty()) {
            showErrorDialog();
            return;
        }
        
        /**
         * Try to save profile
         */
        try {
        	db.addProfile(username);
        } catch(Exception ex) {
        	Log.e("save_profile", ex.getMessage());
        }
        
        Intent myIntent = new Intent(NewGameActivity.this, GameActivity.class);
        myIntent.putExtra("username", username);
        NewGameActivity.this.startActivity(myIntent);
    }

    /**
     * Show error dialog
     */
    private void showErrorDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.no_user_name);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }
}