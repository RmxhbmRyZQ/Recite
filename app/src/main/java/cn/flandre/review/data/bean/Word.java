package cn.flandre.review.data.bean;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 单词信息类，对照 ALL_WORDS 表
 */
public class Word {
    private int id;
    private String word;
    private String accent;
    private String means;
    private int freq;
    private int len;

    public Word(int id, String word, String accent, String means, int freq, int len) {
        this.id = id;
        this.word = word;
        this.accent = accent;
        this.means = means;
        this.freq = freq;
        this.len = len;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getAccent() {
        return accent;
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }

    public String getMeans() {
        return means;
    }

    public void setMeans(String means) {
        this.means = means;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
}
