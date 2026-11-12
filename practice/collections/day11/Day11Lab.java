import java.util.HashMap;
import java.util.Map;

public class Day11Lab {

    public static void main(String[] args) {

        Map<String, Integer> marks =
                new HashMap<>();

        marks.put("Ravi", 80);
        marks.put("Amit", 95);
        marks.put("John", 70);
        marks.put("Kiran", 85);

        System.out.println(
                "Amit Marks : "
                        + marks.get("Amit"));

        System.out.println(
                "Contains Ravi : "
                        + marks.containsKey("Ravi"));

        System.out.println(
                "Contains Arjun : "
                        + marks.containsKey("Arjun"));

        System.out.println(
                "Kiran Marks : "
                        + marks.getOrDefault("Kiran", 0));

        System.out.println(
                "Unknown Marks : "
                        + marks.getOrDefault("Unknown", 0));

        System.out.println(
                "\nAll Entries");

        for (Map.Entry<String, Integer> entry
                : marks.entrySet()) {

            System.out.println(
                    entry.getKey()
                            + " = "
                            + entry.getValue());
        }
    }
}