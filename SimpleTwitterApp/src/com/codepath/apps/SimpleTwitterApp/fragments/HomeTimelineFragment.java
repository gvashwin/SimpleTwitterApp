package com.codepath.apps.SimpleTwitterApp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.activities.DetailedTweetViewActivity;
import com.codepath.apps.SimpleTwitterApp.adapters.TweetsArrayAdapter;
import com.codepath.apps.SimpleTwitterApp.listeners.EndlessScrollListener;
import com.codepath.apps.SimpleTwitterApp.models.Tweet;
import com.codepath.apps.SimpleTwitterApp.utils.NetworkUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragment {
	
	
	NetworkUtil netUtil = NetworkUtil.getInstance();
	JsonHttpResponseHandler reponseHandler = new JsonHttpResponseHandler(){
			
			@Override
		protected void handleFailureMessage(Throwable arg0, String arg1) {
			// TODO Auto-generated method stub
			super.handleFailureMessage(arg0, arg1);
		}

		@Override
		protected void handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			super.handleMessage(arg0);
		}

		@Override
		protected void handleSuccessJsonMessage(int arg0, Object arg1) {
			// TODO Auto-generated method stub
			super.handleSuccessJsonMessage(arg0, arg1);
		}

		@Override
		public void onFailure(Throwable arg0, JSONArray arg1) {
			// TODO Auto-generated method stub
			super.onFailure(arg0, arg1);
		}

		@Override
		public void onFailure(Throwable arg0, JSONObject arg1) {
			// TODO Auto-generated method stub
			super.onFailure(arg0, arg1);
		}

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
				TwitterApplication.lastLoadedTweet = tweets.get(tweets.size()-1).getUid();
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
		View v = super.onCreateView( inflater, container, savedInstanceState);
		setupListeners();
		fillTimeLineWithTweets();
		return v;
	}
	
	
	private void setupListeners() {
		lvTweets.setOnScrollListener(new EndlessScrollListener(){
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMoreTweets();
			}
			
		});
		
		lvTweets.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Tweet t = tweets.get(position);
				Intent  i = new Intent(view.getContext(),DetailedTweetViewActivity.class);
				i.putExtra("tweetId", t.getUid());
				i.putExtra("tweetText", t.getText());
				i.putExtra("timeStamp", t.getTimeStamp());
				i.putExtra("retweetCount", t.getRetweetCount());
				i.putExtra("favCount", t.getFavCount());
				i.putExtra("authorName",t.getAuthor().getName());
				i.putExtra("authorHandle",t.getAuthor().getHandle());
				i.putExtra("authorImgUrl", t.getAuthor().getImgUrl());
				i.putExtra("mediaUrl",t.getMediaUrl());
				i.putExtra("authorVerified", t.getAuthor().isVerified());
				startActivity(i);
			}
			
		});
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
		    @Override
		    public void onRefresh() {
		    	aTweets.clear();
		    	aTweets.notifyDataSetChanged();
		    	fillTimeLineWithTweets();
		    	lvTweets.onRefreshComplete();
		    }
		});
	}
	
	public void showUserProfile(View v) {
		Toast.makeText(getActivity(), "showUserProfile fn in HomeTimeLineFrgment", Toast.LENGTH_SHORT).show();
	}
	
	public void fillTimeLineWithTweets() {
		
		if(!netUtil.isNetworkAvailable(getActivity())) {
			Toast.makeText(getActivity(), "Network Unavailable", Toast.LENGTH_SHORT).show();
		} else {
			aTweets.clear();
			aTweets.notifyDataSetChanged();
			Log.i("debug","Filling timeline with tweets");
			TwitterApplication.lastLoadedTweet = null;
			client.getHomeTimeline(null,reponseHandler);
		}
		
	}
	
	protected void loadMoreTweets() {
		if(!netUtil.isNetworkAvailable(getActivity())) {
			Toast.makeText(getActivity(), "Network Unavailable", Toast.LENGTH_SHORT).show();
		} else {
			Log.i("debug","LOADING MORE TWEETS"+TwitterApplication.lastLoadedTweet);
			if(TwitterApplication.lastLoadedTweet!=null) {
				client.getHomeTimeline(TwitterApplication.lastLoadedTweet, reponseHandler);
			}
		}
		
	}

}
