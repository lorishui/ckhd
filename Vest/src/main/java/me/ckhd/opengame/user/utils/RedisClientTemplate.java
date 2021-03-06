package me.ckhd.opengame.user.utils;

import java.util.Iterator;
import java.util.Set;

import me.ckhd.opengame.common.utils.SerializeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@Component("redisClientTemplate")
public class RedisClientTemplate {

    private static final Logger log = LoggerFactory.getLogger(RedisClientTemplate.class);

    @Autowired
    private RedisDataSource redisDataSource;

    public void disconnect() {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        shardedJedis.disconnect();
    }

    /**
     * 设置单个值
     * 
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        String result = null;

        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
//            throw e;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * 设置单个值 ,有时效
     * 
     * @param key
     * @param value
     * @param time 秒
     * @return
     */
    public String set(String key, String value,int time) {
        String result = null;

        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setex(key, time, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
//            throw e;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }
    
    /**
     * 获取单个值
     * 
     * @param key
     * @return
     */
    public String get(String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }

        boolean broken = false;
        try {
            result = shardedJedis.get(key);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
    }
    
    /**
     * 移除单个值
     * 
     * @param key
     * @return
     */
    public void delete(String key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
        	log.info("redis get client failure!");
            return ;
        }

        boolean broken = false;
        try {
           shardedJedis.del(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
    }
    
    /**
     * 存储对象
     * 
     * @param key
     * @return
     */
    public void setObject(String key,Object data) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
        	log.info("redis get client failure!");
            return ;
        }

        boolean broken = false;
        try {
        	byte[] b = SerializeUtil.serialize(data);
        	if(b != null){
        		shardedJedis.set(key.getBytes(), b);
        	}
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
    }
    
    /**
     * 存储限时对象
     * 
     * @param key
     * @param data
     * @param seconds 时间：秒
     * @return
     */
    public void setObjectLimited(String key,Object data,int seconds) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
        	log.info("redis get client failure!");
            return ;
        }

        boolean broken = false;
        try {
        	byte[] b = SerializeUtil.serialize(data);
        	if(b != null){
        		shardedJedis.setex(key.getBytes(), seconds, b);
        	}
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
    }
    
    /**
     * 获取对象
     * 
     * @param key
     * @return
     */
    public Object getObject(String key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Object obj = null;
        if (shardedJedis == null) {
        	log.info("redis get client failure!");
            return obj;
        }
        boolean broken = false;
        try {
        	byte[] b = shardedJedis.get(key.getBytes());
        	if(b != null){
        		log.info("basestate byte size:"+b.length);
        		obj = SerializeUtil.unserialize(b);
        	}
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return obj;
    }
    
    /**
     * 删除对象
     * 
     * @param key
     * @return
     */
    public void delObject(String key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
        	log.info("redis get client failure!");
            return;
        }
        boolean broken = false;
        try {
        	shardedJedis.del(key.getBytes());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
    }
    
    /**
     * 批量删除对象
     * 
     * @param key
     * @return
     */
    public void delAllByKey(String key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
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
        }
    }
}
