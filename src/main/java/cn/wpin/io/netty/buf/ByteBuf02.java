package cn.wpin.io.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * netty创建byteBuf另外一种方式
 *
 * @author wangpin
 */
public class ByteBuf02 {

    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,wold", Charset.defaultCharset());

        if (byteBuf.hasArray()) {

            byte[] array = byteBuf.array();

            System.out.println(new String(array, Charset.defaultCharset()));

            System.out.println("byteBuf=" + byteBuf);

            //0
            System.out.println(byteBuf.arrayOffset());

            //0
            System.out.println(byteBuf.readerIndex());

            //10
            System.out.println(byteBuf.writerIndex());

            //30
            System.out.println(byteBuf.capacity());

            //104
            System.out.println(byteBuf.getByte(0));


            int len = byteBuf.readableBytes();

            System.out.println(len);

            for (int i = 0; i < len; i++) {
                System.out.println(byteBuf.getByte(i));
            }

            //开始读，后面的四个字节
            System.out.println(byteBuf.getCharSequence(0, 4, Charset.defaultCharset()));

            //4开始读，后面的6个字节
            System.out.println(byteBuf.getCharSequence(4, 6, Charset.defaultCharset()));
        }
    }
}
