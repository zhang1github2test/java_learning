package cn.szyrm.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyChannelInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(16668).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }




    }
}
