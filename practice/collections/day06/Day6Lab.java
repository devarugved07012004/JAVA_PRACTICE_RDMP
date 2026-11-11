import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day6Lab {

    public static void main(String[] args) {

        List<String> names =
                new ArrayList<>();

        names.add("Ravi");
        names.add("Amit");
        names.add("Arjun");
        names.add("John");

        Iterator<String> it =
                names.iterator();

        while(it.hasNext())
        {
            String name =
                    it.next();

            if(name.startsWith("A"))
            {
                it.remove();
            }
        }

        System.out.println(names);
    }
}