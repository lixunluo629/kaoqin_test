package org.springframework.oxm.mime;

import javax.activation.DataHandler;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/mime/MimeContainer.class */
public interface MimeContainer {
    boolean isXopPackage();

    boolean convertToXopPackage();

    void addAttachment(String str, DataHandler dataHandler);

    DataHandler getAttachment(String str);
}
