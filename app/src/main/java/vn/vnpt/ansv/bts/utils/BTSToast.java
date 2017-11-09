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

    public static void showToast(String text, int color) {
        SuperToast.cancelAllSuperToasts();
        SuperToast superToast = new SuperToast(context);
        superToast.setAnimations(Utils.TOAST_ANIMATION);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);
        superToast.setText(text);
        superToast.setBackground(color);
        superToast.getTextView().setTypeface(null);
        superToast.show();
    }
}
