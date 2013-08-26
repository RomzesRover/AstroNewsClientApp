package com.BBsRs.SlidingMenu;



import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BBsRs.astronews.R;



public class SlidingMenuAdapter extends ArrayAdapter<String> {

	 private final Context context;
	    String[] names;
	    boolean[] checked;
	    boolean f1;
	    //TypedArray imgs;
	    public SlidingMenuAdapter(Context context,  String[] names, boolean[] checked) {
	        super(context, R.layout.row, names);
	        this.context = context;
	        this.names=names;
	        this.checked=checked;
	        //imgs = context.getResources().obtainTypedArray(R.array.MainViewerTabs);
	    }
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	    	if ((position!=0) && (position!=2) && (position!=4)&& (position!=19)){
	        LayoutInflater inflater = (LayoutInflater) context
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View rowView = inflater.inflate(R.layout.row, parent, false);
	        TextView textName = (TextView) rowView.findViewById(R.id.row_title);
	        ImageView imageView = (ImageView)rowView.findViewById(R.id.row_icon);
	        
	        textName.setText(names[position]);					//������ �����
        	//imageView.setBackgroundResource(imgs.getResourceId(position, -1));
	        if (checked[position]){
	        	//rowView.setBackground(context.getResources().getDrawable(R.drawable.bg_drawer_selected));
	        	rowView.setBackgroundResource(R.drawable.bg_drawer_selected);
	        	//rowView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.list_selector_holo_checked));
	        	f1=true;
	        }
	        return rowView;} 
	    	else {
	    		LayoutInflater inflater = (LayoutInflater) context
		                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        View rowView = inflater.inflate(R.layout.row_top, parent, false);
		        TextView textName = (TextView) rowView.findViewById(R.id.row_title);
		        textName.setText(names[position]);					//������ �����
		        return rowView;	
	    	}
	    }
	    
	    public void onSetChecked(int position, int lastSelected){
	    	if (position!=lastSelected && position!=-1 && lastSelected!=-1){
	    	checked[position]=true;
	    	checked[lastSelected]=false;
	    	notifyDataSetChanged();
	    	}
	    }
	    
	 

}
