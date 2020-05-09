package com.han.mvpdome;

/**
 * xmind
 *
 * @author liyuanli
 * @data 2017/11/28
 */

public class AppConstant {

    /**
     * app的网络请求路径
     */
    public final class RequestPath {

        public static final String HTTP = "http://";

        public static final String HTTPS = "https://";
        /**
         * cxtech   https://www.didaxiaozhen.com
         */
        public static final String IMAGE_HOST = "http://www.didaxiaozhen.com/";
        public static final String HOST = "www.pomelo001.com/task_platform/";             //正式
//        public static final String API = HTTPS + HOST;
        public static final String API = HTTP + HOST;
//        public static final String HOST = "dd.didaxiaozhen.com/towntest/api/";            //测试
//        public static final String API = HTTP + HOST;
        /**
         * 图像识别
         */
        public static final String HOST_AI = "ia.didaxiaozhen.com:8081/";
        public static final String API_AI = HTTP + HOST_AI;
        public static final String API_LITE = "API-lite/";
        /**
         * 手绘地图搜索
         */
//        public static final String HOST_MAP = "103.242.169.51:8203/lcsservice/";
        public static final String HOST_MAP = "wuzhen.lcs.emapgo.cn/lcsservice/";
        public static final String HOST_IMAGE_MAP = "http://lcs.emapgo.cn/img/";
        public static final String API_MAP = HTTP + HOST_MAP;
        //地图搜索
        public static final String POI_SEARCH = "poi/search";
        public static final String POI_DETAIL = "poi/detail";
        public static final String LOCATION = "location";

        /**
         * 分享链接
         */
        //资讯详情分享
        public static final String SHARE_ZIXUN = "https://www.didaxiaozhen.com/share/zixunshare.html?phoneType=2";
        //路线详情分享
        public static final String SHARE_LUXIAN = "https://www.didaxiaozhen.com/share/luxianshare.html?phoneType=2";
        //景点详情分享
        public static final String SHARE_ATTRACTIONS = "https://www.didaxiaozhen.com/share/attractions.html?phoneType=2";

        //登录接口
        public static final String URL_LOGIN = "login";
        public static final String SEND_CODE = "sendSms";
        public static final String getBank = "company/accountLogin";

        private String apkUrl = "";

    }

    /**
     * Sp相关的
     */
    public final class SpConstants {
        public static final String USER_INFO = "xmind_user_info";

        public static final String USER = "user";//保存用户信息

        public static final String IS_FIRST = "isFirst";

        public static final String CLIENT_ID = "UserId";
        public static final String USERNAME = "UserName";
        public static final String SEX = "Sex";
        public static final String PHONE = "Phone";
        public static final String PHOTO = "Photo";
        public static final String AGE = "Age";
        public static final String BIRTHDAY = "Birthday";
        public static final String RECOMMENDATIONID = "recommendationId";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String FIRST = "first";
    }

    /**
     * intent 传参的名字
     */
    public final class IntentKey {
        public static final String CITY_NAME = "city_name";

    }

    public final class OtherSDKNumber {
        public static final String WX_APP_KEY = "wxc4305cfa36ca4412";
    }
    public final class App {
        public static final String AppName = "MvpDome";//下载安装包名
    }
    public final class Db{
        public static final String DB_NAME = "cache.db";//数据库名
        public static final int DB_VERSION = 1;//版本号
        public  static final String CACHE_TABLE = "cache";//表名
    }

}
