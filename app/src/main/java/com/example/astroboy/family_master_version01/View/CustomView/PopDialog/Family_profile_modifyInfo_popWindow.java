package com.example.astroboy.family_master_version01.View.CustomView.PopDialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.ContextHolder;


/**
 * Created by AstroBoy on 2016/8/12.
 */
public class Family_profile_modifyInfo_popWindow extends PopupWindow {

    public static View popView;
    private int layoutID = 0;

    public Family_profile_modifyInfo_popWindow(Activity context, View.OnClickListener itemsOnClick, String title, String input_Type) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("ModifyInfo", "Title"+title+"  Input_Type:"+input_Type);
        switch (input_Type) {
            case "modify_text":
                layoutID = R.id.info_layout;
                popView = inflater.inflate(R.layout.family_profile_modify_userinfo_dialog, null);
                Button modify_edit_confirm = (Button) popView.findViewById(R.id.modify_edit_confirm);
                Button modify_edit_cancel = (Button) popView.findViewById(R.id.modify_edit_cancel);
                setWindowTitle(title);
                //取消按钮
                modify_edit_cancel.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        dismiss();
                    }
                });
                modify_edit_cancel.setOnClickListener(itemsOnClick);
                modify_edit_confirm.setOnClickListener(itemsOnClick);
                break;
            case "modify_gender":
                layoutID = R.id.gender_layout;
                popView = inflater.inflate(R.layout.family_profile_modify_usergender_dialog, null);
                Button modify_gender_confirm = (Button) popView.findViewById(R.id.modify_gender_confirm);
                Button modify_gender_cancel = (Button) popView.findViewById(R.id.modify_gender_cancel);
                modify_gender_cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                modify_gender_confirm.setOnClickListener(itemsOnClick);
                break;
            case "modify_icon":
                layoutID = R.id.userIcon_layout;
                popView = inflater.inflate(R.layout.family_profile_modify_usericon_dialog, null);
                Button btn_take_photo = (Button) popView.findViewById(R.id.item_popupwindows_camera);
                Button btn_pick_photo = (Button) popView.findViewById(R.id.item_popupwindows_Photo);
                Button btn_cancel = (Button) popView.findViewById(R.id.modify_userIcon_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                btn_pick_photo.setOnClickListener(itemsOnClick);
                btn_take_photo.setOnClickListener(itemsOnClick);
                break;
            default:
                Toast.makeText(ContextHolder.getContext(),"使用方法错误",Toast.LENGTH_SHORT).show();
                break;
        }

        //设置SelectPicPopupWindow的View
        this.setContentView(popView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = popView.findViewById(layoutID).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    private void setWindowTitle(String title) {
        TextView modify_text;
        modify_text = (TextView) popView.findViewById(R.id.modify_text);
        modify_text.setText(title);
    }
}
