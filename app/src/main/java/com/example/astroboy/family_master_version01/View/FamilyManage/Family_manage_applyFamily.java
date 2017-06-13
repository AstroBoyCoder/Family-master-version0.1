package com.example.astroboy.family_master_version01.View.FamilyManage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.astroboy.family_master_version01.Model.Adapter.FamilySearchListAdapter;
import com.example.astroboy.family_master_version01.Model.Bean.Family_bean;
import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_userLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.astroboy.family_master_version01.View.MainPageComponents.Family_Fragment_Switcher_Activity.UID;

public class Family_manage_applyFamily extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "Manage_applyFamily";
    private Context context = Family_manage_applyFamily.this;
    private String reCode = "";
    private Handler mHandler;

    private List<Family_bean> searchFamilie = new ArrayList<>();
    private FamilySearchListAdapter searchListAdapter = null;

    ImageButton back;
    EditText search_edit;
    Button search_btn;
    ListView search_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_manage_apply_family_activity);

        initView();
    }

    private void initView() {

        back = (ImageButton) findViewById(R.id.searchFamily_back);
        search_edit = (EditText) findViewById(R.id.searchFamily_edit);
        search_btn = (Button) findViewById(R.id.searchFamily_search);
        search_lv = (ListView) findViewById(R.id.searched_families);

        searchListAdapter = new FamilySearchListAdapter(new ArrayList<Family_bean>(),context);
        search_lv.setAdapter(searchListAdapter);
        back.setOnClickListener(this);
        search_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.searchFamily_back:
                onBackPressed();
                break;
            case R.id.searchFamily_search:
                String search_text = search_edit.getText().toString();

                if (search_text.length()>0){
                    searchFamilyThread(search_text);
                }else {
                    Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
                }
                break;
        }



    }

    public void searchFamilyThread(final String searchKey) {

        searchFamilyHandler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG,"searchKey" + searchKey);
                    Map<String, String> data = new HashMap<>();
                    data.put("User_ID", UID);
                    data.put("FM_ID", searchKey);
                    reCode = Post_userLogin.getStringCha(Constant.family_search, data);//向服务器提交用户头像上传请求
                    Log.d(TAG, "Post searchFamily result:" + reCode);//服务器端返回的结果 100 失败 200 成功
                    Message msg = new Message();//消息处理机制
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("response", reCode);
                    Log.d("用户请求登录返回的结果", String.valueOf(bundle));
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = e;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void searchFamilyHandler(){
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String response = msg.getData().getString("response");
                        try {
                            JSONObject json = new JSONObject(response);
                            String Flag = json.getString("flag");
                            String Msg = json.getString("msg");
                            Log.d(TAG,"Flag"+Flag);
                            if (Flag.equals("1")) {
                                searchFamilie = null;
                                searchFamilie = JSON.parseArray(Msg, Family_bean.class);
                                Log.d(TAG,"searchFamilie"+searchFamilie);
                                if (searchFamilie.size()!=0){
                                    searchListAdapter.setData(searchFamilie);
                                }
                            }else if (Flag.equals("2")){
                                Toast.makeText(context,"不存在符合条件的家庭",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 2:
                        Toast.makeText(context,"更新异常:"+msg.obj,Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"更新成员列表捕获异常:"+msg.obj);
                        break;
                }
            }
        };
    }
}
