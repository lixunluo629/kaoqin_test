package org.springframework.data.redis.core;

import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ScanOptions.class */
public class ScanOptions {
    public static ScanOptions NONE = new ScanOptions();
    private Long count;
    private String pattern;

    private ScanOptions() {
    }

    public static ScanOptionsBuilder scanOptions() {
        return new ScanOptionsBuilder();
    }

    public Long getCount() {
        return this.count;
    }

    public String getPattern() {
        return this.pattern;
    }

    public String toOptionString() {
        if (equals(NONE)) {
            return "";
        }
        String params = "";
        if (this.count != null) {
            params = params + ", 'count', " + this.count;
        }
        if (StringUtils.hasText(this.pattern)) {
            params = params + ", 'match' , '" + this.pattern + "'";
        }
        return params;
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ScanOptions$ScanOptionsBuilder.class */
    public static class ScanOptionsBuilder {
        ScanOptions options = new ScanOptions();

        public ScanOptionsBuilder count(long count) {
            this.options.count = Long.valueOf(count);
            return this;
        }

        public ScanOptionsBuilder match(String pattern) {
            this.options.pattern = pattern;
            return this;
        }

        public ScanOptions build() {
            return this.options;
        }
    }
}
