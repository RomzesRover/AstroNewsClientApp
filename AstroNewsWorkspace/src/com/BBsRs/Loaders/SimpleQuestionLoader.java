package com.BBsRs.Loaders;

import java.io.IOException;
import java.util.ArrayList;

import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.ProgressBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BBsRs.Adapters.SimpleNewsAdapter;
import com.BBsRs.astronews.CommentsBaseInfoArray;
import com.BBsRs.astronews.R;
import com.BBsRs.astronews.URLImageParser;
import com.actionbarsherlock.app.ActionBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class SimpleQuestionLoader {
	
	ListView listView;
	ProgressBar progressBar;
	Context context;
	Thread thr;
	RelativeLayout errLt;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	Document doc;
	LayoutInflater inflater;
	final Handler handler = new Handler();
	
	String LOG_TAG = "SimpleQuestionViewerLoader";
	String html, url="", title;
	
	int error=0;
	ActionBar ab;
	
	ArrayList<CommentsBaseInfoArray> commentsBaseInfoArray = new ArrayList<CommentsBaseInfoArray>();
	SimpleNewsAdapter  simpleNewsAdapter;
	
	
	public SimpleQuestionLoader(Context context,ListView view, ProgressBar progressBar, DisplayImageOptions options,RelativeLayout errLt, ActionBar ab){
		this.listView=view;												//gridview
		this.context=context;											//context
		this.progressBar=progressBar;									//progressBar
		this.options=options;											//ÐœÐ°ÑÑÑÐŸÐ¹ÐºÐž Ð·Ð°Ð³ÑÑÐ·ÐºÐž ÐºÐ°ÑÑÐžÐœÐŸÐº
		this.errLt=errLt;
		this.ab=ab;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
	    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}
	
	public void  settingUpStringArray(ArrayList<CommentsBaseInfoArray> commentsBaseInfoArray, String html, String title, int posX){
		this.commentsBaseInfoArray=commentsBaseInfoArray;
		this.html=html;
		this.title=title;
		
		int index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
		SettingUpAdapter();
		listView.setSelectionFromTop(index, top);
		listView.setSelection(posX);
	}
	
	public void SettingUpAdapter(){
		final Runnable updater = new Runnable() {
		    public void run() {
		    	
		    	View headerView = inflater.inflate(R.layout.header_view_simple_news, null, false);	//setting up header view and update news text
		    	TextView questTitle = (TextView)headerView.findViewById(R.id.textView1);
		    	TextView TextView = (TextView)headerView.findViewById(R.id.newsContent);
		    	Spanned text = Html.fromHtml (html,new URLImageParser(TextView, context), null);
		    	TextView.setText(text);
		    	questTitle.setText(context.getResources().getString(R.string.question));
//		    	TextView.setMovementMethod(new LinkMovementMethod(){}.getInstance());
		    	TextView.setLinksClickable(true);
		    	TextView.setAutoLinkMask(Linkify.ALL); //to open links
		    																						//set header to list of comments
		    	listView.addHeaderView(headerView);
		    	
		    	simpleNewsAdapter = new SimpleNewsAdapter(context, commentsBaseInfoArray, options);
        		listView.setAdapter(simpleNewsAdapter);
        		
        		listView.setOnScrollListener(new OnScrollListener(){	//to impove scrool perfomance
	      			@Override
	      			public void onScrollStateChanged(AbsListView view, int scrollState) {
	      				switch (scrollState) {							//to impove scrool perfomance
	      				case OnScrollListener.SCROLL_STATE_IDLE:
	      					imageLoader.resume();
	      					break;
	      				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
	      						imageLoader.pause();
	      					break;
	      				case OnScrollListener.SCROLL_STATE_FLING:
	      						imageLoader.pause();
	      					break;
	      			}
	      			}
	      			@Override
	      			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {}
	      		});
        		
        		ab.setTitle(title);
		    	
		    	progressBar.setVisibility(View.GONE);
	        	listView.setVisibility(View.VISIBLE);
		    }
		};
		handler.post(updater);
	}
	
	public void LoadSimpleNews(final String url){
		this.url=url;
		thr=new Thread(new Runnable() {				//Äåëàåì â íîâîì ïîòîêå
	        public void run() {
	        	try {
	        		imageLoader.pause();			//pause to kill lags
	        		
	        		Log.i(LOG_TAG, "StartLoadingSimpleNews page:"+url);
	        		
					doc =  Jsoup.connect(url)
								.userAgent(context.getResources().getString(R.string.user_agent))
								.timeout(context.getResources().getInteger(R.integer.user_timeout))
								//.cookie("remember", sPref.getString("remember", null))
								.get();
					
					html = doc.select("div[style=color: #2b6989; margin: 10px 0 35 0px;]").html();
					title = doc.select("div[style=color: #2b6989; margin: 10px 0 35 0px;]").text();
					
					for (Element table : doc.select("div[class=border_line][style=background: #fffbef; border: 1px solid #ccc; border-radius: 7px; padding: 20px; overflow: hidden;]")){
						commentsBaseInfoArray.add(new CommentsBaseInfoArray(
								table.select("p[style=margin: 0;]").text(),			//comment
								table.select("a").first().text(),					//auo
								"http://www.astronews.ru/"+table.select("img").first().attr("src"),			//img
								"http://www.astronews.ru/"+table.select("a").first().attr("href"),				//auo link
								table.select("span[style=float: right;]").last().child(1).text().replace(" ", "")	//rate2
								));
					}
					
					SettingUpAdapter();
					
					imageLoader.resume();
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
	
	public void Error(){ //Îøèáêà ïðè çàãðóçêå
		final Runnable updater = new Runnable() {
	    public void run() {
	    	error=1;
	    	errLt.setVisibility(View.VISIBLE);
	    	listView.setVisibility(View.GONE);
	    	progressBar.setVisibility(View.GONE);
	    	//êíîïêà ïîïðîáîâàòü ñíîâà
	    	Button retry = (Button)errLt.findViewById(R.id.retry);
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
	    	listView.setVisibility(View.GONE);
	    	progressBar.setVisibility(View.VISIBLE);
	    	LoadSimpleNews(url);
	    }};
		handler.post(updater);
	}
	
	public ArrayList<CommentsBaseInfoArray>  returnStringArray(){
		return commentsBaseInfoArray;
	}
	
	public int  returnPosX(){
		return  listView.getFirstVisiblePosition();
	}
	
	public String returnHtml(){
		return html;
	}
	
	public String returnTitle(){
		return title;
	}
	
}