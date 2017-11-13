package vn.vnpt.ansv.bts.ui.settings;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;

/**
 * Created by ANSV on 11/13/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initToolbar();
    }

    private void initToolbar() {

        if (Build.VERSION.SDK_INT < 23) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.sl_terbium_green));
        } else {
            toolbar.setBackgroundColor(getColor(R.color.sl_terbium_green));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        toolbar.setLayoutParams(params);
    }
}
