package com.adobe.xmp.impl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.impl.xpath.XMPPath;
import com.adobe.xmp.impl.xpath.XMPPathParser;
import com.adobe.xmp.options.IteratorOptions;
import com.adobe.xmp.options.PropertyOptions;
import com.adobe.xmp.properties.XMPPropertyInfo;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.springframework.beans.PropertyAccessor;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/XMPIteratorImpl.class */
public class XMPIteratorImpl implements XMPIterator {
    private IteratorOptions options;
    private String baseNS;
    protected boolean skipSiblings = false;
    protected boolean skipSubtree = false;
    private Iterator nodeIterator;

    /* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/XMPIteratorImpl$NodeIterator.class */
    private class NodeIterator implements Iterator {
        protected static final int ITERATE_NODE = 0;
        protected static final int ITERATE_CHILDREN = 1;
        protected static final int ITERATE_QUALIFIER = 2;
        private int state;
        private XMPNode visitedNode;
        private String path;
        private Iterator childrenIterator;
        private int index;
        private Iterator subIterator;
        private XMPPropertyInfo returnProperty;

        public NodeIterator() {
            this.state = 0;
            this.childrenIterator = null;
            this.index = 0;
            this.subIterator = Collections.EMPTY_LIST.iterator();
            this.returnProperty = null;
        }

        public NodeIterator(XMPNode xMPNode, String str, int i) {
            this.state = 0;
            this.childrenIterator = null;
            this.index = 0;
            this.subIterator = Collections.EMPTY_LIST.iterator();
            this.returnProperty = null;
            this.visitedNode = xMPNode;
            this.state = 0;
            if (xMPNode.getOptions().isSchemaNode()) {
                XMPIteratorImpl.this.setBaseNS(xMPNode.getName());
            }
            this.path = accumulatePath(xMPNode, str, i);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.returnProperty != null) {
                return true;
            }
            if (this.state == 0) {
                return reportNode();
            }
            if (this.state != 1) {
                if (this.childrenIterator == null) {
                    this.childrenIterator = this.visitedNode.iterateQualifier();
                }
                return iterateChildren(this.childrenIterator);
            }
            if (this.childrenIterator == null) {
                this.childrenIterator = this.visitedNode.iterateChildren();
            }
            boolean zIterateChildren = iterateChildren(this.childrenIterator);
            if (!zIterateChildren && this.visitedNode.hasQualifier() && !XMPIteratorImpl.this.getOptions().isOmitQualifiers()) {
                this.state = 2;
                this.childrenIterator = null;
                zIterateChildren = hasNext();
            }
            return zIterateChildren;
        }

        protected boolean reportNode() {
            this.state = 1;
            if (this.visitedNode.getParent() == null || (XMPIteratorImpl.this.getOptions().isJustLeafnodes() && this.visitedNode.hasChildren())) {
                return hasNext();
            }
            this.returnProperty = createPropertyInfo(this.visitedNode, XMPIteratorImpl.this.getBaseNS(), this.path);
            return true;
        }

        private boolean iterateChildren(Iterator it) {
            if (XMPIteratorImpl.this.skipSiblings) {
                XMPIteratorImpl.this.skipSiblings = false;
                this.subIterator = Collections.EMPTY_LIST.iterator();
            }
            if (!this.subIterator.hasNext() && it.hasNext()) {
                XMPNode xMPNode = (XMPNode) it.next();
                this.index++;
                this.subIterator = XMPIteratorImpl.this.new NodeIterator(xMPNode, this.path, this.index);
            }
            if (!this.subIterator.hasNext()) {
                return false;
            }
            this.returnProperty = (XMPPropertyInfo) this.subIterator.next();
            return true;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no more nodes to return");
            }
            XMPPropertyInfo xMPPropertyInfo = this.returnProperty;
            this.returnProperty = null;
            return xMPPropertyInfo;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        protected String accumulatePath(XMPNode xMPNode, String str, int i) {
            String str2;
            String name;
            if (xMPNode.getParent() == null || xMPNode.getOptions().isSchemaNode()) {
                return null;
            }
            if (xMPNode.getParent().getOptions().isArray()) {
                str2 = "";
                name = PropertyAccessor.PROPERTY_KEY_PREFIX + String.valueOf(i) + "]";
            } else {
                str2 = "/";
                name = xMPNode.getName();
            }
            return (str == null || str.length() == 0) ? name : XMPIteratorImpl.this.getOptions().isJustLeafname() ? !name.startsWith("?") ? name : name.substring(1) : str + str2 + name;
        }

        protected XMPPropertyInfo createPropertyInfo(final XMPNode xMPNode, final String str, final String str2) {
            final String value = xMPNode.getOptions().isSchemaNode() ? null : xMPNode.getValue();
            return new XMPPropertyInfo() { // from class: com.adobe.xmp.impl.XMPIteratorImpl.NodeIterator.1
                @Override // com.adobe.xmp.properties.XMPPropertyInfo
                public String getNamespace() {
                    if (xMPNode.getOptions().isSchemaNode()) {
                        return str;
                    }
                    return XMPMetaFactory.getSchemaRegistry().getNamespaceURI(new QName(xMPNode.getName()).getPrefix());
                }

                @Override // com.adobe.xmp.properties.XMPPropertyInfo
                public String getPath() {
                    return str2;
                }

                @Override // com.adobe.xmp.properties.XMPPropertyInfo, com.adobe.xmp.properties.XMPProperty
                public String getValue() {
                    return value;
                }

                @Override // com.adobe.xmp.properties.XMPPropertyInfo, com.adobe.xmp.properties.XMPProperty
                public PropertyOptions getOptions() {
                    return xMPNode.getOptions();
                }

                @Override // com.adobe.xmp.properties.XMPProperty
                public String getLanguage() {
                    return null;
                }
            };
        }

        protected Iterator getChildrenIterator() {
            return this.childrenIterator;
        }

        protected void setChildrenIterator(Iterator it) {
            this.childrenIterator = it;
        }

        protected XMPPropertyInfo getReturnProperty() {
            return this.returnProperty;
        }

        protected void setReturnProperty(XMPPropertyInfo xMPPropertyInfo) {
            this.returnProperty = xMPPropertyInfo;
        }
    }

    /* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/XMPIteratorImpl$NodeIteratorChildren.class */
    private class NodeIteratorChildren extends NodeIterator {
        private String parentPath;
        private Iterator childrenIterator;
        private int index;

        public NodeIteratorChildren(XMPNode xMPNode, String str) {
            super();
            this.index = 0;
            if (xMPNode.getOptions().isSchemaNode()) {
                XMPIteratorImpl.this.setBaseNS(xMPNode.getName());
            }
            this.parentPath = accumulatePath(xMPNode, str, 1);
            this.childrenIterator = xMPNode.iterateChildren();
        }

        @Override // com.adobe.xmp.impl.XMPIteratorImpl.NodeIterator, java.util.Iterator
        public boolean hasNext() {
            if (getReturnProperty() != null) {
                return true;
            }
            if (XMPIteratorImpl.this.skipSiblings || !this.childrenIterator.hasNext()) {
                return false;
            }
            XMPNode xMPNode = (XMPNode) this.childrenIterator.next();
            this.index++;
            String strAccumulatePath = null;
            if (xMPNode.getOptions().isSchemaNode()) {
                XMPIteratorImpl.this.setBaseNS(xMPNode.getName());
            } else if (xMPNode.getParent() != null) {
                strAccumulatePath = accumulatePath(xMPNode, this.parentPath, this.index);
            }
            if (XMPIteratorImpl.this.getOptions().isJustLeafnodes() && xMPNode.hasChildren()) {
                return hasNext();
            }
            setReturnProperty(createPropertyInfo(xMPNode, XMPIteratorImpl.this.getBaseNS(), strAccumulatePath));
            return true;
        }
    }

    public XMPIteratorImpl(XMPMetaImpl xMPMetaImpl, String str, String str2, IteratorOptions iteratorOptions) throws XMPException {
        XMPNode xMPNodeFindSchemaNode;
        this.baseNS = null;
        this.nodeIterator = null;
        this.options = iteratorOptions != null ? iteratorOptions : new IteratorOptions();
        String string = null;
        boolean z = str != null && str.length() > 0;
        boolean z2 = str2 != null && str2.length() > 0;
        if (!z && !z2) {
            xMPNodeFindSchemaNode = xMPMetaImpl.getRoot();
        } else if (z && z2) {
            XMPPath xMPPathExpandXPath = XMPPathParser.expandXPath(str, str2);
            XMPPath xMPPath = new XMPPath();
            for (int i = 0; i < xMPPathExpandXPath.size() - 1; i++) {
                xMPPath.add(xMPPathExpandXPath.getSegment(i));
            }
            xMPNodeFindSchemaNode = XMPNodeUtils.findNode(xMPMetaImpl.getRoot(), xMPPathExpandXPath, false, null);
            this.baseNS = str;
            string = xMPPath.toString();
        } else {
            if (!z || z2) {
                throw new XMPException("Schema namespace URI is required", 101);
            }
            xMPNodeFindSchemaNode = XMPNodeUtils.findSchemaNode(xMPMetaImpl.getRoot(), str, false);
        }
        if (xMPNodeFindSchemaNode == null) {
            this.nodeIterator = Collections.EMPTY_LIST.iterator();
        } else if (this.options.isJustChildren()) {
            this.nodeIterator = new NodeIteratorChildren(xMPNodeFindSchemaNode, string);
        } else {
            this.nodeIterator = new NodeIterator(xMPNodeFindSchemaNode, string, 1);
        }
    }

    @Override // com.adobe.xmp.XMPIterator
    public void skipSubtree() {
        this.skipSubtree = true;
    }

    @Override // com.adobe.xmp.XMPIterator
    public void skipSiblings() {
        skipSubtree();
        this.skipSiblings = true;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.nodeIterator.hasNext();
    }

    @Override // java.util.Iterator
    public Object next() {
        return this.nodeIterator.next();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("The XMPIterator does not support remove().");
    }

    protected IteratorOptions getOptions() {
        return this.options;
    }

    protected String getBaseNS() {
        return this.baseNS;
    }

    protected void setBaseNS(String str) {
        this.baseNS = str;
    }
}
