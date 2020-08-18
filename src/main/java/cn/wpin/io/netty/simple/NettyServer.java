package cn.wpin.io.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty写法的 http服务端
 *
 * @author wangpin
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {
        //创建一个bossGroup,用于监听accept事件
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //创建一个workerGroup事件，用来处理线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器的启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            //使用链式编程进行设置
            serverBootstrap
                    //设置两个线程组
                    .group(bossGroup, workerGroup)
                    //使用NioServerSocketChannel的通道来实现
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到的连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //创建一个通道测试对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    //自定义的handler
                                    .addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("------服务器 is ready ----");

            //绑定一个端口并且同步，生成一个ChannelFuture对象
            //启动服务器，并绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();

            //对关闭通道事件进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //优雅的关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
