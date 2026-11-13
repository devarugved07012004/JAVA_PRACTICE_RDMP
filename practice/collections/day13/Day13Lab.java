import java.util.Map;
import java.util.TreeMap;

public class Day13Lab {

    public static void main(String[] args) {

        TreeMap<String,Integer> marks =
                new TreeMap<>();

        marks.put("Ravi",80);
        marks.put("John",70);
        marks.put("Amit",95);
        marks.put("Kiran",85);

        System.out.println(
                "Map : " + marks);

        System.out.println(
                "First Key : "
                        + marks.firstKey());

        System.out.println(
                "Last Key : "
                        + marks.lastKey());

        System.out.println(
                "Higher than John : "
                        + marks.higherKey("John"));

        System.out.println(
                "Lower than John : "
                        + marks.lowerKey("John"));

        System.out.println(
                "SubMap : "
                        + marks.subMap(
                                "Amit",
                                "Ravi"));
    }
}