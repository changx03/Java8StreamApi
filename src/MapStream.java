import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Luke Chang on 27-Dec-16.
 */
public class MapStream {

    public static void streamExample1() {
        List<String> list = Arrays.asList("a1", "a2", "b1", "c3", "c1", "c2");

        // operation inside map()
        list.stream()
                .filter(p -> p.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);

        // The 2 expressions below are identical.
        Arrays.asList("a1", "a2", "a3")
                .stream()
                .findFirst()
                .ifPresent(System.out::println);  // a1

        Stream.of("a1", "a2", "a3")
                .findFirst()
                .ifPresent(System.out::println);  // a1

        // int, long and double have their Stream classes, IntStream, LongStream and DoubleStream
        IntStream.range(1, 4)
                .forEach(System.out::println);


        // operations with stream()
        Arrays.stream(new int[]{1, 2, 3})
                .map(n -> 2 * n + 1)
                .average()
                .ifPresent(System.out::println);  // 5.0

        List<Integer> in = Arrays.asList(1, 2, 3);
        List<Integer> collect = in.stream()
                .map(n -> 2 * n + 1)
                .collect(Collectors.toList());  // 5.0
        System.out.println(collect);

        // mapToInt() and mapToLong()
        List<Integer> collect2 = new ArrayList<>();
        Stream.of("a1", "a2", "a3")
                .map(m -> m.substring(1))
                .mapToInt(Integer::parseInt)
                .forEach(collect2::add);
        System.out.println(collect2);   // 1, 2 , 3

        Stream.of("a1", "a2", "a3")
                .map(s -> s.substring(1))
                .mapToInt(Integer::parseInt)
                .max()
                .ifPresent(System.out::println);  // 3

        // from IntSteam to object via mapToObj()
        IntStream.range(1, 4)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);  // a1, a2, a3

        Stream.of(1.0, 2.0, 3.0)
                .mapToInt(Double::intValue)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);  // a1, a2, a3
    }

    public static void processStream() {
        System.out.println("Inside filter()");
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                }); // does not print anthing
        System.out.println();

        System.out.println("Inside filter() and forEach()");
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                })
                .forEach(s -> System.out.println("forEach: " + s)); // prints both
        System.out.println();

        System.out.println("anyMatch() example: ");
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("A");
                }); // stops at A2
    }

    public static void streamOrder() {
        System.out.println("stream() order example1: ");
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("A");
                })
                .forEach(s -> System.out.println("forEach: " + s));
        System.out.println();
        // Outputs:
//        map: d2
//        filter: D2
//        map: a2
//        filter: A2
//        forEach: A2
//        map: b1
//        filter: B1
//        map: b3
//        filter: B3
//        map: c
//        filter: C

    }

    public static void streamOrder2() {
        System.out.println("stream() order example2: ");
        Stream.of("d2", "a2", "b1", "b3", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));
        System.out.println();
    }
    // filter:  d2
    // filter:  a2
    // map:     a2
    // forEach: A2
    // filter:  b1
    // filter:  b3
    // filter:  c

    public static void streamOrder3() {
        // Sorting cannot be break down!
        System.out.println("stream() order example3: ");
        Stream.of("d2", "a2", "b1", "b3", "c")
                .sorted((s1, s2) -> {   // sorting first
                    System.out.printf("sort: %s; %s\n", s1, s2);
                    return s1.compareTo(s2);
                })
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.startsWith("a");
                })
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.println("forEach: " + s));
        System.out.println();
//        sort:    a2; d2
//        sort:    b1; a2
//        sort:    b1; d2
//        sort:    b1; a2
//        sort:    b3; b1
//        sort:    b3; d2
//        sort:    c; b3
//        sort:    c; d2
//        filter:  a2
//        map:     a2
//        forEach: A2
//        filter:  b1
//        filter:  b3
//        filter:  c
//        filter:  d2
    }

    /**
     * Stream() cannot be resued!
     */
    public static void streamReuse() {
        Stream<String> stream =
                Stream.of("d2", "a2", "b1", "b3", "c")
                        .filter(s -> s.startsWith("a"));

        stream.anyMatch(s -> true);    // ok
        stream.noneMatch(s -> true);   // exception
    }

    /**
     * Use Supplier to construct a new stream
     */
    public static void streamSupplier() {
        Supplier<Stream<String>> streamSupplier =
                () -> Stream.of("d2", "a2", "b1", "b3", "c")
                        .filter(s -> s.startsWith("a"));

        streamSupplier.get().anyMatch(s -> true);   // ok
        streamSupplier.get().noneMatch(s -> true);  // ok
    }

    public static void main(String[] args) {
//        streamExample1();   // C1, C2, C3
//        processStream();
//        streamOrder();
//        streamOrder2();
//        streamOrder3();
//        streamReuse();
//        streamSupplier();

//        AdvancedStream myClass = new AdvancedStream();
//        StreamFlatMap myClass2 = new StreamFlatMap();

//        StreamReduce myClass3 = new StreamReduce();
        ParallelStreams myClass4 = new ParallelStreams();
    }
}
