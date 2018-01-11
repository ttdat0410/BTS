package vn.vnpt.ansv.bts.ui.monitor;

/**
 * Created by ANSV on 11/10/2017.
 */

public interface RecyclerMonitorView {
    void startBackground(final int intervalMS);
    void stopBackground();
    void showUpdate(boolean isUpdate);
    void showConnectedServer(boolean isConnect);
}
