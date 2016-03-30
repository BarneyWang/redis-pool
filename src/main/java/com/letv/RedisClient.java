package com.letv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.letv.serializer.Jackson2JsonSerializer;
import com.letv.serializer.SerializationUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisClusterException;

import java.util.*;

/**
 * Created by wangdi on 16-3-29.
 */
public class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    private Jackson2JsonSerializer serializer = new Jackson2JsonSerializer();

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
    public RedisClient(String cluster, String prefix) {
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

    public String set(final String key, final Object value) {
        return this.jc.set(getKey(key), serializer.serialize(value));
    }

    public String set(final String key, final int expTime, final Object value) {
        return this.jc.setex(getKey(key), expTime, serializer.serialize(value));
    }

    public String set(final String key, final Object value, final String nxxx, final String expx, final long time) {
        return this.jc.set(getKey(key), serializer.serialize(value), nxxx.getBytes(), expx.getBytes(), time);
    }

    public <T> T get(final String key, Class<T> javaType) {
        return serializer.deserialize(this.jc.get(getKey(key)), javaType);
    }

    public <T> T get(final String key, TypeReference<T> tr) {
        byte[] value = this.jc.get(getKey(key));
        return serializer.deserialize(value, tr);
    }

    public String get(final String key) {
        return serializer.deserialize(this.jc.get(getKey(key)), String.class);
    }

    public long delete(final String key) {
        return this.jc.del(getKey(key));
    }



    public byte[] hget(final String key, final String field) {
        return this.jc.hget(getKey(key), getKey(field));
    }

    public long hincrBy(final String key, final String field, final long value) {
        return this.jc.hincrBy(getKey(key), getKey(field), value);
    }

    public long incrBy(final String key, final long delta) {
        return this.jc.incrBy(getKey(key), delta);
    }

    public long expire(final String key, final int seconds) {
        return this.jc.expire(getKey(key), seconds);
    }

    public long rpush(final String key, final Object value) {
        return this.jc.rpush(getKey(key), serializer.serialize(value));
    }

    public <T> T lpop(final String key, Class<T> javaType) {
        return serializer.deserialize(this.jc.lpop(getKey(key)), javaType);
    }

    public <T> T blpop(final String key, final int timeout, Class<T> javaType) {
        List<byte[]> list = this.jc.blpop(timeout, getKey(key));
        return CollectionUtils.isEmpty(list) ? null : serializer.deserialize(list.get(1), javaType);
    }

    public <T> List<T> lrange(final String key, int start, int end) {
        List<byte[]> list = this.jc.lrange(getKey(key), start, end);

        return (List)deserializeValues(list, List.class);
    }

    private <T extends Collection<?>> T deserializeValues(Collection<byte[]> rawValues, Class<T> type) {
        if(rawValues == null) {
            return null;
        } else {
            Object values = List.class.isAssignableFrom(type)?new ArrayList(rawValues.size()):new LinkedHashSet(rawValues.size());
            Iterator i$ = rawValues.iterator();

            while(i$.hasNext()) {
                byte[] bs = (byte[])i$.next();
                ((Collection)values).add(serializer.deserialize(bs, Object.class));
            }

            return (T) values;
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

    private boolean hasPrefix() {
        return this.prefix != null && this.prefix.length > 0;
    }


}
