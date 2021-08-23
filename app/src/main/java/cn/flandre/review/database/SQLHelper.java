package cn.flandre.review.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.flandre.review.R;
import cn.flandre.review.data.GroupWord;
import cn.flandre.review.tools.GroupWordHelper;
import cn.flandre.review.data.Word;
import cn.flandre.review.recite.ReciteTimeManager;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cn.flandre.review.controller.Controller.MAX_REVIEW_GROUP_COUNT;
import static cn.flandre.review.recite.ReciteTimeManager.DEFAULT_RECITE_TIME;

public class SQLHelper {
    /**
     * 把文本的单词全部加入数据库
     */
    public static void addAllWord(Context context, SQLRecite sqlRecite) {
        SQLiteDatabase db = sqlRecite.getReadableDatabase();

        Cursor c = db.query("ALL_WORDS", null, null, null, null, null, null);
        boolean hasData = c.getCount() > 0;
        c.close();

        if (hasData) return;

        db.beginTransaction();
        try {
            JSONArray text = new ReadDataFromFile(context).getText(R.raw.bczall);
            for (int i = 0; i < text.length(); i++) {
                JSONArray array = (JSONArray) text.get(i);
                db.execSQL("INSERT INTO ALL_WORDS (ID,WORD,ACCENT,MEANS,FREQ,LENGTH) VALUES (?,?,?,?,?,?)",
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

        Cursor c = db.query("RECITE_RECORD", null, null, null, null, null, null);
        boolean hasData = c.getCount() > 0;
        c.close();

        if (hasData) return;

        db.beginTransaction();
        try {
            JSONArray text = new ReadDataFromFile(context).getText(R.raw.word_data);

            for (int i = 0; i < text.length(); i++) {
                String[] data = text.getString(i).split(":");
                db.execSQL("INSERT INTO RECITE_RECORD (WORD_ID, WRONG_INDEX, CORRECT_INDEX,WRONG_TIMES, RIGHT_TIMES," +
                        "RECITE_TIME, LAST_RECITE_TIME, NEXT_RECITE_TIME)VALUES(?,?,?,?,?,?,?,?)", new String[]{
                        data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]
                });
            }
            db.setTransactionSuccessful();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 获取单词信息
     */
    public static Word getWord(int id, SQLRecite sqlRecite) {
        Word word = null;
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        Cursor cursor = readableDatabase.query("ALL_WORDS", new String[]{"ID", "WORD", "ACCENT", "MEANS", "FREQ", "LENGTH"},
                "ID=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToNext()) {
            word = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
        }
        cursor.close();
        return word;
    }

    private static void setGroupWord(GroupWord groupWord, Cursor cursor, SQLRecite sqlRecite) {
        groupWord.setId(cursor.getInt(0));
        GroupWordHelper.setWordId(groupWord, cursor.getString(1));
        GroupWordHelper.setWrongIndex(groupWord, cursor.getString(2));
        GroupWordHelper.setCorrectIndex(groupWord, cursor.getString(3));
        GroupWordHelper.setWrongTimes(groupWord, cursor.getString(4));
        GroupWordHelper.setRightTimes(groupWord, cursor.getString(5));
        groupWord.setWrong(new boolean[groupWord.getWordId().length]);
        groupWord.setReciteTime(cursor.getLong(6));
        groupWord.setLastReciteTime(cursor.getLong(7));
        groupWord.setNextReciteTime(cursor.getLong(8));
        GroupWordHelper.setWords(groupWord, sqlRecite);
    }

    /**
     * 获取组单词
     */
    public static GroupWord getGroupWord(int id, SQLRecite sqlRecite) {
        GroupWord groupWord = new GroupWord();

        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        Cursor cursor = readableDatabase.query("RECITE_RECORD", new String[]{"ID", "WORD_ID", "WRONG_INDEX",
                        "CORRECT_INDEX", "WRONG_TIMES", "RIGHT_TIMES", "RECITE_TIME", "LAST_RECITE_TIME", "NEXT_RECITE_TIME"},
                "ID=?", new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToNext();
        setGroupWord(groupWord, cursor, sqlRecite);
        cursor.close();
        return groupWord;
    }

    /**
     * 获取要背的单词
     */
    public static List<GroupWord> getGroupWords(SQLRecite sqlRecite) {
        ArrayList<GroupWord> list = new ArrayList<>();
        String date = String.valueOf(ReciteTimeManager.turnSystemTimeToNormalTime(System.currentTimeMillis()));

        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        // 越迟背优先(可能是比较不熟)，LAST_RECITE_TIME 上次背诵时间越近越迟背诵(第一优先位)，ID 越大越迟背诵(第二优先位)
        Cursor cursor = readableDatabase.query("RECITE_RECORD", new String[]{"ID", "WORD_ID", "WRONG_INDEX",
                        "CORRECT_INDEX", "WRONG_TIMES", "RIGHT_TIMES", "RECITE_TIME", "LAST_RECITE_TIME", "NEXT_RECITE_TIME"},
                "NEXT_RECITE_TIME<=?", new String[]{date}, null, null, "-LAST_RECITE_TIME, -ID",
                MAX_REVIEW_GROUP_COUNT);

        while (cursor.moveToNext()) {
            GroupWord groupWord = new GroupWord();
            setGroupWord(groupWord, cursor, sqlRecite);
            list.add(groupWord);
        }

        cursor.close();
        return list;
    }

    /**
     * 更新组单词信息
     */
    public static void updateGroupWord(GroupWord groupWord, long last, long next, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("WRONG_INDEX", GroupWordHelper.getWrongIndex(groupWord));
        values.put("CORRECT_INDEX", GroupWordHelper.getCorrectIndex(groupWord));
        values.put("WRONG_TIMES", GroupWordHelper.getWrongTimes(groupWord));
        values.put("RIGHT_TIMES", GroupWordHelper.getRightTimes(groupWord));
        if (last != DEFAULT_RECITE_TIME) values.put("LAST_RECITE_TIME", last);
        if (next != DEFAULT_RECITE_TIME) values.put("NEXT_RECITE_TIME", next);
        readableDatabase.update("RECITE_RECORD", values, "id=?", new String[]{String.valueOf(groupWord.getId())});
    }

    /**
     * 清空某个临时单词表
     */
    public static void clearGWT(String tableName, int type, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        readableDatabase.delete(tableName, "TYPE = ?", new String[]{String.valueOf(type)});
    }

    /**
     * 插入当不存在 ID 类别 时间
     */
    public static void insertIfNotExists(String tableName, GroupWord groupWord, long time, int type, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        Cursor cursor = readableDatabase.query(tableName, new String[]{"WRONG", "TIME"},
                "WORD_ID = ? AND TYPE = ? AND TIME = ?", new String[]{String.valueOf(
                        groupWord.getId()), String.valueOf(type), String.valueOf(type)},
                null, null, null);

        if (cursor.getCount() == 0) {
            insertGWT(tableName, groupWord, time, type, sqlRecite);
        }

        cursor.close();
    }

    public static void insertGWT(String tableName, GroupWord groupWord, int type, SQLRecite sqlRecite) {
        insertGWT(tableName, groupWord, DEFAULT_RECITE_TIME, type, sqlRecite);
    }

    /**
     * 插入组信息到临时存储表
     */
    public static void insertGWT(String tableName, GroupWord groupWord, long time, int type, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("WORD_ID", groupWord.getId());
        values.put("WRONG", GroupWordHelper.getWrong(groupWord));
        if (time != DEFAULT_RECITE_TIME) values.put("TIME", time);
        values.put("TYPE", type);
        readableDatabase.insert(tableName, null, values);
    }

    /**
     * 更新信息到临时存储表
     */
    public static void updateGWT(String tableName, GroupWord groupWord, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("WRONG", GroupWordHelper.getWrong(groupWord));
        readableDatabase.update(tableName, values, "WORD_ID=?", new String[]{String.valueOf(groupWord.getId())});
    }

    /**
     * 更新信息到临时存储表(带时间)
     */
    public static void updateTimeGWT(String tableName, long time, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("TIME", time);
        readableDatabase.update(tableName, values, "TIME = " + DEFAULT_RECITE_TIME, null);
    }

    /**
     * 删除某个临时表的一条数据
     */
    public static void deleteGWT(String tableName, GroupWord groupWord, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        readableDatabase.delete(tableName, "WORD_ID=? AND TYPE = ?",
                new String[]{String.valueOf(groupWord.getId()), String.valueOf(groupWord.getType())});
    }

    private static List<GroupWord> getQueryGW(Cursor query, SQLRecite sqlRecite) {
        List<GroupWord> list = new ArrayList<>();
        while (query.moveToNext()) {
            GroupWord groupWords = getGroupWord(query.getInt(0), sqlRecite);
            GroupWordHelper.setWrong(groupWords, query.getString(1));
            groupWords.setType(query.getInt(2));
            list.add(groupWords);
        }

        query.close();

        return list;
    }

    /**
     * 查询临时文件表
     */
    public static List<GroupWord> queryTimeGWT(String tableName, long time, int type, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        Cursor query = readableDatabase.query(tableName, new String[]{"WORD_ID", "WRONG", "TYPE"},
                "TIME <= ? AND TYPE = ?", new String[]{String.valueOf(time), String.valueOf(type)},
                null, null, null);
        return getQueryGW(query, sqlRecite);
    }

    public static List<GroupWord> queryGWT(String tableName, long time, int type, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        Cursor query = readableDatabase.query(tableName, new String[]{"WORD_ID", "WRONG", "TYPE"},
                "TIME = ? AND TYPE = ?", new String[]{String.valueOf(time), String.valueOf(type)},
                null, null, null);
        return getQueryGW(query, sqlRecite);
    }

    public static List<GroupWord> queryGWT(String tableName, int type, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        Cursor query = readableDatabase.query(tableName, new String[]{"WORD_ID", "WRONG", "TYPE"},
                "TYPE = ?", new String[]{String.valueOf(type)}, null, null, null);
        return getQueryGW(query, sqlRecite);
    }

    /**
     * 临时文件表是否为空
     */
    public static boolean isEmptyGWT(String tableName, int type, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        boolean empty;

        Cursor query = readableDatabase.query(tableName, null, "TYPE = ?",
                new String[]{String.valueOf(type)}, null, null, null);
        empty = query.getCount() == 0;
        query.close();

        return empty;
    }

    public static boolean isEmptyGWT(String tableName, int type, long time, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        boolean empty;

        Cursor query = readableDatabase.query(tableName, null, "TIME = ? AND TYPE = ?",
                new String[]{String.valueOf(time), String.valueOf(type)}, null, null, null);
        empty = query.getCount() == 0;
        query.close();

        return empty;
    }
}
