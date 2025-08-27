package com.graphbuilder.struc;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/struc/Bag.class */
public class Bag {
    protected Object[] data;
    protected int size;

    public Bag() {
        this.data = null;
        this.size = 0;
        this.data = new Object[2];
    }

    public Bag(int initialCapacity) {
        this.data = null;
        this.size = 0;
        this.data = new Object[initialCapacity];
    }

    public Bag(Object[] data, int size) {
        this.data = null;
        this.size = 0;
        if (data == null) {
            throw new IllegalArgumentException("data array cannot be null.");
        }
        if (size < 0 || size > data.length) {
            throw new IllegalArgumentException("required: (size >= 0 && size <= data.length) but: (size = " + size + ", data.length = " + data.length + ")");
        }
        this.data = data;
        this.size = size;
    }

    public void add(Object o) {
        insert(o, this.size);
    }

    public int size() {
        return this.size;
    }

    public void setSize(int s) {
        if (s < 0 || s > this.data.length) {
            throw new IllegalArgumentException("required: (size >= 0 && size <= data.length) but: (size = " + this.size + ", data.length = " + this.data.length + ")");
        }
        this.size = s;
    }

    public void insert(Object o, int index) {
        if (index < 0 || index > this.size) {
            throw new IllegalArgumentException("required: (index >= 0 && index <= size) but: (index = " + index + ", size = " + this.size + ")");
        }
        ensureCapacity(this.size + 1);
        for (int i = this.size; i > index; i--) {
            this.data[i] = this.data[i - 1];
        }
        this.data[index] = o;
        this.size++;
    }

    public void ensureCapacity(int capacity) {
        if (capacity > this.data.length) {
            int x = 2 * this.data.length;
            if (x < capacity) {
                x = capacity;
            }
            Object[] arr = new Object[x];
            for (int i = 0; i < this.size; i++) {
                arr[i] = this.data[i];
            }
            this.data = arr;
        }
    }

    public int getCapacity() {
        return this.data.length;
    }

    private int find(Object o, int i, boolean forward) {
        if (i < 0 || i >= this.size) {
            return -1;
        }
        if (forward) {
            if (o == null) {
                while (i < this.size) {
                    if (this.data[i] != null) {
                        i++;
                    } else {
                        return i;
                    }
                }
                return -1;
            }
            while (i < this.size) {
                if (!o.equals(this.data[i])) {
                    i++;
                } else {
                    return i;
                }
            }
            return -1;
        }
        if (o == null) {
            while (i >= 0) {
                if (this.data[i] != null) {
                    i--;
                } else {
                    return i;
                }
            }
            return -1;
        }
        while (i >= 0) {
            if (!o.equals(this.data[i])) {
                i--;
            } else {
                return i;
            }
        }
        return -1;
    }

    public int remove(Object o) {
        int i = find(o, 0, true);
        if (i >= 0) {
            remove(i);
        }
        return i;
    }

    public Object remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IllegalArgumentException("required: (index >= 0 && index < size) but: (index = " + index + ", size = " + this.size + ")");
        }
        Object o = this.data[index];
        for (int i = index + 1; i < this.size; i++) {
            this.data[i - 1] = this.data[i];
        }
        Object[] objArr = this.data;
        int i2 = this.size - 1;
        this.size = i2;
        objArr[i2] = null;
        return o;
    }

    public Object get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IllegalArgumentException("required: (index >= 0 && index < size) but: (index = " + index + ", size = " + this.size + ")");
        }
        return this.data[index];
    }

    public Object set(Object o, int index) {
        if (index < 0 || index >= this.size) {
            throw new IllegalArgumentException("required: (index >= 0 && index < size) but: (index = " + index + ", size = " + this.size + ")");
        }
        Object old = this.data[index];
        this.data[index] = o;
        return old;
    }

    public boolean contains(Object o) {
        return find(o, 0, true) >= 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void trimArray() {
        if (this.size < this.data.length) {
            Object[] arr = new Object[this.size];
            for (int i = 0; i < this.size; i++) {
                arr[i] = this.data[i];
            }
            this.data = arr;
        }
    }

    public int indexOf(Object o) {
        return find(o, 0, true);
    }

    public int indexOf(Object o, int startIndex) {
        return find(o, startIndex, true);
    }

    public int lastIndexOf(Object o) {
        return find(o, this.size - 1, false);
    }

    public int lastIndexOf(Object o, int startIndex) {
        return find(o, startIndex, false);
    }
}
