package vn.vnpt.ansv.bts.ui.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.johnpersano.supertoasts.SuperToast;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.ui.BTSPreferences;
import vn.vnpt.ansv.bts.ui.PreferenceManager;
import vn.vnpt.ansv.bts.utils.BTSToast;

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
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        (((BTSApplication) getApplication()).getAppComponent()).inject(this);
        ButterKnife.bind(this);
        initToolbar();
        hideKeyboard(ip_edit_text);
        hideKeyboard(port_edit_text);
        loadSharedPreference();
        checkIPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(ip_edit_text);
                hideKeyboard(port_edit_text);
                txtStatus.setVisibility(View.INVISIBLE);
                if (ip_edit_text.length()>=7 && port_edit_text.length()>0) {
                    showLoading();
                    txtStatus.setVisibility(View.INVISIBLE);

                    final String ip = ip_edit_text.getText().toString();
                    final String port = port_edit_text.getText().toString();
                    String urlCheck = "http://" + ip +":"+ port + "/BTSRestWebService/apikey/login?";

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    final StringRequest stringRequest = new StringRequest(Request.Method.GET, urlCheck,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.length() > 5) {
                                        showToast("ip, port khả dụng", SuperToast.Background.GREEN);
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                saveSharePreference(ip, port);
                                                finish();
                                            }
                                        }, 500);
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideLoading();
                            showToast("Không khả dụng, thử lại!", SuperToast.Background.RED);
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            return headers;
                        }
                    };
                    queue.add(stringRequest);

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

    private ProgressDialog dialog;
    public void showLoading() {
        dialog = new ProgressDialog(SettingsActivity.this);
        dialog.setCancelable(false);
        dialog.setMax(100);
        dialog.setMessage("Đang kiểm tra " + ip_edit_text.getText().toString() + "...");
        dialog.setTitle("Thông báo");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    public void hideLoading() {
        if (dialog.isShowing()) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.hide();
                }
            }, 500);
        }
    }

    private void hideKeyboard(EditText fromView) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fromView.getWindowToken(), 0);
    }
    private void loadSharedPreference() {
        BTSPreferences prefs = preferenceManager.getPreferences();
        ip_edit_text.setText(prefs.ip);
        port_edit_text.setText(prefs.port);
    }

    private void saveSharePreference(String ip, String port) {
        BTSPreferences prefs = preferenceManager.getPreferences();
        prefs.ip = ip;
        prefs.port = port;
        preferenceManager.setPreferences(prefs);
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

    private void showToast(String content, int color) {
        new BTSToast(this).showToast(content, color);
    }
}
