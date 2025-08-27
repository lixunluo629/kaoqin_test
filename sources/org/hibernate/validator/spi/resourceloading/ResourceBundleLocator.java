package org.hibernate.validator.spi.resourceloading;

import java.util.Locale;
import java.util.ResourceBundle;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/spi/resourceloading/ResourceBundleLocator.class */
public interface ResourceBundleLocator {
    ResourceBundle getResourceBundle(Locale locale);
}
