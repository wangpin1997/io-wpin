package cn.wpin.io.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * @author wpin
 */
public class HttpServer {

    private final int port;

    private HttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new HttpServer(8888).start();

    }

    private void start() throws InterruptedException {

        //创建一个bossGroup,用于监听accept事件
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //创建一个workerGroup事件，用来处理线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();


        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                //对应bossGroup
//                .handler(null)
                //对应workerGroup
                .childHandler(new HttpServerInitializer())
                //对应bossGroup
                .option(ChannelOption.SO_BACKLOG, 128)
                //对应workerGroup
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
        server.bind(port).sync();

        //常用的channel类型
        //NioServerSocketChannel 异步的服务端TCP连接
        //NioSocketChannel 异步的客户端TCP连接
        //NioDatagramChannel 异步UDP连接
        //NioSctpChannel 异步的sctp客户端连接
        //NioSctpServerChannel 异步的sctp服务端连接
    }

}
