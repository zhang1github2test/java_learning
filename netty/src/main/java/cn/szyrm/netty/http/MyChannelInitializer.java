package cn.szyrm.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //添加 http编解码器
        channel.pipeline().addLast(new HttpServerCodec())
                .addLast(new MyNettyHandler());
    }
}
