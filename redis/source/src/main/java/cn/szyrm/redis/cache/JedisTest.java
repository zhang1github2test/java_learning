package cn.szyrm.redis.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Date;
import java.util.List;

/**
 * redis服务器的版本为5.0.5  jedis客户端的版本号为：3.1.0
 */
public class JedisTest {


    public static void main(String[] args) {
      // testString();


    int visit =0;
        while (testVisitLimit("max_key_incr",2000)){
            visit++;
        }
        System.out.println("基于incr 指令实现限制访问次数:" + visit);
        while (testVisitLimit2("max_key_list",2000)){
            visit++;
        }
        visit = 0;
        System.out.println("基于list 列表实现限制访问次数" + visit);

    }



    /**
     * @return
     */
    public static Jedis getJedis(){

        return JedisPoolUtil.getJedisPool().getResource();
    }

    /**
     * 用jedis操作String类型的数据
     */
    public static void testString(){
        Jedis jedis = getJedis();
        System.out.println("----------------------------------string 类型-----------------------");
        jedis.set("stringkey","hello redis");
        System.out.print("string 类型设值:hello redis\t");
        String value = jedis.get("stringkey");
        System.out.println("stringkey获取到的值为:" + value );
        //当k2不存在的时候  才能将操作成功  并且过期时间设置为20秒0  操作成功返回ok,否则返回null
        String result = jedis.set("k2", "v2", SetParams.setParams().ex(20).nx());
        System.out.println("result:" + result);

        //mset mget操作
        System.out.println( jedis.mset("k1", "v1","k2","v2"));
        List<String> mget = jedis.mget("k1", "k2");

        jedis.close();

    }

    /**
     * 实现访问频率限制
     */
    public static boolean testVisitLimit(String max_key,int max) {
        try(    Jedis jedis = getJedis()){
            String s = jedis.get(max_key);
            if (s != null) {
                int visits = Integer.parseInt(s);
                if (visits < max) {
                    jedis.incr(max_key);
                    //   return true;
                }else{
                    System.out.println("已经超过最大的访问次数");
                    return  false;
                }


            }else{
                jedis.setex(max_key, 60, "1");
            }
        }
        return true;
    }

    /**
     * 实现访问频率限制二
     * @param max_key
     * @param max
     * @return
     */
    public static boolean testVisitLimit2(String max_key,int max){
        try(  Jedis jedis = getJedis()){

            Long num = jedis.llen(max_key);
            if(num < max){
                jedis.rpush(max_key,new Date().getTime()+ "");

            }else{

                long preTime = Long.parseLong(jedis.lindex(max_key, 0));
                if(System.currentTimeMillis() -preTime < 60*1000 ){
                    System.out.println("访问太频繁");
                    return  false;
                }
                jedis.rpush(max_key,new Date().getTime()+ "");
                jedis.ltrim(max_key,-max,-1);

            }
        }

        return  true;
    }

    /**
     * 用jedis的事务操作
     */
    public static void testTx(){

    }
}
