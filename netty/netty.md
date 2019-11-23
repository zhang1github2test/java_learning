## netty学习

java中的IO模式主要有三种:BIO、NIO、AIO

#### 一、BIO

bio是一种同步阻塞的IO模式，其中每一个客户端对应服务端的一个线程。适合客户量小且固定的架构，编程比较简单。

#### 二、NIO 

NIO是一种同步非阻塞的IO模式。服务端采用一个线程来管理一个selector,每个selector来管理多个连接。selector就是一个多路复用器

