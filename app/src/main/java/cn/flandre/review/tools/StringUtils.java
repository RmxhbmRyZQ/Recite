package cn.flandre.review.tools;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class StringUtils {
    public static String join(boolean[] array, String sep) {
        if (array.length == 0) return "";
        StringBuilder builder = new StringBuilder();
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(sep).append(array[i]);
        }
        return builder.toString();
    }

    public static String join(int[] array, String sep) {
        if (array.length == 0) return "";
        StringBuilder builder = new StringBuilder();
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(sep).append(array[i]);
        }
        return builder.toString();
    }

    public static String join(String[] array, String sep) {
        if (array.length == 0) return "";
        StringBuilder builder = new StringBuilder();
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(sep).append(array[i]);
        }
        return builder.toString();
    }
}
