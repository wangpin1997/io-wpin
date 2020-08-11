package cn.wpin.io.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * nio FileChannel 文件拷贝
 *
 * @author wangpin
 */
public class NioFileChannelCopy {

    public static void main(String[] args) throws Exception {
        FileOutputStream outputStream = new FileOutputStream("d:\\a2.png");
        FileInputStream inputStream = new FileInputStream("d:\\arthas.jpg");

        FileChannel srcChannel = inputStream.getChannel();

        FileChannel desChannel = outputStream.getChannel();

        desChannel.transferFrom(srcChannel, 0, srcChannel.size());

        desChannel.close();
        srcChannel.close();
        outputStream.close();
        inputStream.close();

    }
}
