import java.util.ArrayDeque;
import java.util.Queue;

public class Day15Lab {

    public static void main(String[] args) {

        Queue<String> tasks =
                new ArrayDeque<>();

        tasks.offer("Task-1");
        tasks.offer("Task-2");
        tasks.offer("Task-3");

        System.out.println(
                "Queue : " + tasks);

        System.out.println(
                "Peek : "
                        + tasks.peek());

        System.out.println(
                "After Peek : "
                        + tasks);

        System.out.println(
                "Poll : "
                        + tasks.poll());

        System.out.println(
                "After Poll : "
                        + tasks);

        System.out.println(
                "Poll : "
                        + tasks.poll());

        System.out.println(
                "After Poll : "
                        + tasks);
    }
}