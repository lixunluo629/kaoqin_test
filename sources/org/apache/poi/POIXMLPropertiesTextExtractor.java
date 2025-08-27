package org.apache.poi;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.xmp.PdfConst;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.poi.openxml4j.opc.internal.PackagePropertiesPart;
import org.apache.poi.util.LocaleUtil;
import org.hyperic.sigar.win32.EventLog;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;
import org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTProperties;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/POIXMLPropertiesTextExtractor.class */
public class POIXMLPropertiesTextExtractor extends POIXMLTextExtractor {
    private final DateFormat dateFormat;

    public POIXMLPropertiesTextExtractor(POIXMLDocument doc) {
        super(doc);
        DateFormatSymbols dfs = DateFormatSymbols.getInstance(Locale.ROOT);
        this.dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", dfs);
        this.dateFormat.setTimeZone(LocaleUtil.TIMEZONE_UTC);
    }

    public POIXMLPropertiesTextExtractor(POIXMLTextExtractor otherExtractor) {
        this(otherExtractor.getDocument());
    }

    private void appendIfPresent(StringBuffer text, String thing, boolean value) {
        appendIfPresent(text, thing, Boolean.toString(value));
    }

    private void appendIfPresent(StringBuffer text, String thing, int value) {
        appendIfPresent(text, thing, Integer.toString(value));
    }

    private void appendIfPresent(StringBuffer text, String thing, Date value) {
        if (value == null) {
            return;
        }
        appendIfPresent(text, thing, this.dateFormat.format(value));
    }

    private void appendIfPresent(StringBuffer text, String thing, String value) {
        if (value == null) {
            return;
        }
        text.append(thing);
        text.append(" = ");
        text.append(value);
        text.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
    }

    public String getCorePropertiesText() {
        POIXMLDocument document = getDocument();
        if (document == null) {
            return "";
        }
        StringBuffer text = new StringBuffer();
        PackagePropertiesPart props = document.getProperties().getCoreProperties().getUnderlyingProperties();
        appendIfPresent(text, "Category", props.getCategoryProperty().getValue());
        appendIfPresent(text, "Category", props.getCategoryProperty().getValue());
        appendIfPresent(text, "ContentStatus", props.getContentStatusProperty().getValue());
        appendIfPresent(text, "ContentType", props.getContentTypeProperty().getValue());
        appendIfPresent(text, "Created", props.getCreatedProperty().getValue());
        appendIfPresent(text, "CreatedString", props.getCreatedPropertyString());
        appendIfPresent(text, "Creator", props.getCreatorProperty().getValue());
        appendIfPresent(text, "Description", props.getDescriptionProperty().getValue());
        appendIfPresent(text, "Identifier", props.getIdentifierProperty().getValue());
        appendIfPresent(text, PdfConst.Keywords, props.getKeywordsProperty().getValue());
        appendIfPresent(text, "Language", props.getLanguageProperty().getValue());
        appendIfPresent(text, "LastModifiedBy", props.getLastModifiedByProperty().getValue());
        appendIfPresent(text, "LastPrinted", props.getLastPrintedProperty().getValue());
        appendIfPresent(text, "LastPrintedString", props.getLastPrintedPropertyString());
        appendIfPresent(text, "Modified", props.getModifiedProperty().getValue());
        appendIfPresent(text, "ModifiedString", props.getModifiedPropertyString());
        appendIfPresent(text, "Revision", props.getRevisionProperty().getValue());
        appendIfPresent(text, "Subject", props.getSubjectProperty().getValue());
        appendIfPresent(text, StandardRoles.TITLE, props.getTitleProperty().getValue());
        appendIfPresent(text, "Version", props.getVersionProperty().getValue());
        return text.toString();
    }

    public String getExtendedPropertiesText() {
        POIXMLDocument document = getDocument();
        if (document == null) {
            return "";
        }
        StringBuffer text = new StringBuffer();
        CTProperties props = document.getProperties().getExtendedProperties().getUnderlyingProperties();
        appendIfPresent(text, EventLog.APPLICATION, props.getApplication());
        appendIfPresent(text, "AppVersion", props.getAppVersion());
        appendIfPresent(text, "Characters", props.getCharacters());
        appendIfPresent(text, "CharactersWithSpaces", props.getCharactersWithSpaces());
        appendIfPresent(text, "Company", props.getCompany());
        appendIfPresent(text, "HyperlinkBase", props.getHyperlinkBase());
        appendIfPresent(text, "HyperlinksChanged", props.getHyperlinksChanged());
        appendIfPresent(text, "Lines", props.getLines());
        appendIfPresent(text, "LinksUpToDate", props.getLinksUpToDate());
        appendIfPresent(text, "Manager", props.getManager());
        appendIfPresent(text, "Pages", props.getPages());
        appendIfPresent(text, "Paragraphs", props.getParagraphs());
        appendIfPresent(text, "PresentationFormat", props.getPresentationFormat());
        appendIfPresent(text, "Template", props.getTemplate());
        appendIfPresent(text, "TotalTime", props.getTotalTime());
        return text.toString();
    }

    public String getCustomPropertiesText() {
        POIXMLDocument document = getDocument();
        if (document == null) {
            return "";
        }
        StringBuilder text = new StringBuilder();
        org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties props = document.getProperties().getCustomProperties().getUnderlyingProperties();
        CTProperty[] arr$ = props.getPropertyArray();
        for (CTProperty property : arr$) {
            String val = "(not implemented!)";
            if (property.isSetLpwstr()) {
                val = property.getLpwstr();
            } else if (property.isSetLpstr()) {
                val = property.getLpstr();
            } else if (property.isSetDate()) {
                val = property.getDate().toString();
            } else if (property.isSetFiletime()) {
                val = property.getFiletime().toString();
            } else if (property.isSetBool()) {
                val = Boolean.toString(property.getBool());
            } else if (property.isSetI1()) {
                val = Integer.toString(property.getI1());
            } else if (property.isSetI2()) {
                val = Integer.toString(property.getI2());
            } else if (property.isSetI4()) {
                val = Integer.toString(property.getI4());
            } else if (property.isSetI8()) {
                val = Long.toString(property.getI8());
            } else if (property.isSetInt()) {
                val = Integer.toString(property.getInt());
            } else if (property.isSetUi1()) {
                val = Integer.toString(property.getUi1());
            } else if (property.isSetUi2()) {
                val = Integer.toString(property.getUi2());
            } else if (property.isSetUi4()) {
                val = Long.toString(property.getUi4());
            } else if (property.isSetUi8()) {
                val = property.getUi8().toString();
            } else if (property.isSetUint()) {
                val = Long.toString(property.getUint());
            } else if (property.isSetR4()) {
                val = Float.toString(property.getR4());
            } else if (property.isSetR8()) {
                val = Double.toString(property.getR8());
            } else if (property.isSetDecimal()) {
                BigDecimal d = property.getDecimal();
                if (d == null) {
                    val = null;
                } else {
                    val = d.toPlainString();
                }
            }
            text.append(property.getName()).append(" = ").append(val).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        return text.toString();
    }

    @Override // org.apache.poi.POITextExtractor
    public String getText() {
        try {
            return getCorePropertiesText() + getExtendedPropertiesText() + getCustomPropertiesText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.apache.poi.POIXMLTextExtractor, org.apache.poi.POITextExtractor
    public POIXMLPropertiesTextExtractor getMetadataTextExtractor() {
        throw new IllegalStateException("You already have the Metadata Text Extractor, not recursing!");
    }
}
