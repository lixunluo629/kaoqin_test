package org.springframework.web.bind;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.core.CollectionFactory;
import org.springframework.validation.DataBinder;
import org.springframework.web.multipart.MultipartFile;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/WebDataBinder.class */
public class WebDataBinder extends DataBinder {
    public static final String DEFAULT_FIELD_MARKER_PREFIX = "_";
    public static final String DEFAULT_FIELD_DEFAULT_PREFIX = "!";
    private String fieldMarkerPrefix;
    private String fieldDefaultPrefix;
    private boolean bindEmptyMultipartFiles;

    public WebDataBinder(Object target) {
        super(target);
        this.fieldMarkerPrefix = "_";
        this.fieldDefaultPrefix = "!";
        this.bindEmptyMultipartFiles = true;
    }

    public WebDataBinder(Object target, String objectName) {
        super(target, objectName);
        this.fieldMarkerPrefix = "_";
        this.fieldDefaultPrefix = "!";
        this.bindEmptyMultipartFiles = true;
    }

    public void setFieldMarkerPrefix(String fieldMarkerPrefix) {
        this.fieldMarkerPrefix = fieldMarkerPrefix;
    }

    public String getFieldMarkerPrefix() {
        return this.fieldMarkerPrefix;
    }

    public void setFieldDefaultPrefix(String fieldDefaultPrefix) {
        this.fieldDefaultPrefix = fieldDefaultPrefix;
    }

    public String getFieldDefaultPrefix() {
        return this.fieldDefaultPrefix;
    }

    public void setBindEmptyMultipartFiles(boolean bindEmptyMultipartFiles) {
        this.bindEmptyMultipartFiles = bindEmptyMultipartFiles;
    }

    public boolean isBindEmptyMultipartFiles() {
        return this.bindEmptyMultipartFiles;
    }

    @Override // org.springframework.validation.DataBinder
    protected void doBind(MutablePropertyValues mpvs) {
        checkFieldDefaults(mpvs);
        checkFieldMarkers(mpvs);
        super.doBind(mpvs);
    }

    protected void checkFieldDefaults(MutablePropertyValues mpvs) {
        String fieldDefaultPrefix = getFieldDefaultPrefix();
        if (fieldDefaultPrefix != null) {
            PropertyValue[] pvArray = mpvs.getPropertyValues();
            for (PropertyValue pv : pvArray) {
                if (pv.getName().startsWith(fieldDefaultPrefix)) {
                    String field = pv.getName().substring(fieldDefaultPrefix.length());
                    if (getPropertyAccessor().isWritableProperty(field) && !mpvs.contains(field)) {
                        mpvs.add(field, pv.getValue());
                    }
                    mpvs.removePropertyValue(pv);
                }
            }
        }
    }

    protected void checkFieldMarkers(MutablePropertyValues mpvs) {
        String fieldMarkerPrefix = getFieldMarkerPrefix();
        if (fieldMarkerPrefix != null) {
            PropertyValue[] pvArray = mpvs.getPropertyValues();
            for (PropertyValue pv : pvArray) {
                if (pv.getName().startsWith(fieldMarkerPrefix)) {
                    String field = pv.getName().substring(fieldMarkerPrefix.length());
                    if (getPropertyAccessor().isWritableProperty(field) && !mpvs.contains(field)) {
                        Class<?> fieldType = getPropertyAccessor().getPropertyType(field);
                        mpvs.add(field, getEmptyValue(field, fieldType));
                    }
                    mpvs.removePropertyValue(pv);
                }
            }
        }
    }

    protected Object getEmptyValue(String field, Class<?> fieldType) {
        if (fieldType != null) {
            try {
                if (Boolean.TYPE == fieldType || Boolean.class == fieldType) {
                    return Boolean.FALSE;
                }
                if (fieldType.isArray()) {
                    return Array.newInstance(fieldType.getComponentType(), 0);
                }
                if (Collection.class.isAssignableFrom(fieldType)) {
                    return CollectionFactory.createCollection(fieldType, 0);
                }
                if (Map.class.isAssignableFrom(fieldType)) {
                    return CollectionFactory.createMap(fieldType, 0);
                }
                return null;
            } catch (IllegalArgumentException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to create default value - falling back to null: " + ex.getMessage());
                    return null;
                }
                return null;
            }
        }
        return null;
    }

    protected void bindMultipart(Map<String, List<MultipartFile>> multipartFiles, MutablePropertyValues mpvs) {
        for (Map.Entry<String, List<MultipartFile>> entry : multipartFiles.entrySet()) {
            String key = entry.getKey();
            List<MultipartFile> values = entry.getValue();
            if (values.size() == 1) {
                MultipartFile value = values.get(0);
                if (isBindEmptyMultipartFiles() || !value.isEmpty()) {
                    mpvs.add(key, value);
                }
            } else {
                mpvs.add(key, values);
            }
        }
    }
}
