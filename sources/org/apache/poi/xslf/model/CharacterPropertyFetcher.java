package org.apache.poi.xslf.model;

import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraphProperties;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/model/CharacterPropertyFetcher.class */
public abstract class CharacterPropertyFetcher<T> extends ParagraphPropertyFetcher<T> {
    public abstract boolean fetch(CTTextCharacterProperties cTTextCharacterProperties);

    public CharacterPropertyFetcher(int level) {
        super(level);
    }

    @Override // org.apache.poi.xslf.model.ParagraphPropertyFetcher
    public boolean fetch(CTTextParagraphProperties props) {
        if (props != null && props.isSetDefRPr()) {
            return fetch(props.getDefRPr());
        }
        return false;
    }
}
