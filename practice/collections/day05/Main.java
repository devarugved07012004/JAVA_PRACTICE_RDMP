import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println(
                "Student Records");

        List<String> students =
                new ArrayList<>();

        students.add("Ravi");
        students.add("Amit");
        students.add("John");

        System.out.println(
                students.get(1));

        System.out.println(
                "\nTicket Queue");

        Deque<String> queue =
                new ArrayDeque<>();

        queue.addLast("Ticket1");
        queue.addLast("Ticket2");
        queue.addLast("Ticket3");

        System.out.println(
                queue.pollFirst());

        System.out.println(
                queue);

        System.out.println(
                "\nBrowser History");

        Deque<String> history =
                new ArrayDeque<>();

        history.push("Google");
        history.push("YouTube");
        history.push("GitHub");

        System.out.println(
                history.pop());

        System.out.println(
                history);
    }
}