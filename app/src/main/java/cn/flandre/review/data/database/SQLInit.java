package cn.flandre.review.data.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import cn.flandre.review.R;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import static cn.flandre.review.data.database.SQLRecite.*;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 数据库舒适化数据类
 */
public class SQLInit {
    private static final int MAX_VERSION = 1;

    public static boolean available(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("review", Activity.MODE_PRIVATE);
        int version = sharedPreferences.getInt("version", 0);

        return version <= MAX_VERSION;
    }

    /**
     * 初始化数据库的数据，数据库的数据都是内部文件自带的:)
     */
    public static void init(Context context) {
        int version, v;
        SQLRecite sqlRecite = SQLRecite.getSQLRecite();

        // 获取当前的版本保证初始化代码只运行一次
        version = v = ShareHelper.getDatabaseVersion(context, 0);

        switch (version) {
            case 0:
                addAllWord(context, sqlRecite);
                addAllGroupData(context, sqlRecite);
                version++;  // 不要忘记结尾处++
            case MAX_VERSION:
                addAllZPK(context, sqlRecite);
                version++;
                break;
        }

        if (v != version) {
            ShareHelper.setDatabaseVersion(context, version);
        }
    }

    /**
     * 把文本的单词全部加入数据库
     */
    public static void addAllWord(Context context, SQLRecite sqlRecite) {
        SQLiteDatabase db = sqlRecite.getReadableDatabase();

//        Cursor c = db.query("ALL_WORDS", null, null, null, null, null, null);
//        boolean hasData = c.getCount() > 0;
//        c.close();
//
//        if (hasData) return;

        db.beginTransaction();
        try {
            JSONArray text = ReadDataFromFile.getText(context, R.raw.bczall);
            for (int i = 0; i < text.length(); i++) {
                JSONArray array = (JSONArray) text.get(i);
                db.execSQL("INSERT INTO " + ALL_WORDS + " (ID,WORD,ACCENT,MEANS,FREQ,LENGTH) VALUES (?,?,?,?,?,?)",
                        new String[]{array.getString(0), array.getString(1), array.getString(2),
                                array.getString(3), array.getString(4), array.getString(5)});
            }
            db.setTransactionSuccessful();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 把文本的单词组全部加入数据库
     */
    public static void addAllGroupData(Context context, SQLRecite sqlRecite) {
        SQLiteDatabase db = sqlRecite.getReadableDatabase();

//        Cursor c = db.query("RECITE_RECORD", null, null, null, null, null, null);
//        boolean hasData = c.getCount() > 0;
//        c.close();
//
//        if (hasData) return;

        db.beginTransaction();
        try {
            JSONArray text = ReadDataFromFile.getText(context, R.raw.word_data);

            for (int i = 0; i < text.length(); i++) {
                String[] data = text.getString(i).split(":");
                db.execSQL("INSERT INTO " + RECITE_RECORD + " (WORD_ID, WRONG_INDEX, CORRECT_INDEX,WRONG_TIMES, RIGHT_TIMES," +
                        "RECITE_TIME, LAST_RECITE_TIME, NEXT_RECITE_TIME, RECITE_TIMES)VALUES(?,?,?,?,?,?,?,?,?)", new String[]{
                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]
                });
            }
            db.setTransactionSuccessful();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static void addAllZPK(Context context, SQLRecite sqlRecite) {
        SQLiteDatabase db = sqlRecite.getReadableDatabase();

//        Cursor c = db.query("RECITE_RECORD", null, null, null, null, null, null);
//        boolean hasData = c.getCount() > 0;
//        c.close();
//
//        if (hasData) return;

        db.beginTransaction();
        try {
            JSONArray text = ReadDataFromFile.getText(context, R.raw.extra_data);
            for (int i = 0; i < text.length(); i++) {
                JSONArray jsonArray = text.getJSONArray(i);
                db.execSQL("INSERT INTO " + ZPK + " (WORD_ID, ZPK_PATH, MD5,COVERAGE, VERSION)VALUES" +
                        "(?,?,?,?,?)", new String[]{jsonArray.getString(0), jsonArray.getString(1),
                        jsonArray.getString(2), jsonArray.getString(3), jsonArray.getString(4)});
            }
            db.setTransactionSuccessful();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
