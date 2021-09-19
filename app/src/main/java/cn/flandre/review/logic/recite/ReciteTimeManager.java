package cn.flandre.review.logic.recite;

import cn.flandre.review.data.bean.GroupWord;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 否则管理背诵的时间
 */
public class ReciteTimeManager {
    public static final long DEFAULT_RECITE_TIME = 210012300000L;
    public static final int HOUR = 60 * 60 * 1000;
    public static final int DAY = HOUR * 24;

    public static final String DEtAIL_TIME_STRING = "yyyyMMddHHmm";
    public static final String NORMAL_TIME_STRING = "yyyyMMdd";

    /**
     * 转换时间戳到详细存储时间
     */
    public static long turnSystemTimeToDetailTime(long time) {
        return Long.parseLong(new SimpleDateFormat(DEtAIL_TIME_STRING).format(time));
    }

    /**
     * 转换时间戳到正常存储时间
     */
    public static long turnSystemTimeToNormalTime(long time) {
        return Long.parseLong(new SimpleDateFormat(NORMAL_TIME_STRING).format(time)) * 10000;
    }

    public static long turnDetailTimeToSystemTime(long time) throws ParseException {
        if (time == 0) return System.currentTimeMillis();
        return new SimpleDateFormat(DEtAIL_TIME_STRING).parse(String.valueOf(time)).getTime();
    }

    public static long turnNormalTimeToSystemTime(long time) throws ParseException {
        if (time == 0) return System.currentTimeMillis();
        return new SimpleDateFormat(NORMAL_TIME_STRING).parse(String.valueOf(time / 10000)).getTime();
    }

    /**
     * 计算下一个背诵的时间
     */
    public static long calculationNextTime(GroupWord groupWord, long reciteTime) throws ParseException {
        long last = turnNormalTimeToSystemTime(groupWord.getLastReciteTime());
        long next = turnNormalTimeToSystemTime(groupWord.getNextReciteTime());
        int day = (int) ((next - last) / DAY);
        if (day <= 0) day = 1;
        int wrongCount = 0;
        for (boolean b : groupWord.getWrong()) {
            if (b) wrongCount++;
        }
        if (groupWord.getWrong().length == 1)
            if (groupWord.getWrong()[0])
                day--;
            else if (!groupWord.getSuspect()[0])
                day++;
        else
            switch (wrongCount) {
                case 0:
                case 1:  // 错1,0个优秀
                    ++day;
                    break;
                case 2:  // 错两个合格
                    break;
                default:  // 错两个以上失败
                    --day;
                    break;
            }
        // 如果延迟背诵，惩罚天数加一
        if (reciteTime > next + 2 * DAY) {
            --day;
        }
        if (day <= 0) day = 1;
        return turnSystemTimeToNormalTime(reciteTime + (long) day * DAY);
    }
}
