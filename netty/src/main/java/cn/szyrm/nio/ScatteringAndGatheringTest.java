package cn.szyrm.nio;

import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket，并启动
        channel.socket().bind(inetSocketAddress);

        //创建buffer数组

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] =ByteBuffer.allocate(3);

        int messageLength = 8;

        //等待客户端链接
        SocketChannel accept = channel.accept();

        //循环读取数据
        while (true){
            int byteRead = 0;
            while (byteRead < messageLength ){
                long read = accept.read(byteBuffers);
                byteRead += read;
                System.out.println("byteRead=" + byteRead);
                //使用流打印，看看当前的这个buffer的position
                Arrays.asList(byteBuffers).stream().map(buffer ->
                     "position=" + buffer.position() + ",limit=" + buffer.limit()
                ).forEach(System.out::println);
            }
            //将所有的buffer进行flip
            Arrays.asList(byteBuffers).forEach(buffer->buffer.flip());
            long byteWrite = 0;
            while(byteWrite <messageLength){
                long write = accept.write(byteBuffers);

                byteWrite += write;
            }

            //将所有的buffer进行clear操作
            Arrays.asList(byteBuffers).forEach(buffer ->buffer.clear());
            System.out.println("byteRead=" + byteRead + " byteWrite=" + byteWrite + " messageLength=" + messageLength );
        }


    }
}
