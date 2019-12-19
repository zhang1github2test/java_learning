package cn.szyrm.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO编程的
 */
public class NIOServer {
    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            Selector selector = Selector.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(6666));
            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while(true){
                if(selector.select(1000) == 0){
                    System.out.println("服务器等待了1秒,无连接");
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while(keyIterator.hasNext()){
                    //获取到selectionKey
                    SelectionKey next = keyIterator.next();
                    //根据key 对应的通道法僧的事件做出相应的处理
                    if(next.isAcceptable()){
                        //为该客户端生成一个SocketChannel
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        System.out.println("客户端连接成功，生成了一个socketChannel["+socketChannel.hashCode() + "]");
                        //将socketChannel 注册到selector，关注事件为OP_READ,同时给SocketChannel 关联一个buffer
                        socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    }else if(next.isReadable()){
                        //通过key反向获取对应的channel
                       SocketChannel channel = (SocketChannel) next.channel();
                       //获取到该channel关联的buffer
                        ByteBuffer buffer = (ByteBuffer) next.attachment();
                        channel.read(buffer);
                        System.out.println("from 客户端:" + new String(buffer.array()));
                    }
                    //手动从集合中将key移除
                    keyIterator.remove();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
