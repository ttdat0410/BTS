package vn.vnpt.ansv.bts.ui.monitor.container;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.objects.MinStationFullObj;
import vn.vnpt.ansv.bts.ui.BTSActivity;
import vn.vnpt.ansv.bts.utils.BTSToast;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MonitorContainer extends BTSActivity implements MonitorView {

    public static List<MinStationFullObj> listAllStation = null;

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
        (((BTSApplication)getApplication()).getAppComponent()).inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);
        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        new Thread(new Runnable() {
            public void run() {
                mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        return presenter.setFragment(listAllStation, position);
                    }
                    @Override
                    public int getCount() {
                        return presenter.getCount(listAllStation);
                    }
                    @Override
                    public CharSequence getPageTitle(int position) {
                        return presenter.getPageTitle(listAllStation, position);
                    }
                });

                mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
                    @Override
                    public HeaderDesign getHeaderDesign(int page) {
                        return presenter.getHeaderDesign(listAllStation, page);

                    }
                });

                mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
                mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
            }
        }).start();

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
        dialog = new ProgressDialog(MonitorContainer.this);
        dialog.setCancelable(false);
        dialog.setMax(100);
        dialog.setMessage(getResources().getString(R.string.dialog_verify_get_sensor_list));
        dialog.setTitle("");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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

    private AlertDialog.Builder builder = null;
    @Override
    public AlertDialog.Builder showAlert() {
        builder = new AlertDialog.Builder(MonitorContainer.this);
        builder.setTitle("Thông báo");
        builder.setMessage("Mất kết nối server.");
        builder.setPositiveButton(getResources().getString(R.string.dialog_try_again_button), null);
        builder.setCancelable(false);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showLoading();
                dialog.dismiss();
                MonitorPresenterImpl.isShowAlert = true;
            }
        });
        return builder;
    }

    @Override
    public void showToast(String content, int color) {
        if (dialog.isShowing()) {
            new BTSToast(this).showToast(content, color);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listAllStation = null;
    }
}
