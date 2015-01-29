package com.gcioropina.jocrazboi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Main activity.
 * @author gcioropina
 * @created 1/13/15
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.main, menu);
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
     * Exit app.
     *
     * @param v
     */
    public Boolean onExitBtnClick(MenuItem v) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        return true;
    }

    /**
     * Start new game activity
     *
     * @param v
     */
    public void onNewGameBtnClick(View v) {
        if (v.getId() == R.id.button) {
            Intent myIntent = new Intent(MainActivity.this, NewGameActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
    }
    
    /**
     * Start profiles activity.
     * @param v
     * @return
     */
    public Boolean onProfilesMenuItemClick(MenuItem v) {
    	Intent myIntent = new Intent(MainActivity.this, ProfilesActivity.class);
        MainActivity.this.startActivity(myIntent);
    	return true;
    }
}
