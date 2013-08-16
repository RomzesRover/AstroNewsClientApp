package com.BBsRs.astronews;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

import android.net.Uri;
import android.os.Bundle;

public class SimpleNewsViewerActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Uri data = getIntent().getData();										//intent data
	    setContentView(R.layout.activity_simple_news_viewer);
	    final ActionBar ab = getSupportActionBar();
	    ab.setDisplayHomeAsUpEnabled(true);										//back button
		ab.setLogo(getResources().getDrawable(R.drawable.ic_launcher));			//setting up normal icon (for miui and others ui)
		
	    TextView text = (TextView)findViewById(R.id.textView1);
	    text.setText(data.toString());
	  
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
	          case android.R.id.home:
	          	  finish();
	        	  break;
	          default:
	              return super.onOptionsItemSelected(item);
	      }
	      return true;
	}
}
	
	
