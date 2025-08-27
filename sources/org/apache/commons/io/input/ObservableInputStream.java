package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOIntConsumer;
import org.apache.commons.io.input.ProxyInputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ObservableInputStream.class */
public class ObservableInputStream extends ProxyInputStream {
    private final List<Observer> observers;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ObservableInputStream$AbstractBuilder.class */
    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> extends ProxyInputStream.AbstractBuilder<ObservableInputStream, T> {
        private List<Observer> observers;

        @Override // org.apache.commons.io.input.ProxyInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ AbstractStreamBuilder setAfterRead(IOIntConsumer iOIntConsumer) {
            return super.setAfterRead(iOIntConsumer);
        }

        @Override // org.apache.commons.io.input.ProxyInputStream.AbstractBuilder
        public /* bridge */ /* synthetic */ IOIntConsumer getAfterRead() {
            return super.getAfterRead();
        }

        public void setObservers(List<Observer> observers) {
            this.observers = observers;
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ObservableInputStream$Builder.class */
    public static class Builder extends AbstractBuilder<Builder> {
        @Override // org.apache.commons.io.function.IOSupplier
        public ObservableInputStream get() throws IOException {
            return new ObservableInputStream(this);
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/ObservableInputStream$Observer.class */
    public static abstract class Observer {
        public void closed() throws IOException {
        }

        public void data(byte[] buffer, int offset, int length) throws IOException {
        }

        public void data(int value) throws IOException {
        }

        public void error(IOException exception) throws IOException {
            throw exception;
        }

        public void finished() throws IOException {
        }
    }

    ObservableInputStream(AbstractBuilder builder) throws IOException {
        super(builder);
        this.observers = builder.observers;
    }

    public ObservableInputStream(InputStream inputStream) {
        this(inputStream, new ArrayList());
    }

    private ObservableInputStream(InputStream inputStream, List<Observer> observers) {
        super(inputStream);
        this.observers = observers;
    }

    public ObservableInputStream(InputStream inputStream, Observer... observers) {
        this(inputStream, (List<Observer>) Arrays.asList(observers));
    }

    public void add(Observer observer) {
        this.observers.add(observer);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        IOException ioe = null;
        try {
            super.close();
        } catch (IOException e) {
            ioe = e;
        }
        if (ioe == null) {
            noteClosed();
        } else {
            noteError(ioe);
        }
    }

    public void consume() throws IOException {
        IOUtils.consume(this);
    }

    private void forEachObserver(IOConsumer<Observer> action) throws IOException {
        IOConsumer.forAll(action, this.observers);
    }

    public List<Observer> getObservers() {
        return new ArrayList(this.observers);
    }

    protected void noteClosed() throws IOException {
        forEachObserver((v0) -> {
            v0.closed();
        });
    }

    protected void noteDataByte(int value) throws IOException {
        forEachObserver(observer -> {
            observer.data(value);
        });
    }

    protected void noteDataBytes(byte[] buffer, int offset, int length) throws IOException {
        forEachObserver(observer -> {
            observer.data(buffer, offset, length);
        });
    }

    protected void noteError(IOException exception) throws IOException {
        forEachObserver(observer -> {
            observer.error(exception);
        });
    }

    protected void noteFinished() throws IOException {
        forEachObserver((v0) -> {
            v0.finished();
        });
    }

    private void notify(byte[] buffer, int offset, int result, IOException ioe) throws IOException {
        if (ioe != null) {
            noteError(ioe);
            throw ioe;
        }
        if (result == -1) {
            noteFinished();
        } else if (result > 0) {
            noteDataBytes(buffer, offset, result);
        }
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int result = 0;
        IOException ioe = null;
        try {
            result = super.read();
        } catch (IOException ex) {
            ioe = ex;
        }
        if (ioe != null) {
            noteError(ioe);
            throw ioe;
        }
        if (result == -1) {
            noteFinished();
        } else {
            noteDataByte(result);
        }
        return result;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        int result = 0;
        IOException ioe = null;
        try {
            result = super.read(buffer);
        } catch (IOException ex) {
            ioe = ex;
        }
        notify(buffer, 0, result, ioe);
        return result;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer, int offset, int length) throws IOException {
        int result = 0;
        IOException ioe = null;
        try {
            result = super.read(buffer, offset, length);
        } catch (IOException ex) {
            ioe = ex;
        }
        notify(buffer, offset, result, ioe);
        return result;
    }

    public void remove(Observer observer) {
        this.observers.remove(observer);
    }

    public void removeAllObservers() {
        this.observers.clear();
    }
}
