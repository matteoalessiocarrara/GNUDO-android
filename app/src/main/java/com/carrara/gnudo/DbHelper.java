package com.carrara.gnudo;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DbHelper extends  SQLiteOpenHelper {
    private static DbHelper sInstance;

    public static final String DB_NAME = "gnudo.db";
    public static final int DB_VERSION = 1;

    public static class Tables {
        public static final String tasks = "tasks";
    }

    public static class Columns {
        public class Task {
            public static final String id = "_id";
            public static final String title = "title";
            public static final String description = "description";
            public static final String creationTime = "ctime";
            public static final String modificationTime = "mtime";
            public static final String completed = "completed";
        }
    }

    public static synchronized DbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DbHelper(context);
        }
        return sInstance;
    }

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String sql = "CREATE TABLE  IF NOT EXISTS " + Tables.tasks +
                "(" +
                Columns.Task.id + " INTEGER PRIMARY KEY," +
                Columns.Task.title + " TEXT NOT NULL," +
                Columns.Task.description + " TEXT," +
                Columns.Task.creationTime + " INTEGER NOT NULL," +
                Columns.Task.modificationTime + " INTEGER NOT NULL," +
                Columns.Task.completed + " INTEGER NOT NULL" +
                ");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public class Task {
        Task(final Long id) {

        }

        public void getId() {

        }

        public String getTitle() {
            return "";
        }

        public String getDescription() {
            return "";
        }

        public Long getCreationTime() {
            return 0L;
        }

        public Long getModificationTime() {
            return 0L;
        }

        public Boolean isCompleted() {
            return false;
        }

        public void setTitle(final String title) {

        }

        public void setDescription(final String description) {

        }

        public void setCreationTime(final Long time) {

        }

        public void setModificationTime(final Long time) {

        }

        public void setStatus(final Boolean isCompleted) {

        }
    }


    public class Tasks {
        public void getIdList() {

        }


        public void add(final Short priority, final String title, final String description,
                        final Long creationTime, final long modificationTime, final Boolean isCompleted) {

        }

        public void getTask(final long id) {

        }

        public void remove(final long id) {

        }
    }
}
