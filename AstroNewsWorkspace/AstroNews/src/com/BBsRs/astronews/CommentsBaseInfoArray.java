/*Class to desc all data about news and search page!
  Include: name, img path, author's nickname, number of comments, current rating by user's, and url link to full news page
  Also here supported writeToParcel to save this data after rotate screen on all of the devices!
  Author Roman Gaitbaev writed for AstroNews.ru 
  http://vk.com/romzesrover 
  Created: 20.07.2013 21:14*/

package com.BBsRs.astronews;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentsBaseInfoArray implements Parcelable {
  
  public String name, imgSrc,author, comments, rating, url;
  

  public CommentsBaseInfoArray(String _name, String _imgSrc,String _author,String _comments, String _rating, String _url) {
	  name = _name;
	  imgSrc = _imgSrc;
	  author = _author;
	  comments = _comments;
	  rating = _rating;
	  url=_url;
  }


@Override
public int describeContents() {
	return 0;
}

private CommentsBaseInfoArray(Parcel in) {
    name = in.readString();
	imgSrc = in.readString();
	author = in.readString();
	comments = in.readString();
	rating = in.readString();
	url = in.readString();
}

@Override
public void writeToParcel(Parcel out, int flags) {
	 out.writeString(name);
     out.writeString(imgSrc);
     out.writeString(author);
     out.writeString(comments);
     out.writeString(rating);
     out.writeString(url);
}

public static final Parcelable.Creator<CommentsBaseInfoArray> CREATOR = new Parcelable.Creator<CommentsBaseInfoArray>() {
    public CommentsBaseInfoArray  createFromParcel(Parcel in) {
        return new CommentsBaseInfoArray (in);
    }

    public CommentsBaseInfoArray [] newArray(int size) {
        return new CommentsBaseInfoArray [size];
    }
};
}