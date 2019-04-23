package com.example.llj32.enjoymusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.llj32.enjoymusic.database.MusicDbSchema.SongListItemTable;
import com.example.llj32.enjoymusic.database.MusicDbSchema.SongListTable;

import static com.example.llj32.enjoymusic.database.MusicDbSchema.MusicTable;
import static com.example.llj32.enjoymusic.util.DataUtils.MY_COLLECTION_SONGLIST;

public class MusicBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "MusicBaseHelper";
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "musicBase.db";

    public MusicBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MusicTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                MusicTable.Cols.TITLE + ", " +
                MusicTable.Cols.SONG_ID + ", " +
                MusicTable.Cols.ARTIST + ", " +
                MusicTable.Cols.ALBUM + ", " +
                MusicTable.Cols.ALBUM_ID + ", " +
                MusicTable.Cols.DURATION + ", " +
                MusicTable.Cols.PATH + ", " +
                MusicTable.Cols.FILE_NAME + ", " +
                MusicTable.Cols.FILE_SIZE +
                ")"
        );
        db.execSQL("create table " + SongListTable.NAME + "(" +
                SongListTable.Cols.SONG_LIST_ID +
                " integer primary key autoincrement, " +
                SongListTable.Cols.SONG_LIST_NAME +
                ")"
        );
        db.execSQL("create table " + SongListItemTable.NAME + "(" +
                SongListItemTable.Cols.SONG_LIST_ID + " integer, " +
                SongListItemTable.Cols.ITEM_SONG_ID + " integer" +
                ")"
        );

        //首先添加我的收藏歌单
        ContentValues values = SongListLab.getContentValues(MY_COLLECTION_SONGLIST);
        db.insert(SongListTable.NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
