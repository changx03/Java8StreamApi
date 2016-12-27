import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Luke Chang on 27-Dec-16.
 */

public class StreamFlatMap {
    class Foo {
        String name;
        List<Bar> bars = new ArrayList<>();

        Foo(String name) {
            this.name = name;
        }
    }

    class Bar {
        String name;

        Bar(String name) {
            this.name = name;
        }
    }

    private List<Foo> foos;

    private void createList() {
        // create foos with IntStream()
        IntStream.range(1, 4)
                .forEach(i -> foos.add(new Foo("Foo" + i)));

        // create bars in nested List
        foos.forEach(
                f -> IntStream.range(1, 4)
                        .forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));
    }

    private void createList2() {
        IntStream.range(1, 4)
                .mapToObj(i -> new Foo("Foo" + i))
                .peek(f -> IntStream.range(1, 4)
                        .mapToObj(i -> new Bar("Bar" + i + " <- " + f.name))
                        .forEach(f.bars::add))
                .flatMap(f -> f.bars.stream())
                .forEach(b -> System.out.println(b.name));
    }

    // Using flatMap
    private void printList() {
        foos.stream()
                .flatMap(f -> f.bars.stream())
                .forEach(b -> System.out.println(b.name));
        // Bar1 <- Foo1
        // Bar2 <- Foo1
        // Bar3 <- Foo1
        // Bar1 <- Foo2
        // Bar2 <- Foo2
        // Bar3 <- Foo2
        // Bar1 <- Foo3
        // Bar2 <- Foo3
        // Bar3 <- Foo3
    }
    public StreamFlatMap() {
        this.foos = new ArrayList<>();

        createList();
        printList();

        System.out.println("Example2:");
        createList2();
    }
}
