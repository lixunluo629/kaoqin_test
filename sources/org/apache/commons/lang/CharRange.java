package org.apache.commons.lang;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.lang.text.StrBuilder;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/CharRange.class */
public final class CharRange implements Serializable {
    private static final long serialVersionUID = 8270183163158333422L;
    private final char start;
    private final char end;
    private final boolean negated;
    private transient String iToString;

    /* renamed from: org.apache.commons.lang.CharRange$1, reason: invalid class name */
    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/CharRange$1.class */
    static class AnonymousClass1 {
    }

    public static CharRange is(char ch2) {
        return new CharRange(ch2, ch2, false);
    }

    public static CharRange isNot(char ch2) {
        return new CharRange(ch2, ch2, true);
    }

    public static CharRange isIn(char start, char end) {
        return new CharRange(start, end, false);
    }

    public static CharRange isNotIn(char start, char end) {
        return new CharRange(start, end, true);
    }

    public CharRange(char ch2) {
        this(ch2, ch2, false);
    }

    public CharRange(char ch2, boolean negated) {
        this(ch2, ch2, negated);
    }

    public CharRange(char start, char end) {
        this(start, end, false);
    }

    public CharRange(char start, char end, boolean negated) {
        if (start > end) {
            start = end;
            end = start;
        }
        this.start = start;
        this.end = end;
        this.negated = negated;
    }

    public char getStart() {
        return this.start;
    }

    public char getEnd() {
        return this.end;
    }

    public boolean isNegated() {
        return this.negated;
    }

    public boolean contains(char ch2) {
        return (ch2 >= this.start && ch2 <= this.end) != this.negated;
    }

    public boolean contains(CharRange range) {
        if (range == null) {
            throw new IllegalArgumentException("The Range must not be null");
        }
        return this.negated ? range.negated ? this.start >= range.start && this.end <= range.end : range.end < this.start || range.start > this.end : range.negated ? this.start == 0 && this.end == 65535 : this.start <= range.start && this.end >= range.end;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CharRange)) {
            return false;
        }
        CharRange other = (CharRange) obj;
        return this.start == other.start && this.end == other.end && this.negated == other.negated;
    }

    public int hashCode() {
        return 'S' + this.start + (7 * this.end) + (this.negated ? 1 : 0);
    }

    public String toString() {
        if (this.iToString == null) {
            StrBuilder buf = new StrBuilder(4);
            if (isNegated()) {
                buf.append('^');
            }
            buf.append(this.start);
            if (this.start != this.end) {
                buf.append('-');
                buf.append(this.end);
            }
            this.iToString = buf.toString();
        }
        return this.iToString;
    }

    public Iterator iterator() {
        return new CharacterIterator(this, null);
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/CharRange$CharacterIterator.class */
    private static class CharacterIterator implements Iterator {
        private char current;
        private final CharRange range;
        private boolean hasNext;

        CharacterIterator(CharRange x0, AnonymousClass1 x1) {
            this(x0);
        }

        private CharacterIterator(CharRange r) {
            this.range = r;
            this.hasNext = true;
            if (this.range.negated) {
                if (this.range.start == 0) {
                    if (this.range.end == 65535) {
                        this.hasNext = false;
                        return;
                    } else {
                        this.current = (char) (this.range.end + 1);
                        return;
                    }
                }
                this.current = (char) 0;
                return;
            }
            this.current = this.range.start;
        }

        private void prepareNext() {
            if (!this.range.negated) {
                if (this.current < this.range.end) {
                    this.current = (char) (this.current + 1);
                    return;
                } else {
                    this.hasNext = false;
                    return;
                }
            }
            if (this.current != 65535) {
                if (this.current + 1 == this.range.start) {
                    if (this.range.end == 65535) {
                        this.hasNext = false;
                        return;
                    } else {
                        this.current = (char) (this.range.end + 1);
                        return;
                    }
                }
                this.current = (char) (this.current + 1);
                return;
            }
            this.hasNext = false;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.hasNext;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            char cur = this.current;
            prepareNext();
            return new Character(cur);
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
