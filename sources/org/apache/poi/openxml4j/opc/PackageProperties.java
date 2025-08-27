package org.apache.poi.openxml4j.opc;

import java.util.Date;
import org.apache.poi.openxml4j.util.Nullable;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/opc/PackageProperties.class */
public interface PackageProperties {
    public static final String NAMESPACE_DCTERMS = "http://purl.org/dc/terms/";
    public static final String NAMESPACE_DC = "http://purl.org/dc/elements/1.1/";

    Nullable<String> getCategoryProperty();

    void setCategoryProperty(String str);

    Nullable<String> getContentStatusProperty();

    void setContentStatusProperty(String str);

    Nullable<String> getContentTypeProperty();

    void setContentTypeProperty(String str);

    Nullable<Date> getCreatedProperty();

    void setCreatedProperty(String str);

    void setCreatedProperty(Nullable<Date> nullable);

    Nullable<String> getCreatorProperty();

    void setCreatorProperty(String str);

    Nullable<String> getDescriptionProperty();

    void setDescriptionProperty(String str);

    Nullable<String> getIdentifierProperty();

    void setIdentifierProperty(String str);

    Nullable<String> getKeywordsProperty();

    void setKeywordsProperty(String str);

    Nullable<String> getLanguageProperty();

    void setLanguageProperty(String str);

    Nullable<String> getLastModifiedByProperty();

    void setLastModifiedByProperty(String str);

    Nullable<Date> getLastPrintedProperty();

    void setLastPrintedProperty(String str);

    void setLastPrintedProperty(Nullable<Date> nullable);

    Nullable<Date> getModifiedProperty();

    void setModifiedProperty(String str);

    void setModifiedProperty(Nullable<Date> nullable);

    Nullable<String> getRevisionProperty();

    void setRevisionProperty(String str);

    Nullable<String> getSubjectProperty();

    void setSubjectProperty(String str);

    Nullable<String> getTitleProperty();

    void setTitleProperty(String str);

    Nullable<String> getVersionProperty();

    void setVersionProperty(String str);
}
