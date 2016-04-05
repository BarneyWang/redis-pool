package com.letv.msgpack;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.msgpack.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangdi5 on 2016/4/1.
 */
public class MessagePackObjectFactory implements PooledObjectFactory<MessagePack> {


    private final static Logger logger = LoggerFactory.getLogger(MessagePackObjectFactory.class);



    public PooledObject<MessagePack> makeObject() throws Exception {
        return  new DefaultPooledObject<MessagePack>(new MessagePack());
    }

    @Override
    public void destroyObject(PooledObject<MessagePack> pooledObject) throws Exception {
        MessagePack messagePack = pooledObject.getObject();
        messagePack=null;
    }

    @Override
    public boolean validateObject(PooledObject<MessagePack> pooledObject) {
        if(logger.isDebugEnabled()){
            logger.debug("MessagePack validate");
        }
        return true;
    }

    @Override
    public void activateObject(PooledObject<MessagePack> pooledObject) throws Exception {
        if(logger.isDebugEnabled()){
            logger.debug("MessagePack activateObject");
        }
    }



    /**
     * 功能描述：钝化资源对象
     *
     * 什么时候会调用此方法
     * 1：将资源返还给资源池时，调用此方法。
     */
    @Override
    public void passivateObject(PooledObject<MessagePack> pooledObject) throws Exception {
        if(logger.isDebugEnabled()){
            logger.debug("MessagePack passivateObject");
        }
    }
}
