# `Redis`  入门

## 一、简介

>  `redis`是一种`NoSQL`数据库。`NoSql`数据的共同特点是去掉关系型数据的关系型特点。数据之间没有关系，这样就非常容易扩展。也无形之间，在架构层面上带来了可扩展的能力。

 [官网][https://redis.io/documentation]

| RDBMS                        | Nosql                                    |
| ---------------------------- | ---------------------------------------- |
| 结构化查询语言               | 没有声明查询语言                         |
| 数据和关系都存储在单独的表中 | 键值对存储、列存储、文档存储、图形数据库 |
| 严格一致性                   | 最终一致性、高性能、高可用和可伸缩性     |
| 结构化数据                   | 非结构化和不可预知的数据                 |

传统的ACID:A(Atomicity)原子性，C(consistency)一致性，I(Isolation) 隔离性,D(Durability)持久性

#### CAP理论：

* C(consistency)一致性:在分布式环境中，一致性是指在多个数据副本之间保持一致的特性。在一致性的需求下，当一个系统在数据一致性的要求下执行更新操作，应该保证系统的数据任然处于一致的状态。

* A(Availability)可用性：可用性是指系统提供的服务必须一直处于可用的状态，对于用户的每一个操作请求总是能够在有限的时间内返回结果。
* P(Partition tolerance)分区容错性：分区容错性约束了一个分布式系统需要具有如下特性：分布式系统在遇到任何网络分区故障的时候，需要能够保证对外提供一致性和可用性服务。

> jedispool 的连接池耗尽的时候会抛如下异常：Could not get a resource since the pool is exhausted

## 二、`redis` 安装

下载及安装

> ```
> $ wget http://download.redis.io/releases/redis-5.0.6.tar.gz
> $ tar xzf redis-5.0.6.tar.gz
> $ cd redis-5.0.6
> $ make
> ```

启动redis服务器

>```
>$ src/redis-server
>```

启动redis客户端并进行测试

> ```
> $ src/redis-cli
> redis> set foo bar
> OK
> redis> get foo
> "bar"
> ```

## 三、`redis` 命令学习

`redis` 支持的常用五种数据结构：string（字符串类型）,list(列表类型)、hash (散列类型)、set(集合类型)、`sortSet`(有序集合类型)

* ### `redis`的key

  * redis的key是二进制安全(可以任何二进制序列作为键)的，空字符串也是一个合法的key.
  * 太长的key值不是好主意
    * 消耗内存
    * 查找键消耗时间长

  * 太短的key也不是好主意
    * 可读性太差
    * 容易发生键冲突

  * 试着坚持一个模式。例如，“object-type:id”是一个好主意
  * 键最大允许值为 512M

* #### `redis` string类型

  string 类型是最简单的一种redis数据类型。

  常用命令：

  * ##### `SET`

    SET KEY VALUE [EX seconds] [PX milliseconds] [NX|EX]

    设置字符串的值为value。如果`key ` 已经有其他值，set 会覆盖旧值。无视类型 

    示例：

    * 对不存在的键设值 

      >127.0.0.1:6379> keys * 
      >(empty list or set)
      >127.0.0.1:6379> set key value
      >OK
      >127.0.0.1:6379> get key
      >"value"

    * 对已经存在的键进行设值

      > 127.0.0.1:6379> exists key
      > (integer) 1
      > 127.0.0.1:6379> set key newValue
      > OK
      > 127.0.0.1:6379> get key
      > "newValue"

    * 使用`EX`参数

      添加过期时间，以秒为单位

      > 127.0.0.1:6379> set key newValue ex 100 
      > OK
      > 127.0.0.1:6379> ttl key
      > (integer) 92

    * 使用`pX` 参数

      添加过期时间，以毫秒为单位

      >127.0.0.1:6379> set key-with-expire-time value  px 5000
      >OK
      >127.0.0.1:6379> pttl key-with-expire-time
      >(integer) 3119

    * 使用`NX` 参数

      键不存在的时可以设值成功

      >127.0.0.1:6379> set no-exists-key value NX EX 100
      >OK   #
      >127.0.0.1:6379> set no-exists-key value NX EX 100
      >(nil)  #  可以利用此特性来设置一个分布锁的枷锁功能  键存在的时候，设置不成功

    * 使用`EX` 参数

      只有当键存在的时候，才能设值成功

      > 127.0.0.1:6379> exists exists-key 
      > (integer) 0   #键不存在
      > 127.0.0.1:6379> set exists-key value xx
      > (nil)   #不存在，所以设值失败
      > 127.0.0.1:6379> set exists-key value 
      > OK  #先设值
      > 127.0.0.1:6379> set exists-key newvalue xx
      > OK  #设值新值是否成功
      > 127.0.0.1:6379> get exists-key
      > "newvalue"

  * ##### `GET `： GET key 

    返回`key `对应的字符串。

    如果key 不存在,返回特使值`nil` ;否则返回`key` 的值。

    如果key的值并非字符串类型，返回一个错误。因为`GET` 命令只能用于字符串值。

    示例代码：

    >127.0.0.1:6379> get exists-key
    >"newvalue"
    >127.0.0.1:6379> get no_exists_key
    >(nil)
    >
    >127.0.0.1:6379> rpush mylist 1 2 3 5 6 6 7 8 
    >(integer) 8
    >127.0.0.1:6379> get mylist
    >(error) WRONGTYPE Operation against a key holding the wrong kind of value

  * ##### `INCR`: INCR key  

    为key 存储的数字值加一。并返回执行加一操作后的值

    如果键 `key` 不存在， 那么它的值会先被初始化为 `0` ， 然后再执行 `INCR` 命令。

    如果键 `key` 储存的值不能被解释为数字， 那么 `INCR` 命令将返回一个错误。

    本操作的值限制在 64 位(bit)有符号数字表示之内。

    > INCR` 命令是一个针对字符串的操作。 因为 Redis 并没有专用的整数类型， 所以键 `key` 储存的值在执行 `INCR` 命令时会被解释为十进制 64 位有符号整数。

    示例：

    > 127.0.0.1:6379> incr k2
    > (integer) 1
    > 127.0.0.1:6379> incr k2
    > (integer) 2
    > 127.0.0.1:6379> incr k2
    > (integer) 3
    > 127.0.0.1:6379> incr k2
    > (integer) 4

  * ##### `INCRBY` ：` incrby key  increment` 

    为键 `key` 储存的数字值加上增量 `increment` 。并返回加上incrment之后的值。

    如果键 `key` 不存在， 那么键 `key` 的值会先被初始化为 `0` ， 然后再执行 `INCRBY` 命令。

    如果键 `key` 储存的值不能被解释为数字， 那么 `INCRBY` 命令将返回一个错误。

    本操作的值限制在 64 位(bit)有符号数字表示之内。

    示例：

    >127.0.0.1:6379> set k3 20
    >OK
    >127.0.0.1:6379> incrby k3 50
    >(integer) 70

  * ##### `DECR` ;decy key 

    将key的值减一

  * ##### `DECRBY` 

    将key key 减一个固定值。并返回操作后的值

  * ##### `MSET` ：`MSET key value [key value …]` 

    同时为多个键设值

    如果某个给定键已经存在， 那么 `MSET` 将使用新值去覆盖旧值， 如果这不是你所希望的效果， 请考虑使用 `MSETNX` 命令， 这个命令只会在所有给定键都不存在的情况下进行设置。

    `MSET` 是一个原子性(atomic)操作， 所有给定键都会在同一时间内被设置， 不会出现某些键被设置了但是另一些键没有被设置的情况。

    >  127.0.0.1:6379> mset k1 v1 k2 v2 k3 v3 k4 v4
    > OK
    >
    > 127.0.0.1:6379> del "k1 v1"
    > (integer) 1
    > 127.0.0.1:6379> keys k*
    > 1) "k1"
    > 2) "k4"
    > 3) "k3"
    > 4) "k2"

  * ##### `MGET` 

    同时获取多个键的值：

    > 127.0.0.1:6379> mget k1 k2 k3 k4
    > 1) "v1"
    > 2) "v2"
    > 3) "v3"
    > 4) "v4"

  其他命令：

  - ##### `SETNX` 

    只在键 `key` 不存在的情况下， 将键 `key` 的值设置为 `value` 。

    若键 `key` 已经存在， 则 `SETNX` 命令不做任何动作。

    `SETNX` 是『SET if Not eXists』(如果不存在，则 SET)的简写。

    设值成功返回1，失败返回0.

    > 127.0.0.1:6379> setnx k1 newV1
    > (integer) 0
    > 127.0.0.1:6379> del k1
    > (integer) 1
    > 127.0.0.1:6379> setnx k1 newV1
    > (integer) 1

  - ##### `SETEX`

    将键 `key` 的值设置为 `value` ， 并将键 `key` 的生存时间设置为 `seconds` 秒钟。

    如果键 `key` 已经存在， 那么 `SETEX` 命令将覆盖已有的值。

    setEX 是一个原子性操作。它可以在同一时间内完成设值和设置过期时间的这两个操作。

  - ##### `PSETEX`

    类似于 SETNX ，但是其过期时间为 毫秒。

  - ##### ` STRLEN` 

    返回字符串值的长度。当key 不存在的时候返回0。如果存储的不是字符串，返回一个错误。

    > 127.0.0.1:6379> strlen k1
    > (integer) 5
    > 127.0.0.1:6379> get k1
    > "newV1"

  - ##### `APPEND`

    如果键 `key` 已经存在并且它的值是一个字符串， `APPEND` 命令将把 `value` 追加到键 `key` 现有值的末尾。

    如果 `key` 不存在， `APPEND` 就简单地将键 `key` 的值设为 `value` ， 就像执行 `SET key value` 一样。

    返回值：追加 `value` 之后， 键 `key` 的值的长度。

    >127.0.0.1:6379> append k1 appendValue
    >(integer) 16
    >127.0.0.1:6379> get k1
    >"newV1appendValue"

  - ##### `SETRANGE` 

    从偏移量 `offset` 开始， 用 `value` 参数覆写(overwrite)键 `key` 储存的字符串值。

    不存在的键 `key` 当作空白字符串处理。

    > 当生成一个很长的字符串时， Redis 需要分配内存空间， 该操作有时候可能会造成服务器阻塞(block)。 在2010年出产的Macbook Pro上， 设置偏移量为 536870911(512MB 内存分配)将耗费约 300 毫秒， 设置偏移量为 134217728(128MB 内存分配)将耗费约 80 毫秒， 设置偏移量 33554432(32MB 内存分配)将耗费约 30 毫秒， 设置偏移量为 8388608(8MB 内存分配)将耗费约 8 毫秒。

    >127.0.0.1:6379> set greeting "hello world"
    >OK
    >127.0.0.1:6379> SETRANGE greeting  6 "Redis"
    >(integer) 11
    >127.0.0.1:6379> get greeting 
    >"hello Redis"

  - ##### GETSET

    将键 `key` 的值设为 `value` ， 并返回键 `key` 在被设置之前的旧值。

    返回值：

    返回给定键 `key` 的旧值。

    如果键 `key` 没有旧值， 也即是说， 键 `key` 在被设置之前并不存在， 那么命令返回 `nil` 。

    当键 `key` 存在但不是字符串类型时， 命令返回一个错误。

    >127.0.0.1:6379> getset greeting "hello world"
    >"hello Redis"
    >127.0.0.1:6379> get greeting
    >"hello world"

  - ##### `GETRANGE`

    返回键 `key` 储存的字符串值的指定部分， 字符串的截取范围由 `start` 和 `end` 两个偏移量决定 (包括 `start` 和 `end` 在内)。

    负数偏移量表示从字符串的末尾开始计数， `-1` 表示最后一个字符， `-2` 表示倒数第二个字符， 以此类推。

    `GETRANGE` 通过保证子字符串的值域(range)不超过实际字符串的值域来处理超出范围的值域请求。

    > 127.0.0.1:6379> set key "hello world"
    > OK
    > 127.0.0.1:6379> GETRANGE key 0 4
    > "hello"

  - ##### `INCRBYFLOAT` 

    为键 `key` 储存的值加上浮点数增量 `increment` 。

    如果键 `key` 不存在， 那么 `INCRBYFLOAT` 会先将键 `key` 的值设为 `0` ， 然后再执行加法操作。

    如果命令执行成功， 那么键 `key` 的值会被更新为执行加法计算之后的新值， 并且新值会以字符串的形式返回给调用者。

    无论是键 `key` 的值还是增量 `increment` ， 都可以使用像 `2.0e7` 、 `3e5` 、 `90e-2` 那样的指数符号(exponential notation)来表示， 但是， **执行 INCRBYFLOAT 命令之后的值**总是以同样的形式储存， 也即是， 它们总是由一个数字， 一个（可选的）小数点和一个任意长度的小数部分组成（比如 `3.14` 、 `69.768` ，诸如此类)， 小数部分尾随的 `0` 会被移除， 如果可能的话， 命令还会将浮点数转换为整数（比如 `3.0` 会被保存成 `3` ）。

    此外， 无论加法计算所得的浮点数的实际精度有多长， `INCRBYFLOAT` 命令的计算结果最多只保留小数点的后十七位。

    >127.0.0.1:6379> set floatkey 1
    >OK
    >127.0.0.1:6379> INCRBYFLOAT floatkey 0.3
    >"1.3"

  - ##### `MSETNX` 

    当且仅当所有的key不存在的时候，为所有的key设置值

    即使只有一个给定键已经存在， `MSETNX` 命令也会拒绝执行对所有键的设置操作。

    `MSETNX` 是一个原子性(atomic)操作， 所有给定键要么就全部都被设置， 要么就全部都不设置， 不可能出现第三种状态。

    返回值：成功1，失败：0

* #### `redis` list类型

* #### `redis hash` 类型

- [HSET](http://redisdoc.com/hash/hset.html)

  ###### `HSET hash field value` 

  将哈希表 `hash` 中域 `field` 的值设置为 `value` 。

  如果给定的哈希表并不存在， 那么一个新的哈希表将被创建并执行 `HSET` 操作。

  如果域 `field` 已经存在于哈希表中， 那么它的旧值将被新值 `value` 覆盖。

  ###### 返回值

  当 `HSET` 命令在哈希表中新创建 `field` 域并成功为它设置值时， 命令返回 `1` ； 如果域 `field` 已经存在于哈希表， 并且 `HSET` 命令成功使用新值覆盖了它的旧值， 那么命令返回 `0` 。

  >127.0.0.1:6379> hset user:1 name zhangsan
  >(integer) 1
  >127.0.0.1:6379> hget user:1 name
  >"zhangsan"
  >127.0.0.1:6379> hset user:1 name libai
  >(integer) 0
  >127.0.0.1:6379> hget user:1 name
  >"libai"

- [HSETNX](http://redisdoc.com/hash/hsetnx.html)

  

- [HGET](http://redisdoc.com/hash/hget.html)

- [HEXISTS](http://redisdoc.com/hash/hexists.html)

- [HDEL](http://redisdoc.com/hash/hdel.html)

- [HLEN](http://redisdoc.com/hash/hlen.html)

- [HSTRLEN](http://redisdoc.com/hash/hstrlen.html)

- [HINCRBY](http://redisdoc.com/hash/hincrby.html)

- [HINCRBYFLOAT](http://redisdoc.com/hash/hincrbyfloat.html)

- [HMSET](http://redisdoc.com/hash/hmset.html)

- [HMGET](http://redisdoc.com/hash/hmget.html)

- [HKEYS](http://redisdoc.com/hash/hkeys.html)

- [HVALS](http://redisdoc.com/hash/hvals.html)

- [HGETALL](http://redisdoc.com/hash/hgetall.html)

- [HSCAN](http://redisdoc.com/hash/hscan.html)