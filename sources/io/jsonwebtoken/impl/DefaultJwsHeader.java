package io.jsonwebtoken.impl;

import io.jsonwebtoken.JwsHeader;
import java.util.Map;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/DefaultJwsHeader.class */
public class DefaultJwsHeader extends DefaultHeader implements JwsHeader {
    public DefaultJwsHeader() {
    }

    public DefaultJwsHeader(Map<String, Object> map) {
        super(map);
    }

    @Override // io.jsonwebtoken.JwsHeader
    public String getAlgorithm() {
        return getString(JwsHeader.ALGORITHM);
    }

    @Override // io.jsonwebtoken.JwsHeader
    public JwsHeader setAlgorithm(String alg) {
        setValue(JwsHeader.ALGORITHM, alg);
        return this;
    }

    @Override // io.jsonwebtoken.JwsHeader
    public String getKeyId() {
        return getString(JwsHeader.KEY_ID);
    }

    @Override // io.jsonwebtoken.JwsHeader
    public JwsHeader setKeyId(String kid) {
        setValue(JwsHeader.KEY_ID, kid);
        return this;
    }
}
