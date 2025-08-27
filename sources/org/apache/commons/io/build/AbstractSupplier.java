package org.apache.commons.io.build;

import org.apache.commons.io.build.AbstractSupplier;
import org.apache.commons.io.function.IOSupplier;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/build/AbstractSupplier.class */
public abstract class AbstractSupplier<T, B extends AbstractSupplier<T, B>> implements IOSupplier<T> {
    protected B asThis() {
        return this;
    }
}
