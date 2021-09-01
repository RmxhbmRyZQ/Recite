package cn.flandre.review.data.bean;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * ZPK 文件解析的数据类
 */
public class DetailData {
    public static final String JSON = "meta.json";

    private String wordId;
    private String wordAudioPath;
    private String WordImagePath;
    private String wordRoot;
    private String wordExplanation;
    private String sentence;
    private String sentenceMeans;
    private String sentenceAudioPath;
    private String sentenceImagePath;

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getWordAudioPath() {
        return wordAudioPath;
    }

    public void setWordAudioPath(String wordAudioPath) {
        this.wordAudioPath = wordAudioPath;
    }

    public String getWordImagePath() {
        return WordImagePath;
    }

    public void setWordImagePath(String wordImagePath) {
        WordImagePath = wordImagePath;
    }

    public String getWordRoot() {
        return wordRoot;
    }

    public void setWordRoot(String wordRoot) {
        this.wordRoot = wordRoot;
    }

    public String getWordExplanation() {
        return wordExplanation;
    }

    public void setWordExplanation(String wordExplanation) {
        this.wordExplanation = wordExplanation;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentenceMeans() {
        return sentenceMeans;
    }

    public void setSentenceMeans(String sentenceMeans) {
        this.sentenceMeans = sentenceMeans;
    }

    public String getSentenceAudioPath() {
        return sentenceAudioPath;
    }

    public void setSentenceAudioPath(String sentenceAudioPath) {
        this.sentenceAudioPath = sentenceAudioPath;
    }

    public String getSentenceImagePath() {
        return sentenceImagePath;
    }

    public void setSentenceImagePath(String sentenceImagePath) {
        this.sentenceImagePath = sentenceImagePath;
    }
}
