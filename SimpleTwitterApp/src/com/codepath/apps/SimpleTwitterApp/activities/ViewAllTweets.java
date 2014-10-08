package com.codepath.apps.SimpleTwitterApp.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.TwitterClient;
import com.codepath.apps.SimpleTwitterApp.R.layout;
import com.codepath.apps.SimpleTwitterApp.adapters.TweetsArrayAdapter;
import com.codepath.apps.SimpleTwitterApp.listeners.EndlessScrollListener;
import com.codepath.apps.SimpleTwitterApp.models.Tweet;
import com.codepath.apps.SimpleTwitterApp.utils.NetworkUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ViewAllTweets extends Activity {
	ActionBar actionBar;
	ListView lvTweets;
	String lastLoadedTweetId;
	NetworkUtil  netUtil = NetworkUtil.getInstance();
	private TwitterClient client = TwitterApplication.getRestClient();
	ArrayList<Tweet> tweets;
	TweetsArrayAdapter aTweets;
	ProgressBar pb;
	String profileId;
	JsonHttpResponseHandler reponseHandler = new JsonHttpResponseHandler(){
		
		@Override
		public void onStart() {
			pb.setVisibility(ProgressBar.VISIBLE);
			super.onStart();
		}

		@Override
		public void onFailure(Throwable e, String s) {
			Log.i("debug",e.toString());
			Log.i("debug",s.toString());
		}

		@Override
		public void onSuccess(int statusCode, JSONArray jsonResponseArray) {
			pb.setVisibility(ProgressBar.INVISIBLE);
			Log.i("debug","Return Status Code"+statusCode);
			Log.i("debug",jsonResponseArray.toString());
			ArrayList<Tweet> tweets = Tweet.fromJsonArray(jsonResponseArray);
			lastLoadedTweetId = tweets.get(tweets.size()-1).getUid();
			Log.i("debug",tweets.toString());
			aTweets.addAll(tweets);
			aTweets.notifyDataSetChanged();
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tweets_list);
		Bundle extras = getIntent().getExtras();
		profileId = new String(extras.getString("id"));
		setupActionBar();
		// Creating a Array List to hold tweet objects
		tweets = new ArrayList<Tweet>(); 
		// Creating a Adapter to for the tweets
		aTweets = new TweetsArrayAdapter(this, tweets);
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		
		pb = (ProgressBar) findViewById(R.id.pbTimeline);
		pb.setVisibility(ProgressBar.INVISIBLE);
		setupListenerts();
		fillTimeLineWithTweets();
	}

	private void setupListenerts() {
		lvTweets.setOnScrollListener(new EndlessScrollListener(){
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMoreTweets();
			}
			
		});
		
	}

	private void setupActionBar() {
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(TwitterApplication.D_BLUE_COLOR);
		actionBar.setStackedBackgroundDrawable(TwitterApplication.D_WHITE_COLOR);
		actionBar.setIcon(R.drawable.ic_actionbar_logo);
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Tweets</font>"));
		
	}
	
	private void loadMoreTweets() {
		if(!netUtil.isNetworkAvailable(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
		} else {
			client.getAccountRecentTweets(profileId,null, lastLoadedTweetId,reponseHandler);
		}
	}
	public void fillTimeLineWithTweets() {
		if(!netUtil.isNetworkAvailable(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
		} else {
			aTweets.clear();
			aTweets.notifyDataSetChanged();
			client.getAccountRecentTweets(profileId,null,null,reponseHandler);
		}
	}
}
