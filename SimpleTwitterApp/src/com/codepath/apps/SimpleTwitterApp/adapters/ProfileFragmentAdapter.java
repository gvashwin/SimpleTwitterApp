package com.codepath.apps.SimpleTwitterApp.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ProfileFragmentAdapter extends FragmentPagerAdapter {
	private List<Fragment> profleFragments;

	public ProfileFragmentAdapter(FragmentManager fm, List<Fragment> profileFragments) {
		super(fm);
		this.profleFragments = profileFragments;
	}

	@Override
	public Fragment getItem(int position) {
		return this.profleFragments.get(position);
	}

	@Override
	public int getCount() {
		return this.profleFragments.size();
	}

}
