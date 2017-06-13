package com.example.astroboy.family_master_version01.Util.HttpUtil;

import android.util.Log;

import com.lzy.okgo.OkGo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by AstroBoy on 2016/12/1.
 */

public class Post_essay {
    public static String getStringCha(String url, final Map<String, String> map,String filePath) throws IOException {
        Log.d("PostLogin", map.toString());
        Response response;
        if (filePath!=null) {
            response = OkGo
                    .post(url)
                    .isMultipart(true)
                    .params("img",new File(filePath))
                    .tag("")
                    .params(map)
                    .connTimeOut(5000)
                    //.upJson(jsonObject.toString())
                    .execute();
        }else {
            response = OkGo
                    .post(url)
                    .isMultipart(true)
                    .tag("")
                    .params(map)
                    .connTimeOut(5000)
                    //.upJson(jsonObject.toString())
                    .execute();
        }

        System.out.println("-------------------------------");
        Log.d("PostLogin", "response" + response.toString());
        System.out.println("-------------------------------");
        //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

}
