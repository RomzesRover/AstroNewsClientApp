package com.BBsRs.astronews;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.Button;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoaderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loader);
		Button start = (Button)findViewById(R.id.button1);
		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainViewerActivity.class));
			}
		});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.loader, menu);
//		return true;
//	}

}
