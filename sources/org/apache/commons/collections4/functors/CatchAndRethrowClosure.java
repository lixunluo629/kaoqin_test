package org.apache.commons.collections4.functors;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.FunctorException;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/functors/CatchAndRethrowClosure.class */
public abstract class CatchAndRethrowClosure<E> implements Closure<E> {
    protected abstract void executeAndThrow(E e) throws Throwable;

    @Override // org.apache.commons.collections4.Closure
    public void execute(E input) {
        try {
            executeAndThrow(input);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new FunctorException(t);
        }
    }
}
