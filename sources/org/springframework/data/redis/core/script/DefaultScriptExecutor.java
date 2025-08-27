package org.springframework.data.redis.core.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/script/DefaultScriptExecutor.class */
public class DefaultScriptExecutor<K> implements ScriptExecutor<K> {
    private RedisTemplate<K, ?> template;

    public DefaultScriptExecutor(RedisTemplate<K, ?> template) {
        this.template = template;
    }

    @Override // org.springframework.data.redis.core.script.ScriptExecutor
    public <T> T execute(RedisScript<T> redisScript, List<K> list, Object... objArr) {
        return (T) execute(redisScript, this.template.getValueSerializer(), this.template.getValueSerializer(), list, objArr);
    }

    @Override // org.springframework.data.redis.core.script.ScriptExecutor
    public <T> T execute(final RedisScript<T> redisScript, final RedisSerializer<?> redisSerializer, final RedisSerializer<T> redisSerializer2, final List<K> list, final Object... objArr) {
        return (T) this.template.execute(new RedisCallback<T>() { // from class: org.springframework.data.redis.core.script.DefaultScriptExecutor.1
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public T doInRedis2(RedisConnection redisConnection) throws DataAccessException {
                ReturnType returnTypeFromJavaType = ReturnType.fromJavaType(redisScript.getResultType());
                byte[][] bArrKeysAndArgs = DefaultScriptExecutor.this.keysAndArgs(redisSerializer, list, objArr);
                int size = list != null ? list.size() : 0;
                if (redisConnection.isPipelined() || redisConnection.isQueueing()) {
                    redisConnection.eval(DefaultScriptExecutor.this.scriptBytes(redisScript), returnTypeFromJavaType, size, bArrKeysAndArgs);
                    return null;
                }
                return (T) DefaultScriptExecutor.this.eval(redisConnection, redisScript, returnTypeFromJavaType, size, bArrKeysAndArgs, redisSerializer2);
            }
        });
    }

    protected <T> T eval(RedisConnection redisConnection, RedisScript<T> redisScript, ReturnType returnType, int i, byte[][] bArr, RedisSerializer<T> redisSerializer) {
        Object objEval;
        try {
            objEval = redisConnection.evalSha(redisScript.getSha1(), returnType, i, bArr);
        } catch (Exception e) {
            if (!exceptionContainsNoScriptError(e)) {
                if (e instanceof RuntimeException) {
                    throw ((RuntimeException) e);
                }
                throw new RedisSystemException(e.getMessage(), e);
            }
            objEval = redisConnection.eval(scriptBytes(redisScript), returnType, i, bArr);
        }
        if (redisScript.getResultType() == null) {
            return null;
        }
        return (T) deserializeResult(redisSerializer, objEval);
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [byte[], byte[][]] */
    protected byte[][] keysAndArgs(RedisSerializer redisSerializer, List<K> list, Object[] objArr) {
        ?? r0 = new byte[objArr.length + (list != null ? list.size() : 0)];
        int i = 0;
        if (list != null) {
            for (K k : list) {
                if (keySerializer() == null && (k instanceof byte[])) {
                    int i2 = i;
                    i++;
                    r0[i2] = (byte[]) k;
                } else {
                    int i3 = i;
                    i++;
                    r0[i3] = keySerializer().serialize(k);
                }
            }
        }
        for (Object obj : objArr) {
            if (redisSerializer == null && (obj instanceof byte[])) {
                int i4 = i;
                i++;
                r0[i4] = (byte[]) obj;
            } else {
                int i5 = i;
                i++;
                r0[i5] = redisSerializer.serialize(obj);
            }
        }
        return r0;
    }

    protected byte[] scriptBytes(RedisScript<?> script) {
        return this.template.getStringSerializer().serialize(script.getScriptAsString());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v5, types: [T, java.util.ArrayList, java.util.List] */
    protected <T> T deserializeResult(RedisSerializer<T> redisSerializer, Object obj) {
        if (obj instanceof byte[]) {
            if (redisSerializer == null) {
                return obj;
            }
            return redisSerializer.deserialize((byte[]) obj);
        }
        if (obj instanceof List) {
            ?? r0 = (T) new ArrayList();
            Iterator it = ((List) obj).iterator();
            while (it.hasNext()) {
                r0.add(deserializeResult(redisSerializer, it.next()));
            }
            return r0;
        }
        return obj;
    }

    protected RedisSerializer keySerializer() {
        return this.template.getKeySerializer();
    }

    private boolean exceptionContainsNoScriptError(Exception e) {
        if (!(e instanceof NonTransientDataAccessException)) {
            return false;
        }
        Throwable cause = e;
        while (true) {
            Throwable current = cause;
            if (current != null) {
                String exMessage = current.getMessage();
                if (exMessage != null && exMessage.contains("NOSCRIPT")) {
                    return true;
                }
                cause = current.getCause();
            } else {
                return false;
            }
        }
    }
}
