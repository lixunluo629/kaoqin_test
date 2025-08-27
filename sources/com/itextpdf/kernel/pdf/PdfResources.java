package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.kernel.pdf.colorspace.PdfShading;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfResources.class */
public class PdfResources extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = 7160318458835945391L;
    private static final String F = "F";
    private static final String Im = "Im";
    private static final String Fm = "Fm";
    private static final String Gs = "Gs";
    private static final String Pr = "Pr";
    private static final String Cs = "Cs";
    private static final String P = "P";
    private static final String Sh = "Sh";
    private Map<PdfObject, PdfName> resourceToName;
    private ResourceNameGenerator fontNamesGen;
    private ResourceNameGenerator imageNamesGen;
    private ResourceNameGenerator formNamesGen;
    private ResourceNameGenerator egsNamesGen;
    private ResourceNameGenerator propNamesGen;
    private ResourceNameGenerator csNamesGen;
    private ResourceNameGenerator patternNamesGen;
    private ResourceNameGenerator shadingNamesGen;
    private boolean readOnly;
    private boolean isModified;

    public PdfResources(PdfDictionary pdfObject) {
        super(pdfObject);
        this.resourceToName = new HashMap();
        this.fontNamesGen = new ResourceNameGenerator(PdfName.Font, F);
        this.imageNamesGen = new ResourceNameGenerator(PdfName.XObject, Im);
        this.formNamesGen = new ResourceNameGenerator(PdfName.XObject, Fm);
        this.egsNamesGen = new ResourceNameGenerator(PdfName.ExtGState, Gs);
        this.propNamesGen = new ResourceNameGenerator(PdfName.Properties, Pr);
        this.csNamesGen = new ResourceNameGenerator(PdfName.ColorSpace, Cs);
        this.patternNamesGen = new ResourceNameGenerator(PdfName.Pattern, "P");
        this.shadingNamesGen = new ResourceNameGenerator(PdfName.Shading, Sh);
        this.readOnly = false;
        this.isModified = false;
        buildResources(pdfObject);
    }

    public PdfResources() {
        this(new PdfDictionary());
    }

    public PdfName addFont(PdfDocument pdfDocument, PdfFont font) {
        pdfDocument.addFont(font);
        return addResource(font, this.fontNamesGen);
    }

    public PdfName addImage(PdfImageXObject image) {
        return addResource(image, this.imageNamesGen);
    }

    public PdfName addImage(PdfStream image) {
        return addResource(image, this.imageNamesGen);
    }

    public PdfImageXObject getImage(PdfName name) {
        PdfStream image = getResource(PdfName.XObject).getAsStream(name);
        if (image == null || !PdfName.Image.equals(image.getAsName(PdfName.Subtype))) {
            return null;
        }
        return new PdfImageXObject(image);
    }

    public PdfName addForm(PdfFormXObject form) {
        return addResource(form, this.formNamesGen);
    }

    public PdfName addForm(PdfStream form) {
        return addResource(form, this.formNamesGen);
    }

    public PdfName addForm(PdfFormXObject form, PdfName name) {
        if (getResourceNames(PdfName.XObject).contains(name)) {
            name = addResource(form, this.formNamesGen);
        } else {
            addResource(form.getPdfObject(), PdfName.XObject, name);
        }
        return name;
    }

    public PdfFormXObject getForm(PdfName name) {
        PdfStream form = getResource(PdfName.XObject).getAsStream(name);
        if (form == null || !PdfName.Form.equals(form.getAsName(PdfName.Subtype))) {
            return null;
        }
        return new PdfFormXObject(form);
    }

    public PdfName addExtGState(PdfExtGState extGState) {
        return addResource(extGState, this.egsNamesGen);
    }

    public PdfName addExtGState(PdfDictionary extGState) {
        return addResource(extGState, this.egsNamesGen);
    }

    public PdfExtGState getPdfExtGState(PdfName name) {
        PdfDictionary dic = getResource(PdfName.ExtGState).getAsDictionary(name);
        if (dic != null) {
            return new PdfExtGState(dic);
        }
        return null;
    }

    public PdfName addProperties(PdfDictionary properties) {
        return addResource(properties, this.propNamesGen);
    }

    public PdfObject getProperties(PdfName name) {
        return getResourceObject(PdfName.Properties, name);
    }

    public PdfName addColorSpace(PdfColorSpace cs) {
        return addResource(cs, this.csNamesGen);
    }

    public PdfName addColorSpace(PdfObject colorSpace) {
        return addResource(colorSpace, this.csNamesGen);
    }

    public PdfColorSpace getColorSpace(PdfName name) {
        PdfObject colorSpace = getResourceObject(PdfName.ColorSpace, name);
        if (colorSpace != null) {
            return PdfColorSpace.makeColorSpace(colorSpace);
        }
        return null;
    }

    public PdfName addPattern(PdfPattern pattern) {
        return addResource(pattern, this.patternNamesGen);
    }

    public PdfName addPattern(PdfDictionary pattern) {
        return addResource(pattern, this.patternNamesGen);
    }

    public PdfPattern getPattern(PdfName name) {
        PdfObject pattern = getResourceObject(PdfName.Pattern, name);
        if (pattern instanceof PdfDictionary) {
            return PdfPattern.getPatternInstance((PdfDictionary) pattern);
        }
        return null;
    }

    public PdfName addShading(PdfShading shading) {
        return addResource(shading, this.shadingNamesGen);
    }

    public PdfName addShading(PdfDictionary shading) {
        return addResource(shading, this.shadingNamesGen);
    }

    public PdfShading getShading(PdfName name) {
        PdfObject shading = getResourceObject(PdfName.Shading, name);
        if (shading instanceof PdfDictionary) {
            return PdfShading.makeShading((PdfDictionary) shading);
        }
        return null;
    }

    protected boolean isReadOnly() {
        return this.readOnly;
    }

    protected void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    protected boolean isModified() {
        return this.isModified;
    }

    @Deprecated
    protected void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public PdfObjectWrapper<PdfDictionary> setModified() {
        this.isModified = true;
        return super.setModified();
    }

    public void setDefaultGray(PdfColorSpace defaultCs) {
        addResource(defaultCs.getPdfObject(), PdfName.ColorSpace, PdfName.DefaultGray);
    }

    public void setDefaultRgb(PdfColorSpace defaultCs) {
        addResource(defaultCs.getPdfObject(), PdfName.ColorSpace, PdfName.DefaultRGB);
    }

    public void setDefaultCmyk(PdfColorSpace defaultCs) {
        addResource(defaultCs.getPdfObject(), PdfName.ColorSpace, PdfName.DefaultCMYK);
    }

    public <T extends PdfObject> PdfName getResourceName(PdfObjectWrapper<T> resource) {
        return getResourceName(resource.getPdfObject());
    }

    public PdfName getResourceName(PdfObject resource) {
        PdfName resName = this.resourceToName.get(resource);
        if (resName == null) {
            resName = this.resourceToName.get(resource.getIndirectReference());
        }
        return resName;
    }

    public Set<PdfName> getResourceNames() {
        Set<PdfName> names = new TreeSet<>();
        for (PdfName resType : getPdfObject().keySet()) {
            names.addAll(getResourceNames(resType));
        }
        return names;
    }

    public PdfArray getProcSet() {
        return getPdfObject().getAsArray(PdfName.ProcSet);
    }

    public void setProcSet(PdfArray array) {
        getPdfObject().put(PdfName.ProcSet, array);
    }

    public Set<PdfName> getResourceNames(PdfName resType) {
        PdfDictionary resourceCategory = getPdfObject().getAsDictionary(resType);
        return resourceCategory == null ? new TreeSet() : resourceCategory.keySet();
    }

    public PdfDictionary getResource(PdfName resType) {
        return getPdfObject().getAsDictionary(resType);
    }

    public PdfObject getResourceObject(PdfName resType, PdfName resName) {
        PdfDictionary resource = getResource(resType);
        if (resource != null) {
            return resource.get(resName);
        }
        return null;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return false;
    }

    <T extends PdfObject> PdfName addResource(PdfObjectWrapper<T> resource, ResourceNameGenerator nameGen) {
        return addResource(resource.getPdfObject(), nameGen);
    }

    protected void addResource(PdfObject resource, PdfName resType, PdfName resName) {
        if (resType.equals(PdfName.XObject)) {
            checkAndResolveCircularReferences(resource);
        }
        if (this.readOnly) {
            setPdfObject(getPdfObject().clone(Collections.emptyList()));
            buildResources(getPdfObject());
            this.isModified = true;
            this.readOnly = false;
        }
        if (getPdfObject().containsKey(resType) && getPdfObject().getAsDictionary(resType).containsKey(resName)) {
            return;
        }
        this.resourceToName.put(resource, resName);
        PdfDictionary resourceCategory = getPdfObject().getAsDictionary(resType);
        if (resourceCategory == null) {
            PdfDictionary pdfObject = getPdfObject();
            PdfDictionary pdfDictionary = new PdfDictionary();
            resourceCategory = pdfDictionary;
            pdfObject.put(resType, pdfDictionary);
        }
        resourceCategory.put(resName, resource);
        setModified();
    }

    PdfName addResource(PdfObject resource, ResourceNameGenerator nameGen) {
        PdfName resName = getResourceName(resource);
        if (resName == null) {
            resName = nameGen.generate(this);
            addResource(resource, nameGen.getResourceType(), resName);
        }
        return resName;
    }

    protected void buildResources(PdfDictionary dictionary) {
        for (PdfName resourceType : dictionary.keySet()) {
            if (getPdfObject().get(resourceType) == null) {
                getPdfObject().put(resourceType, new PdfDictionary());
            }
            PdfDictionary resources = dictionary.getAsDictionary(resourceType);
            if (resources != null) {
                for (PdfName resourceName : resources.keySet()) {
                    PdfObject resource = resources.get(resourceName, false);
                    this.resourceToName.put(resource, resourceName);
                }
            }
        }
    }

    private void checkAndResolveCircularReferences(PdfObject pdfObject) {
        PdfDictionary pdfXObject;
        PdfObject pdfXObjectResources;
        if ((pdfObject instanceof PdfDictionary) && !pdfObject.isFlushed() && (pdfXObjectResources = (pdfXObject = (PdfDictionary) pdfObject).get(PdfName.Resources)) != null && pdfXObjectResources.getIndirectReference() != null && pdfXObjectResources.getIndirectReference().equals(getPdfObject().getIndirectReference())) {
            PdfObject cloneResources = getPdfObject().m850clone();
            cloneResources.makeIndirect(getPdfObject().getIndirectReference().getDocument());
            pdfXObject.put(PdfName.Resources, cloneResources.getIndirectReference());
        }
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/PdfResources$ResourceNameGenerator.class */
    static class ResourceNameGenerator implements Serializable {
        private static final long serialVersionUID = 1729961083476558303L;
        private PdfName resourceType;
        private int counter;
        private String prefix;

        public ResourceNameGenerator(PdfName resourceType, String prefix, int seed) {
            this.prefix = prefix;
            this.resourceType = resourceType;
            this.counter = seed;
        }

        public ResourceNameGenerator(PdfName resourceType, String prefix) {
            this(resourceType, prefix, 1);
        }

        public PdfName getResourceType() {
            return this.resourceType;
        }

        public PdfName generate(PdfResources resources) {
            StringBuilder sbAppend = new StringBuilder().append(this.prefix);
            int i = this.counter;
            this.counter = i + 1;
            PdfName newName = new PdfName(sbAppend.append(i).toString());
            PdfDictionary r = resources.getPdfObject();
            if (r.containsKey(this.resourceType)) {
                while (r.getAsDictionary(this.resourceType).containsKey(newName)) {
                    StringBuilder sbAppend2 = new StringBuilder().append(this.prefix);
                    int i2 = this.counter;
                    this.counter = i2 + 1;
                    newName = new PdfName(sbAppend2.append(i2).toString());
                }
            }
            return newName;
        }
    }
}
