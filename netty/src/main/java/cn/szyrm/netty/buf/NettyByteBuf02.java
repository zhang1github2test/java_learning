package cn.szyrm.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        //创建byteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world", CharsetUtil.UTF_8);
        //使用相关的方法
        if(byteBuf.hasArray()){
            byte[] array = byteBuf.array();
            System.out.println(new String(array,CharsetUtil.UTF_8));
            System.out.println("byteBuf=" + byteBuf);
            System.out.println(byteBuf.arrayOffset());//
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity()); //36
            System.out.println(byteBuf.readByte());

            int len = byteBuf.readableBytes();
            System.out.println("可读长度=" + len);

            for(int i= 0;i<len;i++){
                System.out.print((char) byteBuf.readByte());
            }
        }

    }
}
