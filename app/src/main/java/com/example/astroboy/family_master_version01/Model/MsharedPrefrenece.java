package com.example.astroboy.family_master_version01.Model;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.astroboy.family_master_version01.Util.ContextHolder.getContext;

/**
 * Created by AstroBoy on 2016/11/26.
 */

public class MsharedPrefrenece {

    public static String[] GetUserLoginSetting(Context context){
        String[] strings = new String[5];
        SharedPreferences preferences=context.getSharedPreferences("UserLoginSetting", getContext().MODE_PRIVATE);
        String userName = preferences.getString("userName","");
        String password = preferences.getString("password","");
        String isRem = preferences.getString("isRem","");
        String isAuto = preferences.getString("isAuto","");
        strings[0] = userName;
        strings[1] = password;
        strings[2] = isRem;
        strings[3] = isAuto;
        return strings;
    }

    public static  void SetUserLoginSetting(Context context,String userName,String password,String isRem,String isAuto){
        SharedPreferences.Editor editor= context.getSharedPreferences("UserLoginSetting", getContext().MODE_PRIVATE).edit();
        editor.putString("userName",userName);
        editor.putString("password",password);
        editor.putString("isRem",isRem);
        editor.putString("isAuto",isAuto);
        editor.commit();
    }



}
