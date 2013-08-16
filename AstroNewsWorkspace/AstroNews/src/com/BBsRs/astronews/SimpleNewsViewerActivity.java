package com.BBsRs.astronews;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.TextView;

import android.net.Uri;
import android.os.Bundle;

public class SimpleNewsViewerActivity extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Uri data = getIntent().getData();
	    setContentView(R.layout.activity_simple_news_viewer);
	    TextView text = (TextView)findViewById(R.id.textView1);
	    text.setText(data.toString());
	  
	}
}
	
	
