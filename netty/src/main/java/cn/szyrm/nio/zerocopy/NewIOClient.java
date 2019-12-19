package cn.szyrm.nio.zerocopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 7001));
        String fileName = "protoc-3.6.1-win32.zip";
        //得到一个文件channel
        FileChannel channel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();
        //在linux下一个transferTo 方法就可以完成传输
        //windows下 一次调用transferTo 只能发送8M,就需要分段传输文件，而且区分传输的位置
        //transferTo 底层使用到零拷贝
        long count = channel.transferTo(0, channel.size(), socketChannel);

        System.out.println("发送的字节总数=" + count + "耗时:" + (System.currentTimeMillis() - startTime));
        channel.close();
        socketChannel.close();

    }
}
