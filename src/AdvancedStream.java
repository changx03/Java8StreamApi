import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Luke Chang on 27-Dec-16.
 */
public class AdvancedStream {
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

    List<Person> persons;

    public AdvancedStream() {
        persons = Arrays.asList(
                new Person("Max", 18),
                new Person("Peter", 23),
                new Person("Pamela", 23),
                new Person("Nico", 23),
                new Person("David", 12));

        List<Person> filtered = persons.stream()
                .filter(p -> p.name.startsWith("P"))
                .collect(Collectors.toList());
        System.out.println(filtered);

        // groups all person by age
        Map<Integer, List<Person>> personsByAge = persons.stream()
                .collect(Collectors.groupingBy(p -> p.age));
        personsByAge.forEach((age, p) -> System.out.format("age %s: %s\n", age, p));

        // average
        double averageAge = persons.stream()
                .collect(Collectors.averagingDouble(p -> p.age));
        System.out.println(averageAge);

        // Statistics summary: IntSummaryStatistics, DoubleSummaryStatistics
        IntSummaryStatistics ageSummary = persons.stream()
                .collect(Collectors.summarizingInt(p -> p.age));
        System.out.println(ageSummary);
        DoubleSummaryStatistics ageSummaryD = persons.stream()
                .collect(Collectors.summarizingDouble(p -> p.age));
        System.out.println(ageSummaryD);

        // Joins all objects into a string
        String pStr = persons.stream()
                .filter(p -> p.age >= 18)
                .map(p -> p.name)
                .collect(Collectors.joining(" , ", "In Germany ", " are of legal age."));
        System.out.println(pStr);
        // Collectors.joining(delimiter, prefix, suffix)
        // In Germany Max and Peter and Pamela are of legal age.

        // Transform the stream elements into a map
        // If the Key isn't unique, IllegalStateException. Avoid exception by passing merge
        Map<Integer, String> pMap = persons
                .stream()
                .collect(Collectors.toMap(
                        p -> p.age,
                        p -> p.name,
                        (name1, name2) -> name1 + ";" + name2));
        System.out.println(pMap);
        // {18=Max, 23=Peter;Pamela;Nico, 12=David}

        // Collector
        Collector<Person, StringJoiner, String> personNameCollector = Collector.of(
                () -> new StringJoiner(", "),   // supplier
                (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
                (j1, j2) -> j1.merge(j2),   // combiner
                StringJoiner::toString);    // finisher
        String names = persons.stream()
                .collect(personNameCollector);
        System.out.println(names);
    }
}
