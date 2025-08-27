package com.itextpdf.layout.font;

import java.util.HashMap;
import java.util.Map;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/FontSelectorCache.class */
class FontSelectorCache {
    private final FontSetSelectors defaultSelectors;
    private final FontSet defaultFontSet;
    private final Map<Long, FontSetSelectors> caches = new HashMap();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FontSelectorCache.class.desiredAssertionStatus();
    }

    FontSelectorCache(FontSet defaultFontSet) {
        if (!$assertionsDisabled && defaultFontSet == null) {
            throw new AssertionError();
        }
        this.defaultSelectors = new FontSetSelectors();
        this.defaultSelectors.update(defaultFontSet);
        this.defaultFontSet = defaultFontSet;
    }

    FontSelector get(FontSelectorKey key) {
        if (update(null, null)) {
            return null;
        }
        return this.defaultSelectors.map.get(key);
    }

    FontSelector get(FontSelectorKey key, FontSet fontSet) {
        if (fontSet == null) {
            return get(key);
        }
        FontSetSelectors selectors = this.caches.get(Long.valueOf(fontSet.getId()));
        if (selectors == null) {
            Map<Long, FontSetSelectors> map = this.caches;
            Long lValueOf = Long.valueOf(fontSet.getId());
            FontSetSelectors fontSetSelectors = new FontSetSelectors();
            selectors = fontSetSelectors;
            map.put(lValueOf, fontSetSelectors);
        }
        if (update(selectors, fontSet)) {
            return null;
        }
        return selectors.map.get(key);
    }

    void put(FontSelectorKey key, FontSelector fontSelector) {
        update(null, null);
        this.defaultSelectors.map.put(key, fontSelector);
    }

    void put(FontSelectorKey key, FontSelector fontSelector, FontSet fontSet) {
        if (fontSet == null) {
            put(key, fontSelector);
            return;
        }
        FontSetSelectors selectors = this.caches.get(Long.valueOf(fontSet.getId()));
        if (selectors == null) {
            Map<Long, FontSetSelectors> map = this.caches;
            Long lValueOf = Long.valueOf(fontSet.getId());
            FontSetSelectors fontSetSelectors = new FontSetSelectors();
            selectors = fontSetSelectors;
            map.put(lValueOf, fontSetSelectors);
        }
        update(selectors, fontSet);
        selectors.map.put(key, fontSelector);
    }

    private boolean update(FontSetSelectors selectors, FontSet fontSet) {
        boolean updated = false;
        if (this.defaultSelectors.update(this.defaultFontSet)) {
            updated = true;
        }
        if (selectors != null && selectors.update(fontSet)) {
            updated = true;
        }
        return updated;
    }

    /* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/FontSelectorCache$FontSetSelectors.class */
    private static class FontSetSelectors {
        final Map<FontSelectorKey, FontSelector> map;
        private int fontSetSize;
        static final /* synthetic */ boolean $assertionsDisabled;

        private FontSetSelectors() {
            this.map = new HashMap();
            this.fontSetSize = -1;
        }

        static {
            $assertionsDisabled = !FontSelectorCache.class.desiredAssertionStatus();
        }

        boolean update(FontSet fontSet) {
            if (!$assertionsDisabled && fontSet == null) {
                throw new AssertionError();
            }
            if (this.fontSetSize == fontSet.size()) {
                return false;
            }
            this.map.clear();
            this.fontSetSize = fontSet.size();
            return true;
        }
    }
}
