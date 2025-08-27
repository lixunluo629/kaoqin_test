package net.sf.cglib.util;

import java.util.Comparator;
import net.sf.cglib.core.AbstractClassGenerator;
import net.sf.cglib.core.ClassesKey;
import net.sf.cglib.core.ReflectUtils;
import org.objectweb.asm.ClassVisitor;

/* loaded from: cglib-3.1.jar:net/sf/cglib/util/ParallelSorter.class */
public abstract class ParallelSorter extends SorterTemplate {
    protected Object[] a;
    private Comparer comparer;
    static Class class$net$sf$cglib$util$ParallelSorter;

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/ParallelSorter$Comparer.class */
    interface Comparer {
        int compare(int i, int i2);
    }

    public abstract ParallelSorter newInstance(Object[] objArr);

    protected ParallelSorter() {
    }

    public static ParallelSorter create(Object[] arrays) {
        Generator gen = new Generator();
        gen.setArrays(arrays);
        return gen.create();
    }

    private int len() {
        return ((Object[]) this.a[0]).length;
    }

    public void quickSort(int index) {
        quickSort(index, 0, len(), null);
    }

    public void quickSort(int index, int lo, int hi) {
        quickSort(index, lo, hi, null);
    }

    public void quickSort(int index, Comparator cmp) {
        quickSort(index, 0, len(), cmp);
    }

    public void quickSort(int index, int lo, int hi, Comparator cmp) {
        chooseComparer(index, cmp);
        super.quickSort(lo, hi - 1);
    }

    public void mergeSort(int index) {
        mergeSort(index, 0, len(), null);
    }

    public void mergeSort(int index, int lo, int hi) {
        mergeSort(index, lo, hi, null);
    }

    public void mergeSort(int index, Comparator cmp) {
        mergeSort(index, 0, len(), cmp);
    }

    public void mergeSort(int index, int lo, int hi, Comparator cmp) {
        chooseComparer(index, cmp);
        super.mergeSort(lo, hi - 1);
    }

    private void chooseComparer(int index, Comparator cmp) {
        Object array = this.a[index];
        Class type = array.getClass().getComponentType();
        if (type.equals(Integer.TYPE)) {
            this.comparer = new IntComparer((int[]) array);
            return;
        }
        if (type.equals(Long.TYPE)) {
            this.comparer = new LongComparer((long[]) array);
            return;
        }
        if (type.equals(Double.TYPE)) {
            this.comparer = new DoubleComparer((double[]) array);
            return;
        }
        if (type.equals(Float.TYPE)) {
            this.comparer = new FloatComparer((float[]) array);
            return;
        }
        if (type.equals(Short.TYPE)) {
            this.comparer = new ShortComparer((short[]) array);
            return;
        }
        if (type.equals(Byte.TYPE)) {
            this.comparer = new ByteComparer((byte[]) array);
        } else if (cmp != null) {
            this.comparer = new ComparatorComparer((Object[]) array, cmp);
        } else {
            this.comparer = new ObjectComparer((Object[]) array);
        }
    }

    @Override // net.sf.cglib.util.SorterTemplate
    protected int compare(int i, int j) {
        return this.comparer.compare(i, j);
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/ParallelSorter$ComparatorComparer.class */
    static class ComparatorComparer implements Comparer {
        private Object[] a;
        private Comparator cmp;

        public ComparatorComparer(Object[] a, Comparator cmp) {
            this.a = a;
            this.cmp = cmp;
        }

        @Override // net.sf.cglib.util.ParallelSorter.Comparer
        public int compare(int i, int j) {
            return this.cmp.compare(this.a[i], this.a[j]);
        }
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/ParallelSorter$ObjectComparer.class */
    static class ObjectComparer implements Comparer {
        private Object[] a;

        public ObjectComparer(Object[] a) {
            this.a = a;
        }

        @Override // net.sf.cglib.util.ParallelSorter.Comparer
        public int compare(int i, int j) {
            return ((Comparable) this.a[i]).compareTo(this.a[j]);
        }
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/ParallelSorter$IntComparer.class */
    static class IntComparer implements Comparer {
        private int[] a;

        public IntComparer(int[] a) {
            this.a = a;
        }

        @Override // net.sf.cglib.util.ParallelSorter.Comparer
        public int compare(int i, int j) {
            return this.a[i] - this.a[j];
        }
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/ParallelSorter$LongComparer.class */
    static class LongComparer implements Comparer {
        private long[] a;

        public LongComparer(long[] a) {
            this.a = a;
        }

        @Override // net.sf.cglib.util.ParallelSorter.Comparer
        public int compare(int i, int j) {
            long vi = this.a[i];
            long vj = this.a[j];
            if (vi == vj) {
                return 0;
            }
            return vi > vj ? 1 : -1;
        }
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/ParallelSorter$FloatComparer.class */
    static class FloatComparer implements Comparer {
        private float[] a;

        public FloatComparer(float[] a) {
            this.a = a;
        }

        @Override // net.sf.cglib.util.ParallelSorter.Comparer
        public int compare(int i, int j) {
            float vi = this.a[i];
            float vj = this.a[j];
            if (vi == vj) {
                return 0;
            }
            return vi > vj ? 1 : -1;
        }
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/ParallelSorter$DoubleComparer.class */
    static class DoubleComparer implements Comparer {
        private double[] a;

        public DoubleComparer(double[] a) {
            this.a = a;
        }

        @Override // net.sf.cglib.util.ParallelSorter.Comparer
        public int compare(int i, int j) {
            double vi = this.a[i];
            double vj = this.a[j];
            if (vi == vj) {
                return 0;
            }
            return vi > vj ? 1 : -1;
        }
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/ParallelSorter$ShortComparer.class */
    static class ShortComparer implements Comparer {
        private short[] a;

        public ShortComparer(short[] a) {
            this.a = a;
        }

        @Override // net.sf.cglib.util.ParallelSorter.Comparer
        public int compare(int i, int j) {
            return this.a[i] - this.a[j];
        }
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/ParallelSorter$ByteComparer.class */
    static class ByteComparer implements Comparer {
        private byte[] a;

        public ByteComparer(byte[] a) {
            this.a = a;
        }

        @Override // net.sf.cglib.util.ParallelSorter.Comparer
        public int compare(int i, int j) {
            return this.a[i] - this.a[j];
        }
    }

    /* loaded from: cglib-3.1.jar:net/sf/cglib/util/ParallelSorter$Generator.class */
    public static class Generator extends AbstractClassGenerator {
        private static final AbstractClassGenerator.Source SOURCE;
        private Object[] arrays;

        static {
            Class clsClass$;
            if (ParallelSorter.class$net$sf$cglib$util$ParallelSorter == null) {
                clsClass$ = ParallelSorter.class$("net.sf.cglib.util.ParallelSorter");
                ParallelSorter.class$net$sf$cglib$util$ParallelSorter = clsClass$;
            } else {
                clsClass$ = ParallelSorter.class$net$sf$cglib$util$ParallelSorter;
            }
            SOURCE = new AbstractClassGenerator.Source(clsClass$.getName());
        }

        public Generator() {
            super(SOURCE);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected ClassLoader getDefaultClassLoader() {
            return null;
        }

        public void setArrays(Object[] arrays) {
            this.arrays = arrays;
        }

        public ParallelSorter create() {
            return (ParallelSorter) super.create(ClassesKey.create(this.arrays));
        }

        @Override // net.sf.cglib.core.ClassGenerator
        public void generateClass(ClassVisitor v) throws Exception {
            if (this.arrays.length == 0) {
                throw new IllegalArgumentException("No arrays specified to sort");
            }
            for (int i = 0; i < this.arrays.length; i++) {
                if (!this.arrays[i].getClass().isArray()) {
                    throw new IllegalArgumentException(new StringBuffer().append(this.arrays[i].getClass()).append(" is not an array").toString());
                }
            }
            new ParallelSorterEmitter(v, getClassName(), this.arrays);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object firstInstance(Class type) {
            return ((ParallelSorter) ReflectUtils.newInstance(type)).newInstance(this.arrays);
        }

        @Override // net.sf.cglib.core.AbstractClassGenerator
        protected Object nextInstance(Object instance) {
            return ((ParallelSorter) instance).newInstance(this.arrays);
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}
