package cn.flandre.review.database;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ReadDataFromFile {
    private final Context context;

    public ReadDataFromFile(Context context) {
        this.context = context;
    }

    public JSONArray getText(int id) throws IOException, JSONException {
        InputStreamReader inputStreamReader = new InputStreamReader(context.getResources().openRawResource(id), StandardCharsets.UTF_8);;
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
        String text = stringBuilder.toString();
        return new JSONArray(text);
    }
}
