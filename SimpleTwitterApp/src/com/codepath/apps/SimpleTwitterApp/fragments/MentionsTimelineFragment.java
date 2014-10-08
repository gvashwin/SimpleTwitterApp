package com.codepath.apps.SimpleTwitterApp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.activities.DetailedTweetViewActivity;
import com.codepath.apps.SimpleTwitterApp.listeners.EndlessScrollListener;
import com.codepath.apps.SimpleTwitterApp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class MentionsTimelineFragment extends TweetsListFragment {
	
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
			Log.i("debug","Home Timeline "+jsonResponseArray.toString());
			pb.setVisibility(ProgressBar.INVISIBLE);
			Log.i("debug","Return Status Code"+statusCode);
			Log.i("debug",jsonResponseArray.toString());
			ArrayList<Tweet> tweets = Tweet.fromJsonArray(jsonResponseArray);
			Log.i("debug",tweets.toString());
			aTweets.addAll(tweets);
			aTweets.notifyDataSetChanged();
			TwitterApplication.lastLoadedMention = tweets.get(tweets.size()-1).getUid();
		}
		
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		setupListeners();
		fillTimelineWithMentions();
		return v;
	}
	
	private void setupListeners() {
		lvTweets.setOnScrollListener(new EndlessScrollListener(){
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMoreMentions();
			}
		});
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
		    @Override
		    public void onRefresh() {
		    	aTweets.clear();
		    	aTweets.notifyDataSetChanged();
		    	fillTimelineWithMentions();
		    	lvTweets.onRefreshComplete();
		    }
		});
	}
	
	protected void loadMoreMentions() {
			client.getMentionsTimeline(TwitterApplication.lastLoadedMention,reponseHandler);
	}

	public void fillTimelineWithMentions() {
		Log.i("debug", "Filling timeline with mentions");
		aTweets.clear();
		aTweets.notifyDataSetChanged();
		TwitterApplication.lastLoadedMention = null;
		client.getMentionsTimeline(null,reponseHandler);
	}
}
