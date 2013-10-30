package com.BBsRs.astronews;

import java.util.ArrayList;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.ProgressBar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.BBsRs.Loaders.SimpleNewsViewerLoader;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.actionbarsherlock.widget.ShareActionProvider.OnShareTargetSelectedListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class SimpleNewsViewerActivity extends Activity {
	
	SimpleNewsViewerLoader simpleNewsViewerLoader; 
	ListView listView;
	ProgressBar progressBar;
	DisplayImageOptions options ;
	SharedPreferences sPref; 
	ArrayList<CommentsBaseInfoArray> commentsBaseInfoArray = new ArrayList<CommentsBaseInfoArray>();
	String html, title, url;
	int posX=0;
	Menu mainMenu; 																// local variable for menu
	ShareActionProvider actionProvider;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Uri data = getIntent().getData();										//intent data
	    url = data.toString();
	    setContentView(R.layout.activity_simple_news_viewer);
	    final ActionBar ab = getSupportActionBar();
	    ab.setDisplayHomeAsUpEnabled(true);										//back button
		ab.setLogo(getResources().getDrawable(R.drawable.ic_launcher));			//setting up normal icon (for miui and others ui)
		
		options = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.ic_stub)
        //.showImageForEmptyUri(R.drawable.logo)
        .cacheOnDisc(true)	
        .cacheInMemory(true)						//�������� �� ������, ����� ����������
        .build();
        
		final RelativeLayout errLt = (RelativeLayout)findViewById(R.id.ErrorRelativeLayout); //��� ������
        listView = (ListView)findViewById(R.id.NewsListView);
	    progressBar = (ProgressBar)findViewById(R.id.NewsProgressBar);
	    simpleNewsViewerLoader = new SimpleNewsViewerLoader(getApplicationContext(), listView, progressBar, options,errLt,ab);
	    
	    if(savedInstanceState == null) {
	    	simpleNewsViewerLoader.LoadSimpleNews(url);
	    }
	    else{
	    	commentsBaseInfoArray = savedInstanceState.getParcelableArrayList("commentsBaseInfoArray");
	    	html=savedInstanceState.getString("html");
	    	title=savedInstanceState.getString("title");
	    	posX=savedInstanceState.getInt("posX");
	    	simpleNewsViewerLoader.settingUpStringArray(commentsBaseInfoArray, html, title, posX);
	    }
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		 outState.putParcelableArrayList("commentsBaseInfoArray", simpleNewsViewerLoader.returnStringArray());
		 outState.putString("html", simpleNewsViewerLoader.returnHtml());
		 outState.putString("title", simpleNewsViewerLoader.returnTitle());
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
      getSupportMenuInflater().inflate(R.menu.menu_activity_simple_news_viewer, menu);
      mainMenu=menu;
      MenuItem actionItem = menu.findItem(R.id.menu_share);
      actionProvider = (ShareActionProvider) actionItem.getActionProvider();
      actionProvider.setShareIntent(createShareIntent());
      actionProvider.setOnShareTargetSelectedListener(new OnShareTargetSelectedListener(){
		@Override
		 public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
      	  	Intent shareIntent = intent;
      	  	startActivity(shareIntent);
      	  	return true;
        }});
      return true;
  }
	
	private Intent createShareIntent() {
	      Intent shareIntent = new Intent(Intent.ACTION_SEND);
	      shareIntent.setType("text/plain");
	      shareIntent.putExtra(Intent.EXTRA_TEXT, url);
	      return shareIntent;
	}
}
	
	
