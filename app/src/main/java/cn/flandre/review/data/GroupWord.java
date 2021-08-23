package cn.flandre.review.data;

import java.io.Serializable;

public class GroupWord implements Serializable {
    private int id;

    private int []wordId;
    private boolean []wrongIndex;
    private boolean []correctIndex;

    private int []wrongTimes;
    private int []rightTimes;

    // 具体数据从数据库拿, 不会存储到硬盘
    private Word [] word;
    // 用于文件存储时使用
    private boolean []wrong;
    private int type;

    private long reciteTime;
    private long lastReciteTime;
    private long nextReciteTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getWordId() {
        return wordId;
    }

    public void setWordId(int[] wordId) {
        this.wordId = wordId;
    }

    public boolean[] getWrongIndex() {
        return wrongIndex;
    }

    public void setWrongIndex(boolean[] wrongIndex) {
        this.wrongIndex = wrongIndex;
    }

    public boolean[] getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(boolean[] correctIndex) {
        this.correctIndex = correctIndex;
    }

    public int[] getWrongTimes() {
        return wrongTimes;
    }

    public void setWrongTimes(int[] wrongTimes) {
        this.wrongTimes = wrongTimes;
    }

    public int[] getRightTimes() {
        return rightTimes;
    }

    public void setRightTimes(int[] rightTimes) {
        this.rightTimes = rightTimes;
    }

    public Word[] getWord() {
        return word;
    }

    public void setWord(Word[] word) {
        this.word = word;
    }

    public boolean[] getWrong() {
        return wrong;
    }

    public void setWrong(boolean[] wrong) {
        this.wrong = wrong;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getReciteTime() {
        return reciteTime;
    }

    public void setReciteTime(long reciteTime) {
        this.reciteTime = reciteTime;
    }

    public long getLastReciteTime() {
        return lastReciteTime;
    }

    public void setLastReciteTime(long lastReciteTime) {
        this.lastReciteTime = lastReciteTime;
    }

    public long getNextReciteTime() {
        return nextReciteTime;
    }

    public void setNextReciteTime(long nextReciteTime) {
        this.nextReciteTime = nextReciteTime;
    }
}
