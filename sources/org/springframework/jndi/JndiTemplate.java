package org.springframework.jndi;

import java.util.Hashtable;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jndi/JndiTemplate.class */
public class JndiTemplate {
    protected final Log logger = LogFactory.getLog(getClass());
    private Properties environment;

    public JndiTemplate() {
    }

    public JndiTemplate(Properties environment) {
        this.environment = environment;
    }

    public void setEnvironment(Properties environment) {
        this.environment = environment;
    }

    public Properties getEnvironment() {
        return this.environment;
    }

    public <T> T execute(JndiCallback<T> contextCallback) throws NamingException {
        Context ctx = getContext();
        try {
            T tDoInContext = contextCallback.doInContext(ctx);
            releaseContext(ctx);
            return tDoInContext;
        } catch (Throwable th) {
            releaseContext(ctx);
            throw th;
        }
    }

    public Context getContext() throws NamingException {
        return createInitialContext();
    }

    public void releaseContext(Context ctx) {
        if (ctx != null) {
            try {
                ctx.close();
            } catch (NamingException e) {
                this.logger.debug("Could not close JNDI InitialContext", e);
            }
        }
    }

    protected Context createInitialContext() throws NamingException {
        Hashtable<?, ?> icEnv = null;
        Properties env = getEnvironment();
        if (env != null) {
            icEnv = new Hashtable<>(env.size());
            CollectionUtils.mergePropertiesIntoMap(env, icEnv);
        }
        return new InitialContext(icEnv);
    }

    public Object lookup(final String name) throws NamingException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Looking up JNDI object with name [" + name + "]");
        }
        return execute(new JndiCallback<Object>() { // from class: org.springframework.jndi.JndiTemplate.1
            /* JADX INFO: Thrown type has an unknown type hierarchy: javax.naming.NameNotFoundException */
            @Override // org.springframework.jndi.JndiCallback
            public Object doInContext(Context ctx) throws NamingException, NameNotFoundException {
                Object located = ctx.lookup(name);
                if (located == null) {
                    throw new NameNotFoundException("JNDI object with [" + name + "] not found: JNDI implementation returned null");
                }
                return located;
            }
        });
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.naming.NamingException */
    public <T> T lookup(String str, Class<T> cls) throws NamingException {
        T t = (T) lookup(str);
        if (cls != null && !cls.isInstance(t)) {
            throw new TypeMismatchNamingException(str, cls, t != null ? t.getClass() : null);
        }
        return t;
    }

    public void bind(final String name, final Object object) throws NamingException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Binding JNDI object with name [" + name + "]");
        }
        execute(new JndiCallback<Object>() { // from class: org.springframework.jndi.JndiTemplate.2
            @Override // org.springframework.jndi.JndiCallback
            public Object doInContext(Context ctx) throws NamingException {
                ctx.bind(name, object);
                return null;
            }
        });
    }

    public void rebind(final String name, final Object object) throws NamingException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Rebinding JNDI object with name [" + name + "]");
        }
        execute(new JndiCallback<Object>() { // from class: org.springframework.jndi.JndiTemplate.3
            @Override // org.springframework.jndi.JndiCallback
            public Object doInContext(Context ctx) throws NamingException {
                ctx.rebind(name, object);
                return null;
            }
        });
    }

    public void unbind(final String name) throws NamingException {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Unbinding JNDI object with name [" + name + "]");
        }
        execute(new JndiCallback<Object>() { // from class: org.springframework.jndi.JndiTemplate.4
            @Override // org.springframework.jndi.JndiCallback
            public Object doInContext(Context ctx) throws NamingException {
                ctx.unbind(name);
                return null;
            }
        });
    }
}
