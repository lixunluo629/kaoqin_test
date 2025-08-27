package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.UUID;
import org.apache.commons.io.TaggedIOException;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/TaggedReader.class */
public class TaggedReader extends ProxyReader {
    private final Serializable tag;

    public TaggedReader(Reader proxy) {
        super(proxy);
        this.tag = UUID.randomUUID();
    }

    @Override // org.apache.commons.io.input.ProxyReader
    protected void handleIOException(IOException e) throws IOException {
        throw new TaggedIOException(e, this.tag);
    }

    public boolean isCauseOf(Throwable exception) {
        return TaggedIOException.isTaggedWith(exception, this.tag);
    }

    public void throwIfCauseOf(Throwable throwable) throws IOException {
        TaggedIOException.throwCauseIfTaggedWith(throwable, this.tag);
    }
}
