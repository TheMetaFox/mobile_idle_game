package com.example.idle_game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String GENERATION_TABLE_NAME = "GenerationTable";
    private static final String GENERATION_COLUMN = "Generation";
    private static final String LEVEL_COLUMN = "Level";

    private static final String STATS_TABLE_NAME = "StatsTable";
    private static final String SAVE_FILE_COLUMN = "SaveFile";
    private static final String CURRENT_STUFF_COLUMN = "CurrentStuff";

    private static final String CREATE_GENERATION_TABLE = "create Table " + GENERATION_TABLE_NAME + "( " +
            GENERATION_COLUMN + " INT primary key, " +
            LEVEL_COLUMN + " INT)";
    private static final String CREATE_STATS_TABLE = "create Table " + STATS_TABLE_NAME + "( " +
            SAVE_FILE_COLUMN + " INT primary key, " +
            CURRENT_STUFF_COLUMN + " INT)";

    public DBHelper(Context context) {
        super(context, "DBName.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(CREATE_GENERATION_TABLE);
        DB.execSQL(CREATE_STATS_TABLE);

        setData(DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists " + GENERATION_TABLE_NAME);
        DB.execSQL("drop Table if exists " + STATS_TABLE_NAME);

        onCreate(DB);
    }
    @Override
    public void onDowngrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists " + GENERATION_TABLE_NAME);
        DB.execSQL("drop Table if exists " + STATS_TABLE_NAME);

        onCreate(DB);
    }

    public void setData(SQLiteDatabase DB) {
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();

        contentValues.put(GENERATION_COLUMN, 1);
        contentValues.put(LEVEL_COLUMN, 0);
        contentValues1.put(SAVE_FILE_COLUMN, 0);
        contentValues1.put(CURRENT_STUFF_COLUMN, 0);

        DB.insert(GENERATION_TABLE_NAME, null, contentValues);
        DB.insert(STATS_TABLE_NAME,null, contentValues1);
    }


    public boolean levelUp(int Generation) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues CV = new ContentValues();

        int level = getLevel(Generation);

        level++;

        CV.put(LEVEL_COLUMN, level);

        long result = DB.update(GENERATION_TABLE_NAME, CV, GENERATION_COLUMN + " = ?", new String[]{String.valueOf(Generation)});

        return result!=-1;
    }
    public int getLevel(int Generation) {
        SQLiteDatabase DB = this.getReadableDatabase();

        Cursor cursor = DB.rawQuery("Select * from " + GENERATION_TABLE_NAME + " where " + GENERATION_COLUMN + " = ?", new String[]{String.valueOf(Generation)});
        cursor.moveToFirst();
        int level = cursor.getInt(1);
        cursor.close();

        return level;
    }
    public int getStuff() {
        SQLiteDatabase DB = this.getReadableDatabase();

        Cursor cursor = DB.rawQuery("Select * from " + STATS_TABLE_NAME + " where " + SAVE_FILE_COLUMN + " = ?", new String[]{String.valueOf(0)});
        cursor.moveToFirst();
        int stuff = cursor.getInt(1);
        cursor.close();

        return stuff;
    }
    public boolean updateStuff(int x) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues CV = new ContentValues();

        int stuff = getStuff();
        stuff += x;

        CV.put(CURRENT_STUFF_COLUMN, stuff);

        long result = DB.update(STATS_TABLE_NAME, CV, SAVE_FILE_COLUMN + " = ?", new String[]{String.valueOf(0)});

        return result != -1;
    }

    /*
    public boolean addStuff() {

        long result = DB.update(GENERATION_TABLE_NAME, CV, LEVEL_COLUMN + " = ?", new String[]{String.valueOf(level)});
        return result!=-1;
    }
    */
}
