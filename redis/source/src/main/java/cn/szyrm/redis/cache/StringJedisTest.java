package cn.szyrm.redis.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 用来演示操作redis string 类型数据结构
 */
public class StringJedisTest {
    private static JedisPool jedisPool = JedisPoolUtil.getJedisPool();

    /**
     * set命令的使用
     */
    public static  void set(){
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set("set_key", "value");
        System.out.println(result);
    }
}
