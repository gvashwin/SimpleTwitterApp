package com.codepath.apps.SimpleTwitterApp.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterClient;
import com.codepath.apps.SimpleTwitterApp.R.layout;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.adapters.TweetsArrayAdapter;
import com.codepath.apps.SimpleTwitterApp.listeners.EndlessScrollListener;
import com.codepath.apps.SimpleTwitterApp.models.Tweet;
import com.codepath.apps.SimpleTwitterApp.utils.NetworkUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class SearchTwitterActivity extends Activity implements OnQueryTextListener {
	ActionBar actionBar;
	MenuInflater menuInflater;
	MenuItem searchItem;
	SearchView searchView;
	ListView lvSearchResult;
	ProgressBar pb;
	ArrayList<Tweet> tweets;
	TweetsArrayAdapter aLvSearchResult;
	String searchQuery;
	String lastLoadedTweetId;
	private TwitterClient client = TwitterApplication.getRestClient();
	private NetworkUtil netUtil = NetworkUtil.getInstance();
	JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){

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
		public void onSuccess(JSONObject jsonObject) {
			Log.i("debug","Json Object :"+jsonObject.toString());
			JSONArray jsonTweetObjectList = null;
			try {
				jsonTweetObjectList = jsonObject.getJSONArray("statuses");
				Log.i("debug","jsonTweetObjectList" +jsonTweetObjectList.toString());
				ArrayList<Tweet> tweetList = Tweet.fromJsonArray(jsonTweetObjectList);
				pb.setVisibility(ProgressBar.INVISIBLE);
				lastLoadedTweetId = tweetList.get(tweetList.size()-1).getUid();
				aLvSearchResult.addAll(tweetList);
				aLvSearchResult.notifyDataSetChanged();
				searchView.clearFocus();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tweets_list);
		lvSearchResult = (ListView) findViewById(R.id.lvTweets);
		pb = (ProgressBar) findViewById(R.id.pbTimeline);
		tweets = new ArrayList<Tweet>();
		aLvSearchResult = new TweetsArrayAdapter(this, tweets);
		lvSearchResult.setAdapter(aLvSearchResult);
		pb.setVisibility(ProgressBar.INVISIBLE);
		setupListeners();
	}
	
	private void setupListeners() {
		lvSearchResult.setOnScrollListener(new EndlessScrollListener(){
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMoreSearchResutl();
			}

			
		});
		
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Getting a reference to the actionbar
		actionBar = getActionBar();
		// Getting a reference to the menu inflator	
		menuInflater = getMenuInflater();
		// Creating a reference to twitter blue color
		setupActionbar(menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	private void setupActionbar(Menu menu) {
		actionBar.setBackgroundDrawable(TwitterApplication.D_BLUE_COLOR);
		actionBar.setStackedBackgroundDrawable(TwitterApplication.D_WHITE_COLOR);
		actionBar.setIcon(R.drawable.ic_actionbar_logo);
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Search</font>"));
		actionBar.setDisplayHomeAsUpEnabled(true);
		menuInflater.inflate(R.menu.search_activity_action_bar_items, menu);
		searchItem = menu.findItem(R.id.miSearchActivitySearch);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(this);
		searchItem.expandActionView();
		Log.i("debug","TotalTabs is "+actionBar.getTabCount());
	}
	
	

	@Override
	public boolean onQueryTextSubmit(String query) {
		searchQuery = query;
		//Toast.makeText(this, "Searching for "+query, Toast.LENGTH_SHORT).show();
		searchTwitter(query);
		return true;
	}

	
	
	private void loadMoreSearchResutl() {
		if(!netUtil.isNetworkAvailable(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
		} else {
			if(lastLoadedTweetId !=null) {
			client.searchTweets(searchQuery, lastLoadedTweetId, responseHandler); 
			}
		}
		
	}
	private void searchTwitter(String query) {
		
		if(!netUtil.isNetworkAvailable(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
		} else {
			aLvSearchResult.clear();
			client.searchTweets(query, null,responseHandler); 
		}
		
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}
}
