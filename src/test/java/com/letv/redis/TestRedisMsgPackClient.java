package com.letv.redis;

import com.letv.RedisMsgPackClient;
import org.msgpack.template.Templates;

/**
 * Created by wangdi5 on 2016/4/5.
 */
public class TestRedisMsgPackClient {

    private static final RedisMsgPackClient REDIS_MSG_PACK_CLIENT = new RedisMsgPackClient(
            "10.140.120.123:7006,10.140.120.123:7005," +
                    "10.140.120.122:7004,10.140.120.122:7003," +
                    "10.140.120.121:7001,10.140.120.121:7000", "auth-");
    public static void main(String[] args) {
        System.out.println(REDIS_MSG_PACK_CLIENT.set("abc", "test", "NX", "EX", 150)); //有用
        System.out.println("----------done---------");
        System.out.println(REDIS_MSG_PACK_CLIENT.get("abc", Templates.TString));

//        final String key, final Object value, final String nxxx, final String expx, final long time
    }

}
