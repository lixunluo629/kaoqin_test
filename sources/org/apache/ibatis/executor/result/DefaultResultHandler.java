package org.apache.ibatis.executor.result;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/result/DefaultResultHandler.class */
public class DefaultResultHandler implements ResultHandler<Object> {
    private final List<Object> list;

    public DefaultResultHandler() {
        this.list = new ArrayList();
    }

    public DefaultResultHandler(ObjectFactory objectFactory) {
        this.list = (List) objectFactory.create(List.class);
    }

    @Override // org.apache.ibatis.session.ResultHandler
    public void handleResult(ResultContext<? extends Object> context) {
        this.list.add(context.getResultObject());
    }

    public List<Object> getResultList() {
        return this.list;
    }
}
