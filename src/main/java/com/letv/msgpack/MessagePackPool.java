package com.letv.msgpack;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.msgpack.MessagePack;

/**
 * Created by wangdi on 16-4-1.
 */
public  class MessagePackPool {


    public static  GenericObjectPool<MessagePack> pool = null;


    static{
        MessagePackObjectFactory factory = new MessagePackObjectFactory();
        GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
        conf.setMaxIdle(10);
        conf.setMaxTotal(100);
        conf.setMinIdle(20);
        pool = new GenericObjectPool<MessagePack>(factory,conf);
    }

    public  GenericObjectPool<MessagePack> getPool(){

        return pool;
    }





}


