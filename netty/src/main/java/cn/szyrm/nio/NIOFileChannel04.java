package cn.szyrm.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 *使用FileChannel和方法transferFrom完成文件的拷贝
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws IOException {
        //创建相关的流
        FileInputStream is = new FileInputStream("8kznds1.jpg");
        FileOutputStream os = new FileOutputStream("cp_8kznds1.jpg");

        //获取各个流对应的fileChannel
        FileChannel rChannel = is.getChannel();
        FileChannel wChannel = os.getChannel();

        //使用transferFrom完成拷贝
        wChannel.transferFrom(rChannel,0,rChannel.size());


        is.close();
        os.close();

    }
}
