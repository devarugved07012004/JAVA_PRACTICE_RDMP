import java.util.LinkedHashSet;
import java.util.Set;

public class Day7Lab {

    public static void main(String[] args) {

        String sentence =
                "java spring java collections spring java";

        String[] words =
                sentence.split("\\s+");

        Set<String> uniqueWords =
                new LinkedHashSet<>();

        for(String word : words)
        {
            uniqueWords.add(word);
        }

        System.out.println(
                "Original Count : "
                        + words.length);

        System.out.println(
                "Unique Count : "
                        + uniqueWords.size());

        System.out.println(
                uniqueWords);
    }
}