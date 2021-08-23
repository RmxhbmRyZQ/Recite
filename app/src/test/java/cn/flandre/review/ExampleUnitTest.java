package cn.flandre.review;

import cn.flandre.review.recite.ReciteTimeManager;
import cn.flandre.review.tools.StringUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cn.flandre.review.recite.ReciteTimeManager.DEtAIL_TIME_STRING;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws ParseException {
        long l = System.currentTimeMillis();
        System.out.println(l);
        long l1 = ReciteTimeManager.turnSystemTimeToDetailTime(l);
        System.out.println(l1);
        System.out.println(new SimpleDateFormat(DEtAIL_TIME_STRING).parse(String.valueOf(l1)).getTime());
//        assertEquals(4, 2 + 2);
    }
}