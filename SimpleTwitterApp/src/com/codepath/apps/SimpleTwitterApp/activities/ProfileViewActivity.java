package com.codepath.apps.SimpleTwitterApp.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.util.Log;
import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.R.layout;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.TwitterClient;
import com.codepath.apps.SimpleTwitterApp.adapters.ProfileFragmentAdapter;
import com.codepath.apps.SimpleTwitterApp.adapters.TweetsArrayAdapter;
import com.codepath.apps.SimpleTwitterApp.fragments.MyTestFragment;
import com.codepath.apps.SimpleTwitterApp.fragments.ProfileImageFragment;
import com.codepath.apps.SimpleTwitterApp.fragments.ProfileInfoFragment;
import com.codepath.apps.SimpleTwitterApp.models.Tweet;
import com.codepath.apps.SimpleTwitterApp.utils.NumberFormatter;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileViewActivity extends FragmentActivity {
	private ProfileFragmentAdapter aProfileFragmengts;
	private List<Fragment> fragments;
	private ViewPager vp;
	
	private String profileName;
	private String profileTwitterHndl;
	private String profilePicUrl;
	private String profileId;
	private String profileBio;
	private String pofileBannerUrl;
	private Boolean isVerfied;
	
	private TextView tvFollowers;
	private TextView tvFollowing;
	private TextView tvTweetsCount;
	private ListView lvProfileTweets;
	private TweetsArrayAdapter aProfileTweetsArrayAdapter;
	private List<Tweet> myTweets;
	private ActionBar actionBar;
	ProgressBar pb;
	NumberFormatter numberFormatter = NumberFormatter.getInstance();
	private TwitterClient client = TwitterApplication.getRestClient();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.i("debug", "ProfileViewActivity - on create");
		setupActionBar();
		Bundle extras = getIntent().getExtras();
		//profileName = new String(extras.getString("name"));
		profileTwitterHndl = new String(extras.getString("hndl"));
		//profilePicUrl = new String(extras.getString("imgUrl"));
		//profileId = new String(extras.getString("id"));
		
		Log.i("debug", "ProfileViewActivity - on create"+profilePicUrl+" id is :"+profileId);
		
		setContentView(R.layout.activity_profile_view);
		fragments = getFragments();
		aProfileFragmengts = new ProfileFragmentAdapter(getSupportFragmentManager(),fragments);
		vp = (ViewPager) findViewById(R.id.vpPager);
		vp.setAdapter(aProfileFragmengts);
		aProfileFragmengts.notifyDataSetChanged();
		tvFollowers = (TextView) findViewById(R.id.tvFollowerCount);
		tvFollowing = (TextView) findViewById(R.id.tvFollowingCount);
		tvTweetsCount = (TextView) findViewById(R.id.tvTweetsCount);
		lvProfileTweets = (ListView) findViewById(R.id.lvProfileTweets);
		pb = (ProgressBar) findViewById(R.id.pbVpPager);
		myTweets = new ArrayList<Tweet>();
		aProfileTweetsArrayAdapter = new TweetsArrayAdapter(this, myTweets);
		lvProfileTweets.setAdapter(aProfileTweetsArrayAdapter);
		getProfileBasicInfo();
		pb.setVisibility(ProgressBar.VISIBLE);
	}

	private void getAccountTweets() {
		client.getAccountRecentTweets(profileId, ""+10, null,new JsonHttpResponseHandler(){

			@Override
			public void onFailure(Throwable e, String s) {
				Log.i("debug",e.toString());
				Log.i("debug",s.toString());
			}

			@Override
			public void onSuccess(int statusCode, JSONArray jsonResponseArray) {
				Log.i("debug","Use Show Ids Json is :"+jsonResponseArray.toString());
					Log.i("debug","Return Status Code"+statusCode);
					Log.i("debug",jsonResponseArray.toString());
					ArrayList<Tweet> tweets = Tweet.fromJsonArray(jsonResponseArray);
					Log.i("debug",tweets.toString());
					aProfileTweetsArrayAdapter.addAll(tweets);
					aProfileTweetsArrayAdapter.notifyDataSetChanged();
					pb.setVisibility(ProgressBar.INVISIBLE);
			}
			
		});
		
	}

	private void setupActionBar() {
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(TwitterApplication.D_BLUE_COLOR);
		actionBar.setStackedBackgroundDrawable(TwitterApplication.D_WHITE_COLOR);
		actionBar.setIcon(R.drawable.ic_actionbar_logo);
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Profile </font>"));
	}

	private void getProfileBasicInfo() {
		Log.i("debug","Getting account stats and updating the acount info");
		client.getAccountStats(profileTwitterHndl, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(Throwable e, String s) {
				Log.i("debug",e.toString());
				Log.i("debug",s.toString());
			}

			@Override
			public void onSuccess(JSONObject jsonObject) {
				//Log.i("debug","Use Show Ids Json is :"+jsonObject.toString());
				try {
					int totalTweets = jsonObject.getInt("statuses_count");
					int totalFollowing = jsonObject.getInt("friends_count");
					int totalFollowers = jsonObject.getInt("followers_count");
					profileBio = new String(jsonObject.getString("description"));
					profileName = new String(jsonObject.getString("name"));
					profilePicUrl = new String(jsonObject.getString("profile_image_url"));
					isVerfied = jsonObject.getBoolean("verified");
					if(jsonObject.has("profile_banner_url")) {
						pofileBannerUrl = new String(jsonObject.getString("profile_banner_url"));
					} else {
						pofileBannerUrl = null;
					}
					
					profileId = new String(jsonObject.getString("id_str"));

					//Log.i("Debug", "TWEETS COUNT is "+totalTweets);
					tvTweetsCount.setText(numberFormatter.getTwitterNumberFormat(""+totalTweets));
					tvFollowers.setText(numberFormatter.getTwitterNumberFormat(""+totalFollowers));
					tvFollowing.setText(numberFormatter.getTwitterNumberFormat(""+totalFollowing));
					ProfileImageFragment f1 = (ProfileImageFragment) aProfileFragmengts.getItem(0);
					f1.updateView(profileName, profileTwitterHndl, profilePicUrl, pofileBannerUrl, isVerfied);
					ProfileInfoFragment f2 = (ProfileInfoFragment) aProfileFragmengts.getItem(1);
					f2.updateView(profileBio, pofileBannerUrl);
					getAccountTweets();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		
	}
	
	private List<Fragment> getFragments() {
		List <Fragment> toReturn = new ArrayList<Fragment>();
		toReturn.add(ProfileImageFragment.newInstance());
		toReturn.add(ProfileInfoFragment.newInstance());
		
		return toReturn;
	}
	
	public void loadMoreTweets(View v) {
		//Toast.makeText(this, "Loading more tweets", Toast.LENGTH_SHORT).show();
		Log.i("debug","starting activity to view all tweets");
		Intent showAllTweets = new Intent(this, ViewAllTweets.class);
		showAllTweets.putExtra("id", profileId);
		startActivity(showAllTweets);
	}
	
	public void showFollowerList(View v) {
		Log.i("debug","starting activity to view all followes");
		Intent showFollowerList = new Intent(this, FollowerListViewActivity.class);
		showFollowerList.putExtra("id", profileId);
		startActivity(showFollowerList);
	}
	
	public void showFollowingList(View v) {
		Log.i("debug","starting activity to view all following");
		Intent showFollowingList = new Intent(this, FollowingListViewActivity.class);
		showFollowingList.putExtra("id", profileId);
		startActivity(showFollowingList);
	}
}
