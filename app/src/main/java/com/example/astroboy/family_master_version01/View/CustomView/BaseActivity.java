package com.example.astroboy.family_master_version01.View.CustomView;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDarkStatusIcon(true);
    }

    public void setDarkStatusIcon(boolean bDark) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            View decorView = getWindow().getDecorView();
            if(decorView != null){
                int vis = decorView.getSystemUiVisibility();
                if(bDark){
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else{
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }
}
