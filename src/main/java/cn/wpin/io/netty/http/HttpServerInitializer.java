package cn.wpin.io.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 自定义的HTTP服务端初始化工具
 *
 * @author wangpin
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel channel) {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = channel.pipeline();

        //加入netty提供的HttpServerCodec codec=>[coder ,decoder]
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //增加一个自定义的HttpHandler
        pipeline.addLast("httpServerHandler", new HttpServerHandler());
        pipeline.addLast("httpServerHandler2", new HttpServerHandler2());


    }
}
