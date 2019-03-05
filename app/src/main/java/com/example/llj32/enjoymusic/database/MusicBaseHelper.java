package com.example.llj32.enjoymusic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.llj32.enjoymusic.database.MusicDbSchema.MusicTable;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
