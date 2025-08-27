package com.mysql.jdbc;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.util.LRUCache;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/PerConnectionLRUFactory.class */
public class PerConnectionLRUFactory implements CacheAdapterFactory<String, PreparedStatement.ParseInfo> {
    @Override // com.mysql.jdbc.CacheAdapterFactory
    public CacheAdapter<String, PreparedStatement.ParseInfo> getInstance(Connection forConnection, String url, int cacheMaxSize, int maxKeySize, Properties connectionProperties) throws SQLException {
        return new PerConnectionLRU(forConnection, cacheMaxSize, maxKeySize);
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/PerConnectionLRUFactory$PerConnectionLRU.class */
    class PerConnectionLRU implements CacheAdapter<String, PreparedStatement.ParseInfo> {
        private final int cacheSqlLimit;
        private final LRUCache<String, PreparedStatement.ParseInfo> cache;
        private final Connection conn;

        protected PerConnectionLRU(Connection forConnection, int cacheMaxSize, int maxKeySize) {
            this.cacheSqlLimit = maxKeySize;
            this.cache = new LRUCache<>(cacheMaxSize);
            this.conn = forConnection;
        }

        @Override // com.mysql.jdbc.CacheAdapter
        public PreparedStatement.ParseInfo get(String key) {
            PreparedStatement.ParseInfo parseInfo;
            if (key == null || key.length() > this.cacheSqlLimit) {
                return null;
            }
            synchronized (this.conn.getConnectionMutex()) {
                parseInfo = this.cache.get(key);
            }
            return parseInfo;
        }

        @Override // com.mysql.jdbc.CacheAdapter
        public void put(String key, PreparedStatement.ParseInfo value) {
            if (key == null || key.length() > this.cacheSqlLimit) {
                return;
            }
            synchronized (this.conn.getConnectionMutex()) {
                this.cache.put(key, value);
            }
        }

        @Override // com.mysql.jdbc.CacheAdapter
        public void invalidate(String key) {
            synchronized (this.conn.getConnectionMutex()) {
                this.cache.remove(key);
            }
        }

        @Override // com.mysql.jdbc.CacheAdapter
        public void invalidateAll(Set<String> keys) {
            synchronized (this.conn.getConnectionMutex()) {
                for (String key : keys) {
                    this.cache.remove(key);
                }
            }
        }

        @Override // com.mysql.jdbc.CacheAdapter
        public void invalidateAll() {
            synchronized (this.conn.getConnectionMutex()) {
                this.cache.clear();
            }
        }
    }
}
