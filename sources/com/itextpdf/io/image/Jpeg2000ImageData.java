package com.itextpdf.io.image;

import com.itextpdf.io.LogMessageConstant;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/image/Jpeg2000ImageData.class */
public class Jpeg2000ImageData extends ImageData {
    protected Parameters parameters;

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/image/Jpeg2000ImageData$Parameters.class */
    public static class Parameters {
        public int numOfComps;
        public List<ColorSpecBox> colorSpecBoxes = null;
        public boolean isJp2 = false;
        public boolean isJpxBaseline = false;
        public byte[] bpcBoxData;
    }

    /* loaded from: io-7.1.10.jar:com/itextpdf/io/image/Jpeg2000ImageData$ColorSpecBox.class */
    public static class ColorSpecBox extends ArrayList<Integer> {
        private static final long serialVersionUID = -6008490897027025733L;
        private byte[] colorProfile;

        public int getMeth() {
            return get(0).intValue();
        }

        public int getPrec() {
            return get(1).intValue();
        }

        public int getApprox() {
            return get(2).intValue();
        }

        public int getEnumCs() {
            return get(3).intValue();
        }

        public byte[] getColorProfile() {
            return this.colorProfile;
        }

        void setColorProfile(byte[] colorProfile) {
            this.colorProfile = colorProfile;
        }
    }

    protected Jpeg2000ImageData(URL url) {
        super(url, ImageType.JPEG2000);
    }

    protected Jpeg2000ImageData(byte[] bytes) {
        super(bytes, ImageType.JPEG2000);
    }

    @Override // com.itextpdf.io.image.ImageData
    public boolean canImageBeInline() {
        Logger logger = LoggerFactory.getLogger((Class<?>) ImageData.class);
        logger.warn(LogMessageConstant.IMAGE_HAS_JPXDECODE_FILTER);
        return false;
    }

    public Parameters getParameters() {
        return this.parameters;
    }
}
