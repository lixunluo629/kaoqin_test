package org.apache.poi.sl.usermodel;

import java.awt.Dimension;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/SlideShow.class */
public interface SlideShow<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends Closeable {
    Slide<S, P> createSlide() throws IOException;

    List<? extends Slide<S, P>> getSlides();

    MasterSheet<S, P> createMasterSheet() throws IOException;

    List<? extends MasterSheet<S, P>> getSlideMasters();

    Resources getResources();

    Dimension getPageSize();

    void setPageSize(Dimension dimension);

    List<? extends PictureData> getPictureData();

    PictureData addPicture(byte[] bArr, PictureData.PictureType pictureType) throws IOException;

    PictureData addPicture(InputStream inputStream, PictureData.PictureType pictureType) throws IOException;

    PictureData addPicture(File file, PictureData.PictureType pictureType) throws IOException;

    PictureData findPictureData(byte[] bArr);

    void write(OutputStream outputStream) throws IOException;
}
