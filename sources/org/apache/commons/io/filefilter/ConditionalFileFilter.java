package org.apache.commons.io.filefilter;

import java.util.List;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/filefilter/ConditionalFileFilter.class */
public interface ConditionalFileFilter {
    void addFileFilter(IOFileFilter iOFileFilter);

    List<IOFileFilter> getFileFilters();

    boolean removeFileFilter(IOFileFilter iOFileFilter);

    void setFileFilters(List<IOFileFilter> list);
}
