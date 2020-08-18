package cn.wpin.io.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

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
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx=" + ctx);
        //将msg转成一个  ByteBuf,ByteBuf是netty提供的，不是NIO的ByteBuffer
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送的消息是：" + byteBuf.toString(Charset.defaultCharset()));
        System.out.println("客户端地址为：" + ctx.channel().remoteAddress());
        super.channelRead(ctx, msg);
    }

    /**
     * 数据读取完毕
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //writeAndFlush s write 和flush
        //将数据写入缓存，并刷新，编码为UTF-8
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端·", Charset.defaultCharset()));
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
    }
}
