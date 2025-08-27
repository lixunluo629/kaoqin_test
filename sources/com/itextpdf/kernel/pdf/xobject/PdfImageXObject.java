package com.itextpdf.kernel.pdf.xobject;

import com.itextpdf.io.LogMessageConstant;
import com.itextpdf.io.colors.IccProfile;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageType;
import com.itextpdf.io.image.RawImageData;
import com.itextpdf.io.image.RawImageHelper;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfLiteral;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.canvas.wmf.WmfImageData;
import com.itextpdf.kernel.pdf.colorspace.PdfCieBasedCs;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.kernel.pdf.colorspace.PdfSpecialCs;
import com.itextpdf.kernel.pdf.filters.DoNothingFilter;
import com.itextpdf.kernel.pdf.filters.FilterHandlers;
import com.itextpdf.kernel.pdf.filters.IFilterHandler;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.slf4j.LoggerFactory;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/xobject/PdfImageXObject.class */
public class PdfImageXObject extends PdfXObject {
    private static final long serialVersionUID = -205889576153966580L;
    private float width;
    private float height;
    private boolean mask;
    private boolean softMask;

    public PdfImageXObject(ImageData image) {
        this(image, null);
    }

    public PdfImageXObject(ImageData image, PdfImageXObject imageMask) {
        this(createPdfStream(checkImageType(image), imageMask));
        this.mask = image.isMask();
        this.softMask = image.isSoftMask();
    }

    public PdfImageXObject(PdfStream pdfStream) {
        super(pdfStream);
        if (!pdfStream.isFlushed()) {
            initWidthField();
            initHeightField();
        }
    }

    @Override // com.itextpdf.kernel.pdf.xobject.PdfXObject
    public float getWidth() {
        return this.width;
    }

    @Override // com.itextpdf.kernel.pdf.xobject.PdfXObject
    public float getHeight() {
        return this.height;
    }

    @Override // com.itextpdf.kernel.pdf.PdfObjectWrapper
    public void flush() {
        super.flush();
    }

    public PdfImageXObject copyTo(PdfDocument document) {
        PdfImageXObject image = new PdfImageXObject((PdfStream) getPdfObject().copyTo(document));
        image.mask = this.mask;
        image.softMask = this.softMask;
        return image;
    }

    public BufferedImage getBufferedImage() throws IOException {
        byte[] img = getImageBytes();
        return ImageIO.read(new ByteArrayInputStream(img));
    }

    public byte[] getImageBytes() {
        return getImageBytes(true);
    }

    public byte[] getImageBytes(boolean decoded) {
        byte[] bytes = getPdfObject().getBytes(false);
        if (decoded) {
            Map<PdfName, IFilterHandler> filters = new HashMap<>(FilterHandlers.getDefaultFilterHandlers());
            DoNothingFilter stubFilter = new DoNothingFilter();
            filters.put(PdfName.DCTDecode, stubFilter);
            filters.put(PdfName.JBIG2Decode, stubFilter);
            filters.put(PdfName.JPXDecode, stubFilter);
            bytes = PdfReader.decodeBytes(bytes, getPdfObject(), filters);
            if (stubFilter.getLastFilterName() == null) {
                try {
                    bytes = new ImagePdfBytesInfo(this).decodeTiffAndPngBytes(bytes);
                } catch (IOException e) {
                    throw new RuntimeException("IO exception in PdfImageXObject", e);
                }
            }
        }
        return bytes;
    }

    public ImageType identifyImageType() {
        PdfObject filter = getPdfObject().get(PdfName.Filter);
        PdfArray filters = new PdfArray();
        if (filter != null) {
            if (filter.getType() == 6) {
                filters.add(filter);
            } else if (filter.getType() == 1) {
                filters = (PdfArray) filter;
            }
        }
        for (int i = filters.size() - 1; i >= 0; i--) {
            PdfName filterName = (PdfName) filters.get(i);
            if (PdfName.DCTDecode.equals(filterName)) {
                return ImageType.JPEG;
            }
            if (PdfName.JBIG2Decode.equals(filterName)) {
                return ImageType.JBIG2;
            }
            if (PdfName.JPXDecode.equals(filterName)) {
                return ImageType.JPEG2000;
            }
        }
        ImagePdfBytesInfo imageInfo = new ImagePdfBytesInfo(this);
        if (imageInfo.getPngColorType() < 0) {
            return ImageType.TIFF;
        }
        return ImageType.PNG;
    }

    public String identifyImageFileExtension() {
        ImageType bytesType = identifyImageType();
        switch (bytesType) {
            case PNG:
                return ContentTypes.EXTENSION_PNG;
            case JPEG:
                return ContentTypes.EXTENSION_JPG_1;
            case JPEG2000:
                return "jp2";
            case TIFF:
                return "tif";
            case JBIG2:
                return "jbig2";
            default:
                throw new IllegalStateException("Should have never happened. This type of image is not allowed for ImageXObject");
        }
    }

    public PdfImageXObject put(PdfName key, PdfObject value) {
        getPdfObject().put(key, value);
        return this;
    }

    private float initWidthField() {
        PdfNumber wNum = getPdfObject().getAsNumber(PdfName.Width);
        if (wNum != null) {
            this.width = wNum.floatValue();
        }
        return this.width;
    }

    private float initHeightField() {
        PdfNumber hNum = getPdfObject().getAsNumber(PdfName.Height);
        if (hNum != null) {
            this.height = hNum.floatValue();
        }
        return this.height;
    }

    private static PdfStream createPdfStream(ImageData image, PdfImageXObject imageMask) {
        PdfName colorSpace;
        if (image.getOriginalType() == ImageType.RAW) {
            RawImageHelper.updateImageAttributes((RawImageData) image, null);
        }
        PdfStream stream = new PdfStream(image.getData());
        String filter = image.getFilter();
        if (filter != null && "JPXDecode".equals(filter) && image.getColorSpace() <= 0) {
            stream.setCompressionLevel(0);
            image.setBpc(0);
        }
        stream.put(PdfName.Type, PdfName.XObject);
        stream.put(PdfName.Subtype, PdfName.Image);
        PdfDictionary decodeParms = createDictionaryFromMap(stream, image.getDecodeParms());
        if (decodeParms != null) {
            stream.put(PdfName.DecodeParms, decodeParms);
        }
        switch (image.getColorSpace()) {
            case 1:
                colorSpace = PdfName.DeviceGray;
                break;
            case 3:
                colorSpace = PdfName.DeviceRGB;
                break;
            default:
                colorSpace = PdfName.DeviceCMYK;
                break;
        }
        stream.put(PdfName.ColorSpace, colorSpace);
        if (image.getBpc() != 0) {
            stream.put(PdfName.BitsPerComponent, new PdfNumber(image.getBpc()));
        }
        if (image.getFilter() != null) {
            stream.put(PdfName.Filter, new PdfName(image.getFilter()));
        }
        if (image.getColorSpace() == -1) {
            stream.remove(PdfName.ColorSpace);
        }
        PdfDictionary additional = createDictionaryFromMap(stream, image.getImageAttributes());
        if (additional != null) {
            stream.putAll(additional);
        }
        IccProfile iccProfile = image.getProfile();
        if (iccProfile != null) {
            PdfStream iccProfileStream = PdfCieBasedCs.IccBased.getIccProfileStream(iccProfile);
            PdfArray iccBasedColorSpace = new PdfArray();
            iccBasedColorSpace.add(PdfName.ICCBased);
            iccBasedColorSpace.add(iccProfileStream);
            PdfObject colorSpaceObject = stream.get(PdfName.ColorSpace);
            boolean iccProfileShouldBeApplied = true;
            if (colorSpaceObject != null) {
                PdfColorSpace cs = PdfColorSpace.makeColorSpace(colorSpaceObject);
                if (cs == null) {
                    LoggerFactory.getLogger((Class<?>) PdfImageXObject.class).error(LogMessageConstant.IMAGE_HAS_INCORRECT_OR_UNSUPPORTED_COLOR_SPACE_OVERRIDDEN_BY_ICC_PROFILE);
                } else if (cs instanceof PdfSpecialCs.Indexed) {
                    PdfColorSpace baseCs = ((PdfSpecialCs.Indexed) cs).getBaseCs();
                    if (baseCs == null) {
                        LoggerFactory.getLogger((Class<?>) PdfImageXObject.class).error(LogMessageConstant.IMAGE_HAS_INCORRECT_OR_UNSUPPORTED_BASE_COLOR_SPACE_IN_INDEXED_COLOR_SPACE_OVERRIDDEN_BY_ICC_PROFILE);
                    } else if (baseCs.getNumberOfComponents() != iccProfile.getNumComponents()) {
                        LoggerFactory.getLogger((Class<?>) PdfImageXObject.class).error(LogMessageConstant.IMAGE_HAS_ICC_PROFILE_WITH_INCOMPATIBLE_NUMBER_OF_COLOR_COMPONENTS_COMPARED_TO_BASE_COLOR_SPACE_IN_INDEXED_COLOR_SPACE);
                        iccProfileShouldBeApplied = false;
                    } else {
                        iccProfileStream.put(PdfName.Alternate, baseCs.getPdfObject());
                    }
                    if (iccProfileShouldBeApplied) {
                        ((PdfArray) colorSpaceObject).set(1, iccBasedColorSpace);
                        iccProfileShouldBeApplied = false;
                    }
                } else if (cs.getNumberOfComponents() != iccProfile.getNumComponents()) {
                    LoggerFactory.getLogger((Class<?>) PdfImageXObject.class).error(LogMessageConstant.IMAGE_HAS_ICC_PROFILE_WITH_INCOMPATIBLE_NUMBER_OF_COLOR_COMPONENTS_COMPARED_TO_COLOR_SPACE);
                    iccProfileShouldBeApplied = false;
                } else {
                    iccProfileStream.put(PdfName.Alternate, colorSpaceObject);
                }
            }
            if (iccProfileShouldBeApplied) {
                stream.put(PdfName.ColorSpace, iccBasedColorSpace);
            }
        }
        if (image.isMask() && (image.getBpc() == 1 || image.getBpc() > 255)) {
            stream.put(PdfName.ImageMask, PdfBoolean.TRUE);
        }
        if (imageMask != null) {
            if (imageMask.softMask) {
                stream.put(PdfName.SMask, imageMask.getPdfObject());
            } else if (imageMask.mask) {
                stream.put(PdfName.Mask, imageMask.getPdfObject());
            }
        }
        ImageData mask = image.getImageMask();
        if (mask != null) {
            if (mask.isSoftMask()) {
                stream.put(PdfName.SMask, new PdfImageXObject(image.getImageMask()).getPdfObject());
            } else if (mask.isMask()) {
                stream.put(PdfName.Mask, new PdfImageXObject(image.getImageMask()).getPdfObject());
            }
        }
        if (image.getDecode() != null) {
            stream.put(PdfName.Decode, new PdfArray(image.getDecode()));
        }
        if (image.isMask() && image.isInverted()) {
            stream.put(PdfName.Decode, new PdfArray(new float[]{1.0f, 0.0f}));
        }
        if (image.isInterpolation()) {
            stream.put(PdfName.Interpolate, PdfBoolean.TRUE);
        }
        int[] transparency = image.getTransparency();
        if (transparency != null && !image.isMask() && imageMask == null) {
            PdfArray t = new PdfArray();
            for (int transparencyItem : transparency) {
                t.add(new PdfNumber(transparencyItem));
            }
            stream.put(PdfName.Mask, t);
        }
        stream.put(PdfName.Width, new PdfNumber(image.getWidth()));
        stream.put(PdfName.Height, new PdfNumber(image.getHeight()));
        return stream;
    }

    private static PdfDictionary createDictionaryFromMap(PdfStream stream, Map<String, Object> parms) {
        if (parms != null) {
            PdfDictionary dictionary = new PdfDictionary();
            for (Map.Entry<String, Object> entry : parms.entrySet()) {
                Object value = entry.getValue();
                String key = entry.getKey();
                if (value instanceof Integer) {
                    dictionary.put(new PdfName(key), new PdfNumber(((Integer) value).intValue()));
                } else if (value instanceof Float) {
                    dictionary.put(new PdfName(key), new PdfNumber(((Float) value).floatValue()));
                } else if (value instanceof String) {
                    if (value.equals("Mask")) {
                        dictionary.put(PdfName.Mask, new PdfLiteral((String) value));
                    } else {
                        String str = (String) value;
                        if (str.indexOf(47) == 0) {
                            dictionary.put(new PdfName(key), new PdfName(str.substring(1)));
                        } else {
                            dictionary.put(new PdfName(key), new PdfString(str));
                        }
                    }
                } else if (value instanceof byte[]) {
                    PdfStream globalsStream = new PdfStream();
                    globalsStream.getOutputStream().writeBytes((byte[]) value);
                    dictionary.put(PdfName.JBIG2Globals, globalsStream);
                } else if (value instanceof Boolean) {
                    dictionary.put(new PdfName(key), PdfBoolean.valueOf(((Boolean) value).booleanValue()));
                } else if (value instanceof Object[]) {
                    dictionary.put(new PdfName(key), createArray(stream, (Object[]) value));
                } else if (value instanceof float[]) {
                    dictionary.put(new PdfName(key), new PdfArray((float[]) value));
                } else if (value instanceof int[]) {
                    dictionary.put(new PdfName(key), new PdfArray((int[]) value));
                }
            }
            return dictionary;
        }
        return null;
    }

    private static PdfArray createArray(PdfStream stream, Object[] objects) {
        PdfArray array = new PdfArray();
        for (Object obj : objects) {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.indexOf(47) == 0) {
                    array.add(new PdfName(str.substring(1)));
                } else {
                    array.add(new PdfString(str));
                }
            } else if (obj instanceof Integer) {
                array.add(new PdfNumber(((Integer) obj).intValue()));
            } else if (obj instanceof Float) {
                array.add(new PdfNumber(((Float) obj).floatValue()));
            } else if (obj instanceof Object[]) {
                array.add(createArray(stream, (Object[]) obj));
            } else {
                array.add(createDictionaryFromMap(stream, (Map) obj));
            }
        }
        return array;
    }

    private static ImageData checkImageType(ImageData image) {
        if (image instanceof WmfImageData) {
            throw new PdfException(PdfException.CannotCreatePdfImageXObjectByWmfImage);
        }
        return image;
    }
}
