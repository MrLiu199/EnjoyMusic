package com.example.llj32.enjoymusic;

import android.app.Application;
import com.example.llj32.enjoymusic.preference.Preferences;
import com.example.llj32.enjoymusic.util.ToastUtils;

public class MusicApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ToastUtils.init(this);
        Preferences.init(this);
    }
}
