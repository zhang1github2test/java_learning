package cn.szyrm.netty.websocket;

import cn.szyrm.netty.groupChat.GroupChatServer;
import cn.szyrm.netty.groupChat.GroupChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
    private int port;//监听端口

    public WebSocketServer(int port) {
        this.port = port;
    }

    public void run(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            //因为是基于http协议的，使用htttp编码和解码器
                            pipeline.addLast(new HttpServerCodec())
                                    //以快的方式写，添加 ChunkedWriteHandler处理器
                                    .addLast(new ChunkedWriteHandler())
                                    /**
                                     * 1、http数据在传输过程中是分段的，HttpObjectAggregator 就是可以将多个段聚合
                                     * 2、这就是为什么，当浏览器发送大量数据时，就会发出多次请求
                                     */
                                    .addLast(new HttpObjectAggregator(8192))
                                    /**
                                     * 1、对于webSocket，它的数据是以帧的方式传递的
                                     * 2、可以看到webSocketFrame下面有六个子类
                                     * 3、浏览器请求时:ws://localhost:7000/hello,表示请求的uri
                                     * 4、WebSocketServerProtocolHandler的核心功能时将http协议升级为ws协议，保持长连接
                                     */
                                    .addLast(new WebSocketServerProtocolHandler("/hello"))
                                    //自定义的handler，处理业务逻辑
                                    .addLast(new WebSocketFrameHandler());

                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new WebSocketServer(7000).run();
    }
}
