package com.example.astroboy.family_master_version01.View.MainPageComponents;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astroboy.family.GreenDao.User;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.ActionSheetDialog;
import com.example.astroboy.family_master_version01.Util.Db_UserService;
import com.example.astroboy.family_master_version01.Util.ExitApplication;
import com.example.astroboy.family_master_version01.Util.JumpToAnyware;
import com.example.astroboy.family_master_version01.View.Other.Family_Functon_unOpen_Activity;
import com.example.astroboy.family_master_version01.View.Profile.Family_profile_infoSetting_Activity;
import com.example.astroboy.family_master_version01.View.Profile.ManageFamilies.Family_profile_manageFamilies_Activity;
import com.example.astroboy.family_master_version01.View.UserLoadApp.Family_Login_Activity;

import java.util.List;

import static com.example.astroboy.family_master_version01.Util.BaseFragment.ARGS_INSTANCE;
import static com.example.astroboy.family_master_version01.View.MainPageComponents.Family_Fragment_Switcher_Activity.UID;

/**
 * A simple {@link Fragment} subclass.
 */
public class Family_myProfile_Fragment extends Fragment implements View.OnClickListener{

    //private LinearLayout personSetting,softwareSetting,infoSetting,update,manageFamily,feedback,exit;
    //private RoundImageView userIcon;
    private TextView userName,userMotto;
    private User currentUser = null;

    private Handler uiHandler;

    public static Family_myProfile_Fragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        Family_myProfile_Fragment fragment = new Family_myProfile_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.family_profile_fragment, container, false);
        initView(v);
        init_uiHandler();
        initData();
        return v;
    }

    private void init_uiHandler() {
        uiHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        userName.setText(currentUser.getUser_Name());
                        userMotto.setText(currentUser.getUser_Hobby());
                        break;
                    case 2:
                        Toast.makeText(getContext(),"获取用户信息失败,请重新登录",Toast.LENGTH_SHORT).show();
                        jumpToLogin();
                        break;
                    default:
                        Toast.makeText(getContext(),"Unknown Status:"+msg.what,Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void initData() {
        queryUser();
    }

    private void queryUser() {
        Message msg = new Message();
        List<User> users = Db_UserService.getInstance(getContext()).loadAllUser();
        for (User single : users){
            if (single.getUser_ID()==Integer.parseInt(UID)){
                currentUser = single;
            }
        }
        if (currentUser!=null){
            msg.what = 1;
            uiHandler.sendMessage(msg);
        }else{
            msg.what = 2;
            uiHandler.sendMessage(msg);
        }
    }

    private void initView(View v) {
        LinearLayout personSetting,softwareSetting,infoSetting,update,manageFamily,feedback,exit;
        personSetting = (LinearLayout) v.findViewById(R.id.profile_personSetting);
        softwareSetting = (LinearLayout) v.findViewById(R.id.profile_softwareSetting);
        infoSetting = (LinearLayout) v.findViewById(R.id.profile_infoSetting);
        update = (LinearLayout) v.findViewById(R.id.profile_update);
        manageFamily = (LinearLayout) v.findViewById(R.id.profile_manageFamily);
        feedback = (LinearLayout) v.findViewById(R.id.profile_feedback);
        exit = (LinearLayout) v.findViewById(R.id.profile_exit);

        userName = (TextView) v.findViewById(R.id.profile_name);
        userMotto = (TextView) v.findViewById(R.id.profile_motto);
        //userIcon = (RoundImageView) v.findViewById(R.id.profile_userIcon);

        personSetting.setOnClickListener(this);
        softwareSetting.setOnClickListener(this);
        infoSetting.setOnClickListener(this);
        update.setOnClickListener(this);
        manageFamily.setOnClickListener(this);
        feedback.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_manageFamily:
                new JumpToAnyware(getContext(), Family_profile_manageFamilies_Activity.class);
            break;
            case R.id.profile_softwareSetting:
                new JumpToAnyware(getContext(), Family_Functon_unOpen_Activity.class);
                break;
            case R.id.profile_personSetting:
                new JumpToAnyware(getContext(), Family_Functon_unOpen_Activity.class);
                break;
            case R.id.profile_infoSetting:
                new JumpToAnyware(getContext(), Family_profile_infoSetting_Activity.class);
                break;
            case R.id.profile_feedback:
                new JumpToAnyware(getContext(), Family_Functon_unOpen_Activity.class);
                break;
            case R.id.profile_update:
                new JumpToAnyware(getContext(), Family_Functon_unOpen_Activity.class);
                break;
            case R.id.profile_exit:
                new ActionSheetDialog(getContext())
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("退出登陆", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                ExitApplication.getInstance().Login();
                                jumpToLogin();
                            }
                        })
                        .addSheetItem("退出Family", ActionSheetDialog.SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                ExitApplication.getInstance().exit();
                            }
                        })
                        .show();
                break;
        }
    }

    private void jumpToLogin(){
        Intent intent = new Intent(getActivity(), Family_Login_Activity.class);
        startActivity(intent);
    }
}
