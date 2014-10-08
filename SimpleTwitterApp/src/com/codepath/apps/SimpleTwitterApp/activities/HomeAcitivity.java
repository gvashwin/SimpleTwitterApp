package com.codepath.apps.SimpleTwitterApp.activities;

import org.json.JSONException;
import org.json.JSONObject;


import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.TwitterClient;
import com.codepath.apps.SimpleTwitterApp.R.layout;
import com.codepath.apps.SimpleTwitterApp.fragments.ComposeTweetFragment;
import com.codepath.apps.SimpleTwitterApp.fragments.ComposeTweetFragment.PostTweetListener;
import com.codepath.apps.SimpleTwitterApp.fragments.HomeTimelineFragment;
import com.codepath.apps.SimpleTwitterApp.fragments.MentionsTimelineFragment;
import com.codepath.apps.SimpleTwitterApp.fragments.ProfileImageFragment;
import com.codepath.apps.SimpleTwitterApp.fragments.TweetsListFragment;
import com.codepath.apps.SimpleTwitterApp.listeners.FragmentTabListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class HomeAcitivity extends FragmentActivity implements PostTweetListener{
	private ActionBar actionBar;
	private Drawable blueColorDrawable;
	private Drawable whiteColorDrawable;
	private MenuInflater menuInflater;
	private MenuItem writeTweet;
	private Tab homeTab;
	private Tab mentionsTab;
	private int numTabs;
	public static final String TBLUE = "#55ACEE";
	public static final String TWHITE = "#FFFFFF";
	private String accountName;
	private String accountHndl;
	private String accountId;
	private String accountPicUrl;
	private TwitterClient client = TwitterApplication.getRestClient();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_acitivity);
		getBasicAccountInfo();
	}
	
	private void getBasicAccountInfo() {
		Log.i("debug","Getting account basic info");
		client.getAccountBasicInfo(new JsonHttpResponseHandler(){

			@Override
			public void onFailure(Throwable e, String s) {
				Log.i("debug",e.toString());
				Log.i("debug",s.toString());
			}

			@Override
			public void onSuccess(JSONObject jsonObject) {
				Log.i("debug","Profile info json is :"+jsonObject.toString());
				try {
					accountName = jsonObject.getString("name");
					accountHndl = jsonObject.getString("screen_name");
					accountPicUrl = jsonObject.getString("profile_image_url");
					accountId = new String(jsonObject.getString("id_str"));
					Log.i("debug","Getting account basic info - Done" +accountPicUrl);
					TwitterApplication.accountName = new String(accountName);
					TwitterApplication.accountHandle = new String(accountHndl) ;
					TwitterApplication.accountImgUrl = new String(accountPicUrl);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
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
		blueColorDrawable = new ColorDrawable(Color.parseColor(TBLUE));
		whiteColorDrawable = new ColorDrawable(Color.parseColor(TWHITE));
		setupActionbar(menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/*
	 * This method sets up the action bar and its menu items 
	 */
	private void setupActionbar(Menu menu) {
		actionBar.setBackgroundDrawable(blueColorDrawable);
		actionBar.setStackedBackgroundDrawable(whiteColorDrawable);
		actionBar.setIcon(R.drawable.ic_actionbar_logo);
		actionBar.setTitle("");
		menuInflater.inflate(R.menu.home_actionbar_items, menu);
		writeTweet = menu.findItem(R.id.miComposeTweet);
		setupTabs();
		Log.i("debug","TotalTabs is "+actionBar.getTabCount());
		//numTabs = actionBar.getTabCount()
	}
	
	
	
	private void setupTabs() {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		homeTab = actionBar.newTab()
				.setText(Html.fromHtml("Home"))
				.setIcon(R.drawable.ic_home)
				.setTag("HomeTimelineFragment")
				.setTabListener(new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "HomeTab",
						HomeTimelineFragment.class));
		
		actionBar.addTab(homeTab);
		
		mentionsTab = actionBar.newTab()
				.setText("Mentions")
				.setIcon(R.drawable.ic_mention)
				.setTag("MentionsFragment")
				.setTabListener(new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "MentionsTab",
						MentionsTimelineFragment.class));
		
		
		actionBar.addTab(mentionsTab);
		
		getSupportFragmentManager().executePendingTransactions();
		
		//Toast.makeText(this, (CharSequence) homeTab.getTag(), Toast.LENGTH_LONG).show();
	}

	/*
	 * This method is invoked when the user taps
	 * on the compose tweet icon from the action bar
	 */
	public void composeTweet(MenuItem item) {
		FragmentManager fm = getSupportFragmentManager();
		ComposeTweetFragment ctf = ComposeTweetFragment.getInstance();
		Bundle args = ctf.getArguments();
		
		if(accountName!=null)
			args.putString("name", accountName);
		
		if(accountHndl!=null)
        args.putString("handle", accountHndl);
		
		if(accountPicUrl!=null)
			args.putString("imgUrl", accountPicUrl);
        
		ctf.setArguments(args);
		ctf.show(fm, "dialog");

	}
	
	/*
	 * This method is invoked when the user taps
	 * ont the profile icon on the action bar
	 */
	
	public void startSearchActivity(MenuItem item) {
		Intent i = new Intent(this,SearchTwitterActivity.class);
		startActivity(i);
	}
	public void showUserProfile(MenuItem item) {
		Intent i = new Intent(this, ProfileViewActivity.class);
		i.putExtra("name", accountName);
		i.putExtra("hndl", accountHndl);
		i.putExtra("id", accountId);
		i.putExtra("imgUrl",accountPicUrl);
		startActivity(i);
	}
	
	public void showDirectMessages(MenuItem item){
		Intent i = new Intent(this, MessageListViewActivty.class);
		startActivity(i);
	}
	
	public void showUserProfile(View v) {
		Toast.makeText(this, "showUserProfile fn in HomeActivity", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void postComposedTweet(String tweetText) {
		// Need to do a post request to post the tweet.		
		client.postTweet(tweetText, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(Throwable e, String s) {
				Log.i("debug",e.toString());
				Log.i("debug",s.toString());
			}

			@Override
			public void onSuccess(JSONObject jsonObject) {
				Log.i("debug",jsonObject.toString());
				Log.i("debug","HomeActivity - Pull to refresh called onRefresh");
				getSupportFragmentManager().executePendingTransactions();
				HomeTimelineFragment homeTimeline = 
						(HomeTimelineFragment) getSupportFragmentManager().findFragmentByTag("HomeTab");
				homeTimeline.fillTimeLineWithTweets();
				
				
			}
			
		});
	}

	

}
