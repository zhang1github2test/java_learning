package cn.szyrm.redis.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于redis实现的分布式锁
 */
public abstract class DistributedLock {
    private static JedisPool jedisPool = JedisPoolUtil.getJedisPool();
    private static Map<String,String>   values = new ConcurrentHashMap<>();

    /**
     *利用  SET resource_name my_random_value NX PX 30000  这种命令来实现redis分布式锁
     * @param key
     * @param ex
     * @return
     */
    public static boolean lock(String key,int ex){
        String value = UUID.randomUUID().toString();
        try(  Jedis jedis = jedisPool.getResource();) {
            String status = jedis.set(key,  value, SetParams.setParams().ex(ex).nx());
            if(Objects.equals(status,"OK")){
                values.put(key,value);
                return  true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        boolean lock = lock("lock", 10);
        System.out.println(lock);
        releaseLock("lock");
        boolean lock2 = lock("lock", 10);
        System.out.println(lock2);

    }

    /**
     * 释放分布式锁
     * @param key
     * @return
     */
    public static void releaseLock(String key){
        String value = values.get(key);
        if(value == null) return  ;

        try (Jedis jedis = jedisPool.getResource();){
            String lua = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                    "    return redis.call(\"del\",KEYS[1])\n" +
                    "else\n" +
                    "    return 0\n" +
                    "end";
            Object evalsha = jedis.evalsha(jedis.scriptLoad(lua), Arrays.asList(key), Arrays.asList(value));
            System.out.println(evalsha);
            values.remove(key);
        }

    }


}
