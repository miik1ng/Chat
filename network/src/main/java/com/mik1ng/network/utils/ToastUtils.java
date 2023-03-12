package com.mik1ng.network.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mik1ng.network.R;

public class ToastUtils {
    private static Toast toastText;
    private static Toast toastView;
    private static LayoutInflater layoutInflater;

    public static void showToast(Context ctx, int resID) {
        showToast(ctx, Toast.LENGTH_SHORT, resID);
    }

    public static void showToast(Context ctx, String text) {
        showToast(ctx, Toast.LENGTH_SHORT, text);
    }

    public static void showLongToast(Context context,String text) {
        showToast(context, Toast.LENGTH_LONG, text);
    }

    public static void showToast(Context ctx, int duration, int resID) {
        showToast(ctx, duration, ctx.getString(resID));
    }

    /**
     * Toast一个图片
     */
    public static Toast showToastImage(Context ctx, int resID) {
        final Toast toast = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
        View mNextView = toast.getView();
        if (mNextView != null) {
            mNextView.setBackgroundResource(resID);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    /**
     * Toast防止时间累积
     */
    public static void showToast(final Context ctx, final int duration,
                                 final String text) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(ctx);
        }
        View layerToast = layoutInflater.inflate(R.layout.layer_toast, null);
        TextView txtToast = layerToast.findViewById(R.id.txt_toast);
        if (toastText != null) {
            txtToast.setText(text);
        } else {
            txtToast = layerToast.findViewById(R.id.txt_toast);
            toastText = new Toast(ctx);
            toastText.setView(layerToast);
            toastText.setGravity(Gravity.CENTER, 0, 0);
        }
        txtToast.setText(text);
        toastText.setView(layerToast);
        toastText.show();
    }

    /**
     * toast一个自定义布局
     */
    public static void showToast(View v, Context context) {
        if (toastView != null) {
            toastView.setView(v);
            toastView.setDuration(Toast.LENGTH_SHORT);
            toastView.show();
        } else {
            toastView = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            toastView.setView(v);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.setDuration(Toast.LENGTH_SHORT);
            toastView.show();
        }
    }

}
