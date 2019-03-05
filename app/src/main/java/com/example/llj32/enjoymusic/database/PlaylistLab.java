package com.example.llj32.enjoymusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.llj32.enjoymusic.model.Music;

import java.util.ArrayList;
import java.util.List;

import static com.example.llj32.enjoymusic.database.MusicDbSchema.MusicTable;

public class PlaylistLab {
    private static PlaylistLab sPlaylistLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static PlaylistLab get(Context context) {
        if (sPlaylistLab == null) {
            sPlaylistLab = new PlaylistLab(context);
        }
        return sPlaylistLab;
    }

    private PlaylistLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new MusicBaseHelper(mContext)
                .getWritableDatabase();
    }

    private static ContentValues getContentValues(Music music) {
        ContentValues values = new ContentValues();
        values.put(MusicTable.Cols.TITLE, music.getTitle());
        values.put(MusicTable.Cols.SONG_ID, music.getSongId());
        values.put(MusicTable.Cols.ARTIST, music.getArtist());
        values.put(MusicTable.Cols.ALBUM, music.getAlbum());
        values.put(MusicTable.Cols.ALBUM_ID, music.getAlbumId());
        values.put(MusicTable.Cols.DURATION, music.getDuration());
        values.put(MusicTable.Cols.PATH, music.getPath());
        values.put(MusicTable.Cols.FILE_NAME, music.getFileName());
        values.put(MusicTable.Cols.FILE_SIZE, music.getFileSize());

        return values;
    }

    public void addMusic(Music c) {
        ContentValues values = getContentValues(c);

        mDatabase.insert(MusicTable.NAME, null, values);
    }

    public void updateMusic(Music music) {
        String title = music.getTitle();
        ContentValues values = getContentValues(music);

        mDatabase.update(MusicTable.NAME, values,
                MusicTable.Cols.TITLE + " = ?",
                new String[]{title});
    }

    public void deleteMusic(Music music) {
        String title = music.getTitle();
        ContentValues values = getContentValues(music);

        mDatabase.delete(MusicTable.NAME,
                MusicTable.Cols.TITLE + " = ?",
                new String[]{title});
    }

    private MusicCursorWrapper queryMusics(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                MusicTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new MusicCursorWrapper(cursor);
    }


    public List<Music> getMusics() {
        List<Music> musics = new ArrayList<>();

        MusicCursorWrapper cursor = queryMusics(null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            musics.add(cursor.getMusic());
            cursor.moveToNext();
        }
        cursor.close();

        return musics;
    }

    public Music getMusic(String title) {
        MusicCursorWrapper cursor = queryMusics(
                MusicTable.Cols.TITLE + " = ?",
                new String[]{title}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getMusic();
        } finally {
            cursor.close();
        }
    }
}
