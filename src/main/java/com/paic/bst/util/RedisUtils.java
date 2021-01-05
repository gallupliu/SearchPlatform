package com.paic.bst.util;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * description: RedisUtils
 * date: 2021/1/1 4:12 下午
 * author: gallup
 * version: 1.0
 */
@Component
public class RedisUtils {
//public class RedisUtils implements InitializingBean {
//    @Autowired
//    RedisTemplate redisTemplate;
//
//    public Boolean hasKey(final String key ){ return redisTemplate.hasKey(key);}
//
//    public boolean expire(final String key,final long timeout){return expire(key,timeout, TimeUnit.SECONDS);}
//
//    public boolean expire(final String key,final long timeout,final TimeUnit unit){
//        Boolean ret = redisTemplate.expire(key,timeout,unit);
//        return ret != null && ret;
//    }
//
//    public boolean del(final String key){
//        Boolean ret = redisTemplate.delete(key);
//        return ret != null && ret;
//    }
//
//    public long del(final Collection<String> keys){
//        Long ret = redisTemplate.delete(keys);
//        return ret == null ? 0:ret;
//    }
//
//    public void set(final String key,final Object value){
//        redisTemplate.opsForValue().set(key,value);
//    }
//
//    public void set(final String key,final Object value,final long timeout){
//        redisTemplate.opsForValue().set(key,value,timeout,TimeUnit.SECONDS);
//    }
//
//    public Object get(final String key){
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    public void hPut(final String key,final String hKey,final Object value){
//        redisTemplate.opsForHash().put(key,hKey,value);
//    }
//
//    public void hPutAll(final String key, final Map<String, Object> values){
//        redisTemplate.opsForHash().putAll(key,values);
//    }
//
//    public Object hGet(final String key,final String hKey){
//        return redisTemplate.opsForHash().get(key,hKey);
//    }
//
//    public List<Object> hMultiGet(final String key,final Collection<Object> hKeys){
//        return redisTemplate.opsForHash().multiGet(key,hKeys);
//    }
//
//    public long sSet(final String key,final Object... values){
//        Long count = redisTemplate.opsForSet().add(key,values);
//        return count == null ? 0:count;
//    }
//
//    public long sDel(final String key,final Object... values){
//        Long count = redisTemplate.opsForSet().remove(key,values);
//        return count == null ? 0:count;
//    }
//
//    public long lPush(final String key,final Object value){
//        Long count = redisTemplate.opsForList().rightPush(key,value);
//        return count == null ? 0:count;
//    }
//
//    public long lPush(final String key,final Collection<Object> values){
//        Long count = redisTemplate.opsForList().rightPushAll(key,values);
//        return count == null ? 0:count;
//    }
//
//
//    public long lPush(final String key,final Object... values){
//        Long count = redisTemplate.opsForList().rightPushAll(key,values);
//        return count == null ? 0:count;
//    }
//
//    public List<Object> lGet(final String key,final int start,final int end){
//        return redisTemplate.opsForList().range(key,start,end);
//    }
//
//    public Long delByPattern(String pattern){
//        Set keys = redisTemplate.keys(pattern);
//        if(!keys.isEmpty()){
//            return redisTemplate.delete(keys);
//        }else{
//            return 0L;
//        }
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception{
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//    }
}
