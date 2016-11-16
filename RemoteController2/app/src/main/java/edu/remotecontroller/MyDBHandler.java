package edu.remotecontroller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import android.util.Log;

/**
 * Created by emazwok on 2016-10-28.
 */

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DatabaseVersion = 1;
    private static final String    DATABASE_NAME = "host.db";
    private static final String      TABLE_HOSTS = "hosts";
    private static final String        COLUMN_ID = "id";
    private static final String COLUMN_HOST_NAME = "hostname";
    private static final String COLUMN_HOST_ADDR = "addr";
    private static final String COLUMN_HOST_PORT = "port";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_HOSTS + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HOST_NAME + " TEXT, " +
                COLUMN_HOST_ADDR + " TEXT, " +
                COLUMN_HOST_PORT + " INTEGER " + ");";
        db.execSQL(query);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_HOSTS);
        onCreate(db);
    }

    public void dropTable()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOSTS);
        onCreate(db);
    }

    public void addHost(Host host)
    {
        ContentValues entry = new ContentValues();
        entry.put(COLUMN_HOST_NAME, host.getHostName());
        entry.put(COLUMN_HOST_ADDR, host.getIpAddress());
        entry.put(COLUMN_HOST_PORT, host.getPort());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(
                TABLE_HOSTS,
                null,
                entry
                );
        db.close();
    }

    public void deleteHost(String hostname, String addr, int port)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HOSTS        + " WHERE "
                + COLUMN_HOST_NAME + "=\"" + hostname  + " AND "
                + COLUMN_HOST_ADDR + "=\"" + addr      + " AND "
                + COLUMN_HOST_PORT + "=\"" + port);
    }

    public ArrayList<Host> getDatabase ()
    {
        ArrayList<Host> retHostArray = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_HOSTS +";";
        Cursor dbCursor = db.rawQuery(query, null);
        dbCursor.moveToFirst();
        while(!dbCursor.isAfterLast())
        {
            retHostArray.add(new Host(dbCursor.getInt(dbCursor.getColumnIndex(COLUMN_ID)),
                                    dbCursor.getString(dbCursor.getColumnIndex(COLUMN_HOST_NAME)),
                                    dbCursor.getString(dbCursor.getColumnIndex(COLUMN_HOST_ADDR)),
                                    dbCursor.getInt(dbCursor.getColumnIndex(COLUMN_HOST_PORT))));
            dbCursor.moveToNext();
        }
        for(int i = 0; i < retHostArray.size(); i++)
        {
            Log.i("dbg", retHostArray.get(i).toString());
        }
        return retHostArray;
    }

    public Host getHostByName(String name) {
        Host host;
        SQLiteDatabase db = getWritableDatabase();
        Cursor dbCursor = db.rawQuery("SELECT * FROM " + TABLE_HOSTS + " WHERE " + COLUMN_HOST_NAME + " = " + name + ";", null);
        dbCursor.moveToFirst();
        if (dbCursor.getString(1) != null) {
            host = new Host(dbCursor.getInt(dbCursor.getColumnIndex(COLUMN_ID)),
                    dbCursor.getString(dbCursor.getColumnIndex(COLUMN_HOST_NAME)),
                    dbCursor.getString(dbCursor.getColumnIndex(COLUMN_HOST_ADDR)),
                    dbCursor.getInt(dbCursor.getColumnIndex(COLUMN_HOST_PORT)));
        }
        else
            host = new Host(0, "default", "0.0.0.0", 0);
        return host;
    }

    public void deleteHostByName(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_HOSTS, COLUMN_HOST_NAME + "=?", new String[] { name });
        db.close();
    }
}


