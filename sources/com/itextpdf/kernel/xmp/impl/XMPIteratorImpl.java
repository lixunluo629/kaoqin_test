package com.itextpdf.kernel.xmp.impl;

import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPIterator;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.kernel.xmp.impl.xpath.XMPPath;
import com.itextpdf.kernel.xmp.impl.xpath.XMPPathParser;
import com.itextpdf.kernel.xmp.options.IteratorOptions;
import com.itextpdf.kernel.xmp.options.PropertyOptions;
import com.itextpdf.kernel.xmp.properties.XMPPropertyInfo;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.springframework.beans.PropertyAccessor;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/XMPIteratorImpl.class */
public class XMPIteratorImpl implements XMPIterator {
    private IteratorOptions options;
    private String baseNS;
    protected boolean skipSiblings = false;
    protected boolean skipSubtree = false;
    private Iterator nodeIterator;

    public XMPIteratorImpl(XMPMetaImpl xmp, String schemaNS, String propPath, IteratorOptions options) throws XMPException {
        XMPNode startNode;
        this.baseNS = null;
        this.nodeIterator = null;
        this.options = options != null ? options : new IteratorOptions();
        String initialPath = null;
        boolean baseSchema = schemaNS != null && schemaNS.length() > 0;
        boolean baseProperty = propPath != null && propPath.length() > 0;
        if (!baseSchema && !baseProperty) {
            startNode = xmp.getRoot();
        } else if (baseSchema && baseProperty) {
            XMPPath path = XMPPathParser.expandXPath(schemaNS, propPath);
            XMPPath basePath = new XMPPath();
            for (int i = 0; i < path.size() - 1; i++) {
                basePath.add(path.getSegment(i));
            }
            startNode = XMPNodeUtils.findNode(xmp.getRoot(), path, false, null);
            this.baseNS = schemaNS;
            initialPath = basePath.toString();
        } else if (baseSchema && !baseProperty) {
            startNode = XMPNodeUtils.findSchemaNode(xmp.getRoot(), schemaNS, false);
        } else {
            throw new XMPException("Schema namespace URI is required", 101);
        }
        if (startNode != null) {
            if (!this.options.isJustChildren()) {
                this.nodeIterator = new NodeIterator(startNode, initialPath, 1);
                return;
            } else {
                this.nodeIterator = new NodeIteratorChildren(startNode, initialPath);
                return;
            }
        }
        this.nodeIterator = Collections.emptyIterator();
    }

    @Override // com.itextpdf.kernel.xmp.XMPIterator
    public void skipSubtree() {
        this.skipSubtree = true;
    }

    @Override // com.itextpdf.kernel.xmp.XMPIterator
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

    protected void setBaseNS(String baseNS) {
        this.baseNS = baseNS;
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/XMPIteratorImpl$NodeIterator.class */
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
            this.subIterator = Collections.emptyIterator();
            this.returnProperty = null;
        }

        public NodeIterator(XMPNode visitedNode, String parentPath, int index) {
            this.state = 0;
            this.childrenIterator = null;
            this.index = 0;
            this.subIterator = Collections.emptyIterator();
            this.returnProperty = null;
            this.visitedNode = visitedNode;
            this.state = 0;
            if (visitedNode.getOptions().isSchemaNode()) {
                XMPIteratorImpl.this.setBaseNS(visitedNode.getName());
            }
            this.path = accumulatePath(visitedNode, parentPath, index);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.returnProperty != null) {
                return true;
            }
            if (this.state == 0) {
                return reportNode();
            }
            if (this.state == 1) {
                if (this.childrenIterator == null) {
                    this.childrenIterator = this.visitedNode.iterateChildren();
                }
                boolean hasNext = iterateChildren(this.childrenIterator);
                if (!hasNext && this.visitedNode.hasQualifier() && !XMPIteratorImpl.this.getOptions().isOmitQualifiers()) {
                    this.state = 2;
                    this.childrenIterator = null;
                    hasNext = hasNext();
                }
                return hasNext;
            }
            if (this.childrenIterator == null) {
                this.childrenIterator = this.visitedNode.iterateQualifier();
            }
            return iterateChildren(this.childrenIterator);
        }

        protected boolean reportNode() {
            this.state = 1;
            if (this.visitedNode.getParent() != null && (!XMPIteratorImpl.this.getOptions().isJustLeafnodes() || !this.visitedNode.hasChildren())) {
                this.returnProperty = createPropertyInfo(this.visitedNode, XMPIteratorImpl.this.getBaseNS(), this.path);
                return true;
            }
            return hasNext();
        }

        private boolean iterateChildren(Iterator iterator) {
            if (XMPIteratorImpl.this.skipSiblings) {
                XMPIteratorImpl.this.skipSiblings = false;
                this.subIterator = Collections.emptyIterator();
            }
            if (!this.subIterator.hasNext() && iterator.hasNext()) {
                XMPNode child = (XMPNode) iterator.next();
                this.index++;
                this.subIterator = XMPIteratorImpl.this.new NodeIterator(child, this.path, this.index);
            }
            if (this.subIterator.hasNext()) {
                this.returnProperty = (XMPPropertyInfo) this.subIterator.next();
                return true;
            }
            return false;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (hasNext()) {
                XMPPropertyInfo result = this.returnProperty;
                this.returnProperty = null;
                return result;
            }
            throw new NoSuchElementException("There are no more nodes to return");
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        protected String accumulatePath(XMPNode currNode, String parentPath, int currentIndex) {
            String separator;
            String segmentName;
            if (currNode.getParent() == null || currNode.getOptions().isSchemaNode()) {
                return null;
            }
            if (currNode.getParent().getOptions().isArray()) {
                separator = "";
                segmentName = PropertyAccessor.PROPERTY_KEY_PREFIX + String.valueOf(currentIndex) + "]";
            } else {
                separator = "/";
                segmentName = currNode.getName();
            }
            if (parentPath == null || parentPath.length() == 0) {
                return segmentName;
            }
            if (XMPIteratorImpl.this.getOptions().isJustLeafname()) {
                return !segmentName.startsWith("?") ? segmentName : segmentName.substring(1);
            }
            return parentPath + separator + segmentName;
        }

        protected XMPPropertyInfo createPropertyInfo(final XMPNode node, final String baseNS, final String path) {
            final String value = node.getOptions().isSchemaNode() ? null : node.getValue();
            return new XMPPropertyInfo() { // from class: com.itextpdf.kernel.xmp.impl.XMPIteratorImpl.NodeIterator.1
                @Override // com.itextpdf.kernel.xmp.properties.XMPPropertyInfo
                public String getNamespace() {
                    if (!node.getOptions().isSchemaNode()) {
                        QName qname = new QName(node.getName());
                        return XMPMetaFactory.getSchemaRegistry().getNamespaceURI(qname.getPrefix());
                    }
                    return baseNS;
                }

                @Override // com.itextpdf.kernel.xmp.properties.XMPPropertyInfo
                public String getPath() {
                    return path;
                }

                @Override // com.itextpdf.kernel.xmp.properties.XMPPropertyInfo, com.itextpdf.kernel.xmp.properties.XMPProperty
                public String getValue() {
                    return value;
                }

                @Override // com.itextpdf.kernel.xmp.properties.XMPPropertyInfo, com.itextpdf.kernel.xmp.properties.XMPProperty
                public PropertyOptions getOptions() {
                    return node.getOptions();
                }

                @Override // com.itextpdf.kernel.xmp.properties.XMPProperty
                public String getLanguage() {
                    return null;
                }
            };
        }

        protected Iterator getChildrenIterator() {
            return this.childrenIterator;
        }

        protected void setChildrenIterator(Iterator childrenIterator) {
            this.childrenIterator = childrenIterator;
        }

        protected XMPPropertyInfo getReturnProperty() {
            return this.returnProperty;
        }

        protected void setReturnProperty(XMPPropertyInfo returnProperty) {
            this.returnProperty = returnProperty;
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/XMPIteratorImpl$NodeIteratorChildren.class */
    private class NodeIteratorChildren extends NodeIterator {
        private String parentPath;
        private Iterator childrenIterator;
        private int index;

        public NodeIteratorChildren(XMPNode parentNode, String parentPath) {
            super();
            this.index = 0;
            if (parentNode.getOptions().isSchemaNode()) {
                XMPIteratorImpl.this.setBaseNS(parentNode.getName());
            }
            this.parentPath = accumulatePath(parentNode, parentPath, 1);
            this.childrenIterator = parentNode.iterateChildren();
        }

        @Override // com.itextpdf.kernel.xmp.impl.XMPIteratorImpl.NodeIterator, java.util.Iterator
        public boolean hasNext() {
            if (getReturnProperty() != null) {
                return true;
            }
            if (!XMPIteratorImpl.this.skipSiblings && this.childrenIterator.hasNext()) {
                XMPNode child = (XMPNode) this.childrenIterator.next();
                this.index++;
                String path = null;
                if (child.getOptions().isSchemaNode()) {
                    XMPIteratorImpl.this.setBaseNS(child.getName());
                } else if (child.getParent() != null) {
                    path = accumulatePath(child, this.parentPath, this.index);
                }
                if (!XMPIteratorImpl.this.getOptions().isJustLeafnodes() || !child.hasChildren()) {
                    setReturnProperty(createPropertyInfo(child, XMPIteratorImpl.this.getBaseNS(), path));
                    return true;
                }
                return hasNext();
            }
            return false;
        }
    }
}
