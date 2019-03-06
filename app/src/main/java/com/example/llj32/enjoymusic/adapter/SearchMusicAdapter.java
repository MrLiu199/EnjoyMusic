package com.example.llj32.enjoymusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.llj32.enjoymusic.R;
import com.example.llj32.enjoymusic.model.SearchMusic;
import com.example.llj32.enjoymusic.util.viewbind.Bind;
import com.example.llj32.enjoymusic.util.viewbind.ViewBinder;

import java.util.List;

/**
 * 搜索结果适配器
 * Created by hzwangchenyan on 2016/1/13.
 */
public class SearchMusicAdapter extends RecyclerView.Adapter<SearchMusicAdapter.ViewHolder> {
    private List<SearchMusic.Song> mData;
    private OnItemClickListener onItemClickListener;
    private OnMoreClickListener onMoreClickListener;

    public SearchMusicAdapter(List<SearchMusic.Song> data) {
        mData = data;
    }

    @Override
    public SearchMusicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_holder_music, parent, false);
        return new SearchMusicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchMusicAdapter.ViewHolder holder, int position) {
        holder.tvTitle.setText(mData.get(position).getSongname());
        holder.tvArtist.setText(mData.get(position).getArtistname());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
        holder.ivMore.setOnClickListener(v -> onMoreClickListener.onMoreClick(position));
        holder.vDivider.setVisibility(isShowDivider(position) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private boolean isShowDivider(int position) {
        return position != mData.size() - 1;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnMoreClickListener(OnMoreClickListener listener) {
        this.onMoreClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
            ivCover.setVisibility(View.GONE);
        }
    }
}
