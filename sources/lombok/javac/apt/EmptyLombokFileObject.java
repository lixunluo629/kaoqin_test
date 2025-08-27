package lombok.javac.apt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

/* loaded from: lombok-1.16.22.jar:lombok/javac/apt/EmptyLombokFileObject.SCL.lombok */
class EmptyLombokFileObject implements LombokFileObject {
    private final String name;
    private final JavaFileObject.Kind kind;

    public EmptyLombokFileObject(String name, JavaFileObject.Kind kind) {
        this.name = name;
        this.kind = kind;
    }

    public boolean isNameCompatible(String simpleName, JavaFileObject.Kind kind) {
        String baseName = simpleName + kind.extension;
        return kind.equals(getKind()) && (baseName.equals(toUri().getPath()) || toUri().getPath().endsWith(new StringBuilder().append("/").append(baseName).toString()));
    }

    public URI toUri() {
        return URI.create("file:///" + (this.name.startsWith("/") ? this.name.substring(1) : this.name));
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return "";
    }

    public InputStream openInputStream() throws IOException {
        return new ByteArrayInputStream(new byte[0]);
    }

    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
        return new StringReader("");
    }

    public Writer openWriter() throws IOException {
        return new OutputStreamWriter(openOutputStream());
    }

    public OutputStream openOutputStream() throws IOException {
        return new ByteArrayOutputStream();
    }

    public long getLastModified() {
        return 0L;
    }

    public boolean delete() {
        return false;
    }

    public JavaFileObject.Kind getKind() {
        return this.kind;
    }

    public String getName() {
        return toUri().getPath();
    }

    public NestingKind getNestingKind() {
        return null;
    }

    public Modifier getAccessLevel() {
        return null;
    }

    @Override // lombok.javac.apt.LombokFileObject
    public CharsetDecoder getDecoder(boolean ignoreEncodingErrors) {
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        CodingErrorAction action = ignoreEncodingErrors ? CodingErrorAction.REPLACE : CodingErrorAction.REPORT;
        return decoder.onMalformedInput(action).onUnmappableCharacter(action);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof EmptyLombokFileObject)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        EmptyLombokFileObject other = (EmptyLombokFileObject) obj;
        return this.name.equals(other.name) && this.kind.equals(other.kind);
    }

    public int hashCode() {
        return this.name.hashCode() ^ this.kind.hashCode();
    }
}
