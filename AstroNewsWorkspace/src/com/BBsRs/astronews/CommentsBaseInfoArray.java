/*Class to desc all data about comments on simple news page!
  Include: name, img path, author's nickname, number of comments, current rating by user's, and url link to full news page
  Also here supported writeToParcel to save this data after rotate screen on all of the devices!
  Author Roman Gaitbaev writed for AstroNews.ru 
  http://vk.com/romzesrover 
  Created: 18.08.2013 00:58*/

package com.BBsRs.astronews;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentsBaseInfoArray implements Parcelable {
  
  public String Comment, Name, imgsSrc, auoLink, rate;
  

  public CommentsBaseInfoArray(String _Comments, String _NamesDates, String _imgsSrc, String _auoLink, String _rate) {
	  Comment = _Comments;
	  Name = _NamesDates;
	  imgsSrc = _imgsSrc;
	  auoLink=_auoLink;
	  rate=_rate;
  }


@Override
public int describeContents() {
	return 0;
}

private CommentsBaseInfoArray(Parcel in) {
	Comment = in.readString();
	Name= in.readString();
	imgsSrc = in.readString();
	auoLink = in.readString();
	rate = in.readString();
}

@Override
public void writeToParcel(Parcel out, int flags) {
	 out.writeString(Comment);
     out.writeString(Name);
     out.writeString(imgsSrc);
     out.writeString(auoLink);
     out.writeString(rate);
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