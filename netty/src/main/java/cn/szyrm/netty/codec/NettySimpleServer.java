package cn.szyrm.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettySimpleServer {
    public static void main(String[] args) {
        /**
         *  创建bossGroup 和wokerGroup
         *  1、创建两个线程组boossgroup 和workGroup
         *  2、boosgroup 只是处理连接请求，真正的和客户端业务处理，会交给wokerGroup完成
         *  3、两个都是无限循环
         */
      EventLoopGroup boosGroup = new NioEventLoopGroup();
      EventLoopGroup workGroup = new NioEventLoopGroup();

      //创建服务端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
        //使用链式编程来进行设置
        bootstrap.group(boosGroup,workGroup)//设置两个线程组
                .channel(NioServerSocketChannel.class) //使用NioSocketChannel作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128)//设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()))
                                .addLast(new NettyServerHandler());


                    }
                }) ; //给workGroup 的EventLoop设置处理器

            ChannelFuture future = bootstrap.bind(6668).sync();
            future.addListener(future1 -> {
                if(future.isSuccess()){
                    System.out.println("监听端口 6668 成功");
                }else{
                    System.out.println("监听端口 6668 失败");
                }
            });

            //对关闭通道进行监听
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
