package org.springframework.data.repository.query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/Parameters.class */
public abstract class Parameters<S extends Parameters<S, T>, T extends Parameter> implements Iterable<T> {
    public static final List<Class<?>> TYPES = Arrays.asList(Pageable.class, Sort.class);
    private static final String PARAM_ON_SPECIAL = String.format("You must not user @%s on a parameter typed %s or %s", Param.class.getSimpleName(), Pageable.class.getSimpleName(), Sort.class.getSimpleName());
    private static final String ALL_OR_NOTHING = String.format("Either use @%s on all parameters except %s and %s typed once, or none at all!", Param.class.getSimpleName(), Pageable.class.getSimpleName(), Sort.class.getSimpleName());
    private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
    private final int pageableIndex;
    private final int sortIndex;
    private final List<T> parameters;
    private int dynamicProjectionIndex;

    protected abstract T createParameter(MethodParameter methodParameter);

    protected abstract S createFrom(List<T> list);

    public Parameters(Method method) {
        Assert.notNull(method, "Method must not be null!");
        List listAsList = Arrays.asList(method.getParameterTypes());
        this.parameters = new ArrayList(listAsList.size());
        this.dynamicProjectionIndex = -1;
        int i = -1;
        int i2 = -1;
        for (int i3 = 0; i3 < listAsList.size(); i3++) {
            MethodParameter methodParameter = new MethodParameter(method, i3);
            methodParameter.initParameterNameDiscovery(this.discoverer);
            Parameter parameterCreateParameter = createParameter(methodParameter);
            if (parameterCreateParameter.isSpecialParameter() && parameterCreateParameter.isNamedParameter()) {
                throw new IllegalArgumentException(PARAM_ON_SPECIAL);
            }
            if (parameterCreateParameter.isDynamicProjectionParameter()) {
                this.dynamicProjectionIndex = parameterCreateParameter.getIndex();
            }
            i = Pageable.class.isAssignableFrom(parameterCreateParameter.getType()) ? i3 : i;
            if (Sort.class.isAssignableFrom(parameterCreateParameter.getType())) {
                i2 = i3;
            }
            this.parameters.add(parameterCreateParameter);
        }
        this.pageableIndex = i;
        this.sortIndex = i2;
        assertEitherAllParamAnnotatedOrNone();
    }

    protected Parameters(List<T> originals) {
        this.parameters = new ArrayList(originals.size());
        int pageableIndexTemp = -1;
        int sortIndexTemp = -1;
        int dynamicProjectionTemp = -1;
        for (int i = 0; i < originals.size(); i++) {
            T original = originals.get(i);
            this.parameters.add(original);
            pageableIndexTemp = original.isPageable() ? i : -1;
            sortIndexTemp = original.isSort() ? i : -1;
            dynamicProjectionTemp = original.isDynamicProjectionParameter() ? i : -1;
        }
        this.pageableIndex = pageableIndexTemp;
        this.sortIndex = sortIndexTemp;
        this.dynamicProjectionIndex = dynamicProjectionTemp;
    }

    public boolean hasPageableParameter() {
        return this.pageableIndex != -1;
    }

    public int getPageableIndex() {
        return this.pageableIndex;
    }

    public int getSortIndex() {
        return this.sortIndex;
    }

    public boolean hasSortParameter() {
        return this.sortIndex != -1;
    }

    public int getDynamicProjectionIndex() {
        return this.dynamicProjectionIndex;
    }

    public boolean hasDynamicProjection() {
        return this.dynamicProjectionIndex != -1;
    }

    public boolean potentiallySortsDynamically() {
        return hasSortParameter() || hasPageableParameter();
    }

    public T getParameter(int index) {
        try {
            return this.parameters.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new ParameterOutOfBoundsException("Invalid parameter index! You seem to have declared too little query method parameters!", e);
        }
    }

    public boolean hasParameterAt(int position) {
        try {
            return null != getParameter(position);
        } catch (ParameterOutOfBoundsException e) {
            return false;
        }
    }

    public boolean hasSpecialParameter() {
        return hasSortParameter() || hasPageableParameter();
    }

    public int getNumberOfParameters() {
        return this.parameters.size();
    }

    public S getBindableParameters() {
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T next = it.next();
            if (next.isBindable()) {
                arrayList.add(next);
            }
        }
        return (S) createFrom(arrayList);
    }

    public T getBindableParameter(int i) {
        return (T) getBindableParameters().getParameter(i);
    }

    private final void assertEitherAllParamAnnotatedOrNone() {
        boolean nameFound = false;
        int index = 0;
        Iterator<T> it = getBindableParameters().iterator();
        while (it.hasNext()) {
            T parameter = it.next();
            if (parameter.isNamedParameter()) {
                Assert.isTrue(nameFound || index == 0, ALL_OR_NOTHING);
                nameFound = true;
            } else {
                Assert.isTrue(!nameFound, ALL_OR_NOTHING);
            }
            index++;
        }
    }

    public static boolean isBindable(Class<?> type) {
        return !TYPES.contains(type);
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return this.parameters.iterator();
    }
}
