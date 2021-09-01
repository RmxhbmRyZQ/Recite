package cn.flandre.review.logic.extradata;

import cn.flandre.review.logic.callback.OnRequestZPK;
import cn.flandre.review.data.bean.ZPKData;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 从网上请求 zpk 数据
 */
public class DetailDataRequest {
    private static final String URL1 = "http://ali.bczcdn.com";
    private static final String URL2 = "http://7n.bczcdn.com";
    private static final OkHttpClient client;
    private final static int TIMEOUT = 10 * 1000;

    static {
        client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .connectionPool(new ConnectionPool(4, TIMEOUT, TimeUnit.MILLISECONDS))
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    private final String url;

    public DetailDataRequest(ZPKData zpkData) {
        url = URL1 + zpkData.getPath();
    }

    public void getZPKFile(OnRequestZPK onRequestZPK){
        Request request = new Request.Builder().url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onRequestZPK.onFail(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody body = response.body();
                assert body != null;
                byte[] zpk = body.bytes();
                onRequestZPK.onSuccess(zpk);
                body.close();
            }
        });
    }
}
