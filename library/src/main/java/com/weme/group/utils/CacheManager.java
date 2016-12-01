package com.weme.group.utils;

import android.content.Context;
import android.text.TextUtils;

class CacheManager implements Cache {
	private Cache fileCache, spCache;
	private volatile static CacheManager instance;
	private CacheManager(Context context){
		fileCache = new FileCache(context);
		spCache = new SpCache(context);
	}
	
	static CacheManager getInstance(Context context){
		if(instance == null){
			synchronized (CacheManager.class) {
				if(instance == null){
					instance = new CacheManager(context);
				}
			}
		}
		return instance;
	}
	
	@Override
	public String get() {
		String value = spCache.get();
		if(TextUtils.isEmpty(value)){
			value = fileCache.get();
		}
		return value;
	}
	
	@Override
	public void put(String value) {
		spCache.put(value);
		fileCache.put(value);		
	}
}
