package com.han.mvpdome.httpUtils;

import android.graphics.Bitmap;
import android.util.Log;

import com.han.mvpdome.MyApplication;
import com.han.mvpdome.db.CacheDao;
import com.han.mvpdome.utils.ClippingPicture;
import com.han.mvpdome.utils.Logger;
import com.han.mvpdome.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author liyuanli
 * @date 2018/7/10
 */

public class RetrofitUtil {

    private static Retrofit retrofit;
    private static ApiService apiService;

    public static ApiService getApiService(String baseUrl) {
        if (apiService == null) {
            apiService = getRetrofit(baseUrl).create(ApiService.class);
        }
        return apiService;
    }

    private static Retrofit getRetrofit(String baseUrl) {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Logger.i("RxJava", message);
                }
            });
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            //网络缓存路径文件
            File httpCacheDirectory = new File(MyApplication.getInstance().getCacheDir(),
                    "responses");
            //通过拦截器设置缓存[拦截操作，包括控制缓存的最大生命值，控制缓存的过期时间]
            CacheInterceptor cacheInterceptor = new CacheInterceptor();
            OkHttpClient client = new OkHttpClient.Builder()
                    //log请求参数
                    .addInterceptor(interceptor)
                    //设置`缓存路径以及缓存文件大小
                    .cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024))
                    //网络请求缓存
                    .addInterceptor(cacheInterceptor)
                    .addNetworkInterceptor(cacheInterceptor)
                    //设置连接时间， 读 取时间,默认为10s，
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()//
                    .client(client)//
                    .baseUrl(baseUrl)//
                    .addConverterFactory(GsonConverterFactory.create())//
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        return retrofit;
    }

    /**
     * 对网络接口返回的Response进行分割操作
     *
     * @param response
     * @param <T>
     * @return
     */
    public <T> Observable<T> flatResponse(final Response<T> response) {
        return Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (response.isSuccess()) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(response.getData());
                    }
                } else {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(new APIException(response.success, response.message));
                    }
                    return;
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }

            }
        });
    }


    /**
     * 自定义异常，当接口返回的{@link Response#success}时，需要跑出此异常
     * eg：登录时验证码错误；参数为传递等
     */
    public static class APIException extends Exception {

        public boolean success;
        public String message;

        public APIException(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public boolean getSuccess() {
            return success;
        }

    }

    /**
     * http://www.jianshu.com/p/e9e03194199e
     * <p/>
     * Transformer实际上就是一个Func1<Observable<T>, Observable<R>>，
     * 换言之就是：可以通过它将一种类型的Observable转换成另一种类型的Observable，
     * 和调用一系列的内联操作符是一模一样的。
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    <T> Observable.Transformer<Response<T>, T> applySchedulers() {
        return (Observable.Transformer<Response<T>, T>) transformer;
    }

    @SuppressWarnings("unchecked")
    private final Observable.Transformer transformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1() {
                        @Override
                        public Object call(Object response) {
                            return flatResponse((Response<Object>) response);
                        }
                    });

        }
    };

    /**
     * 当{@link ApiService}中接口的注解为{@link retrofit2.http.Multipart}时，参数为{@link RequestBody}
     * 生成对应的RequestBody
     *
     * @param param
     * @return
     */
    protected RequestBody createRequestBody(String param) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), param);
    }

    protected RequestBody createFormData(String param) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), "this_is_username");
    }

    protected RequestBody createRequestBody(File param) {
        return RequestBody.create(MediaType.parse("image/*"), param);
    }

    protected Map<String, RequestBody> generateRequestBody(Map<String, String> requestDataMap) {
        if (requestDataMap == null) {
            requestDataMap = new HashMap<>();
        }
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (String key : requestDataMap.keySet()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    requestDataMap.get(key) == null ? "" : requestDataMap.get(key));
            requestBodyMap.put(key, requestBody);
        }
        long timestamp = System.currentTimeMillis();
        String apiKey = "336baff6-fcd4-485b-96dc-cbde95374846";
        String token = MD5(timestamp + apiKey + timestamp);

        RequestBody requestBodyToken = RequestBody.create(MediaType.parse("multipart/form-data"), token);
        requestBodyMap.put("token", requestBodyToken);
        RequestBody requestBodyTimestamp = RequestBody.create(MediaType.parse("multipart/form-data"),
                timestamp + "");
        requestBodyMap.put("timestamp", requestBodyTimestamp);
        return requestBodyMap;
    }


    /**
     * 已二进制传递图片文件，对图片文件进行了压缩
     *
     * @param path 文件路径
     * @return
     */
    protected RequestBody createPictureRequestBody(String path) {
        Bitmap bitmap = ClippingPicture.decodeResizeBitmapSd(path, 400, 800);
        return RequestBody.create(MediaType.parse("image/*"), ClippingPicture.bitmapToBytes
                (bitmap));
    }


    //post缓存
    private static final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String url = request.url().toString(); //获取请求URL
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            String params = buffer.readString(Charset.forName("UTF-8")); //获取请求参数
            okhttp3.Response response;
            if (NetworkUtils.isNetworkAvailable(MyApplication.getInstance())) {
                int maxAge = 60 * 60 * 24;
                //如果网络正常，执行请求。
                okhttp3.Response originalResponse = chain.proceed(request);
                //获取MediaType，用于重新构建ResponseBody
                MediaType type = originalResponse.body().contentType();
                //获取body字节即响应，用于存入数据库和重新构建ResponseBody
                byte[] bs = originalResponse.body().bytes();
                response = originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        //重新构建body，原因在于body只能调用一次，之后就关闭了。
                        .body(ResponseBody.create(type, bs))
                        .build();
                //将响应插入数据库
                CacheDao.getInstance(MyApplication.getInstance()).insertResponse(url, params, new String(bs));
            } else {
                //没有网络的时候，由于Okhttp没有缓存post请求，所以不要调用chain.proceed(request)，会导致连接不上服务器而抛出异常（504）
                String b = CacheDao.getInstance(MyApplication.getInstance()).queryResponse(url, params); //读出响应
                Log.d("OkHttp", "request:" + url);
                Log.d("OkHttp", "request method:" + request.method());
                Log.d("OkHttp", "response body:" + null != b ? b + "" : "缓存没查到");
                int maxStale = 60 * 60 * 24 * 28;
                //构建一个新的response响应结果
                response = new okhttp3.Response.Builder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .body(ResponseBody.create(MediaType.parse("application/json"), null != b ? b.getBytes() : "".getBytes()))
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .build();
            }
            return response;
        }
    };

    /**
     * MD5 加密
     *
     * @param strSrc
     * @return
     * @throws Exception
     */
    private static String MD5(String strSrc) {
        byte[] data = strSrc.getBytes(Charset.forName("utf-8"));
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(data);
        byte b[] = md5.digest();
        int i;

        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }

        return buf.toString();
    }
}

