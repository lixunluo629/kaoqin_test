package com.itextpdf.kernel.pdf.tagging;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.util.MessageFormatUtil;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfObjectWrapper;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import org.apache.ibatis.ognl.OgnlContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/tagging/PdfNamespace.class */
public class PdfNamespace extends PdfObjectWrapper<PdfDictionary> {
    private static final long serialVersionUID = -4228596885910641569L;

    public PdfNamespace(PdfDictionary dictionary) {
        super(dictionary);
        setForbidRelease();
    }

    public PdfNamespace(String namespaceName) {
        this(new PdfString(namespaceName));
    }

    public PdfNamespace(PdfString namespaceName) {
        this(new PdfDictionary());
        put(PdfName.Type, PdfName.Namespace);
        put(PdfName.NS, namespaceName);
    }

    public PdfNamespace setNamespaceName(String namespaceName) {
        return setNamespaceName(new PdfString(namespaceName));
    }

    public PdfNamespace setNamespaceName(PdfString namespaceName) {
        return put(PdfName.NS, namespaceName);
    }

    public String getNamespaceName() {
        PdfString ns = getPdfObject().getAsString(PdfName.NS);
        if (ns != null) {
            return ns.toUnicodeString();
        }
        return null;
    }

    public PdfNamespace setSchema(PdfFileSpec fileSpec) {
        return put(PdfName.Schema, fileSpec.getPdfObject());
    }

    public PdfFileSpec getSchema() {
        PdfObject schemaObject = getPdfObject().get(PdfName.Schema);
        return PdfFileSpec.wrapFileSpecObject(schemaObject);
    }

    public PdfNamespace setNamespaceRoleMap(PdfDictionary roleMapNs) {
        return put(PdfName.RoleMapNS, roleMapNs);
    }

    public PdfDictionary getNamespaceRoleMap() {
        return getNamespaceRoleMap(false);
    }

    public PdfNamespace addNamespaceRoleMapping(String thisNsRole, String defaultNsRole) {
        PdfObject prevVal = getNamespaceRoleMap(true).put(PdfStructTreeRoot.convertRoleToPdfName(thisNsRole), PdfStructTreeRoot.convertRoleToPdfName(defaultNsRole));
        logOverwritingOfMappingIfNeeded(thisNsRole, prevVal);
        setModified();
        return this;
    }

    public PdfNamespace addNamespaceRoleMapping(String thisNsRole, String targetNsRole, PdfNamespace targetNs) {
        PdfArray targetMapping = new PdfArray();
        targetMapping.add(PdfStructTreeRoot.convertRoleToPdfName(targetNsRole));
        targetMapping.add(targetNs.getPdfObject());
        PdfObject prevVal = getNamespaceRoleMap(true).put(PdfStructTreeRoot.convertRoleToPdfName(thisNsRole), targetMapping);
        logOverwritingOfMappingIfNeeded(thisNsRole, prevVal);
        setModified();
        return this;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    protected boolean isWrappedObjectMustBeIndirect() {
        return true;
    }

    private PdfNamespace put(PdfName key, PdfObject value) {
        getPdfObject().put(key, value);
        setModified();
        return this;
    }

    private PdfDictionary getNamespaceRoleMap(boolean createIfNotExist) {
        PdfDictionary roleMapNs = getPdfObject().getAsDictionary(PdfName.RoleMapNS);
        if (createIfNotExist && roleMapNs == null) {
            roleMapNs = new PdfDictionary();
            put(PdfName.RoleMapNS, roleMapNs);
        }
        return roleMapNs;
    }

    private void logOverwritingOfMappingIfNeeded(String thisNsRole, PdfObject prevVal) {
        if (prevVal != null) {
            Logger logger = LoggerFactory.getLogger((Class<?>) PdfNamespace.class);
            String nsNameStr = getNamespaceName();
            if (nsNameStr == null) {
                nsNameStr = OgnlContext.THIS_CONTEXT_KEY;
            }
            logger.warn(MessageFormatUtil.format(LogMessageConstant.MAPPING_IN_NAMESPACE_OVERWRITTEN, thisNsRole, nsNameStr));
        }
    }
}
