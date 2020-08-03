package cn.wpin.io.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;


/**
 * @author wpin
 */
public class HttpServer {

    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {


    }

    private void start() throws InterruptedException {
        ServerBootstrap server = new ServerBootstrap();

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        server.group(eventLoopGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) {
                System.out.println(" socketChannel: " + socketChannel);
                socketChannel.pipeline()
                        .addLast("decoder:", new HttpRequestDecoder())
                        .addLast("encoder: ", new HttpRequestEncoder())
                        .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                        .addLast("handler",new HttpHandler());

            }
        }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
        server.bind(port).sync();
    }

}
