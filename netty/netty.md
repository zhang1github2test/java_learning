## netty学习

java中的IO模式主要有三种:BIO、NIO、AIO

#### 一、BIO

bio是一种同步阻塞的IO模式，其中每一个客户端对应服务端的一个线程。适合客户量小且固定的架构，编程比较简单。

#### 二、NIO 

NIO是一种同步非阻塞的IO模式。服务端采用一个线程来管理一个selector,每个selector来管理多个连接。selector就是一个多路复用器

* channel

  NIO的channel类似于流，但有些区别如下：

  * channel可以同时进行读写，而流只能读或只能写
  * channel可以实现异步读取数据
  * 通道可以从缓冲读取数据，也可以写数据到缓冲

  1、FileChannel用于文件的数据读写，DatagramChannel用于UDP数据的去写，ServerSocketChannel和SocketChannel用于TCP的数据读写。

  

  2、可以将一个普通的buffer转成一个只读buffer

  3、NIO还提供了MapperByteBuffer,可以让文件直接在内存中进行修改，而如何同步到文件由NIO来完成

  4、NIO还支持通过多个buffer完成读写操作，即SCattering和Gatering

  

* buffer

  Buffer和Channel的注意事项和细节

  * ByteBuffer支持类型化的put和get，put放入的是什么类型的数据，get就应该使用相应的数据类型来取出。否则就可能有BufferUnderFlowException异常。

    ```java
    public class NIONByteBufferPutAndGet {
        public static void main(String[] args) {
            ByteBuffer buffer = ByteBuffer.allocate(64);
    
    
            //类型方式放入数据
            buffer.putInt(100);
            buffer.putLong(9);
            buffer.putChar('尚');
            buffer.putShort((short) 4);
    
            //取出
            buffer.flip();
    
            System.out.println();
            System.out.println(buffer.getShort());
            System.out.println(buffer.getInt());
            System.out.println(buffer.getLong());
            System.out.println(buffer.getLong());
    
    
        }
    }
    ```

    ![1575989070657](assets/1575989070657.png)

  * 可以将一个普通的buffer转成一个只读的buffer

    

  * dfadf

* selector



