package proj.abigo.coco.cocoapplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DS on 2018-09-16.
 */

/* 자동로그인 및 user_id 저장을 위한 SharedPreferences*/
public class SharedPrefereneUtil {

    private final String Pref_name = "login_pref";
    static Context mContext;

    public SharedPrefereneUtil(Context context) {
        mContext = context;
    }

    public void putSharedPreferences(String key, String value){

        SharedPreferences pref = mContext.getSharedPreferences(Pref_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,value);
        editor.commit();

    }

    public void putSharedPreferences(String key, int value){

        SharedPreferences pref = mContext.getSharedPreferences(Pref_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key,value);
        editor.commit();

    }


    public String getSharedPreferences(String key, String value) {

        SharedPreferences pref = mContext.getSharedPreferences(Pref_name,Context.MODE_PRIVATE);

        try{
            return pref.getString(key, value);
        }catch (Exception e){
            return value;
        }

    }

    public Integer getSharedPreferences(String key, int value) {

        SharedPreferences pref = mContext.getSharedPreferences(Pref_name,Context.MODE_PRIVATE);

        try{
            return pref.getInt(key, value);
        }catch (Exception e){
            return value;
        }

    }


}
