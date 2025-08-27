package io.netty.handler.ipfilter;

import java.net.InetSocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ipfilter/IpFilterRule.class */
public interface IpFilterRule {
    boolean matches(InetSocketAddress inetSocketAddress);

    IpFilterRuleType ruleType();
}
