package net.dongliu.apk.parser.struct.resource;

import net.dongliu.apk.parser.utils.Unsigned;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/ResTableConfig.class */
public class ResTableConfig {
    private int size;
    private short mcc;
    private short mnc;
    private String language;
    private String country;
    private byte orientation;
    private byte touchscreen;
    private short density;
    private short keyboard;
    private short navigation;
    private short inputFlags;
    private short inputPad0;
    private int screenWidth;
    private int screenHeight;
    private int sdkVersion;
    private int minorVersion;
    private short screenLayout;
    private short uiMode;
    private short screenConfigPad1;
    private short screenConfigPad2;

    public int getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = Unsigned.ensureUInt(size);
    }

    public short getMcc() {
        return this.mcc;
    }

    public void setMcc(short mcc) {
        this.mcc = mcc;
    }

    public short getMnc() {
        return this.mnc;
    }

    public void setMnc(short mnc) {
        this.mnc = mnc;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public short getOrientation() {
        return (short) (this.orientation & 255);
    }

    public void setOrientation(short orientation) {
        this.orientation = (byte) orientation;
    }

    public short getTouchscreen() {
        return (short) (this.touchscreen & 255);
    }

    public void setTouchscreen(short touchscreen) {
        this.touchscreen = (byte) touchscreen;
    }

    public int getDensity() {
        return this.density & 65535;
    }

    public void setDensity(int density) {
        this.density = (short) density;
    }

    public short getKeyboard() {
        return this.keyboard;
    }

    public void setKeyboard(short keyboard) {
        this.keyboard = keyboard;
    }

    public short getNavigation() {
        return this.navigation;
    }

    public void setNavigation(short navigation) {
        this.navigation = navigation;
    }

    public short getInputFlags() {
        return this.inputFlags;
    }

    public void setInputFlags(short inputFlags) {
        this.inputFlags = inputFlags;
    }

    public short getInputPad0() {
        return this.inputPad0;
    }

    public void setInputPad0(short inputPad0) {
        this.inputPad0 = inputPad0;
    }

    public int getScreenWidth() {
        return this.screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return this.screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getSdkVersion() {
        return this.sdkVersion;
    }

    public void setSdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public int getMinorVersion() {
        return this.minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public short getScreenLayout() {
        return this.screenLayout;
    }

    public void setScreenLayout(short screenLayout) {
        this.screenLayout = screenLayout;
    }

    public short getUiMode() {
        return this.uiMode;
    }

    public void setUiMode(short uiMode) {
        this.uiMode = uiMode;
    }

    public short getScreenConfigPad1() {
        return this.screenConfigPad1;
    }

    public void setScreenConfigPad1(short screenConfigPad1) {
        this.screenConfigPad1 = screenConfigPad1;
    }

    public short getScreenConfigPad2() {
        return this.screenConfigPad2;
    }

    public void setScreenConfigPad2(short screenConfigPad2) {
        this.screenConfigPad2 = screenConfigPad2;
    }
}
