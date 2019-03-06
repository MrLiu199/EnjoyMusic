package com.example.llj32.enjoymusic.util;

import android.os.Environment;
import android.text.TextUtils;
import com.example.llj32.enjoymusic.MusicApplication;
import com.example.llj32.enjoymusic.R;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {
    private static final String MP3 = ".mp3";
    public static final String EnjoyMusic = "EnjoyMusic";

    private static String getAppDir() {
        return Environment.getExternalStorageDirectory() + "/" + EnjoyMusic;
    }

    public static String getMusicDir() {
        String dir = getAppDir() + "/Music/";
        return mkdirs(dir);
    }

    public static String getRelativeMusicDir() {
        String dir = EnjoyMusic + "/Music/";
        return mkdirs(dir);
    }

    public static String getMp3FileName(String artist, String title) {
        return getFileName(artist, title) + MP3;
    }

    public static String getFileName(String artist, String title) {
        artist = stringFilter(artist);
        title = stringFilter(title);
        if (TextUtils.isEmpty(artist)) {
            artist = MusicApplication.getsMusicApp().getString(R.string.unknown);
        }
        if (TextUtils.isEmpty(title)) {
            title = MusicApplication.getsMusicApp().getString(R.string.unknown);
        }
        return artist + " - " + title;
    }

    public static String getArtistAndAlbum(String artist, String album) {
        if (TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return "";
        } else if (!TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)) {
            return artist;
        } else if (TextUtils.isEmpty(artist) && !TextUtils.isEmpty(album)) {
            return album;
        } else {
            return artist + " - " + album;
        }
    }

    /**
     * 过滤特殊字符(\/:*?"<>|)
     */
    private static String stringFilter(String str) {
        if (str == null) {
            return null;
        }
        String regEx = "[\\/:*?\"<>|]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    private static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }
}
