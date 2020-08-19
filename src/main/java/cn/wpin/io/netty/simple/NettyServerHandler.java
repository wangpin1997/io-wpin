package cn.wpin.io.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * handler 业务处理器 netty
 * <p>
 * 1.、我们自定义一个Handler,需要继承netty，规定好的某个HandlerAdapter(规范)
 * 2、这时我们自定义一个handler，才能成为handler
 *
 * @author wangpin
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 读取数据实际（这里我们可以读取客户端发送的消息）
     *
     * @param ctx 上下文对象，含有管道pipeline，通道channel，地址
     * @param msg 客户端发送的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //----------------------------------
        //最初版
        System.out.println("server ctx=" + ctx);
        //将msg转成一个  ByteBuf,ByteBuf是netty提供的，不是NIO的ByteBuffer
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送的消息是：" + byteBuf.toString(Charset.defaultCharset()));
        System.out.println("客户端地址为：" + ctx.channel().remoteAddress());

        //非阻塞
        ctx.channel().eventLoop().execute(() -> {
            System.out.println("server ctx=" + ctx);
            //将msg转成一个  ByteBuf,ByteBuf是netty提供的，不是NIO的ByteBuffer
            ByteBuf byteBuf1 = (ByteBuf) msg;
            System.out.println("客户端发送的消息是：" + byteBuf1.toString(Charset.defaultCharset()));
            System.out.println("客户端地址为：" + ctx.channel().remoteAddress());
        });

        //定义普通任务
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(5 * 1000);
                //writeAndFlush s write 和flush
                //将数据写入缓存，并刷新，编码为UTF-8
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端2·", Charset.defaultCharset()));
            } catch (InterruptedException e) {
                System.out.println("发生异常：" + e.getMessage());
            }
        });

        //用户自定义定时任务，该任务是提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(5 * 1000);
                //writeAndFlush s write 和flush
                //将数据写入缓存，并刷新，编码为UTF-8
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端3·", Charset.defaultCharset()));
            } catch (InterruptedException e) {
                System.out.println("发生异常：" + e.getMessage());
            }
        }, 5, TimeUnit.SECONDS);

    }

    /**
     * 数据读取完毕
     *
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

        //最简单版本
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端2·", Charset.defaultCharset()));




    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
    }
}
