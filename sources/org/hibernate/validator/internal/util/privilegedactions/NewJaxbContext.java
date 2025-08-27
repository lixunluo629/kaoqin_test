package org.hibernate.validator.internal.util.privilegedactions;

import java.security.PrivilegedExceptionAction;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/NewJaxbContext.class */
public final class NewJaxbContext implements PrivilegedExceptionAction<JAXBContext> {
    private final Class<?> clazz;

    public static NewJaxbContext action(Class<?> clazz) {
        return new NewJaxbContext(clazz);
    }

    private NewJaxbContext(Class<?> clazz) {
        this.clazz = clazz;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedExceptionAction
    public JAXBContext run() throws JAXBException {
        return JAXBContext.newInstance(new Class[]{this.clazz});
    }
}
