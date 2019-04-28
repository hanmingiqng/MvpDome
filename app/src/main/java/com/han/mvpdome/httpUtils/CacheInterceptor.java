package com.han.mvpdome.httpUtils;


import com.han.mvpdome.MyApplication;
import com.han.mvpdome.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述：缓存控制
 */

public class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtils.isNetworkAvailable(MyApplication.getInstance())) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
        }

        Response response = chain.proceed(request);
        if (NetworkUtils.isNetworkAvailable(MyApplication.getInstance())) {
            int maxAge = 30; // read from cache for 1 minute
            response.newBuilder().removeHeader("Pragma")
                    //                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)//
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            response.newBuilder().removeHeader("Pragma")
                    //                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;
        //        Response originalResponse = chain.proceed(chain.request());
        //        Request request = chain.request();
        //        String cacheControl = request.cacheControl().toString();
        //        if (TextUtils.isEmpty(cacheControl)) {
        //            cacheControl = "no-cache";
        //        }
        //        return originalResponse.newBuilder()
        //                .header("Cache-Control", cacheControl)
        //                .removeHeader("Pragma").build();
    }

}

