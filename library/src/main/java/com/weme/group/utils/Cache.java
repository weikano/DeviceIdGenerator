package com.weme.group.utils;

interface Cache {
	/**
	 * get cache value
	 * 
	 * @return can be null
	 */
	String get();
	/**
	 * save value to cache
	 * 
	 * @param value can not be null
	 */
	void put(String value);
}
