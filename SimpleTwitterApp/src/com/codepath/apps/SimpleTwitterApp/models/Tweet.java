package com.codepath.apps.SimpleTwitterApp.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Tweet {
	private String text;
	private String uid;
	private String timeStamp;
	private Profile author;
	private int retweetCount;
	private int favCount;
	private String mediaUrl;
	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Profile getAuthor() {
		return author;
	}

	public void setAuthor(Profile author) {
		this.author = author;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public int getFavCount() {
		return favCount;
	}

	public void setFavCount(int favCount) {
		this.favCount = favCount;
	}

	public String getTweetText() {
		return text;
	}

	public void setTweetText(String tweetText) {
		this.text = tweetText;
	}
	
	public String toString() {
		return getTweetText();
	}
	public static Tweet fromJsonObject(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		try {
			tweet.text = jsonObject.getString("text");
			tweet.uid = jsonObject.getString("id_str");
			tweet.timeStamp = jsonObject.getString("created_at");
			tweet.author = Profile.fromJsonObject(jsonObject.getJSONObject("user"));
			tweet.retweetCount = jsonObject.getInt("retweet_count");
			tweet.favCount = jsonObject.getInt("favorite_count");
			if(!jsonObject.getJSONObject("entities").isNull("media")){
                JSONArray array = jsonObject.getJSONObject("entities").getJSONArray("media");
                tweet.mediaUrl = array.getJSONObject(0).getString("media_url");
                Log.i("debug","Media url set "+ tweet.mediaUrl);
            } else {
            	tweet.mediaUrl = null;
            }
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return tweet;
	}
	
	public static ArrayList fromJsonArray(
			JSONArray jsonResponseArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		JSONObject tweetObject;
		for(int i=0; i<jsonResponseArray.length(); i++) {
			tweetObject = null;
			try {
				tweetObject = jsonResponseArray.getJSONObject(i);	
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			Tweet t = Tweet.fromJsonObject(tweetObject);
			if(t != null) {
				tweets.add(t);
			}
			
		}
		return tweets;
	}
}
