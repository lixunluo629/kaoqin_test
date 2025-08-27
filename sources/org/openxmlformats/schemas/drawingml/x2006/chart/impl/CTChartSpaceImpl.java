package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTExternalData;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTStyle;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTextLanguageID;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: poi-ooxml-schemas-3.17.jar:org/openxmlformats/schemas/drawingml/x2006/chart/impl/CTChartSpaceImpl.class */
public class CTChartSpaceImpl extends XmlComplexContentImpl implements CTChartSpace {
    private static final QName DATE1904$0 = new QName(XSSFRelation.NS_CHART, "date1904");
    private static final QName LANG$2 = new QName(XSSFRelation.NS_CHART, AbstractHtmlElementTag.LANG_ATTRIBUTE);
    private static final QName ROUNDEDCORNERS$4 = new QName(XSSFRelation.NS_CHART, "roundedCorners");
    private static final QName STYLE$6 = new QName(XSSFRelation.NS_CHART, AbstractHtmlElementTag.STYLE_ATTRIBUTE);
    private static final QName CLRMAPOVR$8 = new QName(XSSFRelation.NS_CHART, "clrMapOvr");
    private static final QName PIVOTSOURCE$10 = new QName(XSSFRelation.NS_CHART, "pivotSource");
    private static final QName PROTECTION$12 = new QName(XSSFRelation.NS_CHART, "protection");
    private static final QName CHART$14 = new QName(XSSFRelation.NS_CHART, "chart");
    private static final QName SPPR$16 = new QName(XSSFRelation.NS_CHART, "spPr");
    private static final QName TXPR$18 = new QName(XSSFRelation.NS_CHART, "txPr");
    private static final QName EXTERNALDATA$20 = new QName(XSSFRelation.NS_CHART, "externalData");
    private static final QName PRINTSETTINGS$22 = new QName(XSSFRelation.NS_CHART, "printSettings");
    private static final QName USERSHAPES$24 = new QName(XSSFRelation.NS_CHART, "userShapes");
    private static final QName EXTLST$26 = new QName(XSSFRelation.NS_CHART, "extLst");

    public CTChartSpaceImpl(SchemaType schemaType) {
        super(schemaType);
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTBoolean getDate1904() {
        synchronized (monitor()) {
            check_orphaned();
            CTBoolean cTBoolean = (CTBoolean) get_store().find_element_user(DATE1904$0, 0);
            if (cTBoolean == null) {
                return null;
            }
            return cTBoolean;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetDate1904() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(DATE1904$0) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setDate1904(CTBoolean cTBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            CTBoolean cTBoolean2 = (CTBoolean) get_store().find_element_user(DATE1904$0, 0);
            if (cTBoolean2 == null) {
                cTBoolean2 = (CTBoolean) get_store().add_element_user(DATE1904$0);
            }
            cTBoolean2.set(cTBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTBoolean addNewDate1904() {
        CTBoolean cTBoolean;
        synchronized (monitor()) {
            check_orphaned();
            cTBoolean = (CTBoolean) get_store().add_element_user(DATE1904$0);
        }
        return cTBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetDate1904() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(DATE1904$0, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTTextLanguageID getLang() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextLanguageID cTTextLanguageIDFind_element_user = get_store().find_element_user(LANG$2, 0);
            if (cTTextLanguageIDFind_element_user == null) {
                return null;
            }
            return cTTextLanguageIDFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetLang() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(LANG$2) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setLang(CTTextLanguageID cTTextLanguageID) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextLanguageID cTTextLanguageIDFind_element_user = get_store().find_element_user(LANG$2, 0);
            if (cTTextLanguageIDFind_element_user == null) {
                cTTextLanguageIDFind_element_user = (CTTextLanguageID) get_store().add_element_user(LANG$2);
            }
            cTTextLanguageIDFind_element_user.set(cTTextLanguageID);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTTextLanguageID addNewLang() {
        CTTextLanguageID cTTextLanguageIDAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTTextLanguageIDAdd_element_user = get_store().add_element_user(LANG$2);
        }
        return cTTextLanguageIDAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetLang() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(LANG$2, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTBoolean getRoundedCorners() {
        synchronized (monitor()) {
            check_orphaned();
            CTBoolean cTBoolean = (CTBoolean) get_store().find_element_user(ROUNDEDCORNERS$4, 0);
            if (cTBoolean == null) {
                return null;
            }
            return cTBoolean;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetRoundedCorners() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(ROUNDEDCORNERS$4) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setRoundedCorners(CTBoolean cTBoolean) {
        synchronized (monitor()) {
            check_orphaned();
            CTBoolean cTBoolean2 = (CTBoolean) get_store().find_element_user(ROUNDEDCORNERS$4, 0);
            if (cTBoolean2 == null) {
                cTBoolean2 = (CTBoolean) get_store().add_element_user(ROUNDEDCORNERS$4);
            }
            cTBoolean2.set(cTBoolean);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTBoolean addNewRoundedCorners() {
        CTBoolean cTBoolean;
        synchronized (monitor()) {
            check_orphaned();
            cTBoolean = (CTBoolean) get_store().add_element_user(ROUNDEDCORNERS$4);
        }
        return cTBoolean;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetRoundedCorners() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(ROUNDEDCORNERS$4, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTStyle getStyle() {
        synchronized (monitor()) {
            check_orphaned();
            CTStyle cTStyleFind_element_user = get_store().find_element_user(STYLE$6, 0);
            if (cTStyleFind_element_user == null) {
                return null;
            }
            return cTStyleFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetStyle() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(STYLE$6) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setStyle(CTStyle cTStyle) {
        synchronized (monitor()) {
            check_orphaned();
            CTStyle cTStyleFind_element_user = get_store().find_element_user(STYLE$6, 0);
            if (cTStyleFind_element_user == null) {
                cTStyleFind_element_user = (CTStyle) get_store().add_element_user(STYLE$6);
            }
            cTStyleFind_element_user.set(cTStyle);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTStyle addNewStyle() {
        CTStyle cTStyleAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTStyleAdd_element_user = get_store().add_element_user(STYLE$6);
        }
        return cTStyleAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(STYLE$6, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTColorMapping getClrMapOvr() {
        synchronized (monitor()) {
            check_orphaned();
            CTColorMapping cTColorMapping = (CTColorMapping) get_store().find_element_user(CLRMAPOVR$8, 0);
            if (cTColorMapping == null) {
                return null;
            }
            return cTColorMapping;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetClrMapOvr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(CLRMAPOVR$8) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setClrMapOvr(CTColorMapping cTColorMapping) {
        synchronized (monitor()) {
            check_orphaned();
            CTColorMapping cTColorMapping2 = (CTColorMapping) get_store().find_element_user(CLRMAPOVR$8, 0);
            if (cTColorMapping2 == null) {
                cTColorMapping2 = (CTColorMapping) get_store().add_element_user(CLRMAPOVR$8);
            }
            cTColorMapping2.set(cTColorMapping);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTColorMapping addNewClrMapOvr() {
        CTColorMapping cTColorMapping;
        synchronized (monitor()) {
            check_orphaned();
            cTColorMapping = (CTColorMapping) get_store().add_element_user(CLRMAPOVR$8);
        }
        return cTColorMapping;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetClrMapOvr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(CLRMAPOVR$8, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTPivotSource getPivotSource() {
        synchronized (monitor()) {
            check_orphaned();
            CTPivotSource cTPivotSourceFind_element_user = get_store().find_element_user(PIVOTSOURCE$10, 0);
            if (cTPivotSourceFind_element_user == null) {
                return null;
            }
            return cTPivotSourceFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetPivotSource() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PIVOTSOURCE$10) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setPivotSource(CTPivotSource cTPivotSource) {
        synchronized (monitor()) {
            check_orphaned();
            CTPivotSource cTPivotSourceFind_element_user = get_store().find_element_user(PIVOTSOURCE$10, 0);
            if (cTPivotSourceFind_element_user == null) {
                cTPivotSourceFind_element_user = (CTPivotSource) get_store().add_element_user(PIVOTSOURCE$10);
            }
            cTPivotSourceFind_element_user.set(cTPivotSource);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTPivotSource addNewPivotSource() {
        CTPivotSource cTPivotSourceAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTPivotSourceAdd_element_user = get_store().add_element_user(PIVOTSOURCE$10);
        }
        return cTPivotSourceAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetPivotSource() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PIVOTSOURCE$10, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTProtection getProtection() {
        synchronized (monitor()) {
            check_orphaned();
            CTProtection cTProtectionFind_element_user = get_store().find_element_user(PROTECTION$12, 0);
            if (cTProtectionFind_element_user == null) {
                return null;
            }
            return cTProtectionFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetProtection() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PROTECTION$12) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setProtection(CTProtection cTProtection) {
        synchronized (monitor()) {
            check_orphaned();
            CTProtection cTProtectionFind_element_user = get_store().find_element_user(PROTECTION$12, 0);
            if (cTProtectionFind_element_user == null) {
                cTProtectionFind_element_user = (CTProtection) get_store().add_element_user(PROTECTION$12);
            }
            cTProtectionFind_element_user.set(cTProtection);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTProtection addNewProtection() {
        CTProtection cTProtectionAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTProtectionAdd_element_user = get_store().add_element_user(PROTECTION$12);
        }
        return cTProtectionAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetProtection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROTECTION$12, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTChart getChart() {
        synchronized (monitor()) {
            check_orphaned();
            CTChart cTChart = (CTChart) get_store().find_element_user(CHART$14, 0);
            if (cTChart == null) {
                return null;
            }
            return cTChart;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setChart(CTChart cTChart) {
        synchronized (monitor()) {
            check_orphaned();
            CTChart cTChart2 = (CTChart) get_store().find_element_user(CHART$14, 0);
            if (cTChart2 == null) {
                cTChart2 = (CTChart) get_store().add_element_user(CHART$14);
            }
            cTChart2.set(cTChart);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTChart addNewChart() {
        CTChart cTChart;
        synchronized (monitor()) {
            check_orphaned();
            cTChart = (CTChart) get_store().add_element_user(CHART$14);
        }
        return cTChart;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTShapeProperties getSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTShapeProperties cTShapeProperties = (CTShapeProperties) get_store().find_element_user(SPPR$16, 0);
            if (cTShapeProperties == null) {
                return null;
            }
            return cTShapeProperties;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetSpPr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(SPPR$16) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setSpPr(CTShapeProperties cTShapeProperties) {
        synchronized (monitor()) {
            check_orphaned();
            CTShapeProperties cTShapeProperties2 = (CTShapeProperties) get_store().find_element_user(SPPR$16, 0);
            if (cTShapeProperties2 == null) {
                cTShapeProperties2 = (CTShapeProperties) get_store().add_element_user(SPPR$16);
            }
            cTShapeProperties2.set(cTShapeProperties);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTShapeProperties addNewSpPr() {
        CTShapeProperties cTShapeProperties;
        synchronized (monitor()) {
            check_orphaned();
            cTShapeProperties = (CTShapeProperties) get_store().add_element_user(SPPR$16);
        }
        return cTShapeProperties;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(SPPR$16, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTTextBody getTxPr() {
        synchronized (monitor()) {
            check_orphaned();
            CTTextBody cTTextBody = (CTTextBody) get_store().find_element_user(TXPR$18, 0);
            if (cTTextBody == null) {
                return null;
            }
            return cTTextBody;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetTxPr() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(TXPR$18) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setTxPr(CTTextBody cTTextBody) {
        synchronized (monitor()) {
            check_orphaned();
            CTTextBody cTTextBody2 = (CTTextBody) get_store().find_element_user(TXPR$18, 0);
            if (cTTextBody2 == null) {
                cTTextBody2 = (CTTextBody) get_store().add_element_user(TXPR$18);
            }
            cTTextBody2.set(cTTextBody);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTTextBody addNewTxPr() {
        CTTextBody cTTextBody;
        synchronized (monitor()) {
            check_orphaned();
            cTTextBody = (CTTextBody) get_store().add_element_user(TXPR$18);
        }
        return cTTextBody;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetTxPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(TXPR$18, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTExternalData getExternalData() {
        synchronized (monitor()) {
            check_orphaned();
            CTExternalData cTExternalDataFind_element_user = get_store().find_element_user(EXTERNALDATA$20, 0);
            if (cTExternalDataFind_element_user == null) {
                return null;
            }
            return cTExternalDataFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetExternalData() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTERNALDATA$20) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setExternalData(CTExternalData cTExternalData) {
        synchronized (monitor()) {
            check_orphaned();
            CTExternalData cTExternalDataFind_element_user = get_store().find_element_user(EXTERNALDATA$20, 0);
            if (cTExternalDataFind_element_user == null) {
                cTExternalDataFind_element_user = (CTExternalData) get_store().add_element_user(EXTERNALDATA$20);
            }
            cTExternalDataFind_element_user.set(cTExternalData);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTExternalData addNewExternalData() {
        CTExternalData cTExternalDataAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExternalDataAdd_element_user = get_store().add_element_user(EXTERNALDATA$20);
        }
        return cTExternalDataAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetExternalData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTERNALDATA$20, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTPrintSettings getPrintSettings() {
        synchronized (monitor()) {
            check_orphaned();
            CTPrintSettings cTPrintSettings = (CTPrintSettings) get_store().find_element_user(PRINTSETTINGS$22, 0);
            if (cTPrintSettings == null) {
                return null;
            }
            return cTPrintSettings;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetPrintSettings() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(PRINTSETTINGS$22) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setPrintSettings(CTPrintSettings cTPrintSettings) {
        synchronized (monitor()) {
            check_orphaned();
            CTPrintSettings cTPrintSettings2 = (CTPrintSettings) get_store().find_element_user(PRINTSETTINGS$22, 0);
            if (cTPrintSettings2 == null) {
                cTPrintSettings2 = (CTPrintSettings) get_store().add_element_user(PRINTSETTINGS$22);
            }
            cTPrintSettings2.set(cTPrintSettings);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTPrintSettings addNewPrintSettings() {
        CTPrintSettings cTPrintSettings;
        synchronized (monitor()) {
            check_orphaned();
            cTPrintSettings = (CTPrintSettings) get_store().add_element_user(PRINTSETTINGS$22);
        }
        return cTPrintSettings;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetPrintSettings() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PRINTSETTINGS$22, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTRelId getUserShapes() {
        synchronized (monitor()) {
            check_orphaned();
            CTRelId cTRelIdFind_element_user = get_store().find_element_user(USERSHAPES$24, 0);
            if (cTRelIdFind_element_user == null) {
                return null;
            }
            return cTRelIdFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetUserShapes() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(USERSHAPES$24) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setUserShapes(CTRelId cTRelId) {
        synchronized (monitor()) {
            check_orphaned();
            CTRelId cTRelIdFind_element_user = get_store().find_element_user(USERSHAPES$24, 0);
            if (cTRelIdFind_element_user == null) {
                cTRelIdFind_element_user = (CTRelId) get_store().add_element_user(USERSHAPES$24);
            }
            cTRelIdFind_element_user.set(cTRelId);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTRelId addNewUserShapes() {
        CTRelId cTRelIdAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTRelIdAdd_element_user = get_store().add_element_user(USERSHAPES$24);
        }
        return cTRelIdAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetUserShapes() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(USERSHAPES$24, 0);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$26, 0);
            if (cTExtensionListFind_element_user == null) {
                return null;
            }
            return cTExtensionListFind_element_user;
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public boolean isSetExtLst() {
        boolean z;
        synchronized (monitor()) {
            check_orphaned();
            z = get_store().count_elements(EXTLST$26) != 0;
        }
        return z;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void setExtLst(CTExtensionList cTExtensionList) {
        synchronized (monitor()) {
            check_orphaned();
            CTExtensionList cTExtensionListFind_element_user = get_store().find_element_user(EXTLST$26, 0);
            if (cTExtensionListFind_element_user == null) {
                cTExtensionListFind_element_user = (CTExtensionList) get_store().add_element_user(EXTLST$26);
            }
            cTExtensionListFind_element_user.set(cTExtensionList);
        }
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public CTExtensionList addNewExtLst() {
        CTExtensionList cTExtensionListAdd_element_user;
        synchronized (monitor()) {
            check_orphaned();
            cTExtensionListAdd_element_user = get_store().add_element_user(EXTLST$26);
        }
        return cTExtensionListAdd_element_user;
    }

    @Override // org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(EXTLST$26, 0);
        }
    }
}
