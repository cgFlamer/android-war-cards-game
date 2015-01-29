package com.gcioropina.jocrazboi;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Profile management activity.
 * @author gcioropina
 * @created 1/27/15
 */
public class ProfilesActivity extends Activity {

	private Database db;
	
	ArrayList<String> profiles;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
		
		db = new Database(this);
		profiles = db.getAllProfiles();
		ListView listView = (ListView) findViewById(R.id.profilesListView);
		
		if(profiles.size() == 0) {			
			listView.setVisibility(GridView.INVISIBLE);			
		} else {
			TextView textView = (TextView) findViewById(R.id.noCardsTextView);
			textView.setVisibility(TextView.GONE);
			/**
			 * Create array adapter for profiles& set it for list view.
			 */
			final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			        android.R.layout.simple_expandable_list_item_1, profiles) {
			        	@Override
			        	public View getView(int position, View convertView, ViewGroup parent) {
			        	    View view = super.getView(position, convertView, parent);
			        	    TextView text = (TextView) view.findViewById(android.R.id.text1);
			        	    text.setTextColor(getResources().getColor(R.color.spinner_profiles));
			        	    return view;
			        	  }
			        	};
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, final View view,
						int position, long id) {
					final String item = (String) parent.getItemAtPosition(position);
			        view.animate().setDuration(2000).alpha(0)
			            .withEndAction(new Runnable() {
			              @Override
			              public void run() {
			                profiles.remove(item);
			                adapter.notifyDataSetChanged();
			                view.setAlpha(1);
			              }
			            });
			        /**
			         * Delete profile from sqlite db.
			         */
			        db.deleteProfile(item);
				}

			    });
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.profiles, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
}
