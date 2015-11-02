package com.tzls.search.dic.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
/**
 * 
 * 
 * */
public class RedisUtils {
	public static Logger logger = LoggerFactory.getLogger("ansj-redis-utils");
	
	/**       
     * @return conn        
     */       
    public static Jedis getConnection(JedisPool jedisPool) {  
        Jedis jedis=null;            
        try {                
            jedis=jedisPool.getResource();
        } catch (Exception e) {                
            e.printStackTrace(); 
            logger.error(e.getMessage(), e);
        }            
        return jedis;        
    }     
      
    /**        
     * @param conn        
     */       
    public static void closeConnection(JedisPool jedisPool,Jedis jedis) {            
        if (null != jedis) {                
            try {                    
                jedisPool.returnResource(jedis);                
            } catch (Exception e) {  
                e.printStackTrace();   
                logger.error(e.getMessage(), e);
            }            
        }        
    }    
      
}
