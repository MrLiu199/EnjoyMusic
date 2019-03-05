package com.example.llj32.enjoymusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.llj32.enjoymusic.executor.ControlPanel;
import com.example.llj32.enjoymusic.fragment.LocalMusicFragment;
import com.example.llj32.enjoymusic.fragment.PlayListFragment;
import com.example.llj32.enjoymusic.service.AudioPlayer;
import com.example.llj32.enjoymusic.service.PlayService;
import com.example.llj32.enjoymusic.util.viewbind.Bind;
import com.example.llj32.enjoymusic.util.viewbind.ViewBinder;

public class MusicMainActivity extends AppCompatActivity {
    protected PlayService playService;
    private ServiceConnection serviceConnection;

    private boolean isLocalListShown = false;
    private PlayListFragment mPlayListFragment;
    private LocalMusicFragment mLocalMusicFragment;
    private ControlPanel controlPanel;
    @Bind(R.id.fl_play_bar)
    private FrameLayout flPlayBar;
    @Bind(R.id.v_play_bar_playlist)
    private ImageView vPlayBarPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_main);
        ViewBinder.bind(this);

        bindService();
        switchFragment();
    }

    private void switchFragment() {
        isLocalListShown = !isLocalListShown;
        Fragment fragment;
        int resId;
        if (isLocalListShown) {
            if (mLocalMusicFragment == null) {
                mLocalMusicFragment = new LocalMusicFragment();
            }
            fragment = mLocalMusicFragment;
            resId = R.drawable.ic_play_bar_btn_playlist;
        } else {
            if (mPlayListFragment == null) {
                mPlayListFragment = new PlayListFragment();
            }
            fragment = mPlayListFragment;
            resId = R.drawable.ic_play_bar_btn_locallist;
        }
        vPlayBarPlaylist.setImageResource(resId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        serviceConnection = new PlayServiceConnection();
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void onServiceBound() {
        controlPanel = new ControlPanel(flPlayBar);
        vPlayBarPlaylist.setOnClickListener(v -> {
            switchFragment();
        });
        AudioPlayer.get().addOnPlayEventListener(controlPanel);
        if (mLocalMusicFragment != null) {
            AudioPlayer.get().addOnPlayEventListener(mLocalMusicFragment);
        }
        if (mPlayListFragment != null) {
            AudioPlayer.get().addOnPlayEventListener(mPlayListFragment);
        }
    }

    private void onServiceUnbind() {
        AudioPlayer.get().removeOnPlayEventListener(controlPanel);
    }

    private class PlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playService = ((PlayService.PlayBinder) service).getService();
            onServiceBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(getClass().getSimpleName(), "service disconnected");
            onServiceUnbind();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
    }
}
