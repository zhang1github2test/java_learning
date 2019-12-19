package cn.szyrm.nio.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    private static final String HOST = "127.0.0.1";
    private static  final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel ;
    private String userName;

    public GroupChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        userName = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(userName + "  is ok....");

    }

    //向服务器发送消息
    public void sendInfo(String info){
       info =   userName + " 说:" + info ;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //读取从服务端回复的消息
    public void readInfo(){
        try {
            int readChannels = selector.select();
            if(readChannels > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        //得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();

                        ByteBuffer b = ByteBuffer.allocate(1024);
                        sc.read(b);
                        String msg = new String(b.array());

                        System.out.println(msg.trim());
                    }
                    //删除当前的key
                    iterator.remove();
                }

            }else{
             //   System.out.println("没有可用的通道");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatClient groupChatClient = new GroupChatClient();
        //启动一个线程，每隔3秒，读取从服务器发送过来的数据
        new Thread(()->{
            while (true){
                groupChatClient.readInfo();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //发送数据给服务端
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String msg = scanner.nextLine();

            groupChatClient.sendInfo(msg);
        }

    }
}
