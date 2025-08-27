package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/PerVmServerConfigCacheFactory.class */
public class PerVmServerConfigCacheFactory implements CacheAdapterFactory<String, Map<String, String>> {
    static final ConcurrentHashMap<String, Map<String, String>> serverConfigByUrl = new ConcurrentHashMap<>();
    private static final CacheAdapter<String, Map<String, String>> serverConfigCache = new CacheAdapter<String, Map<String, String>>() { // from class: com.mysql.jdbc.PerVmServerConfigCacheFactory.1
        @Override // com.mysql.jdbc.CacheAdapter
        public Map<String, String> get(String key) {
            return PerVmServerConfigCacheFactory.serverConfigByUrl.get(key);
        }

        @Override // com.mysql.jdbc.CacheAdapter
        public void put(String key, Map<String, String> value) {
            PerVmServerConfigCacheFactory.serverConfigByUrl.putIfAbsent(key, value);
        }

        @Override // com.mysql.jdbc.CacheAdapter
        public void invalidate(String key) {
            PerVmServerConfigCacheFactory.serverConfigByUrl.remove(key);
        }

        @Override // com.mysql.jdbc.CacheAdapter
        public void invalidateAll(Set<String> keys) {
            for (String key : keys) {
                PerVmServerConfigCacheFactory.serverConfigByUrl.remove(key);
            }
        }

        @Override // com.mysql.jdbc.CacheAdapter
        public void invalidateAll() {
            PerVmServerConfigCacheFactory.serverConfigByUrl.clear();
        }
    };

    @Override // com.mysql.jdbc.CacheAdapterFactory
    public CacheAdapter<String, Map<String, String>> getInstance(Connection forConn, String url, int cacheMaxSize, int maxKeySize, Properties connectionProperties) throws SQLException {
        return serverConfigCache;
    }
}
