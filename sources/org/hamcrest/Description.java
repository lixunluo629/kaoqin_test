package org.hamcrest;

/* loaded from: hamcrest-core-1.3.jar:org/hamcrest/Description.class */
public interface Description {
    public static final Description NONE = new NullDescription();

    Description appendText(String str);

    Description appendDescriptionOf(SelfDescribing selfDescribing);

    Description appendValue(Object obj);

    <T> Description appendValueList(String str, String str2, String str3, T... tArr);

    <T> Description appendValueList(String str, String str2, String str3, Iterable<T> iterable);

    Description appendList(String str, String str2, String str3, Iterable<? extends SelfDescribing> iterable);

    /* loaded from: hamcrest-core-1.3.jar:org/hamcrest/Description$NullDescription.class */
    public static final class NullDescription implements Description {
        @Override // org.hamcrest.Description
        public Description appendDescriptionOf(SelfDescribing value) {
            return this;
        }

        @Override // org.hamcrest.Description
        public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
            return this;
        }

        @Override // org.hamcrest.Description
        public Description appendText(String text) {
            return this;
        }

        @Override // org.hamcrest.Description
        public Description appendValue(Object value) {
            return this;
        }

        @Override // org.hamcrest.Description
        public <T> Description appendValueList(String start, String separator, String end, T... values) {
            return this;
        }

        @Override // org.hamcrest.Description
        public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
            return this;
        }

        public String toString() {
            return "";
        }
    }
}
