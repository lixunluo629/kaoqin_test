package org.bouncycastle.i18n.filter;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/i18n/filter/SQLFilter.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/i18n/filter/SQLFilter.class */
public class SQLFilter implements Filter {
    @Override // org.bouncycastle.i18n.filter.Filter
    public String doFilter(String str) {
        StringBuffer stringBuffer = new StringBuffer(str);
        int i = 0;
        while (i < stringBuffer.length()) {
            switch (stringBuffer.charAt(i)) {
                case '\n':
                    stringBuffer.replace(i, i + 1, "\\n");
                    i++;
                    break;
                case '\r':
                    stringBuffer.replace(i, i + 1, "\\r");
                    i++;
                    break;
                case '\"':
                    stringBuffer.replace(i, i + 1, "\\\"");
                    i++;
                    break;
                case '\'':
                    stringBuffer.replace(i, i + 1, "\\'");
                    i++;
                    break;
                case '-':
                    stringBuffer.replace(i, i + 1, "\\-");
                    i++;
                    break;
                case '/':
                    stringBuffer.replace(i, i + 1, "\\/");
                    i++;
                    break;
                case ';':
                    stringBuffer.replace(i, i + 1, "\\;");
                    i++;
                    break;
                case '=':
                    stringBuffer.replace(i, i + 1, "\\=");
                    i++;
                    break;
                case '\\':
                    stringBuffer.replace(i, i + 1, "\\\\");
                    i++;
                    break;
            }
            i++;
        }
        return stringBuffer.toString();
    }

    @Override // org.bouncycastle.i18n.filter.Filter
    public String doFilterUrl(String str) {
        return doFilter(str);
    }
}
