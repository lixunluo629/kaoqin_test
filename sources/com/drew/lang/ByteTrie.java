package com.drew.lang;

import com.drew.lang.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/ByteTrie.class */
public class ByteTrie<T> {
    private final ByteTrieNode<T> _root = new ByteTrieNode<>();
    private int _maxDepth;

    /* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/ByteTrie$ByteTrieNode.class */
    static class ByteTrieNode<T> {
        private final Map<Byte, ByteTrieNode<T>> _children = new HashMap();
        private T _value = null;

        ByteTrieNode() {
        }

        public void setValue(T value) {
            if (this._value != null) {
                throw new RuntimeException("Value already set for this trie node");
            }
            this._value = value;
        }
    }

    @Nullable
    public T find(byte[] bArr) {
        ByteTrieNode<T> byteTrieNode = this._root;
        Object obj = ((ByteTrieNode) byteTrieNode)._value;
        for (byte b : bArr) {
            ByteTrieNode<T> byteTrieNode2 = (ByteTrieNode) ((ByteTrieNode) byteTrieNode)._children.get(Byte.valueOf(b));
            if (byteTrieNode2 == null) {
                break;
            }
            byteTrieNode = byteTrieNode2;
            if (((ByteTrieNode) byteTrieNode)._value != null) {
                obj = ((ByteTrieNode) byteTrieNode)._value;
            }
        }
        return (T) obj;
    }

    public void addPath(T value, byte[]... parts) {
        int depth = 0;
        ByteTrieNode<T> node = this._root;
        for (byte[] part : parts) {
            for (byte b : part) {
                ByteTrieNode<T> child = (ByteTrieNode) ((ByteTrieNode) node)._children.get(Byte.valueOf(b));
                if (child == null) {
                    child = new ByteTrieNode<>();
                    ((ByteTrieNode) node)._children.put(Byte.valueOf(b), child);
                }
                node = child;
                depth++;
            }
        }
        node.setValue(value);
        this._maxDepth = Math.max(this._maxDepth, depth);
    }

    public void setDefaultValue(T defaultValue) {
        this._root.setValue(defaultValue);
    }

    public int getMaxDepth() {
        return this._maxDepth;
    }
}
