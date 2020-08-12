package cn.wpin.io.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NIO 客户端
 * 师从B站韩顺平
 *
 * @author wangpin
 */
public class NioClient2 {

    public static void main(String[] args) throws Exception {
        //创建一个 SocketChannel 连接NioServer2服务端口
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));

        //设置成非阻塞模型
        socketChannel.configureBlocking(false);

        //如果没有连接上，就做其他的工作（这就是nio神奇之处，对客户端非阻塞）
        if (!socketChannel.isConnected()) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作");
            }
        }
        //需要发送的字符串
        String str = " hello ,wpin";

        //需要发送的字符串写入到buffer中
        //wrap方法就是根据字节数，创建一个对应大小的buffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());

        //buffer写入socketChannel
        socketChannel.write(byteBuffer);

        System.in.read();

    }
}
