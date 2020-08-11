package cn.wpin.io.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel 读取文件中的数据，输出到控制台
 *
 * @author wangpin
 */
public class NioFileChannelRead {

    public static void main(String[] args) throws Exception {
        File file = new File("d:\\2.txt");

        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        fileChannel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));
    }
}
