package com.han.mvpdome.view.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.Toast;

import com.han.mvpdome.R;
import com.han.mvpdome.base.BaseActivity;
import com.han.mvpdome.base.BaseFragment;
import com.han.mvpdome.inter.PermissionsBase;
import com.han.mvpdome.presenter.PresenterBase;
import com.han.mvpdome.presenter.impl.MainAPresenterImpl;
import com.han.mvpdome.utils.ToastUtil;
import com.han.mvpdome.view.fragment.Mian1Fragment;
import com.han.mvpdome.view.fragment.Mian2Fragment;
import com.han.mvpdome.view.fragment.Mian3Fragment;
import com.han.mvpdome.view.fragment.MianFragment;
import com.han.mvpdome.view.inter.IMainAView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements IMainAView {

    @BindView(R.id.vp_main)
    ViewPager vpMain;
    private MainAPresenterImpl mainAPresenter;
    private ArrayList<BaseFragment> fragmentList;
    private BaseFragment oneFragment, mian1Fragment, mian2Fragment, mian3Fragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public PresenterBase getPresenterBase() {
        return new MainAPresenterImpl(this, this);
    }

    @OnClick(R.id.btn)
    public void OnClick() {
        requestEachCombined(new PermissionsBase() {
            @Override
            public void isOK() {
                ToastUtil.showShortToast(mContext, "权限申请成功");
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE);
    }

    @Override
    public void initView() {
        fragmentList = new ArrayList<BaseFragment>();
//        for (int x = 0; x < 4; x++) {
        oneFragment = new MianFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", "传递到的数据" + 1);
        oneFragment.setArguments(bundle);
        mian1Fragment = new Mian1Fragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("data", "传递到的数据" + 2);
        mian1Fragment.setArguments(bundle1);
        mian2Fragment = new Mian2Fragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("data", "传递到的数据" + 3);
        mian2Fragment.setArguments(bundle2);
        mian3Fragment = new Mian3Fragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("data", "传递到的数据" + 4);
        mian3Fragment.setArguments(bundle3);
        fragmentList.add(oneFragment);
        fragmentList.add(mian1Fragment);
        fragmentList.add(mian2Fragment);
        fragmentList.add(mian3Fragment);
//        }
        vpMain.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
        requestEachCombined(new PermissionsBase() {
            @Override
            public void isOK() {
                ToastUtil.showShortToast(mContext, "权限申请成功");
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE);
    }


    //当前选中的项
    int currenttab = -1;

    @Override
    public void initData() {
        mainAPresenter = (MainAPresenterImpl) presenterBase;
        //        设置回调
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("checkCode", 111 + "");
        dataMap.put("loginName", "ceshi_han");
        dataMap.put("password ", "123456");
//        mainAPresenter.getList(dataMap);

    }


    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {

        public MyFrageStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        /**
         * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
         */
        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);//这句话要放在最前面，否则会报错
            //获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
            int currentItem = vpMain.getCurrentItem();
            if (currentItem == currenttab) {
                return;
            }
            currenttab = vpMain.getCurrentItem();
        }

    }

    @Override
    public boolean haveEventBus() {
        return false;
    }

    @Override
    public <T> T request(int requestFlag) {
        Toast.makeText(this, "" + requestFlag, Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public <T> T request1(String requestFlag) {
        Toast.makeText(this, "" + requestFlag, Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public <T> void response(T response, int responseFlag) {

    }

}
