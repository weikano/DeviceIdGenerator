package com.weme.group.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

class FileCache implements Cache {
	private File file;
	private final static String DID_SUB_DIR = "/system/settings";

	FileCache(Context context) {
		file = new File(Environment.getExternalStorageDirectory() + DID_SUB_DIR, DidHelper.fileName());
	}

	@Override
	public String get() {
		String cache = null;
		if(available() && file.exists()){
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				cache = new String(buffer);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(fis != null){
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return cache;
		
	}

	@Override
	public void put(String value) {
		if(!available()){
			return;
		}
		if(TextUtils.isEmpty(value)){
			return;
		}
		if(!file.exists()){
			if(file.getParentFile().mkdirs()){
				FileOutputStream fos = null;
				try {
					if(file.createNewFile()){
						fos = new FileOutputStream(file);
						fos.write(value.getBytes());
						fos.flush();
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private boolean available() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

}
