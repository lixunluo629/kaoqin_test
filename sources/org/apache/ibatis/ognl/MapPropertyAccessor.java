package org.apache.ibatis.ognl;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.springframework.web.servlet.tags.form.InputTag;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/MapPropertyAccessor.class */
public class MapPropertyAccessor implements PropertyAccessor {
    @Override // org.apache.ibatis.ognl.PropertyAccessor
    public Object getProperty(Map context, Object target, Object name) throws OgnlException {
        Object result;
        Map map = (Map) target;
        Node currentNode = ((OgnlContext) context).getCurrentNode().jjtGetParent();
        boolean indexedAccess = false;
        if (currentNode == null) {
            throw new OgnlException("node is null for '" + name + "'");
        }
        if (!(currentNode instanceof ASTProperty)) {
            currentNode = currentNode.jjtGetParent();
        }
        if (currentNode instanceof ASTProperty) {
            indexedAccess = ((ASTProperty) currentNode).isIndexedAccess();
        }
        if ((name instanceof String) && !indexedAccess) {
            if (name.equals(InputTag.SIZE_ATTRIBUTE)) {
                result = new Integer(map.size());
            } else if (name.equals("keys") || name.equals("keySet")) {
                result = map.keySet();
            } else if (name.equals("values")) {
                result = map.values();
            } else if (name.equals("isEmpty")) {
                result = map.isEmpty() ? Boolean.TRUE : Boolean.FALSE;
            } else {
                result = map.get(name);
            }
        } else {
            result = map.get(name);
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.PropertyAccessor
    public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
        Map map = (Map) target;
        map.put(name, value);
    }

    @Override // org.apache.ibatis.ognl.PropertyAccessor
    public String getSourceAccessor(OgnlContext context, Object target, Object index) {
        Node currentNode = context.getCurrentNode().jjtGetParent();
        boolean indexedAccess = false;
        if (currentNode == null) {
            throw new RuntimeException("node is null for '" + index + "'");
        }
        if (!(currentNode instanceof ASTProperty)) {
            currentNode = currentNode.jjtGetParent();
        }
        if (currentNode instanceof ASTProperty) {
            indexedAccess = ((ASTProperty) currentNode).isIndexedAccess();
        }
        String indexStr = index.toString();
        context.setCurrentAccessor(Map.class);
        context.setCurrentType(Object.class);
        if (String.class.isInstance(index) && !indexedAccess) {
            String key = indexStr.indexOf(34) >= 0 ? indexStr.replaceAll(SymbolConstants.QUOTES_SYMBOL, "") : indexStr;
            if (key.equals(InputTag.SIZE_ATTRIBUTE)) {
                context.setCurrentType(Integer.TYPE);
                return ".size()";
            }
            if (key.equals("keys") || key.equals("keySet")) {
                context.setCurrentType(Set.class);
                return ".keySet()";
            }
            if (key.equals("values")) {
                context.setCurrentType(Collection.class);
                return ".values()";
            }
            if (key.equals("isEmpty")) {
                context.setCurrentType(Boolean.TYPE);
                return ".isEmpty()";
            }
        }
        return ".get(" + indexStr + ")";
    }

    @Override // org.apache.ibatis.ognl.PropertyAccessor
    public String getSourceSetter(OgnlContext context, Object target, Object index) {
        context.setCurrentAccessor(Map.class);
        context.setCurrentType(Object.class);
        String indexStr = index.toString();
        if (String.class.isInstance(index)) {
            String key = indexStr.indexOf(34) >= 0 ? indexStr.replaceAll(SymbolConstants.QUOTES_SYMBOL, "") : indexStr;
            if (key.equals(InputTag.SIZE_ATTRIBUTE) || key.equals("keys") || key.equals("keySet") || key.equals("values") || key.equals("isEmpty")) {
                return "";
            }
        }
        return ".put(" + indexStr + ", $3)";
    }
}
