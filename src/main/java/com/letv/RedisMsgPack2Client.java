package com.letv;

import com.letv.msgpack.ObjectTemplate;
import com.letv.serializer.Msgpack2JsonSerializer;
import com.letv.serializer.SerializationUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.msgpack.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisClusterException;

import java.util.*;

/**
 * Created by bojack on 16/5/17.
 */
public class RedisMsgPack2Client {

    private static final Logger logger = LoggerFactory.getLogger(RedisMsgPack2Client.class);

    private Msgpack2JsonSerializer serializer = new Msgpack2JsonSerializer();

    private static final int DEFAULT_MAX_TOTAL = 1024;

    private static final int DEFAULT_MAX_IDLE = 30;

    private static final int DEFAULT_MIN_IDLE = 0;

    private static final int CONNECTION_TIMEOUT = 3000;

    private static final int MAX_REDIRECTIONS = 6;

    private JedisCluster jc;

    private byte[] prefix;

    /**
     * cluster格式为ip:port,ip:port
     * @param cluster
     */
    public RedisMsgPack2Client(String cluster, String prefix) {
        try {
            if(cluster == null || cluster.trim().length() == 0) {
                throw new JedisClusterException("cluster str is null.");
            }

            if(prefix == null || prefix.trim().length() == 0) {
                throw new JedisClusterException("prefix is null.");
            }

            String[] hostAndPort = cluster.split(",");
            Set<HostAndPort> jedisClusterNodesSet = new HashSet<HostAndPort>();
            for(String hp : hostAndPort) {
                String[] hap = hp.split(":");
                jedisClusterNodesSet.add(new HostAndPort(hap[0], Integer.parseInt(hap[1])));
            }
            GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
            genericObjectPoolConfig.setMaxIdle(DEFAULT_MAX_IDLE);
            genericObjectPoolConfig.setMaxTotal(DEFAULT_MAX_TOTAL);
            genericObjectPoolConfig.setMinIdle(DEFAULT_MIN_IDLE);
            jc = new JedisCluster(jedisClusterNodesSet, CONNECTION_TIMEOUT, MAX_REDIRECTIONS, genericObjectPoolConfig);
            this.prefix = getKey(prefix);
        } catch (Exception e) {
            logger.error("redis client init error", e);
        }
    }

    private byte[] getKey(String key) {
        byte[] rawKey = SerializationUtils.encode(key);
        if(!this.hasPrefix()) {
            return rawKey;
        } else {
            byte[] prefixedKey = Arrays.copyOf(this.prefix, this.prefix.length + rawKey.length);
            System.arraycopy(rawKey, 0, prefixedKey, this.prefix.length, rawKey.length);
            return prefixedKey;
        }
    }


    private byte[] getField(String field) {
        byte[] rawKey = SerializationUtils.encode(field);
        return rawKey;
    }

    private boolean hasPrefix() {
        return this.prefix != null && this.prefix.length > 0;
    }


    /**
     * String set
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final Object value) {
        return this.jc.set(getKey(key), serializer.serialize(value));
    }

    /**
     * String set
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final int expTime, final Object value) {
        return this.jc.setex(getKey(key), expTime, serializer.serialize(value));
    }


    /**
     *
     * String set
     * 存储数据到缓存中，并制定过期时间和当Key存在时是否覆盖。
     *
     * @param key
     * @param value
     * @param nxxx
     *            nxxx的值只能取NX或者XX，如果取NX，则只有当key不存在是才进行set，如果取XX，则只有当key已经存在时才进行set
     *
     * @param expx expx的值只能取EX或者PX，代表数据过期时间的单位，EX代表秒，PX代表毫秒。
     * @param time 过期时间，单位是expx所代表的单位。
     * @return
     */
    public String set(final String key, final Object value, final String nxxx, final String expx, final long time) {
        return this.jc.set(getKey(key), serializer.serialize(value), nxxx.getBytes(), expx.getBytes(), time);
    }

    /**
     * string get
     * @param key
     * @param javaType
     * @param <T>
     * @return
     */
    public <T> T get(final String key, Class<T> javaType) {
        return serializer.deserialize(this.jc.get(getKey(key)), javaType);
    }

    /**
     * string get
     * @param key
     * @param template
     * @param <T>
     * @return
     */
    public <T> T get(final String key, Template<T> template) {
        byte[] value = this.jc.get(getKey(key));
        return serializer.deserialize(value, template);
    }

    /**
     * 删除
     * @param key
     * @return
     */
    public long delete(final String key) {
        return this.jc.del(getKey(key));
    }

    /**
     * 失效
     * @param key
     * @param seconds
     * @return
     */
    public long expire(final String key, final int seconds) {
        return this.jc.expire(getKey(key), seconds);
    }



    /**
     * increase
     * @param key
     * @param delta
     * @return
     */
    public long incrBy(final String key, final long delta) {
        return this.jc.incrBy(getKey(key), delta);
    }

    /**
     *  list rpush操作
     *
     * @param key
     * @param value
     * @return
     */
    public long rpush(final String key, final Object value) {
        return this.jc.rpush(getKey(key), serializer.serialize(value));
    }


    /**
     * list lrange
     * @param key
     * @param start
     * @param end
     * @param <T>
     * @return
     */
    public <T> List<T> lrange(final String key, int start, int end) {
        List<byte[]> list = this.jc.lrange(getKey(key), start, end);
        return (List)deserializeValues(list);
    }


    /**
     * list lpop
     * @param key
     * @param tTemplate
     * @param <T>
     * @return
     */
    public <T> T lpop(final String key,Template<T> tTemplate){
        return serializer.deserialize(this.jc.lpop(getKey(key)),tTemplate);

    }

    /**
     * list rpop
     * @param key
     * @param tTemplate
     * @param <T>
     * @return
     */
    public <T> T rpop(final String key,Template<T> tTemplate){
        return serializer.deserialize(this.jc.rpop(getKey(key)),tTemplate);

    }


    /**
     * hset
     * @param key
     * @param field
     * @param value
     * @return
     */
    public  Long hset(final String key,final String field,final String value){
        return this.jc.hset(getKey(key),getField(field), serializer.serialize(value));
    }


    /**
     * hset
     * @param key
     * @param field
     * @param value
     * @return
     */
    public  Long hsetnx(final String key,final String field,final String value){
        return this.jc.hsetnx(getKey(key),getField(field),serializer.serialize(value));
    }


    /**
     * hget
     *
     * @param key
     * @param field
     * @param <T>
     * @return
     */
    public <T> T hget(final String key,final String field,Template<T> tTemplate){
        return  serializer.deserialize(this.jc.hget(getKey(key),getField(field)),tTemplate);
    }


    /**
     *  set add
     *
     * @param key
     * @param values
     * @return
     */
    public Long sadd(final String key,final String... values){

        byte[][] bs = new byte[values.length][];
        for (int i = 0; i < bs.length; i++) {
            bs[i]=serializer.serialize(values[i]);
        }
        return this.jc.sadd(this.getKey(key),bs);
    }


    /**
     * set memebers
     * @param key
     * @return
     */
    public Set<String> smembers(final String key){
        return  this.jc.smembers(key);
    }


    /**
     * 反序列化 msg
     * @param rawValues
     * @param <T>
     * @return
     */
    private <T extends Collection<?>> T deserializeValues(Collection<byte[]> rawValues) {
        if(rawValues == null) {
            return null;
        } else {
            Object values = new ArrayList<T>(rawValues.size());
            Iterator $i= rawValues.iterator();
            while($i.hasNext()) {
                byte[] bs = (byte[])$i.next();
                ((Collection)values).add(serializer.deserialize(bs,ObjectTemplate.OBJECT_TEMPLATE));
            }
            return (T) values;
        }
    }







}
