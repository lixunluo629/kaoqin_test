package com.itextpdf.layout.hyphenation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/hyphenation/HyphenationTreeCache.class */
public class HyphenationTreeCache {
    private Map<String, HyphenationTree> hyphenTrees = new HashMap();
    private Set<String> missingHyphenationTrees;

    public HyphenationTree getHyphenationTree(String lang, String country) {
        String key = constructLlccKey(lang, country);
        if (key == null) {
            return null;
        }
        if (this.hyphenTrees.containsKey(key)) {
            return this.hyphenTrees.get(key);
        }
        if (this.hyphenTrees.containsKey(lang)) {
            return this.hyphenTrees.get(lang);
        }
        return null;
    }

    public static String constructLlccKey(String lang, String country) {
        String key = lang;
        if (country != null && !country.equals("none")) {
            key = key + "_" + country;
        }
        return key;
    }

    public static String constructUserKey(String lang, String country, Map<String, String> hyphPatNames) {
        String userKey = null;
        if (hyphPatNames != null) {
            String key = constructLlccKey(lang, country);
            userKey = hyphPatNames.get(key.replace('_', '-'));
        }
        return userKey;
    }

    public void cache(String key, HyphenationTree hTree) {
        this.hyphenTrees.put(key, hTree);
    }

    public void noteMissing(String key) {
        if (this.missingHyphenationTrees == null) {
            this.missingHyphenationTrees = new HashSet();
        }
        this.missingHyphenationTrees.add(key);
    }

    public boolean isMissing(String key) {
        return this.missingHyphenationTrees != null && this.missingHyphenationTrees.contains(key);
    }
}
