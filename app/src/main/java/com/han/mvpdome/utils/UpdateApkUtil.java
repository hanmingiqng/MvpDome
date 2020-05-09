package com.han.mvpdome.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.han.mvpdome.inter.UpdateApkinterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2019-7-10  17:13
 *
 * @name hanmingqing
 * @user hanmq
 */
public class UpdateApkUtil {

    private Context mContext;
    private UpdateApkinterface updateApkinterface;

    public UpdateApkUtil(Context mContext, UpdateApkinterface updateApkinterface) {
        this.mContext = mContext;
        this.updateApkinterface = updateApkinterface;
    }

    public void updateApkUtil(String versionNumber) {
//        判断版本是否要更新
        if (DeviceUtil.getVersionName(mContext).equals(versionNumber)) {

        } else {
            showDialog();

        }
    }

    private AlertDialog dialog;

    private void showDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(mContext)
                    .setTitle("更新提示")//设置对话框的标题
                    .setMessage("是否下载最新版本？")//设置对话框的内容
                    //设置对话框的按钮
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            取消更新
                            updateApkinterface.isNo();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            otherAppMall();

                        }
                    }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        } else {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }


    }

    // 判断市场是否存在的方法
    public boolean isAvilible(String packageName) {
        final PackageManager packageManager = mContext.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        List<String> pName = new ArrayList<>();// 用于存储所有已安装程序的包名
// 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }


    //判断是否有qq应用宝 360  豌豆荚 应用商城
    private void otherAppMall() {
        if (isAvilible("com.tencent.android.qqdownloader")) {
            launchAppDetail("com.tencent.android.qqdownloader");
        } else if (isAvilible("com.qihoo.appstore")) {
            launchAppDetail("com.qihoo.appstore");
        } else if (isAvilible("com.wandoujia.phoenix2")) {
            launchAppDetail("com.wandoujia.phoenix2");
//        } else if (isAvilible(this, "com.baidu.appsearch")) {
//            百度没有上传
//            launchAppDetail(getApplicationContext(), getPackageName(), "com.baidu.appsearch");
        } else {
            intit_getClick();
        }
    }

    private void intit_getClick() {
//        判断是否是华为
        if (RomUtil.isEmui()) {
            if (isAvilible("com.huawei.appmarket")) {
//                判断华为应用商城是否存在
                launchAppDetail("com.huawei.appmarket");
            } else {
                otherAppMallurl();
            }
        } else if (RomUtil.isMiui()) {
//            判断是否是小米
            if (isAvilible("com.xiaomi.market")) {
//                判断小米应用商城是否存在
                launchAppDetail("com.xiaomi.market");
            } else {
                otherAppMallurl();
            }
        } else if (RomUtil.isOppo()) {
//            判断是否是oppo
            if (isAvilible("com.oppo.market")) {
//                判断oppo商城是否存在
                launchAppDetail("com.oppo.market");
            } else {
                otherAppMallurl();
            }
        } else if (RomUtil.isVivo()) {
//            判断是否是 vivo
            if (isAvilible("com.bbk.appstore")) {
//                判断vivo商城是否存在
                launchAppDetail("com.bbk.appstore");
            } else {
                otherAppMallurl();
            }
        } else {
            otherAppMallurl();
        }

    }

    /**
     * 启动到app详情界面
     *
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public void launchAppDetail(String marketPkg) {
        try {
            if (TextUtils.isEmpty(mContext.getPackageName()))
                return;
            Uri uri = Uri.parse("market://details?id=com.cxtech.ticktown");
//            Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //跳转到腾讯的应用宝
    private void otherAppMallurl() {
        Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.cxtech.ticktown");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(it);
    }
}
