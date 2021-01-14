package com.example.qr_code.utils;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.qr_code.apiServices.LoginAPIService;

public class Preferences {

    SharedPreferences p;

    public Preferences(Context c){
        p = c.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
    }

    public void saveDate(String UName, String UEmail){
    SharedPreferences.Editor editor = p.edit();
    editor.putString("UName", UName);
    editor.putString("UEmail", UEmail);
    editor.commit();
    }

    public String[] getDate(){
        String result[] = new String[2];
        result[0]= p.getString("UName","");
        result[1]= p.getString("UEmail", "");
        return result;
    }

    public void deleteDate(){
        SharedPreferences.Editor editor = p.edit();
        editor.remove("UName");
        editor.remove("UEmail");
        editor.commit();
    }


}
