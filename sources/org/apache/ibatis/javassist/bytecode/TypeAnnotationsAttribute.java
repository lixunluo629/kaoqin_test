package org.apache.ibatis.javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.annotation.TypeAnnotationsWriter;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/TypeAnnotationsAttribute.class */
public class TypeAnnotationsAttribute extends AttributeInfo {
    public static final String visibleTag = "RuntimeVisibleTypeAnnotations";
    public static final String invisibleTag = "RuntimeInvisibleTypeAnnotations";

    public TypeAnnotationsAttribute(ConstPool cp, String attrname, byte[] info) {
        super(cp, attrname, info);
    }

    TypeAnnotationsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    public int numAnnotations() {
        return ByteArray.readU16bit(this.info, 0);
    }

    @Override // org.apache.ibatis.javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map classnames) {
        Copier copier = new Copier(this.info, this.constPool, newCp, classnames);
        try {
            copier.annotationArray();
            return new TypeAnnotationsAttribute(newCp, getName(), copier.close());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.apache.ibatis.javassist.bytecode.AttributeInfo
    void renameClass(String oldname, String newname) {
        HashMap map = new HashMap();
        map.put(oldname, newname);
        renameClass(map);
    }

    @Override // org.apache.ibatis.javassist.bytecode.AttributeInfo
    void renameClass(Map classnames) {
        Renamer renamer = new Renamer(this.info, getConstPool(), classnames);
        try {
            renamer.annotationArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.apache.ibatis.javassist.bytecode.AttributeInfo
    void getRefClasses(Map classnames) {
        renameClass(classnames);
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/TypeAnnotationsAttribute$TAWalker.class */
    static class TAWalker extends AnnotationsAttribute.Walker {
        SubWalker subWalker;

        TAWalker(byte[] attrInfo) {
            super(attrInfo);
            this.subWalker = new SubWalker(attrInfo);
        }

        @Override // org.apache.ibatis.javassist.bytecode.AnnotationsAttribute.Walker
        int annotationArray(int pos, int num) throws Exception {
            for (int i = 0; i < num; i++) {
                int targetType = this.info[pos] & 255;
                pos = annotation(this.subWalker.typePath(this.subWalker.targetInfo(pos + 1, targetType)));
            }
            return pos;
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/TypeAnnotationsAttribute$SubWalker.class */
    static class SubWalker {
        byte[] info;

        SubWalker(byte[] attrInfo) {
            this.info = attrInfo;
        }

        final int targetInfo(int pos, int type) throws Exception {
            switch (type) {
                case 0:
                case 1:
                    int index = this.info[pos] & 255;
                    typeParameterTarget(pos, type, index);
                    return pos + 1;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                default:
                    throw new RuntimeException("invalid target type: " + type);
                case 16:
                    int index2 = ByteArray.readU16bit(this.info, pos);
                    supertypeTarget(pos, index2);
                    return pos + 2;
                case 17:
                case 18:
                    int param = this.info[pos] & 255;
                    int bound = this.info[pos + 1] & 255;
                    typeParameterBoundTarget(pos, type, param, bound);
                    return pos + 2;
                case 19:
                case 20:
                case 21:
                    emptyTarget(pos, type);
                    return pos;
                case 22:
                    int index3 = this.info[pos] & 255;
                    formalParameterTarget(pos, index3);
                    return pos + 1;
                case 23:
                    int index4 = ByteArray.readU16bit(this.info, pos);
                    throwsTarget(pos, index4);
                    return pos + 2;
                case 64:
                case 65:
                    int len = ByteArray.readU16bit(this.info, pos);
                    return localvarTarget(pos + 2, type, len);
                case 66:
                    int index5 = ByteArray.readU16bit(this.info, pos);
                    catchTarget(pos, index5);
                    return pos + 2;
                case 67:
                case 68:
                case 69:
                case 70:
                    int offset = ByteArray.readU16bit(this.info, pos);
                    offsetTarget(pos, type, offset);
                    return pos + 2;
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                    int offset2 = ByteArray.readU16bit(this.info, pos);
                    int index6 = this.info[pos + 2] & 255;
                    typeArgumentTarget(pos, type, offset2, index6);
                    return pos + 3;
            }
        }

        void typeParameterTarget(int pos, int targetType, int typeParameterIndex) throws Exception {
        }

        void supertypeTarget(int pos, int superTypeIndex) throws Exception {
        }

        void typeParameterBoundTarget(int pos, int targetType, int typeParameterIndex, int boundIndex) throws Exception {
        }

        void emptyTarget(int pos, int targetType) throws Exception {
        }

        void formalParameterTarget(int pos, int formalParameterIndex) throws Exception {
        }

        void throwsTarget(int pos, int throwsTypeIndex) throws Exception {
        }

        int localvarTarget(int pos, int targetType, int tableLength) throws Exception {
            for (int i = 0; i < tableLength; i++) {
                int start = ByteArray.readU16bit(this.info, pos);
                int length = ByteArray.readU16bit(this.info, pos + 2);
                int index = ByteArray.readU16bit(this.info, pos + 4);
                localvarTarget(pos, targetType, start, length, index);
                pos += 6;
            }
            return pos;
        }

        void localvarTarget(int pos, int targetType, int startPc, int length, int index) throws Exception {
        }

        void catchTarget(int pos, int exceptionTableIndex) throws Exception {
        }

        void offsetTarget(int pos, int targetType, int offset) throws Exception {
        }

        void typeArgumentTarget(int pos, int targetType, int offset, int typeArgumentIndex) throws Exception {
        }

        final int typePath(int pos) throws Exception {
            int len = this.info[pos] & 255;
            return typePath(pos + 1, len);
        }

        int typePath(int pos, int pathLength) throws Exception {
            for (int i = 0; i < pathLength; i++) {
                int kind = this.info[pos] & 255;
                int index = this.info[pos + 1] & 255;
                typePath(pos, kind, index);
                pos += 2;
            }
            return pos;
        }

        void typePath(int pos, int typePathKind, int typeArgumentIndex) throws Exception {
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/TypeAnnotationsAttribute$Renamer.class */
    static class Renamer extends AnnotationsAttribute.Renamer {
        SubWalker sub;

        Renamer(byte[] attrInfo, ConstPool cp, Map map) {
            super(attrInfo, cp, map);
            this.sub = new SubWalker(attrInfo);
        }

        @Override // org.apache.ibatis.javassist.bytecode.AnnotationsAttribute.Walker
        int annotationArray(int pos, int num) throws Exception {
            for (int i = 0; i < num; i++) {
                int targetType = this.info[pos] & 255;
                pos = annotation(this.sub.typePath(this.sub.targetInfo(pos + 1, targetType)));
            }
            return pos;
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/TypeAnnotationsAttribute$Copier.class */
    static class Copier extends AnnotationsAttribute.Copier {
        SubCopier sub;

        Copier(byte[] attrInfo, ConstPool src, ConstPool dest, Map map) {
            super(attrInfo, src, dest, map, false);
            TypeAnnotationsWriter w = new TypeAnnotationsWriter(this.output, dest);
            this.writer = w;
            this.sub = new SubCopier(attrInfo, src, dest, map, w);
        }

        @Override // org.apache.ibatis.javassist.bytecode.AnnotationsAttribute.Copier, org.apache.ibatis.javassist.bytecode.AnnotationsAttribute.Walker
        int annotationArray(int pos, int num) throws Exception {
            this.writer.numAnnotations(num);
            for (int i = 0; i < num; i++) {
                int targetType = this.info[pos] & 255;
                pos = annotation(this.sub.typePath(this.sub.targetInfo(pos + 1, targetType)));
            }
            return pos;
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/TypeAnnotationsAttribute$SubCopier.class */
    static class SubCopier extends SubWalker {
        ConstPool srcPool;
        ConstPool destPool;
        Map classnames;
        TypeAnnotationsWriter writer;

        SubCopier(byte[] attrInfo, ConstPool src, ConstPool dest, Map map, TypeAnnotationsWriter w) {
            super(attrInfo);
            this.srcPool = src;
            this.destPool = dest;
            this.classnames = map;
            this.writer = w;
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        void typeParameterTarget(int pos, int targetType, int typeParameterIndex) throws Exception {
            this.writer.typeParameterTarget(targetType, typeParameterIndex);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        void supertypeTarget(int pos, int superTypeIndex) throws Exception {
            this.writer.supertypeTarget(superTypeIndex);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        void typeParameterBoundTarget(int pos, int targetType, int typeParameterIndex, int boundIndex) throws Exception {
            this.writer.typeParameterBoundTarget(targetType, typeParameterIndex, boundIndex);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        void emptyTarget(int pos, int targetType) throws Exception {
            this.writer.emptyTarget(targetType);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        void formalParameterTarget(int pos, int formalParameterIndex) throws Exception {
            this.writer.formalParameterTarget(formalParameterIndex);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        void throwsTarget(int pos, int throwsTypeIndex) throws Exception {
            this.writer.throwsTarget(throwsTypeIndex);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        int localvarTarget(int pos, int targetType, int tableLength) throws Exception {
            this.writer.localVarTarget(targetType, tableLength);
            return super.localvarTarget(pos, targetType, tableLength);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        void localvarTarget(int pos, int targetType, int startPc, int length, int index) throws Exception {
            this.writer.localVarTargetTable(startPc, length, index);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        void catchTarget(int pos, int exceptionTableIndex) throws Exception {
            this.writer.catchTarget(exceptionTableIndex);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        void offsetTarget(int pos, int targetType, int offset) throws Exception {
            this.writer.offsetTarget(targetType, offset);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        void typeArgumentTarget(int pos, int targetType, int offset, int typeArgumentIndex) throws Exception {
            this.writer.typeArgumentTarget(targetType, offset, typeArgumentIndex);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        int typePath(int pos, int pathLength) throws Exception {
            this.writer.typePath(pathLength);
            return super.typePath(pos, pathLength);
        }

        @Override // org.apache.ibatis.javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        void typePath(int pos, int typePathKind, int typeArgumentIndex) throws Exception {
            this.writer.typePathPath(typePathKind, typeArgumentIndex);
        }
    }
}
