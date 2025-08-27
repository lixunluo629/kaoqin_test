package org.hibernate.validator.internal.util.privilegedactions;

import java.security.PrivilegedExceptionAction;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/Unmarshal.class */
public final class Unmarshal<T> implements PrivilegedExceptionAction<JAXBElement<T>> {
    private final Unmarshaller unmarshaller;
    private final XMLEventReader xmlEventReader;
    private final Class<T> clazz;

    public static <T> Unmarshal<T> action(Unmarshaller unmarshaller, XMLEventReader xmlEventReader, Class<T> clazz) {
        return new Unmarshal<>(unmarshaller, xmlEventReader, clazz);
    }

    private Unmarshal(Unmarshaller unmarshaller, XMLEventReader xmlEventReader, Class<T> clazz) {
        this.unmarshaller = unmarshaller;
        this.xmlEventReader = xmlEventReader;
        this.clazz = clazz;
    }

    @Override // java.security.PrivilegedExceptionAction
    public JAXBElement<T> run() throws JAXBException {
        return this.unmarshaller.unmarshal(this.xmlEventReader, this.clazz);
    }
}
