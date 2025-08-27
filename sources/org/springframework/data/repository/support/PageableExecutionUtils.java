package org.springframework.data.repository.support;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/PageableExecutionUtils.class */
public abstract class PageableExecutionUtils {

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/PageableExecutionUtils$TotalSupplier.class */
    public interface TotalSupplier {
        long get();
    }

    private PageableExecutionUtils() {
    }

    public static <T> Page<T> getPage(List<T> content, Pageable pageable, TotalSupplier totalSupplier) {
        Assert.notNull(content, "Content must not be null!");
        Assert.notNull(totalSupplier, "TotalSupplier must not be null!");
        if (pageable == null || pageable.getOffset() == 0) {
            if (pageable == null || pageable.getPageSize() > content.size()) {
                return new PageImpl(content, pageable, content.size());
            }
            return new PageImpl(content, pageable, totalSupplier.get());
        }
        if (content.size() != 0 && pageable.getPageSize() > content.size()) {
            return new PageImpl(content, pageable, pageable.getOffset() + content.size());
        }
        return new PageImpl(content, pageable, totalSupplier.get());
    }
}
