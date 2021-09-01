package cn.flandre.review.logic.extradata;

import cn.flandre.review.tools.BytesReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 解析 zpk 文件成 5 个文件
 */
public class ZPKResolve {
    private final byte[] bytes;

    public ZPKResolve(byte[] bytes) {
        this.bytes = bytes;
    }

    public Map<String, byte[]> resolve() throws IOException {
        Map<String, byte[]> map = new HashMap<>();

        BytesReader reader = new BytesReader(bytes);

        reader.seek(0xc);
        int fileNumber = reader.readInt(false);

        long fileInfo = reader.readLong(false);
        long fileNameIndex = reader.readLong(false);
        reader.seek((int) fileNameIndex);

        byte[] names = new byte[reader.left()];
        reader.read(names, 0, reader.left());
        String[] files = new String(names, StandardCharsets.UTF_8).split("\n");

        for (int i = 0; i < fileNumber; i++) {
            reader.seek((int) (fileInfo + i * 0x30));
            long fileStart = reader.readLong(false);
            reader.skip(8);
            int fileLength = reader.readInt(false);
            byte[] buf = new byte[fileLength];
            reader.seek((int) fileStart);
            reader.read(buf, 0, fileLength);
            map.put(files[i], buf);
        }

        return map;
    }
}
