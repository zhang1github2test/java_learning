package cn.szyrm.nio;

import java.nio.ByteBuffer;

/**
 * 将一个普通的buffer转换成只读后，就不能往里面放入数据
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for(int i= 0;i<buffer.capacity();i++){
            buffer.put((byte) i);
        }

        //读取
        buffer.flip();
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        System.out.println(readOnlyBuffer.getClass());


        //读取
        while(readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }
        readOnlyBuffer.put((byte) 69);

    }
}
