package com.BBsRs.Loaders;

import java.io.IOException;

import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.BBsRs.astronews.R;
import com.actionbarsherlock.app.ActionBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class UserLoader {
	
	Context context;
	ImageView avatarSrc;
	TextView UserName,  UserInfo;
	Thread thr;
	SharedPreferences sPref; 
	final Handler handler = new Handler();
	Document doc;
	ProgressBar prBr;
	RelativeLayout RlLt;
	String userName,  userInfo,  userAvatarSrc, url;
	RelativeLayout errLt;
	int error=0;
	ActionBar ab;
	String LOG_TAG = "UserLoader";
	
	public UserLoader(Context context, TextView UserName, TextView UserInfo, ImageView avatarSrc,ProgressBar prBr, RelativeLayout RlLt,RelativeLayout errLt, ActionBar ab){
		this.context=context;
		this.UserInfo=UserInfo;
		this.UserName=UserName;
		this.avatarSrc=avatarSrc;
		this.prBr=prBr;
		this.RlLt=RlLt;
		this.errLt=errLt;
		this.ab=ab;
		
	}
	
	public String returnUserName(){
		return userName;
	}
	
	public String returnUserInfo(){
		return userInfo;
	}
	
	public String returnUserAvatarSrc(){
		return userAvatarSrc;
	}
	
	public String returnUserUrl(){
		return url;
	}
	
	public int returnError(){
		return error;
	}
	
	public void SettingUpPage(String userName, String userInfo, String  userAvatarSrc, String url, int error){
		this.userName=userName;
		this.userInfo=userInfo;
		this.url=url;
		this.userAvatarSrc=userAvatarSrc;
		this.error=error;
		if (error==0)
		setupInfo();
		else
		Error();
		
	}
	
	public void setupInfo(){
		final Runnable updater = new Runnable() {
	        public void run() {
	        	
	        	UserName.setText(userName);
	        	UserInfo.setText(Html.fromHtml(userInfo));
	        	
	        	ab.setTitle(context.getResources().getString(R.string.user)+" "+userName);
	        	
	        	ImageLoader imageLoader = ImageLoader.getInstance();
		        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		        DisplayImageOptions options = new DisplayImageOptions.Builder()
		        .showStubImage(R.drawable.ic_stub)
		        //.showImageForEmptyUri(R.drawable.logo)
		        .cacheOnDisc()	
		        .cacheInMemory()						//кешируем на флешке, нужно разрешение
		        .displayer(new FadeInBitmapDisplayer(100))
		        .build();
		        imageLoader.displayImage(userAvatarSrc, avatarSrc, options);
	        	
	        	prBr.setVisibility(View.GONE);
	        	RlLt.setVisibility(View.VISIBLE);
	        	}
	    };
		handler.post(updater);
	}
	
	public void Error(){
		final Runnable updater = new Runnable() {
	    public void run() {
	    	error=1;
	    	errLt.setVisibility(View.VISIBLE);
	    	RlLt.setVisibility(View.GONE);
	    	prBr.setVisibility(View.GONE);
	    	Button retry = (Button)errLt.findViewById(R.id.buttonReconnect);
			//при нажатии на кнопку
	    	retry.setOnClickListener(new View.OnClickListener() {
	    		@Override
	    		public void onClick(View v) {
	    			retry();
	    		}
	    	});
	        }
		};
		handler.post(updater);
	}
	
	public void retry(){
		final Runnable updater = new Runnable() {
	    public void run() {
	    	error=0;
	    	errLt.setVisibility(View.GONE);
	    	RlLt.setVisibility(View.GONE);
	    	prBr.setVisibility(View.VISIBLE);
	    	LoadUserInfo(url);
	    }};
		handler.post(updater);
		
	}
	
	public void LoadUserInfo(final String url){
		this.url=url;
		thr=new Thread(new Runnable() {				//������ � ����� ������
	        public void run() {
	        	try {
	        		Log.i(LOG_TAG, "StartLoadUserPage: "+url);
	        		sPref = context.getSharedPreferences("A", 1);
	        		doc =  Jsoup.connect(url)
	        					.userAgent(context.getResources().getString(R.string.user_agent))
	        					.timeout(context.getResources().getInteger(R.integer.user_timeout))
//								.cookie("remember", sPref.getString("remember", null))
								.header("Accept-Encoding", "identity")
	        					.get();
	        		
	        		userInfo = doc.select("table[border=0][cellpadding=0][cellspacing=0][width=100%]").html()
	        				.replaceAll("<tr align=\"left\">", "<br><tr align=\"left\">")
	        				.replaceAll("</td>", " </td>")
	        				.replaceFirst("<br>", "")
	        				.replaceFirst("<br>", "");
	        		
	        		int first = userInfo.indexOf("<tr align=\"left\">");
	        		int second = userInfo.indexOf("</tr>");
	        		userInfo = userInfo.substring(0, first) + userInfo.substring(second, userInfo.length());
	        		
	        		Log.i(LOG_TAG, userInfo +"\n"+"first="+String.valueOf(first)+" second:"+String.valueOf(second));
	        		
	        		userName = doc.select("h3[style=color : #006593;]").first().text().replaceAll(context.getResources().getString(R.string.replace_ru_one)+" ", "");
	        		
	        		userAvatarSrc = "http://www.astronews.ru/"+doc.select("table[border=0][cellpadding=0][cellspacing=0][width=100%]").select("img").attr("src");
	        		setupInfo();
	        		
				} catch (IOException e) {
					Log.e(LOG_TAG, "Load Error"); Error(); 
					e.printStackTrace();
				} catch (NullPointerException e) {
	        		Log.e(LOG_TAG, "null Load Error"); Error();
					e.printStackTrace();
				} catch (Exception e) {
	        		Log.e(LOG_TAG, "other Load Error"); Error();
					e.printStackTrace();
				}
	        }
	    });
		
		thr.start();
	}
}
