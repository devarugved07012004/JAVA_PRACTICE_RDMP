import java.util.ArrayDeque;
import java.util.Deque;

public class Main {

    public static void main(String[] args) {

        Deque<String> stack =
                new ArrayDeque<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        System.out.println(
                "Stack : "
                        + stack);

        System.out.println(
                "Peek : "
                        + stack.peek());

        System.out.println(
                "After Peek : "
                        + stack);

        System.out.println(
                "Pop : "
                        + stack.pop());

        System.out.println(
                "After Pop : "
                        + stack);
    }
}