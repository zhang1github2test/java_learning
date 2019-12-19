package cn.szyrm.nio.groupChat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            //获得选择器
            selector = Selector.open();
            //获得ServerSecketChannel
            listenChannel = ServerSocketChannel.open();

            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);


        }catch (Exception e){

        }
    }

    public void listen(){
        try {
            while (true){
                int count = selector.select(2000);
                if(count >0){
                    //遍历selectionkey集合
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                    while (keyIterator.hasNext()){
                        SelectionKey key = keyIterator.next();
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector,SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress() + "上线");

                        }
                        if(key.isReadable()){
                            //通道是可读的
                            readData(key);
                        }
                        //手动删除key
                        keyIterator.remove();

                    }
                }else{
                   // System.out.println("等待....");
                }
            }
        }catch (Exception e){

        }finally {

        }
    }
    //读取客户端消息

    private void readData(SelectionKey key){
        SocketChannel channel = null;
        try {
           channel = (SocketChannel) key.channel();
           //创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            if(count > 0){
                String msg = new String(buffer.array(), "UTF-8");
                System.out.println("from 客户端:" + msg);
                //向其他客户端转发消息
                sendInfoToOtherClients(msg,channel);
            }


        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress() + "离线了");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    //转发消息给其他客户端
    private void sendInfoToOtherClients(String msg,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息");
        //遍历所有注册到selector上的SocketChannel，并排除self
        for(SelectionKey key:selector.keys()){
            //通过key 取出对应的socketChannel
            SelectableChannel channel = key.channel();

            //排除自己
            if( channel instanceof SocketChannel && channel != self){
                    SocketChannel  dest = (SocketChannel) channel;
                    //将msg存储到buffer
                ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes("UTF-8"));
                //将buffer的数据写入到通道
                int write = dest.write(wrap);

            }
        }
    }

    public static void main(String[] args) {
            //创建一个服务器对象
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }

}
