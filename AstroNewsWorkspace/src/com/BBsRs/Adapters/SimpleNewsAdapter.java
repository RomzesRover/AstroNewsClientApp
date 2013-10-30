package com.BBsRs.Adapters;



import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BBsRs.astronews.CommentsBaseInfoArray;
import com.BBsRs.astronews.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;



public class SimpleNewsAdapter extends BaseAdapter  {

	LayoutInflater inflater;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    Context context;
	    ArrayList<CommentsBaseInfoArray> commentBaseInfoArray;
	    
	    public SimpleNewsAdapter(Context context, ArrayList<CommentsBaseInfoArray> commentBaseInfoArray  ,DisplayImageOptions options) {
	    	this.context=context;
	        this.commentBaseInfoArray=commentBaseInfoArray;
	        this.options=options;
	        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        imageLoader = ImageLoader.getInstance();
			// Initialize ImageLoader with configuration. Do it once.
	        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	        
	    }
	    
	 // Класс для сохранения во внешний класс и для ограничения доступа
	    // из потомков класса
	    static class ViewHolder {
	        public ImageView imageView;
	        public TextView textName;
	        public TextView textComment;
	        public TextView textRate;
	    }
	    
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	    	
	    	// ViewHolder буферизирует оценку различных полей шаблона элемента
	        ViewHolder holder;
	        View rowView = convertView;
	        if (rowView == null) {
	            rowView = inflater.inflate(R.layout.ic_element_comment_simple, parent, false);
	            holder = new ViewHolder();
	            holder.textName = (TextView) rowView.findViewById(R.id.textName);
	            holder.textComment = (TextView) rowView.findViewById(R.id.textComment);
	            holder.textRate = (TextView) rowView.findViewById(R.id.textRate);
	            holder.imageView = (ImageView)rowView.findViewById(R.id.imageSrc);
	            rowView.setTag(holder);
	        } else {
	            holder = (ViewHolder) rowView.getTag();
	        }
	        
	        final CommentsBaseInfoArray p = commentBaseInfoArray.get(position);
	        
	        holder.imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					context.startActivity(new Intent("com.BBsRs.astronews.UserViewer", Uri.parse(p.auoLink)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
				}
			});
	        
	        holder.textName.setText(p.Name);					//������ �����
	        Spanned text = Html.fromHtml (p.Comment);
	        holder.textComment.setText(text);
	        holder.textRate.setText(p.rate);
	        holder.textRate.setBackgroundColor(context.getResources().getColor(colorBy(Integer.parseInt(p.rate.replace("+", "")))));
																//��������� ��������
	        imageLoader.displayImage(p.imgsSrc, holder.imageView, options);
	        return rowView;
	    }
	    
	    public int colorBy(int arg1){
	    	
	    	int color= R.color.green;
	    	
	    	switch (arg1){
	    	case -5: case-4: case-3:
	    		color = R.color.red;
	    		break;
	    	case -2: case-1: case 0: case 1: case 2:
	    		color = R.color.yellow;
	    		break;
	    	case 3: case 4: case 5:
	    		color = R.color.green;
	    		break;
	    	}
	    	
			return color;
	    }
	    
            @Override
    		public int getCount() {
    			return commentBaseInfoArray.size();
    		}
    		@Override
    		public Object getItem(int position) {
    			 return commentBaseInfoArray.get(position);
    		}
    		@Override
    		public long getItemId(int position) {
    			// TODO Auto-generated method stub
    			return position;
    		}
	    
	    	

}
