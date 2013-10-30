package com.BBsRs.astronews;

import java.util.ArrayList;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.GridView;
import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.TextView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.BBsRs.Loaders.SearchNewsLoader;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class SearchNewsActivity extends Activity {
	
	SearchNewsLoader newsLoader; 
	GridView gridList;
	TextView text;
	ProgressBar progressBar;
	DisplayImageOptions options ;
	SharedPreferences sPref; 
	ArrayList<NewsBaseInfoArray> newsBaseInfoArray = new ArrayList<NewsBaseInfoArray>();
	int page=1,posX=0;
	
	SearchView searchView;
	
	String request;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main_viewer);
	    final ActionBar ab = getSupportActionBar();
	    ab.setDisplayHomeAsUpEnabled(true);										//back button
		ab.setLogo(getResources().getDrawable(R.drawable.ic_launcher));			//setting up normal icon (for miui and others ui)
		
		sPref = getApplicationContext().getSharedPreferences("A", 1);
		request =  sPref.getString("request", "");
		
		ab.setTitle(getResources().getString(R.string.searchM)+ ": "+request);	//set title with search request;
		
		options = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.ic_stub)
        //.showImageForEmptyUri(R.drawable.logo)
        .cacheOnDisc(true)	
        .cacheInMemory(true)						//�������� �� ������, ����� ����������
        .build();
        
		final RelativeLayout errLt = (RelativeLayout)findViewById(R.id.ErrorRelativeLayout); //��� ������
        gridList = (GridView)findViewById(R.id.NewsGridView);
	    progressBar = (ProgressBar)findViewById(R.id.NewsProgressBar);
	    text = (TextView)findViewById(R.id.SearchN);
	    LayoutInflater inflater = getLayoutInflater();
	    View footerView = inflater.inflate(R.layout.ic_element_simple_news_footer, null);
	    gridList.addFooterView(footerView, null, false);
	    newsLoader = new SearchNewsLoader(getApplicationContext(), gridList, progressBar,footerView, options,errLt, request, text);
	    if(savedInstanceState == null || !savedInstanceState.containsKey("newsBaseInfoArray")) {
	    	newsLoader.LoadPageOfNews(1);
	    }
	    else{
	    	newsBaseInfoArray = savedInstanceState.getParcelableArrayList("newsBaseInfoArray");
	    	page=savedInstanceState.getInt("page");
	    	posX=savedInstanceState.getInt("posX");
	    	if (!(newsBaseInfoArray.size()<1))
	    		newsLoader.settingUpStringArray(newsBaseInfoArray, page,posX,savedInstanceState.getInt("error"));
	    	else 
	    		newsLoader.LoadPageOfNews(1);	
	    }
	    
	    
	    // TODO Auto-generated method stub
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		 outState.putParcelableArrayList("newsBaseInfoArray", newsLoader.returnStringArray());
		 outState.putInt("page", newsLoader.returnPage());
		 outState.putInt("posX",  newsLoader.returnPosX());
		 outState.putInt("error", newsLoader.returnError());
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //Create the search view
        searchView = new SearchView(getSupportActionBarContext());
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search));
        menu.add(getResources().getString(R.string.searchM))
            .setIcon(R.drawable.abs__ic_search)
            .setActionView(searchView)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        searchView.setOnQueryTextListener(new OnQueryTextListener(){
			@Override
			public boolean onQueryTextSubmit(String query) {
				InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            	imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            	Intent intt = new Intent(getApplicationContext(), SearchNewsActivity.class);
            	sPref = getApplicationContext().getSharedPreferences("A", 1);
				Editor ed = sPref.edit(); 
				ed.putString("request", query); 
				ed.commit();
            	intt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	getApplicationContext().startActivity(intt);
            	finish();
//            	overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				return false;
			}
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}});	
        return true;
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
	
	public boolean  onKeyUp(int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_UP){
		    switch(keyCode) {
		    case KeyEvent.KEYCODE_SEARCH:
		        searchView.performClick();
		        return true;  
		    }
		}
		return super.onKeyUp(keyCode, event);
		}
	
}
