package cn.flandre.review.data.database;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 把 raw 的 json 文件读取出来
 */
public class ReadDataFromFile {

    public static JSONArray getText(Context context, int id) throws IOException, JSONException {
        InputStreamReader inputStreamReader = new InputStreamReader(context.getResources().openRawResource(id), StandardCharsets.UTF_8);

        String text = readString(inputStreamReader);
        return new JSONArray(text);
    }

    public static JSONObject getText(String path) throws IOException, JSONException {
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);

        String text = readString(inputStreamReader);

        return new JSONObject(text);
    }

    private static String readString(InputStreamReader inputStreamReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            int len;
            char[] chars = new char[1024];
            while (true) {
                len = inputStreamReader.read(chars);
                if (len == -1) break;
                stringBuilder.append(chars, 0, len);
            }
        }finally {
            inputStreamReader.close();
        }
        return stringBuilder.toString();
    }
}
