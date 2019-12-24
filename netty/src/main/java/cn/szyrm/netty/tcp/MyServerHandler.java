package cn.szyrm.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        //
        byte[] bytes = new byte[msg.readableBytes()];
        ByteBuf byteBuf = msg.readBytes(bytes);

        //将buffer转成字符串
        String message = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("服务器端接收到数据" + message);
        System.out.println("服务器接收到消息量" + (++this.count));
       ctx.writeAndFlush(Unpooled.copiedBuffer(UUID.randomUUID().toString(),CharsetUtil.UTF_8)) ;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      ctx.close();
    }
}
