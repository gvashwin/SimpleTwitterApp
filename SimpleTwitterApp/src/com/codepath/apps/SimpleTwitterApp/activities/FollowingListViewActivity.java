package com.codepath.apps.SimpleTwitterApp.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.TwitterClient;
import com.codepath.apps.SimpleTwitterApp.R.layout;
import com.codepath.apps.SimpleTwitterApp.adapters.ProfilesArrayAdapter;
import com.codepath.apps.SimpleTwitterApp.listeners.EndlessScrollListener;
import com.codepath.apps.SimpleTwitterApp.models.Profile;
import com.codepath.apps.SimpleTwitterApp.utils.NetworkUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FollowingListViewActivity extends Activity {
	
	ActionBar actionBar;
	ListView lvFriendsList;
	ArrayList<Profile> friends;
	ProfilesArrayAdapter aFriends;
	ProgressBar pb;
	String profileId;
	String nextProfileCursor;
	private TwitterClient client = TwitterApplication.getRestClient();
	NetworkUtil netUtil = NetworkUtil.getInstance();
	
	JsonHttpResponseHandler reponseHandler = new JsonHttpResponseHandler(){	
		@Override
		protected void handleFailureMessage(Throwable arg0, String arg1) {
			// TODO Auto-generated method stub
			super.handleFailureMessage(arg0, arg1);
			Log.i("debug","handleFailureMessage "+arg1);
		}

		@Override
		protected void handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			super.handleMessage(arg0);
			Log.i("debug","handleMessage "+arg0);
		}

		@Override
		protected void handleSuccessJsonMessage(int arg0, Object arg1) {
			// TODO Auto-generated method stub
			super.handleSuccessJsonMessage(arg0, arg1);
			Log.i("debug","handleSuccessJsonMessage "+arg1);
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
		public void onSuccess(int statusCode, JSONObject jsonResponseObject) {
			JSONArray followerJsonArray=null;
			try {
				followerJsonArray = jsonResponseObject.getJSONArray("users");
				nextProfileCursor = jsonResponseObject.getString("next_cursor_str");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(followerJsonArray != null) {
				pb.setVisibility(ProgressBar.INVISIBLE);
				Log.i("debug","Return Status Code"+statusCode);
				Log.i("debug",followerJsonArray.toString());
				ArrayList<Profile> followers = Profile.fromJsonArray(followerJsonArray);
				Log.i("debug",followers.toString());
				aFriends.addAll(followers);
				aFriends.notifyDataSetChanged();
			} else {
				Log.e("debug","The Json users array is null");
			}
				
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		Bundle extras = getIntent().getExtras();
		profileId = new String(extras.getString("id"));
		Log.i("debug","Creating Follower list view activity");
		friends = new ArrayList<Profile>();
		aFriends = new ProfilesArrayAdapter(this, friends);
		pb = (ProgressBar) findViewById(R.id.pbUsersFetch);
		lvFriendsList = (ListView) findViewById(R.id.lvProfiles);
		lvFriendsList.setAdapter(aFriends);
		setupActionBar();
		setupListeners();
		populateFollowingList();
	}
	
	private void setupListeners() {
		lvFriendsList.setOnScrollListener(new EndlessScrollListener(){
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMoreFriends();
			}			
		});
		
	}

	private void setupActionBar() {
		Log.i("debug", "setting up action bar");
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(TwitterApplication.D_BLUE_COLOR);
		actionBar.setStackedBackgroundDrawable(TwitterApplication.D_WHITE_COLOR);
		actionBar.setIcon(R.drawable.ic_actionbar_logo);
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Following</font>"));
	}
	
	
	private void loadMoreFriends() {
		if(!netUtil.isNetworkAvailable(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
		} else {
			client.getFriendsList(profileId,nextProfileCursor,reponseHandler);
		}
		
	}
	private void populateFollowingList() {
		if(!netUtil.isNetworkAvailable(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_SHORT).show();
		} else {
			Log.i("debug","Calling api to list of followers");
			client.getFriendsList(profileId, null, reponseHandler);
		}
		
	}
}
