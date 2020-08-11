package cn.wpin.io.nio.channel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 测试nio FileChannel
 * 读数据写入本地txt文件
 * @author wangpin
 */
public class NioFileChannelWrite {

    public static void main(String[] args) throws Exception {
        //需要写的内容
        String message="hello word,wpin";
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\2.txt");
        //获取channel
        FileChannel fileChannel=fileOutputStream.getChannel();
        //分配buffer
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        //写入buffer
        byteBuffer.put(message.getBytes());

        //读切换成写，不然啥都没有
        byteBuffer.flip();

        fileChannel.write(byteBuffer);

        fileOutputStream.close();
    }
}
