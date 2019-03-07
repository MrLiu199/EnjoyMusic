package com.example.llj32.enjoymusic.util;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import com.example.llj32.enjoymusic.model.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicUtils {
    private static final String SELECTION = MediaStore.Audio.AudioColumns.SIZE + " >= ? AND " + MediaStore.Audio.AudioColumns.DURATION + " >= ?";

    /**
     * 扫描歌曲
     */
    @NonNull
    public static List<Music> getMusics(Context context) {
//        long filterSize = ParseUtils.parseLong(Preferences.getFilterSize()) * 1024;
//        long filterTime = ParseUtils.parseLong(Preferences.getFilterTime()) * 1000;
        long filterSize = 500 * 1024;//500KB
        long filterTime = 30 * 1000;//30s
        return searchMusics(context, SELECTION, new String[]{
                String.valueOf(filterSize),
                String.valueOf(filterTime)
        });
    }

    public static Music getMusic(Context context, String title) {
        List<Music> musicList = searchMusics(context, MediaStore.Audio.AudioColumns.TITLE + " = ?", new String[]{title});
        if (musicList.isEmpty()) {
            return null;
        } else {
            return musicList.get(0);
        }
    }

    public static List<Music> searchMusics(Context context, String whereClause, String[] whereArgs) {
        List<Music> musicList = new ArrayList<>();
        if (!PermissionUtils.isGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return musicList;
        }

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,
                        MediaStore.Audio.AudioColumns.IS_MUSIC,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.ARTIST,
                        MediaStore.Audio.AudioColumns.ALBUM,
                        MediaStore.Audio.AudioColumns.ALBUM_ID,
                        MediaStore.Audio.AudioColumns.DATA,
                        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                        MediaStore.Audio.AudioColumns.SIZE,
                        MediaStore.Audio.AudioColumns.DURATION
                },
                whereClause,
                whereArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null) {
            return musicList;
        }

        int i = 0;
        while (cursor.moveToNext()) {
            // 是否为音乐，魅族手机上始终为0
//            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_MUSIC));
//            if (!SystemUtils.isFlyme() && isMusic == 0) {
//                continue;
//            }

            long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
            String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)));
            long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
            String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)));
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE));

            Music music = new Music(title, id, artist, album, albumId,
                    duration, path, fileName, fileSize);
//            if (++i <= 20) {
//                // 只加载前20首的缩略图
//                CoverLoader.get().loadThumb(music);
//            }
            musicList.add(music);
        }
        cursor.close();

        return musicList;
    }
}
