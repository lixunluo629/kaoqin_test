package org.apache.poi.xwpf.usermodel;

import org.apache.poi.util.Internal;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/XWPFFieldRun.class */
public class XWPFFieldRun extends XWPFRun {
    private CTSimpleField field;

    public XWPFFieldRun(CTSimpleField field, CTR run, IRunBody p) {
        super(run, p);
        this.field = field;
    }

    @Internal
    public CTSimpleField getCTField() {
        return this.field;
    }

    public String getFieldInstruction() {
        return this.field.getInstr();
    }

    public void setFieldInstruction(String instruction) {
        this.field.setInstr(instruction);
    }
}
