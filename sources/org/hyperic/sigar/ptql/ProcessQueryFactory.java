package org.hyperic.sigar.ptql;

import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ptql/ProcessQueryFactory.class */
public class ProcessQueryFactory {
    private static ProcessQueryFactory instance = null;
    private Map cache = new HashMap();

    public void clear() {
        for (SigarProcessQuery query : this.cache.values()) {
            query.destroy();
        }
        this.cache.clear();
    }

    public static ProcessQueryFactory getInstance() {
        if (instance == null) {
            instance = new ProcessQueryFactory();
        }
        return instance;
    }

    public ProcessQuery getQuery(String query) throws MalformedQueryException {
        if (query == null) {
            throw new MalformedQueryException("null query");
        }
        if (query.length() == 0) {
            throw new MalformedQueryException("empty query");
        }
        ProcessQuery pQuery = (ProcessQuery) this.cache.get(query);
        if (pQuery != null) {
            return pQuery;
        }
        SigarProcessQuery sigarProcessQuery = new SigarProcessQuery();
        sigarProcessQuery.create(query);
        this.cache.put(query, sigarProcessQuery);
        return sigarProcessQuery;
    }

    public static ProcessQuery getInstance(String query) throws MalformedQueryException {
        return getInstance().getQuery(query);
    }
}
