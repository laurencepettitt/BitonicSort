package bitonicSort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class BitonicSort<T> {

    private T[] list;
    private final int offset, length, val2, exp2;
    private final Comparator<T> comp, compRev;

    private class MinPow2{
        int val = 2;
        int exp = 1;
        MinPow2(int min) {
            while (val < min) { val = val << 1; exp++; }
            // now we have: 2^(exp-1) < min <= 2^exp = val
        }
    }

    private BitonicSort(T[] list, int offset, int length, Comparator<T> comparator) {
        this.list = list;
        this.offset = offset; this.length = length;
        this.comp = comparator; this.compRev = comparator.reversed();
        MinPow2 minPow2 = new MinPow2(length);
        val2 = minPow2.val; exp2 = minPow2.exp;
    }

    private void compare(int i, int j, boolean dir) {
        if (comp.compare(list[i], list[j]) > 0 == dir)
            exchange(i, j);
    }

    private void exchange(int i, int j) {
        T t = list[i];
        list[i] = list[j];
        list[j] = t;
    }


    // Finds largest integer which is a power of two and less than n.
    private int largestPossiblePowerOfTwo(int n)
    {
        int k=1;
        while (k>0 && k<n)
            k=k<<1;
        return k>>>1;
    }

    // Merges a bitonic sequence, of length count << 1, starting at offset,
    // into a sorted sequence of length (count << 1)
    private void bitonicMerge(int offset, int count, boolean dir) {
        if (count <= 1) return;

        int n = largestPossiblePowerOfTwo(count);

        for (int i = offset; i < offset + count - n; ++i) {
            compare(i, i + n, dir);
        }

        bitonicMerge(offset, n, dir);
        bitonicMerge(offset + n, count - n, dir);
    }

    private void bitonicSort(int offset, int count, boolean dir) {

        if (count <= 1) return;

        int n = count / 2;

        bitonicSort(offset, n, !dir);
        bitonicSort(offset + n, count - n, dir);

        bitonicMerge(offset, count, dir);
    }

    private static <T> void bitonicSort(T[] list, Comparator<T> comp) {

        final int length = list.length;

        if (length == 0 || length == 1) return;

        BitonicSort<T> bitonicSort = new BitonicSort<T>(list, 0, length, comp);

        bitonicSort.bitonicSort(0, length, true);

    }

    public static <T> void sort(T[] list, Comparator<? super T> comp) {
        assert comp != null;
        bitonicSort(list, comp);
    }

    public static <T extends Comparable<? super T>> void sort(T[] list) { sort(list, Comparator.<T>naturalOrder()); }

    public static <T> void sort(List<T> list, Comparator<? super T> c) {
        assert c != null;
        Object[] a = list.toArray();
        sort(a, (Comparator)c);
        ListIterator<T> i = list.listIterator();
        for (Object t : a) {
            i.next();
            i.set((T) t);
        }
    }

    public static <T extends Comparable<? super T>> void sort(List<T> list) { sort(list, Comparator.<T>naturalOrder()); }
}
