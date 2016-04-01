package com.fpliu.newton.business.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.util.IOUtil;

/**
 * 国家数据
 * 
 * @author 792793182@qq.com 2014-11-20
 * 
 */
public final class Country {

	private static final String DB_NAME = "Province.db";

	private static List<Country> countries;

	private String countryName;

	private String countryCode;

	private Country() { }

	public synchronized static List<Country> getCountrys() {
		if (countries == null) {
			countries = new ArrayList<Country>();
			
			InputStream is = null;
			try {
				is = MyApp.getApp().getAssets().open("data/" + DB_NAME);
			} catch (IOException e) {
				e.printStackTrace();
				return countries;
			}
			
			String path = Environment.getInstance().getMyDir() + "/" + DB_NAME;
			File desFile = new File(path);
			
			//如果文件不存在，就先复制文件
			if (!desFile.exists()) {
				IOUtil.copy(is, desFile);
			}
			
			Cursor cursor = null;
			
			//打开数据库
			SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			if (db != null) {
				cursor = db.rawQuery("select * from Country", null);
			}
			
			if (cursor == null) {
				return null;
			}
			
			try {
				while (cursor.moveToNext()) {
					Country country = new Country();
					country.countryName = cursor.getString(1);// 获取第二列的值
					country.countryCode = cursor.getString(2);// 获取第三列的值
					countries.add(country);
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}

				if (db != null) {
					db.close();
				}
			}
		}
		
		return countries;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getCountryCode() {
		return countryCode;
	}
}
