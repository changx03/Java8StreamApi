import java.util.Arrays;
import java.util.List;

/**
 * Created by Luke Chang on 27-Dec-16.
 */
public class StreamReduce {

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

    private List<Person> persons;

    public StreamReduce() {
        this.persons = Arrays.asList(
                new Person("Max", 18),
                new Person("Peter", 23),
                new Person("Pamela", 23),
                new Person("David", 12));


//        reduceExample1();
//        reduceExample2();
//        reduceExample3();
        reduceExample4();
        System.out.println();
        reduceParallelStream();
    }

    public void reduceExample1() {
        persons.stream()
                .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)  // reduce(accumulator)
                .ifPresent(System.out::println);

        // Do not alter the original object
        persons.forEach(System.out::println);
    }

    public void reduceExample2() {
        Person result = persons.stream()
                .reduce(new Person("", 0), (p1, p2) -> {    // reduce(identity, accumulator)
                    p1.age += p2.age;
                    p1.name += p2.name;
                    return p1;
                });

        System.out.format("name = %s; age = %s", result.name, result.age);
        // name = MaxPeterPamelaDavid; age = 76
    }

    public void reduceExample3() {
        int ageSum = persons.stream()
                .reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);  // reduce(identity, accumulator, combiner)
        System.out.println(ageSum);
    }

    public void reduceExample4() {
        int ageSum = persons.stream()
                .reduce(0, (sum, p) -> {
                            System.out.format("accumulator: sum = %s; person = %s\n", sum, p);
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("Combiner: sum1 = %s; sum2 = %s\n", sum1, sum2);
                            return sum1 + sum2;
                        });

        // accumulator: sum=0; person=Max
        // accumulator: sum=18; person=Peter
        // accumulator: sum=41; person=Pamela
        // accumulator: sum=64; person=David

        // Note: the combiner is never called.
    }

    public void reduceParallelStream() {
        int ageSum = persons.parallelStream()
                .reduce(0, (sum, p) -> {
                            System.out.format("accumulator: sum = %s; person = %s\n", sum, p);
                            return sum += p.age;
                        },
                        (sum1, sum2) -> {
                            System.out.format("Combiner: sum1 = %s; sum2 = %s\n", sum1, sum2);
                            return sum1 + sum2;
                        });
    }
}
