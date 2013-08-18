package com.BBsRs.astronews;

import java.util.ArrayList;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.AdapterView;
import org.holoeverywhere.widget.GridView;
import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.Toast;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.BBsRs.Loaders.NewsViewerLoader;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MainViewerActivity extends Activity implements ActionBar.OnNavigationListener {
	
	NewsViewerLoader newsLoader; 
	GridView gridList;
	ProgressBar progressBar;
	DisplayImageOptions options ;
	SharedPreferences sPref; 
	ArrayList<NewsBaseInfoArray> newsBaseInfoArray = new ArrayList<NewsBaseInfoArray>();
	int page=1,posX=0;
	
	SearchView searchView;
	private String[] mLocations;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main_viewer);
	    final ActionBar ab = getSupportActionBar();
	    ab.setTitle("");
		ab.setLogo(getResources().getDrawable(R.drawable.ic_launcher));			//setting up normal icon (for miui and others ui)
		
		mLocations = getResources().getStringArray(R.array.MainViewerTabs);

        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.MainViewerTabs, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);
		
		options = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.ic_stub)
        //.showImageForEmptyUri(R.drawable.logo)
        .cacheOnDisc(true)	
        .cacheInMemory(true)						//кешируем на флешке, нужно разрешение
        .build();
        
		final RelativeLayout errLt = (RelativeLayout)findViewById(R.id.ErrorRelativeLayout); //дл€ ошибок
        gridList = (GridView)findViewById(R.id.NewsGridView);
	    progressBar = (ProgressBar)findViewById(R.id.NewsProgressBar);
	    LayoutInflater inflater = getLayoutInflater();
	    View footerView = inflater.inflate(R.layout.ic_element_simple_news_footer, null);
	    gridList.addFooterView(footerView, null, false);
	    newsLoader = new NewsViewerLoader(getApplicationContext(), gridList, progressBar,footerView, options,errLt);
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
//            	overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				return false;
			}
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				getSupportActionBar().setTitle("");
				return false;
			}});	
        return true;
    }

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

}
