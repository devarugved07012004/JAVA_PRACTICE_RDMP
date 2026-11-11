import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println(
                "=== LINKED LIST ===");

        List<String> names =
                new LinkedList<>();

        names.add("Ravi");
        names.add("Amit");
        names.add("John");

        System.out.println(names);

        System.out.println(
                "Get Index 1 : "
                        + names.get(1));

        names.remove(0);

        System.out.println(
                names);

        System.out.println(
                "\n=== DEQUE ===");

        Deque<String> deque =
                new LinkedList<>();

        deque.addFirst("B");
        System.out.println(deque);

        deque.addFirst("A");
        System.out.println(deque);

        deque.addLast("C");
        System.out.println(deque);

        deque.addLast("D");
        System.out.println(deque);

        deque.pollFirst();
        System.out.println(deque);

        deque.pollLast();
        System.out.println(deque);
    }
}