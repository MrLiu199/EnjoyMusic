package com.example.llj32.enjoymusic.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.*;
import com.example.llj32.enjoymusic.BuildConfig;
import com.example.llj32.enjoymusic.R;
import com.example.llj32.enjoymusic.SearchMusicActivity;
import com.example.llj32.enjoymusic.adapter.PlaylistAdapter;
import com.example.llj32.enjoymusic.model.Music;
import com.example.llj32.enjoymusic.service.AudioPlayer;
import com.example.llj32.enjoymusic.service.OnPlayerEventListener;
import com.example.llj32.enjoymusic.util.MusicUtils;
import com.example.llj32.enjoymusic.util.ToastUtils;

import java.io.File;
import java.util.List;

import static com.example.llj32.enjoymusic.util.PermissionUtils.isGranted;

//本地音乐列表
public class LocalMusicFragment extends Fragment implements OnPlayerEventListener {
    private static final String TAG = "LocalMusicFragment";
    private SearchView mSearchView;
    private RecyclerView mMusicRecyclerView;
    private List<Music> musicList;
    private PlaylistAdapter mPlaylistAdapter;
    private static final int READ_EXTERNAL_STORAGE = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        getActivity().setTitle("本地音乐");

        mSearchView = view.findViewById(R.id.music_search_view);
        mMusicRecyclerView = view
                .findViewById(R.id.music_recycler_view);
        mMusicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        reqPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE);
        musicList = MusicUtils.getMusics(getActivity());
        mPlaylistAdapter = new PlaylistAdapter(musicList);
        mPlaylistAdapter.setOnItemClickListener(position -> {
            Music music = mPlaylistAdapter.getFilteredList().get(position);
            AudioPlayer.get().addAndPlay(music);
        });
        mPlaylistAdapter.setOnMoreClickListener(position -> {
            Music music = mPlaylistAdapter.getFilteredList().get(position);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(music.getTitle());
            dialog.setItems(R.array.local_music_dialog, (dialog1, which) -> {
                switch (which) {
                    case 0:// 分享
                        shareMusic(music);
                        break;
                    case 1:// 收藏
//                        requestSetRingtone(music);
//                        break;
                    case 2:// 添加到歌单
//                        MusicInfoActivity.start(getActivity(), music);
                        ToastUtils.show(R.string.feature_not_added_yet);
                        break;
                    case 3:// 分享文件
                        shareMusicFile(music);
                        break;
                    case 4:// 删除
                        deleteMusic(music);
                        break;
                }
            });
            dialog.show();
        });
        mMusicRecyclerView.setAdapter(mPlaylistAdapter);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                mPlaylistAdapter.getFilter().filter(newText);
//                if (!TextUtils.isEmpty(newText)) {
//                    mListView.setFilterText(newText);
//                } else {
//                    mListView.clearTextFilter();
//                }
                return false;
            }
        });

        return view;
    }

    private void shareMusicFile(Music music) {
        File file = new File(music.getPath());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("audio/*");
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Nougat 24
            uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    /**
     * 分享音乐
     */
    private void shareMusic(Music music) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享音乐");
        shareIntent.putExtra(Intent.EXTRA_TEXT, String.format("这首歌#%s#挺好听哦，分享给你，希望你也喜欢~", music.getTitle()));
        getActivity().startActivity(Intent.createChooser(shareIntent, "分享音乐"));
    }

    private void deleteMusic(final Music music) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        String title = music.getTitle();
        String msg = getString(R.string.delete_music, title);
        dialog.setMessage(msg);
        dialog.setPositiveButton(R.string.delete, (dialog1, which) -> {
            File file = new File(music.getPath());
            Log.d(TAG, file.getPath());
            if (file.delete()) {
                refreshMediaFiles(music);
                new Handler(Looper.getMainLooper()).postDelayed(this::updateMusicList, 500);
                int index;
                if ((index = AudioPlayer.get().getMusicList().indexOf(music)) != -1) {
                    AudioPlayer.get().delete(index);
                }
            }
        }).setNegativeButton(R.string.cancel, null);
        dialog.show();
    }

    private void refreshMediaFiles(Music music) {
        // 刷新媒体库
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://".concat(music.getPath())));
        getActivity().sendBroadcast(intent);
    }

    private void updateMusicList() {
        musicList.clear();
        musicList.addAll(MusicUtils.getMusics(getActivity()));

        mPlaylistAdapter.getFilter().filter(mSearchView.getQuery());
        mPlaylistAdapter.notifyDataSetChanged();

        updateSubtitle();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMusicList();
    }

    @Override
    public void onChange(Music music) {
        mPlaylistAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlayerStart() {

    }

    @Override
    public void onPlayerPause() {

    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    public void reqPermission(String permission, int reqCode) {
        if (!isGranted(getActivity(), permission)) {
            requestPermissions(new String[]{permission}, reqCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtils.show(R.string.no_permission_storage);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_local_music_list, menu);

        updateSubtitle();
    }

    public void updateSubtitle() {
        int crimeCount = musicList.size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_music:
                Intent intent = SearchMusicActivity.newIntent(getActivity());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
