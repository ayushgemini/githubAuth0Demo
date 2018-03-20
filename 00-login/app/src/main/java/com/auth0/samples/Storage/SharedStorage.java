package com.auth0.samples.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.auth0.samples.R;

/**
 * Created by ayush on 17/3/18.
 */

public class SharedStorage {


    static SharedStorage _sharedStorage;

    Context context;

    public SharedStorage(Context context) {
        this.context = context;
    }


    public static SharedStorage getInstance() {
        return _sharedStorage;
    }

    public void init(Context c) {
        context = c;
        _sharedStorage = new SharedStorage(context);
    }

    public String getStorage(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_storage_file), Context.MODE_PRIVATE);
        String value = sharedPref.getString(key, "-1");
        return value;
    }


    public void postStorage(String appName, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_storage_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(appName, value);
        editor.apply();
    }


}
