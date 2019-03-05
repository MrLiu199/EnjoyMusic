package com.example.llj32.enjoymusic.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.llj32.enjoymusic.model.Music;

import static com.example.llj32.enjoymusic.database.MusicDbSchema.MusicTable;

public class MusicCursorWrapper extends CursorWrapper {
    public MusicCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Music getMusic() {
        String title = getString(getColumnIndex(MusicTable.Cols.TITLE));
        long songId = getLong(getColumnIndex(MusicTable.Cols.SONG_ID));
        String artist = getString(getColumnIndex(MusicTable.Cols.ARTIST));
        String album = getString(getColumnIndex(MusicTable.Cols.ALBUM));
        long albumId = getLong(getColumnIndex(MusicTable.Cols.ALBUM_ID));
        long duration = getLong(getColumnIndex(MusicTable.Cols.DURATION));
        String path = getString(getColumnIndex(MusicTable.Cols.PATH));
        String fileName = getString(getColumnIndex(MusicTable.Cols.FILE_NAME));
        long fileSize = getLong(getColumnIndex(MusicTable.Cols.FILE_SIZE));

        Music crime = new Music(title, songId, artist, album, albumId,
                duration, path, fileName, fileSize);

        return crime;
    }
}
