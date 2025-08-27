package io.netty.handler.codec.mqtt;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/mqtt/MqttSubAckPayload.class */
public class MqttSubAckPayload {
    private final List<Integer> grantedQoSLevels;

    public MqttSubAckPayload(int... grantedQoSLevels) {
        ObjectUtil.checkNotNull(grantedQoSLevels, "grantedQoSLevels");
        List<Integer> list = new ArrayList<>(grantedQoSLevels.length);
        for (int v : grantedQoSLevels) {
            list.add(Integer.valueOf(v));
        }
        this.grantedQoSLevels = Collections.unmodifiableList(list);
    }

    public MqttSubAckPayload(Iterable<Integer> grantedQoSLevels) {
        Integer v;
        ObjectUtil.checkNotNull(grantedQoSLevels, "grantedQoSLevels");
        List<Integer> list = new ArrayList<>();
        Iterator<Integer> it = grantedQoSLevels.iterator();
        while (it.hasNext() && (v = it.next()) != null) {
            list.add(v);
        }
        this.grantedQoSLevels = Collections.unmodifiableList(list);
    }

    public List<Integer> grantedQoSLevels() {
        return this.grantedQoSLevels;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[grantedQoSLevels=" + this.grantedQoSLevels + ']';
    }
}
