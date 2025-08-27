package com.mysql.jdbc;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.PropertyAccessor;

/* compiled from: CharsetMapping.java */
/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/MysqlCharset.class */
class MysqlCharset {
    public final String charsetName;
    public final int mblen;
    public final int priority;
    public final List<String> javaEncodingsUc;
    public int major;
    public int minor;
    public int subminor;

    public MysqlCharset(String charsetName, int mblen, int priority, String[] javaEncodings) {
        this.javaEncodingsUc = new ArrayList();
        this.major = 4;
        this.minor = 1;
        this.subminor = 0;
        this.charsetName = charsetName;
        this.mblen = mblen;
        this.priority = priority;
        for (String encoding : javaEncodings) {
            try {
                Charset cs = Charset.forName(encoding);
                addEncodingMapping(cs.name());
                Set<String> als = cs.aliases();
                Iterator<String> ali = als.iterator();
                while (ali.hasNext()) {
                    addEncodingMapping(ali.next());
                }
            } catch (Exception e) {
                if (mblen == 1) {
                    addEncodingMapping(encoding);
                }
            }
        }
        if (this.javaEncodingsUc.size() == 0) {
            if (mblen > 1) {
                addEncodingMapping("UTF-8");
            } else {
                addEncodingMapping("Cp1252");
            }
        }
    }

    private void addEncodingMapping(String encoding) {
        String encodingUc = encoding.toUpperCase(Locale.ENGLISH);
        if (!this.javaEncodingsUc.contains(encodingUc)) {
            this.javaEncodingsUc.add(encodingUc);
        }
    }

    public MysqlCharset(String charsetName, int mblen, int priority, String[] javaEncodings, int major, int minor) {
        this(charsetName, mblen, priority, javaEncodings);
        this.major = major;
        this.minor = minor;
    }

    public MysqlCharset(String charsetName, int mblen, int priority, String[] javaEncodings, int major, int minor, int subminor) {
        this(charsetName, mblen, priority, javaEncodings);
        this.major = major;
        this.minor = minor;
        this.subminor = subminor;
    }

    public String toString() {
        return PropertyAccessor.PROPERTY_KEY_PREFIX + "charsetName=" + this.charsetName + ",mblen=" + this.mblen + "]";
    }

    boolean isOkayForVersion(Connection conn) throws SQLException {
        return conn.versionMeetsMinimum(this.major, this.minor, this.subminor);
    }

    String getMatchingJavaEncoding(String javaEncoding) {
        if (javaEncoding != null && this.javaEncodingsUc.contains(javaEncoding.toUpperCase(Locale.ENGLISH))) {
            return javaEncoding;
        }
        return this.javaEncodingsUc.get(0);
    }
}
