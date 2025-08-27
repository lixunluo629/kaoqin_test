package org.springframework.data.redis.core.script;

import java.io.IOException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/script/DefaultRedisScript.class */
public class DefaultRedisScript<T> implements RedisScript<T>, InitializingBean {
    private ScriptSource scriptSource;
    private String sha1;
    private Class<T> resultType;
    private final Object shaModifiedMonitor = new Object();

    public DefaultRedisScript() {
    }

    public DefaultRedisScript(String script, Class<T> resultType) {
        setScriptText(script);
        this.resultType = resultType;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.scriptSource, "Either script, script location, or script source is required");
    }

    @Override // org.springframework.data.redis.core.script.RedisScript
    public String getSha1() {
        String str;
        synchronized (this.shaModifiedMonitor) {
            if (this.sha1 == null || this.scriptSource.isModified()) {
                this.sha1 = DigestUtils.sha1DigestAsHex(getScriptAsString());
            }
            str = this.sha1;
        }
        return str;
    }

    @Override // org.springframework.data.redis.core.script.RedisScript
    public Class<T> getResultType() {
        return this.resultType;
    }

    @Override // org.springframework.data.redis.core.script.RedisScript
    public String getScriptAsString() {
        try {
            return this.scriptSource.getScriptAsString();
        } catch (IOException e) {
            throw new ScriptingException("Error reading script text", e);
        }
    }

    public void setResultType(Class<T> resultType) {
        this.resultType = resultType;
    }

    public void setScriptText(String scriptText) {
        this.scriptSource = new StaticScriptSource(scriptText);
    }

    public void setLocation(Resource scriptLocation) {
        this.scriptSource = new ResourceScriptSource(scriptLocation);
    }

    public void setScriptSource(ScriptSource scriptSource) {
        this.scriptSource = scriptSource;
    }
}
