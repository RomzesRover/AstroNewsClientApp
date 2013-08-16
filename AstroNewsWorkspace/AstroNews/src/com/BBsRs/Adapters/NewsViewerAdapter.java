package com.BBsRs.Adapters;



import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BBsRs.astronews.NewsBaseInfoArray;
import com.BBsRs.astronews.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;



				//Adapter where extends BaseAdapter for android
public class NewsViewerAdapter extends BaseAdapter {

	 	ArrayList<NewsBaseInfoArray> videoBaseInfoArray = new ArrayList<NewsBaseInfoArray>();
	    LayoutInflater inflater;
	    DisplayImageOptions options;
	    ImageLoader imageLoader;
	    public NewsViewerAdapter(Context context, ArrayList<NewsBaseInfoArray> videoBaseInfoArray,DisplayImageOptions options, ImageLoader imageLoader) {
	        this.videoBaseInfoArray=videoBaseInfoArray;
	        this.options=options;
	        this.imageLoader = imageLoader;
	        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	    
	    // ViewHolder we use viewHolder to improve speed when we scroll list or gridview, thx to this method an user from habrhabr
	    // Here we desc all kind of views wich we use in our simple item
	    static class ViewHolder {
	        public ImageView imageView;
	        public TextView textName;
	        public TextView textAuthors;
	        public TextView textCom;
	        public TextView textRate;
	    }
	    
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	    	
	    	// ViewHolder we use viewHolder to improve speed when we scroll list or gridview, thx to this method an user from habrhabr
	        ViewHolder holder;
	        View rowView = convertView;
	        if (rowView == null) {
	            rowView = inflater.inflate(R.layout.ic_simple_news, parent, false);
	            holder = new ViewHolder();
	            holder.textName = (TextView) rowView.findViewById(R.id.title);
	            holder.textAuthors = (TextView) rowView.findViewById(R.id.textAuo);
	            holder.textCom = (TextView) rowView.findViewById(R.id.textCom);
	            holder.textRate = (TextView) rowView.findViewById(R.id.textRate);
	            holder.imageView = (ImageView)rowView.findViewById(R.id.imageSrc);
	            rowView.setTag(holder);
	        } else {
	            holder = (ViewHolder) rowView.getTag();
	        }
	        NewsBaseInfoArray p = videoBaseInfoArray.get(position);	//get the data to current visible item
	        
	        holder.textName.setText(p.name);						//setting up text data
	        holder.textAuthors.setText(p.author);
	        holder.textCom.setText(p.comments);
	        holder.textRate.setText(p.rating);
	        														//sett up image data
	        imageLoader.displayImage(p.imgSrc, holder.imageView, options);
	        //return the view what we have right now
	        return rowView;
	    }
		@Override
		public int getCount() {
			return videoBaseInfoArray.size();
		}
		@Override
		public Object getItem(int position) {
			 return videoBaseInfoArray.get(position);
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public void add(ArrayList<NewsBaseInfoArray> videoBaseInfoArrayNew)
		{
			videoBaseInfoArray = videoBaseInfoArrayNew;
		    notifyDataSetChanged();
		}
	    
	 

}
