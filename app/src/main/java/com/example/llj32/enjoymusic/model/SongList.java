package com.example.llj32.enjoymusic.model;

import java.io.Serializable;

public class SongList implements Serializable {
    private long mSongListId;
    private String mSongListName;

    public SongList(String songListName) {
        mSongListName = songListName;
    }

    public SongList(long songListId, String songListName) {
        mSongListId = songListId;
        mSongListName = songListName;
    }

    public long getSongListId() {
        return mSongListId;
    }

    public void setSongListId(long songListId) {
        mSongListId = songListId;
    }

    public String getSongListName() {
        return mSongListName;
    }

    public void setSongListName(String songListName) {
        mSongListName = songListName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SongList) {
            return this.mSongListName.equals(((SongList) obj).mSongListName);
        }
        return false;//super.equals(obj)
    }
}
