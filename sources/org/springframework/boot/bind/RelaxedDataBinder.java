package org.springframework.boot.bind;

import java.beans.PropertyEditor;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.propertyeditors.FileEditor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.AbstractPropertyBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.DataBinder;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedDataBinder.class */
public class RelaxedDataBinder extends DataBinder {
    private static final Set<Class<?>> EXCLUDED_EDITORS;
    private static final Object BLANK;
    private String namePrefix;
    private boolean ignoreNestedProperties;
    private MultiValueMap<String, String> nameAliases;

    static {
        Set<Class<?>> excluded = new HashSet<>();
        excluded.add(FileEditor.class);
        EXCLUDED_EDITORS = Collections.unmodifiableSet(excluded);
        BLANK = new Object();
    }

    public RelaxedDataBinder(Object target) {
        super(wrapTarget(target));
        this.nameAliases = new LinkedMultiValueMap();
    }

    public RelaxedDataBinder(Object target, String namePrefix) {
        super(wrapTarget(target), StringUtils.hasLength(namePrefix) ? namePrefix : DataBinder.DEFAULT_OBJECT_NAME);
        this.nameAliases = new LinkedMultiValueMap();
        this.namePrefix = cleanNamePrefix(namePrefix);
    }

    private String cleanNamePrefix(String namePrefix) {
        if (StringUtils.hasLength(namePrefix)) {
            return namePrefix.endsWith(".") ? namePrefix : namePrefix + ".";
        }
        return null;
    }

    public void setIgnoreNestedProperties(boolean ignoreNestedProperties) {
        this.ignoreNestedProperties = ignoreNestedProperties;
    }

    public void setNameAliases(Map<String, List<String>> aliases) {
        this.nameAliases = new LinkedMultiValueMap(aliases);
    }

    public RelaxedDataBinder withAlias(String name, String... alias) {
        for (String value : alias) {
            this.nameAliases.add(name, value);
        }
        return this;
    }

    @Override // org.springframework.validation.DataBinder
    protected void doBind(MutablePropertyValues propertyValues) {
        super.doBind(modifyProperties(propertyValues, getTarget()));
    }

    private MutablePropertyValues modifyProperties(MutablePropertyValues propertyValues, Object target) {
        MutablePropertyValues propertyValues2 = getPropertyValuesForNamePrefix(propertyValues);
        if (target instanceof MapHolder) {
            propertyValues2 = addMapPrefix(propertyValues2);
        }
        BeanWrapper wrapper = new BeanWrapperImpl(target);
        wrapper.setConversionService(new RelaxedConversionService(getConversionService()));
        wrapper.setAutoGrowNestedPaths(true);
        List<PropertyValue> sortedValues = new ArrayList<>();
        Set<String> modifiedNames = new HashSet<>();
        List<String> sortedNames = getSortedPropertyNames(propertyValues2);
        for (String name : sortedNames) {
            PropertyValue propertyValue = propertyValues2.getPropertyValue(name);
            PropertyValue modifiedProperty = modifyProperty(wrapper, propertyValue);
            if (modifiedNames.add(modifiedProperty.getName())) {
                sortedValues.add(modifiedProperty);
            }
        }
        return new MutablePropertyValues(sortedValues);
    }

    private List<String> getSortedPropertyNames(MutablePropertyValues propertyValues) {
        List<String> names = new LinkedList<>();
        for (PropertyValue propertyValue : propertyValues.getPropertyValueList()) {
            names.add(propertyValue.getName());
        }
        sortPropertyNames(names);
        return names;
    }

    private void sortPropertyNames(List<String> names) {
        Iterator it = new ArrayList(names).iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            int propertyIndex = names.indexOf(name);
            BeanPath path = new BeanPath(name);
            for (String prefix : path.prefixes()) {
                int prefixIndex = names.indexOf(prefix);
                if (prefixIndex >= propertyIndex) {
                    names.remove(name);
                    names.add(prefixIndex, name);
                }
            }
        }
    }

    private MutablePropertyValues addMapPrefix(MutablePropertyValues propertyValues) {
        MutablePropertyValues rtn = new MutablePropertyValues();
        for (PropertyValue pv : propertyValues.getPropertyValues()) {
            rtn.add("map." + pv.getName(), pv.getValue());
        }
        return rtn;
    }

    private MutablePropertyValues getPropertyValuesForNamePrefix(MutablePropertyValues propertyValues) {
        if (!StringUtils.hasText(this.namePrefix) && !this.ignoreNestedProperties) {
            return propertyValues;
        }
        MutablePropertyValues rtn = new MutablePropertyValues();
        for (PropertyValue value : propertyValues.getPropertyValues()) {
            String name = value.getName();
            Iterator<String> it = new RelaxedNames(stripLastDot(this.namePrefix)).iterator();
            while (it.hasNext()) {
                String prefix = it.next();
                for (String separator : new String[]{".", "_"}) {
                    String candidate = StringUtils.hasLength(prefix) ? prefix + separator : prefix;
                    if (name.startsWith(candidate)) {
                        name = name.substring(candidate.length());
                        if (!this.ignoreNestedProperties || !name.contains(".")) {
                            PropertyOrigin propertyOrigin = OriginCapablePropertyValue.getOrigin(value);
                            rtn.addPropertyValue(new OriginCapablePropertyValue(name, value.getValue(), propertyOrigin));
                        }
                    }
                }
            }
        }
        return rtn;
    }

    private String stripLastDot(String string) {
        if (StringUtils.hasLength(string) && string.endsWith(".")) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    private PropertyValue modifyProperty(BeanWrapper target, PropertyValue propertyValue) {
        String name = propertyValue.getName();
        String normalizedName = normalizePath(target, name);
        if (!normalizedName.equals(name)) {
            return new PropertyValue(normalizedName, propertyValue.getValue());
        }
        return propertyValue;
    }

    protected String normalizePath(BeanWrapper wrapper, String path) {
        return initializePath(wrapper, new BeanPath(path), 0);
    }

    @Override // org.springframework.validation.DataBinder
    protected AbstractPropertyBindingResult createBeanPropertyBindingResult() {
        return new RelaxedBeanPropertyBindingResult(getTarget(), getObjectName(), isAutoGrowNestedPaths(), getAutoGrowCollectionLimit(), getConversionService());
    }

    private String initializePath(BeanWrapper wrapper, BeanPath path, int index) {
        String prefix = path.prefix(index);
        String key = path.name(index);
        if (path.isProperty(index)) {
            path.rename(index, getActualPropertyName(wrapper, prefix, key));
        }
        int index2 = index + 1;
        if (path.name(index2) == null) {
            return path.toString();
        }
        String name = path.prefix(index2);
        TypeDescriptor descriptor = wrapper.getPropertyTypeDescriptor(name);
        if (descriptor == null || descriptor.isMap()) {
            if (isMapValueStringType(descriptor) || isBlanked(wrapper, name, path.name(index2))) {
                path.collapseKeys(index2);
            }
            path.mapIndex(index2);
            extendMapIfNecessary(wrapper, path, index2);
        } else if (descriptor.isCollection()) {
            extendCollectionIfNecessary(wrapper, path, index2);
        } else if (descriptor.getType().equals(Object.class)) {
            if (isBlanked(wrapper, name, path.name(index2))) {
                path.collapseKeys(index2);
            }
            path.mapIndex(index2);
            if (path.isLastNode(index2)) {
                wrapper.setPropertyValue(path.toString(), BLANK);
            } else {
                String next = path.prefix(index2 + 1);
                if (wrapper.getPropertyValue(next) == null) {
                    wrapper.setPropertyValue(next, new LinkedHashMap());
                }
            }
        }
        return initializePath(wrapper, path, index2);
    }

    private boolean isMapValueStringType(TypeDescriptor descriptor) {
        if (descriptor == null || descriptor.getMapValueTypeDescriptor() == null) {
            return false;
        }
        if (Properties.class.isAssignableFrom(descriptor.getObjectType())) {
            return true;
        }
        Class<?> valueType = descriptor.getMapValueTypeDescriptor().getObjectType();
        return valueType != null && CharSequence.class.isAssignableFrom(valueType);
    }

    private boolean isBlanked(BeanWrapper wrapper, String propertyName, String key) {
        Object value = wrapper.isReadableProperty(propertyName) ? wrapper.getPropertyValue(propertyName) : null;
        if ((value instanceof Map) && ((Map) value).get(key) == BLANK) {
            return true;
        }
        return false;
    }

    private void extendCollectionIfNecessary(BeanWrapper wrapper, BeanPath path, int index) {
        String name = path.prefix(index);
        TypeDescriptor elementDescriptor = wrapper.getPropertyTypeDescriptor(name).getElementTypeDescriptor();
        if (!elementDescriptor.isMap() && !elementDescriptor.isCollection() && !elementDescriptor.getType().equals(Object.class)) {
            return;
        }
        Object extend = new LinkedHashMap();
        if (!elementDescriptor.isMap() && path.isArrayIndex(index)) {
            extend = new ArrayList();
        }
        wrapper.setPropertyValue(path.prefix(index + 1), extend);
    }

    private void extendMapIfNecessary(BeanWrapper wrapper, BeanPath path, int index) {
        String name = path.prefix(index);
        TypeDescriptor parent = wrapper.getPropertyTypeDescriptor(name);
        if (parent == null) {
            return;
        }
        TypeDescriptor descriptor = parent.getMapValueTypeDescriptor();
        if (descriptor == null) {
            descriptor = TypeDescriptor.valueOf(Object.class);
        }
        if (!descriptor.isMap() && !descriptor.isCollection() && !descriptor.getType().equals(Object.class)) {
            return;
        }
        String extensionName = path.prefix(index + 1);
        if (wrapper.isReadableProperty(extensionName)) {
            Object currentValue = wrapper.getPropertyValue(extensionName);
            if (!descriptor.isCollection() || !(currentValue instanceof Collection)) {
                if (!descriptor.isCollection() && (currentValue instanceof Map)) {
                    return;
                }
            } else {
                return;
            }
        }
        Object extend = new LinkedHashMap();
        if (descriptor.isCollection()) {
            extend = new ArrayList();
        }
        if (descriptor.getType().equals(Object.class) && path.isLastNode(index)) {
            extend = BLANK;
        }
        wrapper.setPropertyValue(extensionName, extend);
    }

    private String getActualPropertyName(BeanWrapper target, String prefix, String name) {
        String propertyName = resolvePropertyName(target, prefix, name);
        if (propertyName == null) {
            propertyName = resolveNestedPropertyName(target, prefix, name);
        }
        return propertyName != null ? propertyName : name;
    }

    private String resolveNestedPropertyName(BeanWrapper target, String prefix, String name) {
        StringBuilder candidate = new StringBuilder();
        for (String field : name.split("[_\\-\\.]")) {
            candidate.append(candidate.length() > 0 ? "." : "");
            candidate.append(field);
            String nested = resolvePropertyName(target, prefix, candidate.toString());
            if (nested != null) {
                Class<?> type = target.getPropertyType(nested);
                if (type != null && Map.class.isAssignableFrom(type)) {
                    return nested + PropertyAccessor.PROPERTY_KEY_PREFIX + name.substring(candidate.length() + 1) + "]";
                }
                String propertyName = resolvePropertyName(target, joinString(prefix, nested), name.substring(candidate.length() + 1));
                if (propertyName != null) {
                    return joinString(nested, propertyName);
                }
            }
        }
        return null;
    }

    private String resolvePropertyName(BeanWrapper target, String prefix, String name) {
        Iterable<String> names = getNameAndAliases(name);
        for (String nameOrAlias : names) {
            Iterator<String> it = new RelaxedNames(nameOrAlias).iterator();
            while (it.hasNext()) {
                String candidate = it.next();
                if (target.getPropertyType(joinString(prefix, candidate)) != null) {
                    return candidate;
                }
            }
        }
        return null;
    }

    private String joinString(String prefix, String name) {
        return StringUtils.hasLength(prefix) ? prefix + "." + name : name;
    }

    private Iterable<String> getNameAndAliases(String name) {
        List<String> aliases = (List) this.nameAliases.get(name);
        if (aliases == null) {
            return Collections.singleton(name);
        }
        List<String> nameAndAliases = new ArrayList<>(aliases.size() + 1);
        nameAndAliases.add(name);
        nameAndAliases.addAll(aliases);
        return nameAndAliases;
    }

    private static Object wrapTarget(Object target) {
        if (target instanceof Map) {
            Map<String, Object> map = (Map) target;
            target = new MapHolder(map);
        }
        return target;
    }

    @Override // org.springframework.validation.DataBinder, org.springframework.beans.PropertyEditorRegistry
    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        if (propertyEditor == null || !EXCLUDED_EDITORS.contains(propertyEditor.getClass())) {
            super.registerCustomEditor(requiredType, propertyEditor);
        }
    }

    @Override // org.springframework.validation.DataBinder, org.springframework.beans.PropertyEditorRegistry
    public void registerCustomEditor(Class<?> requiredType, String field, PropertyEditor propertyEditor) {
        if (propertyEditor == null || !EXCLUDED_EDITORS.contains(propertyEditor.getClass())) {
            super.registerCustomEditor(requiredType, field, propertyEditor);
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedDataBinder$MapHolder.class */
    static class MapHolder {
        private Map<String, Object> map;

        MapHolder(Map<String, Object> map) {
            this.map = map;
        }

        public void setMap(Map<String, Object> map) {
            this.map = map;
        }

        public Map<String, Object> getMap() {
            return this.map;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedDataBinder$BeanPath.class */
    private static class BeanPath {
        private List<PathNode> nodes;

        BeanPath(String path) {
            this.nodes = splitPath(path);
        }

        public List<String> prefixes() {
            List<String> prefixes = new ArrayList<>();
            for (int index = 1; index < this.nodes.size(); index++) {
                prefixes.add(prefix(index));
            }
            return prefixes;
        }

        public boolean isLastNode(int index) {
            return index >= this.nodes.size() - 1;
        }

        private List<PathNode> splitPath(String path) {
            List<PathNode> nodes = new ArrayList<>();
            String current = extractIndexedPaths(path, nodes);
            for (String name : StringUtils.delimitedListToStringArray(current, ".")) {
                if (StringUtils.hasText(name)) {
                    nodes.add(new PropertyNode(name));
                }
            }
            return nodes;
        }

        private String extractIndexedPaths(String path, List<PathNode> nodes) {
            int startRef = path.indexOf(PropertyAccessor.PROPERTY_KEY_PREFIX);
            String current = path;
            while (startRef >= 0) {
                if (startRef > 0) {
                    nodes.addAll(splitPath(current.substring(0, startRef)));
                }
                int endRef = current.indexOf("]", startRef);
                if (endRef > 0) {
                    String sub = current.substring(startRef + 1, endRef);
                    if (sub.matches("[0-9]+")) {
                        nodes.add(new ArrayIndexNode(sub));
                    } else {
                        nodes.add(new MapIndexNode(sub));
                    }
                }
                current = current.substring(endRef + 1);
                startRef = current.indexOf(PropertyAccessor.PROPERTY_KEY_PREFIX);
            }
            return current;
        }

        public void collapseKeys(int index) {
            List<PathNode> revised = new ArrayList<>();
            for (int i = 0; i < index; i++) {
                revised.add(this.nodes.get(i));
            }
            StringBuilder builder = new StringBuilder();
            for (int i2 = index; i2 < this.nodes.size(); i2++) {
                if (i2 > index) {
                    builder.append(".");
                }
                builder.append(this.nodes.get(i2).name);
            }
            revised.add(new PropertyNode(builder.toString()));
            this.nodes = revised;
        }

        public void mapIndex(int index) {
            PathNode node = this.nodes.get(index);
            if (node instanceof PropertyNode) {
                node = ((PropertyNode) node).mapIndex();
            }
            this.nodes.set(index, node);
        }

        public String prefix(int index) {
            return range(0, index);
        }

        public void rename(int index, String name) {
            this.nodes.get(index).name = name;
        }

        public String name(int index) {
            if (index < this.nodes.size()) {
                return this.nodes.get(index).name;
            }
            return null;
        }

        private String range(int start, int end) {
            StringBuilder builder = new StringBuilder();
            for (int i = start; i < end; i++) {
                PathNode node = this.nodes.get(i);
                builder.append(node);
            }
            if (builder.toString().startsWith(".")) {
                builder.replace(0, 1, "");
            }
            return builder.toString();
        }

        public boolean isArrayIndex(int index) {
            return this.nodes.get(index) instanceof ArrayIndexNode;
        }

        public boolean isProperty(int index) {
            return this.nodes.get(index) instanceof PropertyNode;
        }

        public String toString() {
            return prefix(this.nodes.size());
        }

        /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedDataBinder$BeanPath$PathNode.class */
        private static class PathNode {
            protected String name;

            PathNode(String name) {
                this.name = name;
            }
        }

        /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedDataBinder$BeanPath$ArrayIndexNode.class */
        private static class ArrayIndexNode extends PathNode {
            ArrayIndexNode(String name) {
                super(name);
            }

            public String toString() {
                return PropertyAccessor.PROPERTY_KEY_PREFIX + this.name + "]";
            }
        }

        /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedDataBinder$BeanPath$MapIndexNode.class */
        private static class MapIndexNode extends PathNode {
            MapIndexNode(String name) {
                super(name);
            }

            public String toString() {
                return PropertyAccessor.PROPERTY_KEY_PREFIX + this.name + "]";
            }
        }

        /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedDataBinder$BeanPath$PropertyNode.class */
        private static class PropertyNode extends PathNode {
            PropertyNode(String name) {
                super(name);
            }

            public MapIndexNode mapIndex() {
                return new MapIndexNode(this.name);
            }

            public String toString() {
                return "." + this.name;
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedDataBinder$RelaxedBeanPropertyBindingResult.class */
    private static class RelaxedBeanPropertyBindingResult extends BeanPropertyBindingResult {
        private RelaxedConversionService conversionService;

        RelaxedBeanPropertyBindingResult(Object target, String objectName, boolean autoGrowNestedPaths, int autoGrowCollectionLimit, ConversionService conversionService) {
            super(target, objectName, autoGrowNestedPaths, autoGrowCollectionLimit);
            this.conversionService = new RelaxedConversionService(conversionService);
        }

        @Override // org.springframework.validation.BeanPropertyBindingResult
        protected BeanWrapper createBeanWrapper() {
            BeanWrapper beanWrapper = new RelaxedBeanWrapper(getTarget());
            beanWrapper.setConversionService(this.conversionService);
            beanWrapper.registerCustomEditor(InetAddress.class, new InetAddressEditor());
            return beanWrapper;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedDataBinder$RelaxedBeanWrapper.class */
    private static class RelaxedBeanWrapper extends BeanWrapperImpl {
        private static final Set<String> BENIGN_PROPERTY_SOURCE_NAMES;

        static {
            Set<String> names = new HashSet<>();
            names.add("systemEnvironment");
            names.add("systemProperties");
            BENIGN_PROPERTY_SOURCE_NAMES = Collections.unmodifiableSet(names);
        }

        RelaxedBeanWrapper(Object target) {
            super(target);
        }

        @Override // org.springframework.beans.AbstractNestablePropertyAccessor, org.springframework.beans.AbstractPropertyAccessor, org.springframework.beans.PropertyAccessor
        public void setPropertyValue(PropertyValue pv) throws BeansException {
            try {
                super.setPropertyValue(pv);
            } catch (NotWritablePropertyException ex) {
                PropertyOrigin origin = OriginCapablePropertyValue.getOrigin(pv);
                if (isBenign(origin)) {
                    RelaxedDataBinder.logger.debug("Ignoring benign property binding failure", ex);
                } else {
                    if (origin == null) {
                        throw ex;
                    }
                    throw new RelaxedBindingNotWritablePropertyException(ex, origin);
                }
            }
        }

        private boolean isBenign(PropertyOrigin origin) {
            String name = origin != null ? origin.getSource().getName() : null;
            return BENIGN_PROPERTY_SOURCE_NAMES.contains(name);
        }
    }
}
