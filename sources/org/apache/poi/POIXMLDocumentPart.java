package org.apache.poi;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.exceptions.PartAlreadyExistsException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/POIXMLDocumentPart.class */
public class POIXMLDocumentPart {
    private static final POILogger logger = POILogFactory.getLogger((Class<?>) POIXMLDocumentPart.class);
    private String coreDocumentRel;
    private PackagePart packagePart;
    private POIXMLDocumentPart parent;
    private Map<String, RelationPart> relations;
    private int relationCounter;

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/POIXMLDocumentPart$RelationPart.class */
    public static class RelationPart {
        private final PackageRelationship relationship;
        private final POIXMLDocumentPart documentPart;

        RelationPart(PackageRelationship relationship, POIXMLDocumentPart documentPart) {
            this.relationship = relationship;
            this.documentPart = documentPart;
        }

        public PackageRelationship getRelationship() {
            return this.relationship;
        }

        public <T extends POIXMLDocumentPart> T getDocumentPart() {
            return (T) this.documentPart;
        }
    }

    int incrementRelationCounter() {
        this.relationCounter++;
        return this.relationCounter;
    }

    int decrementRelationCounter() {
        this.relationCounter--;
        return this.relationCounter;
    }

    int getRelationCounter() {
        return this.relationCounter;
    }

    public POIXMLDocumentPart(OPCPackage pkg) {
        this(pkg, "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument");
    }

    public POIXMLDocumentPart(OPCPackage pkg, String coreDocumentRel) {
        this(getPartFromOPCPackage(pkg, coreDocumentRel));
        this.coreDocumentRel = coreDocumentRel;
    }

    public POIXMLDocumentPart() {
        this.coreDocumentRel = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument";
        this.relations = new LinkedHashMap();
        this.relationCounter = 0;
    }

    public POIXMLDocumentPart(PackagePart part) {
        this((POIXMLDocumentPart) null, part);
    }

    public POIXMLDocumentPart(POIXMLDocumentPart parent, PackagePart part) {
        this.coreDocumentRel = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument";
        this.relations = new LinkedHashMap();
        this.relationCounter = 0;
        this.packagePart = part;
        this.parent = parent;
    }

    protected final void rebase(OPCPackage pkg) throws InvalidFormatException, InvalidOperationException {
        PackageRelationshipCollection cores = this.packagePart.getRelationshipsByType(this.coreDocumentRel);
        if (cores.size() != 1) {
            throw new IllegalStateException("Tried to rebase using " + this.coreDocumentRel + " but found " + cores.size() + " parts of the right type");
        }
        this.packagePart = this.packagePart.getRelatedPart(cores.getRelationship(0));
    }

    public final PackagePart getPackagePart() {
        return this.packagePart;
    }

    public final List<POIXMLDocumentPart> getRelations() {
        List<POIXMLDocumentPart> l = new ArrayList<>();
        for (RelationPart rp : this.relations.values()) {
            l.add(rp.getDocumentPart());
        }
        return Collections.unmodifiableList(l);
    }

    public final List<RelationPart> getRelationParts() {
        List<RelationPart> l = new ArrayList<>(this.relations.values());
        return Collections.unmodifiableList(l);
    }

    public final POIXMLDocumentPart getRelationById(String id) {
        RelationPart rp = this.relations.get(id);
        if (rp == null) {
            return null;
        }
        return rp.getDocumentPart();
    }

    public final String getRelationId(POIXMLDocumentPart part) {
        for (RelationPart rp : this.relations.values()) {
            if (rp.getDocumentPart() == part) {
                return rp.getRelationship().getId();
            }
        }
        return null;
    }

    public final RelationPart addRelation(String relId, POIXMLRelation relationshipType, POIXMLDocumentPart part) throws InvalidOperationException {
        PackageRelationship pr = this.packagePart.findExistingRelation(part.getPackagePart());
        if (pr == null) {
            PackagePartName ppn = part.getPackagePart().getPartName();
            String relType = relationshipType.getRelation();
            pr = this.packagePart.addRelationship(ppn, TargetMode.INTERNAL, relType, relId);
        }
        addRelation(pr, part);
        return new RelationPart(pr, part);
    }

    private void addRelation(PackageRelationship pr, POIXMLDocumentPart part) {
        this.relations.put(pr.getId(), new RelationPart(pr, part));
        part.incrementRelationCounter();
    }

    protected final void removeRelation(POIXMLDocumentPart part) throws InvalidOperationException {
        removeRelation(part, true);
    }

    protected final boolean removeRelation(POIXMLDocumentPart part, boolean removeUnusedParts) throws InvalidOperationException {
        String id = getRelationId(part);
        if (id == null) {
            return false;
        }
        part.decrementRelationCounter();
        getPackagePart().removeRelationship(id);
        this.relations.remove(id);
        if (removeUnusedParts && part.getRelationCounter() == 0) {
            try {
                part.onDocumentRemove();
                getPackagePart().getPackage().removePart(part.getPackagePart());
                return true;
            } catch (IOException e) {
                throw new POIXMLException(e);
            }
        }
        return true;
    }

    public final POIXMLDocumentPart getParent() {
        return this.parent;
    }

    public String toString() {
        return this.packagePart == null ? "" : this.packagePart.toString();
    }

    protected void commit() throws IOException {
    }

    protected final void onSave(Set<PackagePart> alreadySaved) throws IOException {
        prepareForCommit();
        commit();
        alreadySaved.add(getPackagePart());
        for (RelationPart rp : this.relations.values()) {
            POIXMLDocumentPart p = rp.getDocumentPart();
            if (!alreadySaved.contains(p.getPackagePart())) {
                p.onSave(alreadySaved);
            }
        }
    }

    protected void prepareForCommit() {
        PackagePart part = getPackagePart();
        if (part != null) {
            part.clear();
        }
    }

    public final POIXMLDocumentPart createRelationship(POIXMLRelation descriptor, POIXMLFactory factory) {
        return createRelationship(descriptor, factory, -1, false).getDocumentPart();
    }

    public final POIXMLDocumentPart createRelationship(POIXMLRelation descriptor, POIXMLFactory factory, int idx) {
        return createRelationship(descriptor, factory, idx, false).getDocumentPart();
    }

    protected final int getNextPartNumber(POIXMLRelation descriptor, int minIdx) {
        OPCPackage pkg = this.packagePart.getPackage();
        try {
            String name = descriptor.getDefaultFileName();
            if (name.equals(descriptor.getFileName(9999))) {
                PackagePartName ppName = PackagingURIHelper.createPartName(name);
                if (pkg.containPart(ppName)) {
                    return -1;
                }
                return 0;
            }
            int maxIdx = minIdx + pkg.getParts().size();
            for (int idx = minIdx < 0 ? 1 : minIdx; idx <= maxIdx; idx++) {
                PackagePartName ppName2 = PackagingURIHelper.createPartName(descriptor.getFileName(idx));
                if (!pkg.containPart(ppName2)) {
                    return idx;
                }
            }
            return -1;
        } catch (InvalidFormatException e) {
            throw new POIXMLException(e);
        }
    }

    protected final RelationPart createRelationship(POIXMLRelation descriptor, POIXMLFactory factory, int idx, boolean noRelation) {
        try {
            PackagePartName ppName = PackagingURIHelper.createPartName(descriptor.getFileName(idx));
            PackageRelationship rel = null;
            PackagePart part = this.packagePart.getPackage().createPart(ppName, descriptor.getContentType());
            if (!noRelation) {
                rel = this.packagePart.addRelationship(ppName, TargetMode.INTERNAL, descriptor.getRelation());
            }
            POIXMLDocumentPart doc = factory.newDocumentPart(descriptor);
            doc.packagePart = part;
            doc.parent = this;
            if (!noRelation) {
                addRelation(rel, doc);
            }
            return new RelationPart(rel, doc);
        } catch (PartAlreadyExistsException pae) {
            throw pae;
        } catch (Exception e) {
            throw new POIXMLException(e);
        }
    }

    protected void read(POIXMLFactory factory, Map<PackagePart, POIXMLDocumentPart> context) throws OpenXML4JException, InvalidOperationException {
        PackagePartName relName;
        PackagePart pp = getPackagePart();
        POIXMLDocumentPart otherChild = context.put(pp, this);
        if (otherChild != null && otherChild != this) {
            throw new POIXMLException("Unique PackagePart-POIXMLDocumentPart relation broken!");
        }
        if (pp.hasRelationships()) {
            PackageRelationshipCollection rels = this.packagePart.getRelationships();
            List<POIXMLDocumentPart> readLater = new ArrayList<>();
            Iterator i$ = rels.iterator();
            while (i$.hasNext()) {
                PackageRelationship rel = i$.next();
                if (rel.getTargetMode() == TargetMode.INTERNAL) {
                    URI uri = rel.getTargetURI();
                    if (uri.getRawFragment() != null) {
                        relName = PackagingURIHelper.createPartName(uri.getPath());
                    } else {
                        relName = PackagingURIHelper.createPartName(uri);
                    }
                    PackagePart p = this.packagePart.getPackage().getPart(relName);
                    if (p == null) {
                        logger.log(7, "Skipped invalid entry " + rel.getTargetURI());
                    } else {
                        POIXMLDocumentPart childPart = context.get(p);
                        if (childPart == null) {
                            childPart = factory.createDocumentPart(this, p);
                            childPart.parent = this;
                            context.put(p, childPart);
                            readLater.add(childPart);
                        }
                        addRelation(rel, childPart);
                    }
                }
            }
            Iterator i$2 = readLater.iterator();
            while (i$2.hasNext()) {
                i$2.next().read(factory, context);
            }
        }
    }

    protected PackagePart getTargetPart(PackageRelationship rel) throws InvalidFormatException {
        return getPackagePart().getRelatedPart(rel);
    }

    protected void onDocumentCreate() throws IOException {
    }

    protected void onDocumentRead() throws IOException {
    }

    protected void onDocumentRemove() throws IOException {
    }

    @Internal
    @Deprecated
    public static void _invokeOnDocumentRead(POIXMLDocumentPart part) throws IOException {
        part.onDocumentRead();
    }

    private static PackagePart getPartFromOPCPackage(OPCPackage pkg, String coreDocumentRel) throws InvalidOperationException {
        PackageRelationship coreRel = pkg.getRelationshipsByType(coreDocumentRel).getRelationship(0);
        if (coreRel != null) {
            PackagePart pp = pkg.getPart(coreRel);
            if (pp == null) {
                throw new POIXMLException("OOXML file structure broken/invalid - core document '" + coreRel.getTargetURI() + "' not found.");
            }
            return pp;
        }
        if (pkg.getRelationshipsByType(PackageRelationshipTypes.STRICT_CORE_DOCUMENT).getRelationship(0) != null) {
            throw new POIXMLException("Strict OOXML isn't currently supported, please see bug #57699");
        }
        throw new POIXMLException("OOXML file structure broken/invalid - no core document found!");
    }
}
