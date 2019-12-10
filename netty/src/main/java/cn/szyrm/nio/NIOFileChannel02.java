package cn.szyrm.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用fileChannel读取文件的案例
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {
        File file = new File("d:\\file01.txt");
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            FileChannel channel = fileInputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
            channel.read(byteBuffer);
            System.out.println(new String(byteBuffer.array(),"UTF-8"));
        }
    }
}
