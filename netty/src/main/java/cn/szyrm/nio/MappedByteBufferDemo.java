package cn.szyrm.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *1、MappedByteBuffer 可以让文件直接在内存(堆外内存)中修改，操作系统不需要拷贝一次
 */
public class MappedByteBufferDemo {
    public static void main(String[] args) throws IOException {
        //MappedByteBuffer

        RandomAccessFile accessFile = new RandomAccessFile("1.txt","rw");
        FileChannel channel = accessFile.getChannel();
        /**
         * 参数1：FileChannel.MapMode.READ_WRITE 使用的为读写模式
         * 参数2： 0   可以直接修改的其实位置
         * 参数3： 5    映射到内存的大小
         * 可以直接修改的范围： [0,5)
         * 实际类型：DI
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0, (byte) 'H');
        map.put(3, (byte) '9');
        accessFile.close();

    }
}
