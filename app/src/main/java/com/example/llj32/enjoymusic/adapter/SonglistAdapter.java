package com.example.llj32.enjoymusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.llj32.enjoymusic.R;
import com.example.llj32.enjoymusic.database.SongListLab;
import com.example.llj32.enjoymusic.model.SongList;

import java.util.List;

import static com.example.llj32.enjoymusic.util.DataUtils.MY_COLLECTION_SONGLIST;

public class SonglistAdapter extends RecyclerView.Adapter<SonglistAdapter.ViewHolder> {
    private final List<SongList> mValues;
    private final OnItemClickListener mListener;

    public SonglistAdapter(List<SongList> items, OnItemClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_holder_songlist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SongList songList = mValues.get(i);
        if (songList.equals(MY_COLLECTION_SONGLIST)) {
            viewHolder.mIdView.setImageResource(R.drawable.a_8);
        }
        viewHolder.mContentView.setText(songList.getSongListName());

        viewHolder.itemView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onItemClick(i);
            }
        });
        viewHolder.itemView.setOnLongClickListener(v -> {
            String[] items = new String[]{"删除歌单"};
            Context context = viewHolder.itemView.getContext();
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(songList.getSongListName());
            dialog.setItems(items, (dialog1, which) -> {
                SongListLab.get(context).deleteSongList(songList);
                mValues.remove(i);
                notifyDataSetChanged();
            });
            dialog.show();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mIdView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.iv_songlist_cover);
            mContentView = view.findViewById(R.id.tv_songlist_name);
        }
    }
}
