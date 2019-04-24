package com.example.llj32.enjoymusic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.example.llj32.enjoymusic.R;
import com.example.llj32.enjoymusic.adapter.SonglistAdapter;
import com.example.llj32.enjoymusic.database.SongListLab;
import com.example.llj32.enjoymusic.model.SongList;

import java.util.List;

public class MySongListActivity extends AppCompatActivity {
    private RecyclerView rvSonglist;
    private SonglistAdapter mSonglistAdapter;
    private List<SongList> mSonglists;

    public static Intent newIntent(Context context) {
        return new Intent(context, MySongListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_song_list);

        rvSonglist = findViewById(R.id.song_list_recycler_view);
        rvSonglist.setLayoutManager(new LinearLayoutManager(this));
        mSonglists = SongListLab.get(this).getSonglists();
        mSonglistAdapter = new SonglistAdapter(mSonglists, i -> {
            startActivity(SongListDetailActivity.newIntent(this, mSonglists.get(i)));
        });
        rvSonglist.setAdapter(mSonglistAdapter);
    }

    private void newSonglist() {
        View v = LayoutInflater.from(this)
                .inflate(R.layout.dialog_new_songlist, null);
        EditText etSonglistName = v.findViewById(R.id.et_songlist_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(v)
                .setTitle("新建歌单")
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> {
                            dialog.dismiss();
                        })
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            SongList songList = new SongList(etSonglistName.getText().toString());
                            SongListLab.get(this).addSongList(songList);
//                            mSonglists.clear();
//                            mSonglists.addAll(SongListLab.get(this).getSonglists());
                            mSonglists.add(songList);
                            mSonglistAdapter.notifyDataSetChanged();
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_song_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_songlist:
                newSonglist();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
