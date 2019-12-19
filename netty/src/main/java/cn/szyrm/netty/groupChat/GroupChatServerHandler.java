package cn.szyrm.netty.groupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channel组,管理所有的channel
    //GlobalEventExecutor.INSTANCE
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //handlerAdded 表示连接建立,一旦连接，第一个被执行
    //将当前channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户端加入聊天的信息推送给其他的在线客户端
        //该方法将channelGroup中所有的channel遍历,并发送消息，我们不需要自己遍历
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入聊天室");
        channelGroup.add(channel);


    }
    //表示channel处于活动状态 ，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了");
    }
    //表示channel处于非活跃状态,提示xx离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
       // super.channelInactive(ctx);
        System.out.println(ctx.channel().remoteAddress() + "下线了");
    }

    //断开连接，将xxx客户离开信息推送给当前的在线客户
    //触发该方法的时候，该channel会自动从channelGroup被移除
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //super.handlerRemoved(ctx);
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        //这时候遍历channelGroup，根据不同的情况，会送不同的消息
        channelGroup.forEach(ch->{
            if(channel != ch){
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + "发送了消息" + msg);
            }else{
                ch.writeAndFlush("【自己发送的消息】" + msg);
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
