package bitonicSort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class BitonicSort {

    private static final <T> void swap(T[] list, int i, int j) {
        T t = list[i];
        list[i] = list[j];
        list[j] = t;
    }

    private static <T> void bitonicSort(T[] list, Comparator<? super T> comp) {
//        Arrays.sort(list, (Comparator) asc);
//        Comparator<? super T> desc = comp.reversed();

        final int length = list.length;

        if (length == 0 || length == 1) return;
        int d = 1, size = 2;
        while (size < length) { size = size << 1; d++; }
        // now 2^(d-1) < length <= 2^d

        for (int i = 1; )


        // i is the phase count, cnt is comparison gap 2^i, mask has lower i + 1 bits set to 1 as a bitwise mask to calculate modulo cnt
        for (int i = 0, cnt = 1, mask = 1; true; i++, cnt = cnt << 1, mask = (mask << 1) + 1) {
            if (i == 0) continue; // start with i = 1, cnt = 2, mask = 00000000000000000000000000000011b

            for (int stepSize = cnt; stepSize > 1; stepSize = stepSize >> 1) {


                for (int step = 0; step < stepSize; ++step) {
                    for (int pos = 0; pos < length - stepSize; pos += stepSize) { // TODO - improve iterator calculations
                        // i = 2, cnt = 4, mask = 0111b


                        // compare a[step] with a[step + stepSize]
                    }
                }
            }

            for (int j = 0; j < length - 1; j = j + 2) {
                int res = comp.compare(list[j], list[j+1]);
                if (res == 0) continue;
                boolean lt = res < 0;
                if ( ((j & mask) < cnt) ^ lt ) {
                    swap(list, j, j+1);
                }
            }
            if (cnt >= length) break;
        }
    }

    public static <T> void sort(T[] list, Comparator<? super T> c) {
        // TODO - assertions, like in Arrays.TimSort
        assert c != null;
        bitonicSort(list, c);
    }

    public static <T extends Comparable<? super T>> void sort(T[] list) { sort(list, Comparator.<T>naturalOrder()); }

//    public static <T> void sort(List<T> list, Comparator<? super T> c) {
//        Object[] a = list.toArray();
//        sort(a, (Comparator)c);
//        ListIterator<T> i = list.listIterator();
//        for (Object t : a) {
//            i.next();
//            i.set((T) t);
//        }
//    }
//
//    public static <T extends Comparable<? super T>> void sort(List<T> list) { sort(list, null); }

    static <T> void print(T[] list) {
        for (T t : list) {
            System.out.print(t + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] a = {5, 1, 7, 8, 2};
        print(a);
        sort(a);
        print(a);
    }
}
