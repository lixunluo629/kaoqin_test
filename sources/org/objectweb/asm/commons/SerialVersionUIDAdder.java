package org.objectweb.asm.commons;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import org.aspectj.apache.bcel.Constants;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/commons/SerialVersionUIDAdder.SCL.lombok */
public class SerialVersionUIDAdder extends ClassVisitor {
    private static final String CLINIT = "<clinit>";
    private boolean computeSVUID;
    private boolean hasSVUID;
    private int access;
    private String name;
    private String[] interfaces;
    private Collection<Item> svuidFields;
    private boolean hasStaticInitializer;
    private Collection<Item> svuidConstructors;
    private Collection<Item> svuidMethods;

    public SerialVersionUIDAdder(ClassVisitor classVisitor) {
        this(393216, classVisitor);
        if (getClass() != SerialVersionUIDAdder.class) {
            throw new IllegalStateException();
        }
    }

    protected SerialVersionUIDAdder(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.computeSVUID = (access & 16384) == 0;
        if (this.computeSVUID) {
            this.name = name;
            this.access = access;
            this.interfaces = new String[interfaces.length];
            this.svuidFields = new ArrayList();
            this.svuidConstructors = new ArrayList();
            this.svuidMethods = new ArrayList();
            System.arraycopy(interfaces, 0, this.interfaces, 0, interfaces.length);
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (this.computeSVUID) {
            if ("<clinit>".equals(name)) {
                this.hasStaticInitializer = true;
            }
            int mods = access & 3391;
            if ((access & 2) == 0) {
                if ("<init>".equals(name)) {
                    this.svuidConstructors.add(new Item(name, mods, descriptor));
                } else if (!"<clinit>".equals(name)) {
                    this.svuidMethods.add(new Item(name, mods, descriptor));
                }
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (this.computeSVUID) {
            if ("serialVersionUID".equals(name)) {
                this.computeSVUID = false;
                this.hasSVUID = true;
            }
            if ((access & 2) == 0 || (access & 136) == 0) {
                int mods = access & Constants.MULTIANEWARRAY_QUICK;
                this.svuidFields.add(new Item(name, mods, desc));
            }
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String innerClassName, String outerName, String innerName, int innerClassAccess) {
        if (this.name != null && this.name.equals(innerClassName)) {
            this.access = innerClassAccess;
        }
        super.visitInnerClass(innerClassName, outerName, innerName, innerClassAccess);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitEnd() {
        if (this.computeSVUID && !this.hasSVUID) {
            try {
                addSVUID(computeSVUID());
            } catch (IOException e) {
                throw new IllegalStateException("Error while computing SVUID for " + this.name, e);
            }
        }
        super.visitEnd();
    }

    public boolean hasSVUID() {
        return this.hasSVUID;
    }

    protected void addSVUID(long svuid) {
        FieldVisitor fieldVisitor = super.visitField(24, "serialVersionUID", "J", null, Long.valueOf(svuid));
        if (fieldVisitor != null) {
            fieldVisitor.visitEnd();
        }
    }

    protected long computeSVUID() throws IOException {
        DataOutputStream dataOutputStream = null;
        long svuid = 0;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF(this.name.replace('/', '.'));
            int mods = this.access;
            if ((mods & 512) != 0) {
                mods = this.svuidMethods.isEmpty() ? mods & (-1025) : mods | 1024;
            }
            dataOutputStream.writeInt(mods & 1553);
            Arrays.sort(this.interfaces);
            for (String interfaceName : this.interfaces) {
                dataOutputStream.writeUTF(interfaceName.replace('/', '.'));
            }
            writeItems(this.svuidFields, dataOutputStream, false);
            if (this.hasStaticInitializer) {
                dataOutputStream.writeUTF("<clinit>");
                dataOutputStream.writeInt(8);
                dataOutputStream.writeUTF("()V");
            }
            writeItems(this.svuidConstructors, dataOutputStream, true);
            writeItems(this.svuidMethods, dataOutputStream, true);
            dataOutputStream.flush();
            byte[] hashBytes = computeSHAdigest(byteArrayOutputStream.toByteArray());
            for (int i = Math.min(hashBytes.length, 8) - 1; i >= 0; i--) {
                svuid = (svuid << 8) | (hashBytes[i] & 255);
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            return svuid;
        } catch (Throwable th) {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            throw th;
        }
    }

    protected byte[] computeSHAdigest(byte[] value) {
        try {
            return MessageDigest.getInstance("SHA").digest(value);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.toString());
        }
    }

    private static void writeItems(Collection<Item> itemCollection, DataOutput dataOutputStream, boolean dotted) throws IOException {
        int size = itemCollection.size();
        Item[] items = (Item[]) itemCollection.toArray(new Item[size]);
        Arrays.sort(items, new Comparator<Item>() { // from class: org.objectweb.asm.commons.SerialVersionUIDAdder.1
            @Override // java.util.Comparator
            public int compare(Item item1, Item item2) {
                int result = item1.name.compareTo(item2.name);
                if (result == 0) {
                    result = item1.descriptor.compareTo(item2.descriptor);
                }
                return result;
            }
        });
        for (Item item : items) {
            dataOutputStream.writeUTF(item.name);
            dataOutputStream.writeInt(item.access);
            dataOutputStream.writeUTF(dotted ? item.descriptor.replace('/', '.') : item.descriptor);
        }
    }

    /* loaded from: lombok-1.16.22.jar:org/objectweb/asm/commons/SerialVersionUIDAdder$Item.SCL.lombok */
    private static final class Item {
        final String name;
        final int access;
        final String descriptor;

        Item(String name, int access, String descriptor) {
            this.name = name;
            this.access = access;
            this.descriptor = descriptor;
        }
    }
}
