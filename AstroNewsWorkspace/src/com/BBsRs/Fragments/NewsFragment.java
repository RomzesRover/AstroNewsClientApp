package com.BBsRs.Fragments;

import java.util.ArrayList;

import org.holoeverywhere.widget.GridView;
import org.holoeverywhere.widget.ProgressBar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.BBsRs.Loaders.NewsViewerLoader;
import com.BBsRs.astronews.NewsBaseInfoArray;
import com.BBsRs.astronews.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class NewsFragment extends Fragment {
	
	NewsViewerLoader newsLoader; 
	GridView gridList;
	ProgressBar progressBar;
	DisplayImageOptions options ;
	SharedPreferences sPref; 
	ArrayList<NewsBaseInfoArray> newsBaseInfoArray = new ArrayList<NewsBaseInfoArray>();
	int page=1,posX=0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main_viewer, null);
		
		options = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.ic_stub)
        //.showImageForEmptyUri(R.drawable.logo)
        .cacheOnDisc(true)	
        .cacheInMemory(true)						//�������� �� ������, ����� ����������
        .build();
        
		final RelativeLayout errLt = (RelativeLayout)view.findViewById(R.id.ErrorRelativeLayout); //��� ������
        gridList = (GridView)view.findViewById(R.id.NewsGridView);
	    progressBar = (ProgressBar)view.findViewById(R.id.NewsProgressBar);
	    View footerView = inflater.inflate(R.layout.ic_element_simple_news_footer, null);
	    gridList.addFooterView(footerView, null, false);
	    newsLoader = new NewsViewerLoader(getActivity(), gridList, progressBar,footerView, options,errLt);
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
	    
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		 outState.putParcelableArrayList("newsBaseInfoArray", newsLoader.returnStringArray());
		 outState.putInt("page", newsLoader.returnPage());
		 outState.putInt("posX",  newsLoader.returnPosX());
		 outState.putInt("error", newsLoader.returnError());
	}
	
}
