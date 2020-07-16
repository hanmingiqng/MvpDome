package com.han.mvpdome.presenter.inter;

import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.view.inter.IBaseView;

import java.util.Map;

public interface IMainAPresenter {


    void getList(Map<String, String> map, int responseFlag);

//    interface View extends IBaseView{
//        <T> void showToast(String e);
//        int RESPONSE_ONE = 0;
//        int RESPONSE_TWO = 1;
//        int RESPONSE_THREE = 2;
//        //成功
//        <T> void response(T response, int responseFlag);
//    }
//    interface Presenter  {
//        void getList(Map<String, String> map,int responseFlag);
//
//    }
}
