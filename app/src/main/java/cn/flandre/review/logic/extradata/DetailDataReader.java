package cn.flandre.review.logic.extradata;

import cn.flandre.review.data.bean.DetailData;
import cn.flandre.review.data.database.ReadDataFromFile;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 从磁盘读取 DetailData
 */
public class DetailDataReader {
    private final File root;

    public DetailDataReader(String word, File rootPath) {
        this.root = new File(rootPath, word);
    }

    public boolean available() {
        return root.exists();
    }

    public DetailData readDetailData() throws IOException, JSONException {
        DetailData data = new DetailData();

        JSONObject object = ReadDataFromFile.getText(new File(root, DetailData.JSON).getAbsolutePath());
        String tmp;

        data.setWordId(getJsonString(object, "topic_id", null));
        data.setWordRoot(getJsonString(object, "word_etyma", null));
        data.setWordExplanation(getJsonString(object, "mean_en", null));
        data.setSentence(getJsonString(object, "sentence", null));
        data.setSentenceMeans(getJsonString(object, "sentence_trans", null));

        tmp = getJsonString(object, "word_audio", null);
        data.setWordAudioPath(tmp != null ? new File(root, tmp).getAbsolutePath() : null);

        tmp = getJsonString(object, "deformation_img", null);
        data.setWordImagePath(tmp != null ? new File(root, tmp).getAbsolutePath() : null);

        tmp = getJsonString(object, "sentence_audio", null);
        data.setSentenceAudioPath(tmp != null ? new File(root, tmp).getAbsolutePath() : null);

        tmp = getJsonString(object, "image_file", null);
        data.setSentenceImagePath(tmp != null ? new File(root, tmp).getAbsolutePath() : null);

        return data;
    }

    private String getJsonString(JSONObject object, String key, String def){
        try {
            String string = object.getString(key);
            if (string.isEmpty())
                return def;
            else
                return string;
        }catch (JSONException e){
            return def;
        }
    }
}
