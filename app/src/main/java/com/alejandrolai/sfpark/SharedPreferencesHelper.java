package com.alejandrolai.sfpark;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alejandro on 5/9/15.
 */
public class SharedPreferencesHelper {

    private static SharedPreferencesHelper instance = new SharedPreferencesHelper();

    private SharedPreferencesHelper(){}

    public static SharedPreferencesHelper getInstance() {
        return instance;
    }

    public static final String PREFERENCES_FILE = "MyPreferences";

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }
}
