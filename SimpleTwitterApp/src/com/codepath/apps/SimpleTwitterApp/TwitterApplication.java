package com.codepath.apps.SimpleTwitterApp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 * 
 *     RestClient client = RestClientApp.getRestClient();
 *     // use client to send requests to API
 *     
 */
public class TwitterApplication extends com.activeandroid.app.Application {
	private static Context context;
	public static final String TBLUE = "#55ACEE";
	public static final String TWHITE = "#FFFFFF";
	public static final Drawable D_BLUE_COLOR = 
			new ColorDrawable(Color.parseColor(TwitterApplication.TBLUE));
	public static final Drawable D_WHITE_COLOR = 
			new ColorDrawable(Color.parseColor(TwitterApplication.TWHITE));
	public static String accountName;
	public static String accountHandle;
	public static String accountImgUrl;
	public static String lastLoadedTweet;
	public static String lastLoadedMention;
	@Override
	public void onCreate() {
		super.onCreate();
		TwitterApplication.context = this;

		// Create global configuration and initialize ImageLoader with this configuration
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
				cacheInMemory().cacheOnDisc().build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.defaultDisplayImageOptions(defaultOptions)
		.build();
		ImageLoader.getInstance().init(config);
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApplication.context);
	}
	
	
}