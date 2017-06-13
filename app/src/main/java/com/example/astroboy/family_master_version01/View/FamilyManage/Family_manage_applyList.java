package com.example.astroboy.family_master_version01.View.FamilyManage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.astroboy.family_master_version01.Model.Bean.FamilyApplyList_bean;
import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.Model.Adapter.FamilyApplyListAdapter;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_userLogin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.astroboy.family_master_version01.View.MainPageComponents.Family_Fragment_Switcher_Activity.UID;

public class Family_manage_applyList extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "Family_manage_create";

    private String reCode = "";
    private Handler mHandler;

    ImageButton back;
    ListView applyList;
    List<FamilyApplyList_bean> applyFamilie;
    private FamilyApplyListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_manage_apply_list_activity);

        init();
        initData();
    }

    private void init() {
        back = (ImageButton) findViewById(R.id.applyList_back);
        applyList = (ListView) findViewById(R.id.family_applyList_view);
        listAdapter = new FamilyApplyListAdapter(new ArrayList<FamilyApplyList_bean>(),this);
        applyList.setAdapter(listAdapter);
        back.setOnClickListener(this);

    }

    private void initData() {
        postApplyListFromServer();
    }

    private void postApplyListFromServer() {
        postApplyListFromhandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> data = new HashMap<>();
                    data.put("User_ID", UID);
                    Log.d(TAG, "CONTENT:"+data);
                    /*if (req==200){
                        String filePath = selImageList.get(0).path;
                        //data.put("img",new File(filePath));
                        reCode = Post_essay.getStringCha(Constant.essay_publish, data,filePath);//向服务器提交用户上传请求
                    }else {*/
                    reCode = Post_userLogin.getStringCha(Constant.family_getApplyList, data);//向服务器提交用户上传请求
                    //}

                    Log.d(TAG, "Post upload_Essay result:" + reCode);
                    Message msg = new Message();//消息处理机制
                    Bundle bundle = new Bundle();
                    bundle.putString("response", reCode);
                    Log.d("用户请求登录返回的结果", String.valueOf(bundle));
                    msg.what = 1;
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = e;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void postApplyListFromhandler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        String response = msg.getData().getString("response");
                        //JSONObject json = new JSONObject(response);
                        Log.d(TAG, "Json内容" + response);
                        //String Msg = json.getString("msg");
                        //String flag = json.getString("flag");
                        //if (flag.equals("1")) {
                        //onSuccessDialog();
                        applyFamilie = JSON.parseArray(response, FamilyApplyList_bean.class);
                        listAdapter.setData(applyFamilie);
                        //} else if (flag.equals("0")) {
                        //Toast.makeText(Family_manage_applyList.this, Msg, Toast.LENGTH_SHORT).show();
                        //}
                        break;
                    case 2:
                        Toast.makeText(Family_manage_applyList.this, "更新失败:" + msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.applyList_back:
                onBackPressed();
                break;
        }
    }
}
