import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Integer> nums =
                new ArrayList<>();

        nums.add(10);
        nums.add(20);
        nums.add(30);

        System.out.println(
                "Numbers = " + nums);

        System.out.println(
                "First Number = "
                        + nums.get(0));

        System.out.println(
                "Size = "
                        + nums.size());

        List<String> names =
                new ArrayList<>();

        names.add("Ravi");
        names.add("Amit");

        System.out.println(
                "Names = "
                        + names);

        for (String name : names) {
            System.out.println(name);
        }
    }
}