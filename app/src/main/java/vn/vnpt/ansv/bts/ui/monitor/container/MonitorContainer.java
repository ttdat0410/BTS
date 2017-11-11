package vn.vnpt.ansv.bts.ui.monitor.container;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.objects.MinStationFullObj;
import vn.vnpt.ansv.bts.ui.BTSActivity;
import vn.vnpt.ansv.bts.ui.monitor.RecyclerMonitorFragment;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MonitorContainer extends BTSActivity implements MonitorView {

    public static List<MinStationFullObj> listAllStation = null;
    private int numOfPage = 0;

    @Inject
    MonitorPresenter presenter;

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    public static void launch(Context context, List<MinStationFullObj> listStation) {
        listAllStation = listStation;
        Intent intent = new Intent(context, MonitorContainer.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        setTitle("");
        ButterKnife.bind(this);
        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // xet so luong pages
        if (listAllStation != null) {
            numOfPage = listAllStation.size();
        } else {
            numOfPage = 0;
        }

        new Thread(new Runnable() {
            public void run() {
                mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {

                        for (int i = 0; i<numOfPage; i++) {
                            if (i == position % numOfPage) {
                                int stationId = listAllStation.get(i).getStationInfo().getStationId();
                                return RecyclerMonitorFragment.newInstance(stationId);
                            }
                        }
                        return null;
                    }
                    @Override
                    public int getCount() {
                        return numOfPage;
                    }

                    @Override
                    public CharSequence getPageTitle(int position) {

                        for (int i = 0; i<numOfPage; i++) {
                            if (i == position % numOfPage) {
                                return listAllStation.get(i).getStationInfo().getStationName();
                            }
                        }
                        return "";
                    }
                });

                mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
                    @Override
                    public HeaderDesign getHeaderDesign(int page) {
                        switch (page) {
                            case 0:
                                return HeaderDesign.fromColorResAndUrl(
                                        R.color.sl_terbium_green,
                                        "http://buudienhospital.vn/wp-content/uploads/2017/04/3-1237x386.jpg");
                            case 1:
                                return HeaderDesign.fromColorResAndUrl(
                                        R.color.sl_red_orange,
                                        "http://buudienhospital.vn/wp-content/uploads/2017/04/tgd_vnpt.jpg");
//                                "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
                            case 2:
                                return HeaderDesign.fromColorResAndUrl(
                                        R.color.cyan,
                                        "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                            case 3:
                                return HeaderDesign.fromColorResAndUrl(
                                        R.color.red,
                                        "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                        }

                        return HeaderDesign.fromColorResAndUrl(
                                R.color.sl_terbium_green,
                                "http://buudienhospital.vn/wp-content/uploads/2017/04/3-1237x386.jpg");
                    }
                });

                mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
                mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
            }
        }).start();

        /*mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                for (int i = 0; i<numOfPage; i++) {
                    if (i == position % numOfPage) {
                        int stationId = listAllStation.get(i).getStationInfo().getStationId();
                        return RecyclerMonitorFragment.newInstance(stationId);
                    }
                }
                return null;
            }
            @Override
            public int getCount() {
                return numOfPage;
            }

            @Override
            public CharSequence getPageTitle(int position) {

                for (int i = 0; i<numOfPage; i++) {
                    if (i == position % numOfPage) {
                        return listAllStation.get(i).getStationInfo().getStationName();
                    }
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.sl_terbium_green,
                                "http://buudienhospital.vn/wp-content/uploads/2017/04/3-1237x386.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.sl_red_orange,
                                "http://buudienhospital.vn/wp-content/uploads/2017/04/tgd_vnpt.jpg");
//                                "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                return HeaderDesign.fromColorResAndUrl(
                        R.color.sl_terbium_green,
                        "http://buudienhospital.vn/wp-content/uploads/2017/04/3-1237x386.jpg");
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());*/

        final View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private ProgressDialog dialog;
    @Override
    public void showLoading() {
        Log.i("0x00", "loading...");
        dialog = new ProgressDialog(MonitorContainer.this);
        dialog.setCancelable(false);
        dialog.setMax(100);
        dialog.setMessage(getResources().getString(R.string.dialog_verify_get_sensor_list));
        dialog.setTitle("");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
    }


    @Override
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        numOfPage = 0;
        listAllStation = null;
    }
}

