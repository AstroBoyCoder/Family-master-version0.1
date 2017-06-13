package com.example.astroboy.family_master_version01.View.MainPageComponents;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.astroboy.family.GreenDao.Family;
import com.example.astroboy.family_master_version01.Model.Adapter.FamilyListAdapter;
import com.example.astroboy.family_master_version01.Model.Adapter.FamilyMembersAdapter;
import com.example.astroboy.family_master_version01.Model.Bean.Family_Member_Bean;
import com.example.astroboy.family_master_version01.Model.Bean.Family_bean;
import com.example.astroboy.family_master_version01.Model.Bean.Member_Bean;
import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.Db_FamilyService;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_userLogin;
import com.example.astroboy.family_master_version01.Util.JumpToAnyware;
import com.example.astroboy.family_master_version01.View.FamilyManage.Family_manage_applyFamily;
import com.example.astroboy.family_master_version01.View.FamilyManage.Family_manage_applyList;
import com.example.astroboy.family_master_version01.View.FamilyManage.Family_manage_create;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.example.astroboy.family_master_version01.Util.BaseFragment.ARGS_INSTANCE;
import static com.example.astroboy.family_master_version01.View.MainPageComponents.Family_Fragment_Switcher_Activity.UID;

/**
 * A simple {@link Fragment} subclass.
 */
public class Family_list_Fragment extends Fragment implements View.OnClickListener, PtrHandler {

    private final static String TAG= "Family_list_Fragment";
    private Handler mHandler = new Handler();
    private String reCode = "";
    private List<Family_Member_Bean> familyMembers = new ArrayList<>();
    private FamilyMembersAdapter familyMembersAdapter = null;
    private ExpandableListView family_members_expandableList;
    private LinearLayoutManager layoutManager;
    private ListView family_list;
    private PtrClassicFrameLayout refreshList;
    View headerView = null;
    private List<Family> families;
    private LinearLayout apply_family_ly,create_family_ly,applyList_family_ly,header_ly;
    private FamilyListAdapter listAdapter;

    public static Family_list_Fragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        Family_list_Fragment fragment = new Family_list_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.family_list_fragment, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        initView(v);

        return v;
    }

    private void initView(View v) {
        family_members_expandableList = (ExpandableListView) v.findViewById(R.id.family_members_list_view);
        familyMembersAdapter = new FamilyMembersAdapter(getActivity(),new ArrayList<Family_Member_Bean>());

        View emptyView = View.inflate(getActivity(), R.layout.family_dongtai_listitem_empty, null);
        //getActivity().addContentView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        family_list = (ListView) v.findViewById(R.id.family_list_view);
        headerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.family_list_func_layout, null, false);
        apply_family_ly = (LinearLayout) headerView.findViewById(R.id.apply_family_ly);
        create_family_ly = (LinearLayout) headerView.findViewById(R.id.create_family_ly);
        applyList_family_ly = (LinearLayout) headerView.findViewById(R.id.applyList_family_ly);
        headerView.setOnClickListener(this);
        family_list.addHeaderView(headerView);
        family_list.setDividerHeight(5);
        //family_list.setEmptyView(emptyView);
        //family_members_expandableList.setEmptyView(emptyView);
        family_members_expandableList.addHeaderView(headerView);
        listAdapter = new FamilyListAdapter(new ArrayList<Family>(), getActivity());
        family_members_expandableList.setAdapter(familyMembersAdapter);
        //family_list.setAdapter(listAdapter);
        refreshList = (PtrClassicFrameLayout) v.findViewById(R.id.get_familyInfo_Ptr);
        refreshList.setLastUpdateTimeRelateObject(this);
        refreshList.setPtrHandler(this);
        initData(false);


        /*StoreHouseHeader storeHeder = new StoreHouseHeader(getActivity());
        refreshList.setHeaderView(storeHeder);*/
        //refreshList.addPtrUIHandler(storeHeder);
        //refreshList.setDurationToCloseHeader(3000);


        //header_ly = (LinearLayout) v.findViewById(R.id.family_list_func_ly);


        apply_family_ly.setOnClickListener(this);
        create_family_ly.setOnClickListener(this);
        applyList_family_ly.setOnClickListener(this);


    }

    private void initData(boolean isRefresh) {
        /*families = queryFamiles();
        if (families!=null) {
            listAdapter.setData(families);
        }*/
        postFamilyListToServer(isRefresh);

        //family_list.setAdapter(listAdapter);
        //if (isRefresh) refreshList.refreshComplete();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.apply_family_ly:
                new JumpToAnyware(getActivity(), Family_manage_applyFamily.class);
                break;
            case R.id.create_family_ly:
                new JumpToAnyware(getActivity(), Family_manage_create.class);
                break;
            case R.id.applyList_family_ly:
                new JumpToAnyware(getActivity(), Family_manage_applyList.class);
                break;
        }
    }

    private List<Family> queryFamiles(){
        List<Family> families = new ArrayList<>();
        List<Family> allFamiles =  Db_FamilyService.getInstance(getActivity()).loadAllFamily();
        if (allFamiles.size()>0) {
            for (Family singleFamily : allFamiles) {
                if (singleFamily.getUser_ID() == Integer.parseInt(UID)) {
                    families.add(singleFamily);
                }
            }
            if (families.size()>0) {
                Log.d(TAG,families.toString());
                return families;
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

    private void postFamilyListToServer(final boolean isMore) {
        postFamilyListHandler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "UID"+UID);
                    Map<String, String> data = new HashMap<>();
                    data.put("User_ID",UID);
                    reCode = Post_userLogin.getStringCha(Constant.family_Families_Members, data);
                    Log.d(TAG, "postEssayThread result:" + reCode);
                    Message msg = new Message();//消息处理机制
                    Bundle bundle = new Bundle();
                    bundle.putString("response", reCode);
                    bundle.putBoolean("isMore",isMore);
                    Log.d("postFamilyListThread", String.valueOf(bundle));
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

    private void postFamilyListHandler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Boolean isMore1 = msg.getData().getBoolean("isMore",false);
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        String response = msg.getData().getString("response");
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            familyMembers = new ArrayList<>();
                            for (int i = 0;i<jsonArray.length();i++) {
                                JSONObject singleFamily = new JSONObject(jsonArray.get(i).toString());
                                Family_Member_Bean family_member_arr = new Family_Member_Bean();
                                JSONObject family = new JSONObject(singleFamily.get("family").toString());
                                Family_bean single_family  = JSON.parseObject(family.toString(), Family_bean.class);
                                List<Member_Bean> members_arr = JSON.parseArray(singleFamily.getString("members"), Member_Bean.class);
                                family_member_arr.setFM_ID(single_family.getFM_ID());
                                family_member_arr.setCreate_Time(single_family.getCreate_Time());
                                family_member_arr.setUser_ID(single_family.getUser_ID());
                                family_member_arr.setFM_Address(single_family.getFM_Address());
                                family_member_arr.setReserved_Tel(single_family.getReserved_Tel());
                                family_member_arr.setFM_Name(single_family.getFM_Name());
                                family_member_arr.setMembers(members_arr);
                                familyMembers.add(family_member_arr);
                            }
                            if (familyMembers.size()!=0){
                                Log.d(TAG,"解析到的列表长度:"+familyMembers.size());
                                Log.d(TAG,"解析好的家庭成员列表:"+familyMembers.toString());
                                familyMembersAdapter = new FamilyMembersAdapter(getActivity(),familyMembers);
                                family_members_expandableList.setAdapter(familyMembersAdapter);
                            }
                            if (isMore1) refreshList.refreshComplete();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"更新异常:"+msg.obj,Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"更新成员列表捕获异常:"+msg.obj);
                        if(isMore1)refreshList.refreshComplete();
                        break;
                }
            }
        };
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        //return layoutManager.findFirstCompletelyVisibleItemPosition() == 0;
        return true;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        initData(true);
    }
}
