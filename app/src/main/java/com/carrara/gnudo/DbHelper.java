package com.carrara.gnudo;


import android.content.ContentValues;
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
            getWritableDatabase().execSQL("DELETE FROM " + Tables.tasks + " WHERE " + Columns.Task.id
                    + "=" + id.toString());
        }
    }
    
    public class Task {
        final private Long id;

        Task(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        private Cursor getCursor(final String column) {
            return getReadableDatabase().query(Tables.tasks, new String[] {column},
                    Columns.Task.id + "=" + this.id.toString(), null, null, null, null);
        }


        public String getTitle() {
            Cursor c = getCursor(Columns.Task.title); c.moveToFirst();
            return c.getString(0);
        }

        public String getDescription() {
            Cursor c = getCursor(Columns.Task.description); c.moveToFirst();
            return c.getString(0);
        }

        public Long getCreationTime() {
            Cursor c = getCursor(Columns.Task.creationTime); c.moveToFirst();
            return c.getLong(0);
        }

        public Long getModificationTime() {
            Cursor c = getCursor(Columns.Task.modificationTime); c.moveToFirst();
            return c.getLong(0);
        }

        public Boolean isCompleted() {
            Cursor c = getCursor(Columns.Task.completed); c.moveToFirst();
            return c.getInt(0) != 0? true : false;
        }

        public void setTitle(final String title) {
            ContentValues c = new ContentValues();
            c.put(Columns.Task.title, title);
            getWritableDatabase().update(Tables.tasks, c, Columns.Task.id + "=" + this.id.toString(), null);
        }
;
        public void setDescription(final String description) {
            ContentValues c = new ContentValues();
            c.put(Columns.Task.description, description);
            getWritableDatabase().update(Tables.tasks, c, Columns.Task.id + "=" + this.id.toString(), null);
        }

        public void setCreationTime(final Long time) {
            ContentValues c = new ContentValues();
            c.put(Columns.Task.creationTime, time);
            getWritableDatabase().update(Tables.tasks, c, Columns.Task.id + "=" + this.id.toString(), null);
        }

        public void setModificationTime(final Long time) {
            ContentValues c = new ContentValues();
            c.put(Columns.Task.modificationTime, time);
            getWritableDatabase().update(Tables.tasks, c, Columns.Task.id + "=" + this.id.toString(), null);
        }

        public void setStatus(final Boolean isCompleted) {
            ContentValues c = new ContentValues();
            c.put(Columns.Task.completed, isCompleted);
            getWritableDatabase().update(Tables.tasks, c, Columns.Task.id + "=" + this.id.toString(), null);
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
