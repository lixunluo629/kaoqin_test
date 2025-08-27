package org.bouncycastle.i18n.filter;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/i18n/filter/HTMLFilter.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/i18n/filter/HTMLFilter.class */
public class HTMLFilter implements Filter {
    @Override // org.bouncycastle.i18n.filter.Filter
    public String doFilter(String str) {
        StringBuffer stringBuffer = new StringBuffer(str);
        int i = 0;
        while (i < stringBuffer.length()) {
            switch (stringBuffer.charAt(i)) {
                case '\"':
                    stringBuffer.replace(i, i + 1, "&#34");
                    break;
                case '#':
                    stringBuffer.replace(i, i + 1, "&#35");
                    break;
                case '$':
                case '*':
                case ',':
                case '.':
                case '/':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case ':':
                case '=':
                default:
                    i -= 3;
                    break;
                case '%':
                    stringBuffer.replace(i, i + 1, "&#37");
                    break;
                case '&':
                    stringBuffer.replace(i, i + 1, "&#38");
                    break;
                case '\'':
                    stringBuffer.replace(i, i + 1, "&#39");
                    break;
                case '(':
                    stringBuffer.replace(i, i + 1, "&#40");
                    break;
                case ')':
                    stringBuffer.replace(i, i + 1, "&#41");
                    break;
                case '+':
                    stringBuffer.replace(i, i + 1, "&#43");
                    break;
                case '-':
                    stringBuffer.replace(i, i + 1, "&#45");
                    break;
                case ';':
                    stringBuffer.replace(i, i + 1, "&#59");
                    break;
                case '<':
                    stringBuffer.replace(i, i + 1, "&#60");
                    break;
                case '>':
                    stringBuffer.replace(i, i + 1, "&#62");
                    break;
            }
            i += 4;
        }
        return stringBuffer.toString();
    }

    @Override // org.bouncycastle.i18n.filter.Filter
    public String doFilterUrl(String str) {
        return doFilter(str);
    }
}
