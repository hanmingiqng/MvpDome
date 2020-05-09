package com.han.mvpdome.httpUtils;

import com.han.mvpdome.AppConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

    import retrofit2.Call;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述：网络请求工具类
 */

public class ApiWrapper extends RetrofitUtil {

    Observable.Transformer schedulersTransformer() {  //线程转换
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable) observable).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    ArrayList<Call> calllist = new ArrayList<>();

    public void cancelcall() {
        for (Call call : calllist) {
            call.cancel();
        }
    }

    //发送验证码
    public Observable<ResponseNodata> sendSms(String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        return getApiService(AppConstant.RequestPath.API).sendSms(generateRequestBody(params));
    }
    //获取银行卡
    public Observable<Response> getBank( Map<String, String> params) {
        return getApiService(AppConstant.RequestPath.API).getBank(generateRequestBody(params));
    }

}
