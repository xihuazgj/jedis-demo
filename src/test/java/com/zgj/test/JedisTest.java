package com.zgj.test;


import com.zgj.jedis.util.JedisConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;


public class JedisTest {

    private Jedis jedis;

    @BeforeEach
    void setUp(){
        //1.建立链接
//        jedis = new Jedis("192.168.32.131",6379);
        jedis = JedisConnectionFactory.getJedis();
        //2.选择库
        jedis.select(0);
    }

    @Test
    void testString(){

        //1.存入数据
        String result = jedis.set("name","guojunzhang");
        System.out.println("result = "+ result);
        //2.获取数据
        String name = jedis.get("name");
        System.out.println("name = " + name);
    }
    @Test
    void testHash(){
        //1.插入hash数据
        jedis.hset("user:1","name","Jack");
        jedis.hset("user:1","age","21");
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("name","guojunzhang");
        hashMap.put("age","22");
        hashMap.put("address","四川内江");
        jedis.hmset("usermap:1",hashMap);
        //2.获取hash数据
        Map<String,String> map = jedis.hgetAll("user:1");
        System.out.println(map);
        System.out.println("-------------------------");
        Map<String,String> hm = jedis.hgetAll("usermap:1");
        System.out.println(hm);
        List<String> list = hm.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.toList());
        System.out.println(list);
        System.out.println(list.get(0));
        System.out.println(list.get(1));
    }
    @AfterEach
    void testDown(){
        if (jedis != null){
            jedis.close();
        }
    }
}
