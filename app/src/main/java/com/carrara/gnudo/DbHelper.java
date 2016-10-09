package com.carrara.gnudo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DbHelper extends  SQLiteOpenHelper {
    /********************* STRUTTURA **********************/

    public static final String  DB_NAME = "gnudo.db";
    public static final int     DB_VERSION = 1;

    public static final String  TABLES_TASKS = "tasks";

    public static final String  COLUMNS_TASK_ID = "_id";
    public static final String  COLUMNS_TASK_TITLE = "title";
    public static final String  COLUMNS_TASK_DESCRIPTION = "description";
    public static final String  COLUMNS_TASK_CREATION_TIME = "ctime";
    public static final String  COLUMNS_TASK_MODIFICATION_TIME = "mtime";
    public static final String  COLUMNS_TASK_IS_COMPLETED = "completed";


    /********************* SOTTOCLASSI ****************************/


    public class Tasks {
        public List<Long> getIdList() {
            Cursor c = getReadableDatabase().query(TABLES_TASKS, new String[] {COLUMNS_TASK_ID}, null,
                    null, null, null, null);
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
                    TABLES_TASKS, COLUMNS_TASK_TITLE, COLUMNS_TASK_DESCRIPTION, COLUMNS_TASK_CREATION_TIME,
                    COLUMNS_TASK_MODIFICATION_TIME, COLUMNS_TASK_IS_COMPLETED, DatabaseUtils.sqlEscapeString(title),
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
            getWritableDatabase().delete(TABLES_TASKS, COLUMNS_TASK_ID + "=" + id.toString(), null);
        }
    }

    public class Task {
        final private Long ID;
        final private String WHERE_CLAUSE;

        Task(final Long id) {
            ID = id;
            WHERE_CLAUSE = String.format("%s=%d", COLUMNS_TASK_ID, id);
        }

        public Long getId() {
            return ID;
        }

        private Cursor getColumn(final String column) {
            Cursor c = getReadableDatabase().query(TABLES_TASKS, new String[] {column}, WHERE_CLAUSE,
                    null, null, null, null);
            c.moveToFirst();
            return c;
        }

        public String getTitle() {
            return getColumn(COLUMNS_TASK_TITLE).getString(0);
        }

        public String getDescription() {
            return getColumn(COLUMNS_TASK_DESCRIPTION).getString(0);
        }

        public Long getCreationTime() {
            return getColumn(COLUMNS_TASK_CREATION_TIME).getLong(0);
        }

        public Long getModificationTime() {
            return getColumn(COLUMNS_TASK_MODIFICATION_TIME).getLong(0);
        }

        public Boolean isCompleted() {
            return getColumn(COLUMNS_TASK_IS_COMPLETED).getInt(0) != 0? true : false;
        }

        public void setTitle(final String title) {
            ContentValues c = new ContentValues();
            c.put(COLUMNS_TASK_TITLE, title);
            getWritableDatabase().update(TABLES_TASKS, c, WHERE_CLAUSE, null);
        }

        public void setDescription(final String description) {
            ContentValues c = new ContentValues();
            c.put(COLUMNS_TASK_DESCRIPTION, description);
            getWritableDatabase().update(TABLES_TASKS, c, WHERE_CLAUSE, null);
        }

        public void setCreationTime(final Long time) {
            ContentValues c = new ContentValues();
            c.put(COLUMNS_TASK_CREATION_TIME, time);
            getWritableDatabase().update(TABLES_TASKS, c, WHERE_CLAUSE, null);
        }

        public void setModificationTime(final Long time) {
            ContentValues c = new ContentValues();
            c.put(COLUMNS_TASK_MODIFICATION_TIME, time);
            getWritableDatabase().update(TABLES_TASKS, c, WHERE_CLAUSE, null);
        }

        public void setStatus(final Boolean isCompleted) {
            ContentValues c = new ContentValues();
            c.put(COLUMNS_TASK_IS_COMPLETED, isCompleted);
            getWritableDatabase().update(TABLES_TASKS, c, WHERE_CLAUSE, null);
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

        final String sql = "CREATE TABLE  IF NOT EXISTS " + TABLES_TASKS +
                "(" +
                COLUMNS_TASK_ID + " INTEGER PRIMARY KEY," +
                COLUMNS_TASK_TITLE + " TEXT NOT NULL," +
                COLUMNS_TASK_DESCRIPTION + " TEXT," +
                COLUMNS_TASK_CREATION_TIME + " INTEGER NOT NULL," +
                COLUMNS_TASK_MODIFICATION_TIME + " INTEGER NOT NULL," +
                COLUMNS_TASK_IS_COMPLETED + " INTEGER NOT NULL" +
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
