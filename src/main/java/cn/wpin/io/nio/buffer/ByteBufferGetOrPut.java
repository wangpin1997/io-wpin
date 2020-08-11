package cn.wpin.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * byteBuffer 读写   按数据类型顺序读写，不然会报BufferUnderflowException异常 获取数据
 *
 * @author wangpin
 */
public class ByteBufferGetOrPut {

    public static void main(String[] args) {

        ByteBuffer byteBuffer=ByteBuffer.allocate(64);

        //先写
        byteBuffer.putInt(1);
        byteBuffer.putLong(10L);
        byteBuffer.putChar('2');
        byteBuffer.putShort((short) 1);

        //转向
        byteBuffer.flip();

        //后读，按put的顺序取数据没有问题
        //把最后一个改成大类型long，就会抛出.BufferUnderflowException异常
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());
    }
}
