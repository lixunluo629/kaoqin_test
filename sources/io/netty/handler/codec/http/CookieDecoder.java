package io.netty.handler.codec.http;

import io.netty.handler.codec.DateFormatter;
import io.netty.handler.codec.http.cookie.CookieHeaderNames;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/CookieDecoder.class */
public final class CookieDecoder {
    private final InternalLogger logger = InternalLoggerFactory.getInstance(getClass());
    private static final CookieDecoder STRICT = new CookieDecoder(true);
    private static final CookieDecoder LAX = new CookieDecoder(false);
    private static final String COMMENT = "Comment";
    private static final String COMMENTURL = "CommentURL";
    private static final String DISCARD = "Discard";
    private static final String PORT = "Port";
    private static final String VERSION = "Version";
    private final boolean strict;

    public static Set<Cookie> decode(String header) {
        return decode(header, true);
    }

    public static Set<Cookie> decode(String header, boolean strict) {
        return (strict ? STRICT : LAX).doDecode(header);
    }

    private Set<Cookie> doDecode(String header) throws NumberFormatException {
        int i;
        List<String> names = new ArrayList<>(8);
        List<String> values = new ArrayList<>(8);
        extractKeyValuePairs(header, names, values);
        if (names.isEmpty()) {
            return Collections.emptySet();
        }
        int version = 0;
        if (names.get(0).equalsIgnoreCase(VERSION)) {
            try {
                version = Integer.parseInt(values.get(0));
            } catch (NumberFormatException e) {
            }
            i = 1;
        } else {
            i = 0;
        }
        if (names.size() <= i) {
            return Collections.emptySet();
        }
        Set<Cookie> cookies = new TreeSet<>();
        while (i < names.size()) {
            String name = names.get(i);
            String value = values.get(i);
            if (value == null) {
                value = "";
            }
            Cookie c = initCookie(name, value);
            if (c == null) {
                break;
            }
            boolean discard = false;
            boolean secure = false;
            boolean httpOnly = false;
            String comment = null;
            String commentURL = null;
            String domain = null;
            String path = null;
            long maxAge = Long.MIN_VALUE;
            List<Integer> ports = new ArrayList<>(2);
            int j = i + 1;
            while (j < names.size()) {
                String name2 = names.get(j);
                String value2 = values.get(j);
                if (DISCARD.equalsIgnoreCase(name2)) {
                    discard = true;
                } else if (CookieHeaderNames.SECURE.equalsIgnoreCase(name2)) {
                    secure = true;
                } else if (CookieHeaderNames.HTTPONLY.equalsIgnoreCase(name2)) {
                    httpOnly = true;
                } else if (COMMENT.equalsIgnoreCase(name2)) {
                    comment = value2;
                } else if (COMMENTURL.equalsIgnoreCase(name2)) {
                    commentURL = value2;
                } else if (CookieHeaderNames.DOMAIN.equalsIgnoreCase(name2)) {
                    domain = value2;
                } else if (CookieHeaderNames.PATH.equalsIgnoreCase(name2)) {
                    path = value2;
                } else if ("Expires".equalsIgnoreCase(name2)) {
                    Date date = DateFormatter.parseHttpDate(value2);
                    if (date != null) {
                        long maxAgeMillis = date.getTime() - System.currentTimeMillis();
                        maxAge = (maxAgeMillis / 1000) + (maxAgeMillis % 1000 != 0 ? 1 : 0);
                    }
                } else if (CookieHeaderNames.MAX_AGE.equalsIgnoreCase(name2)) {
                    maxAge = Integer.parseInt(value2);
                } else if (VERSION.equalsIgnoreCase(name2)) {
                    version = Integer.parseInt(value2);
                } else {
                    if (!PORT.equalsIgnoreCase(name2)) {
                        break;
                    }
                    String[] portList = value2.split(",");
                    for (String s1 : portList) {
                        try {
                            ports.add(Integer.valueOf(s1));
                        } catch (NumberFormatException e2) {
                        }
                    }
                }
                j++;
                i++;
            }
            c.setVersion(version);
            c.setMaxAge(maxAge);
            c.setPath(path);
            c.setDomain(domain);
            c.setSecure(secure);
            c.setHttpOnly(httpOnly);
            if (version > 0) {
                c.setComment(comment);
            }
            if (version > 1) {
                c.setCommentUrl(commentURL);
                c.setPorts(ports);
                c.setDiscard(discard);
            }
            cookies.add(c);
            i++;
        }
        return cookies;
    }

    private static void extractKeyValuePairs(String header, List<String> names, List<String> values) {
        String name;
        String value;
        int headerLen = header.length();
        int i = 0;
        while (i != headerLen) {
            switch (header.charAt(i)) {
                case '\t':
                case '\n':
                case 11:
                case '\f':
                case '\r':
                case ' ':
                case ',':
                case ';':
                    i++;
                    break;
                default:
                    while (i != headerLen) {
                        if (header.charAt(i) == '$') {
                            i++;
                        } else {
                            if (i == headerLen) {
                                name = null;
                                value = null;
                            } else {
                                int newNameStart = i;
                                while (true) {
                                    switch (header.charAt(i)) {
                                        case ';':
                                            name = header.substring(newNameStart, i);
                                            value = null;
                                            break;
                                        case '=':
                                            name = header.substring(newNameStart, i);
                                            i++;
                                            if (i == headerLen) {
                                                value = "";
                                                break;
                                            } else {
                                                char c = header.charAt(i);
                                                if (c == '\"' || c == '\'') {
                                                    StringBuilder newValueBuf = new StringBuilder(header.length() - i);
                                                    boolean hadBackslash = false;
                                                    i++;
                                                    while (true) {
                                                        if (i == headerLen) {
                                                            value = newValueBuf.toString();
                                                            break;
                                                        } else if (hadBackslash) {
                                                            hadBackslash = false;
                                                            int i2 = i;
                                                            i++;
                                                            char c2 = header.charAt(i2);
                                                            switch (c2) {
                                                                case '\"':
                                                                case '\'':
                                                                case '\\':
                                                                    newValueBuf.setCharAt(newValueBuf.length() - 1, c2);
                                                                    break;
                                                                default:
                                                                    newValueBuf.append(c2);
                                                                    break;
                                                            }
                                                        } else {
                                                            int i3 = i;
                                                            i++;
                                                            char c3 = header.charAt(i3);
                                                            if (c3 == c) {
                                                                value = newValueBuf.toString();
                                                                break;
                                                            } else {
                                                                newValueBuf.append(c3);
                                                                if (c3 == '\\') {
                                                                    hadBackslash = true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    int semiPos = header.indexOf(59, i);
                                                    if (semiPos > 0) {
                                                        value = header.substring(i, semiPos);
                                                        i = semiPos;
                                                        break;
                                                    } else {
                                                        value = header.substring(i);
                                                        i = headerLen;
                                                        break;
                                                    }
                                                }
                                            }
                                            break;
                                        default:
                                            i++;
                                            if (i == headerLen) {
                                                name = header.substring(newNameStart);
                                                value = null;
                                                break;
                                            }
                                    }
                                }
                            }
                            names.add(name);
                            values.add(value);
                            break;
                        }
                    }
                    return;
            }
        }
    }

    private CookieDecoder(boolean strict) {
        this.strict = strict;
    }

    private DefaultCookie initCookie(String name, String value) {
        int invalidOctetPos;
        int invalidOctetPos2;
        if (name == null || name.length() == 0) {
            this.logger.debug("Skipping cookie with null name");
            return null;
        }
        if (value == null) {
            this.logger.debug("Skipping cookie with null value");
            return null;
        }
        CharSequence unwrappedValue = CookieUtil.unwrapValue(value);
        if (unwrappedValue == null) {
            this.logger.debug("Skipping cookie because starting quotes are not properly balanced in '{}'", unwrappedValue);
            return null;
        }
        if (this.strict && (invalidOctetPos2 = CookieUtil.firstInvalidCookieNameOctet(name)) >= 0) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Skipping cookie because name '{}' contains invalid char '{}'", name, Character.valueOf(name.charAt(invalidOctetPos2)));
                return null;
            }
            return null;
        }
        boolean wrap = unwrappedValue.length() != value.length();
        if (this.strict && (invalidOctetPos = CookieUtil.firstInvalidCookieValueOctet(unwrappedValue)) >= 0) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Skipping cookie because value '{}' contains invalid char '{}'", unwrappedValue, Character.valueOf(unwrappedValue.charAt(invalidOctetPos)));
                return null;
            }
            return null;
        }
        DefaultCookie cookie = new DefaultCookie(name, unwrappedValue.toString());
        cookie.setWrap(wrap);
        return cookie;
    }
}
