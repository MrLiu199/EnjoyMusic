package com.example.llj32.enjoymusic.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.example.llj32.enjoymusic.MusicApplication;
import com.example.llj32.enjoymusic.R;
import com.example.llj32.enjoymusic.model.Music;
import com.example.llj32.enjoymusic.service.AudioPlayer;
import com.example.llj32.enjoymusic.util.ToastUtils;

/**
 * 下载完成广播接收器
 * Created by hzwangchenyan on 2015/12/30.
 */
public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        MusicApplication musicApp = MusicApplication.getsMusicApp();
        Music downloadMusicInfo = musicApp.getDownloadList().get(id);
        if (downloadMusicInfo != null) {
            ToastUtils.show(context.getString(R.string.download_success, downloadMusicInfo.getTitle()));
            if (musicApp.getIdDownload2Play() == id) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    AudioPlayer.get().addAndPlay(downloadMusicInfo.getTitle());
                    musicApp.setIdDownload2Play(-1);
                }, 500);
            }

//            String musicPath = downloadMusicInfo.getMusicPath();
//            String coverPath = downloadMusicInfo.getCoverPath();
//            if (!TextUtils.isEmpty(musicPath) && !TextUtils.isEmpty(coverPath)) {
//                // 设置专辑封面
//                File musicFile = new File(musicPath);
//                File coverFile = new File(coverPath);
//                if (musicFile.exists() && coverFile.exists()) {
//                    ID3Tags id3Tags = new ID3Tags.Builder().setCoverFile(coverFile).build();
//                    ID3TagUtils.setID3Tags(musicFile, id3Tags, false);
//                }
//            }
        }
    }
}
