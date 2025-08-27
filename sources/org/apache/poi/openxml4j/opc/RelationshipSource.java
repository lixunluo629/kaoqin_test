package org.apache.poi.openxml4j.opc;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/opc/RelationshipSource.class */
public interface RelationshipSource {
    PackageRelationship addRelationship(PackagePartName packagePartName, TargetMode targetMode, String str);

    PackageRelationship addRelationship(PackagePartName packagePartName, TargetMode targetMode, String str, String str2);

    PackageRelationship addExternalRelationship(String str, String str2);

    PackageRelationship addExternalRelationship(String str, String str2, String str3);

    void clearRelationships();

    void removeRelationship(String str);

    PackageRelationshipCollection getRelationships() throws OpenXML4JException;

    PackageRelationship getRelationship(String str);

    PackageRelationshipCollection getRelationshipsByType(String str) throws OpenXML4JException, IllegalArgumentException;

    boolean hasRelationships();

    boolean isRelationshipExists(PackageRelationship packageRelationship);
}
