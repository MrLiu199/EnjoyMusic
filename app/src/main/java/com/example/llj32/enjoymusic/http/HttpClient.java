package com.example.llj32.enjoymusic.http;

import android.support.annotation.NonNull;
import com.example.llj32.enjoymusic.model.SearchMusic;
import com.zhy.http.okhttp.OkHttpUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class HttpClient {
    private static final String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting";
    private static final String METHOD_SEARCH_MUSIC = "baidu.ting.search.catalogSug";
    private static final String PARAM_METHOD = "method";
    private static final String PARAM_QUERY = "query";

    static {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public static void searchMusic(String keyword, @NonNull final HttpCallback<SearchMusic> callback) {
        OkHttpUtils.get().url(BASE_URL)
                .addParams(PARAM_METHOD, METHOD_SEARCH_MUSIC)
                .addParams(PARAM_QUERY, keyword)
                .build()
                .execute(new JsonCallback<SearchMusic>(SearchMusic.class) {
                    @Override
                    public void onResponse(SearchMusic response, int id) {
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onAfter(int id) {
                        callback.onFinish();
                    }
                });
    }
}
