package com.example.llj32.enjoymusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import com.example.llj32.enjoymusic.database.MusicDbSchema.SongListItemTable;
import com.example.llj32.enjoymusic.database.MusicDbSchema.SongListTable;
import com.example.llj32.enjoymusic.model.Music;
import com.example.llj32.enjoymusic.model.SongList;
import com.example.llj32.enjoymusic.model.SonglistItem;
import com.example.llj32.enjoymusic.util.MusicUtils;

import java.util.ArrayList;
import java.util.List;

public class SongListLab {
    private static SongListLab sSongListLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private SongListLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new MusicBaseHelper(mContext)
                .getWritableDatabase();
    }

    public static SongListLab get(Context context) {
        if (sSongListLab == null) {
            sSongListLab = new SongListLab(context);
        }
        return sSongListLab;
    }

    public static ContentValues getContentValues(SongList songList) {
        ContentValues values = new ContentValues();
        values.put(SongListTable.Cols.SONG_LIST_NAME, songList.getSongListName());
        values.put(SongListTable.Cols.SONG_LIST_ID, songList.getSongListId());
        return values;
    }

    private static ContentValues getContentValues(SonglistItem songlistItem) {
        ContentValues values = new ContentValues();
        values.put(SongListItemTable.Cols.ITEM_SONG_ID, songlistItem.getItemSongId());
        values.put(SongListItemTable.Cols.SONG_LIST_ID, songlistItem.getSongListId());
        return values;
    }

    //添加歌单
    public void addSongList(SongList songList) {
        ContentValues values = getContentValues(songList);
        values.putNull(SongListTable.Cols.SONG_LIST_ID);
        mDatabase.insert(SongListTable.NAME, null, values);
    }

    //获取歌单列表
    public List<SongList> getSonglists() {
        List<SongList> songlists = new ArrayList<>();

        SongListCursorWrapper cursor = querySonglists(null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            songlists.add(cursor.getSongList());
            cursor.moveToNext();
        }
        cursor.close();

        return songlists;
    }

    //删除歌单
    public void deleteSongList(SongList songList) {
        long songListId = songList.getSongListId();
//        ContentValues values = getContentValues(songList);

        mDatabase.delete(SongListTable.NAME,
                SongListTable.Cols.SONG_LIST_ID + " = ?",
                new String[]{String.valueOf(songListId)});
        mDatabase.delete(SongListItemTable.NAME,
                SongListItemTable.Cols.SONG_LIST_ID + " = ?",
                new String[]{String.valueOf(songListId)});
    }

    //添加歌曲到歌单
    public void addSonglistItem(SonglistItem songlistItem) {
        ContentValues values = getContentValues(songlistItem);

        mDatabase.insert(SongListItemTable.NAME, null, values);
    }

    //获取歌单中歌曲项列表
    public List<SonglistItem> getSonglistItems(SongList songList) {
        List<SonglistItem> songlistItems = new ArrayList<>();

        SongListItemCursorWrapper cursor = querySongListItems(SongListTable.Cols.SONG_LIST_ID + " = ?",
                new String[]{String.valueOf(songList.getSongListId())});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            songlistItems.add(cursor.getSongListItem());
            cursor.moveToNext();
        }
        cursor.close();

        return songlistItems;
    }

    //获取歌单中歌曲列表
    public List<Music> getSonglistMusics(SongList songList) {
        List<SonglistItem> songlistItems = getSonglistItems(songList);
        List<Music> musicList = new ArrayList<>(songlistItems.size());

        for (SonglistItem songItem :
                songlistItems) {
            musicList.addAll(MusicUtils.searchMusics(mContext, BaseColumns._ID + " = ?",
                    new String[]{String.valueOf(songItem.getItemSongId())}));
        }
        return musicList;
    }

    //删除歌单中歌曲
    public void deleteSonglistItem(SonglistItem songlistItem) {
        mDatabase.delete(SongListItemTable.NAME,
                SongListItemTable.Cols.SONG_LIST_ID + " = ? and " +
                        SongListItemTable.Cols.ITEM_SONG_ID + " = ? ",
                new String[]{String.valueOf(songlistItem.getSongListId()),
                        String.valueOf(songlistItem.getItemSongId())});
    }

    private SongListCursorWrapper querySonglists(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                SongListTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                SongListTable.Cols.SONG_LIST_ID + " ASC"  // orderBy
        );

        return new SongListCursorWrapper(cursor);
    }

    private SongListItemCursorWrapper querySongListItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                SongListItemTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new SongListItemCursorWrapper(cursor);
    }

    public static class SongListCursorWrapper extends CursorWrapper {
        public SongListCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public SongList getSongList() {
            String songListName = getString(getColumnIndex(SongListTable.Cols.SONG_LIST_NAME));
            long songListId = getLong(getColumnIndex(SongListTable.Cols.SONG_LIST_ID));
            return new SongList(songListId, songListName);
        }
    }

    public static class SongListItemCursorWrapper extends CursorWrapper {
        public SongListItemCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public SonglistItem getSongListItem() {
            long itemSongId = getLong(getColumnIndex(SongListItemTable.Cols.ITEM_SONG_ID));
            long songListId = getLong(getColumnIndex(SongListItemTable.Cols.SONG_LIST_ID));
            return new SonglistItem(songListId, itemSongId);
        }
    }
}
