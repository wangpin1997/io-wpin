package cn.wpin.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * 只读buffer 测试
 * 当为只读buffer时，put数据会java.nio.ReadOnlyBufferException
 *
 * @author wangpin
 */
public class ReadOnlyBuffer {

    public static void main(String[] args) {
        ByteBuffer byteBuffer=ByteBuffer.allocate(64);

        //先放入数据
        for (int i = 0; i <64 ; i++) {
            byteBuffer.put((byte)i);
        }
        //转向
        byteBuffer.flip();

        //变为只读
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

        //获取class信息
        System.out.println(readOnlyBuffer.getClass());

        //还有剩余就继续读数据
        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }
        //测试只读buffer添加数据
        readOnlyBuffer.put((byte)100);
    }
}
