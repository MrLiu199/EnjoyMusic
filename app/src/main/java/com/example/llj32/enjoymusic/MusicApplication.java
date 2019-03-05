package com.example.llj32.enjoymusic;

import android.app.Application;
import com.example.llj32.enjoymusic.preference.Preferences;
import com.example.llj32.enjoymusic.util.ToastUtils;

public class MusicApplication extends Application {
    private static MusicApplication sMusicApp;

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
}
