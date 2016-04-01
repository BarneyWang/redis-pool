package com.letv.serializer;

import org.msgpack.MessagePack;

/**
 * Created by wangdi on 16-4-1.
 */
public class MsgpackSerializer implements Serializer<Object> {

    private static MessagePack msgpack = new MessagePack();

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (o == null) {
            return SerializationUtils.EMPTY_ARRAY;
        }
        try {
            return this.msgpack.write(o);
        } catch (Exception e) {
            throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws SerializationException {
        return null;
    }
}
