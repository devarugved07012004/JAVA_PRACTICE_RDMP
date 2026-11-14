import java.util.LinkedHashMap;
import java.util.Map;

public class main {

    public static void main(String[] args) {

        Map<String,Integer> report =
                new LinkedHashMap<>();

        report.put("January", 100);
        report.put("February", 120);
        report.put("March", 90);
        report.put("April", 150);

        System.out.println(report);
    }
}