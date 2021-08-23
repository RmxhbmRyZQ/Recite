package cn.flandre.review.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static cn.flandre.review.recite.ReciteTimeManager.DEFAULT_RECITE_TIME;

public class SQLRecite extends SQLiteOpenHelper {
    public static final String ALL_WORDS = "ALL_WORDS";
    public static final String RECITE_RECORD = "RECITE_RECORD";
    public static final String TMPGW = "TMPGW";
    public static final String WGW = "WGW";
    public static final String FHWGW = "FHWGW";
    public static final String FDWGW = "FDWGW";

    private static SQLRecite sqlRecite = null;

    private SQLRecite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    public static SQLRecite getSQLRecite(Context context) {
        if (sqlRecite == null) {
            sqlRecite = newInstance(context.getApplicationContext(), "recite.db", 1);
        }
        return sqlRecite;
    }

    public static SQLRecite getSQLRecite() {
        return sqlRecite;
    }

    public static SQLRecite newInstance(Context context, String name, int version) {
        if (sqlRecite == null) {
            sqlRecite = new SQLRecite(context, name, null, version);
        }
        return sqlRecite;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ALL_WORDS + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "WORD VARCHAR(64)," +
                "ACCENT VARCHAR(128)," +
                "MEANS VARCHAR(255)," +
                "FREQ TINYINT," +
                "LENGTH TINYINT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+RECITE_RECORD+"(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "WORD_ID VARCHAR(128)," +
                "WRONG_INDEX VARCHAR(128)," +
                "CORRECT_INDEX VARCHAR(128)," +
                "WRONG_TIMES VARCHAR(128)," +
                "RIGHT_TIMES VARCHAR(128)," +
                "RECITE_TIME BIGINT," +
                "LAST_RECITE_TIME BIGINT," +
                "NEXT_RECITE_TIME BIGINT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+TMPGW+"(" +
                "WORD_ID INTEGER," +
                "WRONG VARCHAR(128)," +
                "TIME BIGINT DEFAULT " + DEFAULT_RECITE_TIME + "," +
                "TYPE INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+WGW+"(" +
                "WORD_ID INTEGER," +
                "WRONG VARCHAR(128)," +
                "TIME BIGINT DEFAULT " + DEFAULT_RECITE_TIME + "," +
                "TYPE INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+FHWGW+"(" +
                "WORD_ID INTEGER," +
                "WRONG VARCHAR(128)," +
                "TIME BIGINT DEFAULT " + DEFAULT_RECITE_TIME + "," +
                "TYPE INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+FDWGW+"(" +
                "WORD_ID INTEGER," +
                "WRONG VARCHAR(128)," +
                "TIME BIGINT DEFAULT " + DEFAULT_RECITE_TIME + "," +
                "TYPE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
