package com.hsueh.myredis;

import cn.hutool.core.lang.UUID;
import com.hsueh.myredis.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MyredisApplicationTests {

    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    public RedisCache redisCache;

    @Test
    void contextLoads() {
        String lockKey = "123";
        String UUID = "uuid";
//        如果为空就set值，并返回1
//        如果存在(不为空)不进行操作，并返回0
//        超时时间3分钟
//        boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey,UUID,3, TimeUnit.MINUTES);
//        if (!success){
//            System.out.println("锁已存在");
//        }
        // 执行 lua 脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/DelKey.lua")));
        // 指定返回类型
        redisScript.setResultType(Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        List<String> list = new ArrayList<>();
        list.add(lockKey);
//        Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(lockKey),UUID);
        Long result = (Long) redisTemplate.execute(redisScript, list, UUID);
//        redisTemplate.execute(RedisScript<T> script, List<String> keys, Object args[])


        System.out.println(result);
    }

    @Test
    void redisSet() {
        String lockKey = "123";
        String UUID = "uuid";
        redisCache.setCacheObject(lockKey, UUID);
    }

    @Test
    void redisSetNum() {
        int num = 20;
//        redisCache.setCacheObject("goods", num);
        redisTemplate.opsForValue().set("goods", num);
    }
    @Test
    void lua() {

        // 执行 lua 脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/num.lua")));
        // 指定返回类型
        redisScript.setResultType(Long.class);
        List<String> list = new ArrayList<>();
        list.add("goods");
        for (int i = 0; i < 100; i++){
            String userId = UUID.fastUUID().toString();
            Long result = (Long) redisTemplate.execute(redisScript, list, userId);
            System.out.println("i:" + String.valueOf(i) +"---result:"+ String.valueOf(result));
        }

    }

}
