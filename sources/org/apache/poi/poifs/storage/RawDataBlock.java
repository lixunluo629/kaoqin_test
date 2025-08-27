package org.apache.poi.poifs.storage;

import com.alibaba.excel.constant.ExcelXmlConstants;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/storage/RawDataBlock.class */
public class RawDataBlock implements ListManagedBlock {
    private byte[] _data;
    private boolean _eof;
    private boolean _hasData;
    static POILogger log = POILogFactory.getLogger((Class<?>) RawDataBlock.class);

    public RawDataBlock(InputStream stream) throws IOException {
        this(stream, 512);
    }

    public RawDataBlock(InputStream stream, int blockSize) throws IOException {
        this._data = new byte[blockSize];
        int count = IOUtils.readFully(stream, this._data);
        this._hasData = count > 0;
        if (count == -1) {
            this._eof = true;
        } else {
            if (count != blockSize) {
                this._eof = true;
                String type = " byte" + (count == 1 ? "" : ExcelXmlConstants.CELL_DATA_FORMAT_TAG);
                log.log(7, "Unable to read entire block; " + count + type + " read before EOF; expected " + blockSize + " bytes. Your document was either written by software that ignores the spec, or has been truncated!");
                return;
            }
            this._eof = false;
        }
    }

    public boolean eof() {
        return this._eof;
    }

    public boolean hasData() {
        return this._hasData;
    }

    public String toString() {
        return "RawDataBlock of size " + this._data.length;
    }

    @Override // org.apache.poi.poifs.storage.ListManagedBlock
    public byte[] getData() throws IOException {
        if (!hasData()) {
            throw new IOException("Cannot return empty data");
        }
        return this._data;
    }

    public int getBigBlockSize() {
        return this._data.length;
    }
}
