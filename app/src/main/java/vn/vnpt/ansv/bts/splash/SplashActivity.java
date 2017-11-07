package vn.vnpt.ansv.bts.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.monitor.Monitor;

/**
 * Created by ANSV on 11/7/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private static final int TIMER = 2000;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        tryToShowSplashImageView(TIMER);
    }

    /**
     * tryToShowSplashImageView
     *
     * Hàm hiên thị splash khi mới mở app
     * @param timer số giây để bắt đầu ẩn splash và bắt đầu quét mã
     * */
    private void tryToShowSplashImageView(int timer) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), Monitor.class));
            }
        }, timer);
    }
}
