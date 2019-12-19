package cn.szyrm.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.util.Date;

public class MyNettyHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        System.out.println("ctx 类型="+ctx.getClass());

        System.out.println("pipeline hashcode" + ctx.pipeline().hashCode() + " TestHttpServerHandler hash=" + this.hashCode());

        System.out.println("msg 类型=" + msg.getClass());
        System.out.println("客户端地址" + ctx.channel().remoteAddress());


        if(msg instanceof HttpRequest){
            //获取到
            HttpRequest httpRequest = (HttpRequest) msg;
            //获取uri, 过滤指定的资源
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico, 不做响应");
                DefaultFullHttpResponse re = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                re.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                re.headers().set(HttpHeaderNames.CONTENT_LENGTH,0);
                ctx.writeAndFlush(re);
                return;
            }
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello,客户端", CharsetUtil.UTF_8);
            //构造一个http的相应，即 httpresponse
            DefaultFullHttpResponse re = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,byteBuf);
            re.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
            re.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            re.headers().set(HttpHeaderNames.DATE,new Date());

            //将构建好 response返回
            ctx.writeAndFlush(re);
        }
    }
}
