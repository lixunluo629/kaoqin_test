package net.dongliu.apk.parser.utils;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/utils/Pair.class */
public class Pair<K, V> {
    private K left;
    private V right;

    public Pair() {
    }

    public Pair(K left, V right) {
        this.left = left;
        this.right = right;
    }

    public K getLeft() {
        return this.left;
    }

    public void setLeft(K left) {
        this.left = left;
    }

    public V getRight() {
        return this.right;
    }

    public void setRight(V right) {
        this.right = right;
    }
}
