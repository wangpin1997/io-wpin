package cn.wpin.io.nio.buffer;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 一个buffer不够用时，分配到buffer数组
 *
 * @author wangpin
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];

        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8;

        while (true) {

            int byteRead = 0;

            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println(" byteRead= " + byteRead);
                Arrays.stream(byteBuffers).map(buffer -> " position=" + buffer.position() + " ,limit=" + buffer.limit()).forEach(System.out::println);

            }
            Arrays.asList(byteBuffers).forEach(Buffer::flip);

            long byteWrite = 0;

            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }
            Arrays.asList(byteBuffers).forEach(Buffer::clear);

            System.out.println(" byteRead=" + byteRead + "  byteWrite=" + byteWrite + " messageLength=" + messageLength);
        }
    }
}
