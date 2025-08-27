package org.apache.ibatis.reflection.property;

import java.util.Iterator;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/property/PropertyTokenizer.class */
public class PropertyTokenizer implements Iterator<PropertyTokenizer> {
    private String name;
    private final String indexedName;
    private String index;
    private final String children;

    public PropertyTokenizer(String fullname) {
        int delim = fullname.indexOf(46);
        if (delim > -1) {
            this.name = fullname.substring(0, delim);
            this.children = fullname.substring(delim + 1);
        } else {
            this.name = fullname;
            this.children = null;
        }
        this.indexedName = this.name;
        int delim2 = this.name.indexOf(91);
        if (delim2 > -1) {
            this.index = this.name.substring(delim2 + 1, this.name.length() - 1);
            this.name = this.name.substring(0, delim2);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getIndex() {
        return this.index;
    }

    public String getIndexedName() {
        return this.indexedName;
    }

    public String getChildren() {
        return this.children;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.children != null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public PropertyTokenizer next() {
        return new PropertyTokenizer(this.children);
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
    }
}
