package redis.clients.jedis;

import java.util.Arrays;
import redis.clients.util.ByteArrayComparator;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/Tuple.class */
public class Tuple implements Comparable<Tuple> {
    private byte[] element;
    private Double score;

    public Tuple(String element, Double score) {
        this(SafeEncoder.encode(element), score);
    }

    public Tuple(byte[] element, Double score) {
        this.element = element;
        this.score = score;
    }

    public int hashCode() {
        int result = 31 * 1;
        if (null != this.element) {
            for (byte b : this.element) {
                result = (31 * result) + b;
            }
        }
        long temp = Double.doubleToLongBits(this.score.doubleValue());
        return (31 * result) + ((int) (temp ^ (temp >>> 32)));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tuple other = (Tuple) obj;
        if (Arrays.equals(this.element, other.element)) {
            return this.score.equals(other.score);
        }
        return false;
    }

    @Override // java.lang.Comparable
    public int compareTo(Tuple other) {
        return compare(this, other);
    }

    public static int compare(Tuple t1, Tuple t2) {
        int compScore = Double.compare(t1.score.doubleValue(), t2.score.doubleValue());
        return compScore != 0 ? compScore : ByteArrayComparator.compare(t1.element, t2.element);
    }

    public String getElement() {
        if (null != this.element) {
            return SafeEncoder.encode(this.element);
        }
        return null;
    }

    public byte[] getBinaryElement() {
        return this.element;
    }

    public double getScore() {
        return this.score.doubleValue();
    }

    public String toString() {
        return '[' + SafeEncoder.encode(this.element) + ',' + this.score + ']';
    }
}
