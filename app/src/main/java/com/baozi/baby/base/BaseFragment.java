package com.baozi.baby.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    protected boolean isVisible = false;//当前Fragment是否可见
    protected boolean isInitView = false;//是否与View建立起映射关系
    protected boolean isFirstLoad = true;//是否是第一次加载数据

    private View convertView;
    private Unbinder unbinder;

    /**
     * @return fragment布局
     */
    protected abstract int getLayoutId();

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void lazyLoadData();

    public View getConvertView() {
        return convertView;
    }

    private void loadFristData() {
        if (isInitView && isVisible && isFirstLoad) {
            isFirstLoad = false;
            lazyLoadData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, convertView);
        initView();
        isInitView = true;
        loadFristData();
        return convertView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisible = true;
            loadFristData();
        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
