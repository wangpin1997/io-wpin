package cn.wpin.io.nio.zerocopy;

import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * 零拷贝：是指文件拷贝过程中不经过CPU拷贝次数为0
 * <p>
 * NIO提出一种零拷贝概念，有两种方式，mmap ,sendfile
 * 与传统拷贝区别                  一次          两次     上下文切换 4次，四次拷贝
 * 传统拷贝：磁盘--->kernel buffer --->user buffer--->socket buffer--->目标磁盘
 * linux 2.1  mmap拷贝 一次CPU拷贝                        上下文切换 4次，三次拷贝
 * ：磁盘--->kernel buffer --->socket buffer--->目标磁盘  减少了一次上下文切换
 * linux 2.4  sendfile拷贝（真正的零拷贝），基本没有CPU拷贝  上下文切换 3次 两次次拷贝
 * <p>
 * 磁盘--->kernel buffer --->socket buffer（数据非常非常少，如length，offset忽略不计）--->目标磁盘  减少了一次上下文切换
 *
 * @author wangpin
 */
public class ZeroCopy {
    //mmap跟sendfile区别
    //1.mmap适合小数据量读写，sendfile适合大文件传输
    //2.mmap需要4次上下文切换，3次数据拷贝；sendfile需要3次上下文切换，最少两次数据拷贝。
    //3.sendfile可以利用DMA（direct memory access直接内存拷贝，不经过CPU）方式，减少CPU拷贝，mmap则不能，（必须从内核拷贝到缓冲区）


    private void zeroCopy() throws Exception {

        //连接服务器端
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));
        FileOutputStream outputStream = new FileOutputStream("d:\\dingtalk-sdk-java.zip");

        FileChannel fileChannel = outputStream.getChannel();

        long st = System.currentTimeMillis();
        //零拷贝真正的方法。注意：windows每次限制8m,需要分段传输，即position值
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送的字节数为=" + transferCount + " 耗时:" + (System.currentTimeMillis() - st));
        fileChannel.close();
    }
}
