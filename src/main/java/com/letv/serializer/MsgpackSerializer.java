package com.letv.serializer;

import com.letv.msgpack.MessagePackPool;
import org.msgpack.MessagePack;
import org.msgpack.template.Template;

/**
 * Created by wangdi on 16-4-1.
 */
public class MsgpackSerializer implements Serializer<Object> {


   private  static  MessagePackPool messagePackPool = new MessagePackPool();

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (o == null) {
            return SerializationUtils.EMPTY_ARRAY;
        }
        MessagePack messagePack = null;
        try {
            messagePack = messagePackPool.getPool().borrowObject();
            return messagePack.write(o);
        } catch (Exception e) {
            throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
        }finally {
            this.messagePackPool.getPool().returnObject(messagePack);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws SerializationException {

        if (SerializationUtils.isEmpty(bytes)) {
            return null;
        }
        MessagePack messagePack = null;
        try {
            messagePack = messagePackPool.getPool().borrowObject();

            return messagePack.read(bytes,clz);
        } catch (Exception e) {
            throw new SerializationException("Could not read JSON: " + e.getMessage(), e);
        }finally {
            this.messagePackPool.getPool().returnObject(messagePack);
        }

    }


    public <T> T deserialize(byte[] bytes,Template<T> template) throws SerializationException {

        if (SerializationUtils.isEmpty(bytes)) {
            return null;
        }
        MessagePack messagePack = null;
        try {
            messagePack = messagePackPool.getPool().borrowObject();

            return messagePack.read(bytes, template);
        } catch (Exception e) {
            throw new SerializationException("Could not read JSON: " + e.getMessage(), e);
        }finally {
            this.messagePackPool.getPool().returnObject(messagePack);
        }
    }
}
