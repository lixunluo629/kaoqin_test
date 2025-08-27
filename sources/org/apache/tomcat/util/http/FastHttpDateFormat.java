package org.apache.tomcat.util.http;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/FastHttpDateFormat.class */
public final class FastHttpDateFormat {
    public static final String RFC1123_DATE = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static volatile long currentDateGenerated;
    private static String currentDate;
    private static final Map<Long, String> formatCache;
    private static final Map<String, Long> parseCache;
    private static final int CACHE_SIZE = Integer.parseInt(System.getProperty("org.apache.tomcat.util.http.FastHttpDateFormat.CACHE_SIZE", "1000"));
    private static final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    private static final TimeZone gmtZone = TimeZone.getTimeZone("GMT");

    static {
        format.setTimeZone(gmtZone);
        currentDateGenerated = 0L;
        currentDate = null;
        formatCache = new ConcurrentHashMap(CACHE_SIZE);
        parseCache = new ConcurrentHashMap(CACHE_SIZE);
    }

    public static final String getCurrentDate() {
        long now = System.currentTimeMillis();
        if (now - currentDateGenerated > 1000) {
            synchronized (format) {
                if (now - currentDateGenerated > 1000) {
                    currentDate = format.format(new Date(now));
                    currentDateGenerated = now;
                }
            }
        }
        return currentDate;
    }

    public static final String formatDate(long value, DateFormat threadLocalformat) {
        String newDate;
        Long longValue = Long.valueOf(value);
        String cachedDate = formatCache.get(longValue);
        if (cachedDate != null) {
            return cachedDate;
        }
        Date dateValue = new Date(value);
        if (threadLocalformat != null) {
            newDate = threadLocalformat.format(dateValue);
            updateFormatCache(longValue, newDate);
        } else {
            synchronized (format) {
                newDate = format.format(dateValue);
            }
            updateFormatCache(longValue, newDate);
        }
        return newDate;
    }

    public static final long parseDate(String value, DateFormat[] threadLocalformats) throws ParseException {
        Long cachedDate = parseCache.get(value);
        if (cachedDate != null) {
            return cachedDate.longValue();
        }
        if (threadLocalformats != null) {
            Long date = internalParseDate(value, threadLocalformats);
            updateParseCache(value, date);
            if (date == null) {
                return -1L;
            }
            return date.longValue();
        }
        throw new IllegalArgumentException();
    }

    private static final Long internalParseDate(String value, DateFormat[] formats) throws ParseException {
        Date date = null;
        for (int i = 0; date == null && i < formats.length; i++) {
            try {
                date = formats[i].parse(value);
            } catch (ParseException e) {
            }
        }
        if (date == null) {
            return null;
        }
        return Long.valueOf(date.getTime());
    }

    private static void updateFormatCache(Long key, String value) {
        if (value == null) {
            return;
        }
        if (formatCache.size() > CACHE_SIZE) {
            formatCache.clear();
        }
        formatCache.put(key, value);
    }

    private static void updateParseCache(String key, Long value) {
        if (value == null) {
            return;
        }
        if (parseCache.size() > CACHE_SIZE) {
            parseCache.clear();
        }
        parseCache.put(key, value);
    }
}
