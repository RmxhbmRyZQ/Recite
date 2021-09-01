package cn.flandre.review.tools;

import java.io.EOFException;

/**
 * @author RmxhbmRyZQ 2021.8.31
 * 字节数据读取工具
 */
public class BytesReader {
    private final byte[] bytes;
    private int pos;

    public BytesReader(byte[] bytes) {
        this.bytes = bytes;
        pos = 0;
    }

    public void read(byte[] buf, int offset, int len) throws EOFException {
        if (pos + len > bytes.length)
            throw new EOFException();
        System.arraycopy(bytes, pos, buf, offset, len);
        pos += len;
    }

    /**
     * 读取一个整形数据
     *
     * @param big 是否为大端模式，大端高位低地址，小端低位低地址
     */
    public int readInt(boolean big) throws EOFException {
        int result = 0;
        if (pos + 4 > bytes.length)
            throw new EOFException();
        if (big) {
            result += (bytes[pos++] & 0xFF) << 24;
            result += (bytes[pos++] & 0xFF) << 16;
            result += (bytes[pos++] & 0xFF) << 8;
            result += (bytes[pos++] & 0xFF);
        } else {
            result += (bytes[pos++] & 0xFF);
            result += (bytes[pos++] & 0xFF) << 8;
            result += (bytes[pos++] & 0xFF) << 16;
            result += (bytes[pos++] & 0xFF) << 24;
        }
        return result;
    }

    /**
     * 读取一个长整形数据
     *
     * @param big 是否为大端模式，大端高位低地址，小端低位低地址
     */
    public long readLong(boolean big) throws EOFException {
        if (pos + 8 > bytes.length)
            throw new EOFException();
        if (big)
            return ((long) (readInt(big)) << 32) + (readInt(big) & 0xFFFFFFFFL);
        else
            return (readInt(big) & 0xFFFFFFFFL) + ((long) (readInt(big)) << 32);
    }

    public void seek(int pos) {
        this.pos = pos;
    }

    public void skip(int len) throws EOFException {
        if (pos + len > bytes.length)
            throw new EOFException();
        pos += len;
    }

    public int left(){
        return bytes.length - pos;
    }
}
