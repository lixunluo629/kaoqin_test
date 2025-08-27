package com.itextpdf.layout.font;

import java.util.ArrayList;
import java.util.List;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/font/FontSelectorKey.class */
final class FontSelectorKey {
    private List<String> fontFamilies;
    private FontCharacteristics fc;

    FontSelectorKey(List<String> fontFamilies, FontCharacteristics fc) {
        this.fontFamilies = new ArrayList(fontFamilies);
        this.fc = fc;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FontSelectorKey that = (FontSelectorKey) o;
        return this.fontFamilies.equals(that.fontFamilies) && (this.fc == null ? that.fc == null : this.fc.equals(that.fc));
    }

    public int hashCode() {
        int result = this.fontFamilies != null ? this.fontFamilies.hashCode() : 0;
        return (31 * result) + (this.fc != null ? this.fc.hashCode() : 0);
    }
}
