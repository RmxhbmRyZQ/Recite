package cn.flandre.review.data.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.flandre.review.data.bean.GroupWord;
import cn.flandre.review.data.bean.ZPKData;
import cn.flandre.review.tools.GroupWordHelper;
import cn.flandre.review.data.bean.Word;
import cn.flandre.review.logic.recite.ReciteTimeManager;

import java.util.ArrayList;
import java.util.List;

import static cn.flandre.review.data.database.SQLRecite.ZPK;
import static cn.flandre.review.logic.recite.ReciteTimeManager.DEFAULT_RECITE_TIME;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 数据库帮助类
 */
public class SQLHelper {
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
        groupWord.setSuspect(new boolean[groupWord.getWordId().length]);
        groupWord.setReciteTime(cursor.getLong(6));
        groupWord.setLastReciteTime(cursor.getLong(7));
        groupWord.setNextReciteTime(cursor.getLong(8));
        groupWord.setReciteTimes(cursor.getInt(9));
        groupWord.setGroup(cursor.getInt(10));
        GroupWordHelper.setWords(groupWord, sqlRecite);
    }

    /**
     * 获取组单词
     */
    public static GroupWord getGroupWord(int id, SQLRecite sqlRecite) {
        GroupWord groupWord = new GroupWord();

        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        Cursor cursor = readableDatabase.query("RECITE_RECORD", new String[]{"ID", "WORD_ID", "WRONG_INDEX",
                "CORRECT_INDEX", "WRONG_TIMES", "RIGHT_TIMES", "RECITE_TIME", "LAST_RECITE_TIME", "NEXT_RECITE_TIME",
                "RECITE_TIMES", "GROUP_ID"}, "ID=?", new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToNext();
        setGroupWord(groupWord, cursor, sqlRecite);
        cursor.close();
        return groupWord;
    }

    public static List<GroupWord> getAllGroupWords(SQLRecite sqlRecite) {
        ArrayList<GroupWord> list = new ArrayList<>();
        String date = String.valueOf(ReciteTimeManager.turnSystemTimeToNormalTime(System.currentTimeMillis()));

        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        // 越迟背优先(可能是比较不熟)，LAST_RECITE_TIME 上次背诵时间越近越迟背诵(第一优先位)，ID 越大越迟背诵(第二优先位)
        Cursor cursor = readableDatabase.query("RECITE_RECORD", new String[]{"ID", "WORD_ID", "WRONG_INDEX",
                "CORRECT_INDEX", "WRONG_TIMES", "RIGHT_TIMES", "RECITE_TIME", "LAST_RECITE_TIME", "NEXT_RECITE_TIME",
                "RECITE_TIMES", "GROUP_ID"}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            GroupWord groupWord = new GroupWord();
            setGroupWord(groupWord, cursor, sqlRecite);
            list.add(groupWord);
        }

        cursor.close();
        return list;
    }

    /**
     * 获取要背的单词
     */
    public static List<GroupWord> getGroupWords(SQLRecite sqlRecite, String max) {
        ArrayList<GroupWord> list = new ArrayList<>();
        String date = String.valueOf(ReciteTimeManager.turnSystemTimeToNormalTime(System.currentTimeMillis()));

        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        // 越迟背优先(可能是比较不熟)，LAST_RECITE_TIME 上次背诵时间越近越迟背诵(第一优先位)，ID 越大越迟背诵(第二优先位)
        Cursor cursor = readableDatabase.query("RECITE_RECORD", new String[]{"ID", "WORD_ID", "WRONG_INDEX",
                        "CORRECT_INDEX", "WRONG_TIMES", "RIGHT_TIMES", "RECITE_TIME", "LAST_RECITE_TIME", "NEXT_RECITE_TIME",
                        "RECITE_TIMES", "GROUP_ID"}, "NEXT_RECITE_TIME<=?", new String[]{date}, null, null,
                "-LAST_RECITE_TIME, -ID", max);

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
        values.put("RECITE_TIMES", groupWord.getReciteTimes());
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
        readableDatabase.update(tableName, values, "WORD_ID=? AND TYPE=?",
                new String[]{String.valueOf(groupWord.getId()), String.valueOf(groupWord.getType())});
    }

    /**
     * 更新信息到临时存储表(带时间)
     */
    public static void updateTimeGWT(String tableName, long time, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("TIME", time);
        readableDatabase.update(tableName, values, "TIME = ?", new String[]{String.valueOf(DEFAULT_RECITE_TIME)});
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

    public static GroupWord queryGWT(String tableName, GroupWord word, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        Cursor query = readableDatabase.query(tableName, new String[]{"WORD_ID", "WRONG", "TYPE"},
                "WORD_ID=? AND TYPE=?", new String[]{String.valueOf(word.getId()),
                        String.valueOf(word.getType())}, null, null, null);
        GroupWord groupWord = null;

        if (query.moveToNext()) {
            groupWord = new GroupWord();
            // groupWord = getGroupWord(query.getInt(0), sqlRecite);
            groupWord.setId(query.getInt(0));
            GroupWordHelper.setWrong(groupWord, query.getString(1));
            groupWord.setType(query.getInt(2));
        }

        query.close();
        return groupWord;
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

        Cursor query = readableDatabase.query(tableName, null, "TIME <= ? AND TYPE = ?",
                new String[]{String.valueOf(time), String.valueOf(type)}, null, null, null);
        empty = query.getCount() == 0;
        query.close();

        return empty;
    }

    public static boolean isEmptyEQTGWT(String tableName, int type, long time, SQLRecite sqlRecite) {
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        boolean empty;

        Cursor query = readableDatabase.query(tableName, null, "TIME = ? AND TYPE = ?",
                new String[]{String.valueOf(time), String.valueOf(type)}, null, null, null);
        empty = query.getCount() == 0;
        query.close();

        return empty;
    }

    /**
     * 返回 ZPKData 数据
     */
    public static ZPKData getZPKData(int wordId, SQLRecite sqlRecite) {
        ZPKData zpkData = new ZPKData();
        SQLiteDatabase readableDatabase = sqlRecite.getReadableDatabase();
        Cursor query = readableDatabase.query(ZPK, new String[]{"WORD_ID", "ZPK_PATH", "MD5", "COVERAGE", "VERSION"},
                "WORD_ID=?", new String[]{String.valueOf(wordId)}, null, null, null);
        query.moveToNext();

        zpkData.setId(query.getInt(0));
        zpkData.setPath(query.getString(1));
        zpkData.setMd5(query.getString(2));
        zpkData.setCoverage(query.getInt(3));
        zpkData.setVersion(query.getInt(4));

        query.close();
        return zpkData;
    }
}
