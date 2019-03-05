package com.example.llj32.enjoymusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.llj32.enjoymusic.R;
import com.example.llj32.enjoymusic.model.Music;
import com.example.llj32.enjoymusic.service.AudioPlayer;
import com.example.llj32.enjoymusic.util.FileUtils;
import com.example.llj32.enjoymusic.util.viewbind.Bind;
import com.example.llj32.enjoymusic.util.viewbind.ViewBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地音乐列表适配器
 * Created by wcy on 2015/11/27.
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> implements Filterable {
    private List<Music> musicList;
    private List<Music> mFilteredList;
    private OnItemClickListener onItemClickListener;
    private OnMoreClickListener onMoreClickListener;

    public PlaylistAdapter(List<Music> musicList) {
        this.musicList = musicList;
        this.mFilteredList = musicList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnMoreClickListener(OnMoreClickListener listener) {
        this.onMoreClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_holder_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Music music = mFilteredList.get(position);
        if (AudioPlayer.get().getMusicList().size() > 0) {
            Music currentMusic = AudioPlayer.get().getMusicList().get(AudioPlayer.get().getPlayPosition());
            holder.vPlaying.setVisibility(music.equals(currentMusic) ? View.VISIBLE : View.INVISIBLE);
        }
        holder.tvTitle.setText(music.getTitle());
        String artist = FileUtils.getArtistAndAlbum(music.getArtist(), music.getAlbum());
        holder.tvArtist.setText(artist);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
        holder.ivMore.setOnClickListener(v -> {
            if (onMoreClickListener != null) {
                onMoreClickListener.onMoreClick(position);
            }
        });
        holder.vDivider.setVisibility(isShowDivider(position) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (TextUtils.isEmpty(constraint)) {
                    mFilteredList = musicList;
                } else {
                    mFilteredList = new ArrayList<>();
                    for (Music music :
                            musicList) {
                        if (music.getTitle().contains(constraint) || music.getArtist().contains(constraint)) {
                            mFilteredList.add(music);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = mFilteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values instanceof List) {
                    notifyDataSetChanged();
                }
            }
        };
    }

    private boolean isShowDivider(int position) {
        return position != mFilteredList.size() - 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.v_playing)
        private View vPlaying;
        @Bind(R.id.iv_cover)
        private ImageView ivCover;
        @Bind(R.id.tv_title)
        private TextView tvTitle;
        @Bind(R.id.tv_artist)
        private TextView tvArtist;
        @Bind(R.id.iv_more)
        private ImageView ivMore;
        @Bind(R.id.v_divider)
        private View vDivider;

        public ViewHolder(View view) {
            super(view);
            ViewBinder.bind(this, view);
        }
    }
}
