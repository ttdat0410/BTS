package vn.vnpt.ansv.bts.ui;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import vn.vnpt.ansv.bts.BuildConfig;
import vn.vnpt.ansv.bts.R;

/**
 * Created by ANSV on 11/9/2017.
 */

public class BTSActivity extends AppCompatActivity {

    private static final String TAG = BTSActivity.class.getSimpleName();
    /*private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BuildConfig.DEBUG) {
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStart() {
        super.onStart();
        /*mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }*/
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return mDrawerToggle.onOptionsItemSelected(item) ||
        return super.onOptionsItemSelected(item);
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