package org.springframework.data.repository.query.parser;

import java.util.Iterator;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/parser/AbstractQueryCreator.class */
public abstract class AbstractQueryCreator<T, S> {
    private final ParameterAccessor parameters;
    private final PartTree tree;

    protected abstract S create(Part part, Iterator<Object> it);

    protected abstract S and(Part part, S s, Iterator<Object> it);

    protected abstract S or(S s, S s2);

    protected abstract T complete(S s, Sort sort);

    public AbstractQueryCreator(PartTree tree, ParameterAccessor parameters) {
        Assert.notNull(tree, "PartTree must not be null");
        this.tree = tree;
        this.parameters = parameters;
    }

    public AbstractQueryCreator(PartTree tree) {
        this(tree, null);
    }

    public T createQuery() {
        Sort dynamicSort = this.parameters != null ? this.parameters.getSort() : null;
        return createQuery(dynamicSort);
    }

    public T createQuery(Sort sort) {
        Sort sort2 = this.tree.getSort();
        return complete(createCriteria(this.tree), sort2 != null ? sort2.and(sort) : sort);
    }

    private S createCriteria(PartTree tree) {
        S base = null;
        Iterator<Object> iterator = this.parameters == null ? null : this.parameters.iterator();
        Iterator<PartTree.OrPart> it = tree.iterator();
        while (it.hasNext()) {
            PartTree.OrPart node = it.next();
            S criteria = null;
            Iterator<Part> it2 = node.iterator();
            while (it2.hasNext()) {
                Part part = it2.next();
                criteria = criteria == null ? create(part, iterator) : and(part, criteria, iterator);
            }
            base = base == null ? criteria : or(base, criteria);
        }
        return base;
    }
}
