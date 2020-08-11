package cn.wpin.io.nio.buffer;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 直接操作内存的buffer
 *
 * @author wangpin
 * @see java.nio.DirectByteBuffer
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\1.txt", "rw");

        FileChannel channel = randomAccessFile.getChannel();

        //参数1：FileChannel.MapMode.READ_WRITE 使用读写模式
        //参数2：0 可以直接修改的起始位置
        //参数3：映射到内存中字节的大小，5即代表1.txt中 只有5个字节能映射
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(0, (byte) 'p');
        //如何index==size，会下标越界异常
        map.put(3, (byte) '9');

        randomAccessFile.close();

        System.out.println("修改成功--");

    }
}
