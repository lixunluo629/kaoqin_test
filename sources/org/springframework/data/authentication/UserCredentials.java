package org.springframework.data.authentication;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/authentication/UserCredentials.class */
public class UserCredentials {
    public static final UserCredentials NO_CREDENTIALS = new UserCredentials(null, null);
    private final String username;
    private final String password;

    public UserCredentials(String username, String password) {
        this.username = StringUtils.hasText(username) ? username : null;
        this.password = StringUtils.hasText(password) ? password : null;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean hasUsername() {
        return this.username != null;
    }

    public boolean hasPassword() {
        return this.password != null;
    }

    public String getObfuscatedPassword() {
        if (!hasPassword()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        if (this.password.length() < 3) {
            for (int i = this.password.length(); i != 0; i--) {
                builder.append("*");
            }
            return builder.toString();
        }
        builder.append(this.password.charAt(0));
        for (int i2 = this.password.length() - 2; i2 != 0; i2--) {
            builder.append("*");
        }
        return builder.append(this.password.substring(this.password.length() - 1)).toString();
    }

    public String toString() {
        return String.format("username = [%s], password = [%s]", this.username, getObfuscatedPassword());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        UserCredentials that = (UserCredentials) obj;
        return ObjectUtils.nullSafeEquals(this.username, that.username) && ObjectUtils.nullSafeEquals(this.password, that.password);
    }

    public int hashCode() {
        int result = 17 + (31 * ObjectUtils.nullSafeHashCode(this.username));
        return result + (31 * ObjectUtils.nullSafeHashCode(this.password));
    }
}
