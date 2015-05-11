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

    /**
     * Writes to a shared preferences file
     * @param context Context of the calling activity
     * @param preferenceName Key of the value to be saved
     * @param preferenceValue Value to be saved
     */
    public void saveToPreferences(Context context, String preferenceName, String preferenceValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    /**
     * Reads the value of the provided key, if the provided key does not exist it returns a default value
     * @param context Context of the calling activity
     * @param preferenceName Key of the value to be read
     * @param defaultValue Default value to be return in case there is no value for the key provided
     * @return String value of the requested key
     */
    public String readFromPreferences(Context context, String preferenceName, String defaultValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }
}
