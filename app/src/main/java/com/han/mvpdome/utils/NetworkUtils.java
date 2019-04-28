package com.han.mvpdome.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Locale;

/**
 * 这个类是专门检测网络状态的类。
 */


public class NetworkUtils {

    public static final String TAG = NetworkUtils.class.getSimpleName();

    public static enum NetType {
        WIFI, CMNET, CMWAP, noneNet, GWAP_3, GNET_3, UNIWAP, UNINET, CTWAP, CTNET
    }

    /**
     * 网络状态是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
            if (allNetworkInfo != null) {
                for (int i = 0; i < allNetworkInfo.length; i++) {
                    if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
            return false;
        }
        Logger.d(TAG, "context is null");
        return false;

    }

    /**
     * 这里只是判断是否有网路连接
     * 因为有的网路可以连接但是不能用。
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isAvailable();
            }
            return false;
        }
        Logger.d(TAG, "context is null");
        return false;
    }


    /**
     * 判断wifi状态是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
            return false;
        }
        Logger.d(TAG, "context is null");
        return false;
    }

    /**
     * 判断Mobile连接是否可用。
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
            return false;
        }
        Logger.d(TAG, "context is null");
        return false;
    }

    /**
     * 检测wifi是否连接
     *
     * @return 返回true 表示WIFI已连接 否则返回false
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                return activeNetworkInfo.getType();
            }
            return -1;
        }
        Logger.d(TAG, "context is null");
        return -1;

    }

    /**
     * @param context
     * @return netType返回类型
     * @方法名: getAPNType
     * @说明: 获取当前的网路状态 -1：没有网路 1：wifi网络 2：wap 网路 3：net网路
     */
    public static NetType getAPNType(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return NetType.noneNet;
            }
            int nType = activeNetworkInfo.getType();
            if (nType == ConnectivityManager.TYPE_MOBILE) {

                if (activeNetworkInfo.getExtraInfo().toLowerCase(Locale.getDefault())
                        .equals(APNNet.CMNET)) {
                    return NetType.CMNET;
                } else if (activeNetworkInfo.getExtraInfo()
                        .toUpperCase(Locale.getDefault()).equals(APNNet.CMWAP)) {
                    return NetType.CMWAP;
                    // 中国联通
                } else if (activeNetworkInfo.getExtraInfo()
                        .toLowerCase(Locale.getDefault()).equals(APNNet.GNET_3)) {
                    return NetType.GNET_3;
                } else if (activeNetworkInfo.getExtraInfo()
                        .toLowerCase(Locale.getDefault()).equals(APNNet.GWAP_3)) {
                    return NetType.GWAP_3;
                } else if (activeNetworkInfo.getExtraInfo()
                        .toLowerCase(Locale.getDefault()).equals(APNNet.UNIWAP)) {
                    return NetType.UNIWAP;
                } else if (activeNetworkInfo.getExtraInfo()
                        .toLowerCase(Locale.getDefault()).equals(APNNet.UNINET)) {
                    return NetType.UNINET;
                    // 中国联通
                } else if (activeNetworkInfo.getExtraInfo()
                        .toLowerCase(Locale.getDefault()).equals(APNNet.CTNET)) {
                    return NetType.CTNET;
                } else if (activeNetworkInfo.getExtraInfo()
                        .toLowerCase(Locale.getDefault()).equals(APNNet.CTWAP)) {
                    return NetType.CTNET;
                }

            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                return NetType.WIFI;
            }
        }

        Logger.d(TAG, "context is null");
        return NetType.noneNet;
    }

    public static class APNNet {
        /**
         * 　 中国移动cmwap
         */
        public static String CMWAP = "cmwap";
        /**
         * 中国移动cmnet
         */
        public static String CMNET = "cmnet";
        // 中国联通3GWAP设置 中国联通3G因特网设置 中国联通WAP设置 中国联通因特网设置
        // 3gwap 3gnet uniwap uninet
        /**
         * 3G wap 中国联通3gwap APN
         */
        public static String GWAP_3 = "3gwap";

        /**
         * 3G net 中国联通3gnet APN
         */
        public static String GNET_3 = "3gnet";
        /**
         * uni wap 中国联通uni wap APN
         */
        public static String UNIWAP = "uniwap";
        /**
         * uni net 中国联通uni net APN
         */
        public static String UNINET = "uninet";

        /**
         * 中国电信 net APN
         */
        public static String CTNET = "ctnet";
        /**
         * 中国电信 net APN
         */
        public static String CTWAP = "ctwap";


    }

}
