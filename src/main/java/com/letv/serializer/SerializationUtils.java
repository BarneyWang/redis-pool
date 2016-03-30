package com.letv.serializer;

import java.nio.charset.Charset;

/**
 * Created by wangdi on 16-3-30.
 */
public class SerializationUtils {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static final byte[] EMPTY_ARRAY = new byte[0];


    static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }

    public static byte[] encode(String str) {
        if(str == null) {
            return SerializationUtils.EMPTY_ARRAY;
        }

        try {
            return str.getBytes(DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new SerializationException("Could not write String: " + e.getMessage(), e);
        }
    }
}
