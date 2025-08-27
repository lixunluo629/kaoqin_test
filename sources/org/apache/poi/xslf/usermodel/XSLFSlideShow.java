package org.apache.poi.xslf.usermodel;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.util.Internal;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList;
import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPresentation;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry;
import org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument;
import org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument;
import org.openxmlformats.schemas.presentationml.x2006.main.PresentationDocument;
import org.openxmlformats.schemas.presentationml.x2006.main.SldDocument;
import org.openxmlformats.schemas.presentationml.x2006.main.SldMasterDocument;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFSlideShow.class */
public class XSLFSlideShow extends POIXMLDocument {
    private PresentationDocument presentationDoc;
    private List<PackagePart> embedds;

    public XSLFSlideShow(OPCPackage container) throws XmlException, OpenXML4JException, IOException, InvalidOperationException {
        super(container);
        if (getCorePart().getContentType().equals(XSLFRelation.THEME_MANAGER.getContentType())) {
            rebase(getPackage());
        }
        this.presentationDoc = PresentationDocument.Factory.parse(getCorePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        this.embedds = new LinkedList();
        CTSlideIdListEntry[] arr$ = getSlideReferences().getSldIdArray();
        for (CTSlideIdListEntry ctSlide : arr$) {
            PackagePart corePart = getCorePart();
            PackagePart slidePart = corePart.getRelatedPart(corePart.getRelationship(ctSlide.getId2()));
            Iterator i$ = slidePart.getRelationshipsByType(POIXMLDocument.OLE_OBJECT_REL_TYPE).iterator();
            while (i$.hasNext()) {
                PackageRelationship rel = i$.next();
                if (TargetMode.EXTERNAL != rel.getTargetMode()) {
                    this.embedds.add(slidePart.getRelatedPart(rel));
                }
            }
            Iterator i$2 = slidePart.getRelationshipsByType(POIXMLDocument.PACK_OBJECT_REL_TYPE).iterator();
            while (i$2.hasNext()) {
                this.embedds.add(slidePart.getRelatedPart(i$2.next()));
            }
        }
    }

    public XSLFSlideShow(String file) throws XmlException, OpenXML4JException, IOException {
        this(openPackage(file));
    }

    @Internal
    public CTPresentation getPresentation() {
        return this.presentationDoc.getPresentation();
    }

    @Internal
    public CTSlideIdList getSlideReferences() {
        if (!getPresentation().isSetSldIdLst()) {
            getPresentation().setSldIdLst(CTSlideIdList.Factory.newInstance());
        }
        return getPresentation().getSldIdLst();
    }

    @Internal
    public CTSlideMasterIdList getSlideMasterReferences() {
        return getPresentation().getSldMasterIdLst();
    }

    public PackagePart getSlideMasterPart(CTSlideMasterIdListEntry master) throws XmlException, IOException {
        try {
            PackagePart corePart = getCorePart();
            return corePart.getRelatedPart(corePart.getRelationship(master.getId2()));
        } catch (InvalidFormatException e) {
            throw new XmlException(e);
        }
    }

    @Internal
    public CTSlideMaster getSlideMaster(CTSlideMasterIdListEntry master) throws XmlException, IOException {
        PackagePart masterPart = getSlideMasterPart(master);
        SldMasterDocument masterDoc = SldMasterDocument.Factory.parse(masterPart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        return masterDoc.getSldMaster();
    }

    public PackagePart getSlidePart(CTSlideIdListEntry slide) throws XmlException, IOException {
        try {
            PackagePart corePart = getCorePart();
            return corePart.getRelatedPart(corePart.getRelationship(slide.getId2()));
        } catch (InvalidFormatException e) {
            throw new XmlException(e);
        }
    }

    @Internal
    public CTSlide getSlide(CTSlideIdListEntry slide) throws XmlException, IOException {
        PackagePart slidePart = getSlidePart(slide);
        SldDocument slideDoc = SldDocument.Factory.parse(slidePart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        return slideDoc.getSld();
    }

    public PackagePart getNodesPart(CTSlideIdListEntry parentSlide) throws XmlException, IOException, InvalidOperationException {
        PackagePart slidePart = getSlidePart(parentSlide);
        try {
            PackageRelationshipCollection notes = slidePart.getRelationshipsByType(XSLFRelation.NOTES.getRelation());
            if (notes.size() == 0) {
                return null;
            }
            if (notes.size() > 1) {
                throw new IllegalStateException("Expecting 0 or 1 notes for a slide, but found " + notes.size());
            }
            try {
                return slidePart.getRelatedPart(notes.getRelationship(0));
            } catch (InvalidFormatException e) {
                throw new IllegalStateException(e);
            }
        } catch (InvalidFormatException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Internal
    public CTNotesSlide getNotes(CTSlideIdListEntry slide) throws XmlException, IOException, InvalidOperationException {
        PackagePart notesPart = getNodesPart(slide);
        if (notesPart == null) {
            return null;
        }
        NotesDocument notesDoc = NotesDocument.Factory.parse(notesPart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        return notesDoc.getNotes();
    }

    @Internal
    public CTCommentList getSlideComments(CTSlideIdListEntry slide) throws XmlException, IOException, InvalidOperationException {
        PackagePart slidePart = getSlidePart(slide);
        try {
            PackageRelationshipCollection commentRels = slidePart.getRelationshipsByType(XSLFRelation.COMMENTS.getRelation());
            if (commentRels.size() == 0) {
                return null;
            }
            if (commentRels.size() > 1) {
                throw new IllegalStateException("Expecting 0 or 1 comments for a slide, but found " + commentRels.size());
            }
            try {
                PackagePart cPart = slidePart.getRelatedPart(commentRels.getRelationship(0));
                CmLstDocument commDoc = CmLstDocument.Factory.parse(cPart.getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
                return commDoc.getCmLst();
            } catch (InvalidFormatException e) {
                throw new IllegalStateException(e);
            }
        } catch (InvalidFormatException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // org.apache.poi.POIXMLDocument
    public List<PackagePart> getAllEmbedds() throws OpenXML4JException {
        return this.embedds;
    }
}
