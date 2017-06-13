package com.example.astroboy.family_master_version01.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by AstroBoy on 2016/12/21.
 */

public class JumpToAnyware extends Activity {

    public JumpToAnyware(Context context, Class c) {
        Intent in = new Intent(context, c);
        context.startActivity(in);
    }

}
