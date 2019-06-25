package tests;

import java.util.*;
import java.util.stream.Collectors;
import bitonicSort.BitonicSort;

interface ITest {
    boolean test();
}

/**
 * Provides a number of methods which run tests against the BitonicSort class.
 * Tests include integration tests and performance tests.
 */
public class Tests {

    /**
     * Checks whether given list is sorted according to comparator comp
     * @param list  List to check if sorted
     * @param comp  Comparator to define order
     * @param <T>
     * @return      Whether list is sorted
     */
    private static <T> boolean isSorted(List<T> list, Comparator<T> comp) {
        if (list == null || list.size() <= 1) return true;
        for (int i = 1; i < list.size(); i++) {
            if (comp.compare(list.get(i-1), list.get(i)) > 0)
                return false;
        }
        return true;
    }

    /**
     * Sorts given list using BitonicSort class and returns true if BitonicSort sorted list successfully
     * @param list  List to sort
     * @param comp  Comparator to define order
     * @param <T>
     * @return      Whether list was sorted successfully by BitonicSort
     */
    private static <T> boolean sortsList(List<T> list, Comparator<T> comp) {
        BitonicSort.sort(list, comp);
        return isSorted(list, comp);
    }


    /**
     * Test
     * @return Whether sorts integer list of length which is not a power of two.
     */
    private static boolean testSortsIntegerList() {
        List<Integer> list = new ArrayList<>();
        list.add(65);
        list.add(23);
        list.add(89);
        list.add(1);
        list.add(555555555);
        return sortsList(list, Comparator.naturalOrder());
    }

    /**
     * Test
     * @return  Whether sorts random integer list of length 10.
     */
    private static boolean testSortsRandomIntegerList() {
        List<Integer> list = new Random().ints(10, 0, 200).boxed().collect(Collectors.toList());
        return sortsList(list, Comparator.naturalOrder());
    }

    /**
     * Test
     * @return Whether sorts random integer list of length 10, in reverse order.
     */
    private static boolean testSortsReverseRandomIntegerList() {
        List<Integer> list = new Random().ints(8, Integer.MIN_VALUE, Integer.MAX_VALUE).boxed().collect(Collectors.toList());
        return sortsList(list, Comparator.reverseOrder());
    }

    /**
     * Test
     * Performance tests to compare speed of sorting a random integer list of length 1000
     * with bitonicSort.BitonicSort.sort vs java.util.List.sort
     */
    private static void performanceTests() {
        String format = " %-50.50s %-10.10s%n";
        List<Integer> list = new Random().ints(10000, Integer.MIN_VALUE, Integer.MAX_VALUE).boxed().collect(Collectors.toList());
        long startTime = System.nanoTime();
        BitonicSort.sort(list, Comparator.naturalOrder());
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.printf(format, "BitonicSort: ", duration);

        list = new Random().ints(10000, Integer.MIN_VALUE, Integer.MAX_VALUE).boxed().collect(Collectors.toList());
        startTime = System.nanoTime();
        list.sort(Comparator.naturalOrder());
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        System.out.printf(format, "MergeSort: ", duration);
    }

    /**
     * Run all Test
     * @param args ignored
     */
    public static void main(String[] args) {
        Map<String, ITest> tests = new HashMap<String, ITest>();
        tests.put("SortsIntegerList", Tests::testSortsIntegerList);
        tests.put("SortsRandomIntegerList", Tests::testSortsRandomIntegerList);
        tests.put("SortsReverseRandomIntegerList", Tests::testSortsReverseRandomIntegerList);

        System.out.println(("Tests: "));
        for (Map.Entry<String, ITest> test :
                tests.entrySet()) {
            System.out.printf(
                    " %-50.50s %-7.7s%n",
                    test.getKey(),
                    (test.getValue().test() ? "success" : "fail")
            );
        }

        System.out.println();

        System.out.println("Performance (nanoseconds): ");
        performanceTests();
    }
}
