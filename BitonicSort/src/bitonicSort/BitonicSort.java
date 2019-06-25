package bitonicSort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * Sorts a sequence of values of type T using the bitonic sort algorithm.
 * Comparator class may be given as a parameter
 * or, if the class supports it, then default comparator may be used instead.
 */
public class BitonicSort<T> {

    private T[] list;
    private final int offset, length, val2, exp2;
    private final Comparator<T> comp, compRev;

    /**
     * Helper class to find the minimum power of two greater than or equal to a given integer.
     */
    private class MinPow2{
        int val = 2;
        int exp = 1;

        /**
         * After execution, we have: 2^(exp-1) < min <= 2^exp = val
         * @param min Number for which we would like to find the minimum power of two greater than or equal to.
         */
        MinPow2(int min) {
            while (val < min) { val = val << 1; exp++; }
        }
    }

    /**
     * Constructor for BitonicSort.
     * Sets up basic fields.
     * @param list          List to be sorted
     * @param offset        Offset to start in list
     * @param length        Length of section in list to be sorted
     * @param comparator    Comparotar of elements in list
     */
    private BitonicSort(T[] list, int offset, int length, Comparator<T> comparator) {
        this.list = list;
        this.offset = offset; this.length = length;
        this.comp = comparator; this.compRev = comparator.reversed();
        MinPow2 minPow2 = new MinPow2(length);
        val2 = minPow2.val; exp2 = minPow2.exp;
    }

    /**
     * Compare value in list at position i with value at position j in direction dir.
     * @param i     Position in list
     * @param j     Position in list
     * @param dir   Direction of comparison, i.e. boolean value representing either 'up' or 'down'
     */
    private void compare(int i, int j, boolean dir) {
        if (comp.compare(list[i], list[j]) > 0 == dir)
            exchange(i, j);
    }

    /**
     * Swap value in list at position i with value in list at position j
     * where (0 <= i,j <= list.length) and (i != j).
     * @param i Position in list
     * @param j Position in list
     */
    private void exchange(int i, int j) {
        T t = list[i];
        list[i] = list[j];
        list[j] = t;
    }


    /**
     * Finds largest integer which is a power of two and less than n.
     * @param   n   Integer for which the return value will be the largest power of two less than.
     * @return      Largest integer which is a power of two and less than n.
     */
    private int largestPossiblePowerOfTwo(int n)
    {
        int k=1;
        while (k>0 && k<n)
            k=k<<1;
        return k>>>1;
    }

    /**
     * Merges a bitonic sequence, of length count << 1, starting at offset,
     * into a sorted sequence of length (count << 1)
     * @param offset    Offset from beginning of list
     * @param count     Length of sequence merge will be performed on
     * @param dir       Direction of comparison
     */
    private void bitonicMerge(int offset, int count, boolean dir) {
        if (count <= 1) return;

        int n = largestPossiblePowerOfTwo(count);

        for (int i = offset; i < offset + count - n; ++i) {
            compare(i, i + n, dir);
        }

        bitonicMerge(offset, n, dir);
        bitonicMerge(offset + n, count - n, dir);
    }

    /**
     * Recursive function to sort sequence, of length count, starting at offset, in direction dir.
     * @param offset    Offset from beginning of list
     * @param count     Length of sequence sort will be performed on
     * @param dir       Direction of comparison
     */
    private void bitonicSort(int offset, int count, boolean dir) {

        if (count <= 1) return;

        int n = count / 2;

        bitonicSort(offset, n, !dir);
        bitonicSort(offset + n, count - n, dir);

        bitonicMerge(offset, count, dir);
    }


    /**
     * Sorts entire sequence list of type T using comparator comp.
     * @param list  Array to be sorted
     * @param comp  Comparator for comparisons of types in list
     * @param <T>   Type of elements in list
     */
    private static <T> void bitonicSort(T[] list, Comparator<T> comp) {

        final int length = list.length;

        if (length == 0 || length == 1) return;

        BitonicSort<T> bitonicSort = new BitonicSort<T>(list, 0, length, comp);

        bitonicSort.bitonicSort(0, length, true);

    }

    /**
     * Sorts given array of type T using given comparator.
     * When the function finishes, given array will be sorted.
     * @param list  Array to be sorted
     * @param comp  Comparator class to use for comparisons
     */
    public static <T> void sort(T[] list, Comparator<? super T> comp) {
        assert comp != null;
        bitonicSort(list, comp);
    }

    /**
     * Sorts given array of type T using default comparator in natural order.
     * Type T must be extend Comparable.
     * When the function finishes, given array will be sorted.
     * @param list  Array to be sorted
     */
    public static <T extends Comparable<? super T>> void sort(T[] list) { sort(list, Comparator.<T>naturalOrder()); }

    /**
     * Sorts given list of type T using given comparator.
     * When the function finishes, given list will be sorted.
     * @param list  List to be sorted
     * @param comp  Comparator class to use for comparisons
     */
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

    /**
     * Sorts given list of type T using default comparator in natural order.
     * Type T must be extend Comparable.
     * When the function finishes, given list will be sorted.
     * @param list  List to be sorted
     */
    public static <T extends Comparable<? super T>> void sort(List<T> list) { sort(list, Comparator.<T>naturalOrder()); }
}
