package com.example.llj32.enjoymusic.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.llj32.enjoymusic.R;
import com.example.llj32.enjoymusic.adapter.PlaylistAdapter;
import com.example.llj32.enjoymusic.database.SongListLab;
import com.example.llj32.enjoymusic.model.Music;
import com.example.llj32.enjoymusic.model.SongList;
import com.example.llj32.enjoymusic.model.SonglistItem;
import com.example.llj32.enjoymusic.service.AudioPlayer;
import com.example.llj32.enjoymusic.service.OnPlayerEventListener;

import java.util.List;

//播放列表 | 歌单中歌曲列表
public class PlayListFragment extends Fragment implements OnPlayerEventListener {
    private RecyclerView mMusicRecyclerView;
    private PlaylistAdapter mPlaylistAdapter;

    public List<Music> mPlaylistMusics;
    private boolean isSonglist = false;//歌单还是播放列表
    private SongList mSonglist;
    private List<Music> mSonglistMusics;

    public static PlayListFragment newInstance(SongList songList) {
        PlayListFragment fragment = new PlayListFragment();
        fragment.isSonglist = true;
        fragment.mSonglist = songList;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_list, container, false);
        getActivity().setTitle(isSonglist ? mSonglist.getSongListName() : "播放列表");

        mMusicRecyclerView = view.findViewById(R.id.music_recycler_view);
        mMusicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (isSonglist) {
            mSonglistMusics = SongListLab.get(getActivity()).getSonglistMusics(mSonglist);
        } else {
            mPlaylistMusics = AudioPlayer.get().getMusicList();
        }
        mPlaylistAdapter = new PlaylistAdapter(isSonglist ? mSonglistMusics : mPlaylistMusics);
        mPlaylistAdapter.setOnItemClickListener(position -> {
            if (isSonglist) {
                AudioPlayer.get().addAndPlay(mSonglistMusics.get(position));
            } else {
                AudioPlayer.get().play(position);
            }
        });
        mPlaylistAdapter.setOnMoreClickListener(position -> {
            String[] items = new String[]{"移除"};
            Music music = isSonglist ? mSonglistMusics.get(position) : mPlaylistMusics.get(position);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(music.getTitle());
            dialog.setItems(items, (dialog1, which) -> {
                if (isSonglist) {
                    SongListLab.get(getActivity()).deleteSonglistItem(new SonglistItem(mSonglist.getSongListId(),
                            music.getSongId()));
                    mSonglistMusics.remove(position);
                } else {
                    AudioPlayer.get().delete(position);
                }
                mPlaylistAdapter.notifyDataSetChanged();
                updateSubtitle();
            });
            dialog.show();
        });
        mMusicRecyclerView.setAdapter(mPlaylistAdapter);

        AudioPlayer.get().addOnPlayEventListener(this);

        updateSubtitle();

        return view;
    }

    private void updateSubtitle() {
        int musicCount = isSonglist ? mSonglistMusics.size() : mPlaylistMusics.size();
        String subtitle = getString(R.string.subtitle_format, musicCount);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(subtitle);
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
    public void onDestroyView() {
        super.onDestroyView();
        AudioPlayer.get().removeOnPlayEventListener(this);
    }
}
