package com.BBsRs.Loaders;

import java.io.IOException;
import java.util.Calendar;

import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.ProgressBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.BBsRs.astronews.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class SimpleNewsViewerLoader {
	
	ListView listView;
	ProgressBar progressBar;
	Context context;
	Thread thr;
	RelativeLayout errLt;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	Document doc;
	WebView webView;
	final Handler handler = new Handler();
	
	String LOG_TAG = "SimpleNewsViewerLoader";
	String html;
	
	public SimpleNewsViewerLoader(Context context,ListView view, ProgressBar progressBar, DisplayImageOptions options,RelativeLayout errLt){
		this.listView=view;												//gridview
		this.context=context;											//context
		this.progressBar=progressBar;									//progressBar
		this.options=options;											//РЅР°СЃС‚СЂРѕР№РєРё Р·Р°РіСЂСѓР·РєРё РєР°СЂС‚РёРЅРѕРє
		this.errLt=errLt;
		
		this.webView=new WebView(context);
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
	    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}
	
	public void SettingUpAdapter(){
		final Runnable updater = new Runnable() {
		    public void run() {
		    	
		    	webView.loadDataWithBaseURL(null,html,"text/html", "utf-8",null); // указываем страницу загрузки
		    	listView.addHeaderView(webView);
		    	
		    	  String[] values = new String[] { "Android List View", 
                          "Adapter implementation",
                          "Simple List View In Android",
                          "Create List View Android", 
                          "Android Example", 
                          "List View Source Code", 
                          "List View Array Adapter", 
                          "Android Example List View" 
                         };
		    	
		    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
		    	          android.R.layout.simple_list_item_1, android.R.id.text1, values);
		    	
		    	listView.setAdapter(adapter);
		    	
		    	progressBar.setVisibility(View.GONE);
	        	listView.setVisibility(View.VISIBLE);
		    }
		};
		handler.post(updater);
	}
	
	public void LoadSimpleNews(final String url){
		thr=new Thread(new Runnable() {				//Делаем в новом потоке
	        public void run() {
	        	try {
	        		imageLoader.pause();			//pause to kill lags
	        		
	        		Log.i(LOG_TAG, "StartLoadingSimpleNews page:"+url);
	        		
					doc =  Jsoup.connect(url)
								.userAgent(context.getResources().getString(R.string.user_agent))
								.timeout(context.getResources().getInteger(R.integer.user_timeout))
								//.cookie("remember", sPref.getString("remember", null))
								.get();
					
					html = doc.select("table").get(5).child(0).child(0).html()
							.replaceAll("/news/", "http://www.astronews.ru/news/")
							.replaceAll("hspace=\"40\" vspace=\"25\"", "hspace=\"0\" vspace=\"5\"");
					
					Calendar c = Calendar.getInstance(); 
					Calendar.getInstance();
					html = html.substring(0, html.indexOf("<a href=\"?data="+String.valueOf(c.get(Calendar.YEAR))));
					
					SettingUpAdapter();
					
					imageLoader.resume();
				} catch (IOException e) {
					Log.e(LOG_TAG, "Page: "+url);
					e.printStackTrace();
				}
	        }
	    });
		
		thr.start();
	}

}
