package com.BBsRs.astronews;

import java.util.ArrayList;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.ProgressBar;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.BBsRs.Loaders.SimpleNewsViewerLoader;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class SimpleNewsViewerActivity extends Activity {
	
	SimpleNewsViewerLoader simpleNewsViewerLoader; 
	ListView listView;
	ProgressBar progressBar;
	DisplayImageOptions options ;
	SharedPreferences sPref; 
	ArrayList<CommentsBaseInfoArray> commentsBaseInfoArray = new ArrayList<CommentsBaseInfoArray>();
	String html;
	int posX=0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Uri data = getIntent().getData();										//intent data
	    setContentView(R.layout.activity_simple_news_viewer);
	    final ActionBar ab = getSupportActionBar();
	    ab.setDisplayHomeAsUpEnabled(true);										//back button
		ab.setLogo(getResources().getDrawable(R.drawable.ic_launcher));			//setting up normal icon (for miui and others ui)
		
		options = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.ic_stub)
        //.showImageForEmptyUri(R.drawable.logo)
        .cacheOnDisc(true)	
        .cacheInMemory(true)						//кешируем на флешке, нужно разрешение
        .build();
        
		final RelativeLayout errLt = (RelativeLayout)findViewById(R.id.ErrorRelativeLayout); //дл€ ошибок
        listView = (ListView)findViewById(R.id.NewsListView);
	    progressBar = (ProgressBar)findViewById(R.id.NewsProgressBar);
	    simpleNewsViewerLoader = new SimpleNewsViewerLoader(getApplicationContext(), listView, progressBar, options,errLt);
	    
	    if(savedInstanceState == null || !savedInstanceState.containsKey("commentsBaseInfoArray")) {
	    	simpleNewsViewerLoader.LoadSimpleNews(data.toString());
	    }
	    else{
	    	commentsBaseInfoArray = savedInstanceState.getParcelableArrayList("commentsBaseInfoArray");
	    	html=savedInstanceState.getString("html");
	    	posX=savedInstanceState.getInt("posX");
	    	if (!(commentsBaseInfoArray.size()<1))
	    		simpleNewsViewerLoader.settingUpStringArray(commentsBaseInfoArray, html,posX);
	    	else 
	    		simpleNewsViewerLoader.LoadSimpleNews(data.toString());	
	    }
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		 outState.putParcelableArrayList("commentsBaseInfoArray", simpleNewsViewerLoader.returnStringArray());
		 outState.putString("html", simpleNewsViewerLoader.returnHtml());
		 outState.putInt("posX",  simpleNewsViewerLoader.returnPosX());
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
	
	
