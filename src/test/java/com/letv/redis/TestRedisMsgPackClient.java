package com.letv.redis;

import com.letv.RedisMsgPackClient;
import com.letv.msgpack.ObjectTemplate;
import org.assertj.core.util.Lists;
import org.msgpack.template.Templates;

import java.util.List;

/**
 * Created by wangdi5 on 2016/4/5.
 */
public class TestRedisMsgPackClient {

    private static final RedisMsgPackClient CLIENT = new RedisMsgPackClient(
            "10.140.120.123:7006,10.140.120.123:7005," +
                    "10.140.120.122:7004,10.140.120.122:7003," +
                    "10.140.120.121:7001,10.140.120.121:7000", "auth-");
    public static void main(String[] args) {
        System.out.println(CLIENT.set("abc", "test", "NX", "EX", 150)); //有用
        System.out.println("----------done---------");
        System.out.println(CLIENT.get("abc", Templates.TString));

        CLIENT.delete("test-list");
        System.out.println("--------lrang------");
        List<Object> list = Lists.newArrayList();
        list.add("111");
        list.add("222");
        list.add("333");
        CLIENT.set("test-list", list, "NX", "EX", 600);

//                for(int i = 0; i < 10; i ++) {
//            System.out.println(CLIENT.rpush("mytest", String.valueOf(i)));
//        }

        System.out.println(CLIENT.rpush("mytest", "1111111"));
        CLIENT.lrange("mytest", 0, -1);
        System.out.println(CLIENT.get("test-list", Templates.tList(ObjectTemplate.getInstance())));
        List<Object> str = CLIENT.lrange("test-list", 0, -1);
        for(Object s : str) {
            System.out.println(s);
        }
        System.out.println("-------xxxxxxx--------");
        for(int i = 1; i < 12; i ++) {
            str = CLIENT.lrange("auth-lrange-" + i, 0, -1);
            for(Object s : str) {
                System.out.println(s);
            }
            System.out.println("---------------");
        }








//        final String key, final Object value, final String nxxx, final String expx, final long time
    }

}
