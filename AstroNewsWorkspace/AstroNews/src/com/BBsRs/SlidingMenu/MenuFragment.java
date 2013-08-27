package com.BBsRs.SlidingMenu;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.BBsRs.Fragments.NewsFragment;
import com.BBsRs.Fragments.QuestionsFragment;
import com.BBsRs.astronews.MainActivity;
import com.BBsRs.astronews.R;

public class MenuFragment extends ListFragment {
	
	int currentSelected=1;
	SlidingMenuAdapter adapter;
	SharedPreferences sPref;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] titles = getResources().getStringArray(R.array.MainViewerTabs);
		sPref = getActivity().getSharedPreferences("A", 1);
		//titles[3] = sPref.getString("username", "");
		boolean[] checked = new boolean[titles.length];
		
		try {
			Log.i("CurrentSelectedForMenuSliding", String.valueOf("one two three"));
		currentSelected = numberCurrentFromTitle(String.valueOf(getActivity().getTitle()));
		}
		catch (Exception e) {
			currentSelected=-1;
			Log.d("CurrentSelectedForMenuSliding", "CurrentSelectedUnnavailable");
			e.printStackTrace();
			
			//nuller(false, true);
		}
		if (currentSelected!=-1)
		checked[currentSelected]=true;
		
		adapter = new SlidingMenuAdapter(getActivity(), titles,checked);
		setListAdapter(adapter);
	}

	public int numberCurrentFromTitle (String arg1){
		String[] arg2 = getActivity().getResources().getStringArray(R.array.MainViewerTabs);
		//arg2[3] = sPref.getString("username", "");
		for (int i=0; i<=arg2.length; i++){
			if (arg1.equals(arg2[i])){
				Log.i("CurrentSelectedForMenuSliding", String.valueOf(i));
				return i;
			}
		}
		return 1;
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;

		if ((position!=0) && (position!=3) && (position!=5)){
		adapter.onSetChecked(position, currentSelected);
		currentSelected=position;
		}
		String title = getActivity().getResources().getStringArray(R.array.MainViewerTabs)[position];
		if (position==3)
			title = sPref.getString("username", "");
		
		switch (position) {
		case 1:
			newContent = new NewsFragment();
			break;
		case 2:
			newContent = new QuestionsFragment();
		}
		if (newContent != null)
			switchFragment(newContent, title);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment, String Id) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment, Id);
	}}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		 outState.putInt("currentSelected", currentSelected);
	}

}
