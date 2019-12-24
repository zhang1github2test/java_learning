package cn.szyrm.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 说明：
 *  1、我们自定义一个Handler，需要继承netty规定的HandlerAdapter
 *   此时定义好的一个handler才能称为一个handler
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student msg) throws Exception {
        System.out.println("接收到消息:" + msg.getId() + "  " + msg.getName());
    }

    //读取实际数据（这里我们可以读取客端发送的消息）

    /**
     *
     * @param ctx  上下文对象,含有管道pipeline 。通道channel，地址
     * @param msg ：客户端发送的数据 默认Object
     * @throws Exception
     */
  /*  @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);
        //将msg转成一个ByteBuf ,是netty的ByteBuf 而不是NIO的ByteBuffer
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送消息是:"  +byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址:" + ctx.channel().remoteAddress());
    }*/

    /**
     * 数据读取完毕后
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入缓存
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
