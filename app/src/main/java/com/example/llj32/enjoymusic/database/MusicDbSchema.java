package com.example.llj32.enjoymusic.database;

public class MusicDbSchema {
    public static final class MusicTable {
        public static final String NAME = "musics";

        public static final class Cols {
            public static final String TITLE = "title";
            public static final String SONG_ID = "songId";
            public static final String ARTIST = "artist";
            public static final String ALBUM = "album";
            public static final String ALBUM_ID = "albumId";
            public static final String DURATION = "duration";
            public static final String PATH = "path";
            public static final String FILE_NAME = "fileName";
            public static final String FILE_SIZE = "fileSize";
        }
    }

    public static final class SongListTable {
        public static final String NAME = "songLists";

        public static final class Cols {
            public static final String SONG_LIST_ID = "songListId";
            public static final String SONG_LIST_NAME = "songListName";
        }
    }

    public static final class SongListItemTable {
        public static final String NAME = "songListItems";

        public static final class Cols {
            public static final String SONG_LIST_ID = "songListId";
            public static final String ITEM_SONG_ID = "itemSongId";
        }
    }
}
