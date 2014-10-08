package com.codepath.apps.SimpleTwitterApp.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.SimpleTwitterApp.R;
import com.codepath.apps.SimpleTwitterApp.TwitterApplication;
import com.codepath.apps.SimpleTwitterApp.TwitterClient;
import com.codepath.apps.SimpleTwitterApp.R.layout;
import com.codepath.apps.SimpleTwitterApp.adapters.MessageArrayAdapter;
import com.codepath.apps.SimpleTwitterApp.models.Message;
import com.codepath.apps.SimpleTwitterApp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MessageListViewActivty extends Activity {
	TwitterClient client = TwitterApplication.getRestClient();
	ProgressBar pb;
	ArrayList<Message> msgList;
	MessageArrayAdapter aMsgList;
	ListView lvMessages;
	ActionBar actionBar;
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
			ArrayList<Message> messages = Message.fromJsonArray(jsonResponseArray);
			Log.i("debug",messages.toString());
			aMsgList.addAll(messages);
			aMsgList.notifyDataSetChanged();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
		setContentView(R.layout.activity_message_list_view_activty);
		pb = (ProgressBar) findViewById(R.id.pbMessage);
		lvMessages = (ListView) findViewById(R.id.lvMessages);
		msgList = new ArrayList<Message>();
		aMsgList = new MessageArrayAdapter(this, msgList);
		lvMessages.setAdapter(aMsgList);
		populateMessageList();
	}
	
	private void setupActionBar() {
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(TwitterApplication.D_BLUE_COLOR);
		actionBar.setStackedBackgroundDrawable(TwitterApplication.D_WHITE_COLOR);
		actionBar.setIcon(R.drawable.ic_actionbar_logo);
		actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Messages</font>"));
	}
	private void populateMessageList() {
		Log.i("debug", "Getting Messages");
		client.getDirectMessages(reponseHandler);
	}
}
