package cn.szyrm.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

// 这里 TextWebSocketFrame 表示一个文本帧(frame)
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到的消息" + msg.text());
        //回复消息
        ctx.writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + "" + msg.text()));
    }
    //当web客户端连接后，触发方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
       // id 表示唯一的值, LongText是唯一的shortText，不是唯一
        System.out.println("handlerAdded被调用" + ctx.channel().id().asLongText() );
        System.out.println("handlerAdded被调用" + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved被调用" + ctx.channel().id());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        System.out.println("发生异常:" + cause.getMessage());
        ctx.close();
    }
}
