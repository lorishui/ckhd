package me.ckhd.opengame.user.utils;

import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

public class RedisUtils {
	//Redis服务器IP
    private static String ADDR = "127.0.0.1";
    
    //Redis的端口号
    private static int PORT = 6379;
    
    //访问密码
//    private static String AUTH = "admin";
    
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;
    
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;
    
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;
    
    private static int TIMEOUT = 10000;
    
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    
    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
//            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT,AUTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
    
    public static String get(String key){
    	String obj = getJedis().get(key);
    	if( obj != null ){
    		return obj;
    	}
    	return null;
    }
    
    public static void add(String key,String value){
    	getJedis().set(key, value);
    }
    
    public static void add(String key,String value,int time){
    	getJedis().setex(key, time, value);
    }
    
    public static void delete(String key){
    	getJedis().del(key);
    }
    
    /**
     * 批量删除对象
     * 
     * @param key
     * @return
     */
    public void delAllByKey(String key) {
       /* ShardedJedis shardedJedis = getJedis();
        if (shardedJedis == null) {
        	log.info("redis get client failure!");
            return;
        }
        boolean broken = false;
        try {
        	Object[] obj = shardedJedis.getAllShards().toArray();
        	for(Object jedis:obj){
	            Set<String> set = ((Jedis)jedis).keys(key);
	            Iterator<String> it = set.iterator();  
//	            Transaction tx = ((Jedis)jedis).multi(); //事务
	            while(it.hasNext()){
	                String keyStr = it.next();
	                log.info("key="+keyStr);
	                //tx.del(keyStr);
	                ((Jedis)jedis).del(keyStr);
	            } 
//	            List<Object> results = tx.exec();
        	}
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }*/
    	Jedis jedis = getJedis();
    	 Set<String> set = jedis.keys(key);
         Iterator<String> it = set.iterator(); 
    	 Transaction tx = getJedis().multi(); //事务
         while(it.hasNext()){
             String keyStr = it.next();
//             log.info("key="+keyStr);
             //tx.del(keyStr);
             jedis.del(keyStr);
         } 
    }
    
    public static void main(String[] args) {
    	Set<String> set = getJedis().keys("CFG_PARAM_KEY_mmextend_1046_*_*_*_*");
    	for(String str:set){
    		System.out.println(str);
    	}
	}
}
