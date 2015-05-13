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
    public static final String UNIT = "unitKey";
    public static final String RADIUS = "radiusKey";
    public static final String FIRST_BOOT = "firstBootKey";
    public static final String GOOD_COLOR = "goodColorKey";
    public static final String OK_COLOR = "okColorKey";
    public static final String BAD_COLOR = "badColorKey";

    /**
     * Writes strings to a shared preferences file
     * @param context Context of the calling activity
     * @param preferenceName Key of the value to be saved
     * @param preferenceValue Value to be saved
     */
    public void saveStringToPreferences(Context context, String preferenceName, String preferenceValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    /**
     * Reads strings from the value of the provided key, if the provided key does not exist it returns a default value
     * @param context Context of the calling activity
     * @param preferenceName Key of the value to be read
     * @param defaultValue Default value to be return in case there is no value for the key provided
     * @return String value of the requested key
     */
    public String readStringsFromPreferences(Context context, String preferenceName, String defaultValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    /**
     * Writes booleans to a shared preferences file
     * @param context Context of the calling activity
     * @param preferenceName Key of the value to be read
     * @param preferenceValue Value to be saved
     */
    public void saveBooleanToPreferences(Context context, String preferenceName, boolean preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();
    }

    /**
     * Reads boolean from the value of the provided key, if the provided key does not exist it returns a default value
     * @param context Context of the calling activity
     * @param preferenceName Key of the value to be read
     * @param defaultValue Default value to be return in case there is no value for the key provided
     * @return boolean value of the requested key
     */
    public boolean readBooleanFromPreferences(Context context, String preferenceName, boolean defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(preferenceName, defaultValue);
    }

    /**
     * Writes int to a shared preferences file
     * @param context Context of the calling activity
     * @param preferenceName Key of the value to be read
     * @param preferenceValue Value to be saved
     */
    public void saveIntToPreferences(Context context, String preferenceName, int preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(preferenceName, preferenceValue);
        editor.apply();
    }

    /**
     * Reads boolean from the value of the provided key, if the provided key does not exist it returns a default value
     * @param context Context of the calling activity
     * @param preferenceName Key of the value to be read
     * @param defaultValue Default value to be return in case there is no value for the key provided
     * @return int value of the requested key
     */
    public int readIntFromPreferences(Context context, String preferenceName, int defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(preferenceName, defaultValue);
    }
}
