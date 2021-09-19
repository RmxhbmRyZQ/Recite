package cn.flandre.review.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static cn.flandre.review.logic.recite.ReciteTimeManager.DEFAULT_RECITE_TIME;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 数据库类本类
 */
public class SQLRecite extends SQLiteOpenHelper {
    public static final String ALL_WORDS = "ALL_WORDS";  // 全部单词的表
    public static final String RECITE_RECORD = "RECITE_RECORD";  // 背诵记录的表
    public static final String TMPGW = "TMPGW";  // 临时记录表
    public static final String WGW = "WGW";  // 错误记录表
    public static final String FHWGW = "FHWGW";  // 小时后复习的记录表
    public static final String FDWGW = "FDWGW";  // 一天后复习记录表
    public static final String SGW = "SGW";  // 怀疑的暂存表
    public static final String ZPK = "ZPK";  // zpk 文件记录表

    private static SQLRecite sqlRecite = null;

    private SQLRecite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    public static SQLRecite getSQLRecite(Context context) {
        if (sqlRecite == null) {
            sqlRecite = newInstance(context.getApplicationContext(), "recite.db", 3);
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
                "WORD VARCHAR(64)," +  // 单词字母
                "ACCENT VARCHAR(128)," +  // 英标
                "MEANS VARCHAR(255)," +  // 单词意思
                "FREQ TINYINT," +
                "LENGTH TINYINT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + RECITE_RECORD + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +  // ALL_WORDS 的 ID
                "WORD_ID VARCHAR(128)," +
                "WRONG_INDEX VARCHAR(128)," +  // 常错单词
                "CORRECT_INDEX VARCHAR(128)," +  // 常对单词
                "WRONG_TIMES VARCHAR(128)," +  // 错误次数
                "RIGHT_TIMES VARCHAR(128)," +  // 正确次数
                "RECITE_TIME BIGINT," +  // 背诵时间
                "LAST_RECITE_TIME BIGINT," +  // 上次复习时间
                "NEXT_RECITE_TIME BIGINT," +  // 下次复习时间
                "RECITE_TIMES INTEGER," +  // 背诵次数
                "GROUP_ID INTEGER)");  // 所属组

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TMPGW + "(" +
                "WORD_ID INTEGER," +  // RECITE_RECORD 的 ID
                "WRONG VARCHAR(128)," +  // 错的位置
                "TIME BIGINT DEFAULT " + DEFAULT_RECITE_TIME + "," +  // 上次背诵时间
                "TYPE INTEGER)");  // 背诵类型

        db.execSQL("CREATE TABLE IF NOT EXISTS " + WGW + "(" +
                "WORD_ID INTEGER," +
                "WRONG VARCHAR(128)," +
                "TIME BIGINT DEFAULT " + DEFAULT_RECITE_TIME + "," +
                "TYPE INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + FHWGW + "(" +
                "WORD_ID INTEGER," +
                "WRONG VARCHAR(128)," +
                "TIME BIGINT DEFAULT " + DEFAULT_RECITE_TIME + "," +
                "TYPE INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + FDWGW + "(" +
                "WORD_ID INTEGER," +
                "WRONG VARCHAR(128)," +
                "TIME BIGINT DEFAULT " + DEFAULT_RECITE_TIME + "," +
                "TYPE INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + SGW + "(" +
                "WORD_ID INTEGER," +
                "WRONG VARCHAR(128)," +
                "TIME BIGINT DEFAULT " + DEFAULT_RECITE_TIME + "," +
                "TYPE INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ZPK + "(" +
                "WORD_ID INTEGER," +  // ALL_WORDS 的 ID
                "ZPK_PATH VARCHAR(128)," +  // ZPK 的路径
                "MD5 VARCHAR(128)," +
                "COVERAGE TINYINT," +
                "VERSION INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("CREATE TABLE IF NOT EXISTS " + SGW + "(" +
                        "WORD_ID INTEGER," +
                        "WRONG VARCHAR(128)," +
                        "TIME BIGINT DEFAULT " + DEFAULT_RECITE_TIME + "," +
                        "TYPE INTEGER)");

                db.execSQL("CREATE TABLE IF NOT EXISTS " + ZPK + "(" +
                        "WORD_ID INTEGER," +  // ALL_WORDS 的 ID
                        "ZPK_PATH VARCHAR(128)," +  // ZPK 的路径
                        "MD5 VARCHAR(128)," +
                        "COVERAGE TINYINT," +
                        "VERSION INTEGER)");
                break;
            case 2:
                db.execSQL("ALTER TABLE '" + RECITE_RECORD + "' ADD  'GROUP_ID' INTEGER");
                break;
        }
    }
}
