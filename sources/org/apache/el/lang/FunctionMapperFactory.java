package org.apache.el.lang;

import java.lang.reflect.Method;
import javax.el.FunctionMapper;

/* loaded from: tomcat-embed-el-8.5.43.jar:org/apache/el/lang/FunctionMapperFactory.class */
public class FunctionMapperFactory extends FunctionMapper {
    protected FunctionMapperImpl memento = null;
    protected final FunctionMapper target;

    public FunctionMapperFactory(FunctionMapper mapper) {
        if (mapper == null) {
            throw new NullPointerException("FunctionMapper target cannot be null");
        }
        this.target = mapper;
    }

    @Override // javax.el.FunctionMapper
    public Method resolveFunction(String prefix, String localName) {
        if (this.memento == null) {
            this.memento = new FunctionMapperImpl();
        }
        Method m = this.target.resolveFunction(prefix, localName);
        if (m != null) {
            this.memento.mapFunction(prefix, localName, m);
        }
        return m;
    }

    @Override // javax.el.FunctionMapper
    public void mapFunction(String prefix, String localName, Method method) {
        if (this.memento == null) {
            this.memento = new FunctionMapperImpl();
        }
        this.memento.mapFunction(prefix, localName, method);
    }

    public FunctionMapper create() {
        return this.memento;
    }
}
