package cn.flandre.review;

import cn.flandre.review.tools.StringUtils;
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
        System.out.println("true".split(","));
//        assertEquals(4, 2 + 2);
    }
}
