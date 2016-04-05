package com.letv.redis;

import com.letv.RedisClient;

import java.util.List;

/**
 * Created by wangdi on 16-3-30.
 */
public class TestRedisClient {

    private static final RedisClient REDIS_CLIENT = new RedisClient(
            "10.140.120.123:7006,10.140.120.123:7005," +
            "10.140.120.122:7004,10.140.120.122:7003" +
            "10.140.120.121:7001,10.140.120.121:7000", "auth-");

    public static void main(String[] args) {
        System.out.println(REDIS_CLIENT.set("abc", "test", "NX", "EX", 150)); //有用
        System.out.println("----------done---------");

//        final String key, final Object value, final String nxxx, final String expx, final long time
    }

    public static void main2(String[] args) {
        List<String> str = REDIS_CLIENT.lrange("auth-list", 0, -1);
        for(String s : str) {
            System.out.println(s);
        }
        System.out.println("-------xxxxxxx--------");
        for(int i = 1; i < 12; i ++) {
            str = REDIS_CLIENT.lrange("auth-lrange-" + i, 0, -1);
            for(String s : str) {
                System.out.println(s);
            }
            System.out.println("---------------");
        }

//        for(int i = 0; i < 10; i ++) {
//            System.out.println(REDIS_CLIENT.rpush("mytest", String.valueOf(i)));
//        }

//        for(int i = 0; i < 10; i ++) {
//            String value = REDIS_CLIENT.lpop("mytest", String.class);
//            System.out.println("result=" + value);
//        }

        System.out.println("----------xxxxxxxxxxxx--------------------");

//        List<String> result = REDIS_CLIENT.lrange("mytest", 0, -1);
//        for(String s : result) {
//            System.out.println("value = " + s);
//        }

        System.out.println("--------done------");
    }
}
