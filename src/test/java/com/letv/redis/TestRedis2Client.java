package com.letv.redis;

import com.letv.RedisMsgPack2Client;
import com.letv.RedisMsgPackClient;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * Created by bojack on 16/5/17.
 */
public class TestRedis2Client {

    private static final RedisMsgPack2Client CLIENT = new RedisMsgPack2Client(
            "10.140.120.123:7006,10.140.120.123:7005," +
                    "10.140.120.122:7004,10.140.120.122:7003," +
                    "10.140.120.121:7001,10.140.120.121:7000", "test-");

    public static void main(String[] args) {

        CLIENT.delete("test-list");
        System.out.println("--------lrang------");
//        List<Object> list = Lists.newArrayList();
//        list.add("111");
//        list.add("222");
//        list.add("333");
        CLIENT.rpush("test-list", "111");

        List<String> l = CLIENT.lrange("test-list",0,-1);
    }

}
