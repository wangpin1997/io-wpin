package cn.wpin.io.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 基于nio群聊  ,服务端
 *
 * @author wangpin
 */
public class GroupChatServer {

    private Selector selector;

    private ServerSocketChannel listenChannel;

    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            //获取选择器
            selector = Selector.open();

            //获取ServerSocketChannel
            listenChannel = ServerSocketChannel.open();

            //绑定端口6667
            listenChannel.bind(new InetSocketAddress(PORT));

            //设置成非阻塞
            listenChannel.configureBlocking(false);

            //注册监听事件为 OP_ACCEPT
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听
     */
    private void listen() {
        try {
            while (true) {
                //监听
                int count = selector.select();
                if (count > 0) {
                    //拿到所有的SelectionKey，并遍历
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();

                            //设置成非阻塞
                            sc.configureBlocking(false);

                            //监听OP_READ
                            sc.register(selector, SelectionKey.OP_READ);

                            System.out.println(sc.getRemoteAddress() + " 上线");
                        }

                        if (key.isReadable()) {
                            //通道发送read事件，即通道是可读的状态
                            readData(key);
                        }
                        iterator.remove();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("处理异常。。。");
        }
    }

    /**
     * 读取数据
     *
     * @param key key
     */
    private void readData(SelectionKey key) {
        SocketChannel socketChannel = null;
        try {
            //得到channel
            socketChannel = (SocketChannel) key.channel();

            //创建一个buffer来存放数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            //将通道中的数据读入buffer
            int count = socketChannel.read(byteBuffer);

            if (count > 0) {
                //输出buffer中的内容
                String msg = new String(byteBuffer.array());

                System.out.println("接收到客户端消息：" + msg);

                //向其他客户端转发消息
                sendInfoToOtherClients(msg, socketChannel);
            }
        } catch (Exception e) {
            try {
                assert socketChannel != null;
                System.out.println(socketChannel.getRemoteAddress() + "  离线了。。。");
                key.cancel();
                socketChannel.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void sendInfoToOtherClients(String msg, SocketChannel self) throws Exception {

        System.out.println("服务器消息转发中。。。。。。");

        //遍历selector上所有的SelectionKey
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();

            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                //转型
                SocketChannel socketChannel = (SocketChannel) targetChannel;

                //将msg写入buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());

                //将buffer的数据写入通道
                socketChannel.write(buffer);

            }
        }

    }


    public static void main(String[] args) {
        new GroupChatServer().listen();
    }
}
