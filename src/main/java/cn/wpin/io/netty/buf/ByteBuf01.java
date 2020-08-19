package cn.wpin.io.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * netty提供的ByteBuf类,Unpooled类
 *
 * @author wangpin
 */
public class ByteBuf01 {

    public static void main(String[] args) {

        //创建一个  ByteBuf,对象中包含一个数组arr,byte[10]
        //在netty的buffer中，不需要flip反转读写
        //底层维护了writerIndex和readerIndex
        ByteBuf byteBuf= Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }
        System.out.println("写入完成");
        for (int i = 0; i <byteBuf.capacity() ; i++) {
            byteBuf.readBytes(i);
        }

    }
}
