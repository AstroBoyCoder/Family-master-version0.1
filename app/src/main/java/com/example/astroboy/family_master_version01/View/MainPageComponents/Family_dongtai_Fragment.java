package com.example.astroboy.family_master_version01.View.MainPageComponents;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.astroboy.family.GreenDao.Family;
import com.example.astroboy.family_master_version01.Model.Adapter.Essay_Adapter;
import com.example.astroboy.family_master_version01.Model.Bean.Essay_Bean;
import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.Db_FamilyService;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_userLogin;
import com.example.astroboy.family_master_version01.Util.JumpToAnyware;
import com.example.astroboy.family_master_version01.Util.RoundImageView;
import com.example.astroboy.family_master_version01.View.DongTai.Family_dongTai_publishEssay;
import com.example.astroboy.family_master_version01.View.Profile.Family_profile_infoSetting_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.example.astroboy.family_master_version01.Util.BaseFragment.ARGS_INSTANCE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Family_dongtai_Fragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final static String TAG = "Family_dongTai";
    Context context = getActivity();
    String UID = Family_Fragment_Switcher_Activity.UID;
    private String reCode = "";
    private Essay_Adapter mAdapter;
    private List<Essay_Bean> essaysData;
    private List<Family> families;

    private int selectedFM_ID = 0;

    View emptyView = null;
    PtrClassicFrameLayout ptr;
    ListView listView;
    RoundImageView userIcon;
    Spinner publisherSpinner;
    ImageButton publishEssay_btn;

    Handler mHandler;

    public static Family_dongtai_Fragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        Family_dongtai_Fragment fragment = new Family_dongtai_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //onCreateView(null,null,null);
        //initView(getView());
    }

    @Override
    public void onStart() {
        super.onStart();
        postEssayThread(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.family_dongtai_fragment, container, false);
        //initView(inflater,container);
        emptyView = inflater.inflate(R.layout.family_dongtai_listitem_empty,container, false);
        //getActivity().addContentView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initView(v);
        initData();

        ptr.setLastUpdateTimeRelateObject(this);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                postEssayThread(true);
            }
        });
        return v;
    }

    public void initView(View v){
        ptr = (PtrClassicFrameLayout) v.findViewById(R.id.dongTaiPtr);
        listView = (ListView) v.findViewById(R.id.dongTaiListView);
        listView.setEmptyView(emptyView);
        publishEssay_btn = (ImageButton) v.findViewById(R.id.dongTaiPublisher);
        //listView.setEmptyView(emptyView);
        mAdapter = new Essay_Adapter(getActivity(),new ArrayList<Essay_Bean>());
        listView.setAdapter(mAdapter);
        userIcon = (RoundImageView) v.findViewById(R.id.dongTaiUserIcon);
        publisherSpinner = (Spinner) v.findViewById(R.id.dongTaiPublisherSpinner);
        userIcon.setOnClickListener(this);
        publishEssay_btn.setOnClickListener(this);

    }

    private void initData() {
        families = queryFamiles();
        if (families != null) {
            String[] families_String = new String[families.size()+1];
            families_String[0] = "全部动态";
            for (int i = 0;i<families.size();i++) {
                families_String[i+1]=families.get(i).getFM_Name();
            }
            ArrayAdapter<String> familyString = new ArrayAdapter<>(getActivity(),R.layout.xel_spiner_learntrack_view,families_String);

            publisherSpinner.setAdapter(familyString);
            publisherSpinner.setOnItemSelectedListener(this);
        }

    }

    public void postEssayThread(final boolean isMore) {
        postEssayHandler();
        final int state = selectedFM_ID;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "UID"+UID);
                    Map<String, String> data = new HashMap<>();
                    data.put("User_ID",UID);
                    if (state == 0) {
                        reCode = Post_userLogin.getStringCha(Constant.essay_getAll, data);//向服务器提交用户上传请求
                    }else {
                        data.put("FM_ID", String.valueOf(selectedFM_ID));
                        reCode = Post_userLogin.getStringCha(Constant.essay_getEssayByFM_ID, data);
                    }
                    Log.d(TAG, "postEssayThread result:" + reCode);
                    Message msg = new Message();//消息处理机制
                    Bundle bundle = new Bundle();
                    bundle.putInt("selectedFM_ID",state);
                    bundle.putString("response", reCode);
                    bundle.putBoolean("isMore",isMore);
                    Log.d("postEssayThread", String.valueOf(bundle));
                    msg.what = 1;
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isMore",isMore);
                    msg.what = 2;
                    msg.obj = e;
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void postEssayHandler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        try {
                            //mAdapter.setData(essaysData);
                            Boolean isMore1 = msg.getData().getBoolean("isMore",false);
                            String response = msg.getData().getString("response");
                            List<Essay_Bean> essays;
                            Integer selectedFM_ID = msg.getData().getInt("selectedFM_ID");
                            Log.d(TAG,"selectedFM_ID"+selectedFM_ID);
                            if (selectedFM_ID==0) {
                                essays = JSON.parseArray(response, Essay_Bean.class);
                            }else {
                                JSONObject json = new JSONObject(response);
                                essays = JSON.parseArray(json.getString("fmEssays"),Essay_Bean.class);
                            }
                            Log.d(TAG, "Json内容" + essays);
                            if (isMore1) {
                                //essaysData.addAll(essays);
                                essaysData = essays;
                            } else {
                                essaysData = essays;
                            }
                            ptr.refreshComplete();
                            Log.d(TAG,"postEssayHandler"+" isMore"+isMore1+" essaysData"+essaysData);
                            mAdapter.setData(essaysData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        Boolean isMore2 = msg.getData().getBoolean("isMore",false);
                        if (isMore2){
                            ptr.refreshComplete();
                        }
                        Toast.makeText(context,"更新失败:"+msg.obj,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private List<Family> queryFamiles(){
        List<Family> families = new ArrayList<>();
        List<Family> allFamiles =  Db_FamilyService.getInstance(getActivity()).loadAllFamily();
        if (allFamiles.size()>0) {
            for (Family singleFamily : allFamiles) {
                //if (singleFamily.getUser_ID() == Integer.parseInt(UID)) {
                    families.add(singleFamily);
                //}
            }
            if (families.size()>0) {
                return families;
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.dongTaiUserIcon:
                new JumpToAnyware(getActivity(), Family_profile_infoSetting_Activity.class);
                break;
            case R.id.dongTaiPublisher:
                Intent in = new Intent(getActivity(),Family_dongTai_publishEssay.class);
                startActivity(in);
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i!=0) {
            selectedFM_ID = families.get(i-1).getFM_ID();
        }else {
            selectedFM_ID = 0;
        }
        postEssayThread(false);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
