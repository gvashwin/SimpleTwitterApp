package com.codepath.apps.SimpleTwitterApp;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "H68LpC5xSWytoIhABcqR2eaZk";       // Change this
	public static final String REST_CONSUMER_SECRET = "mhdxk1SSZyDhaGqdmF3VHj2zIaCPbGOAU2zY7imO0UqCon9CUH"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}
	
	public void getHomeTimeline(String after,AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		if(after == null ) {
			//Log.i("debug", "Twitter Client : getting tweets");
			client.get(apiUrl, null, handler);
		} else {
			
			//Log.i("debug", "Twitter Client :getting tweets after "+after);
			RequestParams params = new RequestParams();
			params.put("max_id", after);
			client.get(apiUrl, params, handler);
		}
		

	}

	public void getMentionsTimeline(String after,AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		if(after == null ) {
			//Log.i("debug", "Twitter Client : getting tweets");
			client.get(apiUrl, null, handler);
		} else {
			
			//Log.i("debug", "Twitter Client :getting tweets after "+after);
			RequestParams params = new RequestParams();
			params.put("max_id", after);
			client.get(apiUrl, params, handler);
		}
	}
	
	/*
	 * End point to get the account info of authenticated user.
	 * Can be used to get the img url to account picture.
	 * Can be used to get the account name to be displayed. 
	 */
	
	public void getAccountBasicInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, null, handler);
	}
	
	/*
	 * End point to get the follower id of authenticated user
	 * as a list. 
	 */
	
	public void getFollowerIds(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/ids.json");
		client.get(apiUrl, null, handler);
	}
	
	/*
	 * End point to get the following of authenticated user
	 * as a list. 
	 */
	
	public void getFollowingIds(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("friends/ids.json");
		client.get(apiUrl, null, handler);
	}
	
	/*
	 * End point shows the following account info
	 * tweet count
	 * follower count
	 * following count 
	 */
	
	public void getAccountStats(String hndl, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", hndl);
		client.get(apiUrl, params, handler);
	}
	
	
	public void getAccountRecentTweets(String id, String count, String max_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("user_id", id);
		if(count != null) {
			params.put("count", count);
		}
		if(max_id != null) {
			params.put("max_id", max_id);
		}
		client.get(apiUrl, params, handler);
	}
	
	
	/*
	 * This End point is used to get the list of user objects 
	 * who are followers of the given specific user id
	 */
	
	public void getFollowerList(String id, String cursor, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/list.json");
		RequestParams params = new RequestParams();
		params.put("user_id", id);
		if(cursor != null) {
			params.put("cursor", cursor);
		}
		client.get(apiUrl, params, handler);
		
	}
	
	/*
	 * This end point is used to get the list of user object 
	 * whom the specified user-id follows
	 */
	public void getFriendsList(String id, String cursor, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("friends/list.json");
		RequestParams params = new RequestParams();
		params.put("user_id", id);
		if(cursor != null) {
			params.put("cursor", cursor);
		}
		client.get(apiUrl, params, handler);
		
	}
	
	public void getDirectMessages(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("direct_messages.json");
		client.get(apiUrl, null, handler);
	}
	
	/*
	 * End point to post tweets on authenticated accounts.
	 */
	public void postTweet(String tweetText, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweetText);
		client.post(apiUrl, params, handler);
	}
	
	/*
	 * End point to post re-tweets on authenticated accounts.
	 */
	public void postRetweet(String id, AsyncHttpResponseHandler handler) {
		String apiSuffix = "statuses/retweet/"+id+".json";
		String apiUrl = getApiUrl(apiSuffix);
		/*RequestParams params = new RequestParams();
		params.put("status", tweetText);*/
		client.post(apiUrl, null, handler);
	}
	
	public void searchTweets(String query, String max_id, AsyncHttpResponseHandler handler) {
		String apiSuffix = "search/tweets.json";
		String apiUrl = getApiUrl(apiSuffix);
		RequestParams params = new RequestParams();
		params.put("q", query);
		if(max_id!=null) {
			params.put("max_id", max_id);
		}
		client.get(apiUrl, params, handler);
	}
	
}