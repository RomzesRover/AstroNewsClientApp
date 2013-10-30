package com.BBsRs.astronews;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.TextView;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.BBsRs.Loaders.UserLoader;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

public class UserViewer extends Activity {
	UserLoader userLoader;
	SharedPreferences sPref;
	String userName;
	boolean animation=false;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_user);
	    final ActionBar ab = getSupportActionBar();			//берем бар
        ab.setDisplayHomeAsUpEnabled(true);					//включаем кнопку в баре "домой"
        ab.setLogo(getResources().getDrawable(R.drawable.ic_launcher));			//setting up normal icon (for miui and others ui)
        
        final RelativeLayout errLt = (RelativeLayout)findViewById(R.id.RelativeErrorLayout); //для ошибок
	    ImageView avatarSrc = (ImageView)findViewById(R.id.avatarSrc);
		TextView UserName = (TextView)findViewById(R.id.UserName),  
				 UserInfo = (TextView)findViewById(R.id.UserInfo);
		ProgressBar prBr = (ProgressBar)findViewById(R.id.progressBar1);
		RelativeLayout RlLt = (RelativeLayout)findViewById(R.id.UserRlLt);
		
		Uri data = getIntent().getData();
		userName =  data.toString().replace("mng", "mngold");
		ab.setTitle(getResources().getString(R.string.user));
		
		
		userLoader = new UserLoader(this, UserName, UserInfo, avatarSrc, prBr, RlLt,errLt, ab);
		if(savedInstanceState == null)
		userLoader.LoadUserInfo(userName);
		else{
			userLoader.SettingUpPage(savedInstanceState.getString("userName"), 
					savedInstanceState.getString("userInfo"), 
					savedInstanceState.getString("userAvatarSrc"), 
					savedInstanceState.getString("url"),
					savedInstanceState.getInt("error"));
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		 outState.putString("userName", userLoader.returnUserName());
		 outState.putString("userInfo", userLoader.returnUserInfo());
		 outState.putString("userAvatarSrc", userLoader.returnUserAvatarSrc());
		 outState.putString("url", userLoader.returnUserUrl());
		 outState.putInt("error", userLoader.returnError());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
	          case android.R.id.home:
	        	  animation=true;
	          	  finish();
	        	  break;
	          default:
	              return super.onOptionsItemSelected(item);
	      }
	      return true;
	}

}
