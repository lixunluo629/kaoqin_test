package io.netty.handler.codec.compression;

import org.objectweb.asm.Opcodes;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Bzip2DivSufSort.class */
final class Bzip2DivSufSort {
    private static final int STACK_SIZE = 64;
    private static final int BUCKET_A_SIZE = 256;
    private static final int BUCKET_B_SIZE = 65536;
    private static final int SS_BLOCKSIZE = 1024;
    private static final int INSERTIONSORT_THRESHOLD = 8;
    private static final int[] LOG_2_TABLE = {-1, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
    private final int[] SA;
    private final byte[] T;
    private final int n;

    Bzip2DivSufSort(byte[] block, int[] bwtBlock, int blockLength) {
        this.T = block;
        this.SA = bwtBlock;
        this.n = blockLength;
    }

    private static void swapElements(int[] array1, int idx1, int[] array2, int idx2) {
        int temp = array1[idx1];
        array1[idx1] = array2[idx2];
        array2[idx2] = temp;
    }

    private int ssCompare(int p1, int p2, int depth) {
        int[] SA = this.SA;
        byte[] T = this.T;
        int U1n = SA[p1 + 1] + 2;
        int U2n = SA[p2 + 1] + 2;
        int U1 = depth + SA[p1];
        int U2 = depth + SA[p2];
        while (U1 < U1n && U2 < U2n && T[U1] == T[U2]) {
            U1++;
            U2++;
        }
        if (U1 >= U1n) {
            return U2 < U2n ? -1 : 0;
        }
        if (U2 < U2n) {
            return (T[U1] & 255) - (T[U2] & 255);
        }
        return 1;
    }

    private int ssCompareLast(int pa, int p1, int p2, int depth, int size) {
        int[] SA = this.SA;
        byte[] T = this.T;
        int U1 = depth + SA[p1];
        int U2 = depth + SA[p2];
        int U2n = SA[p2 + 1] + 2;
        while (U1 < size && U2 < U2n && T[U1] == T[U2]) {
            U1++;
            U2++;
        }
        if (U1 < size) {
            if (U2 < U2n) {
                return (T[U1] & 255) - (T[U2] & 255);
            }
            return 1;
        }
        if (U2 == U2n) {
            return 1;
        }
        int U12 = U1 % size;
        int U1n = SA[pa] + 2;
        while (U12 < U1n && U2 < U2n && T[U12] == T[U2]) {
            U12++;
            U2++;
        }
        if (U12 >= U1n) {
            return U2 < U2n ? -1 : 0;
        }
        if (U2 < U2n) {
            return (T[U12] & 255) - (T[U2] & 255);
        }
        return 1;
    }

    private void ssInsertionSort(int pa, int first, int last, int depth) {
        int r;
        int[] SA = this.SA;
        for (int i = last - 2; first <= i; i--) {
            int t = SA[i];
            int j = i + 1;
            do {
                r = ssCompare(pa + t, pa + SA[j], depth);
                if (0 >= r) {
                    break;
                }
                do {
                    SA[j - 1] = SA[j];
                    j++;
                    if (j >= last) {
                        break;
                    }
                } while (SA[j] < 0);
            } while (last > j);
            if (r == 0) {
                SA[j] = SA[j] ^ (-1);
            }
            SA[j - 1] = t;
        }
    }

    private void ssFixdown(int td, int pa, int sa, int i, int size) {
        int[] SA = this.SA;
        byte[] T = this.T;
        int v = SA[sa + i];
        int c = T[td + SA[pa + v]] & 255;
        while (true) {
            int j = (2 * i) + 1;
            if (j >= size) {
                break;
            }
            int j2 = j + 1;
            int k = j;
            int d = T[td + SA[pa + SA[sa + j]]] & 255;
            int e = T[td + SA[pa + SA[sa + j2]]] & 255;
            if (d < e) {
                k = j2;
                d = e;
            }
            if (d <= c) {
                break;
            }
            SA[sa + i] = SA[sa + k];
            i = k;
        }
        SA[sa + i] = v;
    }

    private void ssHeapSort(int td, int pa, int sa, int size) {
        int[] SA = this.SA;
        byte[] T = this.T;
        int m = size;
        if (size % 2 == 0) {
            m--;
            if ((T[td + SA[pa + SA[sa + (m / 2)]]] & 255) < (T[td + SA[pa + SA[sa + m]]] & 255)) {
                swapElements(SA, sa + m, SA, sa + (m / 2));
            }
        }
        for (int i = (m / 2) - 1; 0 <= i; i--) {
            ssFixdown(td, pa, sa, i, m);
        }
        if (size % 2 == 0) {
            swapElements(SA, sa, SA, sa + m);
            ssFixdown(td, pa, sa, 0, m);
        }
        for (int i2 = m - 1; 0 < i2; i2--) {
            int t = SA[sa];
            SA[sa] = SA[sa + i2];
            ssFixdown(td, pa, sa, 0, i2);
            SA[sa + i2] = t;
        }
    }

    private int ssMedian3(int td, int pa, int v1, int v2, int v3) {
        int[] SA = this.SA;
        byte[] T = this.T;
        int T_v1 = T[td + SA[pa + SA[v1]]] & 255;
        int T_v2 = T[td + SA[pa + SA[v2]]] & 255;
        int T_v3 = T[td + SA[pa + SA[v3]]] & 255;
        if (T_v1 > T_v2) {
            v1 = v2;
            v2 = v1;
            T_v1 = T_v2;
            T_v2 = T_v1;
        }
        if (T_v2 > T_v3) {
            if (T_v1 > T_v3) {
                return v1;
            }
            return v3;
        }
        return v2;
    }

    private int ssMedian5(int td, int pa, int v1, int v2, int v3, int v4, int v5) {
        int[] SA = this.SA;
        byte[] T = this.T;
        int T_v1 = T[td + SA[pa + SA[v1]]] & 255;
        int T_v2 = T[td + SA[pa + SA[v2]]] & 255;
        int T_v3 = T[td + SA[pa + SA[v3]]] & 255;
        int T_v4 = T[td + SA[pa + SA[v4]]] & 255;
        int T_v5 = T[td + SA[pa + SA[v5]]] & 255;
        if (T_v2 > T_v3) {
            v2 = v3;
            v3 = v2;
            T_v2 = T_v3;
            T_v3 = T_v2;
        }
        if (T_v4 > T_v5) {
            v4 = v5;
            v5 = v4;
            T_v4 = T_v5;
            T_v5 = T_v4;
        }
        if (T_v2 > T_v4) {
            int temp = v2;
            v4 = temp;
            int T_vtemp = T_v2;
            T_v4 = T_vtemp;
            int temp2 = v3;
            v3 = v5;
            v5 = temp2;
            int T_vtemp2 = T_v3;
            T_v3 = T_v5;
            T_v5 = T_vtemp2;
        }
        if (T_v1 > T_v3) {
            v1 = v3;
            v3 = v1;
            T_v1 = T_v3;
            T_v3 = T_v1;
        }
        if (T_v1 > T_v4) {
            int temp3 = v1;
            v4 = temp3;
            int T_vtemp3 = T_v1;
            T_v4 = T_vtemp3;
            v3 = v5;
            T_v3 = T_v5;
        }
        if (T_v3 > T_v4) {
            return v4;
        }
        return v3;
    }

    private int ssPivot(int td, int pa, int first, int last) {
        int t = last - first;
        int middle = first + (t / 2);
        if (t <= 512) {
            if (t <= 32) {
                return ssMedian3(td, pa, first, middle, last - 1);
            }
            int t2 = t >> 2;
            return ssMedian5(td, pa, first, first + t2, middle, (last - 1) - t2, last - 1);
        }
        int t3 = t >> 3;
        return ssMedian3(td, pa, ssMedian3(td, pa, first, first + t3, first + (t3 << 1)), ssMedian3(td, pa, middle - t3, middle, middle + t3), ssMedian3(td, pa, (last - 1) - (t3 << 1), (last - 1) - t3, last - 1));
    }

    private static int ssLog(int n) {
        return (n & 65280) != 0 ? 8 + LOG_2_TABLE[(n >> 8) & 255] : LOG_2_TABLE[n & 255];
    }

    private int ssSubstringPartition(int pa, int first, int last, int depth) {
        int[] SA = this.SA;
        int a = first - 1;
        int b = last;
        while (true) {
            a++;
            if (a < b && SA[pa + SA[a]] + depth >= SA[pa + SA[a] + 1] + 1) {
                SA[a] = SA[a] ^ (-1);
            } else {
                do {
                    b--;
                    if (a >= b) {
                        break;
                    }
                } while (SA[pa + SA[b]] + depth < SA[pa + SA[b] + 1] + 1);
                if (b <= a) {
                    break;
                }
                int t = SA[b] ^ (-1);
                SA[b] = SA[a];
                SA[a] = t;
            }
        }
        if (first < a) {
            SA[first] = SA[first] ^ (-1);
        }
        return a;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Bzip2DivSufSort$StackEntry.class */
    private static class StackEntry {
        final int a;
        final int b;
        final int c;
        final int d;

        StackEntry(int a, int b, int c, int d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }
    }

    private void ssMultiKeyIntroSort(int pa, int first, int last, int depth) {
        int[] SA = this.SA;
        byte[] T = this.T;
        StackEntry[] stack = new StackEntry[64];
        int x = 0;
        int ssize = 0;
        int limit = ssLog(last - first);
        while (true) {
            if (last - first <= 8) {
                if (1 < last - first) {
                    ssInsertionSort(pa, first, last, depth);
                }
                if (ssize == 0) {
                    return;
                }
                ssize--;
                StackEntry entry = stack[ssize];
                first = entry.a;
                last = entry.b;
                depth = entry.c;
                limit = entry.d;
            } else {
                int Td = depth;
                int i = limit;
                limit--;
                if (i == 0) {
                    ssHeapSort(Td, pa, first, last - first);
                }
                if (limit < 0) {
                    int a = first + 1;
                    int v = T[Td + SA[pa + SA[first]]] & 255;
                    while (a < last) {
                        int i2 = T[Td + SA[pa + SA[a]]] & 255;
                        x = i2;
                        if (i2 != v) {
                            if (1 < a - first) {
                                break;
                            }
                            v = x;
                            first = a;
                        }
                        a++;
                    }
                    if ((T[(Td + SA[pa + SA[first]]) - 1] & 255) < v) {
                        first = ssSubstringPartition(pa, first, a, depth);
                    }
                    if (a - first <= last - a) {
                        if (1 < a - first) {
                            int i3 = ssize;
                            ssize++;
                            stack[i3] = new StackEntry(a, last, depth, -1);
                            last = a;
                            depth++;
                            limit = ssLog(a - first);
                        } else {
                            first = a;
                            limit = -1;
                        }
                    } else if (1 < last - a) {
                        int i4 = ssize;
                        ssize++;
                        stack[i4] = new StackEntry(first, a, depth + 1, ssLog(a - first));
                        first = a;
                        limit = -1;
                    } else {
                        last = a;
                        depth++;
                        limit = ssLog(a - first);
                    }
                } else {
                    int a2 = ssPivot(Td, pa, first, last);
                    int v2 = T[Td + SA[pa + SA[a2]]] & 255;
                    swapElements(SA, first, SA, a2);
                    int b = first + 1;
                    while (b < last) {
                        int i5 = T[Td + SA[pa + SA[b]]] & 255;
                        x = i5;
                        if (i5 != v2) {
                            break;
                        } else {
                            b++;
                        }
                    }
                    int i6 = b;
                    int a3 = i6;
                    if (i6 < last && x < v2) {
                        while (true) {
                            b++;
                            if (b >= last) {
                                break;
                            }
                            int i7 = T[Td + SA[pa + SA[b]]] & 255;
                            x = i7;
                            if (i7 > v2) {
                                break;
                            } else if (x == v2) {
                                swapElements(SA, b, SA, a3);
                                a3++;
                            }
                        }
                    }
                    int c = last - 1;
                    while (b < c) {
                        int i8 = T[Td + SA[pa + SA[c]]] & 255;
                        x = i8;
                        if (i8 != v2) {
                            break;
                        } else {
                            c--;
                        }
                    }
                    int i9 = c;
                    int d = i9;
                    if (b < i9 && x > v2) {
                        while (true) {
                            c--;
                            if (b >= c) {
                                break;
                            }
                            int i10 = T[Td + SA[pa + SA[c]]] & 255;
                            x = i10;
                            if (i10 < v2) {
                                break;
                            } else if (x == v2) {
                                swapElements(SA, c, SA, d);
                                d--;
                            }
                        }
                    }
                    while (b < c) {
                        swapElements(SA, b, SA, c);
                        while (true) {
                            b++;
                            if (b >= c) {
                                break;
                            }
                            int i11 = T[Td + SA[pa + SA[b]]] & 255;
                            x = i11;
                            if (i11 > v2) {
                                break;
                            } else if (x == v2) {
                                swapElements(SA, b, SA, a3);
                                a3++;
                            }
                        }
                        while (true) {
                            c--;
                            if (b < c) {
                                int i12 = T[Td + SA[pa + SA[c]]] & 255;
                                x = i12;
                                if (i12 >= v2) {
                                    if (x == v2) {
                                        swapElements(SA, c, SA, d);
                                        d--;
                                    }
                                }
                            }
                        }
                    }
                    if (a3 <= d) {
                        int c2 = b - 1;
                        int i13 = a3 - first;
                        int s = i13;
                        int t = b - a3;
                        if (i13 > t) {
                            s = t;
                        }
                        int e = first;
                        int f = b - s;
                        while (0 < s) {
                            swapElements(SA, e, SA, f);
                            s--;
                            e++;
                            f++;
                        }
                        int i14 = d - c2;
                        int s2 = i14;
                        int t2 = (last - d) - 1;
                        if (i14 > t2) {
                            s2 = t2;
                        }
                        int e2 = b;
                        int f2 = last - s2;
                        while (0 < s2) {
                            swapElements(SA, e2, SA, f2);
                            s2--;
                            e2++;
                            f2++;
                        }
                        int a4 = first + (b - a3);
                        int c3 = last - (d - c2);
                        int b2 = v2 <= (T[(Td + SA[pa + SA[a4]]) - 1] & 255) ? a4 : ssSubstringPartition(pa, a4, c3, depth);
                        if (a4 - first <= last - c3) {
                            if (last - c3 <= c3 - b2) {
                                int i15 = ssize;
                                int ssize2 = ssize + 1;
                                stack[i15] = new StackEntry(b2, c3, depth + 1, ssLog(c3 - b2));
                                ssize = ssize2 + 1;
                                stack[ssize2] = new StackEntry(c3, last, depth, limit);
                                last = a4;
                            } else if (a4 - first <= c3 - b2) {
                                int i16 = ssize;
                                int ssize3 = ssize + 1;
                                stack[i16] = new StackEntry(c3, last, depth, limit);
                                ssize = ssize3 + 1;
                                stack[ssize3] = new StackEntry(b2, c3, depth + 1, ssLog(c3 - b2));
                                last = a4;
                            } else {
                                int i17 = ssize;
                                int ssize4 = ssize + 1;
                                stack[i17] = new StackEntry(c3, last, depth, limit);
                                ssize = ssize4 + 1;
                                stack[ssize4] = new StackEntry(first, a4, depth, limit);
                                first = b2;
                                last = c3;
                                depth++;
                                limit = ssLog(c3 - b2);
                            }
                        } else if (a4 - first <= c3 - b2) {
                            int i18 = ssize;
                            int ssize5 = ssize + 1;
                            stack[i18] = new StackEntry(b2, c3, depth + 1, ssLog(c3 - b2));
                            ssize = ssize5 + 1;
                            stack[ssize5] = new StackEntry(first, a4, depth, limit);
                            first = c3;
                        } else if (last - c3 <= c3 - b2) {
                            int i19 = ssize;
                            int ssize6 = ssize + 1;
                            stack[i19] = new StackEntry(first, a4, depth, limit);
                            ssize = ssize6 + 1;
                            stack[ssize6] = new StackEntry(b2, c3, depth + 1, ssLog(c3 - b2));
                            first = c3;
                        } else {
                            int i20 = ssize;
                            int ssize7 = ssize + 1;
                            stack[i20] = new StackEntry(first, a4, depth, limit);
                            ssize = ssize7 + 1;
                            stack[ssize7] = new StackEntry(c3, last, depth, limit);
                            first = b2;
                            last = c3;
                            depth++;
                            limit = ssLog(c3 - b2);
                        }
                    } else {
                        limit++;
                        if ((T[(Td + SA[pa + SA[first]]) - 1] & 255) < v2) {
                            first = ssSubstringPartition(pa, first, last, depth);
                            limit = ssLog(last - first);
                        }
                        depth++;
                    }
                }
            }
        }
    }

    private static void ssBlockSwap(int[] array1, int first1, int[] array2, int first2, int size) {
        int i = size;
        int a = first1;
        int b = first2;
        while (0 < i) {
            swapElements(array1, a, array2, b);
            i--;
            a++;
            b++;
        }
    }

    private void ssMergeForward(int pa, int[] buf, int bufoffset, int first, int middle, int last, int depth) {
        int[] SA = this.SA;
        int bufend = (bufoffset + (middle - first)) - 1;
        ssBlockSwap(buf, bufoffset, SA, first, middle - first);
        int t = SA[first];
        int i = first;
        int j = bufoffset;
        int k = middle;
        while (true) {
            int r = ssCompare(pa + buf[j], pa + SA[k], depth);
            if (r < 0) {
                do {
                    int i2 = i;
                    i++;
                    SA[i2] = buf[j];
                    if (bufend <= j) {
                        buf[j] = t;
                        return;
                    } else {
                        int i3 = j;
                        j++;
                        buf[i3] = SA[i];
                    }
                } while (buf[j] < 0);
            } else if (r > 0) {
                do {
                    int i4 = i;
                    i++;
                    SA[i4] = SA[k];
                    int i5 = k;
                    k++;
                    SA[i5] = SA[i];
                    if (last <= k) {
                        while (j < bufend) {
                            int i6 = i;
                            i++;
                            SA[i6] = buf[j];
                            int i7 = j;
                            j++;
                            buf[i7] = SA[i];
                        }
                        SA[i] = buf[j];
                        buf[j] = t;
                        return;
                    }
                } while (SA[k] < 0);
            } else {
                SA[k] = SA[k] ^ (-1);
                do {
                    int i8 = i;
                    i++;
                    SA[i8] = buf[j];
                    if (bufend <= j) {
                        buf[j] = t;
                        return;
                    } else {
                        int i9 = j;
                        j++;
                        buf[i9] = SA[i];
                    }
                } while (buf[j] < 0);
                do {
                    int i10 = i;
                    i++;
                    SA[i10] = SA[k];
                    int i11 = k;
                    k++;
                    SA[i11] = SA[i];
                    if (last <= k) {
                        while (j < bufend) {
                            int i12 = i;
                            i++;
                            SA[i12] = buf[j];
                            int i13 = j;
                            j++;
                            buf[i13] = SA[i];
                        }
                        SA[i] = buf[j];
                        buf[j] = t;
                        return;
                    }
                } while (SA[k] < 0);
            }
        }
    }

    private void ssMergeBackward(int pa, int[] buf, int bufoffset, int first, int middle, int last, int depth) {
        int p1;
        int p2;
        int[] SA = this.SA;
        int bufend = bufoffset + (last - middle);
        ssBlockSwap(buf, bufoffset, SA, middle, last - middle);
        int x = 0;
        if (buf[bufend - 1] < 0) {
            x = 0 | 1;
            p1 = pa + (buf[bufend - 1] ^ (-1));
        } else {
            p1 = pa + buf[bufend - 1];
        }
        if (SA[middle - 1] < 0) {
            x |= 2;
            p2 = pa + (SA[middle - 1] ^ (-1));
        } else {
            p2 = pa + SA[middle - 1];
        }
        int t = SA[last - 1];
        int i = last - 1;
        int j = bufend - 1;
        int k = middle - 1;
        while (true) {
            int r = ssCompare(p1, p2, depth);
            if (r > 0) {
                if ((x & 1) != 0) {
                    do {
                        int i2 = i;
                        i--;
                        SA[i2] = buf[j];
                        int i3 = j;
                        j--;
                        buf[i3] = SA[i];
                    } while (buf[j] < 0);
                    x ^= 1;
                }
                int i4 = i;
                i--;
                SA[i4] = buf[j];
                if (j <= bufoffset) {
                    buf[j] = t;
                    return;
                }
                int i5 = j;
                j--;
                buf[i5] = SA[i];
                if (buf[j] < 0) {
                    x |= 1;
                    p1 = pa + (buf[j] ^ (-1));
                } else {
                    p1 = pa + buf[j];
                }
            } else if (r < 0) {
                if ((x & 2) != 0) {
                    do {
                        int i6 = i;
                        i--;
                        SA[i6] = SA[k];
                        int i7 = k;
                        k--;
                        SA[i7] = SA[i];
                    } while (SA[k] < 0);
                    x ^= 2;
                }
                int i8 = i;
                i--;
                SA[i8] = SA[k];
                int i9 = k;
                k--;
                SA[i9] = SA[i];
                if (k < first) {
                    while (bufoffset < j) {
                        int i10 = i;
                        i--;
                        SA[i10] = buf[j];
                        int i11 = j;
                        j--;
                        buf[i11] = SA[i];
                    }
                    SA[i] = buf[j];
                    buf[j] = t;
                    return;
                }
                if (SA[k] < 0) {
                    x |= 2;
                    p2 = pa + (SA[k] ^ (-1));
                } else {
                    p2 = pa + SA[k];
                }
            } else {
                if ((x & 1) != 0) {
                    do {
                        int i12 = i;
                        i--;
                        SA[i12] = buf[j];
                        int i13 = j;
                        j--;
                        buf[i13] = SA[i];
                    } while (buf[j] < 0);
                    x ^= 1;
                }
                int i14 = i;
                int i15 = i - 1;
                SA[i14] = buf[j] ^ (-1);
                if (j <= bufoffset) {
                    buf[j] = t;
                    return;
                }
                int i16 = j;
                j--;
                buf[i16] = SA[i15];
                if ((x & 2) != 0) {
                    do {
                        int i17 = i15;
                        i15--;
                        SA[i17] = SA[k];
                        int i18 = k;
                        k--;
                        SA[i18] = SA[i15];
                    } while (SA[k] < 0);
                    x ^= 2;
                }
                int i19 = i15;
                i = i15 - 1;
                SA[i19] = SA[k];
                int i20 = k;
                k--;
                SA[i20] = SA[i];
                if (k < first) {
                    while (bufoffset < j) {
                        int i21 = i;
                        i--;
                        SA[i21] = buf[j];
                        int i22 = j;
                        j--;
                        buf[i22] = SA[i];
                    }
                    SA[i] = buf[j];
                    buf[j] = t;
                    return;
                }
                if (buf[j] < 0) {
                    x |= 1;
                    p1 = pa + (buf[j] ^ (-1));
                } else {
                    p1 = pa + buf[j];
                }
                if (SA[k] < 0) {
                    x |= 2;
                    p2 = pa + (SA[k] ^ (-1));
                } else {
                    p2 = pa + SA[k];
                }
            }
        }
    }

    private static int getIDX(int a) {
        return 0 <= a ? a : a ^ (-1);
    }

    private void ssMergeCheckEqual(int pa, int depth, int a) {
        int[] SA = this.SA;
        if (0 <= SA[a] && ssCompare(pa + getIDX(SA[a - 1]), pa + SA[a], depth) == 0) {
            SA[a] = SA[a] ^ (-1);
        }
    }

    private void ssMerge(int pa, int first, int middle, int last, int[] buf, int bufoffset, int bufsize, int depth) {
        int[] SA = this.SA;
        StackEntry[] stack = new StackEntry[64];
        int check = 0;
        int ssize = 0;
        while (true) {
            if (last - middle <= bufsize) {
                if (first < middle && middle < last) {
                    ssMergeBackward(pa, buf, bufoffset, first, middle, last, depth);
                }
                if ((check & 1) != 0) {
                    ssMergeCheckEqual(pa, depth, first);
                }
                if ((check & 2) != 0) {
                    ssMergeCheckEqual(pa, depth, last);
                }
                if (ssize == 0) {
                    return;
                }
                ssize--;
                StackEntry entry = stack[ssize];
                first = entry.a;
                middle = entry.b;
                last = entry.c;
                check = entry.d;
            } else if (middle - first <= bufsize) {
                if (first < middle) {
                    ssMergeForward(pa, buf, bufoffset, first, middle, last, depth);
                }
                if ((check & 1) != 0) {
                    ssMergeCheckEqual(pa, depth, first);
                }
                if ((check & 2) != 0) {
                    ssMergeCheckEqual(pa, depth, last);
                }
                if (ssize == 0) {
                    return;
                }
                ssize--;
                StackEntry entry2 = stack[ssize];
                first = entry2.a;
                middle = entry2.b;
                last = entry2.c;
                check = entry2.d;
            } else {
                int m = 0;
                int len = Math.min(middle - first, last - middle);
                int i = len;
                while (true) {
                    int half = i >> 1;
                    if (0 >= len) {
                        break;
                    }
                    if (ssCompare(pa + getIDX(SA[middle + m + half]), pa + getIDX(SA[((middle - m) - half) - 1]), depth) < 0) {
                        m += half + 1;
                        half -= (len & 1) ^ 1;
                    }
                    len = half;
                    i = half;
                }
                if (0 < m) {
                    ssBlockSwap(SA, middle - m, SA, middle, m);
                    int i2 = middle;
                    int j = i2;
                    int i3 = i2;
                    int next = 0;
                    if (middle + m < last) {
                        if (SA[middle + m] < 0) {
                            while (SA[i3 - 1] < 0) {
                                i3--;
                            }
                            SA[middle + m] = SA[middle + m] ^ (-1);
                        }
                        j = middle;
                        while (SA[j] < 0) {
                            j++;
                        }
                        next = 1;
                    }
                    if (i3 - first <= last - j) {
                        int i4 = ssize;
                        ssize++;
                        stack[i4] = new StackEntry(j, middle + m, last, (check & 2) | (next & 1));
                        middle -= m;
                        last = i3;
                        check &= 1;
                    } else {
                        if (i3 == middle && middle == j) {
                            next <<= 1;
                        }
                        int i5 = ssize;
                        ssize++;
                        stack[i5] = new StackEntry(first, middle - m, i3, (check & 1) | (next & 2));
                        first = j;
                        middle += m;
                        check = (check & 2) | (next & 1);
                    }
                } else {
                    if ((check & 1) != 0) {
                        ssMergeCheckEqual(pa, depth, first);
                    }
                    ssMergeCheckEqual(pa, depth, middle);
                    if ((check & 2) != 0) {
                        ssMergeCheckEqual(pa, depth, last);
                    }
                    if (ssize == 0) {
                        return;
                    }
                    ssize--;
                    StackEntry entry3 = stack[ssize];
                    first = entry3.a;
                    middle = entry3.b;
                    last = entry3.c;
                    check = entry3.d;
                }
            }
        }
    }

    private void subStringSort(int pa, int first, int last, int[] buf, int bufoffset, int bufsize, int depth, boolean lastsuffix, int size) {
        int[] SA = this.SA;
        if (lastsuffix) {
            first++;
        }
        int a = first;
        int i = 0;
        while (a + 1024 < last) {
            ssMultiKeyIntroSort(pa, a, a + 1024, depth);
            int[] curbuf = SA;
            int curbufoffset = a + 1024;
            int curbufsize = last - (a + 1024);
            if (curbufsize <= bufsize) {
                curbufsize = bufsize;
                curbuf = buf;
                curbufoffset = bufoffset;
            }
            int b = a;
            int k = 1024;
            int i2 = i;
            while (true) {
                int j = i2;
                if ((j & 1) != 0) {
                    ssMerge(pa, b - k, b, b + k, curbuf, curbufoffset, curbufsize, depth);
                    b -= k;
                    k <<= 1;
                    i2 = j >>> 1;
                }
            }
            a += 1024;
            i++;
        }
        ssMultiKeyIntroSort(pa, a, last, depth);
        int k2 = 1024;
        while (i != 0) {
            if ((i & 1) != 0) {
                ssMerge(pa, a - k2, a, last, buf, bufoffset, bufsize, depth);
                a -= k2;
            }
            k2 <<= 1;
            i >>= 1;
        }
        if (lastsuffix) {
            int a2 = first;
            int i3 = SA[first - 1];
            int r = 1;
            while (a2 < last) {
                if (SA[a2] >= 0) {
                    int iSsCompareLast = ssCompareLast(pa, pa + i3, pa + SA[a2], depth, size);
                    r = iSsCompareLast;
                    if (0 >= iSsCompareLast) {
                        break;
                    }
                }
                SA[a2 - 1] = SA[a2];
                a2++;
            }
            if (r == 0) {
                SA[a2] = SA[a2] ^ (-1);
            }
            SA[a2 - 1] = i3;
        }
    }

    private int trGetC(int isa, int isaD, int isaN, int p) {
        return isaD + p < isaN ? this.SA[isaD + p] : this.SA[isa + (((isaD - isa) + p) % (isaN - isa))];
    }

    private void trFixdown(int isa, int isaD, int isaN, int sa, int i, int size) {
        int[] SA = this.SA;
        int v = SA[sa + i];
        int c = trGetC(isa, isaD, isaN, v);
        while (true) {
            int j = (2 * i) + 1;
            if (j >= size) {
                break;
            }
            int j2 = j + 1;
            int k = j;
            int d = trGetC(isa, isaD, isaN, SA[sa + k]);
            int e = trGetC(isa, isaD, isaN, SA[sa + j2]);
            if (d < e) {
                k = j2;
                d = e;
            }
            if (d <= c) {
                break;
            }
            SA[sa + i] = SA[sa + k];
            i = k;
        }
        SA[sa + i] = v;
    }

    private void trHeapSort(int isa, int isaD, int isaN, int sa, int size) {
        int[] SA = this.SA;
        int m = size;
        if (size % 2 == 0) {
            m--;
            if (trGetC(isa, isaD, isaN, SA[sa + (m / 2)]) < trGetC(isa, isaD, isaN, SA[sa + m])) {
                swapElements(SA, sa + m, SA, sa + (m / 2));
            }
        }
        for (int i = (m / 2) - 1; 0 <= i; i--) {
            trFixdown(isa, isaD, isaN, sa, i, m);
        }
        if (size % 2 == 0) {
            swapElements(SA, sa, SA, sa + m);
            trFixdown(isa, isaD, isaN, sa, 0, m);
        }
        for (int i2 = m - 1; 0 < i2; i2--) {
            int t = SA[sa];
            SA[sa] = SA[sa + i2];
            trFixdown(isa, isaD, isaN, sa, 0, i2);
            SA[sa + i2] = t;
        }
    }

    private void trInsertionSort(int isa, int isaD, int isaN, int first, int last) {
        int r;
        int[] SA = this.SA;
        for (int a = first + 1; a < last; a++) {
            int t = SA[a];
            int b = a - 1;
            do {
                r = trGetC(isa, isaD, isaN, t) - trGetC(isa, isaD, isaN, SA[b]);
                if (0 <= r) {
                    break;
                }
                do {
                    SA[b + 1] = SA[b];
                    b--;
                    if (first > b) {
                        break;
                    }
                } while (SA[b] < 0);
            } while (b >= first);
            if (r == 0) {
                SA[b] = SA[b] ^ (-1);
            }
            SA[b + 1] = t;
        }
    }

    private static int trLog(int n) {
        return (n & Opcodes.V_PREVIEW_EXPERIMENTAL) != 0 ? (n & (-16777216)) != 0 ? 24 + LOG_2_TABLE[(n >> 24) & 255] : LOG_2_TABLE[(n >> 16) & 271] : (n & 65280) != 0 ? 8 + LOG_2_TABLE[(n >> 8) & 255] : LOG_2_TABLE[n & 255];
    }

    private int trMedian3(int isa, int isaD, int isaN, int v1, int v2, int v3) {
        int[] SA = this.SA;
        int SA_v1 = trGetC(isa, isaD, isaN, SA[v1]);
        int SA_v2 = trGetC(isa, isaD, isaN, SA[v2]);
        int SA_v3 = trGetC(isa, isaD, isaN, SA[v3]);
        if (SA_v1 > SA_v2) {
            v1 = v2;
            v2 = v1;
            SA_v1 = SA_v2;
            SA_v2 = SA_v1;
        }
        if (SA_v2 > SA_v3) {
            if (SA_v1 > SA_v3) {
                return v1;
            }
            return v3;
        }
        return v2;
    }

    private int trMedian5(int isa, int isaD, int isaN, int v1, int v2, int v3, int v4, int v5) {
        int[] SA = this.SA;
        int SA_v1 = trGetC(isa, isaD, isaN, SA[v1]);
        int SA_v2 = trGetC(isa, isaD, isaN, SA[v2]);
        int SA_v3 = trGetC(isa, isaD, isaN, SA[v3]);
        int SA_v4 = trGetC(isa, isaD, isaN, SA[v4]);
        int SA_v5 = trGetC(isa, isaD, isaN, SA[v5]);
        if (SA_v2 > SA_v3) {
            v2 = v3;
            v3 = v2;
            SA_v2 = SA_v3;
            SA_v3 = SA_v2;
        }
        if (SA_v4 > SA_v5) {
            v4 = v5;
            v5 = v4;
            SA_v4 = SA_v5;
            SA_v5 = SA_v4;
        }
        if (SA_v2 > SA_v4) {
            int temp = v2;
            v4 = temp;
            int SA_vtemp = SA_v2;
            SA_v4 = SA_vtemp;
            int temp2 = v3;
            v3 = v5;
            v5 = temp2;
            int SA_vtemp2 = SA_v3;
            SA_v3 = SA_v5;
            SA_v5 = SA_vtemp2;
        }
        if (SA_v1 > SA_v3) {
            v1 = v3;
            v3 = v1;
            SA_v1 = SA_v3;
            SA_v3 = SA_v1;
        }
        if (SA_v1 > SA_v4) {
            int temp3 = v1;
            v4 = temp3;
            int SA_vtemp3 = SA_v1;
            SA_v4 = SA_vtemp3;
            v3 = v5;
            SA_v3 = SA_v5;
        }
        if (SA_v3 > SA_v4) {
            return v4;
        }
        return v3;
    }

    private int trPivot(int isa, int isaD, int isaN, int first, int last) {
        int t = last - first;
        int middle = first + (t / 2);
        if (t <= 512) {
            if (t <= 32) {
                return trMedian3(isa, isaD, isaN, first, middle, last - 1);
            }
            int t2 = t >> 2;
            return trMedian5(isa, isaD, isaN, first, first + t2, middle, (last - 1) - t2, last - 1);
        }
        int t3 = t >> 3;
        return trMedian3(isa, isaD, isaN, trMedian3(isa, isaD, isaN, first, first + t3, first + (t3 << 1)), trMedian3(isa, isaD, isaN, middle - t3, middle, middle + t3), trMedian3(isa, isaD, isaN, (last - 1) - (t3 << 1), (last - 1) - t3, last - 1));
    }

    private void lsUpdateGroup(int isa, int first, int last) {
        int[] SA = this.SA;
        int a = first;
        while (a < last) {
            if (0 <= SA[a]) {
                int b = a;
                do {
                    SA[isa + SA[a]] = a;
                    a++;
                    if (a >= last) {
                        break;
                    }
                } while (0 <= SA[a]);
                SA[b] = b - a;
                if (last <= a) {
                    return;
                }
            }
            int b2 = a;
            do {
                SA[a] = SA[a] ^ (-1);
                a++;
            } while (SA[a] < 0);
            do {
                SA[isa + SA[b2]] = a;
                b2++;
            } while (b2 <= a);
            a++;
        }
    }

    private void lsIntroSort(int isa, int isaD, int isaN, int first, int last) {
        int[] SA = this.SA;
        StackEntry[] stack = new StackEntry[64];
        int x = 0;
        int ssize = 0;
        int limit = trLog(last - first);
        while (true) {
            if (last - first <= 8) {
                if (1 < last - first) {
                    trInsertionSort(isa, isaD, isaN, first, last);
                    lsUpdateGroup(isa, first, last);
                } else if (last - first == 1) {
                    SA[first] = -1;
                }
                if (ssize == 0) {
                    return;
                }
                ssize--;
                StackEntry entry = stack[ssize];
                first = entry.a;
                last = entry.b;
                limit = entry.c;
            } else {
                int i = limit;
                limit--;
                if (i == 0) {
                    trHeapSort(isa, isaD, isaN, first, last - first);
                    int i2 = last - 1;
                    while (true) {
                        int a = i2;
                        if (first >= a) {
                            break;
                        }
                        x = trGetC(isa, isaD, isaN, SA[a]);
                        int b = a - 1;
                        while (first <= b && trGetC(isa, isaD, isaN, SA[b]) == x) {
                            SA[b] = SA[b] ^ (-1);
                            b--;
                        }
                        i2 = b;
                    }
                    lsUpdateGroup(isa, first, last);
                    if (ssize == 0) {
                        return;
                    }
                    ssize--;
                    StackEntry entry2 = stack[ssize];
                    first = entry2.a;
                    last = entry2.b;
                    limit = entry2.c;
                } else {
                    swapElements(SA, first, SA, trPivot(isa, isaD, isaN, first, last));
                    int v = trGetC(isa, isaD, isaN, SA[first]);
                    int b2 = first + 1;
                    while (b2 < last) {
                        int iTrGetC = trGetC(isa, isaD, isaN, SA[b2]);
                        x = iTrGetC;
                        if (iTrGetC != v) {
                            break;
                        } else {
                            b2++;
                        }
                    }
                    int i3 = b2;
                    int a2 = i3;
                    if (i3 < last && x < v) {
                        while (true) {
                            b2++;
                            if (b2 >= last) {
                                break;
                            }
                            int iTrGetC2 = trGetC(isa, isaD, isaN, SA[b2]);
                            x = iTrGetC2;
                            if (iTrGetC2 > v) {
                                break;
                            } else if (x == v) {
                                swapElements(SA, b2, SA, a2);
                                a2++;
                            }
                        }
                    }
                    int c = last - 1;
                    while (b2 < c) {
                        int iTrGetC3 = trGetC(isa, isaD, isaN, SA[c]);
                        x = iTrGetC3;
                        if (iTrGetC3 != v) {
                            break;
                        } else {
                            c--;
                        }
                    }
                    int i4 = c;
                    int d = i4;
                    if (b2 < i4 && x > v) {
                        while (true) {
                            c--;
                            if (b2 >= c) {
                                break;
                            }
                            int iTrGetC4 = trGetC(isa, isaD, isaN, SA[c]);
                            x = iTrGetC4;
                            if (iTrGetC4 < v) {
                                break;
                            } else if (x == v) {
                                swapElements(SA, c, SA, d);
                                d--;
                            }
                        }
                    }
                    while (b2 < c) {
                        swapElements(SA, b2, SA, c);
                        while (true) {
                            b2++;
                            if (b2 >= c) {
                                break;
                            }
                            int iTrGetC5 = trGetC(isa, isaD, isaN, SA[b2]);
                            x = iTrGetC5;
                            if (iTrGetC5 > v) {
                                break;
                            } else if (x == v) {
                                swapElements(SA, b2, SA, a2);
                                a2++;
                            }
                        }
                        while (true) {
                            c--;
                            if (b2 < c) {
                                int iTrGetC6 = trGetC(isa, isaD, isaN, SA[c]);
                                x = iTrGetC6;
                                if (iTrGetC6 >= v) {
                                    if (x == v) {
                                        swapElements(SA, c, SA, d);
                                        d--;
                                    }
                                }
                            }
                        }
                    }
                    if (a2 <= d) {
                        int c2 = b2 - 1;
                        int i5 = a2 - first;
                        int s = i5;
                        int t = b2 - a2;
                        if (i5 > t) {
                            s = t;
                        }
                        int e = first;
                        int f = b2 - s;
                        while (0 < s) {
                            swapElements(SA, e, SA, f);
                            s--;
                            e++;
                            f++;
                        }
                        int i6 = d - c2;
                        int s2 = i6;
                        int t2 = (last - d) - 1;
                        if (i6 > t2) {
                            s2 = t2;
                        }
                        int e2 = b2;
                        int f2 = last - s2;
                        while (0 < s2) {
                            swapElements(SA, e2, SA, f2);
                            s2--;
                            e2++;
                            f2++;
                        }
                        int a3 = first + (b2 - a2);
                        int b3 = last - (d - c2);
                        int v2 = a3 - 1;
                        for (int c3 = first; c3 < a3; c3++) {
                            SA[isa + SA[c3]] = v2;
                        }
                        if (b3 < last) {
                            int v3 = b3 - 1;
                            for (int c4 = a3; c4 < b3; c4++) {
                                SA[isa + SA[c4]] = v3;
                            }
                        }
                        if (b3 - a3 == 1) {
                            SA[a3] = -1;
                        }
                        if (a3 - first <= last - b3) {
                            if (first < a3) {
                                int i7 = ssize;
                                ssize++;
                                stack[i7] = new StackEntry(b3, last, limit, 0);
                                last = a3;
                            } else {
                                first = b3;
                            }
                        } else if (b3 < last) {
                            int i8 = ssize;
                            ssize++;
                            stack[i8] = new StackEntry(first, a3, limit, 0);
                            first = b3;
                        } else {
                            last = a3;
                        }
                    } else {
                        if (ssize == 0) {
                            return;
                        }
                        ssize--;
                        StackEntry entry3 = stack[ssize];
                        first = entry3.a;
                        last = entry3.b;
                        limit = entry3.c;
                    }
                }
            }
        }
    }

    private void lsSort(int isa, int n, int depth) {
        int[] SA = this.SA;
        int i = isa;
        int i2 = depth;
        while (true) {
            int isaD = i + i2;
            if ((-n) < SA[0]) {
                int first = 0;
                int skip = 0;
                do {
                    int t = SA[first];
                    if (t < 0) {
                        first -= t;
                        skip += t;
                    } else {
                        if (skip != 0) {
                            SA[first + skip] = skip;
                            skip = 0;
                        }
                        int last = SA[isa + t] + 1;
                        lsIntroSort(isa, isaD, isa + n, first, last);
                        first = last;
                    }
                } while (first < n);
                if (skip != 0) {
                    SA[first + skip] = skip;
                }
                if (n >= isaD - isa) {
                    i = isaD;
                    i2 = isaD - isa;
                } else {
                    int first2 = 0;
                    do {
                        int t2 = SA[first2];
                        if (t2 < 0) {
                            first2 -= t2;
                        } else {
                            int last2 = SA[isa + t2] + 1;
                            for (int i3 = first2; i3 < last2; i3++) {
                                SA[isa + SA[i3]] = i3;
                            }
                            first2 = last2;
                        }
                    } while (first2 < n);
                    return;
                }
            } else {
                return;
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Bzip2DivSufSort$PartitionResult.class */
    private static class PartitionResult {
        final int first;
        final int last;

        PartitionResult(int first, int last) {
            this.first = first;
            this.last = last;
        }
    }

    private PartitionResult trPartition(int isa, int isaD, int isaN, int first, int last, int v) {
        int x;
        int x2;
        int x3;
        int[] SA = this.SA;
        int x4 = 0;
        int b = first;
        while (b < last) {
            int iTrGetC = trGetC(isa, isaD, isaN, SA[b]);
            x4 = iTrGetC;
            if (iTrGetC != v) {
                break;
            }
            b++;
        }
        int i = b;
        int a = i;
        if (i < last && x4 < v) {
            while (true) {
                b++;
                if (b >= last) {
                    break;
                }
                int iTrGetC2 = trGetC(isa, isaD, isaN, SA[b]);
                x4 = iTrGetC2;
                if (iTrGetC2 > v) {
                    break;
                }
                if (x4 == v) {
                    swapElements(SA, b, SA, a);
                    a++;
                }
            }
        }
        int c = last - 1;
        while (b < c) {
            int iTrGetC3 = trGetC(isa, isaD, isaN, SA[c]);
            x4 = iTrGetC3;
            if (iTrGetC3 != v) {
                break;
            }
            c--;
        }
        int i2 = c;
        int d = i2;
        if (b < i2 && x4 > v) {
            while (true) {
                c--;
                if (b >= c || (x3 = trGetC(isa, isaD, isaN, SA[c])) < v) {
                    break;
                }
                if (x3 == v) {
                    swapElements(SA, c, SA, d);
                    d--;
                }
            }
        }
        while (b < c) {
            swapElements(SA, b, SA, c);
            while (true) {
                b++;
                if (b >= c || (x2 = trGetC(isa, isaD, isaN, SA[b])) > v) {
                    break;
                }
                if (x2 == v) {
                    swapElements(SA, b, SA, a);
                    a++;
                }
            }
            while (true) {
                c--;
                if (b >= c || (x = trGetC(isa, isaD, isaN, SA[c])) < v) {
                    break;
                }
                if (x == v) {
                    swapElements(SA, c, SA, d);
                    d--;
                }
            }
        }
        if (a <= d) {
            int c2 = b - 1;
            int i3 = a - first;
            int s = i3;
            int t = b - a;
            if (i3 > t) {
                s = t;
            }
            int e = first;
            int f = b - s;
            while (0 < s) {
                swapElements(SA, e, SA, f);
                s--;
                e++;
                f++;
            }
            int i4 = d - c2;
            int s2 = i4;
            int t2 = (last - d) - 1;
            if (i4 > t2) {
                s2 = t2;
            }
            int e2 = b;
            int f2 = last - s2;
            while (0 < s2) {
                swapElements(SA, e2, SA, f2);
                s2--;
                e2++;
                f2++;
            }
            first += b - a;
            last -= d - c2;
        }
        return new PartitionResult(first, last);
    }

    private void trCopy(int isa, int isaN, int first, int a, int b, int last, int depth) {
        int[] SA = this.SA;
        int v = b - 1;
        int d = a - 1;
        for (int c = first; c <= d; c++) {
            int i = SA[c] - depth;
            int s = i;
            if (i < 0) {
                s += isaN - isa;
            }
            if (SA[isa + s] == v) {
                d++;
                SA[d] = s;
                SA[isa + s] = d;
            }
        }
        int c2 = last - 1;
        int e = d + 1;
        int d2 = b;
        while (e < d2) {
            int i2 = SA[c2] - depth;
            int s2 = i2;
            if (i2 < 0) {
                s2 += isaN - isa;
            }
            if (SA[isa + s2] == v) {
                d2--;
                SA[d2] = s2;
                SA[isa + s2] = d2;
            }
            c2--;
        }
    }

    private void trIntroSort(int isa, int isaD, int isaN, int first, int last, TRBudget budget, int size) {
        int[] SA = this.SA;
        StackEntry[] stack = new StackEntry[64];
        int x = 0;
        int ssize = 0;
        int limit = trLog(last - first);
        while (true) {
            if (limit < 0) {
                if (limit == -1) {
                    if (!budget.update(size, last - first)) {
                        break;
                    }
                    PartitionResult result = trPartition(isa, isaD - 1, isaN, first, last, last - 1);
                    int a = result.first;
                    int b = result.last;
                    if (first < a || b < last) {
                        if (a < last) {
                            int v = a - 1;
                            for (int c = first; c < a; c++) {
                                SA[isa + SA[c]] = v;
                            }
                        }
                        if (b < last) {
                            int v2 = b - 1;
                            for (int c2 = a; c2 < b; c2++) {
                                SA[isa + SA[c2]] = v2;
                            }
                        }
                        int i = ssize;
                        int ssize2 = ssize + 1;
                        stack[i] = new StackEntry(0, a, b, 0);
                        ssize = ssize2 + 1;
                        stack[ssize2] = new StackEntry(isaD - 1, first, last, -2);
                        if (a - first <= last - b) {
                            if (1 < a - first) {
                                ssize++;
                                stack[ssize] = new StackEntry(isaD, b, last, trLog(last - b));
                                last = a;
                                limit = trLog(a - first);
                            } else if (1 < last - b) {
                                first = b;
                                limit = trLog(last - b);
                            } else {
                                if (ssize == 0) {
                                    return;
                                }
                                ssize--;
                                StackEntry entry = stack[ssize];
                                isaD = entry.a;
                                first = entry.b;
                                last = entry.c;
                                limit = entry.d;
                            }
                        } else if (1 < last - b) {
                            ssize++;
                            stack[ssize] = new StackEntry(isaD, first, a, trLog(a - first));
                            first = b;
                            limit = trLog(last - b);
                        } else if (1 < a - first) {
                            last = a;
                            limit = trLog(a - first);
                        } else {
                            if (ssize == 0) {
                                return;
                            }
                            ssize--;
                            StackEntry entry2 = stack[ssize];
                            isaD = entry2.a;
                            first = entry2.b;
                            last = entry2.c;
                            limit = entry2.d;
                        }
                    } else {
                        for (int c3 = first; c3 < last; c3++) {
                            SA[isa + SA[c3]] = c3;
                        }
                        if (ssize == 0) {
                            return;
                        }
                        ssize--;
                        StackEntry entry3 = stack[ssize];
                        isaD = entry3.a;
                        first = entry3.b;
                        last = entry3.c;
                        limit = entry3.d;
                    }
                } else if (limit == -2) {
                    int ssize3 = ssize - 1;
                    trCopy(isa, isaN, first, stack[ssize3].b, stack[ssize3].c, last, isaD - isa);
                    if (ssize3 == 0) {
                        return;
                    }
                    ssize = ssize3 - 1;
                    StackEntry entry4 = stack[ssize];
                    isaD = entry4.a;
                    first = entry4.b;
                    last = entry4.c;
                    limit = entry4.d;
                } else {
                    if (0 <= SA[first]) {
                        int a2 = first;
                        do {
                            SA[isa + SA[a2]] = a2;
                            a2++;
                            if (a2 >= last) {
                                break;
                            }
                        } while (0 <= SA[a2]);
                        first = a2;
                    }
                    if (first < last) {
                        int a3 = first;
                        do {
                            SA[a3] = SA[a3] ^ (-1);
                            a3++;
                        } while (SA[a3] < 0);
                        int next = SA[isa + SA[a3]] != SA[isaD + SA[a3]] ? trLog((a3 - first) + 1) : -1;
                        int a4 = a3 + 1;
                        if (a4 < last) {
                            int v3 = a4 - 1;
                            for (int b2 = first; b2 < a4; b2++) {
                                SA[isa + SA[b2]] = v3;
                            }
                        }
                        if (a4 - first <= last - a4) {
                            int i2 = ssize;
                            ssize++;
                            stack[i2] = new StackEntry(isaD, a4, last, -3);
                            isaD++;
                            last = a4;
                            limit = next;
                        } else if (1 < last - a4) {
                            int i3 = ssize;
                            ssize++;
                            stack[i3] = new StackEntry(isaD + 1, first, a4, next);
                            first = a4;
                            limit = -3;
                        } else {
                            isaD++;
                            last = a4;
                            limit = next;
                        }
                    } else {
                        if (ssize == 0) {
                            return;
                        }
                        ssize--;
                        StackEntry entry5 = stack[ssize];
                        isaD = entry5.a;
                        first = entry5.b;
                        last = entry5.c;
                        limit = entry5.d;
                    }
                }
            } else if (last - first <= 8) {
                if (!budget.update(size, last - first)) {
                    break;
                }
                trInsertionSort(isa, isaD, isaN, first, last);
                limit = -3;
            } else {
                int i4 = limit;
                limit--;
                if (i4 == 0) {
                    if (!budget.update(size, last - first)) {
                        break;
                    }
                    trHeapSort(isa, isaD, isaN, first, last - first);
                    int i5 = last - 1;
                    while (true) {
                        int a5 = i5;
                        if (first >= a5) {
                            break;
                        }
                        x = trGetC(isa, isaD, isaN, SA[a5]);
                        int b3 = a5 - 1;
                        while (first <= b3 && trGetC(isa, isaD, isaN, SA[b3]) == x) {
                            SA[b3] = SA[b3] ^ (-1);
                            b3--;
                        }
                        i5 = b3;
                    }
                    limit = -3;
                } else {
                    swapElements(SA, first, SA, trPivot(isa, isaD, isaN, first, last));
                    int v4 = trGetC(isa, isaD, isaN, SA[first]);
                    int b4 = first + 1;
                    while (b4 < last) {
                        int iTrGetC = trGetC(isa, isaD, isaN, SA[b4]);
                        x = iTrGetC;
                        if (iTrGetC != v4) {
                            break;
                        } else {
                            b4++;
                        }
                    }
                    int i6 = b4;
                    int a6 = i6;
                    if (i6 < last && x < v4) {
                        while (true) {
                            b4++;
                            if (b4 >= last) {
                                break;
                            }
                            int iTrGetC2 = trGetC(isa, isaD, isaN, SA[b4]);
                            x = iTrGetC2;
                            if (iTrGetC2 > v4) {
                                break;
                            } else if (x == v4) {
                                swapElements(SA, b4, SA, a6);
                                a6++;
                            }
                        }
                    }
                    int c4 = last - 1;
                    while (b4 < c4) {
                        int iTrGetC3 = trGetC(isa, isaD, isaN, SA[c4]);
                        x = iTrGetC3;
                        if (iTrGetC3 != v4) {
                            break;
                        } else {
                            c4--;
                        }
                    }
                    int i7 = c4;
                    int d = i7;
                    if (b4 < i7 && x > v4) {
                        while (true) {
                            c4--;
                            if (b4 >= c4) {
                                break;
                            }
                            int iTrGetC4 = trGetC(isa, isaD, isaN, SA[c4]);
                            x = iTrGetC4;
                            if (iTrGetC4 < v4) {
                                break;
                            } else if (x == v4) {
                                swapElements(SA, c4, SA, d);
                                d--;
                            }
                        }
                    }
                    while (b4 < c4) {
                        swapElements(SA, b4, SA, c4);
                        while (true) {
                            b4++;
                            if (b4 >= c4) {
                                break;
                            }
                            int iTrGetC5 = trGetC(isa, isaD, isaN, SA[b4]);
                            x = iTrGetC5;
                            if (iTrGetC5 > v4) {
                                break;
                            } else if (x == v4) {
                                swapElements(SA, b4, SA, a6);
                                a6++;
                            }
                        }
                        while (true) {
                            c4--;
                            if (b4 < c4) {
                                int iTrGetC6 = trGetC(isa, isaD, isaN, SA[c4]);
                                x = iTrGetC6;
                                if (iTrGetC6 >= v4) {
                                    if (x == v4) {
                                        swapElements(SA, c4, SA, d);
                                        d--;
                                    }
                                }
                            }
                        }
                    }
                    if (a6 <= d) {
                        int c5 = b4 - 1;
                        int i8 = a6 - first;
                        int s = i8;
                        int t = b4 - a6;
                        if (i8 > t) {
                            s = t;
                        }
                        int e = first;
                        int f = b4 - s;
                        while (0 < s) {
                            swapElements(SA, e, SA, f);
                            s--;
                            e++;
                            f++;
                        }
                        int i9 = d - c5;
                        int s2 = i9;
                        int t2 = (last - d) - 1;
                        if (i9 > t2) {
                            s2 = t2;
                        }
                        int e2 = b4;
                        int f2 = last - s2;
                        while (0 < s2) {
                            swapElements(SA, e2, SA, f2);
                            s2--;
                            e2++;
                            f2++;
                        }
                        int a7 = first + (b4 - a6);
                        int b5 = last - (d - c5);
                        int next2 = SA[isa + SA[a7]] != v4 ? trLog(b5 - a7) : -1;
                        int v5 = a7 - 1;
                        for (int c6 = first; c6 < a7; c6++) {
                            SA[isa + SA[c6]] = v5;
                        }
                        if (b5 < last) {
                            int v6 = b5 - 1;
                            for (int c7 = a7; c7 < b5; c7++) {
                                SA[isa + SA[c7]] = v6;
                            }
                        }
                        if (a7 - first <= last - b5) {
                            if (last - b5 <= b5 - a7) {
                                if (1 < a7 - first) {
                                    int i10 = ssize;
                                    int ssize4 = ssize + 1;
                                    stack[i10] = new StackEntry(isaD + 1, a7, b5, next2);
                                    ssize = ssize4 + 1;
                                    stack[ssize4] = new StackEntry(isaD, b5, last, limit);
                                    last = a7;
                                } else if (1 < last - b5) {
                                    int i11 = ssize;
                                    ssize++;
                                    stack[i11] = new StackEntry(isaD + 1, a7, b5, next2);
                                    first = b5;
                                } else if (1 < b5 - a7) {
                                    isaD++;
                                    first = a7;
                                    last = b5;
                                    limit = next2;
                                } else {
                                    if (ssize == 0) {
                                        return;
                                    }
                                    ssize--;
                                    StackEntry entry6 = stack[ssize];
                                    isaD = entry6.a;
                                    first = entry6.b;
                                    last = entry6.c;
                                    limit = entry6.d;
                                }
                            } else if (a7 - first <= b5 - a7) {
                                if (1 < a7 - first) {
                                    int i12 = ssize;
                                    int ssize5 = ssize + 1;
                                    stack[i12] = new StackEntry(isaD, b5, last, limit);
                                    ssize = ssize5 + 1;
                                    stack[ssize5] = new StackEntry(isaD + 1, a7, b5, next2);
                                    last = a7;
                                } else if (1 < b5 - a7) {
                                    int i13 = ssize;
                                    ssize++;
                                    stack[i13] = new StackEntry(isaD, b5, last, limit);
                                    isaD++;
                                    first = a7;
                                    last = b5;
                                    limit = next2;
                                } else {
                                    first = b5;
                                }
                            } else if (1 < b5 - a7) {
                                int i14 = ssize;
                                int ssize6 = ssize + 1;
                                stack[i14] = new StackEntry(isaD, b5, last, limit);
                                ssize = ssize6 + 1;
                                stack[ssize6] = new StackEntry(isaD, first, a7, limit);
                                isaD++;
                                first = a7;
                                last = b5;
                                limit = next2;
                            } else {
                                int i15 = ssize;
                                ssize++;
                                stack[i15] = new StackEntry(isaD, b5, last, limit);
                                last = a7;
                            }
                        } else if (a7 - first <= b5 - a7) {
                            if (1 < last - b5) {
                                int i16 = ssize;
                                int ssize7 = ssize + 1;
                                stack[i16] = new StackEntry(isaD + 1, a7, b5, next2);
                                ssize = ssize7 + 1;
                                stack[ssize7] = new StackEntry(isaD, first, a7, limit);
                                first = b5;
                            } else if (1 < a7 - first) {
                                int i17 = ssize;
                                ssize++;
                                stack[i17] = new StackEntry(isaD + 1, a7, b5, next2);
                                last = a7;
                            } else if (1 < b5 - a7) {
                                isaD++;
                                first = a7;
                                last = b5;
                                limit = next2;
                            } else {
                                int i18 = ssize;
                                ssize++;
                                stack[i18] = new StackEntry(isaD, first, last, limit);
                            }
                        } else if (last - b5 <= b5 - a7) {
                            if (1 < last - b5) {
                                int i19 = ssize;
                                int ssize8 = ssize + 1;
                                stack[i19] = new StackEntry(isaD, first, a7, limit);
                                ssize = ssize8 + 1;
                                stack[ssize8] = new StackEntry(isaD + 1, a7, b5, next2);
                                first = b5;
                            } else if (1 < b5 - a7) {
                                int i20 = ssize;
                                ssize++;
                                stack[i20] = new StackEntry(isaD, first, a7, limit);
                                isaD++;
                                first = a7;
                                last = b5;
                                limit = next2;
                            } else {
                                last = a7;
                            }
                        } else if (1 < b5 - a7) {
                            int i21 = ssize;
                            int ssize9 = ssize + 1;
                            stack[i21] = new StackEntry(isaD, first, a7, limit);
                            ssize = ssize9 + 1;
                            stack[ssize9] = new StackEntry(isaD, b5, last, limit);
                            isaD++;
                            first = a7;
                            last = b5;
                            limit = next2;
                        } else {
                            int i22 = ssize;
                            ssize++;
                            stack[i22] = new StackEntry(isaD, first, a7, limit);
                            first = b5;
                        }
                    } else {
                        if (!budget.update(size, last - first)) {
                            break;
                        }
                        limit++;
                        isaD++;
                    }
                }
            }
        }
        for (int s3 = 0; s3 < ssize; s3++) {
            if (stack[s3].d == -3) {
                lsUpdateGroup(isa, stack[s3].b, stack[s3].c);
            }
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/Bzip2DivSufSort$TRBudget.class */
    private static class TRBudget {
        int budget;
        int chance;

        TRBudget(int budget, int chance) {
            this.budget = budget;
            this.chance = chance;
        }

        boolean update(int size, int n) {
            this.budget -= n;
            if (this.budget <= 0) {
                int i = this.chance - 1;
                this.chance = i;
                if (i == 0) {
                    return false;
                }
                this.budget += size;
                return true;
            }
            return true;
        }
    }

    private void trSort(int isa, int n, int depth) {
        int[] SA = this.SA;
        int first = 0;
        if ((-n) < SA[0]) {
            TRBudget budget = new TRBudget(n, ((trLog(n) * 2) / 3) + 1);
            do {
                int t = SA[first];
                if (t < 0) {
                    first -= t;
                } else {
                    int last = SA[isa + t] + 1;
                    if (1 < last - first) {
                        trIntroSort(isa, isa + depth, isa + n, first, last, budget, n);
                        if (budget.chance == 0) {
                            if (0 < first) {
                                SA[0] = -first;
                            }
                            lsSort(isa, n, depth);
                            return;
                        }
                    }
                    first = last;
                }
            } while (first < n);
        }
    }

    private static int BUCKET_B(int c0, int c1) {
        return (c1 << 8) | c0;
    }

    private static int BUCKET_BSTAR(int c0, int c1) {
        return (c0 << 8) | c1;
    }

    private int sortTypeBstar(int[] bucketA, int[] bucketB) {
        int ti;
        int ti1;
        int ti2;
        int ti12;
        byte[] T = this.T;
        int[] SA = this.SA;
        int n = this.n;
        int[] tempbuf = new int[256];
        int i = 1;
        int flag = 1;
        while (true) {
            if (i >= n) {
                break;
            }
            if (T[i - 1] == T[i]) {
                i++;
            } else if ((T[i - 1] & 255) > (T[i] & 255)) {
                flag = 0;
            }
        }
        int i2 = n - 1;
        int m = n;
        int ti3 = T[i2] & 255;
        int t0 = T[0] & 255;
        if (ti3 < t0 || (T[i2] == T[0] && flag != 0)) {
            if (flag == 0) {
                int iBUCKET_BSTAR = BUCKET_BSTAR(ti3, t0);
                bucketB[iBUCKET_BSTAR] = bucketB[iBUCKET_BSTAR] + 1;
                m--;
                SA[m] = i2;
            } else {
                int iBUCKET_B = BUCKET_B(ti3, t0);
                bucketB[iBUCKET_B] = bucketB[iBUCKET_B] + 1;
            }
            while (true) {
                i2--;
                if (0 <= i2 && (ti = T[i2] & 255) <= (ti1 = T[i2 + 1] & 255)) {
                    int iBUCKET_B2 = BUCKET_B(ti, ti1);
                    bucketB[iBUCKET_B2] = bucketB[iBUCKET_B2] + 1;
                }
            }
        }
        while (0 <= i2) {
            do {
                int i3 = T[i2] & 255;
                bucketA[i3] = bucketA[i3] + 1;
                i2--;
                if (0 > i2) {
                    break;
                }
            } while ((T[i2] & 255) >= (T[i2 + 1] & 255));
            if (0 <= i2) {
                int iBUCKET_BSTAR2 = BUCKET_BSTAR(T[i2] & 255, T[i2 + 1] & 255);
                bucketB[iBUCKET_BSTAR2] = bucketB[iBUCKET_BSTAR2] + 1;
                m--;
                SA[m] = i2;
                while (true) {
                    i2--;
                    if (0 > i2 || (ti2 = T[i2] & 255) > (ti12 = T[i2 + 1] & 255)) {
                        break;
                    }
                    int iBUCKET_B3 = BUCKET_B(ti2, ti12);
                    bucketB[iBUCKET_B3] = bucketB[iBUCKET_B3] + 1;
                }
            }
        }
        int m2 = n - m;
        if (m2 == 0) {
            for (int i4 = 0; i4 < n; i4++) {
                SA[i4] = i4;
            }
            return 0;
        }
        int i5 = -1;
        int j = 0;
        for (int c0 = 0; c0 < 256; c0++) {
            int t = i5 + bucketA[c0];
            bucketA[c0] = i5 + j;
            i5 = t + bucketB[BUCKET_B(c0, c0)];
            for (int c1 = c0 + 1; c1 < 256; c1++) {
                j += bucketB[BUCKET_BSTAR(c0, c1)];
                bucketB[(c0 << 8) | c1] = j;
                i5 += bucketB[BUCKET_B(c0, c1)];
            }
        }
        int PAb = n - m2;
        for (int i6 = m2 - 2; 0 <= i6; i6--) {
            int t2 = SA[PAb + i6];
            int c02 = T[t2] & 255;
            int c12 = T[t2 + 1] & 255;
            int iBUCKET_BSTAR3 = BUCKET_BSTAR(c02, c12);
            int i7 = bucketB[iBUCKET_BSTAR3] - 1;
            bucketB[iBUCKET_BSTAR3] = i7;
            SA[i7] = i6;
        }
        int t3 = SA[(PAb + m2) - 1];
        int c03 = T[t3] & 255;
        int c13 = T[t3 + 1] & 255;
        int iBUCKET_BSTAR4 = BUCKET_BSTAR(c03, c13);
        int i8 = bucketB[iBUCKET_BSTAR4] - 1;
        bucketB[iBUCKET_BSTAR4] = i8;
        SA[i8] = m2 - 1;
        int[] buf = SA;
        int bufoffset = m2;
        int bufsize = n - (2 * m2);
        if (bufsize <= 256) {
            buf = tempbuf;
            bufoffset = 0;
            bufsize = 256;
        }
        int c04 = 255;
        int j2 = m2;
        while (0 < j2) {
            for (int c14 = 255; c04 < c14; c14--) {
                int i9 = bucketB[BUCKET_BSTAR(c04, c14)];
                if (1 < j2 - i9) {
                    subStringSort(PAb, i9, j2, buf, bufoffset, bufsize, 2, SA[i9] == m2 - 1, n);
                }
                j2 = i9;
            }
            c04--;
        }
        int i10 = m2 - 1;
        while (0 <= i10) {
            if (0 <= SA[i10]) {
                int j3 = i10;
                do {
                    SA[m2 + SA[i10]] = i10;
                    i10--;
                    if (0 > i10) {
                        break;
                    }
                } while (0 <= SA[i10]);
                SA[i10 + 1] = i10 - j3;
                if (i10 <= 0) {
                    break;
                }
            }
            int j4 = i10;
            do {
                int i11 = SA[i10] ^ (-1);
                SA[i10] = i11;
                SA[m2 + i11] = j4;
                i10--;
            } while (SA[i10] < 0);
            SA[m2 + SA[i10]] = j4;
            i10--;
        }
        trSort(m2, m2, 1);
        int i12 = n - 1;
        int j5 = m2;
        if ((T[i12] & 255) < (T[0] & 255) || (T[i12] == T[0] && flag != 0)) {
            if (flag == 0) {
                j5--;
                SA[SA[m2 + j5]] = i12;
            }
            do {
                i12--;
                if (0 <= i12) {
                }
            } while ((T[i12] & 255) <= (T[i12 + 1] & 255));
        }
        while (0 <= i12) {
            do {
                i12--;
                if (0 > i12) {
                    break;
                }
            } while ((T[i12] & 255) >= (T[i12 + 1] & 255));
            if (0 <= i12) {
                j5--;
                SA[SA[m2 + j5]] = i12;
                do {
                    i12--;
                    if (0 <= i12) {
                    }
                } while ((T[i12] & 255) <= (T[i12 + 1] & 255));
            }
        }
        int i13 = n - 1;
        int k = m2 - 1;
        for (int c05 = 255; 0 <= c05; c05--) {
            for (int c15 = 255; c05 < c15; c15--) {
                int t4 = i13 - bucketB[BUCKET_B(c05, c15)];
                bucketB[BUCKET_B(c05, c15)] = i13 + 1;
                i13 = t4;
                int j6 = bucketB[BUCKET_BSTAR(c05, c15)];
                while (j6 <= k) {
                    SA[i13] = SA[k];
                    i13--;
                    k--;
                }
            }
            int t5 = i13 - bucketB[BUCKET_B(c05, c05)];
            bucketB[BUCKET_B(c05, c05)] = i13 + 1;
            if (c05 < 255) {
                bucketB[BUCKET_BSTAR(c05, c05 + 1)] = t5 + 1;
            }
            i13 = bucketA[c05];
        }
        return m2;
    }

    private int constructBWT(int[] bucketA, int[] bucketB) {
        byte[] T = this.T;
        int[] SA = this.SA;
        int n = this.n;
        int t = 0;
        int c2 = 0;
        int orig = -1;
        for (int c1 = 254; 0 <= c1; c1--) {
            int i = bucketB[BUCKET_BSTAR(c1, c1 + 1)];
            t = 0;
            c2 = -1;
            for (int j = bucketA[c1 + 1]; i <= j; j--) {
                int s = SA[j];
                if (0 <= s) {
                    int s2 = s - 1;
                    if (s2 < 0) {
                        s2 = n - 1;
                    }
                    int c0 = T[s2] & 255;
                    if (c0 <= c1) {
                        SA[j] = s ^ (-1);
                        if (0 < s2 && (T[s2 - 1] & 255) > c0) {
                            s2 ^= -1;
                        }
                        if (c2 == c0) {
                            t--;
                            SA[t] = s2;
                        } else {
                            if (0 <= c2) {
                                bucketB[BUCKET_B(c2, c1)] = t;
                            }
                            c2 = c0;
                            int i2 = bucketB[BUCKET_B(c0, c1)] - 1;
                            t = i2;
                            SA[i2] = s2;
                        }
                    }
                } else {
                    SA[j] = s ^ (-1);
                }
            }
        }
        for (int i3 = 0; i3 < n; i3++) {
            int s3 = SA[i3];
            int s1 = s3;
            if (0 <= s3) {
                int s4 = s3 - 1;
                if (s4 < 0) {
                    s4 = n - 1;
                }
                int c02 = T[s4] & 255;
                if (c02 >= (T[s4 + 1] & 255)) {
                    if (0 < s4 && (T[s4 - 1] & 255) < c02) {
                        s4 ^= -1;
                    }
                    if (c02 == c2) {
                        t++;
                        SA[t] = s4;
                    } else {
                        if (c2 != -1) {
                            bucketA[c2] = t;
                        }
                        c2 = c02;
                        int i4 = bucketA[c02] + 1;
                        t = i4;
                        SA[i4] = s4;
                    }
                }
            } else {
                s1 ^= -1;
            }
            if (s1 == 0) {
                SA[i3] = T[n - 1];
                orig = i3;
            } else {
                SA[i3] = T[s1 - 1];
            }
        }
        return orig;
    }

    public int bwt() {
        int[] SA = this.SA;
        byte[] T = this.T;
        int n = this.n;
        int[] bucketA = new int[256];
        int[] bucketB = new int[65536];
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            SA[0] = T[0];
            return 0;
        }
        int m = sortTypeBstar(bucketA, bucketB);
        if (0 < m) {
            return constructBWT(bucketA, bucketB);
        }
        return 0;
    }
}
