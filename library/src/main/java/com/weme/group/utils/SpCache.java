package com.weme.group.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class SpCache implements Cache {
	private final static String KEY = "deviceinfoex_did_key_fixed";
	private SharedPreferences sp;
	SpCache(Context context){
		sp = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	@Override
	public String get() {
		return sp.getString(KEY, null);
	}

	@Override
	public void put(String value) {
		sp.edit().putString(KEY, value).apply();
	}

}
