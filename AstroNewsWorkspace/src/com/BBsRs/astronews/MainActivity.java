package com.BBsRs.astronews;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.BBsRs.Fragments.NewsFragment;
import com.BBsRs.SlidingMenu.MenuFragment;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity{
	
	private Fragment mContent;
	SearchView searchView;
	SharedPreferences sPref;    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final ActionBar ab = getSupportActionBar();
        ab.setLogo(getResources().getDrawable(R.drawable.ic_launcher));
        
		setContentView(R.layout.responsive_content_frame);
		
		if (savedInstanceState != null)
			setTitle(savedInstanceState.getString("title")); 
		else
			setTitle(getResources().getStringArray(R.array.MainViewerTabs)[1]);
		
		// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// show home as up so we can toggle
		//	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		
		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new NewsFragment();	
		
		// set the Above View
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		
		// set the Behind View
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new MenuFragment())
		.commit();
		
		
		// customize the SlidingMenu
				SlidingMenu sm = getSlidingMenu();
				sm.setShadowWidthRes(R.dimen.shadow_width);
				sm.setShadowDrawable(R.drawable.shadow);
				sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
				sm.setFadeDegree(0.35f);
				sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				setSlidingActionBarEnabled(false);
	}
	
	
	/*
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case menu.:
	                break;
	            case R.id.menu_search:
	            	Intent srt = new Intent(getApplicationContext(), Activity_search.class);
	            	startActivity(srt);
	            	break;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	        return true;
	    }*/
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        //Used to put dark icons on light action bar

	        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	        //Create the search view
	        searchView = new SearchView(getSupportActionBar().getThemedContext());
	        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	        searchView.setQueryHint(getResources().getString(R.string.search));
	        menu.add(getResources().getString(R.string.searchM))
	            .setIcon(R.drawable.abs__ic_search)
	            .setActionView(searchView)
	            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	        searchView.setOnQueryTextListener(new OnQueryTextListener(){
				@Override
				public boolean onQueryTextSubmit(String query) {
//					Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
					InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
	            	imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
//	            	setTitle(getResources().getString(R.string.searchM)+": "+query);
	            	
//	    			mContent = new SearchFragment(query);
//	    			getSupportFragmentManager()
//	    			.beginTransaction()
//	    			.replace(R.id.content_frame, mContent)
//	    			.commit();
	            	Intent intt = new Intent(getApplicationContext(), SearchNewsActivity.class);
	            	sPref = getApplicationContext().getSharedPreferences("A", 1);
					Editor ed = sPref.edit(); 
					ed.putString("request", query); 
					ed.commit();
	            	intt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            	getApplicationContext().startActivity(intt);
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
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
		outState.putString("title", String.valueOf(getTitle()));
	}
	
	public void switchContent(Fragment fragment, String Id) {
		if (!getTitle().equals(Id)){
			setTitle(Id);
			mContent = fragment;
			getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, fragment)
			.commit();}
		getSlidingMenu().showContent();
	}

}