package org.apache.poi.openxml4j.opc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.util.DocumentHelper;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/openxml4j/opc/PackageRelationshipCollection.class */
public final class PackageRelationshipCollection implements Iterable<PackageRelationship> {
    private static final POILogger logger = POILogFactory.getLogger((Class<?>) PackageRelationshipCollection.class);
    private TreeMap<String, PackageRelationship> relationshipsByID;
    private TreeMap<String, PackageRelationship> relationshipsByType;
    private HashMap<String, PackageRelationship> internalRelationshipsByTargetName;
    private PackagePart relationshipPart;
    private PackagePart sourcePart;
    private PackagePartName partName;
    private OPCPackage container;
    private int nextRelationshipId;

    PackageRelationshipCollection() {
        this.internalRelationshipsByTargetName = new HashMap<>();
        this.nextRelationshipId = -1;
        this.relationshipsByID = new TreeMap<>();
        this.relationshipsByType = new TreeMap<>();
    }

    public PackageRelationshipCollection(PackageRelationshipCollection coll, String filter) {
        this();
        for (PackageRelationship rel : coll.relationshipsByID.values()) {
            if (filter == null || rel.getRelationshipType().equals(filter)) {
                addRelationship(rel);
            }
        }
    }

    public PackageRelationshipCollection(OPCPackage container) throws InvalidFormatException {
        this(container, (PackagePart) null);
    }

    public PackageRelationshipCollection(PackagePart part) throws InvalidFormatException {
        this(part._container, part);
    }

    public PackageRelationshipCollection(OPCPackage container, PackagePart part) throws InvalidFormatException, DOMException {
        this();
        if (container == null) {
            throw new IllegalArgumentException("container needs to be specified");
        }
        if (part != null && part.isRelationshipPart()) {
            throw new IllegalArgumentException("part");
        }
        this.container = container;
        this.sourcePart = part;
        this.partName = getRelationshipPartName(part);
        if (container.getPackageAccess() != PackageAccess.WRITE && container.containPart(this.partName)) {
            this.relationshipPart = container.getPart(this.partName);
            parseRelationshipsPart(this.relationshipPart);
        }
    }

    private static PackagePartName getRelationshipPartName(PackagePart part) throws InvalidOperationException {
        PackagePartName partName;
        if (part == null) {
            partName = PackagingURIHelper.PACKAGE_ROOT_PART_NAME;
        } else {
            partName = part.getPartName();
        }
        return PackagingURIHelper.getRelationshipPartName(partName);
    }

    public void addRelationship(PackageRelationship relPart) {
        this.relationshipsByID.put(relPart.getId(), relPart);
        this.relationshipsByType.put(relPart.getRelationshipType(), relPart);
    }

    public PackageRelationship addRelationship(URI targetUri, TargetMode targetMode, String relationshipType, String id) {
        if (id == null) {
            if (this.nextRelationshipId == -1) {
                this.nextRelationshipId = size() + 1;
            }
            do {
                StringBuilder sbAppend = new StringBuilder().append("rId");
                int i = this.nextRelationshipId;
                this.nextRelationshipId = i + 1;
                id = sbAppend.append(i).toString();
            } while (this.relationshipsByID.get(id) != null);
        }
        PackageRelationship rel = new PackageRelationship(this.container, this.sourcePart, targetUri, targetMode, relationshipType, id);
        this.relationshipsByID.put(rel.getId(), rel);
        this.relationshipsByType.put(rel.getRelationshipType(), rel);
        if (targetMode == TargetMode.INTERNAL) {
            this.internalRelationshipsByTargetName.put(targetUri.toASCIIString(), rel);
        }
        return rel;
    }

    public void removeRelationship(String id) {
        PackageRelationship rel;
        if (this.relationshipsByID != null && this.relationshipsByType != null && (rel = this.relationshipsByID.get(id)) != null) {
            this.relationshipsByID.remove(rel.getId());
            this.relationshipsByType.values().remove(rel);
            this.internalRelationshipsByTargetName.values().remove(rel);
        }
    }

    public PackageRelationship getRelationship(int index) {
        if (index < 0 || index > this.relationshipsByID.values().size()) {
            throw new IllegalArgumentException(BeanDefinitionParserDelegate.INDEX_ATTRIBUTE);
        }
        int i = 0;
        for (PackageRelationship rel : this.relationshipsByID.values()) {
            int i2 = i;
            i++;
            if (index == i2) {
                return rel;
            }
        }
        return null;
    }

    public PackageRelationship getRelationshipByID(String id) {
        return this.relationshipsByID.get(id);
    }

    public int size() {
        return this.relationshipsByID.values().size();
    }

    public void parseRelationshipsPart(PackagePart relPart) throws InvalidFormatException, DOMException {
        try {
            logger.log(1, "Parsing relationship: " + relPart.getPartName());
            Document xmlRelationshipsDoc = DocumentHelper.readDocument(relPart.getInputStream());
            Element root = xmlRelationshipsDoc.getDocumentElement();
            boolean fCorePropertiesRelationship = false;
            NodeList nodeList = root.getElementsByTagNameNS(PackageNamespaces.RELATIONSHIPS, PackageRelationship.RELATIONSHIP_TAG_NAME);
            int nodeCount = nodeList.getLength();
            for (int i = 0; i < nodeCount; i++) {
                Element element = (Element) nodeList.item(i);
                String id = element.getAttribute(PackageRelationship.ID_ATTRIBUTE_NAME);
                String type = element.getAttribute("Type");
                if (type.equals(PackageRelationshipTypes.CORE_PROPERTIES)) {
                    if (!fCorePropertiesRelationship) {
                        fCorePropertiesRelationship = true;
                    } else {
                        throw new InvalidFormatException("OPC Compliance error [M4.1]: there is more than one core properties relationship in the package !");
                    }
                }
                Attr targetModeAttr = element.getAttributeNode(PackageRelationship.TARGET_MODE_ATTRIBUTE_NAME);
                TargetMode targetMode = TargetMode.INTERNAL;
                if (targetModeAttr != null) {
                    targetMode = targetModeAttr.getValue().toLowerCase(Locale.ROOT).equals("internal") ? TargetMode.INTERNAL : TargetMode.EXTERNAL;
                }
                URI target = PackagingURIHelper.toURI("http://invalid.uri");
                String value = element.getAttribute(PackageRelationship.TARGET_ATTRIBUTE_NAME);
                try {
                    target = PackagingURIHelper.toURI(value);
                } catch (URISyntaxException e) {
                    logger.log(7, "Cannot convert " + value + " in a valid relationship URI-> dummy-URI used", e);
                }
                addRelationship(target, targetMode, type, id);
            }
        } catch (Exception e2) {
            logger.log(7, e2);
            throw new InvalidFormatException(e2.getMessage());
        }
    }

    public PackageRelationshipCollection getRelationships(String typeFilter) {
        return new PackageRelationshipCollection(this, typeFilter);
    }

    @Override // java.lang.Iterable
    public Iterator<PackageRelationship> iterator() {
        return this.relationshipsByID.values().iterator();
    }

    public Iterator<PackageRelationship> iterator(String typeFilter) {
        ArrayList<PackageRelationship> retArr = new ArrayList<>();
        for (PackageRelationship rel : this.relationshipsByID.values()) {
            if (rel.getRelationshipType().equals(typeFilter)) {
                retArr.add(rel);
            }
        }
        return retArr.iterator();
    }

    public void clear() {
        this.relationshipsByID.clear();
        this.relationshipsByType.clear();
        this.internalRelationshipsByTargetName.clear();
    }

    public PackageRelationship findExistingInternalRelation(PackagePart packagePart) {
        return this.internalRelationshipsByTargetName.get(packagePart.getPartName().getName());
    }

    public String toString() {
        String str;
        String str2;
        String str3;
        String str4;
        if (this.relationshipsByID == null) {
            str = "relationshipsByID=null";
        } else {
            str = this.relationshipsByID.size() + " relationship(s) = [";
        }
        if (this.relationshipPart != null && this.relationshipPart._partName != null) {
            str2 = str + "," + this.relationshipPart._partName;
        } else {
            str2 = str + ",relationshipPart=null";
        }
        if (this.sourcePart != null && this.sourcePart._partName != null) {
            str3 = str2 + "," + this.sourcePart._partName;
        } else {
            str3 = str2 + ",sourcePart=null";
        }
        if (this.partName != null) {
            str4 = str3 + "," + this.partName;
        } else {
            str4 = str3 + ",uri=null)";
        }
        return str4 + "]";
    }
}
