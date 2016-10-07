package com.carrara.gnudo;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class DbHelper extends  SQLiteOpenHelper {
    /********************* STRUTTURA **********************/

    public static final String  DB_NAME = "gnudo.db";
    public static final int     DB_VERSION = 1;

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


    /********************* SOTTOCLASSI ****************************/


    public class Tasks {
        public List<Long> getIdList() {
            Cursor c = getReadableDatabase().rawQuery("SELECT " + Columns.Task.id + " FROM " + Tables.tasks, null);
            List<Long> r = new ArrayList<Long>();

            if (c.moveToFirst()) {
                do {
                    r.add(c.getLong(0));
                } while(c.moveToNext());
            }

            return r;
        }

        public Task add(final String title, final String description, final Long creationTime,
                        final Long modificationTime, final Boolean isCompleted) {
            final String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (\"%s\", \"%s\", %d, %d, %d)",
                    Tables.tasks, Columns.Task.title, Columns.Task.description, Columns.Task.creationTime,
                    Columns.Task.modificationTime, Columns.Task.completed, DatabaseUtils.sqlEscapeString(title),
                    DatabaseUtils.sqlEscapeString(description), creationTime, modificationTime, isCompleted? 1 : 0);
            getWritableDatabase().execSQL(sql);
            
            Cursor c = getReadableDatabase().rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            return new Task(c.getLong(0));
        }

        public Task getTask(final Long id) {
            return new Task(id);
        }

        public void remove(final Long id) {
            getWritableDatabase().rawQuery("DELETE FROM " + Tables.tasks + " WHERE id=" + id.toString(), null);
        }
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

    /*************************************************************/

    private static DbHelper sInstance;
    private final Tasks     tasks;


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        tasks = new Tasks();
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

    public static synchronized DbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DbHelper(context);
        }
        return sInstance;
    }

    public Tasks getTasks() {
        return tasks;
    }

}
