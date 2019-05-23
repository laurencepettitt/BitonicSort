package tests;

import java.util.*;
import java.util.stream.Collectors;
import bitonicSort.BitonicSort;

interface ITest {
    boolean test();
}

public class Tests {

    private static <T> boolean isSorted(List<T> list, Comparator<T> comp) {
        if (list == null || list.size() <= 1) return true;
        for (int i = 1; i < list.size(); i++) {
            if (comp.compare(list.get(i-1), list.get(i)) > 0)
                return false;
        }
        return true;
    }

    private static <T> boolean sortsList(List<T> list, Comparator<T> comp) {
        BitonicSort.sort(list, comp);
        return isSorted(list, comp);
    }


    private static boolean testSortsIntegerList() {
        List<Integer> list = new ArrayList<>();
        list.add(65);
        list.add(23);
        list.add(89);
        list.add(1);
        list.add(555555555);
        return sortsList(list, Comparator.naturalOrder());
    }

    private static boolean testSortsRandomIntegerList() {
        List<Integer> list = new Random().ints(10, 0, 200).boxed().collect(Collectors.toList());
        return sortsList(list, Comparator.naturalOrder());
    }

    private static boolean testSortsReverseRandomIntegerList() {
        List<Integer> list = new Random().ints(8, Integer.MIN_VALUE, Integer.MAX_VALUE).boxed().collect(Collectors.toList());
        return sortsList(list, Comparator.reverseOrder());
    }

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
