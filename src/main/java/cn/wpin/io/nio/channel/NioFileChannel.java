package cn.wpin.io.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel 从一个文件读取，写入到另外一个文件
 *
 * @author wangpin
 */
public class NioFileChannel {

    public static void main(String[] args) throws Exception {
        String srcPath = "d:\\1.txt";
        String desPath = "d:\\2.txt";
        write(read(srcPath), desPath);
    }

    /**
     * 读取源文件内容
     *
     * @param path 源文件地址
     * @return ByteBuffer
     * @throws Exception
     */
    private static ByteBuffer read(String path) throws Exception {
        File file = new File(path);

        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        fileChannel.read(byteBuffer);

        return byteBuffer;

    }

    /**
     * 写入目标文件
     *
     * @param buffer
     * @param path
     * @throws Exception
     */
    private static void write(ByteBuffer buffer, String path) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(path);

        FileChannel fileChannel = fileOutputStream.getChannel();

        //这一步十分重要
        buffer.flip();

        fileChannel.write(buffer);

        fileOutputStream.close();

    }
}
