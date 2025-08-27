package com.mysql.fabric;

import java.util.concurrent.TimeUnit;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/FabricStateResponse.class */
public class FabricStateResponse<T> {
    private T data;
    private int secsTtl;
    private long expireTimeMillis;

    public FabricStateResponse(T data, int secsTtl) {
        this.data = data;
        this.secsTtl = secsTtl;
        this.expireTimeMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(secsTtl);
    }

    public FabricStateResponse(T data, int secsTtl, long presetExpireTimeMillis) {
        this.data = data;
        this.secsTtl = secsTtl;
        this.expireTimeMillis = presetExpireTimeMillis;
    }

    public T getData() {
        return this.data;
    }

    public int getTtl() {
        return this.secsTtl;
    }

    public long getExpireTimeMillis() {
        return this.expireTimeMillis;
    }
}
