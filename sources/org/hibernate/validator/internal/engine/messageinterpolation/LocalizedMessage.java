package org.hibernate.validator.internal.engine.messageinterpolation;

import java.util.Locale;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/messageinterpolation/LocalizedMessage.class */
public class LocalizedMessage {
    private final String message;
    private final Locale locale;

    public LocalizedMessage(String message, Locale locale) {
        this.message = message;
        this.locale = locale;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocalizedMessage that = (LocalizedMessage) o;
        if (this.locale != null) {
            if (!this.locale.equals(that.locale)) {
                return false;
            }
        } else if (that.locale != null) {
            return false;
        }
        if (this.message != null) {
            if (!this.message.equals(that.message)) {
                return false;
            }
            return true;
        }
        if (that.message != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.message != null ? this.message.hashCode() : 0;
        return (31 * result) + (this.locale != null ? this.locale.hashCode() : 0);
    }
}
