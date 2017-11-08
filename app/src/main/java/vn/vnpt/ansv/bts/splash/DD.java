package vn.vnpt.ansv.bts.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.ui.BTSActivity;

/**
 * Created by ANSV on 11/8/2017.
 */

public class DD extends AppCompatActivity {

    public static DD newInstance() {
        return new DD();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }
}
