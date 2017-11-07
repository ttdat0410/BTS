package vn.vnpt.ansv.bts.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.ui.BTSActivity;

/**
 * Created by ANSV on 11/7/2017.
 */

public class SplashActivity extends BTSActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        component().inject(this);
    }
}
