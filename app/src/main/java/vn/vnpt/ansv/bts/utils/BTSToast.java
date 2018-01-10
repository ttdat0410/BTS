package vn.vnpt.ansv.bts.utils;

import android.content.Context;
import android.graphics.Color;

import com.github.johnpersano.supertoasts.SuperToast;

/**
 * Created by ANSV on 11/9/2017.
 */

public class BTSToast {

    private static Context context;
    public BTSToast(Context context) {
        this.context = context;
    }

    /**
     * Hàm hiển thị toast thông báo trạng thái hiện thời
     * @param content nội dung cần hiển thị
     * @param color màu nền cho toast
     * */
    public static void showToast(String content, int color) {
        SuperToast.cancelAllSuperToasts();
        SuperToast superToast = new SuperToast(context);
        superToast.setAnimations(Utils.TOAST_ANIMATION);
        superToast.setDuration(1000);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);
        superToast.setText(content);
        superToast.setBackground(color);
        superToast.getTextView().setTypeface(null);
        superToast.show();
    }
}
