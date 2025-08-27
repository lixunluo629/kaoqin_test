package org.hibernate.validator.internal.engine.path;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ElementKind;
import javax.validation.Path;
import org.hibernate.validator.internal.metadata.aggregated.ExecutableMetaData;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.logging.Messages;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/path/PathImpl.class */
public final class PathImpl implements Path, Serializable {
    private static final long serialVersionUID = 7564511574909882392L;
    private static final String PROPERTY_PATH_SEPARATOR = ".";
    private static final String LEADING_PROPERTY_GROUP = "[^\\[\\.]+";
    private static final String OPTIONAL_INDEX_GROUP = "\\[(\\w*)\\]";
    private static final String REMAINING_PROPERTY_STRING = "\\.(.*)";
    private static final int PROPERTY_NAME_GROUP = 1;
    private static final int INDEXED_GROUP = 2;
    private static final int INDEX_GROUP = 3;
    private static final int REMAINING_STRING_GROUP = 5;
    private final List<Path.Node> nodeList;
    private NodeImpl currentLeafNode;
    private int hashCode;
    private static final Log log = LoggerFactory.make();
    private static final Pattern PATH_PATTERN = Pattern.compile("([^\\[\\.]+)(\\[(\\w*)\\])?(\\.(.*))*");

    public static PathImpl createPathFromString(String propertyPath) {
        Contracts.assertNotNull(propertyPath, Messages.MESSAGES.propertyPathCannotBeNull());
        if (propertyPath.length() == 0) {
            return createRootPath();
        }
        return parseProperty(propertyPath);
    }

    public static PathImpl createPathForExecutable(ExecutableMetaData executable) {
        Contracts.assertNotNull(executable, "A method is required to create a method return value path.");
        PathImpl path = createRootPath();
        if (executable.getKind() == ElementKind.CONSTRUCTOR) {
            path.addConstructorNode(executable.getName(), executable.getParameterTypes());
        } else {
            path.addMethodNode(executable.getName(), executable.getParameterTypes());
        }
        return path;
    }

    public static PathImpl createRootPath() {
        PathImpl path = new PathImpl();
        path.addBeanNode();
        return path;
    }

    public static PathImpl createCopy(PathImpl path) {
        return new PathImpl(path);
    }

    public boolean isRootPath() {
        return this.nodeList.size() == 1 && this.nodeList.get(0).getName() == null;
    }

    public PathImpl getPathWithoutLeafNode() {
        return new PathImpl(this.nodeList.subList(0, this.nodeList.size() - 1));
    }

    public NodeImpl addPropertyNode(String nodeName) {
        NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl) this.nodeList.get(this.nodeList.size() - 1);
        this.currentLeafNode = NodeImpl.createPropertyNode(nodeName, parent);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    public NodeImpl addCollectionElementNode() {
        NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl) this.nodeList.get(this.nodeList.size() - 1);
        this.currentLeafNode = NodeImpl.createCollectionElementNode(parent);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    public NodeImpl addParameterNode(String nodeName, int index) {
        NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl) this.nodeList.get(this.nodeList.size() - 1);
        this.currentLeafNode = NodeImpl.createParameterNode(nodeName, parent, index);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    public NodeImpl addCrossParameterNode() {
        NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl) this.nodeList.get(this.nodeList.size() - 1);
        this.currentLeafNode = NodeImpl.createCrossParameterNode(parent);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    public NodeImpl addBeanNode() {
        NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl) this.nodeList.get(this.nodeList.size() - 1);
        this.currentLeafNode = NodeImpl.createBeanNode(parent);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    public NodeImpl addReturnValueNode() {
        NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl) this.nodeList.get(this.nodeList.size() - 1);
        this.currentLeafNode = NodeImpl.createReturnValue(parent);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    private NodeImpl addConstructorNode(String name, Class<?>[] parameterTypes) {
        NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl) this.nodeList.get(this.nodeList.size() - 1);
        this.currentLeafNode = NodeImpl.createConstructorNode(name, parent, parameterTypes);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    private NodeImpl addMethodNode(String name, Class<?>[] parameterTypes) {
        NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl) this.nodeList.get(this.nodeList.size() - 1);
        this.currentLeafNode = NodeImpl.createMethodNode(name, parent, parameterTypes);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    public NodeImpl makeLeafNodeIterable() {
        this.currentLeafNode = NodeImpl.makeIterable(this.currentLeafNode);
        this.nodeList.remove(this.nodeList.size() - 1);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    public NodeImpl setLeafNodeIndex(Integer index) {
        this.currentLeafNode = NodeImpl.setIndex(this.currentLeafNode, index);
        this.nodeList.remove(this.nodeList.size() - 1);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    public NodeImpl setLeafNodeMapKey(Object key) {
        this.currentLeafNode = NodeImpl.setMapKey(this.currentLeafNode, key);
        this.nodeList.remove(this.nodeList.size() - 1);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    public NodeImpl setLeafNodeValue(Object value) {
        this.currentLeafNode = NodeImpl.setPropertyValue(this.currentLeafNode, value);
        this.nodeList.remove(this.nodeList.size() - 1);
        this.nodeList.add(this.currentLeafNode);
        this.hashCode = -1;
        return this.currentLeafNode;
    }

    public NodeImpl getLeafNode() {
        return this.currentLeafNode;
    }

    @Override // java.lang.Iterable
    public Iterator<Path.Node> iterator() {
        if (this.nodeList.size() == 0) {
            return Collections.emptyList().iterator();
        }
        if (this.nodeList.size() == 1) {
            return this.nodeList.iterator();
        }
        return this.nodeList.subList(1, this.nodeList.size()).iterator();
    }

    public String asString() {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (int i = 1; i < this.nodeList.size(); i++) {
            NodeImpl nodeImpl = (NodeImpl) this.nodeList.get(i);
            String name = nodeImpl.asString();
            if (!name.isEmpty()) {
                if (!first) {
                    builder.append(".");
                }
                builder.append(nodeImpl.asString());
                first = false;
            }
        }
        return builder.toString();
    }

    public String toString() {
        return asString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PathImpl other = (PathImpl) obj;
        if (this.nodeList == null) {
            if (other.nodeList != null) {
                return false;
            }
            return true;
        }
        if (!this.nodeList.equals(other.nodeList)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.hashCode == -1) {
            this.hashCode = buildHashCode();
        }
        return this.hashCode;
    }

    private int buildHashCode() {
        int result = (31 * 1) + (this.nodeList == null ? 0 : this.nodeList.hashCode());
        return result;
    }

    private PathImpl(PathImpl path) {
        this(path.nodeList);
        this.currentLeafNode = (NodeImpl) this.nodeList.get(this.nodeList.size() - 1);
    }

    private PathImpl() {
        this.nodeList = new ArrayList();
        this.hashCode = -1;
    }

    private PathImpl(List<Path.Node> nodeList) {
        this.nodeList = new ArrayList(nodeList);
        this.hashCode = -1;
    }

    private static PathImpl parseProperty(String propertyName) {
        PathImpl path = createRootPath();
        String tmp = propertyName;
        do {
            Matcher matcher = PATH_PATTERN.matcher(tmp);
            if (matcher.matches()) {
                String value = matcher.group(1);
                if (!isValidJavaIdentifier(value)) {
                    throw log.getInvalidJavaIdentifierException(value);
                }
                path.addPropertyNode(value);
                if (matcher.group(2) != null) {
                    path.makeLeafNodeIterable();
                }
                String indexOrKey = matcher.group(3);
                if (indexOrKey != null && indexOrKey.length() > 0) {
                    try {
                        Integer i = Integer.valueOf(Integer.parseInt(indexOrKey));
                        path.setLeafNodeIndex(i);
                    } catch (NumberFormatException e) {
                        path.setLeafNodeMapKey(indexOrKey);
                    }
                }
                tmp = matcher.group(5);
            } else {
                throw log.getUnableToParsePropertyPathException(propertyName);
            }
        } while (tmp != null);
        if (path.getLeafNode().isIterable()) {
            path.addBeanNode();
        }
        return path;
    }

    private static boolean isValidJavaIdentifier(String identifier) {
        Contracts.assertNotNull(identifier, "identifier param cannot be null");
        if (identifier.length() == 0 || !Character.isJavaIdentifierStart((int) identifier.charAt(0))) {
            return false;
        }
        for (int i = 1; i < identifier.length(); i++) {
            if (!Character.isJavaIdentifierPart((int) identifier.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
