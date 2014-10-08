package com.codepath.apps.SimpleTwitterApp.fragments;

import java.util.ArrayList;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.TwitterClient;
import com.codepath.apps.SimpleTwitterApp.activities.DetailedTweetViewActivity;
import com.codepath.apps.SimpleTwitterApp.activities.ProfileViewActivity;
import com.codepath.apps.SimpleTwitterApp.adapters.TweetsArrayAdapter;
import com.codepath.apps.SimpleTwitterApp.listeners.EndlessScrollListener;
import com.codepath.apps.SimpleTwitterApp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TweetsListFragment extends Fragment {
	
	PullToRefreshListView lvTweets;
	TwitterClient client;
	ArrayList<Tweet> tweets;
	TweetsArrayAdapter aTweets;
	ProgressBar pb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Creating a Array List to hold tweet objects
		tweets = new ArrayList<Tweet>(); 
		// Creating a Adapter to for the tweets
		aTweets = new TweetsArrayAdapter(getActivity(), tweets);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	
			Bundle savedInstanceState) {
		// Inflating Layout
		View v = inflater.inflate(R.layout.fragment_tweets_list,container, false);
		lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		
		pb = (ProgressBar) v.findViewById(R.id.pbTimeline);
		pb.setVisibility(ProgressBar.INVISIBLE);
		return v;
	}
	
	public void showUserProfile(View v) {
		Toast.makeText(getActivity(), "showUserProfile fn in TweetListFragment", Toast.LENGTH_SHORT).show();
	}
}
