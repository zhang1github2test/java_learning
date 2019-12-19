package cn.szyrm.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 服务端
 */
public class NewIOServer {
    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(address);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while (true){
            SocketChannel channel = serverSocketChannel.accept();
             int readcount = 0;
             while(-1 != readcount){
                 try{
                     readcount = channel.read(byteBuffer);
                 }catch (Exception e){
                    break;
                 }
                byteBuffer.rewind();
             }
        }

    }
}
