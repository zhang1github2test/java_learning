package cn.szyrm.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException {
        //获得一个网络通道
        SocketChannel channel = SocketChannel.open();

        //提供服务端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        //设置为非阻塞
        channel.configureBlocking(false);
        //连接服务器
        if(!channel.connect(inetSocketAddress)){
            while(!channel.finishConnect()){
                System.out.println("因为连接需要时间,客户端不会阻塞，可以做其他的工作");
            }
        }
        String str = "hello,world";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes("UTF-8"));
        //发送数据
        channel.write(buffer);
        System.in.read();
    }
}
