package cn.szyrm.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用一个Buffer来完成文件读取：
 *  时可用FileChannel完成文件的拷贝
 *  2）、拷贝一个文件1.txt,放入到项目下即可
 *
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel rChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel wChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while (true){
            //将byteBuffer还原
            byteBuffer.clear();
            int read = rChannel.read(byteBuffer);
            System.out.println("read大小="+read );
            if(read == -1){
                break;
            }
            //将buffer中数据写入到wChannel
            byteBuffer.flip();
            wChannel.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
