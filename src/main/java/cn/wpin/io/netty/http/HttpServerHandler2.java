package cn.wpin.io.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * 模拟浏览器接受客户端信息，并回复
 *
 * @author wangpin
 */
public class HttpServerHandler2 extends SimpleChannelInboundHandler<HttpObject> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpRequest) {
            System.out.println("msg 类型：" + msg.getClass());
            System.out.println("客户端地址：" + ctx.channel().remoteAddress());

            HttpRequest request = (HttpRequest) msg;
            try {
                URI uri = new URI(request.uri());
                String ico = "/favicon.ico";
                if (ico.equals(uri.getPath())) {
                    System.out.println("特殊资源，不做响应");
                    return;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        //回复消息给浏览器 HTTP协议
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,我是服务器", Charset.defaultCharset());

        //构建一个HTTP响应，即HttpResponse
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, byteBuf);

        fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN + ";charset=utf-8")
                .set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

        //将构建好的response返回
        ctx.writeAndFlush(fullHttpResponse);

    }
}
