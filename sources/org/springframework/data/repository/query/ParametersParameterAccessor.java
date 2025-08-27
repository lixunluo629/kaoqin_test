package org.springframework.data.repository.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.util.QueryExecutionConverters;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ParametersParameterAccessor.class */
public class ParametersParameterAccessor implements ParameterAccessor {
    private final Parameters<?, ?> parameters;
    private final List<Object> values;

    public ParametersParameterAccessor(Parameters<?, ?> parameters, Object[] values) {
        Assert.notNull(parameters, "Parameters must not be null!");
        Assert.notNull(values, "Values must not be null!");
        Assert.isTrue(parameters.getNumberOfParameters() == values.length, "Invalid number of parameters given!");
        this.parameters = parameters;
        List<Object> unwrapped = new ArrayList<>(values.length);
        for (Object element : (Object[]) values.clone()) {
            unwrapped.add(QueryExecutionConverters.unwrap(element));
        }
        this.values = unwrapped;
    }

    public Parameters<?, ?> getParameters() {
        return this.parameters;
    }

    @Override // org.springframework.data.repository.query.ParameterAccessor
    public Pageable getPageable() {
        if (!this.parameters.hasPageableParameter()) {
            return null;
        }
        return (Pageable) this.values.get(this.parameters.getPageableIndex());
    }

    @Override // org.springframework.data.repository.query.ParameterAccessor
    public Sort getSort() {
        if (this.parameters.hasSortParameter()) {
            return (Sort) this.values.get(this.parameters.getSortIndex());
        }
        if (this.parameters.hasPageableParameter() && getPageable() != null) {
            return getPageable().getSort();
        }
        return null;
    }

    @Override // org.springframework.data.repository.query.ParameterAccessor
    public Class<?> getDynamicProjection() {
        if (this.parameters.hasDynamicProjection()) {
            return (Class) this.values.get(this.parameters.getDynamicProjectionIndex());
        }
        return null;
    }

    protected <T> T getValue(int i) {
        return (T) this.values.get(i);
    }

    @Override // org.springframework.data.repository.query.ParameterAccessor
    public Object getBindableValue(int index) {
        return this.values.get(this.parameters.getBindableParameter(index).getIndex());
    }

    @Override // org.springframework.data.repository.query.ParameterAccessor
    public boolean hasBindableNullValue() {
        Iterator it = this.parameters.getBindableParameters().iterator();
        while (it.hasNext()) {
            Parameter parameter = (Parameter) it.next();
            if (this.values.get(parameter.getIndex()) == null) {
                return true;
            }
        }
        return false;
    }

    @Override // org.springframework.data.repository.query.ParameterAccessor, java.lang.Iterable
    /* renamed from: iterator, reason: merged with bridge method [inline-methods] */
    public Iterator<Object> iterator2() {
        return new BindableParameterIterator(this);
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/ParametersParameterAccessor$BindableParameterIterator.class */
    private static class BindableParameterIterator implements Iterator<Object> {
        private final int bindableParameterCount;
        private final ParameterAccessor accessor;
        private int currentIndex = 0;

        public BindableParameterIterator(ParametersParameterAccessor accessor) {
            Assert.notNull(accessor, "ParametersParameterAccessor must not be null!");
            this.accessor = accessor;
            this.bindableParameterCount = accessor.getParameters().getBindableParameters().getNumberOfParameters();
        }

        @Override // java.util.Iterator
        public Object next() {
            ParameterAccessor parameterAccessor = this.accessor;
            int i = this.currentIndex;
            this.currentIndex = i + 1;
            return parameterAccessor.getBindableValue(i);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.bindableParameterCount > this.currentIndex;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
