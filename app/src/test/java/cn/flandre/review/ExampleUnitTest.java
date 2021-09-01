package cn.flandre.review;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final long TIMEOUT = 10 * 1000;

    @Test
    public void addition_isCorrect() throws ParseException {
        OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .connectionPool(new ConnectionPool(4, TIMEOUT, TimeUnit.MILLISECONDS))
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder().url("http://7n.bczcdn.com/r/zp_185_13_0_20200728113319.zpk").get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody body = response.body();
                assert body != null;
                byte[] zpk = body.bytes();

                synchronized (ExampleUnitTest.this) {
                    ExampleUnitTest.this.notify();
                }
            }
        });
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        assertEquals(4, 2 + 2);
    }
}
