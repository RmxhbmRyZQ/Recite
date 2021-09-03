package cn.flandre.review.logic.enumerate;

/**
 * @author RmxhbmRyZQ 2021.9.2
 */
public enum ReviewMode {
    LISTEN_MODE(0), ENGLISH_MODE(1), CHINESE_MODE(2), ENGLISH_CHINESE_MODE(3);
    public static String[] ITEMS = new String[]{"听力模式", "英语模式", "中文模式", "英中模式"};

    private final int index;

    ReviewMode(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static ReviewMode parseInt(int i) {
        switch (i) {
            case 0:
                return LISTEN_MODE;
            case 1:
                return ENGLISH_MODE;
            case 2:
                return CHINESE_MODE;
            case 3:
                return ENGLISH_CHINESE_MODE;
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
