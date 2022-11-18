package com.example.newsapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    public static final String SP_NAME = "userSession";
    public static final String USER_NAME = "nama";
    public static final String USER_EMAIL = "email";
    public static final String USER_PHOTO = "foto";
    public static final String SESSION_MODE = "isAlreadyLogin";

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
    }

    public void saveSPString(String keySP, String value) {
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, Boolean value) {
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public String getSPEmail() {
        return sharedPreferences.getString(USER_EMAIL, "");
    }

    public String getSPPhoto() {
        return sharedPreferences.getString(USER_PHOTO, "");
    }

    public Boolean getSPAlreadyLogin() {
        return sharedPreferences.getBoolean(SESSION_MODE, false);
    }

    public void removeData(){
        spEditor.clear();
        spEditor.commit();
    }

}
