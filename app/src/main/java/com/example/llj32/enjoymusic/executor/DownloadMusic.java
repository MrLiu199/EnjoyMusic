package com.example.llj32.enjoymusic.executor;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.webkit.MimeTypeMap;
import com.example.llj32.enjoymusic.MusicApplication;
import com.example.llj32.enjoymusic.R;
import com.example.llj32.enjoymusic.model.Music;
import com.example.llj32.enjoymusic.preference.Preferences;
import com.example.llj32.enjoymusic.util.FileUtils;
import com.example.llj32.enjoymusic.util.NetworkUtils;
import com.example.llj32.enjoymusic.util.ToastUtils;

/**
 * Created by hzwangchenyan on 2017/1/20.
 */
public abstract class DownloadMusic implements IExecutor<Void> {
    private Context mContext;

    public DownloadMusic(Context context) {
        mContext = context;
    }

    @Override
    public void execute() {
        checkNetwork();
    }

    private void checkNetwork() {
        boolean mobileNetworkDownload = Preferences.enableMobileNetworkDownload();
        if (NetworkUtils.isActiveNetworkMobile(mContext) && !mobileNetworkDownload) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.tips);
            builder.setMessage(R.string.download_tips);
            builder.setPositiveButton(R.string.download_tips_sure, (dialog, which) -> downloadWrapper());
            builder.setNegativeButton(R.string.cancel, null);

            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            downloadWrapper();
        }
    }

    private void downloadWrapper() {
        onPrepare();
        download();
    }

    protected abstract void download();

    protected void downloadMusic(String url, String artist, String title, String coverPath, boolean isPlayAfterDownload) {
        try {
            String fileName = FileUtils.getMp3FileName(artist, title);
            Uri uri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(FileUtils.getFileName(artist, title));
            request.setDescription("正在下载…");
            request.setDestinationInExternalPublicDir(FileUtils.getRelativeMusicDir(), fileName);
            request.setMimeType(MimeTypeMap.getFileExtensionFromUrl(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(false); // 不允许漫游

            MusicApplication musicApp = MusicApplication.getsMusicApp();
            DownloadManager downloadManager = (DownloadManager) musicApp.getSystemService(Context.DOWNLOAD_SERVICE);
            long id = downloadManager.enqueue(request);
            String musicAbsPath = FileUtils.getMusicDir().concat(fileName);
            Music downloadMusicInfo = new Music();
            downloadMusicInfo.setTitle(title);
            downloadMusicInfo.setArtist(artist);
            downloadMusicInfo.setPath(musicAbsPath);
            musicApp.getDownloadList().put(id, downloadMusicInfo);
            if (isPlayAfterDownload) {
                musicApp.setIdDownload2Play(id);
            }
        } catch (Throwable th) {
            th.printStackTrace();
            ToastUtils.show("下载失败");
        }
    }
}
