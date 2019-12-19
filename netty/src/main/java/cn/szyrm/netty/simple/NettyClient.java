package cn.szyrm.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup loopGroup = null;
        try {
            //客户端需要 一个事件循环组
             loopGroup = new NioEventLoopGroup();
            //创建客户端启动对象
            //注意是 Bootstrap 而不是 ServerBootstrap
            Bootstrap bootstrap = new Bootstrap();

            //设置相关参数
            bootstrap.group(loopGroup)//设置线程组
                    .channel(NioSocketChannel.class)//
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("客户端 ok ...");
            ChannelFuture future = bootstrap.connect("127.0.0.1", 6668).sync();
            future.channel().closeFuture().sync();
        }finally {
            loopGroup.shutdownGracefully();
        }

    }
}
