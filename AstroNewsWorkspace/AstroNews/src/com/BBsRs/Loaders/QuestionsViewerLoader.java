/*Class to download all news and search from the page!
  Here we Parce and save: name, img path, author's nickname, number of comments, current rating by user's, and url link to full news page
  Also here supported return method's to save this data after rotate screen on all of the devices!
  Author Roman Gaitbaev writed for AstroNews.ru 
  http://vk.com/romzesrover 
  Created: 20.07.2013 23:32*/
package com.BBsRs.Loaders;

import java.io.IOException;
import java.util.ArrayList;

import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.GridView;
import org.holoeverywhere.widget.ProgressBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.BBsRs.Adapters.QuestionsViewerAdapter;
import com.BBsRs.astronews.NewsBaseInfoArray;
import com.BBsRs.astronews.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class QuestionsViewerLoader {
																		//переменные для работы загрузки
	GridView  gridView;
	ProgressBar progressBar;
	Context context;
	Thread thr;
	View v;
	Document doc;
	QuestionsViewerAdapter feedAdapter; 
	final Handler handler = new Handler();
	RelativeLayout errLt;
	int error=0;
	
	String LOG_TAG = "NewNewsLoader";
	
	ArrayList<NewsBaseInfoArray> newsBaseInfoArray = new ArrayList<NewsBaseInfoArray>();
	
	int page=1;															//загружаемая страница,1 - по умолчанию
	Boolean end=false;													//конец или нет..
	DisplayImageOptions options ;
	ImageLoader imageLoader;
	
	SharedPreferences sPref;    										//для настроек
																		//конструктор
	public QuestionsViewerLoader(Context context,GridView view, ProgressBar progressBar,View v,DisplayImageOptions options,RelativeLayout errLt){
		this.gridView=view;												//gridview
		this.context=context;											//context
		this.progressBar=progressBar;									//progressBar
		this.v=v;														//footer для gridView
		this.options=options;											//настройки загрузки картинок
		this.errLt=errLt;
		
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
	    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}
												
	public void settingUpAdapter(final boolean first){					//функция установки адаптера для grid, true=первая страница false=последующие страницы, 
																		//чтобы не обновлять адаптер
		final Runnable updater = new Runnable() {
	        public void run() {
	        	if (first){
//	        		boolean pauseOnScroll = true; // or true
//	        	    boolean pauseOnFling = true; // or false
//	        	    PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling);
//	        	    gridView.setOnScrollListener(listener);
	        															//создаем адаптер
	        	feedAdapter = new QuestionsViewerAdapter(context, newsBaseInfoArray ,options, imageLoader);
	        									
	        	gridView.setAdapter(feedAdapter);						//ставим адаптер
	        													
	        	gridView.setOnScrollListener(new OnScrollListener(){	//метод оnscrool (ловим когда пользаватель прокрутил список до самого низа)
	      			@Override
	      			public void onScrollStateChanged(AbsListView view, int scrollState) {
	      				switch (scrollState) {							//����� �� ������
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
	      			public void onScroll(AbsListView view, int firstVisibleItem,
	    				int visibleItemCount, int totalItemCount) {}
	      		});
	        	
	        	gridView.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
				        context.startActivity(new Intent("com.BBsRs.astronews.SimpleQuestionViewerActivity", Uri.parse(newsBaseInfoArray.get(arg2).url)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					}
				});
	        	
	        	progressBar.setVisibility(View.GONE);
	        	gridView.setVisibility(View.VISIBLE);}
	        	else {
	        		feedAdapter.add(newsBaseInfoArray);
	        	}
	        }
	    };
	    handler.post(updater);
	}
	
	public void nuller(){
		if (end){
		gridView.addFooterView(v);}
		end=false;
	}
	
	public ArrayList<NewsBaseInfoArray>  returnStringArray(){
		return  newsBaseInfoArray;
	}
	
	public int  returnPage(){
		return  page;
	}
	
	public int  returnPosX(){
		return  gridView.getFirstVisiblePosition();
	}
	
	public int returnError(){
		return error;
	}
	
	public void  settingUpStringArray(ArrayList<NewsBaseInfoArray> newsBaseInfoArray, int page,int posX,int error){
		this.newsBaseInfoArray=newsBaseInfoArray;
		this.page=page;
		this.error=error;
		if (error==0){
			settingUpAdapter(true);
		gridView.setSelection(posX);}
			else
			Error();
		
		}
	
	public void end(){ //����� ��������� ������� �� �����, �� ������ �����
		Log.d("End_FeedLoader", "StartLoadingFeed Page:"+String.valueOf(page));
		final Runnable updater = new Runnable() {
	    public void run() {
		end=true;
		gridView.removeFooterView(v);
	        }
		};
		handler.post(updater);
	}
	
	public void Error(){ //������ ��� ��������
		final Runnable updater = new Runnable() {
	    public void run() {
	    	error=1;
	    	errLt.setVisibility(View.VISIBLE);
	    	gridView.setVisibility(View.GONE);
	    	progressBar.setVisibility(View.GONE);
	    	//������ ����������� �����
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
	    	gridView.setVisibility(View.GONE);
	    	progressBar.setVisibility(View.VISIBLE);
	    	LoadPageOfNews(1);
	    }};
		handler.post(updater);
	}
	
	public void pagePlusPlus(){
		page++;
	}
	
	public void LoadPageOfNews(final int pageN){
		thr=new Thread(new Runnable() {				//������ � ����� ������
	        public void run() {
	        	try {
	        		imageLoader.pause();			//pause to kill lags
	        		
	        		Log.i(LOG_TAG, "StartLoadingFeed Page:"+String.valueOf(pageN));
	        		
	        		sPref = context.getSharedPreferences("A", 1);
					doc =  Jsoup.connect("http://www.astronews.ru/cgi-bin/mng.cgi?page=question")
								.userAgent(context.getResources().getString(R.string.user_agent))
								.timeout(context.getResources().getInteger(R.integer.user_timeout))
								//.cookie("remember", sPref.getString("remember", null))
								.get();
					
					for (Element row : doc.select("table[bgcolor=FEFFD5]")) {
					        newsBaseInfoArray.add(new NewsBaseInfoArray(
					        		row.select("a[href]").get(1).text(),								//title
					        		"http://www.astronews.ru/"+row.select("img").attr("src"),			//img
					    			row.select("a[href]").get(row.select("a[href]").size()-2).text(),	//auo
					    			row.select("b").get(row.select("b").size()-4).text(),				//com
					    			row.select("b").last().text(),										//rate
					    			"http://www.astronews.ru/cgi-bin/mng.cgi"+row.select("a").get(1).attr("href")										//url
					    			));
					}
						settingUpAdapter(true);
			    		end();
					imageLoader.resume();
				} catch (IOException e) {
					Error();
					Log.e(LOG_TAG, "Page: "+String.valueOf(pageN));
					e.printStackTrace();
				}
	        }
	    });
		
		thr.start();
	}
}
