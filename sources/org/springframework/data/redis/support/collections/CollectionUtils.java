package org.springframework.data.redis.support.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/CollectionUtils.class */
abstract class CollectionUtils {
    CollectionUtils() {
    }

    static <E> Collection<E> reverse(Collection<? extends E> c) {
        Object[] reverse = new Object[c.size()];
        int index = c.size();
        for (E e : c) {
            index--;
            reverse[index] = e;
        }
        return Arrays.asList(reverse);
    }

    static Collection<String> extractKeys(Collection<? extends RedisStore> stores) {
        Collection<String> keys = new ArrayList<>(stores.size());
        for (RedisStore store : stores) {
            keys.add(store.getKey());
        }
        return keys;
    }

    static <K> void rename(final K key, final K newKey, RedisOperations<K, ?> operations) {
        operations.execute(new SessionCallback<Object>() { // from class: org.springframework.data.redis.support.collections.CollectionUtils.1
            @Override // org.springframework.data.redis.core.SessionCallback
            public Object execute(RedisOperations operations2) throws DataAccessException {
                do {
                    operations2.watch((RedisOperations) key);
                    if (operations2.hasKey(key).booleanValue()) {
                        operations2.multi();
                        operations2.rename(key, newKey);
                    } else {
                        operations2.multi();
                    }
                } while (operations2.exec() == null);
                return null;
            }
        });
    }

    static <K> Boolean renameIfAbsent(final K key, final K newKey, RedisOperations<K, ?> operations) {
        return (Boolean) operations.execute(new SessionCallback<Boolean>() { // from class: org.springframework.data.redis.support.collections.CollectionUtils.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.SessionCallback
            public Boolean execute(RedisOperations operations2) throws DataAccessException {
                List<Object> exec;
                do {
                    operations2.watch((RedisOperations) key);
                    if (operations2.hasKey(key).booleanValue()) {
                        operations2.multi();
                        operations2.renameIfAbsent(key, newKey);
                    } else {
                        operations2.watch((RedisOperations) newKey);
                        operations2.multi();
                        operations2.hasKey(newKey);
                        operations2.hasKey(newKey);
                    }
                    exec = operations2.exec();
                } while (exec == null);
                boolean result = ((Long) exec.get(0)).longValue() == 1;
                if (exec.size() > 1) {
                    result = !result;
                }
                return Boolean.valueOf(result);
            }
        });
    }
}
