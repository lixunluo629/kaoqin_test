package com.itextpdf.kernel.geom;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/geom/NoninvertibleTransformException.class */
public class NoninvertibleTransformException extends Exception {
    private static final long serialVersionUID = 6137225240503990466L;
    public static final String DETERMINANT_IS_ZERO_CANNOT_INVERT_TRANSFORMATION = "Determinant is zero. Cannot invert transformation.";

    public NoninvertibleTransformException(String message) {
        super(message);
    }
}
