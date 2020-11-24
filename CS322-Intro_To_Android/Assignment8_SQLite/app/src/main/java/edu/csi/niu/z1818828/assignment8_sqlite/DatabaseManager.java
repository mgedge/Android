package edu.csi.niu.z1818828.assignment8_sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "noteDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TITLE = "note";
    private static final String ID = "id ";
    private static final String TITLE = "title";
    private static final String NOTE = "note";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "create table " + TABLE_TITLE + " ( " + ID;
        sqlCreate += " integer primary key autoincrement, " + TITLE;
        sqlCreate += " text, " + NOTE + " longtext )";

        Log.w("onCreate", sqlCreate);

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlUpgrade = "drop table if exists ";
        sqlUpgrade += TABLE_TITLE;

        Log.w("onUpgrade", sqlUpgrade);

        db.execSQL(sqlUpgrade);
        onCreate(db);
    }

    public void insert(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlInsert = "insert into " + TABLE_TITLE;
        sqlInsert += " values(null, '" + note.getTitle();
        sqlInsert += "', '" + note.getNote() + "' )";

        Log.w("insert", sqlInsert);

        db.execSQL(sqlInsert);
        db.close();
    }

    public Cursor readAll() {
        String sqlQuery = "select * from " + TABLE_TITLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(sqlQuery, null);
        }

        return cursor;
    }

    public void updateByID(int id, String title, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlUpdate = "update " + TABLE_TITLE;
        sqlUpdate += " set " + TITLE + " = '" + title + "', ";
        sqlUpdate += NOTE + " = '" + note + "'";
        sqlUpdate += " where " + ID + " = " + id;

        Log.w("updateByID", sqlUpdate);

        db.execSQL(sqlUpdate);
        db.close();
    }

    public void deleteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlDelete = "delete from " + TABLE_TITLE;
        sqlDelete += " where " + ID + " = " + id;

        Log.w("deleteById", sqlDelete);

        db.execSQL(sqlDelete);
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlDeleteAll = "delete * from " + TABLE_TITLE;

        Log.w("deleteAll", sqlDeleteAll);

        db.execSQL(sqlDeleteAll);
        db.close();
    }
}
