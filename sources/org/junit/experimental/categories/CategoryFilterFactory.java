package org.junit.experimental.categories;

import java.util.ArrayList;
import java.util.List;
import org.junit.internal.Classes;
import org.junit.runner.FilterFactory;
import org.junit.runner.FilterFactoryParams;
import org.junit.runner.manipulation.Filter;

/* loaded from: junit-4.12.jar:org/junit/experimental/categories/CategoryFilterFactory.class */
abstract class CategoryFilterFactory implements FilterFactory {
    protected abstract Filter createFilter(List<Class<?>> list);

    CategoryFilterFactory() {
    }

    @Override // org.junit.runner.FilterFactory
    public Filter createFilter(FilterFactoryParams params) throws FilterFactory.FilterNotCreatedException {
        try {
            return createFilter(parseCategories(params.getArgs()));
        } catch (ClassNotFoundException e) {
            throw new FilterFactory.FilterNotCreatedException(e);
        }
    }

    private List<Class<?>> parseCategories(String categories) throws ClassNotFoundException {
        List<Class<?>> categoryClasses = new ArrayList<>();
        String[] arr$ = categories.split(",");
        for (String category : arr$) {
            Class<?> categoryClass = Classes.getClass(category);
            categoryClasses.add(categoryClass);
        }
        return categoryClasses;
    }
}
