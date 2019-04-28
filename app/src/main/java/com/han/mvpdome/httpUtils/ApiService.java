package com.han.mvpdome.httpUtils;


import com.han.mvpdome.AppConstant;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 项目名称：CutTheGold
 * 创建人：李元利
 * 创建时间：2017/8/8 18:57
 * 描述：网络请求内容
 */

public interface ApiService {

    //    /**
//     * 用户登录post请求
//     */
//    @Multipart
//    @POST(AppConstant.RequestPath.URL_LOGIN)
//    Observable<Response<LoginBean.DataBean>> login(@PartMap Map<String, RequestBody> requestBodyMap);
    //下载
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    //发送验证码
    @Multipart
    @POST(AppConstant.RequestPath.SEND_CODE)
    Observable<ResponseNodata> sendSms(@PartMap Map<String, RequestBody> requestBodyMap);

    //发送验证码
    @Multipart
    @POST(AppConstant.RequestPath.getBank)
    Observable<Response> getBank(@PartMap Map<String, RequestBody> requestBodyMap);

}
