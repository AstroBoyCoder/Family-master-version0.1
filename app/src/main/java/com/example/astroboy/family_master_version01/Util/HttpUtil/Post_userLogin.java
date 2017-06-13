package com.example.astroboy.family_master_version01.Util.HttpUtil;

import android.util.Log;

import com.lzy.okgo.OkGo;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by AstroBoy on 2016/12/1.
 */

public class Post_userLogin {
    public static String getStringCha(String url, final Map<String, String> map) throws IOException {
        Log.d("PostLogin", map.toString());
        /*RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name",un)
                .addFormDataPart("password",pw)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                //.post(requestBody)
                .build();*/

      /*  HashMap<String, String> params = new HashMap<>();
        params.put("name", un);
        params.put("password", pw);
        JSONObject jsonObject = new JSONObject(params);*/

        Response response = OkGo
                .post(url)
                .tag("")
                .params(map)
                .connTimeOut(5000)
                //.upJson(jsonObject.toString())
                .execute();

        System.out.println("-------------------------------");
        Log.d("PostLogin", "response" + response.toString());
        System.out.println("-------------------------------");
        //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

}
