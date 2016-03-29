package com.twinstar.childtimeutilization;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLDataException;

/**
 * Created by JOE on 3/24/2016.
 */

public class ChildDBHandler extends SQLiteOpenHelper {
    protected ChildInfoApp app;
    private static ChildDBHandler sInstance;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "childinfo.db";
    public static final String TABLE_PARENTINFO = "parentinfo";
    public static final String TABLE_CHILDINFO = "childinfo";
    public static final String TABLE_STATISTICS = "statistics";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CHILD_NAME = "childname";
    //ChildInfo specific
    public static final String COLUMN_PARENT_NAME = "parentname";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_DOB = "birthdate";
    // Statistics specific
    public static final String COLUMN_WENT_TO_BED = "wenttobed";
    public static final String COLUMN_WOKE_UP = "wokeup";
    public static final String COLUMN_TOTAL_SCREEN_TIME = "totalscreentime";
    public static final String COLUMN_TOTAL_FRIENDS_AND_FAMILY_TIME = "totalfriendsandfamilytime";

    // String to create child info table
    String CREATE_PARENT_INFO_TABLE = "CREATE TABLE " + TABLE_PARENTINFO + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PARENT_NAME + " TEXT"  + ")";

    // String to create child info table
    String CREATE_CHILD_INFO_TABLE = "CREATE TABLE " + TABLE_CHILDINFO + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PARENT_NAME + " TEXT,"
            + COLUMN_CHILD_NAME + " TEXT,"
            + COLUMN_GENDER + " TEXT,"
            + COLUMN_DOB + " TEXT" + ")";

    // String to create statistics table
    String CREATE_STATISTICS_TABLE = "CREATE TABLE " + TABLE_STATISTICS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PARENT_NAME + " TEXT,"
            + COLUMN_CHILD_NAME + " TEXT,"
            + COLUMN_WENT_TO_BED + " TEXT,"
            + COLUMN_WOKE_UP + " TEXT,"
            + COLUMN_TOTAL_SCREEN_TIME + " TEXT,"
            + COLUMN_TOTAL_FRIENDS_AND_FAMILY_TIME + " TEXT" + ")";

    public static synchronized ChildDBHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new ChildDBHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private ChildDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates the 2 tables used by the app in the Sqlite database
        db.execSQL(CREATE_PARENT_INFO_TABLE);
        db.execSQL(CREATE_CHILD_INFO_TABLE);
        db.execSQL(CREATE_STATISTICS_TABLE);
        app = ChildInfoApp.getInstance();
    }
    public void deleteDB(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If an upgrade is requested, recreate the 3 tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARENTINFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILDINFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);
        onCreate(db);
    }

    public void addOrUpdateChildInfo(ChildInfo ci, Boolean bAdd) {
        app = ChildInfoApp.getInstance();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, ci.getId());
        cv.put(COLUMN_PARENT_NAME, ci.getParentName());

        String f = ci.getChildFirstName().trim();
        String l = ci.getChildLastName().trim();
        String d = app.getDelimiter();

        String strChildName = f + d + l;
        cv.put(COLUMN_CHILD_NAME, strChildName);
        cv.put(COLUMN_GENDER, ci.getMale());
        cv.put(COLUMN_DOB, ci.getDate());
        Log.v("EDITCI", "before add to db bAdd = " + bAdd + "parent name = " + ci.getParentName() + " child = " + strChildName);
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            if (bAdd.equals(true)) {
                db.execSQL("INSERT INTO " + TABLE_CHILDINFO + "( "
                        + COLUMN_PARENT_NAME + ","
                        + COLUMN_CHILD_NAME + ","
                        + COLUMN_GENDER + ","
                        + COLUMN_DOB +
                        " ) VALUES ("
                        + "'" + ci.getParentName() + "',"
                        + "'" + strChildName + "',"
                        + "'" + ci.getMale() + "',"
                        + "'" +  ci.getDate() + "')");

 //    FIXED paijnful bug ...  db.insert(TABLE_CHILDINFO, null, cv);
            Log.v("EDITCI", " After Insert cv values are " + strChildName + " " + ci.getParentName());
            }
            else {
                String strFilter = COLUMN_CHILD_NAME + " = \"" + strChildName + "\"" + " AND " + COLUMN_PARENT_NAME + " = \"" + ci.getParentName() + "\"";
                 db.update(TABLE_CHILDINFO, cv, strFilter, null);
                Log.v("EDITCI", " After update cv values are " + strChildName + " " + ci.getParentName());
            }
        }
        catch (Exception e) {
            Log.v("EDITCI", "Exception insert or update child");

        }
        db.close();

    }
    public void addParent(String strParent) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PARENT_NAME, strParent);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PARENTINFO, null, cv);
        db.close();

    }

    public void addStatistics(Statistics si) {
        app = ChildInfoApp.getInstance();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PARENT_NAME, si.getParentName());
        cv.put(COLUMN_CHILD_NAME, si.getChildFirstName() + app.getDelimiter() + si.getChildLastName());
        cv.put(COLUMN_WENT_TO_BED, si.getWentToBed());
        cv.put(COLUMN_WOKE_UP, si.getWokeUp());
        cv.put(COLUMN_TOTAL_SCREEN_TIME, si.getTotalScreenTime());
        cv.put(COLUMN_TOTAL_FRIENDS_AND_FAMILY_TIME, si.getTotalFriendsAndFamilyTime());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_STATISTICS, null, cv);
        db.close();
    }

    public ChildInfo findChildInfo(String strChildName, String strParentName) {
        app = ChildInfoApp.getInstance();
        String query = "select * from " + TABLE_CHILDINFO +
                " WHERE " + COLUMN_CHILD_NAME + " = \"" + strChildName + "\"" + " AND " + COLUMN_PARENT_NAME + " = \"" + strParentName + "\"";
        Log.v("EDITCI", " In findChild query = " + query + "strChildName = " + strChildName + "strParentName = " + strParentName) ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ChildInfo ci = new ChildInfo();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            ci.setId(cursor.getInt(0));
            ci.setParentName(cursor.getString(1));
            // Here I need to split the ChildName into First and Last. This has been delimited by
            // ZZZ
            String strSplitName = cursor.getString(2);
            String[] names = strSplitName.split(app.getDelimiter());
            ci.setChildFirstName(names[0]);
            ci.setChildLastName(names[1]);
            ci.setMale(cursor.getString(3));
            ci.setDate(cursor.getString(4));
            Log.v("EDITCI", " In findChild loop select values are " + strChildName + " " + ci.getParentName());

            cursor.close();
        } else {
            ci = null;
        }
        return ci;
    }
    public Cursor getParents() {
        String query = "select " + COLUMN_PARENT_NAME + " from " + TABLE_PARENTINFO;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(query, null);
    }

    public Cursor getChildren(String strParent) {
        String query = "SELECT * FROM " + TABLE_CHILDINFO +
                " WHERE " + COLUMN_PARENT_NAME + " = \"" + strParent + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Log.v("EDITCI", "select getChildren query " + query);

        return db.rawQuery(query, null);
    }

    public void emptyTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_CHILDINFO);
        db.execSQL("delete from "+ TABLE_PARENTINFO);
        db.execSQL("delete from "+ TABLE_STATISTICS);
        db.close();
    }
    public void closeDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }

    public Boolean parentNameAlreadyExists (String strParentName) throws SQLException {
        int count = -1;
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT COUNT(*) FROM "
                    + TABLE_PARENTINFO + " WHERE " + COLUMN_PARENT_NAME + " = ?";
            c = db.rawQuery(query, new String[] {strParentName});
            if (c.moveToFirst()) {
                // returns the count found ...
                count = c.getInt(0);
            }
            return count > 0;
        }
        finally {
            if (c != null) {
                c.close();
                db.close();
            }
        }
    }
    public Boolean isParentTableEmpty() throws SQLException {
        int count = -1;
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "SELECT COUNT(*) FROM "
                    + TABLE_PARENTINFO;
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                // returns the count found ...
                count = c.getInt(0);
            }
            return count == 0;
        }
        finally {
            if (c != null) {
                c.close();
                db.close();
            }
        }
    }}
