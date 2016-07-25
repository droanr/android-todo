package com.drishi.todoapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.drishi.todoapp.models.TodoTask;

import java.util.ArrayList;

/**
 * Created by drishi on 7/24/16.
 */
public class ToDoDatabaseHelper extends SQLiteOpenHelper {
    // Database info
    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 2;

    // Table names
    private static final String TABLE_TODO_TASK = "todoTask";

    // TodoTask Table Columns
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_DESCRIPTION = "taskDescription";

    private static ToDoDatabaseHelper sInstance;

    public static synchronized ToDoDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ToDoDatabaseHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    public ToDoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO_TASK +
                "(" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY," + // Define a primary keY
                KEY_TODO_DESCRIPTION + " TEXT" +
                ")";

        db.execSQL(CREATE_TODO_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_TASK);
            onCreate(db);
        }
    }

    // insert a to-do item into the database
    public void addItem(TodoTask task) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_DESCRIPTION, task.taskDescription);
            db.insertOrThrow(TABLE_TODO_TASK, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("ERROR", "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public int updateItem(TodoTask task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_DESCRIPTION, task.taskDescription);

        return db.update(TABLE_TODO_TASK, values, KEY_TODO_ID + " = ?", new String[] { String.valueOf(task.id)});
    }

    public void deleteTodoTask(TodoTask task) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_TODO_TASK, "Id="+Integer.toString(task.id), null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("ERROR", "Error while trying to delete task");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<TodoTask> getAllItems() {
        ArrayList<TodoTask> tasks = new ArrayList<TodoTask>();

        // SELECT * FROM
        String TODO_ITEM_SELECT_QUERY = String.format("SELECT * FROM %s ", TABLE_TODO_TASK);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODO_ITEM_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(KEY_TODO_ID));
                    String description = cursor.getString(cursor.getColumnIndex(KEY_TODO_DESCRIPTION));
                    TodoTask task = new TodoTask(description);
                    task.id = id;
                    tasks.add(task);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
                Log.d("ERROR", "Error when crying to retrieve to-do tasks from the database");
        } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
        }
        return tasks;
    }


}
