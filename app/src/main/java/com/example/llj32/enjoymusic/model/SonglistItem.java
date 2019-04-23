package com.example.llj32.enjoymusic.model;

public class SonglistItem {
    private long mSongListId;
    private long mItemSongId;

    public SonglistItem(long songListId, long itemSongId) {
        mSongListId = songListId;
        mItemSongId = itemSongId;
    }

    public long getSongListId() {
        return mSongListId;
    }

    public void setSongListId(long songListId) {
        mSongListId = songListId;
    }

    public long getItemSongId() {
        return mItemSongId;
    }

    public void setItemSongId(long itemSongId) {
        mItemSongId = itemSongId;
    }
}
