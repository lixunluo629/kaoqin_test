package org.springframework.boot.bind;

import java.beans.PropertyEditorSupport;
import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/InetAddressEditor.class */
public class InetAddressEditor extends PropertyEditorSupport {
    public String getAsText() {
        return ((InetAddress) getValue()).getHostAddress();
    }

    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(InetAddress.getByName(text));
        } catch (UnknownHostException ex) {
            throw new IllegalArgumentException("Cannot locate host", ex);
        }
    }
}
