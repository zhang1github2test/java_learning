package cn.szyrm.redis.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {
    private volatile static JedisPool jedisPool = null;
    private JedisPoolUtil(){}

    /**
     * 通过双重检查锁获取JedisPool
     * @return
     */
    public static JedisPool getJedisPool(){
        if(jedisPool == null){
            synchronized (JedisPoolUtil.class){
                if(jedisPool ==  null){
                    JedisPoolConfig config = new JedisPoolConfig();
                    //控制一个pool最多可以配置多少个jedis实例
                    config.setMaxTotal(1000);
                    //最多空闲数量
                    config.setMaxIdle(30);
                    // 最长等待时间 ms
                    config.setMaxWaitMillis(2000);
                    config.setTestOnBorrow(true);
                    jedisPool = new JedisPool(config,"192.168.195.129", 6379,0,"123456");
                }
            }
        }
        return  jedisPool;
    }

    /**
     * 请直接调用jedis的close方法
     * @param jedisPool
     * @param jedis
     */
    public static void release(JedisPool jedisPool, Jedis jedis){
        if(null != jedis){
            jedis.close();
        }
    }

}
