package org.ehcache.sizeof.impl;

import org.ehcache.sizeof.SizeOf;
import org.ehcache.sizeof.filters.SizeOfFilter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/impl/AgentSizeOf.class */
public class AgentSizeOf extends SizeOf {
    public static final String BYPASS_LOADING = "org.ehcache.sizeof.AgentSizeOf.bypass";
    private static final boolean AGENT_LOADED;

    static {
        AGENT_LOADED = !Boolean.getBoolean(BYPASS_LOADING) && AgentLoader.loadAgent();
    }

    public AgentSizeOf() throws UnsupportedOperationException {
        this(new PassThroughFilter());
    }

    public AgentSizeOf(SizeOfFilter filter) throws UnsupportedOperationException {
        this(filter, true, true);
    }

    public AgentSizeOf(SizeOfFilter filter, boolean caching, boolean bypassFlyweight) throws UnsupportedOperationException {
        super(filter, caching, bypassFlyweight);
        if (!AGENT_LOADED) {
            throw new UnsupportedOperationException("Agent not available or loadable");
        }
    }

    @Override // org.ehcache.sizeof.SizeOf
    public long sizeOf(Object obj) {
        long measuredSize = AgentLoader.agentSizeOf(obj);
        return Math.max(JvmInformation.CURRENT_JVM_INFORMATION.getMinimumObjectSize(), measuredSize + JvmInformation.CURRENT_JVM_INFORMATION.getAgentSizeOfAdjustment());
    }
}
