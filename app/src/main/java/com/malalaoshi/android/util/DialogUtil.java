package com.malalaoshi.android.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;

import com.malalaoshi.android.MalaApplication;
import com.malalaoshi.android.core.stat.StatReporter;
import com.malalaoshi.android.dialogs.PromptDialog;
import com.malalaoshi.android.dialogs.SingleEditDialog;

/**
 * Created by kang on 16/3/22.
 */
public class DialogUtil {
    private static  ProgressDialog progressDialog;

    private static void startProcessDialog(Context context, int max, int style, String message, boolean cancelable, boolean canceledOnTouchOutside){
        stopProcessDialog();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);   // 设置进度条的形式为圆形转动的进度条
        progressDialog.setCancelable(true);                              // 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.setMax(max);
        progressDialog.show();
    }


    public static void startCircularProcessDialog(Context context, String message, boolean cancelable, boolean canceledOnTouchOutside){
        startProcessDialog(context, 0, ProgressDialog.STYLE_SPINNER, message, cancelable, canceledOnTouchOutside);
    }

    public static void startHorizontalProcessDialog(Context context,int max,  String message, boolean cancelable, boolean canceledOnTouchOutside){
        startProcessDialog(context, max, ProgressDialog.STYLE_HORIZONTAL, message, cancelable, canceledOnTouchOutside);
    }

    public static void setProcess(int value){
        if (progressDialog!=null){
            progressDialog.setProgress(value);
        }
    }

    public static void stopProcessDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    
    public static void showMessageOKCancel(Context context ,String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    public static void showDoubleButtonPromptDialog(FragmentManager manager,int drawableId, String message, String leftText, String rightText,PromptDialog.OnCloseListener onCloseListener, boolean cancelable, boolean backable){
        PromptDialog promptDialog = createDoubleButtonPromptDialog(drawableId,message,  leftText, rightText, onCloseListener, cancelable, backable);
        promptDialog.show(manager, PromptDialog.class.getName());
    }

    public static void showPromptDialog(FragmentManager manager , int drawableId, String message, String btnText,PromptDialog.OnDismissListener onDismissListener , boolean cancelable, boolean backable){
        PromptDialog promptDialog = createPromptDialog(drawableId, message, btnText,onDismissListener , cancelable, backable);
        promptDialog.show(manager, PromptDialog.class.getName());
    }

    public static PromptDialog createDoubleButtonPromptDialog(int drawableId, String message, String leftText, String rightText,PromptDialog.OnCloseListener onCloseListener, boolean cancelable, boolean backable){
        PromptDialog promptDialog = PromptDialog.newInstance(drawableId, message, leftText, rightText, cancelable,backable);
        promptDialog.setOnCloseListener(onCloseListener);
        return promptDialog;
    }

    public static PromptDialog createPromptDialog(int drawableId, String message, String btnText,PromptDialog.OnDismissListener onDismissListener , boolean cancelable, boolean backable){
        PromptDialog promptDialog = PromptDialog.newInstance(drawableId, message, btnText,cancelable, backable);
        promptDialog.setDismissListener(onDismissListener);
        return promptDialog;
    }

    public static SingleEditDialog createSingleEditDialog( String message, String leftText, String rightText, SingleEditDialog.OnCloseListener onCloseListener , boolean cancelable, boolean backable){
        SingleEditDialog singleEditDialog = SingleEditDialog.newInstance(message, leftText, rightText,cancelable, backable);
        singleEditDialog.setOnCloseListener(onCloseListener);
        return singleEditDialog;
    }
    public static void showSingleEditDialog(FragmentManager manager , String message, String leftText, String rightText,SingleEditDialog.OnCloseListener onCloseListener , boolean cancelable, boolean backable){
        SingleEditDialog singleEditDialog = createSingleEditDialog(message, leftText, rightText,onCloseListener , cancelable, backable);
        singleEditDialog.show(manager, SingleEditDialog.class.getName());
    }
}
