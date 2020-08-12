package cn.wpin.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * nio 服务端
 * 师从B站韩顺平
 *
 * @author wangpin
 */
public class NioServer2 {

    public static void main(String[] args) throws IOException {

        //定义一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定端口6666
        serverSocketChannel.bind(new InetSocketAddress(6666));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //注册事件为连接Accept事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        for (; ; ) {

            //没有事件发生
            if (selector.select(1000) == 0) {
                System.out.println("服务器无连接");
                continue;
            }
            //如果不等于0，说明有事件发生，直接获取到相关的SelectionKey 集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //使用迭代器，遍历
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            //遍历事件
            while (iterator.hasNext()) {
                //拿到具体的事件
                SelectionKey key = iterator.next();

                //如果是accept事件
                if (key.isAcceptable()) {
                    //该客户端  生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    socketChannel.configureBlocking(false);

                    System.out.println("客户端连接成功，生成了一个socketChannel: "+socketChannel.hashCode());
                    //将 socketChannel注册到Selector,关注事件为OP_READ
                    //同时给socketChannel关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //如果是read事件
                if (key.isReadable()) {
                    //通过key反向获取对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    channel.read(buffer);

                    System.out.println("form 客户端：" + new String(buffer.array()));
                }
                iterator.remove();
            }
        }
    }
}
