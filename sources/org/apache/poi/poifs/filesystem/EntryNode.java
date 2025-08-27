package org.apache.poi.poifs.filesystem;

import org.apache.poi.poifs.property.Property;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/filesystem/EntryNode.class */
public abstract class EntryNode implements Entry {
    private Property _property;
    private DirectoryNode _parent;

    protected abstract boolean isDeleteOK();

    protected EntryNode(Property property, DirectoryNode parent) {
        this._property = property;
        this._parent = parent;
    }

    protected Property getProperty() {
        return this._property;
    }

    protected boolean isRoot() {
        return this._parent == null;
    }

    @Override // org.apache.poi.poifs.filesystem.Entry
    public String getName() {
        return this._property.getName();
    }

    @Override // org.apache.poi.poifs.filesystem.Entry
    public boolean isDirectoryEntry() {
        return false;
    }

    @Override // org.apache.poi.poifs.filesystem.Entry
    public boolean isDocumentEntry() {
        return false;
    }

    @Override // org.apache.poi.poifs.filesystem.Entry
    public DirectoryEntry getParent() {
        return this._parent;
    }

    @Override // org.apache.poi.poifs.filesystem.Entry
    public boolean delete() {
        boolean rval = false;
        if (!isRoot() && isDeleteOK()) {
            rval = this._parent.deleteEntry(this);
        }
        return rval;
    }

    @Override // org.apache.poi.poifs.filesystem.Entry
    public boolean renameTo(String newName) throws ArrayIndexOutOfBoundsException {
        boolean rval = false;
        if (!isRoot()) {
            rval = this._parent.changeName(getName(), newName);
        }
        return rval;
    }
}
