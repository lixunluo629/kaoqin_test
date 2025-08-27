package org.hamcrest;

import java.io.IOException;

/* loaded from: hamcrest-core-1.3.jar:org/hamcrest/StringDescription.class */
public class StringDescription extends BaseDescription {
    private final Appendable out;

    public StringDescription() {
        this(new StringBuilder());
    }

    public StringDescription(Appendable out) {
        this.out = out;
    }

    public static String toString(SelfDescribing selfDescribing) {
        return new StringDescription().appendDescriptionOf(selfDescribing).toString();
    }

    public static String asString(SelfDescribing selfDescribing) {
        return toString(selfDescribing);
    }

    @Override // org.hamcrest.BaseDescription
    protected void append(String str) throws IOException {
        try {
            this.out.append(str);
        } catch (IOException e) {
            throw new RuntimeException("Could not write description", e);
        }
    }

    @Override // org.hamcrest.BaseDescription
    protected void append(char c) throws IOException {
        try {
            this.out.append(c);
        } catch (IOException e) {
            throw new RuntimeException("Could not write description", e);
        }
    }

    public String toString() {
        return this.out.toString();
    }
}
