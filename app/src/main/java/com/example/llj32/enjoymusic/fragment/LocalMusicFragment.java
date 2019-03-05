package com.example.llj32.enjoymusic.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.*;
import com.example.llj32.enjoymusic.R;
import com.example.llj32.enjoymusic.SearchMusicActivity;
import com.example.llj32.enjoymusic.adapter.PlaylistAdapter;
import com.example.llj32.enjoymusic.model.Music;
import com.example.llj32.enjoymusic.service.AudioPlayer;
import com.example.llj32.enjoymusic.service.OnPlayerEventListener;
import com.example.llj32.enjoymusic.util.MusicUtils;

import java.io.File;
import java.util.List;

//本地音乐列表
public class LocalMusicFragment extends Fragment implements OnPlayerEventListener {
    private SearchView mSearchView;
    private RecyclerView mMusicRecyclerView;
    private List<Music> musicList;
    private PlaylistAdapter mPlaylistAdapter;

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
        musicList = MusicUtils.scanMusic(getActivity());
        mPlaylistAdapter = new PlaylistAdapter(musicList);
        mPlaylistAdapter.setOnItemClickListener(position -> {
            Music music = musicList.get(position);
            AudioPlayer.get().addAndPlay(music);
        });
        mPlaylistAdapter.setOnMoreClickListener(position -> {
            Music music = musicList.get(position);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(music.getTitle());
            dialog.setItems(R.array.local_music_dialog, (dialog1, which) -> {
                switch (which) {
                    case 0:// 分享
                        shareMusic(music);
                        break;
//                    case 1:// 设为铃声
//                        requestSetRingtone(music);
//                        break;
//                    case 2:// 查看歌曲信息
//                        MusicInfoActivity.start(getContext(), music);
//                        break;
                    case 3:// 删除
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

    /**
     * 分享音乐
     */
    private void shareMusic(Music music) {
        File file = new File(music.getPath());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("audio/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    private void deleteMusic(final Music music) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        String title = music.getTitle();
        String msg = getString(R.string.delete_music, title);
        dialog.setMessage(msg);
        dialog.setPositiveButton(R.string.delete, (dialog1, which) -> {
            File file = new File(music.getPath());
            if (file.delete()) {
                // 刷新媒体库
                Intent intent =
                        new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://".concat(music.getPath())));
                getContext().sendBroadcast(intent);
            }
        });
        dialog.setNegativeButton(R.string.cancel, null);
        dialog.show();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_local_music_list, menu);

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
