package vn.vnpt.ansv.bts.common.ui;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.common.injection.component.ActivityComponent;
import vn.vnpt.ansv.bts.common.injection.component.DaggerActivityComponent;

/**
 * Created by ANSV on 11/7/2017.
 */

public class BTSActivity extends AppCompatActivity {

    private static final String TAG = BTSActivity.class.getSimpleName();
    protected ActivityComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @TargetApi(21)
    protected void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(color);
        }
    }

    protected int getResourceColor(int resourceColor) {
        if (Build.VERSION.SDK_INT < 23) {
            return getResources().getColor(resourceColor);
        } else {
            return getColor(resourceColor);
        }
    }

    protected ActivityComponent component() {

        if (component == null) {
            component = DaggerActivityComponent.builder().bTSComponent(((BTSApplication) getApplication()).component()).build();
        }
        return component;
    }

    public int getStatusBarHeight() {
        int result = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Resources res = getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelOffset(resourceId);
            }

        }
        return result;
    }

}

