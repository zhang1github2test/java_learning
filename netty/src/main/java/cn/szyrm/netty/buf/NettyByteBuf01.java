package cn.szyrm.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * netty buf使用示例
 * 1、创建对象，该对象包含一个数组arr,
 * 2,在netty的buffer中，不需要使用flip进行反转
 *   底层原因：同时维护了一个readerIndex、writerIndex  、capacity 来进行标识可读、可写区间
 */
public class NettyByteBuf01 {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);
        for(int i=0;i<10;i++){
            buffer.writeByte(i);
        }
        System.out.println("capacity=" + buffer.capacity());
        //输出
        for(int i=0;i<buffer.capacity();i++){
            System.out.println(buffer.getByte(i));
        }
    }
}
