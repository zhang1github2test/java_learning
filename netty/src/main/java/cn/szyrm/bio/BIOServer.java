package cn.szyrm.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        //线程池机制
        //思路
        //1.创建一个线程池
        //2、如果有客户端，就创建一个线程，与之通讯
        ExecutorService pool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动");
        while(true){
            //监听 ，等待客户端连接  阻塞
           final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            pool.execute(()->{
                handler(socket);
            });
        }
    }

    public static void handler(Socket socket){
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
           while (true){
               //阻塞的地方
               int read = inputStream.read(bytes);
               if(read != -1){
                   System.out.println(new String(bytes,0,read));
               }else{
                   break;
               }
           }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
