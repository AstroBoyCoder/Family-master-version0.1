package com.example.astroboy.family_master_version01.View.CustomView;

import android.content.Context;
import android.widget.ScrollView;

/**
 * Created by AstroBoy on 2016/12/25.
 */

public class ObservableScrollView extends ScrollView{

    private ScrollView.OnScrollChangeListener scrollChangeListener = null;
    public ObservableScrollView(Context context) {
        super(context);
    }

}
