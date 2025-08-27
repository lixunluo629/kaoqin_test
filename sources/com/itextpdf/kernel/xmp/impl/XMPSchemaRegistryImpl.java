package com.itextpdf.kernel.xmp.impl;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.xmp.PdfConst;
import com.itextpdf.kernel.xmp.XMPConst;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPSchemaRegistry;
import com.itextpdf.kernel.xmp.options.AliasOptions;
import com.itextpdf.kernel.xmp.properties.XMPAliasInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.apache.poi.openxml4j.opc.ContentTypes;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/XMPSchemaRegistryImpl.class */
public final class XMPSchemaRegistryImpl implements XMPConst, XMPSchemaRegistry {
    private Map namespaceToPrefixMap = new HashMap();
    private Map prefixToNamespaceMap = new HashMap();
    private Map aliasMap = new HashMap();
    private Pattern p = Pattern.compile("[/*?\\[\\]]");

    public XMPSchemaRegistryImpl() {
        try {
            registerStandardNamespaces();
            registerStandardAliases();
        } catch (XMPException e) {
            throw new RuntimeException("The XMPSchemaRegistry cannot be initialized!");
        }
    }

    @Override // com.itextpdf.kernel.xmp.XMPSchemaRegistry
    public synchronized String registerNamespace(String namespaceURI, String suggestedPrefix) throws XMPException {
        ParameterAsserts.assertSchemaNS(namespaceURI);
        ParameterAsserts.assertPrefix(suggestedPrefix);
        if (suggestedPrefix.charAt(suggestedPrefix.length() - 1) != ':') {
            suggestedPrefix = suggestedPrefix + ':';
        }
        if (!Utils.isXMLNameNS(suggestedPrefix.substring(0, suggestedPrefix.length() - 1))) {
            throw new XMPException("The prefix is a bad XML name", 201);
        }
        String registeredPrefix = (String) this.namespaceToPrefixMap.get(namespaceURI);
        String registeredNS = (String) this.prefixToNamespaceMap.get(suggestedPrefix);
        if (registeredPrefix != null) {
            return registeredPrefix;
        }
        if (registeredNS != null) {
            String generatedPrefix = suggestedPrefix;
            int i = 1;
            while (this.prefixToNamespaceMap.containsKey(generatedPrefix)) {
                generatedPrefix = suggestedPrefix.substring(0, suggestedPrefix.length() - 1) + "_" + i + "_:";
                i++;
            }
            suggestedPrefix = generatedPrefix;
        }
        this.prefixToNamespaceMap.put(suggestedPrefix, namespaceURI);
        this.namespaceToPrefixMap.put(namespaceURI, suggestedPrefix);
        return suggestedPrefix;
    }

    @Override // com.itextpdf.kernel.xmp.XMPSchemaRegistry
    public synchronized void deleteNamespace(String namespaceURI) {
        String prefixToDelete = getNamespacePrefix(namespaceURI);
        if (prefixToDelete != null) {
            this.namespaceToPrefixMap.remove(namespaceURI);
            this.prefixToNamespaceMap.remove(prefixToDelete);
        }
    }

    @Override // com.itextpdf.kernel.xmp.XMPSchemaRegistry
    public synchronized String getNamespacePrefix(String namespaceURI) {
        return (String) this.namespaceToPrefixMap.get(namespaceURI);
    }

    @Override // com.itextpdf.kernel.xmp.XMPSchemaRegistry
    public synchronized String getNamespaceURI(String namespacePrefix) {
        if (namespacePrefix != null && !namespacePrefix.endsWith(":")) {
            namespacePrefix = namespacePrefix + ":";
        }
        return (String) this.prefixToNamespaceMap.get(namespacePrefix);
    }

    @Override // com.itextpdf.kernel.xmp.XMPSchemaRegistry
    public synchronized Map getNamespaces() {
        return Collections.unmodifiableMap(new TreeMap(this.namespaceToPrefixMap));
    }

    @Override // com.itextpdf.kernel.xmp.XMPSchemaRegistry
    public synchronized Map getPrefixes() {
        return Collections.unmodifiableMap(new TreeMap(this.prefixToNamespaceMap));
    }

    private void registerStandardNamespaces() throws XMPException {
        registerNamespace("http://www.w3.org/XML/1998/namespace", "xml");
        registerNamespace("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf");
        registerNamespace("http://purl.org/dc/elements/1.1/", "dc");
        registerNamespace("http://iptc.org/std/Iptc4xmpCore/1.0/xmlns/", "Iptc4xmpCore");
        registerNamespace("http://iptc.org/std/Iptc4xmpExt/2008-02-29/", "Iptc4xmpExt");
        registerNamespace("http://ns.adobe.com/DICOM/", "DICOM");
        registerNamespace("http://ns.useplus.org/ldf/xmp/1.0/", "plus");
        registerNamespace("adobe:ns:meta/", "x");
        registerNamespace("http://ns.adobe.com/iX/1.0/", "iX");
        registerNamespace("http://ns.adobe.com/xap/1.0/", "xmp");
        registerNamespace("http://ns.adobe.com/xap/1.0/rights/", "xmpRights");
        registerNamespace("http://ns.adobe.com/xap/1.0/mm/", "xmpMM");
        registerNamespace("http://ns.adobe.com/xap/1.0/bj/", "xmpBJ");
        registerNamespace("http://ns.adobe.com/xmp/note/", "xmpNote");
        registerNamespace("http://ns.adobe.com/pdf/1.3/", "pdf");
        registerNamespace("http://ns.adobe.com/pdfx/1.3/", "pdfx");
        registerNamespace("http://www.npes.org/pdfx/ns/id/", "pdfxid");
        registerNamespace("http://www.aiim.org/pdfa/ns/schema#", "pdfaSchema");
        registerNamespace("http://www.aiim.org/pdfa/ns/property#", "pdfaProperty");
        registerNamespace("http://www.aiim.org/pdfa/ns/type#", "pdfaType");
        registerNamespace("http://www.aiim.org/pdfa/ns/field#", "pdfaField");
        registerNamespace("http://www.aiim.org/pdfa/ns/id/", "pdfaid");
        registerNamespace(XMPConst.NS_PDFUA_ID, "pdfuaid");
        registerNamespace("http://www.aiim.org/pdfa/ns/extension/", "pdfaExtension");
        registerNamespace("http://ns.adobe.com/photoshop/1.0/", "photoshop");
        registerNamespace("http://ns.adobe.com/album/1.0/", "album");
        registerNamespace("http://ns.adobe.com/exif/1.0/", "exif");
        registerNamespace("http://cipa.jp/exif/1.0/", "exifEX");
        registerNamespace("http://ns.adobe.com/exif/1.0/aux/", "aux");
        registerNamespace("http://ns.adobe.com/tiff/1.0/", "tiff");
        registerNamespace("http://ns.adobe.com/png/1.0/", ContentTypes.EXTENSION_PNG);
        registerNamespace("http://ns.adobe.com/jpeg/1.0/", ContentTypes.EXTENSION_JPG_2);
        registerNamespace("http://ns.adobe.com/jp2k/1.0/", "jp2k");
        registerNamespace("http://ns.adobe.com/camera-raw-settings/1.0/", "crs");
        registerNamespace("http://ns.adobe.com/StockPhoto/1.0/", "bmsp");
        registerNamespace("http://ns.adobe.com/creatorAtom/1.0/", "creatorAtom");
        registerNamespace("http://ns.adobe.com/asf/1.0/", "asf");
        registerNamespace("http://ns.adobe.com/xmp/wav/1.0/", "wav");
        registerNamespace("http://ns.adobe.com/bwf/bext/1.0/", "bext");
        registerNamespace("http://ns.adobe.com/riff/info/", "riffinfo");
        registerNamespace("http://ns.adobe.com/xmp/1.0/Script/", "xmpScript");
        registerNamespace("http://ns.adobe.com/TransformXMP/", "txmp");
        registerNamespace("http://ns.adobe.com/swf/1.0/", "swf");
        registerNamespace("http://ns.adobe.com/xmp/1.0/DynamicMedia/", "xmpDM");
        registerNamespace("http://ns.adobe.com/xmp/transient/1.0/", "xmpx");
        registerNamespace("http://ns.adobe.com/xap/1.0/t/", "xmpT");
        registerNamespace("http://ns.adobe.com/xap/1.0/t/pg/", "xmpTPg");
        registerNamespace("http://ns.adobe.com/xap/1.0/g/", "xmpG");
        registerNamespace("http://ns.adobe.com/xap/1.0/g/img/", "xmpGImg");
        registerNamespace("http://ns.adobe.com/xap/1.0/sType/Font#", "stFnt");
        registerNamespace("http://ns.adobe.com/xap/1.0/sType/Dimensions#", "stDim");
        registerNamespace("http://ns.adobe.com/xap/1.0/sType/ResourceEvent#", "stEvt");
        registerNamespace("http://ns.adobe.com/xap/1.0/sType/ResourceRef#", "stRef");
        registerNamespace("http://ns.adobe.com/xap/1.0/sType/Version#", "stVer");
        registerNamespace("http://ns.adobe.com/xap/1.0/sType/Job#", "stJob");
        registerNamespace("http://ns.adobe.com/xap/1.0/sType/ManifestItem#", "stMfs");
        registerNamespace("http://ns.adobe.com/xmp/Identifier/qual/1.0/", "xmpidq");
    }

    @Override // com.itextpdf.kernel.xmp.XMPSchemaRegistry
    public synchronized XMPAliasInfo resolveAlias(String aliasNS, String aliasProp) {
        String aliasPrefix = getNamespacePrefix(aliasNS);
        if (aliasPrefix == null) {
            return null;
        }
        return (XMPAliasInfo) this.aliasMap.get(aliasPrefix + aliasProp);
    }

    @Override // com.itextpdf.kernel.xmp.XMPSchemaRegistry
    public synchronized XMPAliasInfo findAlias(String qname) {
        return (XMPAliasInfo) this.aliasMap.get(qname);
    }

    @Override // com.itextpdf.kernel.xmp.XMPSchemaRegistry
    public synchronized XMPAliasInfo[] findAliases(String aliasNS) {
        String prefix = getNamespacePrefix(aliasNS);
        List<XMPAliasInfo> result = new ArrayList<>();
        if (prefix != null) {
            for (Object key : this.aliasMap.keySet()) {
                String qname = (String) key;
                if (qname.startsWith(prefix)) {
                    result.add(findAlias(qname));
                }
            }
        }
        return (XMPAliasInfo[]) result.toArray(new XMPAliasInfo[result.size()]);
    }

    synchronized void registerAlias(String aliasNS, String aliasProp, final String actualNS, final String actualProp, AliasOptions aliasForm) throws XMPException {
        ParameterAsserts.assertSchemaNS(aliasNS);
        ParameterAsserts.assertPropName(aliasProp);
        ParameterAsserts.assertSchemaNS(actualNS);
        ParameterAsserts.assertPropName(actualProp);
        final AliasOptions aliasOpts = aliasForm != null ? new AliasOptions(XMPNodeUtils.verifySetOptions(aliasForm.toPropertyOptions(), null).getOptions()) : new AliasOptions();
        if (this.p.matcher(aliasProp).find() || this.p.matcher(actualProp).find()) {
            throw new XMPException("Alias and actual property names must be simple", 102);
        }
        String aliasPrefix = getNamespacePrefix(aliasNS);
        final String actualPrefix = getNamespacePrefix(actualNS);
        if (aliasPrefix == null) {
            throw new XMPException("Alias namespace is not registered", 101);
        }
        if (actualPrefix == null) {
            throw new XMPException("Actual namespace is not registered", 101);
        }
        String key = aliasPrefix + aliasProp;
        if (this.aliasMap.containsKey(key)) {
            throw new XMPException("Alias is already existing", 4);
        }
        if (this.aliasMap.containsKey(actualPrefix + actualProp)) {
            throw new XMPException("Actual property is already an alias, use the base property", 4);
        }
        XMPAliasInfo aliasInfo = new XMPAliasInfo() { // from class: com.itextpdf.kernel.xmp.impl.XMPSchemaRegistryImpl.1
            @Override // com.itextpdf.kernel.xmp.properties.XMPAliasInfo
            public String getNamespace() {
                return actualNS;
            }

            @Override // com.itextpdf.kernel.xmp.properties.XMPAliasInfo
            public String getPrefix() {
                return actualPrefix;
            }

            @Override // com.itextpdf.kernel.xmp.properties.XMPAliasInfo
            public String getPropName() {
                return actualProp;
            }

            @Override // com.itextpdf.kernel.xmp.properties.XMPAliasInfo
            public AliasOptions getAliasForm() {
                return aliasOpts;
            }

            public String toString() {
                return actualPrefix + actualProp + " NS(" + actualNS + "), FORM (" + getAliasForm() + ")";
            }
        };
        this.aliasMap.put(key, aliasInfo);
    }

    @Override // com.itextpdf.kernel.xmp.XMPSchemaRegistry
    public synchronized Map getAliases() {
        return Collections.unmodifiableMap(new TreeMap(this.aliasMap));
    }

    private void registerStandardAliases() throws XMPException {
        AliasOptions aliasToArrayOrdered = new AliasOptions().setArrayOrdered(true);
        AliasOptions aliasToArrayAltText = new AliasOptions().setArrayAltText(true);
        registerAlias("http://ns.adobe.com/xap/1.0/", "Author", "http://purl.org/dc/elements/1.1/", PdfConst.Creator, aliasToArrayOrdered);
        registerAlias("http://ns.adobe.com/xap/1.0/", "Authors", "http://purl.org/dc/elements/1.1/", PdfConst.Creator, null);
        registerAlias("http://ns.adobe.com/xap/1.0/", "Description", "http://purl.org/dc/elements/1.1/", "description", null);
        registerAlias("http://ns.adobe.com/xap/1.0/", "Format", "http://purl.org/dc/elements/1.1/", "format", null);
        registerAlias("http://ns.adobe.com/xap/1.0/", PdfConst.Keywords, "http://purl.org/dc/elements/1.1/", PdfConst.Subject, null);
        registerAlias("http://ns.adobe.com/xap/1.0/", "Locale", "http://purl.org/dc/elements/1.1/", PdfConst.Language, null);
        registerAlias("http://ns.adobe.com/xap/1.0/", StandardRoles.TITLE, "http://purl.org/dc/elements/1.1/", "title", null);
        registerAlias("http://ns.adobe.com/xap/1.0/rights/", "Copyright", "http://purl.org/dc/elements/1.1/", PdfConst.Rights, null);
        registerAlias("http://ns.adobe.com/pdf/1.3/", "Author", "http://purl.org/dc/elements/1.1/", PdfConst.Creator, aliasToArrayOrdered);
        registerAlias("http://ns.adobe.com/pdf/1.3/", PdfConst.BaseURL, "http://ns.adobe.com/xap/1.0/", PdfConst.BaseURL, null);
        registerAlias("http://ns.adobe.com/pdf/1.3/", "CreationDate", "http://ns.adobe.com/xap/1.0/", PdfConst.CreateDate, null);
        registerAlias("http://ns.adobe.com/pdf/1.3/", "Creator", "http://ns.adobe.com/xap/1.0/", PdfConst.CreatorTool, null);
        registerAlias("http://ns.adobe.com/pdf/1.3/", "ModDate", "http://ns.adobe.com/xap/1.0/", PdfConst.ModifyDate, null);
        registerAlias("http://ns.adobe.com/pdf/1.3/", "Subject", "http://purl.org/dc/elements/1.1/", "description", aliasToArrayAltText);
        registerAlias("http://ns.adobe.com/pdf/1.3/", StandardRoles.TITLE, "http://purl.org/dc/elements/1.1/", "title", aliasToArrayAltText);
        registerAlias("http://ns.adobe.com/photoshop/1.0/", "Author", "http://purl.org/dc/elements/1.1/", PdfConst.Creator, aliasToArrayOrdered);
        registerAlias("http://ns.adobe.com/photoshop/1.0/", StandardRoles.CAPTION, "http://purl.org/dc/elements/1.1/", "description", aliasToArrayAltText);
        registerAlias("http://ns.adobe.com/photoshop/1.0/", "Copyright", "http://purl.org/dc/elements/1.1/", PdfConst.Rights, aliasToArrayAltText);
        registerAlias("http://ns.adobe.com/photoshop/1.0/", PdfConst.Keywords, "http://purl.org/dc/elements/1.1/", PdfConst.Subject, null);
        registerAlias("http://ns.adobe.com/photoshop/1.0/", "Marked", "http://ns.adobe.com/xap/1.0/rights/", "Marked", null);
        registerAlias("http://ns.adobe.com/photoshop/1.0/", StandardRoles.TITLE, "http://purl.org/dc/elements/1.1/", "title", aliasToArrayAltText);
        registerAlias("http://ns.adobe.com/photoshop/1.0/", "WebStatement", "http://ns.adobe.com/xap/1.0/rights/", "WebStatement", null);
        registerAlias("http://ns.adobe.com/tiff/1.0/", "Artist", "http://purl.org/dc/elements/1.1/", PdfConst.Creator, aliasToArrayOrdered);
        registerAlias("http://ns.adobe.com/tiff/1.0/", "Copyright", "http://purl.org/dc/elements/1.1/", PdfConst.Rights, null);
        registerAlias("http://ns.adobe.com/tiff/1.0/", "DateTime", "http://ns.adobe.com/xap/1.0/", PdfConst.ModifyDate, null);
        registerAlias("http://ns.adobe.com/tiff/1.0/", "ImageDescription", "http://purl.org/dc/elements/1.1/", "description", null);
        registerAlias("http://ns.adobe.com/tiff/1.0/", "Software", "http://ns.adobe.com/xap/1.0/", PdfConst.CreatorTool, null);
        registerAlias("http://ns.adobe.com/png/1.0/", "Author", "http://purl.org/dc/elements/1.1/", PdfConst.Creator, aliasToArrayOrdered);
        registerAlias("http://ns.adobe.com/png/1.0/", "Copyright", "http://purl.org/dc/elements/1.1/", PdfConst.Rights, aliasToArrayAltText);
        registerAlias("http://ns.adobe.com/png/1.0/", "CreationTime", "http://ns.adobe.com/xap/1.0/", PdfConst.CreateDate, null);
        registerAlias("http://ns.adobe.com/png/1.0/", "Description", "http://purl.org/dc/elements/1.1/", "description", aliasToArrayAltText);
        registerAlias("http://ns.adobe.com/png/1.0/", "ModificationTime", "http://ns.adobe.com/xap/1.0/", PdfConst.ModifyDate, null);
        registerAlias("http://ns.adobe.com/png/1.0/", "Software", "http://ns.adobe.com/xap/1.0/", PdfConst.CreatorTool, null);
        registerAlias("http://ns.adobe.com/png/1.0/", StandardRoles.TITLE, "http://purl.org/dc/elements/1.1/", "title", aliasToArrayAltText);
    }
}
