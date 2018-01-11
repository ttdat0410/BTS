package vn.vnpt.ansv.bts.ui.monitor.container;

import vn.vnpt.ansv.bts.utils.EStatus;

/**
 * Created by ANSV on 11/11/2017.
 */

public interface MonitorView {
//    void showLoading();
//    void hideLoading();
    void showUpdate(boolean isUpdate);

//    AlertDialog.Builder showAlert();
//    void showToast(String content, int color);
    void showSnackBar(EStatus msServerState, String status);

}
