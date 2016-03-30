package com.letv.serializer;




/**
 * Created by wangdi on 16-3-30.
 */
public interface Serializer<T> {


    public byte[] serialize(T t) throws SerializationException;

    public <T> T deserialize(byte[] bytes, Class<T> clz) throws SerializationException;
}
