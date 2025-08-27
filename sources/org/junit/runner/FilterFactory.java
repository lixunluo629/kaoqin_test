package org.junit.runner;

import org.junit.runner.manipulation.Filter;

/* loaded from: junit-4.12.jar:org/junit/runner/FilterFactory.class */
public interface FilterFactory {
    Filter createFilter(FilterFactoryParams filterFactoryParams) throws FilterNotCreatedException;

    /* loaded from: junit-4.12.jar:org/junit/runner/FilterFactory$FilterNotCreatedException.class */
    public static class FilterNotCreatedException extends Exception {
        public FilterNotCreatedException(Exception exception) {
            super(exception.getMessage(), exception);
        }
    }
}
