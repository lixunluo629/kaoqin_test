package org.apache.poi.ddf;

/* loaded from: poi-3.17.jar:org/apache/poi/ddf/EscherProperty.class */
public abstract class EscherProperty {
    private short _id;

    public abstract int serializeSimplePart(byte[] bArr, int i);

    public abstract int serializeComplexPart(byte[] bArr, int i);

    public abstract String toString();

    public EscherProperty(short id) {
        this._id = id;
    }

    public EscherProperty(short propertyNumber, boolean isComplex, boolean isBlipId) {
        this._id = (short) (propertyNumber + (isComplex ? (short) 32768 : (short) 0) + (isBlipId ? 16384 : 0));
    }

    public short getId() {
        return this._id;
    }

    public short getPropertyNumber() {
        return (short) (this._id & 16383);
    }

    public boolean isComplex() {
        return (this._id & Short.MIN_VALUE) != 0;
    }

    public boolean isBlipId() {
        return (this._id & 16384) != 0;
    }

    public String getName() {
        return EscherProperties.getPropertyName(getPropertyNumber());
    }

    public int getPropertySize() {
        return 6;
    }

    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append("<").append(getClass().getSimpleName()).append(" id=\"").append((int) getId()).append("\" name=\"").append(getName()).append("\" blipId=\"").append(isBlipId()).append("\"/>\n");
        return builder.toString();
    }
}
