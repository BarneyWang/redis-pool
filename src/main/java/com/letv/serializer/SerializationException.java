package com.letv.serializer;

/**
 * Created by wangdi on 16-3-30.
 */
public class SerializationException extends RuntimeException {


    public SerializationException() {
        super();
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
