package cn.flandre.review.logic.extradata;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import cn.flandre.review.logic.callback.OnRequestDetailData;
import cn.flandre.review.logic.callback.OnRequestZPK;
import cn.flandre.review.data.bean.DetailData;
import cn.flandre.review.data.bean.Word;
import cn.flandre.review.data.bean.ZPKData;
import cn.flandre.review.data.database.SQLHelper;
import cn.flandre.review.data.database.SQLRecite;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 额外数据的获取者
 */
public class DetailDataFactory {
    public static void requestDetailData(Context context, Word word, OnRequestDetailData callback) {
        File path = new File(context.getExternalFilesDir(null), "work_infos");
        if (!path.exists())
            path.mkdir();
        DetailDataReader reader = new DetailDataReader(word.getWord(), path);
        if (reader.available()) {
            try {
                callback.onSuccess(reader.readDetailData());
            } catch (IOException | JSONException e) {
                callback.onFail(e);
            }
            return;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        ZPKData zpkData = SQLHelper.getZPKData(word.getId(), SQLRecite.getSQLRecite());
        DetailDataRequest request = new DetailDataRequest(zpkData);
        request.getZPKFile(new OnRequestZPK() {
            @Override
            public void onSuccess(byte[] zpk) {
                DetailDataWriter detailDataWriter = new DetailDataWriter(zpk, word.getWord(), path);
                try {
                    detailDataWriter.write();
                    // 这里可以直接解析内存里面的 zpk，而不是从磁盘里面再读取
                    // 但这点性能消耗有关系吗？没有关系
                    DetailData detailData = reader.readDetailData();

                    // 切回主线程
                    handler.post(() -> {
                        callback.onSuccess(detailData);
                    });
                } catch (IOException | JSONException e) {
                    handler.post(() -> {
                        callback.onFail(e);
                    });
                }
            }

            @Override
            public void onFail(IOException e) {
                handler.post(() -> {
                    callback.onFail(e);
                });
            }
        });
    }
}
