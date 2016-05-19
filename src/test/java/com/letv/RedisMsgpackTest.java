package com.letv;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.msgpack.template.Template;
import org.msgpack.template.Templates;

import java.util.Set;

/**
 * Created by bojack on 16/5/18.
 */
@RunWith(JUnit4.class)
public class RedisMsgpackTest {


    /**
     * redis 集群
     */
    private static final RedisMsgPack2Client CLIENT = new RedisMsgPack2Client(
            "10.140.120.123:7006,10.140.120.123:7005," +
                    "10.140.120.122:7004,10.140.120.122:7003," +
                    "10.140.120.121:7001,10.140.120.121:7000", "test-");




    @Test
    public void test(){
        RedisMsgpackTest test= new RedisMsgpackTest();
//        Long l = test.sadd();
//        Assert.assertEquals(Long.valueOf(1L),l);
        Set<String> set = test.smembers();


    }

    public Long sadd(){
        String[] strs = new String[]{"a","b","c"};
       return CLIENT.sadd("myset",strs);
    }

    public Set<String> smembers(){

        return CLIENT.smembers("test-myset");
    }


    public void hset() {
        long l = CLIENT.hset("test", "17", "abc");
        Assert.assertEquals(1L, l);

    }


    public void hget() {
    String value = CLIENT.hget("test", "17", Templates.TString);
        Assert.assertEquals("111", value);
    }

    public void rpush() {


    }


    public void rlange() {

    }


}
