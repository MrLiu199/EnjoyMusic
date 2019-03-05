package com.example.llj32.enjoymusic.model;

import android.text.TextUtils;

import java.io.Serializable;

public class Music implements Serializable {
    private String title; // 音乐标题
    private long songId; // [本地]歌曲ID
    private String artist; // 艺术家
    private String album; // 专辑
    private long albumId; // [本地]专辑ID
    private long duration; // 持续时间
    private String path; // 播放地址
    private String fileName; // [本地]文件名
    private long fileSize; // [本地]文件大小

    public Music() {
    }

    public Music(String title, long songId, String artist, String album, long albumId,
                 long duration, String path, String fileName, long fileSize) {
        this.title = title;
        this.songId = songId;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.duration = duration;
        this.path = path;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Music)) {
            return false;
        }
        Music music = (Music) o;
        if (music.songId > 0 && music.songId == this.songId) {
            return true;
        }
        if (TextUtils.equals(music.title, this.title)
                && TextUtils.equals(music.artist, this.artist)
                && TextUtils.equals(music.album, this.album)
                && music.duration == this.duration) {
            return true;
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
