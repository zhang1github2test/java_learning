package cn.szyrm.nio;

import java.nio.ByteBuffer;

/**
 *
 */
public class NIONByteBufferPutAndGet {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);


        //类型方式放入数据
        buffer.putInt(100);
        buffer.putLong(9);
        buffer.putChar('尚');
        buffer.putShort((short) 4);

        //取出
        buffer.flip();

        System.out.println();
        System.out.println(buffer.getShort());
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getLong());


    }
}
