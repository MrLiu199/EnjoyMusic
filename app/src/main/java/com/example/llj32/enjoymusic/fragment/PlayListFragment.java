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
import com.example.llj32.enjoymusic.model.Music;
import com.example.llj32.enjoymusic.service.AudioPlayer;
import com.example.llj32.enjoymusic.service.OnPlayerEventListener;

//播放列表
public class PlayListFragment extends Fragment implements OnPlayerEventListener {
    private RecyclerView mMusicRecyclerView;
    private PlaylistAdapter mPlaylistAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_list, container, false);
        getActivity().setTitle("播放列表");

        mMusicRecyclerView = view
                .findViewById(R.id.music_recycler_view);
        mMusicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPlaylistAdapter = new PlaylistAdapter(AudioPlayer.get().getMusicList());
        mPlaylistAdapter.setOnItemClickListener(position -> {
            AudioPlayer.get().play(position);
        });
        mPlaylistAdapter.setOnMoreClickListener(position -> {
            String[] items = new String[]{"移除"};
            Music music = AudioPlayer.get().getMusicList().get(position);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(music.getTitle());
            dialog.setItems(items, (dialog1, which) -> {
                AudioPlayer.get().delete(position);
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
        int crimeCount = AudioPlayer.get().getMusicList().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(subtitle);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_search_music, menu);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.onActionViewExpanded();
//        searchView.setQueryHint(getString(R.string.search_tips));
////        searchView.setOnQueryTextListener(this);
//        searchView.setSubmitButtonEnabled(true);
//        try {
//            Field field = searchView.getClass().getDeclaredField("mGoButton");
//            field.setAccessible(true);
//            ImageView mGoButton = (ImageView) field.get(searchView);
//            mGoButton.setImageResource(R.drawable.ic_menu_search);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        return super.onCreateOptionsMenu(menu);
//    }

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
