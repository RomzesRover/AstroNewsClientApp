package com.BBsRs.astronews;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.TextView;

public class URLImageParser implements ImageGetter {
    Context c;
    TextView container;

    /***
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     * @param t
     * @param c
     */
    public URLImageParser(TextView t, Context c) {
        this.c = c;
        this.container = t;
    }

    public Drawable getDrawable(String source) {
        URLDrawable urlDrawable = new URLDrawable();

        // get the actual source
        ImageGetterAsyncTask asyncTask = 
            new ImageGetterAsyncTask( urlDrawable);

        asyncTask.execute(source);

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable>  {
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable d) {
            this.urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override 
        protected void onPostExecute(Drawable result) { 
            // set the correct bound according to the result from HTTP call 
        	float scale = (float) URLImageParser.this.container.getWidth() / (float) result.getIntrinsicWidth();
        	if (scale == 0) scale = 1;
        	
            urlDrawable.setBounds(0, 0, (int)URLImageParser.this.container.getWidth(), (int)(scale * result.getIntrinsicHeight()));  

            // change the reference of the current drawable to the result 
            // from the HTTP call 
            urlDrawable.drawable = result; 

            // redraw the image by invalidating the container 
            URLImageParser.this.container.invalidate();

            // For ICS
            URLImageParser.this.container.setHeight((URLImageParser.this.container.getHeight() 
            + (int)(scale * result.getIntrinsicHeight())));

            // Pre ICS
            URLImageParser.this.container.setEllipsize(null);
            
        } 

        /***
         * Get the Drawable from URL
         * @param urlString
         * @return
         */
        public Drawable fetchDrawable(String urlString) {
            try {
                InputStream is = fetch(urlString);
                Drawable drawable = Drawable.createFromStream(is, "src");
                
                float scale = (float) URLImageParser.this.container.getWidth() / (float) drawable.getIntrinsicWidth();
                if (scale == 0) scale = 1;
                
                drawable.setBounds(0, 0,(int)URLImageParser.this.container.getWidth(), (int)(scale * drawable.getIntrinsicHeight())); 
                return drawable;
            } catch (Exception e) {
            	try {
					Drawable d =Drawable.createFromStream(c.getAssets().open("error.png"), null);
					Log.i("URLImageParser", "error png loaded");
					
					float scale = (float) URLImageParser.this.container.getWidth() / (float) d.getIntrinsicWidth();
	                if (scale == 0) scale = 1;
	                d.setBounds(0, 0,(int)URLImageParser.this.container.getWidth(), (int)(scale * d.getIntrinsicHeight())); 
					return d;
				} catch (IOException e1) {
					Log.e("URLImageParser", "Unexcepted error");
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
                return null;
            } 
        }

        private InputStream fetch(String urlString) throws MalformedURLException, IOException {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);
            return response.getEntity().getContent();
        }
    }
    
    @SuppressWarnings("deprecation")
	public class URLDrawable extends BitmapDrawable {
        // the drawable that you need to set, you could set the initial drawing
        // with the loading image if you need to
        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if(drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
}