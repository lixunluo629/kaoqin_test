package org.apache.ibatis.executor.result;

import java.util.Map;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/result/DefaultMapResultHandler.class */
public class DefaultMapResultHandler<K, V> implements ResultHandler<V> {
    private final Map<K, V> mappedResults;
    private final String mapKey;
    private final ObjectFactory objectFactory;
    private final ObjectWrapperFactory objectWrapperFactory;
    private final ReflectorFactory reflectorFactory;

    public DefaultMapResultHandler(String mapKey, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory, ReflectorFactory reflectorFactory) {
        this.objectFactory = objectFactory;
        this.objectWrapperFactory = objectWrapperFactory;
        this.reflectorFactory = reflectorFactory;
        this.mappedResults = (Map) objectFactory.create(Map.class);
        this.mapKey = mapKey;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.ibatis.session.ResultHandler
    public void handleResult(ResultContext<? extends V> resultContext) {
        V resultObject = resultContext.getResultObject();
        this.mappedResults.put(MetaObject.forObject(resultObject, this.objectFactory, this.objectWrapperFactory, this.reflectorFactory).getValue(this.mapKey), resultObject);
    }

    public Map<K, V> getMappedResults() {
        return this.mappedResults;
    }
}
