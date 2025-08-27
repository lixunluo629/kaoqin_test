package org.springframework.data.redis.support.collections;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/RedisCollectionFactoryBean.class */
public class RedisCollectionFactoryBean implements InitializingBean, BeanNameAware, FactoryBean<RedisStore> {
    private RedisStore store;
    private CollectionType type = null;
    private RedisTemplate<String, ?> template;
    private String key;
    private String beanName;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/support/collections/RedisCollectionFactoryBean$CollectionType.class */
    public enum CollectionType {
        LIST { // from class: org.springframework.data.redis.support.collections.RedisCollectionFactoryBean.CollectionType.1
            @Override // org.springframework.data.redis.support.collections.RedisCollectionFactoryBean.CollectionType
            public DataType dataType() {
                return DataType.LIST;
            }
        },
        SET { // from class: org.springframework.data.redis.support.collections.RedisCollectionFactoryBean.CollectionType.2
            @Override // org.springframework.data.redis.support.collections.RedisCollectionFactoryBean.CollectionType
            public DataType dataType() {
                return DataType.SET;
            }
        },
        ZSET { // from class: org.springframework.data.redis.support.collections.RedisCollectionFactoryBean.CollectionType.3
            @Override // org.springframework.data.redis.support.collections.RedisCollectionFactoryBean.CollectionType
            public DataType dataType() {
                return DataType.ZSET;
            }
        },
        MAP { // from class: org.springframework.data.redis.support.collections.RedisCollectionFactoryBean.CollectionType.4
            @Override // org.springframework.data.redis.support.collections.RedisCollectionFactoryBean.CollectionType
            public DataType dataType() {
                return DataType.HASH;
            }
        },
        PROPERTIES { // from class: org.springframework.data.redis.support.collections.RedisCollectionFactoryBean.CollectionType.5
            @Override // org.springframework.data.redis.support.collections.RedisCollectionFactoryBean.CollectionType
            public DataType dataType() {
                return DataType.HASH;
            }
        };

        abstract DataType dataType();
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (!StringUtils.hasText(this.key)) {
            this.key = this.beanName;
        }
        Assert.hasText(this.key, "Collection key is required - no key or bean name specified");
        Assert.notNull(this.template, "Redis template is required");
        DataType dt = this.template.type(this.key);
        Assert.isTrue(!DataType.STRING.equals(dt), "Cannot create store on keys of type 'string'");
        this.store = createStore(dt);
        if (this.store == null) {
            if (this.type == null) {
                this.type = CollectionType.LIST;
            }
            this.store = createStore(this.type.dataType());
        }
    }

    private RedisStore createStore(DataType dt) {
        switch (dt) {
            case LIST:
                return new DefaultRedisList(this.key, this.template);
            case SET:
                return new DefaultRedisSet(this.key, this.template);
            case ZSET:
                return new DefaultRedisZSet(this.key, this.template);
            case HASH:
                if (CollectionType.PROPERTIES.equals(this.type)) {
                    return new RedisProperties(this.key, this.template);
                }
                return new DefaultRedisMap(this.key, this.template);
            default:
                return null;
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public RedisStore getObject() {
        return this.store;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return this.store != null ? this.store.getClass() : RedisStore.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }

    @Override // org.springframework.beans.factory.BeanNameAware
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void setType(CollectionType type) {
        this.type = type;
    }

    public void setTemplate(RedisTemplate<String, ?> template) {
        this.template = template;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
