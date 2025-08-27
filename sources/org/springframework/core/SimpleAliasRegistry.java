package org.springframework.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/SimpleAliasRegistry.class */
public class SimpleAliasRegistry implements AliasRegistry {
    private final Map<String, String> aliasMap = new ConcurrentHashMap(16);

    @Override // org.springframework.core.AliasRegistry
    public void registerAlias(String name, String alias) {
        Assert.hasText(name, "'name' must not be empty");
        Assert.hasText(alias, "'alias' must not be empty");
        synchronized (this.aliasMap) {
            if (alias.equals(name)) {
                this.aliasMap.remove(alias);
            } else {
                String registeredName = this.aliasMap.get(alias);
                if (registeredName != null) {
                    if (registeredName.equals(name)) {
                        return;
                    }
                    if (!allowAliasOverriding()) {
                        throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + name + "': It is already registered for name '" + registeredName + "'.");
                    }
                }
                checkForAliasCircle(name, alias);
                this.aliasMap.put(alias, name);
            }
        }
    }

    protected boolean allowAliasOverriding() {
        return true;
    }

    public boolean hasAlias(String name, String alias) {
        for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
            String registeredName = entry.getValue();
            if (registeredName.equals(name)) {
                String registeredAlias = entry.getKey();
                if (registeredAlias.equals(alias) || hasAlias(registeredAlias, alias)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // org.springframework.core.AliasRegistry
    public void removeAlias(String alias) {
        synchronized (this.aliasMap) {
            String name = this.aliasMap.remove(alias);
            if (name == null) {
                throw new IllegalStateException("No alias '" + alias + "' registered");
            }
        }
    }

    @Override // org.springframework.core.AliasRegistry
    public boolean isAlias(String name) {
        return this.aliasMap.containsKey(name);
    }

    @Override // org.springframework.core.AliasRegistry, org.springframework.beans.factory.BeanFactory
    public String[] getAliases(String name) {
        List<String> result = new ArrayList<>();
        synchronized (this.aliasMap) {
            retrieveAliases(name, result);
        }
        return StringUtils.toStringArray(result);
    }

    private void retrieveAliases(String name, List<String> result) {
        for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
            String registeredName = entry.getValue();
            if (registeredName.equals(name)) {
                String alias = entry.getKey();
                result.add(alias);
                retrieveAliases(alias, result);
            }
        }
    }

    public void resolveAliases(StringValueResolver valueResolver) {
        Assert.notNull(valueResolver, "StringValueResolver must not be null");
        synchronized (this.aliasMap) {
            Map<String, String> aliasCopy = new HashMap<>(this.aliasMap);
            Iterator<String> it = aliasCopy.keySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String alias = it.next();
                String registeredName = aliasCopy.get(alias);
                String resolvedAlias = valueResolver.resolveStringValue(alias);
                String resolvedName = valueResolver.resolveStringValue(registeredName);
                if (resolvedAlias == null || resolvedName == null || resolvedAlias.equals(resolvedName)) {
                    this.aliasMap.remove(alias);
                } else if (!resolvedAlias.equals(alias)) {
                    String existingName = this.aliasMap.get(resolvedAlias);
                    if (existingName != null) {
                        if (existingName.equals(resolvedName)) {
                            this.aliasMap.remove(alias);
                        } else {
                            throw new IllegalStateException("Cannot register resolved alias '" + resolvedAlias + "' (original: '" + alias + "') for name '" + resolvedName + "': It is already registered for name '" + registeredName + "'.");
                        }
                    } else {
                        checkForAliasCircle(resolvedName, resolvedAlias);
                        this.aliasMap.remove(alias);
                        this.aliasMap.put(resolvedAlias, resolvedName);
                    }
                } else if (!registeredName.equals(resolvedName)) {
                    this.aliasMap.put(alias, resolvedName);
                }
            }
        }
    }

    protected void checkForAliasCircle(String name, String alias) {
        if (hasAlias(alias, name)) {
            throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + name + "': Circular reference - '" + name + "' is a direct or indirect alias for '" + alias + "' already");
        }
    }

    public String canonicalName(String name) {
        String resolvedName;
        String canonicalName = name;
        do {
            resolvedName = this.aliasMap.get(canonicalName);
            if (resolvedName != null) {
                canonicalName = resolvedName;
            }
        } while (resolvedName != null);
        return canonicalName;
    }
}
