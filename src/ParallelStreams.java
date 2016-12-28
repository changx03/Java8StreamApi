import javafx.animation.ParallelTransition;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * Created by Luke Chang on 28-Dec-16.
 */
public class ParallelStreams {

    class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public ParallelStreams() {
        // # of threads
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println(commonPool.getParallelism());

//        parallelExample1();
//        parallelSort();
        parallelReduce();
    }

    // the same thread works on the same object
    public void parallelExample1() {
        Arrays.asList("a1", "a2", "b1", "c2", "c1").parallelStream()
                .filter(s -> {
                    System.out.format("filter: %s [%s]\n", s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .forEach(s -> System.out.format("forEach: %s [%s]\n",
                        s, Thread.currentThread().getName()));
        //filter: b1 [main]
        //filter: c2 [ForkJoinPool.commonPool-worker-4]
        //filter: a1 [ForkJoinPool.commonPool-worker-3]
        //filter: a2 [ForkJoinPool.commonPool-worker-1]
        //map: a2 [ForkJoinPool.commonPool-worker-1]
        //filter: c1 [ForkJoinPool.commonPool-worker-2]
        //map: c1 [ForkJoinPool.commonPool-worker-2]
        //forEach: A2 [ForkJoinPool.commonPool-worker-1]
        //map: a1 [ForkJoinPool.commonPool-worker-3]
        //map: c2 [ForkJoinPool.commonPool-worker-4]
        //map: b1 [main]
        //forEach: C2 [ForkJoinPool.commonPool-worker-4]
        //forEach: A1 [ForkJoinPool.commonPool-worker-3]
        //forEach: C1 [ForkJoinPool.commonPool-worker-2]
        //forEach: B1 [main]
    }

    // parallelStream().sort() in Java8 uses Arrays.parallelSort() under the hood. However it only uses main thread to print.
    public void parallelSort() {

        List<String> result = Arrays.asList("a1", "a2", "b1", "c2", "c1")
                .parallelStream()
                .filter(s -> {
                    System.out.format("filter: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.format("map: %s [%s]\n",
                            s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .sorted((s1, s2) -> {
                    System.out.format("sort: %s <> %s [%s]\n",
                            s1, s2, Thread.currentThread().getName());
                    return s1.compareTo(s2);
                })
                .collect(Collectors.toList());
        System.out.println(result);
    }


    // Both accumulator and combiner are executed in parallel, and there is no sync issues.
    public void parallelReduce() {
        List<Person> persons = Arrays.asList(
                new Person("Max", 18),
                new Person("Peter", 23),
                new Person("Pamela", 23),
                new Person("David", 12));

        int ageSum = persons.parallelStream()
                .reduce(0, (sum, p) -> {
                            System.out.format("accumulator: sum = %s; person = %s [%s]\n",
                                    sum, p, Thread.currentThread().getName());
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("combiner: sum1 = %s; sum2 = %s [%s]\n",
                                    sum1, sum2, Thread.currentThread().getName());
                            return sum1 + sum2;
                        });
        System.out.println(ageSum);

    }
}
