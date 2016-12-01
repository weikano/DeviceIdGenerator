package com.weme.group.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.text.TextUtils;

public final class DidHelper {
	
	private volatile String did;
	private volatile String md5Did;
	private volatile String generated;
	
	private volatile static DidHelper instance;
	private CacheManager cache;
	
	private DidHelper(Context context){
		Context app = context.getApplicationContext();
		cache = CacheManager.getInstance(app);
		generated = generateDid(app);
	}
	
	public static DidHelper getInstance(Context context){
		if(instance == null){
			synchronized (DidHelper.class) {
				if(instance == null){
					instance = new DidHelper(context);
				}
			}
		}
		return instance;
	}
	
	public synchronized String getDid(){
		if(TextUtils.isEmpty(md5Did)){
			if(TextUtils.isEmpty(did)){
				did = cache.get();
			}
			if(TextUtils.isEmpty(did)){
				did = generated;
				cache.put(did);
			}
			md5Did = md5(did);
		}
		return md5Did;
	}
	
	private String generateDid(Context context){
		JSONObject json = new JSONObject();
		try {
			json.put("manufacturer", Build.MANUFACTURER);
			json.put("brand", Build.BRAND);
			json.put("device", Build.DEVICE);
			json.put("model", Build.MODEL);
			json.put("serial", Build.SERIAL);
			json.put("fingerprint", Build.FINGERPRINT);
			json.put("version_sdk_int", Build.VERSION.SDK_INT);
			json.put("version_release", Build.VERSION.RELEASE);
			json.put("android_id", Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));	
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
		return json.toString();
	}
	
	/**
	 * MD5
	 * @param str
	 * @return
	 */
	private static String md5(String str){
		StringBuffer result = new StringBuffer();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(str.getBytes("utf-8"));
			
			byte[] bytes = digest.digest();
			for(int i=0,j=bytes.length;i<j;i++){
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if(hex.length() == 1){
					result.append("0");
				}
				result.append(hex);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	/**
	 * SD卡文件名
	 * @return
	 */
	static String fileName(){
		return md5("com.weme.group");
	}
}
