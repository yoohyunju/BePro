package com.example.bepro;

import android.content.Context;
import android.content.SharedPreferences;

//자동 로그인 체크버튼 싱글톤 처리
public class PrefsHelper {
    public static final String PREFERENCE_NAME="pref";
    private Context mContext;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefsEditor;
    private static PrefsHelper instance;

    public static synchronized PrefsHelper init(Context context){
        if(instance == null)
            instance = new PrefsHelper(context);
        return instance;
    }

    private PrefsHelper(Context context) {
        mContext = context;
        prefs = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE );
        prefsEditor = prefs.edit();
    }

    public static String read(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    public static void write(String key, String value) {
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static void remove(String key){
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    public static void clear() {
        prefsEditor.clear();
        prefsEditor.commit();
    }
}
