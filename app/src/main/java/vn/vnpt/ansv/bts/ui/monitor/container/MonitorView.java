package vn.vnpt.ansv.bts.ui.monitor.container;

import android.app.AlertDialog;

/**
 * Created by ANSV on 11/11/2017.
 */

public interface MonitorView {
    void showLoading();
    void hideLoading();
    AlertDialog.Builder showAlert();
    void showToast(String content, int color);
}
