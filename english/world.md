### 2019-11-12  

* algorithms:算法

* precise algorithms:精确算法
* approximated algorithms:近似算法

* tune ：调整
  * tune it for speed or accuracy :调优速度或者准确性
* accuracy:准确性   
* directive:指示，指令

### 2019-11-14

* durability:持久性，耐久性

  * > For a wider overview of Redis persistence and the durability guarantees it provides you may also want to read [Redis persistence demystified](http://antirez.com/post/redis-persistence-demystified.html).

  * demystified:使非神秘化

* reconstructing:重组，再现

* compact:紧凑的

  * > RDB is a very compact single-file point-in-time representation of your Redis data   RDB是一个非常紧凑的单文件时间点表示您的Redis数据

* disaster:灾难

  * > RDB is very good for disaster recovery  RDB非常适合灾难恢复

>```lua
>if redis.call("get",KEYS[1]) == ARGV[1] then
>    return redis.call("del",KEYS[1])
>else
>    return 0
>end
>```

2019-11-19

* disposable:adj. 可任意处理的；可自由使用的；用完即可丢弃的
* initializing:正在初始化  initializing  initializing  

