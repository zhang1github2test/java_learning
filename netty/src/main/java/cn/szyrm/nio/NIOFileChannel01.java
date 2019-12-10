package cn.szyrm.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用fileChannel 写入文件的案例
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = "hello world";
        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d://file01.txt");
        //通过文件输出流获取FileChannel
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());
        //对byteBuffer 进行flip,好让数据能够进行都
        byteBuffer.flip();
        //将byteBuffer的数据写入到fileChannel中
        channel.write(byteBuffer);
        //关闭流
        fileOutputStream.close();



    }
}
