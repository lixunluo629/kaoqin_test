package com.itextpdf.layout.hyphenation;

import com.itextpdf.io.util.ResourceUtil;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/hyphenation/Hyphenator.class */
public final class Hyphenator {
    private static final char SOFT_HYPHEN = 173;
    private static final Object staticLock = new Object();
    private static Logger log = LoggerFactory.getLogger((Class<?>) Hyphenator.class);
    private static HyphenationTreeCache hTreeCache;
    private static List<String> additionalHyphenationFileDirectories;
    protected String lang;
    protected String country;
    int leftMin;
    int rightMin;
    Map<String, String> hyphPathNames;

    public Hyphenator(String lang, String country, int leftMin, int rightMin) {
        this.lang = lang;
        this.country = country;
        this.leftMin = leftMin;
        this.rightMin = rightMin;
    }

    public Hyphenator(String lang, String country, int leftMin, int rightMin, Map<String, String> hyphPathNames) {
        this(lang, country, leftMin, rightMin);
        this.hyphPathNames = hyphPathNames;
    }

    public static void registerAdditionalHyphenationFileDirectory(String directory) {
        synchronized (staticLock) {
            if (additionalHyphenationFileDirectories == null) {
                additionalHyphenationFileDirectories = new ArrayList();
            }
            additionalHyphenationFileDirectories.add(directory);
        }
    }

    public static HyphenationTreeCache getHyphenationTreeCache() {
        synchronized (staticLock) {
            if (hTreeCache == null) {
                hTreeCache = new HyphenationTreeCache();
            }
        }
        return hTreeCache;
    }

    public static void clearHyphenationTreeCache() {
        synchronized (staticLock) {
            hTreeCache = new HyphenationTreeCache();
        }
    }

    public static HyphenationTree getHyphenationTree(String lang, String country, Map<String, String> hyphPathNames) throws IOException {
        String llccKey = HyphenationTreeCache.constructLlccKey(lang, country);
        HyphenationTreeCache cache = getHyphenationTreeCache();
        if (cache.isMissing(llccKey)) {
            return null;
        }
        HyphenationTree hTree = getHyphenationTree2(lang, country, hyphPathNames);
        if (hTree == null && country != null && !country.equals("none")) {
            String llKey = HyphenationTreeCache.constructLlccKey(lang, null);
            if (!cache.isMissing(llKey)) {
                hTree = getHyphenationTree2(lang, null, hyphPathNames);
                if (hTree != null && log.isDebugEnabled()) {
                    log.debug("Couldn't find hyphenation pattern for lang=\"" + lang + "\",country=\"" + country + "\". Using general language pattern for lang=\"" + lang + "\" instead.");
                }
                if (hTree == null) {
                    cache.noteMissing(llKey);
                } else {
                    cache.cache(llccKey, hTree);
                }
            }
        }
        if (hTree == null) {
            cache.noteMissing(llccKey);
            log.error("Couldn't find hyphenation pattern for lang=\"" + lang + SymbolConstants.QUOTES_SYMBOL + ((country == null || country.equals("none")) ? "" : ",country=\"" + country + SymbolConstants.QUOTES_SYMBOL) + ".");
        }
        return hTree;
    }

    public static HyphenationTree getHyphenationTree2(String lang, String country, Map<String, String> hyphPathNames) throws IOException {
        InputStream defaultHyphenationResourceStream;
        String llccKey = HyphenationTreeCache.constructLlccKey(lang, country);
        HyphenationTreeCache cache = getHyphenationTreeCache();
        HyphenationTree hTree = getHyphenationTreeCache().getHyphenationTree(lang, country);
        if (hTree != null) {
            return hTree;
        }
        String key = HyphenationTreeCache.constructUserKey(lang, country, hyphPathNames);
        if (key == null) {
            key = llccKey;
        }
        if (additionalHyphenationFileDirectories != null) {
            for (String dir : additionalHyphenationFileDirectories) {
                hTree = getHyphenationTree(dir, key);
                if (hTree != null) {
                    break;
                }
            }
        }
        if (hTree == null && (defaultHyphenationResourceStream = ResourceUtil.getResourceStream(HyphenationConstants.HYPHENATION_DEFAULT_RESOURCE + key + ".xml")) != null) {
            hTree = getHyphenationTree(defaultHyphenationResourceStream, key);
        }
        if (hTree != null) {
            cache.cache(llccKey, hTree);
        }
        return hTree;
    }

    public static HyphenationTree getHyphenationTree(String searchDirectory, String key) {
        String name = key + ".xml";
        try {
            InputStream fis = new FileInputStream(searchDirectory + File.separator + name);
            return getHyphenationTree(fis, name);
        } catch (IOException ioe) {
            if (log.isDebugEnabled()) {
                log.debug("I/O problem while trying to load " + name + ": " + ioe.getMessage());
                return null;
            }
            return null;
        }
    }

    public static HyphenationTree getHyphenationTree(InputStream in, String name) throws IOException {
        if (in == null) {
            return null;
        }
        try {
            try {
                HyphenationTree hTree = new HyphenationTree();
                hTree.loadPatterns(in, name);
                try {
                    in.close();
                } catch (Exception e) {
                }
                return hTree;
            } catch (Throwable th) {
                try {
                    in.close();
                } catch (Exception e2) {
                }
                throw th;
            }
        } catch (HyphenationException ex) {
            log.error("Can't load user patterns from XML file " + name + ": " + ex.getMessage());
            try {
                in.close();
            } catch (Exception e3) {
            }
            return null;
        }
    }

    public static Hyphenation hyphenate(String lang, String country, Map<String, String> hyphPathNames, String word, int leftMin, int rightMin) throws IOException {
        if (wordContainsSoftHyphens(word)) {
            return hyphenateBasedOnSoftHyphens(word, leftMin, rightMin);
        }
        HyphenationTree hTree = null;
        if (lang != null) {
            hTree = getHyphenationTree(lang, country, hyphPathNames);
        }
        if (hTree != null) {
            return hTree.hyphenate(word, leftMin, rightMin);
        }
        return null;
    }

    public static Hyphenation hyphenate(String lang, String country, String word, int leftMin, int rightMin) {
        return hyphenate(lang, country, null, word, leftMin, rightMin);
    }

    public Hyphenation hyphenate(String word) {
        return hyphenate(this.lang, this.country, this.hyphPathNames, word, this.leftMin, this.rightMin);
    }

    private static boolean wordContainsSoftHyphens(String word) {
        return word.indexOf(173) >= 0;
    }

    private static Hyphenation hyphenateBasedOnSoftHyphens(String word, int leftMin, int rightMin) {
        List<Integer> softHyphens = new ArrayList<>();
        int i = -1;
        while (true) {
            int lastSoftHyphenIndex = i;
            int curSoftHyphenIndex = word.indexOf(173, lastSoftHyphenIndex + 1);
            if (curSoftHyphenIndex <= 0) {
                break;
            }
            softHyphens.add(Integer.valueOf(curSoftHyphenIndex));
            i = curSoftHyphenIndex;
        }
        int leftInd = 0;
        int rightInd = softHyphens.size() - 1;
        while (leftInd < softHyphens.size() && word.substring(0, softHyphens.get(leftInd).intValue()).replace(String.valueOf((char) 173), "").length() < leftMin) {
            leftInd++;
        }
        while (rightInd >= 0 && word.substring(softHyphens.get(rightInd).intValue() + 1).replace(String.valueOf((char) 173), "").length() < rightMin) {
            rightInd--;
        }
        if (leftInd <= rightInd) {
            int[] hyphenationPoints = new int[(rightInd - leftInd) + 1];
            for (int i2 = leftInd; i2 <= rightInd; i2++) {
                hyphenationPoints[i2 - leftInd] = softHyphens.get(i2).intValue();
            }
            return new Hyphenation(word, hyphenationPoints);
        }
        return null;
    }
}
