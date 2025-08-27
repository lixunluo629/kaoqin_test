package org.apache.xmlbeans.impl.xsd2inst;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.swagger.models.properties.StringProperty;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.xmlbeans.GDate;
import org.apache.xmlbeans.GDateBuilder;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.GDurationBuilder;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDate;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlDecimal;
import org.apache.xmlbeans.XmlDuration;
import org.apache.xmlbeans.XmlGDay;
import org.apache.xmlbeans.XmlGMonth;
import org.apache.xmlbeans.XmlGMonthDay;
import org.apache.xmlbeans.XmlGYear;
import org.apache.xmlbeans.XmlGYearMonth;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlTime;
import org.apache.xmlbeans.impl.schema.SoapEncSchemaTypeSystem;
import org.apache.xmlbeans.impl.util.Base64;
import org.apache.xmlbeans.impl.util.HexBin;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xsd2inst/SampleXmlUtil.class */
public class SampleXmlUtil {
    private boolean _soapEnc;
    private static final int MAX_ELEMENTS = 1000;
    private int _nElements;
    public static final String[] WORDS = {"ipsa", "iovis", "rapidum", "iaculata", "e", "nubibus", "ignem", "disiecitque", "rates", "evertitque", "aequora", "ventis", "illum", "exspirantem", "transfixo", "pectore", "flammas", "turbine", "corripuit", "scopuloque", "infixit", "acuto", "ast", "ego", "quae", "divum", "incedo", "regina", "iovisque", "et", "soror", "et", "coniunx", "una", "cum", "gente", "tot", "annos", "bella", "gero", "et", "quisquam", "numen", "iunonis", "adorat", "praeterea", "aut", "supplex", "aris", "imponet", "honorem", "talia", "flammato", "secum", "dea", "corde", "volutans", "nimborum", "in", "patriam", "loca", "feta", "furentibus", "austris", "aeoliam", "venit", "hic", "vasto", "rex", "aeolus", "antro", "luctantis", "ventos", "tempestatesque", "sonoras", "imperio", "premit", "ac", "vinclis", "et", "carcere", "frenat", "illi", "indignantes", "magno", "cum", "murmure", "montis", "circum", "claustra", "fremunt", "celsa", "sedet", "aeolus", "arce", "sceptra", "tenens", "mollitque", "animos", "et", "temperat", "iras", "ni", "faciat", "maria", "ac", "terras", "caelumque", "profundum", "quippe", "ferant", "rapidi", "secum", "verrantque", "per", "auras", "sed", "pater", "omnipotens", "speluncis", "abdidit", "atris", "hoc", "metuens", "molemque", "et", "montis", "insuper", "altos", "imposuit", "regemque", "dedit", "qui", "foedere", "certo", "et", "premere", "et", "laxas", "sciret", "dare", "iussus", "habenas"};
    private static final String[] DNS1 = {"corp", "your", "my", "sample", "company", "test", Languages.ANY};
    private static final String[] DNS2 = {"com", "org", "com", "gov", "org", "com", "org", "com", "edu"};
    private static final QName HREF = new QName("href");
    private static final QName ID = new QName("id");
    private static final QName XSI_TYPE = new QName("http://www.w3.org/2001/XMLSchema-instance", "type");
    private static final QName ENC_ARRAYTYPE = new QName("http://schemas.xmlsoap.org/soap/encoding/", SoapEncSchemaTypeSystem.ARRAY_TYPE);
    private static final QName ENC_OFFSET = new QName("http://schemas.xmlsoap.org/soap/encoding/", "offset");
    private static final Set SKIPPED_SOAP_ATTRS = new HashSet(Arrays.asList(HREF, ID, ENC_OFFSET));
    Random _picker = new Random(1);
    private ArrayList _typeStack = new ArrayList();

    private SampleXmlUtil(boolean soapEnc) {
        this._soapEnc = soapEnc;
    }

    public static String createSampleForType(SchemaType sType) {
        XmlObject object = XmlObject.Factory.newInstance();
        XmlCursor cursor = object.newCursor();
        cursor.toNextToken();
        new SampleXmlUtil(false).createSampleForType(sType, cursor);
        XmlOptions options = new XmlOptions();
        options.put(XmlOptions.SAVE_PRETTY_PRINT);
        options.put(XmlOptions.SAVE_PRETTY_PRINT_INDENT, 2);
        options.put(XmlOptions.SAVE_AGGRESSIVE_NAMESPACES);
        String result = object.xmlText(options);
        return result;
    }

    private void createSampleForType(SchemaType stype, XmlCursor xmlc) {
        if (this._typeStack.contains(stype)) {
            return;
        }
        this._typeStack.add(stype);
        try {
            if (stype.isSimpleType() || stype.isURType()) {
                processSimpleType(stype, xmlc);
                this._typeStack.remove(this._typeStack.size() - 1);
                return;
            }
            processAttributes(stype, xmlc);
            switch (stype.getContentType()) {
                case 2:
                    processSimpleType(stype, xmlc);
                    break;
                case 3:
                    if (stype.getContentModel() != null) {
                        processParticle(stype.getContentModel(), xmlc, false);
                        break;
                    }
                    break;
                case 4:
                    xmlc.insertChars(pick(WORDS) + SymbolConstants.SPACE_SYMBOL);
                    if (stype.getContentModel() != null) {
                        processParticle(stype.getContentModel(), xmlc, true);
                    }
                    xmlc.insertChars(pick(WORDS));
                    break;
            }
        } finally {
            this._typeStack.remove(this._typeStack.size() - 1);
        }
    }

    private void processSimpleType(SchemaType stype, XmlCursor xmlc) {
        String sample = sampleDataForSimpleType(stype);
        xmlc.insertChars(sample);
    }

    private String sampleDataForSimpleType(SchemaType sType) {
        String result;
        if (XmlObject.type.equals(sType)) {
            return "anyType";
        }
        if (XmlAnySimpleType.type.equals(sType)) {
            return "anySimpleType";
        }
        if (sType.getSimpleVariety() == 3) {
            SchemaType itemType = sType.getListItemType();
            StringBuffer sb = new StringBuffer();
            int length = pickLength(sType);
            if (length > 0) {
                sb.append(sampleDataForSimpleType(itemType));
            }
            for (int i = 1; i < length; i++) {
                sb.append(' ');
                sb.append(sampleDataForSimpleType(itemType));
            }
            return sb.toString();
        }
        if (sType.getSimpleVariety() == 2) {
            SchemaType[] possibleTypes = sType.getUnionConstituentTypes();
            if (possibleTypes.length == 0) {
                return "";
            }
            return sampleDataForSimpleType(possibleTypes[pick(possibleTypes.length)]);
        }
        XmlAnySimpleType[] enumValues = sType.getEnumerationValues();
        if (enumValues != null && enumValues.length > 0) {
            return enumValues[pick(enumValues.length)].getStringValue();
        }
        switch (sType.getPrimitiveType().getBuiltinTypeCode()) {
            case 0:
            default:
                return "";
            case 1:
            case 2:
                return "anything";
            case 3:
                return pick(2) == 0 ? "true" : "false";
            case 4:
                String result2 = null;
                try {
                    result2 = new String(Base64.encode(formatToLength(pick(WORDS), sType).getBytes("utf-8")));
                } catch (UnsupportedEncodingException e) {
                }
                return result2;
            case 5:
                return HexBin.encode(formatToLength(pick(WORDS), sType));
            case 6:
                return formatToLength("http://www." + pick(DNS1) + "." + pick(DNS2) + "/" + pick(WORDS) + "/" + pick(WORDS), sType);
            case 7:
                return formatToLength("qname", sType);
            case 8:
                return formatToLength("notation", sType);
            case 9:
                return "1.5E2";
            case 10:
                return "1.051732E7";
            case 11:
                switch (closestBuiltin(sType).getBuiltinTypeCode()) {
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    default:
                        return formatDecimal("1000.00", sType);
                    case 22:
                        return formatDecimal("100", sType);
                    case 23:
                        return formatDecimal("10", sType);
                    case 24:
                        return formatDecimal("3", sType);
                    case 25:
                        return formatDecimal("1", sType);
                    case 26:
                        return formatDecimal("2", sType);
                    case 27:
                        return formatDecimal("-200", sType);
                    case 28:
                        return formatDecimal("-201", sType);
                    case 29:
                        return formatDecimal("200", sType);
                    case 30:
                        return formatDecimal("201", sType);
                    case 31:
                        return formatDecimal("11", sType);
                    case 32:
                        return formatDecimal("7", sType);
                    case 33:
                        return formatDecimal("5", sType);
                    case 34:
                        return formatDecimal("6", sType);
                }
            case 12:
                switch (closestBuiltin(sType).getBuiltinTypeCode()) {
                    case 12:
                    case 35:
                        result = StringProperty.TYPE;
                        break;
                    case 36:
                        result = "token";
                        break;
                    default:
                        result = StringProperty.TYPE;
                        break;
                }
                return formatToLength(result, sType);
            case 13:
                return formatDuration(sType);
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                return formatDate(sType);
        }
    }

    private int pick(int n) {
        return this._picker.nextInt(n);
    }

    private String pick(String[] a) {
        return a[pick(a.length)];
    }

    private String pick(String[] a, int count) {
        if (count <= 0) {
            return "";
        }
        int i = pick(a.length);
        StringBuffer sb = new StringBuffer(a[i]);
        while (true) {
            int i2 = count;
            count--;
            if (i2 > 0) {
                i++;
                if (i >= a.length) {
                    i = 0;
                }
                sb.append(' ');
                sb.append(a[i]);
            } else {
                return sb.toString();
            }
        }
    }

    private String pickDigits(int digits) {
        StringBuffer sb = new StringBuffer();
        while (true) {
            int i = digits;
            digits--;
            if (i > 0) {
                sb.append(Integer.toString(pick(10)));
            } else {
                return sb.toString();
            }
        }
    }

    private int pickLength(SchemaType sType) {
        int minInt;
        int maxInt;
        XmlInteger length = (XmlInteger) sType.getFacet(0);
        if (length != null) {
            return length.getBigIntegerValue().intValue();
        }
        XmlInteger min = (XmlInteger) sType.getFacet(1);
        XmlInteger max = (XmlInteger) sType.getFacet(2);
        if (min == null) {
            minInt = 0;
        } else {
            minInt = min.getBigIntegerValue().intValue();
        }
        if (max == null) {
            maxInt = Integer.MAX_VALUE;
        } else {
            maxInt = max.getBigIntegerValue().intValue();
        }
        if (minInt == 0 && maxInt >= 1) {
            minInt = 1;
        }
        if (maxInt > minInt + 2) {
            maxInt = minInt + 2;
        }
        if (maxInt < minInt) {
            maxInt = minInt;
        }
        return minInt + pick(maxInt - minInt);
    }

    private String formatToLength(String s, SchemaType sType) {
        int len;
        String result = s;
        try {
            SimpleValue min = (SimpleValue) sType.getFacet(0);
            if (min == null) {
                min = (SimpleValue) sType.getFacet(1);
            }
            if (min != null) {
                int len2 = min.getIntValue();
                while (result.length() < len2) {
                    result = result + result;
                }
            }
            SimpleValue max = (SimpleValue) sType.getFacet(0);
            if (max == null) {
                max = (SimpleValue) sType.getFacet(2);
            }
            if (max != null && result.length() > (len = max.getIntValue())) {
                result = result.substring(0, len);
            }
        } catch (Exception e) {
        }
        return result;
    }

    private String formatDecimal(String start, SchemaType sType) {
        BigDecimal increment;
        BigDecimal result = new BigDecimal(start);
        XmlDecimal xmlD = (XmlDecimal) sType.getFacet(4);
        BigDecimal min = xmlD != null ? xmlD.getBigDecimalValue() : null;
        XmlDecimal xmlD2 = (XmlDecimal) sType.getFacet(5);
        BigDecimal max = xmlD2 != null ? xmlD2.getBigDecimalValue() : null;
        boolean minInclusive = true;
        boolean maxInclusive = true;
        XmlDecimal xmlD3 = (XmlDecimal) sType.getFacet(3);
        if (xmlD3 != null) {
            BigDecimal minExcl = xmlD3.getBigDecimalValue();
            if (min == null || min.compareTo(minExcl) < 0) {
                min = minExcl;
                minInclusive = false;
            }
        }
        XmlDecimal xmlD4 = (XmlDecimal) sType.getFacet(6);
        if (xmlD4 != null) {
            BigDecimal maxExcl = xmlD4.getBigDecimalValue();
            if (max == null || max.compareTo(maxExcl) > 0) {
                max = maxExcl;
                maxInclusive = false;
            }
        }
        XmlDecimal xmlD5 = (XmlDecimal) sType.getFacet(7);
        int totalDigits = -1;
        if (xmlD5 != null) {
            totalDigits = xmlD5.getBigDecimalValue().intValue();
            StringBuffer sb = new StringBuffer(totalDigits);
            for (int i = 0; i < totalDigits; i++) {
                sb.append('9');
            }
            BigDecimal digitsLimit = new BigDecimal(sb.toString());
            if (max != null && max.compareTo(digitsLimit) > 0) {
                max = digitsLimit;
                maxInclusive = true;
            }
            BigDecimal digitsLimit2 = digitsLimit.negate();
            if (min != null && min.compareTo(digitsLimit2) < 0) {
                min = digitsLimit2;
                minInclusive = true;
            }
        }
        int sigMin = min == null ? 1 : result.compareTo(min);
        int sigMax = max == null ? -1 : result.compareTo(max);
        boolean minOk = sigMin > 0 || (sigMin == 0 && minInclusive);
        boolean maxOk = sigMax < 0 || (sigMax == 0 && maxInclusive);
        XmlDecimal xmlD6 = (XmlDecimal) sType.getFacet(8);
        int fractionDigits = -1;
        if (xmlD6 == null) {
            increment = new BigDecimal(1);
        } else {
            fractionDigits = xmlD6.getBigDecimalValue().intValue();
            if (fractionDigits > 0) {
                StringBuffer sb2 = new StringBuffer("0.");
                for (int i2 = 1; i2 < fractionDigits; i2++) {
                    sb2.append('0');
                }
                sb2.append('1');
                increment = new BigDecimal(sb2.toString());
            } else {
                increment = new BigDecimal(1.0d);
            }
        }
        if (!minOk || !maxOk) {
            if (minOk && !maxOk) {
                result = maxInclusive ? max : max.subtract(increment);
            } else if (!minOk && maxOk) {
                result = minInclusive ? min : min.add(increment);
            }
        }
        int digits = 0;
        BigDecimal ONE = new BigDecimal(BigInteger.ONE);
        BigDecimal n = result;
        while (n.abs().compareTo(ONE) >= 0) {
            n = n.movePointLeft(1);
            digits++;
        }
        if (fractionDigits > 0) {
            if (totalDigits >= 0) {
                result = result.setScale(Math.max(fractionDigits, totalDigits - digits));
            } else {
                result = result.setScale(fractionDigits);
            }
        } else if (fractionDigits == 0) {
            result = result.setScale(0);
        }
        return result.toString();
    }

    private String formatDuration(SchemaType sType) {
        XmlDuration d = (XmlDuration) sType.getFacet(4);
        GDuration minInclusive = null;
        if (d != null) {
            minInclusive = d.getGDurationValue();
        }
        XmlDuration d2 = (XmlDuration) sType.getFacet(5);
        GDuration maxInclusive = null;
        if (d2 != null) {
            maxInclusive = d2.getGDurationValue();
        }
        XmlDuration d3 = (XmlDuration) sType.getFacet(3);
        GDuration minExclusive = null;
        if (d3 != null) {
            minExclusive = d3.getGDurationValue();
        }
        XmlDuration d4 = (XmlDuration) sType.getFacet(6);
        GDuration maxExclusive = null;
        if (d4 != null) {
            maxExclusive = d4.getGDurationValue();
        }
        GDurationBuilder gdurb = new GDurationBuilder();
        gdurb.setSecond(pick(800000));
        gdurb.setMonth(pick(20));
        if (minInclusive != null) {
            if (gdurb.getYear() < minInclusive.getYear()) {
                gdurb.setYear(minInclusive.getYear());
            }
            if (gdurb.getMonth() < minInclusive.getMonth()) {
                gdurb.setMonth(minInclusive.getMonth());
            }
            if (gdurb.getDay() < minInclusive.getDay()) {
                gdurb.setDay(minInclusive.getDay());
            }
            if (gdurb.getHour() < minInclusive.getHour()) {
                gdurb.setHour(minInclusive.getHour());
            }
            if (gdurb.getMinute() < minInclusive.getMinute()) {
                gdurb.setMinute(minInclusive.getMinute());
            }
            if (gdurb.getSecond() < minInclusive.getSecond()) {
                gdurb.setSecond(minInclusive.getSecond());
            }
            if (gdurb.getFraction().compareTo(minInclusive.getFraction()) < 0) {
                gdurb.setFraction(minInclusive.getFraction());
            }
        }
        if (maxInclusive != null) {
            if (gdurb.getYear() > maxInclusive.getYear()) {
                gdurb.setYear(maxInclusive.getYear());
            }
            if (gdurb.getMonth() > maxInclusive.getMonth()) {
                gdurb.setMonth(maxInclusive.getMonth());
            }
            if (gdurb.getDay() > maxInclusive.getDay()) {
                gdurb.setDay(maxInclusive.getDay());
            }
            if (gdurb.getHour() > maxInclusive.getHour()) {
                gdurb.setHour(maxInclusive.getHour());
            }
            if (gdurb.getMinute() > maxInclusive.getMinute()) {
                gdurb.setMinute(maxInclusive.getMinute());
            }
            if (gdurb.getSecond() > maxInclusive.getSecond()) {
                gdurb.setSecond(maxInclusive.getSecond());
            }
            if (gdurb.getFraction().compareTo(maxInclusive.getFraction()) > 0) {
                gdurb.setFraction(maxInclusive.getFraction());
            }
        }
        if (minExclusive != null) {
            if (gdurb.getYear() <= minExclusive.getYear()) {
                gdurb.setYear(minExclusive.getYear() + 1);
            }
            if (gdurb.getMonth() <= minExclusive.getMonth()) {
                gdurb.setMonth(minExclusive.getMonth() + 1);
            }
            if (gdurb.getDay() <= minExclusive.getDay()) {
                gdurb.setDay(minExclusive.getDay() + 1);
            }
            if (gdurb.getHour() <= minExclusive.getHour()) {
                gdurb.setHour(minExclusive.getHour() + 1);
            }
            if (gdurb.getMinute() <= minExclusive.getMinute()) {
                gdurb.setMinute(minExclusive.getMinute() + 1);
            }
            if (gdurb.getSecond() <= minExclusive.getSecond()) {
                gdurb.setSecond(minExclusive.getSecond() + 1);
            }
            if (gdurb.getFraction().compareTo(minExclusive.getFraction()) <= 0) {
                gdurb.setFraction(minExclusive.getFraction().add(new BigDecimal(0.001d)));
            }
        }
        if (maxExclusive != null) {
            if (gdurb.getYear() > maxExclusive.getYear()) {
                gdurb.setYear(maxExclusive.getYear());
            }
            if (gdurb.getMonth() > maxExclusive.getMonth()) {
                gdurb.setMonth(maxExclusive.getMonth());
            }
            if (gdurb.getDay() > maxExclusive.getDay()) {
                gdurb.setDay(maxExclusive.getDay());
            }
            if (gdurb.getHour() > maxExclusive.getHour()) {
                gdurb.setHour(maxExclusive.getHour());
            }
            if (gdurb.getMinute() > maxExclusive.getMinute()) {
                gdurb.setMinute(maxExclusive.getMinute());
            }
            if (gdurb.getSecond() > maxExclusive.getSecond()) {
                gdurb.setSecond(maxExclusive.getSecond());
            }
            if (gdurb.getFraction().compareTo(maxExclusive.getFraction()) > 0) {
                gdurb.setFraction(maxExclusive.getFraction());
            }
        }
        gdurb.normalize();
        return gdurb.toString();
    }

    private String formatDate(SchemaType sType) {
        GDateBuilder gdateb = new GDateBuilder(new Date((1000 * pick(31536000)) + ((30 + pick(20)) * 365 * 24 * 60 * 60 * 1000)));
        GDate min = null;
        GDate max = null;
        switch (sType.getPrimitiveType().getBuiltinTypeCode()) {
            case 14:
                XmlDateTime x = (XmlDateTime) sType.getFacet(4);
                if (x != null) {
                    min = x.getGDateValue();
                }
                XmlDateTime x2 = (XmlDateTime) sType.getFacet(3);
                if (x2 != null && (min == null || min.compareToGDate(x2.getGDateValue()) <= 0)) {
                    min = x2.getGDateValue();
                }
                XmlDateTime x3 = (XmlDateTime) sType.getFacet(5);
                if (x3 != null) {
                    max = x3.getGDateValue();
                }
                XmlDateTime x4 = (XmlDateTime) sType.getFacet(6);
                if (x4 != null && (max == null || max.compareToGDate(x4.getGDateValue()) >= 0)) {
                    max = x4.getGDateValue();
                    break;
                }
                break;
            case 15:
                XmlTime x5 = (XmlTime) sType.getFacet(4);
                if (x5 != null) {
                    min = x5.getGDateValue();
                }
                XmlTime x6 = (XmlTime) sType.getFacet(3);
                if (x6 != null && (min == null || min.compareToGDate(x6.getGDateValue()) <= 0)) {
                    min = x6.getGDateValue();
                }
                XmlTime x7 = (XmlTime) sType.getFacet(5);
                if (x7 != null) {
                    max = x7.getGDateValue();
                }
                XmlTime x8 = (XmlTime) sType.getFacet(6);
                if (x8 != null && (max == null || max.compareToGDate(x8.getGDateValue()) >= 0)) {
                    max = x8.getGDateValue();
                    break;
                }
                break;
            case 16:
                XmlDate x9 = (XmlDate) sType.getFacet(4);
                if (x9 != null) {
                    min = x9.getGDateValue();
                }
                XmlDate x10 = (XmlDate) sType.getFacet(3);
                if (x10 != null && (min == null || min.compareToGDate(x10.getGDateValue()) <= 0)) {
                    min = x10.getGDateValue();
                }
                XmlDate x11 = (XmlDate) sType.getFacet(5);
                if (x11 != null) {
                    max = x11.getGDateValue();
                }
                XmlDate x12 = (XmlDate) sType.getFacet(6);
                if (x12 != null && (max == null || max.compareToGDate(x12.getGDateValue()) >= 0)) {
                    max = x12.getGDateValue();
                    break;
                }
                break;
            case 17:
                XmlGYearMonth x13 = (XmlGYearMonth) sType.getFacet(4);
                if (x13 != null) {
                    min = x13.getGDateValue();
                }
                XmlGYearMonth x14 = (XmlGYearMonth) sType.getFacet(3);
                if (x14 != null && (min == null || min.compareToGDate(x14.getGDateValue()) <= 0)) {
                    min = x14.getGDateValue();
                }
                XmlGYearMonth x15 = (XmlGYearMonth) sType.getFacet(5);
                if (x15 != null) {
                    max = x15.getGDateValue();
                }
                XmlGYearMonth x16 = (XmlGYearMonth) sType.getFacet(6);
                if (x16 != null && (max == null || max.compareToGDate(x16.getGDateValue()) >= 0)) {
                    max = x16.getGDateValue();
                    break;
                }
                break;
            case 18:
                XmlGYear x17 = (XmlGYear) sType.getFacet(4);
                if (x17 != null) {
                    min = x17.getGDateValue();
                }
                XmlGYear x18 = (XmlGYear) sType.getFacet(3);
                if (x18 != null && (min == null || min.compareToGDate(x18.getGDateValue()) <= 0)) {
                    min = x18.getGDateValue();
                }
                XmlGYear x19 = (XmlGYear) sType.getFacet(5);
                if (x19 != null) {
                    max = x19.getGDateValue();
                }
                XmlGYear x20 = (XmlGYear) sType.getFacet(6);
                if (x20 != null && (max == null || max.compareToGDate(x20.getGDateValue()) >= 0)) {
                    max = x20.getGDateValue();
                    break;
                }
                break;
            case 19:
                XmlGMonthDay x21 = (XmlGMonthDay) sType.getFacet(4);
                if (x21 != null) {
                    min = x21.getGDateValue();
                }
                XmlGMonthDay x22 = (XmlGMonthDay) sType.getFacet(3);
                if (x22 != null && (min == null || min.compareToGDate(x22.getGDateValue()) <= 0)) {
                    min = x22.getGDateValue();
                }
                XmlGMonthDay x23 = (XmlGMonthDay) sType.getFacet(5);
                if (x23 != null) {
                    max = x23.getGDateValue();
                }
                XmlGMonthDay x24 = (XmlGMonthDay) sType.getFacet(6);
                if (x24 != null && (max == null || max.compareToGDate(x24.getGDateValue()) >= 0)) {
                    max = x24.getGDateValue();
                    break;
                }
                break;
            case 20:
                XmlGDay x25 = (XmlGDay) sType.getFacet(4);
                if (x25 != null) {
                    min = x25.getGDateValue();
                }
                XmlGDay x26 = (XmlGDay) sType.getFacet(3);
                if (x26 != null && (min == null || min.compareToGDate(x26.getGDateValue()) <= 0)) {
                    min = x26.getGDateValue();
                }
                XmlGDay x27 = (XmlGDay) sType.getFacet(5);
                if (x27 != null) {
                    max = x27.getGDateValue();
                }
                XmlGDay x28 = (XmlGDay) sType.getFacet(6);
                if (x28 != null && (max == null || max.compareToGDate(x28.getGDateValue()) >= 0)) {
                    max = x28.getGDateValue();
                    break;
                }
                break;
            case 21:
                XmlGMonth x29 = (XmlGMonth) sType.getFacet(4);
                if (x29 != null) {
                    min = x29.getGDateValue();
                }
                XmlGMonth x30 = (XmlGMonth) sType.getFacet(3);
                if (x30 != null && (min == null || min.compareToGDate(x30.getGDateValue()) <= 0)) {
                    min = x30.getGDateValue();
                }
                XmlGMonth x31 = (XmlGMonth) sType.getFacet(5);
                if (x31 != null) {
                    max = x31.getGDateValue();
                }
                XmlGMonth x32 = (XmlGMonth) sType.getFacet(6);
                if (x32 != null && (max == null || max.compareToGDate(x32.getGDateValue()) >= 0)) {
                    max = x32.getGDateValue();
                    break;
                }
                break;
        }
        if (min != null && max == null) {
            if (min.compareToGDate(gdateb) >= 0) {
                Calendar c = gdateb.getCalendar();
                c.add(11, pick(8));
                gdateb = new GDateBuilder(c);
            }
        } else if (min == null && max != null) {
            if (max.compareToGDate(gdateb) <= 0) {
                Calendar c2 = gdateb.getCalendar();
                c2.add(11, 0 - pick(8));
                gdateb = new GDateBuilder(c2);
            }
        } else if (min != null && max != null && (min.compareToGDate(gdateb) >= 0 || max.compareToGDate(gdateb) <= 0)) {
            Calendar c3 = min.getCalendar();
            Calendar cmax = max.getCalendar();
            c3.add(11, 1);
            if (c3.after(cmax)) {
                c3.add(11, -1);
                c3.add(12, 1);
                if (c3.after(cmax)) {
                    c3.add(12, -1);
                    c3.add(13, 1);
                    if (c3.after(cmax)) {
                        c3.add(13, -1);
                        c3.add(14, 1);
                        if (c3.after(cmax)) {
                            c3.add(14, -1);
                        }
                    }
                }
            }
            gdateb = new GDateBuilder(c3);
        }
        gdateb.setBuiltinTypeCode(sType.getPrimitiveType().getBuiltinTypeCode());
        if (pick(2) == 0) {
            gdateb.clearTimeZone();
        }
        return gdateb.toString();
    }

    private SchemaType closestBuiltin(SchemaType sType) {
        while (!sType.isBuiltinType()) {
            sType = sType.getBaseType();
        }
        return sType;
    }

    public static QName crackQName(String qName) {
        String ns;
        String name;
        int index = qName.lastIndexOf(58);
        if (index >= 0) {
            ns = qName.substring(0, index);
            name = qName.substring(index + 1);
        } else {
            ns = "";
            name = qName;
        }
        return new QName(ns, name);
    }

    private void processParticle(SchemaParticle sp, XmlCursor xmlc, boolean mixed) {
        int loop = determineMinMaxForSample(sp, xmlc);
        while (true) {
            int i = loop;
            loop--;
            if (i > 0) {
                switch (sp.getParticleType()) {
                    case 1:
                        processAll(sp, xmlc, mixed);
                        break;
                    case 2:
                        processChoice(sp, xmlc, mixed);
                        break;
                    case 3:
                        processSequence(sp, xmlc, mixed);
                        break;
                    case 4:
                        processElement(sp, xmlc, mixed);
                        break;
                    case 5:
                        processWildCard(sp, xmlc, mixed);
                        break;
                }
            } else {
                return;
            }
        }
    }

    private int determineMinMaxForSample(SchemaParticle sp, XmlCursor xmlc) {
        int minOccurs = sp.getIntMinOccurs();
        int maxOccurs = sp.getIntMaxOccurs();
        if (minOccurs == maxOccurs) {
            return minOccurs;
        }
        int result = minOccurs;
        if (result == 0 && this._nElements < 1000) {
            result = 1;
        }
        if (sp.getParticleType() != 4) {
            return result;
        }
        if (sp.getMaxOccurs() == null) {
            if (minOccurs == 0) {
                xmlc.insertComment("Zero or more repetitions:");
            } else {
                xmlc.insertComment(minOccurs + " or more repetitions:");
            }
        } else if (sp.getIntMaxOccurs() > 1) {
            xmlc.insertComment(minOccurs + " to " + String.valueOf(sp.getMaxOccurs()) + " repetitions:");
        } else {
            xmlc.insertComment("Optional:");
        }
        return result;
    }

    private String getItemNameOrType(SchemaParticle sp, XmlCursor xmlc) {
        String elementOrTypeName;
        if (sp.getParticleType() == 4) {
            elementOrTypeName = "Element (" + sp.getName().getLocalPart() + ")";
        } else {
            elementOrTypeName = printParticleType(sp.getParticleType());
        }
        return elementOrTypeName;
    }

    private void processElement(SchemaParticle sp, XmlCursor xmlc, boolean mixed) {
        SchemaLocalElement element = (SchemaLocalElement) sp;
        if (this._soapEnc) {
            xmlc.insertElement(element.getName().getLocalPart());
        } else {
            xmlc.insertElement(element.getName().getLocalPart(), element.getName().getNamespaceURI());
        }
        this._nElements++;
        xmlc.toPrevToken();
        createSampleForType(element.getType(), xmlc);
        xmlc.toNextToken();
    }

    private void moveToken(int numToMove, XmlCursor xmlc) {
        for (int i = 0; i < Math.abs(numToMove); i++) {
            if (numToMove < 0) {
                xmlc.toPrevToken();
            } else {
                xmlc.toNextToken();
            }
        }
    }

    private static final String formatQName(XmlCursor xmlc, QName qName) {
        String name;
        XmlCursor parent = xmlc.newCursor();
        parent.toParent();
        String prefix = parent.prefixForNamespace(qName.getNamespaceURI());
        parent.dispose();
        if (prefix == null || prefix.length() == 0) {
            name = qName.getLocalPart();
        } else {
            name = prefix + ":" + qName.getLocalPart();
        }
        return name;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x00b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void processAttributes(org.apache.xmlbeans.SchemaType r7, org.apache.xmlbeans.XmlCursor r8) {
        /*
            r6 = this;
            r0 = r6
            boolean r0 = r0._soapEnc
            if (r0 == 0) goto L20
            r0 = r7
            javax.xml.namespace.QName r0 = r0.getName()
            r9 = r0
            r0 = r9
            if (r0 == 0) goto L20
            r0 = r8
            javax.xml.namespace.QName r1 = org.apache.xmlbeans.impl.xsd2inst.SampleXmlUtil.XSI_TYPE
            r2 = r8
            r3 = r9
            java.lang.String r2 = formatQName(r2, r3)
            r0.insertAttributeWithValue(r1, r2)
        L20:
            r0 = r7
            org.apache.xmlbeans.SchemaProperty[] r0 = r0.getAttributeProperties()
            r9 = r0
            r0 = 0
            r10 = r0
        L2a:
            r0 = r10
            r1 = r9
            int r1 = r1.length
            if (r0 >= r1) goto Le3
            r0 = r9
            r1 = r10
            r0 = r0[r1]
            r11 = r0
            r0 = r6
            boolean r0 = r0._soapEnc
            if (r0 == 0) goto Lb2
            java.util.Set r0 = org.apache.xmlbeans.impl.xsd2inst.SampleXmlUtil.SKIPPED_SOAP_ATTRS
            r1 = r11
            javax.xml.namespace.QName r1 = r1.getName()
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L53
            goto Ldd
        L53:
            javax.xml.namespace.QName r0 = org.apache.xmlbeans.impl.xsd2inst.SampleXmlUtil.ENC_ARRAYTYPE
            r1 = r11
            javax.xml.namespace.QName r1 = r1.getName()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lb2
            r0 = r7
            org.apache.xmlbeans.SchemaAttributeModel r0 = r0.getAttributeModel()
            r1 = r11
            javax.xml.namespace.QName r1 = r1.getName()
            org.apache.xmlbeans.SchemaLocalAttribute r0 = r0.getAttribute(r1)
            org.apache.xmlbeans.soap.SchemaWSDLArrayType r0 = (org.apache.xmlbeans.soap.SchemaWSDLArrayType) r0
            org.apache.xmlbeans.soap.SOAPArrayType r0 = r0.getWSDLArrayType()
            r12 = r0
            r0 = r12
            if (r0 == 0) goto Ldd
            r0 = r8
            r1 = r11
            javax.xml.namespace.QName r1 = r1.getName()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            r3 = r8
            r4 = r12
            javax.xml.namespace.QName r4 = r4.getQName()
            java.lang.String r3 = formatQName(r3, r4)
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r12
            java.lang.String r3 = r3.soap11DimensionString()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r0.insertAttributeWithValue(r1, r2)
            goto Ldd
        Lb2:
            r0 = r11
            java.lang.String r0 = r0.getDefaultText()
            r12 = r0
            r0 = r8
            r1 = r11
            javax.xml.namespace.QName r1 = r1.getName()
            r2 = r12
            if (r2 != 0) goto Ld6
            r2 = r6
            r3 = r11
            org.apache.xmlbeans.SchemaType r3 = r3.getType()
            java.lang.String r2 = r2.sampleDataForSimpleType(r3)
            goto Ld8
        Ld6:
            r2 = r12
        Ld8:
            r0.insertAttributeWithValue(r1, r2)
        Ldd:
            int r10 = r10 + 1
            goto L2a
        Le3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.xsd2inst.SampleXmlUtil.processAttributes(org.apache.xmlbeans.SchemaType, org.apache.xmlbeans.XmlCursor):void");
    }

    private void processSequence(SchemaParticle sp, XmlCursor xmlc, boolean mixed) {
        SchemaParticle[] spc = sp.getParticleChildren();
        for (int i = 0; i < spc.length; i++) {
            processParticle(spc[i], xmlc, mixed);
            if (mixed && i < spc.length - 1) {
                xmlc.insertChars(pick(WORDS));
            }
        }
    }

    private void processChoice(SchemaParticle sp, XmlCursor xmlc, boolean mixed) {
        SchemaParticle[] spc = sp.getParticleChildren();
        xmlc.insertComment("You have a CHOICE of the next " + String.valueOf(spc.length) + " items at this level");
        for (SchemaParticle schemaParticle : spc) {
            processParticle(schemaParticle, xmlc, mixed);
        }
    }

    private void processAll(SchemaParticle sp, XmlCursor xmlc, boolean mixed) {
        SchemaParticle[] spc = sp.getParticleChildren();
        for (int i = 0; i < spc.length; i++) {
            processParticle(spc[i], xmlc, mixed);
            if (mixed && i < spc.length - 1) {
                xmlc.insertChars(pick(WORDS));
            }
        }
    }

    private void processWildCard(SchemaParticle sp, XmlCursor xmlc, boolean mixed) {
        xmlc.insertComment("You may enter ANY elements at this point");
        xmlc.insertElement("AnyElement");
    }

    private static QName getClosestName(SchemaType sType) {
        while (sType.getName() == null) {
            sType = sType.getBaseType();
        }
        return sType.getName();
    }

    private String printParticleType(int particleType) {
        StringBuffer returnParticleType = new StringBuffer();
        returnParticleType.append("Schema Particle Type: ");
        switch (particleType) {
            case 1:
                returnParticleType.append("ALL\n");
                break;
            case 2:
                returnParticleType.append("CHOICE\n");
                break;
            case 3:
                returnParticleType.append("SEQUENCE\n");
                break;
            case 4:
                returnParticleType.append("ELEMENT\n");
                break;
            case 5:
                returnParticleType.append("WILDCARD\n");
                break;
            default:
                returnParticleType.append("Schema Particle Type Unknown");
                break;
        }
        return returnParticleType.toString();
    }
}
