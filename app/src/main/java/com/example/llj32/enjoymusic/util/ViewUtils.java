package com.example.llj32.enjoymusic.util;

import android.view.View;

/**
 * 视图工具类
 * Created by hzwangchenyan on 2016/1/14.
 */
public class ViewUtils {
    public static void changeViewState(View success, View loading, View fail, LoadStateEnum state) {
        switch (state) {
            case LOADING:
                success.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                fail.setVisibility(View.GONE);
                break;
            case LOAD_SUCCESS:
                success.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                fail.setVisibility(View.GONE);
                break;
            case LOAD_FAIL:
                success.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                fail.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 加载状态
     * Created by wcy on 2016/1/3.
     */
    public enum LoadStateEnum {
        LOADING,// 加载中
        LOAD_SUCCESS,// 加载成功
        LOAD_FAIL// 加载失败
    }
}
