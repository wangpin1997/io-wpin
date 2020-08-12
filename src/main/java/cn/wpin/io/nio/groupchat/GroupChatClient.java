package cn.wpin.io.nio.groupchat;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * nio 群聊天室 客户端
 *
 * @author wangpin
 */
public class GroupChatClient {

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 6667;

    private Selector selector;

    private SocketChannel socketChannel;

    private String username;


    /**
     * 构造器，完成初始化
     *
     * @throws Exception
     */
    private GroupChatClient() throws Exception {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        //设置成非阻塞
        socketChannel.configureBlocking(false);
        //channel注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username，以ip为名称
        username = socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(username + " is ok ...");

    }


    private void sendInfo(String info) throws IOException {
        info = username + " 说：" + info;

        socketChannel.write(ByteBuffer.wrap(info.getBytes()));
    }

    private void readInfo() {
        try {
            int readChannels = selector.select();

            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    if (next.isReadable()) {
                        //得到相关的通道
                        SocketChannel channel = (SocketChannel) next.channel();
                        //得到一个buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //读取
                        channel.read(buffer);
                        //把读到的缓冲区的数据转成字符串
                        System.out.println(new String(buffer.array()));

                    }
                    iterator.remove();
                }
            } else {
                System.out.println("没有可用通道");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        GroupChatClient chatClient = new GroupChatClient();
        ExecutorService executorService = new ThreadPoolExecutor(5, 20, 10000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20), new DefaultThreadFactory("wpin-pool"));

        executorService.execute(() -> {
            while (true) {
                chatClient.readInfo();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}
