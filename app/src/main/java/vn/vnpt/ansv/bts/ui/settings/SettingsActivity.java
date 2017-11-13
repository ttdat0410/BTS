package vn.vnpt.ansv.bts.ui.settings;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.ui.BTSPreferences;
import vn.vnpt.ansv.bts.ui.PreferenceManager;

/**
 * Created by ANSV on 11/13/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ip_edit_text)
    EditText ip_edit_text;

    @BindView(R.id.port_edit_text)
    EditText port_edit_text;

    @BindView(R.id.checkIPButton)
    Button checkIPButton;

    @BindView(R.id.txtStatus)
    TextView txtStatus;

    @Inject
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        (((BTSApplication) getApplication()).getAppComponent()).inject(this);
        ButterKnife.bind(this);
        initToolbar();
        loadSharedPreference();
        checkIPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtStatus.setVisibility(View.INVISIBLE);
                if (ip_edit_text.length()>=7 && port_edit_text.length()>0) {
                    txtStatus.setVisibility(View.INVISIBLE);
                    finish();

                } else {
                    txtStatus.setVisibility(View.VISIBLE);
                    if (ip_edit_text.length() < 7) {
                        txtStatus.setText("trống ip");
                    } else if (port_edit_text.length() < 1) {
                        txtStatus.setText("trống port");
                    }

                }
            }
        });
    }

    private void loadSharedPreference() {
        BTSPreferences prefs = preferenceManager.getPreferences();
        ip_edit_text.setText(prefs.ip);
        port_edit_text.setText(prefs.port);
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
