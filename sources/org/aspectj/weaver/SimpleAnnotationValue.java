package org.aspectj.weaver;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/SimpleAnnotationValue.class */
public class SimpleAnnotationValue extends AnnotationValue {
    private byte theByte;
    private char theChar;
    private int theInt;
    private String theString;
    private double theDouble;
    private float theFloat;
    private long theLong;
    private short theShort;
    private boolean theBoolean;

    public SimpleAnnotationValue(int kind) {
        super(kind);
    }

    public SimpleAnnotationValue(int kind, Object value) {
        super(kind);
        switch (kind) {
            case 66:
                this.theByte = ((Byte) value).byteValue();
                return;
            case 67:
                this.theChar = ((Character) value).charValue();
                return;
            case 68:
                this.theDouble = ((Double) value).doubleValue();
                return;
            case 70:
                this.theFloat = ((Float) value).floatValue();
                return;
            case 73:
                this.theInt = ((Integer) value).intValue();
                return;
            case 74:
                this.theLong = ((Long) value).longValue();
                return;
            case 83:
                this.theShort = ((Short) value).shortValue();
                return;
            case 90:
                this.theBoolean = ((Boolean) value).booleanValue();
                return;
            case 115:
                this.theString = (String) value;
                return;
            default:
                throw new BCException("Not implemented for this kind: " + whatKindIsThis(kind));
        }
    }

    public void setValueString(String s) {
        this.theString = s;
    }

    public void setValueByte(byte b) {
        this.theByte = b;
    }

    public void setValueChar(char c) {
        this.theChar = c;
    }

    public void setValueInt(int i) {
        this.theInt = i;
    }

    @Override // org.aspectj.weaver.AnnotationValue
    public String stringify() {
        switch (this.valueKind) {
            case 66:
                return Byte.toString(this.theByte);
            case 67:
                return new Character(this.theChar).toString();
            case 68:
                return Double.toString(this.theDouble);
            case 70:
                return Float.toString(this.theFloat);
            case 73:
                return Integer.toString(this.theInt);
            case 74:
                return Long.toString(this.theLong);
            case 83:
                return Short.toString(this.theShort);
            case 90:
                return new Boolean(this.theBoolean).toString();
            case 115:
                return this.theString;
            default:
                throw new BCException("Do not understand this kind: " + this.valueKind);
        }
    }

    public String toString() {
        return stringify();
    }
}
