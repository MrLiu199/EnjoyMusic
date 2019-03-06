package com.example.llj32.enjoymusic;

import android.app.Application;
import android.support.v4.util.LongSparseArray;
import com.example.llj32.enjoymusic.model.Music;
import com.example.llj32.enjoymusic.preference.Preferences;
import com.example.llj32.enjoymusic.util.ToastUtils;

public class MusicApplication extends Application {
    private static MusicApplication sMusicApp;
    private final LongSparseArray<Music> mDownloadList = new LongSparseArray<>();
    private long idDownload2Play = -1;

    public static MusicApplication getsMusicApp() {
        return sMusicApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sMusicApp = this;
        ToastUtils.init(this);
        Preferences.init(this);
    }

    public LongSparseArray<Music> getDownloadList() {
        return mDownloadList;
    }

    public long getIdDownload2Play() {
        return idDownload2Play;
    }

    public void setIdDownload2Play(long idDownload2Play) {
        this.idDownload2Play = idDownload2Play;
    }
}
