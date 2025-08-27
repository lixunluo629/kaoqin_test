package org.apache.commons.lang;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.xmlbeans.XmlErrorCodes;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/Entities.class */
class Entities {
    private static final String[][] BASIC_ARRAY = {new String[]{"quot", ANSIConstants.BLUE_FG}, new String[]{"amp", "38"}, new String[]{"lt", "60"}, new String[]{"gt", "62"}};
    private static final String[][] APOS_ARRAY = {new String[]{"apos", ANSIConstants.DEFAULT_FG}};
    static final String[][] ISO8859_1_ARRAY = {new String[]{"nbsp", "160"}, new String[]{"iexcl", "161"}, new String[]{"cent", "162"}, new String[]{"pound", "163"}, new String[]{"curren", "164"}, new String[]{"yen", "165"}, new String[]{"brvbar", "166"}, new String[]{"sect", "167"}, new String[]{"uml", "168"}, new String[]{"copy", "169"}, new String[]{"ordf", "170"}, new String[]{"laquo", "171"}, new String[]{"not", "172"}, new String[]{"shy", "173"}, new String[]{"reg", "174"}, new String[]{"macr", "175"}, new String[]{"deg", "176"}, new String[]{"plusmn", "177"}, new String[]{"sup2", "178"}, new String[]{"sup3", "179"}, new String[]{"acute", "180"}, new String[]{"micro", "181"}, new String[]{"para", "182"}, new String[]{"middot", "183"}, new String[]{"cedil", "184"}, new String[]{"sup1", "185"}, new String[]{"ordm", "186"}, new String[]{"raquo", "187"}, new String[]{"frac14", "188"}, new String[]{"frac12", "189"}, new String[]{"frac34", "190"}, new String[]{"iquest", "191"}, new String[]{"Agrave", "192"}, new String[]{"Aacute", "193"}, new String[]{"Acirc", "194"}, new String[]{"Atilde", "195"}, new String[]{"Auml", "196"}, new String[]{"Aring", "197"}, new String[]{"AElig", "198"}, new String[]{"Ccedil", "199"}, new String[]{"Egrave", "200"}, new String[]{"Eacute", "201"}, new String[]{"Ecirc", "202"}, new String[]{"Euml", "203"}, new String[]{"Igrave", "204"}, new String[]{"Iacute", "205"}, new String[]{"Icirc", "206"}, new String[]{"Iuml", "207"}, new String[]{"ETH", "208"}, new String[]{"Ntilde", "209"}, new String[]{"Ograve", "210"}, new String[]{"Oacute", "211"}, new String[]{"Ocirc", "212"}, new String[]{"Otilde", "213"}, new String[]{"Ouml", "214"}, new String[]{"times", "215"}, new String[]{"Oslash", "216"}, new String[]{"Ugrave", "217"}, new String[]{"Uacute", "218"}, new String[]{"Ucirc", "219"}, new String[]{"Uuml", "220"}, new String[]{"Yacute", "221"}, new String[]{"THORN", "222"}, new String[]{"szlig", "223"}, new String[]{"agrave", "224"}, new String[]{"aacute", "225"}, new String[]{"acirc", "226"}, new String[]{"atilde", "227"}, new String[]{"auml", "228"}, new String[]{"aring", "229"}, new String[]{"aelig", "230"}, new String[]{"ccedil", "231"}, new String[]{"egrave", "232"}, new String[]{"eacute", "233"}, new String[]{"ecirc", "234"}, new String[]{"euml", "235"}, new String[]{"igrave", "236"}, new String[]{"iacute", "237"}, new String[]{"icirc", "238"}, new String[]{"iuml", "239"}, new String[]{"eth", "240"}, new String[]{"ntilde", "241"}, new String[]{"ograve", "242"}, new String[]{"oacute", "243"}, new String[]{"ocirc", "244"}, new String[]{"otilde", "245"}, new String[]{"ouml", "246"}, new String[]{"divide", "247"}, new String[]{"oslash", "248"}, new String[]{"ugrave", "249"}, new String[]{"uacute", "250"}, new String[]{"ucirc", "251"}, new String[]{"uuml", "252"}, new String[]{"yacute", "253"}, new String[]{"thorn", "254"}, new String[]{"yuml", "255"}};
    static final String[][] HTML40_ARRAY = {new String[]{"fnof", "402"}, new String[]{"Alpha", "913"}, new String[]{"Beta", "914"}, new String[]{"Gamma", "915"}, new String[]{"Delta", "916"}, new String[]{"Epsilon", "917"}, new String[]{"Zeta", "918"}, new String[]{"Eta", "919"}, new String[]{"Theta", "920"}, new String[]{"Iota", "921"}, new String[]{"Kappa", "922"}, new String[]{"Lambda", "923"}, new String[]{"Mu", "924"}, new String[]{"Nu", "925"}, new String[]{"Xi", "926"}, new String[]{"Omicron", "927"}, new String[]{"Pi", "928"}, new String[]{"Rho", "929"}, new String[]{"Sigma", "931"}, new String[]{"Tau", "932"}, new String[]{"Upsilon", "933"}, new String[]{"Phi", "934"}, new String[]{"Chi", "935"}, new String[]{"Psi", "936"}, new String[]{"Omega", "937"}, new String[]{"alpha", "945"}, new String[]{"beta", "946"}, new String[]{"gamma", "947"}, new String[]{"delta", "948"}, new String[]{"epsilon", "949"}, new String[]{"zeta", "950"}, new String[]{"eta", "951"}, new String[]{"theta", "952"}, new String[]{"iota", "953"}, new String[]{"kappa", "954"}, new String[]{"lambda", "955"}, new String[]{"mu", "956"}, new String[]{"nu", "957"}, new String[]{"xi", "958"}, new String[]{"omicron", "959"}, new String[]{"pi", "960"}, new String[]{"rho", "961"}, new String[]{"sigmaf", "962"}, new String[]{"sigma", "963"}, new String[]{"tau", "964"}, new String[]{"upsilon", "965"}, new String[]{"phi", "966"}, new String[]{"chi", "967"}, new String[]{"psi", "968"}, new String[]{"omega", "969"}, new String[]{"thetasym", "977"}, new String[]{"upsih", "978"}, new String[]{"piv", "982"}, new String[]{"bull", "8226"}, new String[]{"hellip", "8230"}, new String[]{"prime", "8242"}, new String[]{"Prime", "8243"}, new String[]{"oline", "8254"}, new String[]{"frasl", "8260"}, new String[]{"weierp", "8472"}, new String[]{"image", "8465"}, new String[]{"real", "8476"}, new String[]{"trade", "8482"}, new String[]{"alefsym", "8501"}, new String[]{"larr", "8592"}, new String[]{"uarr", "8593"}, new String[]{"rarr", "8594"}, new String[]{"darr", "8595"}, new String[]{"harr", "8596"}, new String[]{"crarr", "8629"}, new String[]{"lArr", "8656"}, new String[]{"uArr", "8657"}, new String[]{"rArr", "8658"}, new String[]{"dArr", "8659"}, new String[]{"hArr", "8660"}, new String[]{"forall", "8704"}, new String[]{"part", "8706"}, new String[]{"exist", "8707"}, new String[]{"empty", "8709"}, new String[]{"nabla", "8711"}, new String[]{"isin", "8712"}, new String[]{"notin", "8713"}, new String[]{"ni", "8715"}, new String[]{"prod", "8719"}, new String[]{"sum", "8721"}, new String[]{"minus", "8722"}, new String[]{"lowast", "8727"}, new String[]{"radic", "8730"}, new String[]{BeanDefinitionParserDelegate.PROP_ELEMENT, "8733"}, new String[]{"infin", "8734"}, new String[]{"ang", "8736"}, new String[]{"and", "8743"}, new String[]{"or", "8744"}, new String[]{"cap", "8745"}, new String[]{"cup", "8746"}, new String[]{XmlErrorCodes.INT, "8747"}, new String[]{"there4", "8756"}, new String[]{"sim", "8764"}, new String[]{"cong", "8773"}, new String[]{"asymp", "8776"}, new String[]{"ne", "8800"}, new String[]{"equiv", "8801"}, new String[]{"le", "8804"}, new String[]{"ge", "8805"}, new String[]{Claims.SUBJECT, "8834"}, new String[]{"sup", "8835"}, new String[]{"sube", "8838"}, new String[]{"supe", "8839"}, new String[]{"oplus", "8853"}, new String[]{"otimes", "8855"}, new String[]{"perp", "8869"}, new String[]{"sdot", "8901"}, new String[]{"lceil", "8968"}, new String[]{"rceil", "8969"}, new String[]{"lfloor", "8970"}, new String[]{"rfloor", "8971"}, new String[]{AbstractHtmlElementTag.LANG_ATTRIBUTE, "9001"}, new String[]{"rang", "9002"}, new String[]{"loz", "9674"}, new String[]{"spades", "9824"}, new String[]{"clubs", "9827"}, new String[]{"hearts", "9829"}, new String[]{"diams", "9830"}, new String[]{"OElig", "338"}, new String[]{"oelig", "339"}, new String[]{"Scaron", "352"}, new String[]{"scaron", "353"}, new String[]{"Yuml", "376"}, new String[]{"circ", "710"}, new String[]{"tilde", "732"}, new String[]{"ensp", "8194"}, new String[]{"emsp", "8195"}, new String[]{"thinsp", "8201"}, new String[]{"zwnj", "8204"}, new String[]{"zwj", "8205"}, new String[]{"lrm", "8206"}, new String[]{"rlm", "8207"}, new String[]{"ndash", "8211"}, new String[]{"mdash", "8212"}, new String[]{"lsquo", "8216"}, new String[]{"rsquo", "8217"}, new String[]{"sbquo", "8218"}, new String[]{"ldquo", "8220"}, new String[]{"rdquo", "8221"}, new String[]{"bdquo", "8222"}, new String[]{"dagger", "8224"}, new String[]{"Dagger", "8225"}, new String[]{"permil", "8240"}, new String[]{"lsaquo", "8249"}, new String[]{"rsaquo", "8250"}, new String[]{"euro", "8364"}};
    public static final Entities XML;
    public static final Entities HTML32;
    public static final Entities HTML40;
    private final EntityMap map;

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/Entities$EntityMap.class */
    interface EntityMap {
        void add(String str, int i);

        String name(int i);

        int value(String str);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r0v7, types: [java.lang.String[], java.lang.String[][]] */
    static {
        Entities xml = new Entities();
        xml.addEntities(BASIC_ARRAY);
        xml.addEntities(APOS_ARRAY);
        XML = xml;
        Entities html32 = new Entities();
        html32.addEntities(BASIC_ARRAY);
        html32.addEntities(ISO8859_1_ARRAY);
        HTML32 = html32;
        Entities html40 = new Entities();
        fillWithHtml40Entities(html40);
        HTML40 = html40;
    }

    static void fillWithHtml40Entities(Entities entities) {
        entities.addEntities(BASIC_ARRAY);
        entities.addEntities(ISO8859_1_ARRAY);
        entities.addEntities(HTML40_ARRAY);
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/Entities$PrimitiveEntityMap.class */
    static class PrimitiveEntityMap implements EntityMap {
        private final Map mapNameToValue = new HashMap();
        private final IntHashMap mapValueToName = new IntHashMap();

        PrimitiveEntityMap() {
        }

        @Override // org.apache.commons.lang.Entities.EntityMap
        public void add(String name, int value) {
            this.mapNameToValue.put(name, new Integer(value));
            this.mapValueToName.put(value, name);
        }

        @Override // org.apache.commons.lang.Entities.EntityMap
        public String name(int value) {
            return (String) this.mapValueToName.get(value);
        }

        @Override // org.apache.commons.lang.Entities.EntityMap
        public int value(String name) {
            Object value = this.mapNameToValue.get(name);
            if (value == null) {
                return -1;
            }
            return ((Integer) value).intValue();
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/Entities$MapIntMap.class */
    static abstract class MapIntMap implements EntityMap {
        protected final Map mapNameToValue;
        protected final Map mapValueToName;

        MapIntMap(Map nameToValue, Map valueToName) {
            this.mapNameToValue = nameToValue;
            this.mapValueToName = valueToName;
        }

        @Override // org.apache.commons.lang.Entities.EntityMap
        public void add(String name, int value) {
            this.mapNameToValue.put(name, new Integer(value));
            this.mapValueToName.put(new Integer(value), name);
        }

        @Override // org.apache.commons.lang.Entities.EntityMap
        public String name(int value) {
            return (String) this.mapValueToName.get(new Integer(value));
        }

        @Override // org.apache.commons.lang.Entities.EntityMap
        public int value(String name) {
            Object value = this.mapNameToValue.get(name);
            if (value == null) {
                return -1;
            }
            return ((Integer) value).intValue();
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/Entities$HashEntityMap.class */
    static class HashEntityMap extends MapIntMap {
        public HashEntityMap() {
            super(new HashMap(), new HashMap());
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/Entities$TreeEntityMap.class */
    static class TreeEntityMap extends MapIntMap {
        public TreeEntityMap() {
            super(new TreeMap(), new TreeMap());
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/Entities$LookupEntityMap.class */
    static class LookupEntityMap extends PrimitiveEntityMap {
        private String[] lookupTable;
        private static final int LOOKUP_TABLE_SIZE = 256;

        LookupEntityMap() {
        }

        @Override // org.apache.commons.lang.Entities.PrimitiveEntityMap, org.apache.commons.lang.Entities.EntityMap
        public String name(int value) {
            if (value < 256) {
                return lookupTable()[value];
            }
            return super.name(value);
        }

        private String[] lookupTable() {
            if (this.lookupTable == null) {
                createLookupTable();
            }
            return this.lookupTable;
        }

        private void createLookupTable() {
            this.lookupTable = new String[256];
            for (int i = 0; i < 256; i++) {
                this.lookupTable[i] = super.name(i);
            }
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/Entities$ArrayEntityMap.class */
    static class ArrayEntityMap implements EntityMap {
        protected final int growBy;
        protected int size;
        protected String[] names;
        protected int[] values;

        public ArrayEntityMap() {
            this.size = 0;
            this.growBy = 100;
            this.names = new String[this.growBy];
            this.values = new int[this.growBy];
        }

        public ArrayEntityMap(int growBy) {
            this.size = 0;
            this.growBy = growBy;
            this.names = new String[growBy];
            this.values = new int[growBy];
        }

        @Override // org.apache.commons.lang.Entities.EntityMap
        public void add(String name, int value) {
            ensureCapacity(this.size + 1);
            this.names[this.size] = name;
            this.values[this.size] = value;
            this.size++;
        }

        protected void ensureCapacity(int capacity) {
            if (capacity > this.names.length) {
                int newSize = Math.max(capacity, this.size + this.growBy);
                String[] newNames = new String[newSize];
                System.arraycopy(this.names, 0, newNames, 0, this.size);
                this.names = newNames;
                int[] newValues = new int[newSize];
                System.arraycopy(this.values, 0, newValues, 0, this.size);
                this.values = newValues;
            }
        }

        @Override // org.apache.commons.lang.Entities.EntityMap
        public String name(int value) {
            for (int i = 0; i < this.size; i++) {
                if (this.values[i] == value) {
                    return this.names[i];
                }
            }
            return null;
        }

        @Override // org.apache.commons.lang.Entities.EntityMap
        public int value(String name) {
            for (int i = 0; i < this.size; i++) {
                if (this.names[i].equals(name)) {
                    return this.values[i];
                }
            }
            return -1;
        }
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/Entities$BinaryEntityMap.class */
    static class BinaryEntityMap extends ArrayEntityMap {
        public BinaryEntityMap() {
        }

        public BinaryEntityMap(int growBy) {
            super(growBy);
        }

        private int binarySearch(int key) {
            int low = 0;
            int high = this.size - 1;
            while (low <= high) {
                int mid = (low + high) >>> 1;
                int midVal = this.values[mid];
                if (midVal < key) {
                    low = mid + 1;
                } else if (midVal > key) {
                    high = mid - 1;
                } else {
                    return mid;
                }
            }
            return -(low + 1);
        }

        @Override // org.apache.commons.lang.Entities.ArrayEntityMap, org.apache.commons.lang.Entities.EntityMap
        public void add(String name, int value) {
            ensureCapacity(this.size + 1);
            int insertAt = binarySearch(value);
            if (insertAt > 0) {
                return;
            }
            int insertAt2 = -(insertAt + 1);
            System.arraycopy(this.values, insertAt2, this.values, insertAt2 + 1, this.size - insertAt2);
            this.values[insertAt2] = value;
            System.arraycopy(this.names, insertAt2, this.names, insertAt2 + 1, this.size - insertAt2);
            this.names[insertAt2] = name;
            this.size++;
        }

        @Override // org.apache.commons.lang.Entities.ArrayEntityMap, org.apache.commons.lang.Entities.EntityMap
        public String name(int value) {
            int index = binarySearch(value);
            if (index < 0) {
                return null;
            }
            return this.names[index];
        }
    }

    public Entities() {
        this.map = new LookupEntityMap();
    }

    Entities(EntityMap emap) {
        this.map = emap;
    }

    public void addEntities(String[][] entityArray) {
        for (int i = 0; i < entityArray.length; i++) {
            addEntity(entityArray[i][0], Integer.parseInt(entityArray[i][1]));
        }
    }

    public void addEntity(String name, int value) {
        this.map.add(name, value);
    }

    public String entityName(int value) {
        return this.map.name(value);
    }

    public int entityValue(String name) {
        return this.map.value(name);
    }

    public String escape(String str) {
        StringWriter stringWriter = createStringWriter(str);
        try {
            escape(stringWriter, str);
            return stringWriter.toString();
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    public void escape(Writer writer, String str) throws IOException {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            String entityName = entityName(c);
            if (entityName == null) {
                if (c > 127) {
                    writer.write("&#");
                    writer.write(Integer.toString(c, 10));
                    writer.write(59);
                } else {
                    writer.write(c);
                }
            } else {
                writer.write(38);
                writer.write(entityName);
                writer.write(59);
            }
        }
    }

    public String unescape(String str) throws NumberFormatException {
        int firstAmp = str.indexOf(38);
        if (firstAmp < 0) {
            return str;
        }
        StringWriter stringWriter = createStringWriter(str);
        try {
            doUnescape(stringWriter, str, firstAmp);
            return stringWriter.toString();
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    private StringWriter createStringWriter(String str) {
        return new StringWriter((int) (str.length() + (str.length() * 0.1d)));
    }

    public void unescape(Writer writer, String str) throws IOException, NumberFormatException {
        int firstAmp = str.indexOf(38);
        if (firstAmp < 0) {
            writer.write(str);
        } else {
            doUnescape(writer, str, firstAmp);
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:23:0x009b. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00d8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void doUnescape(java.io.Writer r6, java.lang.String r7, int r8) throws java.io.IOException, java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 291
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.Entities.doUnescape(java.io.Writer, java.lang.String, int):void");
    }
}
