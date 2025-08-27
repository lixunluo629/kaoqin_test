package org.apache.poi.xdgf.usermodel;

import com.microsoft.schemas.office.visio.x2012.main.MasterType;
import org.apache.poi.util.Internal;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/XDGFMaster.class */
public class XDGFMaster {
    private MasterType _master;
    protected XDGFMasterContents _content;
    protected XDGFSheet _pageSheet;

    public XDGFMaster(MasterType master, XDGFMasterContents content, XDGFDocument document) {
        this._pageSheet = null;
        this._master = master;
        this._content = content;
        content.setMaster(this);
        if (master.isSetPageSheet()) {
            this._pageSheet = new XDGFPageSheet(master.getPageSheet(), document);
        }
    }

    @Internal
    protected MasterType getXmlObject() {
        return this._master;
    }

    public String toString() {
        return "<Master ID=\"" + getID() + "\" " + this._content + ">";
    }

    public long getID() {
        return this._master.getID();
    }

    public String getName() {
        return this._master.getName();
    }

    public XDGFMasterContents getContent() {
        return this._content;
    }

    public XDGFSheet getPageSheet() {
        return this._pageSheet;
    }
}
