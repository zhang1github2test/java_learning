package cn.szyrm.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer  buffer = IntBuffer.allocate(5);
        //向buffer存储数据
        for(int i=0;i<2;i++){
            buffer.put(2*i);
        }
       //如何从buffer读取数据
        //将buffer转换，读写切换
        buffer.flip();

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
        buffer.clear();
        //向buffer存储数据
        for(int i=0;i<buffer.capacity();i++){
            buffer.put(2*i);
        }
    }
}
