package com.example.llj32.enjoymusic.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.llj32.enjoymusic.R;
import com.example.llj32.enjoymusic.adapter.SearchMusicAdapter;
import com.example.llj32.enjoymusic.executor.DownloadMusic;
import com.example.llj32.enjoymusic.http.HttpCallback;
import com.example.llj32.enjoymusic.http.HttpClient;
import com.example.llj32.enjoymusic.model.DownloadInfo;
import com.example.llj32.enjoymusic.model.SearchMusic;
import com.example.llj32.enjoymusic.service.AudioPlayer;
import com.example.llj32.enjoymusic.util.FileUtils;
import com.example.llj32.enjoymusic.util.ToastUtils;
import com.example.llj32.enjoymusic.util.ViewUtils;
import com.example.llj32.enjoymusic.util.ViewUtils.LoadStateEnum;
import com.example.llj32.enjoymusic.util.viewbind.Bind;
import com.example.llj32.enjoymusic.util.viewbind.ViewBinder;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//搜索网络歌曲
public class SearchMusicActivity extends AppCompatActivity {
    @Bind(R.id.rv_search_result)
    private RecyclerView mMusicRecyclerView;
    @Bind(R.id.tv_loading)
    private TextView tvLoading;
    @Bind(R.id.tv_load_fail)
    private TextView tvLoadFail;
    private List<SearchMusic.Song> searchMusicList = new ArrayList<>();
    private SearchMusicAdapter mAdapter = new SearchMusicAdapter(searchMusicList);
    private Handler handler = new Handler(Looper.getMainLooper());

    public static Intent newIntent(Context context) {
        return new Intent(context, SearchMusicActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_music);
        ViewBinder.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMusicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMusicRecyclerView.setAdapter(mAdapter);
        tvLoadFail.setText(R.string.search_empty);
        mAdapter.setOnItemClickListener(position -> {
            final SearchMusic.Song song = searchMusicList.get(position);
            String path = FileUtils.getMusicDir() + FileUtils.getMp3FileName(song.getArtistname(), song.getSongname());
            File file = new File(path);
            if (file.exists()) {
                AudioPlayer.get().addAndPlay(song);
            } else {
                download(song, true);
            }
        });
        mAdapter.setOnMoreClickListener(position -> {
            String[] items = new String[]{"下载"};
            final SearchMusic.Song song = searchMusicList.get(position);
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(song.getSongname());
            String path = FileUtils.getMusicDir() + FileUtils.getMp3FileName(song.getArtistname(), song.getSongname());
            File file = new File(path);
            dialog.setItems(items, (dialog1, which) -> {
                if (file.exists()) {
                    ToastUtils.show("该歌曲已下载过");
                } else {
                    download(song, false);
                }
            });
            dialog.show();
        });
    }

    private void download(final SearchMusic.Song song, boolean isPlayAfterDownload) {
        CustomProgressDialog progressDialog = new CustomProgressDialog(this);
        new DownloadSearchedMusic(this, song, isPlayAfterDownload) {
            @Override
            public void onPrepare() {
                progressDialog.showProgress();
            }

            @Override
            public void onExecuteSuccess(Void aVoid) {
                progressDialog.cancelProgress();
                ToastUtils.show(getString(R.string.now_download, song.getSongname()));
            }

            @Override
            public void onExecuteFail(Exception e) {
                progressDialog.cancelProgress();
                ToastUtils.show(R.string.unable_to_download);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_music, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.onActionViewExpanded();
        searchView.setQueryHint(getString(R.string.search_tips));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ViewUtils.changeViewState(mMusicRecyclerView, tvLoading, tvLoadFail, LoadStateEnum.LOADING);
                searchMusic(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                mPlaylistAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setSubmitButtonEnabled(true);
        try {
            Field field = searchView.getClass().getDeclaredField("mGoButton");
            field.setAccessible(true);
            ImageView mGoButton = (ImageView) field.get(searchView);
            mGoButton.setImageResource(R.drawable.ic_menu_search);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchMusic(String keyword) {
        HttpClient.searchMusic(keyword, new HttpCallback<SearchMusic>() {
            @Override
            public void onSuccess(SearchMusic response) {
                if (response == null || response.getSong() == null) {
                    ViewUtils.changeViewState(mMusicRecyclerView, tvLoading, tvLoadFail, LoadStateEnum.LOAD_FAIL);
                    return;
                }
                ViewUtils.changeViewState(mMusicRecyclerView, tvLoading, tvLoadFail, LoadStateEnum.LOAD_SUCCESS);
                searchMusicList.clear();
                searchMusicList.addAll(response.getSong());
                mAdapter.notifyDataSetChanged();
                mMusicRecyclerView.requestFocus();
                handler.post(() -> mMusicRecyclerView.scrollToPosition(0));
            }

            @Override
            public void onFail(Exception e) {
                ViewUtils.changeViewState(mMusicRecyclerView, tvLoading, tvLoadFail, LoadStateEnum.LOAD_FAIL);
            }
        });
    }

    public static abstract class DownloadSearchedMusic extends DownloadMusic {
        private SearchMusic.Song mSong;
        private boolean isPlayAfterDownload;

        public DownloadSearchedMusic(Context context, SearchMusic.Song song, boolean isPlayAfterDownload) {
            super(context);
            mSong = song;
            this.isPlayAfterDownload = isPlayAfterDownload;
        }

        @Override
        protected void download() {
            final String artist = mSong.getArtistname();
            final String title = mSong.getSongname();

            // 获取歌曲下载链接
            HttpClient.getMusicDownloadInfo(mSong.getSongid(), new HttpCallback<DownloadInfo>() {
                @Override
                public void onSuccess(DownloadInfo response) {
                    if (response == null || response.getBitrate() == null) {
                        onFail(null);
                        return;
                    }

                    downloadMusic(response.getBitrate().getFile_link(), artist, title, null, isPlayAfterDownload);
                    onExecuteSuccess(null);
                }

                @Override
                public void onFail(Exception e) {
                    onExecuteFail(e);
                }
            });
        }
    }

    public static class CustomProgressDialog {
        private ProgressDialog progressDialog;
        private Context context;

        public CustomProgressDialog(Context context) {
            this.context = context;
        }

        public void showProgress() {
            showProgress(context.getString(R.string.loading));
        }

        public void showProgress(String message) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false);
            }
            progressDialog.setMessage(message);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        public void cancelProgress() {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        }
    }
}
