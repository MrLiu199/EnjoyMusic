package com.example.llj32.enjoymusic;

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
import com.example.llj32.enjoymusic.adapter.SearchMusicAdapter;
import com.example.llj32.enjoymusic.http.HttpCallback;
import com.example.llj32.enjoymusic.http.HttpClient;
import com.example.llj32.enjoymusic.model.SearchMusic;
import com.example.llj32.enjoymusic.util.FileUtils;
import com.example.llj32.enjoymusic.util.ViewUtils;
import com.example.llj32.enjoymusic.util.ViewUtils.LoadStateEnum;
import com.example.llj32.enjoymusic.util.viewbind.Bind;
import com.example.llj32.enjoymusic.util.viewbind.ViewBinder;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

        mAdapter.setOnMoreClickListener(position -> {
            String[] items = new String[]{"下载"};
            final SearchMusic.Song song = searchMusicList.get(position);
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(song.getSongname());
            String path = FileUtils.getMusicDir() + FileUtils.getMp3FileName(song.getArtistname(), song.getSongname());
            File file = new File(path);
            dialog.setItems(items, (dialog1, which) -> {
                download(song);
            });
            dialog.show();
        });
    }

    private void download(final SearchMusic.Song song) {
//        new DownloadSearchedMusic(this, song) {
//            @Override
//            public void onPrepare() {
//                showProgress();
//            }
//
//            @Override
//            public void onExecuteSuccess(Void aVoid) {
//                cancelProgress();
//                ToastUtils.show(getString(R.string.now_download, song.getSongname()));
//            }
//
//            @Override
//            public void onExecuteFail(Exception e) {
//                cancelProgress();
//                ToastUtils.show(R.string.unable_to_download);
//            }
//        }.execute();
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
}
