package com.itextpdf.io.font.otf;

import java.io.Serializable;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/otf/GposAnchor.class */
public class GposAnchor implements Serializable {
    private static final long serialVersionUID = 7153858421411686094L;
    public int XCoordinate;
    public int YCoordinate;

    public GposAnchor() {
    }

    public GposAnchor(GposAnchor other) {
        this.XCoordinate = other.XCoordinate;
        this.YCoordinate = other.YCoordinate;
    }
}
