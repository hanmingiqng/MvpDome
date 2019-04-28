package com.han.mvpdome.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.han.mvpdome.R;


/**
 * 创建人：李元利
 * 创建时间：2017/8/15 10:13
 * 描述：进度条对话框
 */

public class CustomProgressDialog extends Dialog {
    /**
     * 单例对象
     */
    private static CustomProgressDialog customProgressDialog;

    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 创建并返回对话框
     *
     */
    public static CustomProgressDialog createDialog(Context context) {
        customProgressDialog = new CustomProgressDialog(context, R.style.CustomDialog);
        customProgressDialog.setContentView(R.layout.dialog_progress);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        return customProgressDialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (customProgressDialog == null) {
            return;
        }
    }

    /**
     * 设置单例对话框的消息信息
     */
    public CustomProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.text);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return customProgressDialog;
    }

}

