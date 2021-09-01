package cn.flandre.review.logic.extradata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 把 zpk 解析成 5 个文件然后写入磁盘
 */
public class DetailDataWriter {
    private final byte[] zpk;
    private final File path;

    public DetailDataWriter(byte[] zpk, String word, File path) {
        this.zpk = zpk;
        this.path = new File(path, word);
    }

    public void write() throws IOException {
        ZPKResolve zpkResolve = new ZPKResolve(zpk);
        Map<String, byte[]> data = zpkResolve.resolve();
        if (!path.exists())
            path.mkdir();
        for (Map.Entry<String, byte[]> entry : data.entrySet()) {
            FileOutputStream outputStream = new FileOutputStream(new File(path, entry.getKey()));
            outputStream.write(entry.getValue());
        }
    }
}
