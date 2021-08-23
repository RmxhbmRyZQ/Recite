package cn.flandre.review.data;

public class ReciteData {
    private boolean isGetData;
    private final long time;
    private final int type;
    private boolean hasWrongWord = false;

    public ReciteData(boolean isGetData, int type) {
        this.isGetData = isGetData;
        this.type = type;
        time = System.currentTimeMillis();
    }

    public boolean isGetData() {
        return isGetData;
    }

    public void setGetData(boolean getData) {
        isGetData = getData;
    }

    public long getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public boolean isHasWrongWord() {
        return hasWrongWord;
    }

    public void setHasWrongWord(boolean hasWrongWord) {
        this.hasWrongWord = hasWrongWord;
    }
}