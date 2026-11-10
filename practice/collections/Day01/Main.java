import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<String> names =
                new ArrayList<>();

        System.out.println(
                "Initial Size : "
                        + names.size());

        names.add("Ravi");

        System.out.println(names);
        System.out.println(names.size());

        names.add("Amit");

        System.out.println(names);
        System.out.println(names.size());

        names.add("John");

        System.out.println(names);
        System.out.println(names.size());

        System.out.println(
                "First Name : "
                        + names.get(0));

        names.set(1, "Priya");

        System.out.println(
                "After Update : "
                        + names);

        names.remove(0);

        System.out.println(
                "After Remove : "
                        + names);

        System.out.println(
                "Final Size : "
                        + names.size());

        System.out.println(
                "\nLoop Output");

        for(String name : names)
        {
            System.out.println(name);
        }
    }
}