import java.util.Set;
import java.util.TreeSet;

class Student implements Comparable<Student> {

    private String name;
    private int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(Student other) {

        // First compare scores
        int scoreComparison =
                Integer.compare(this.score,
                                other.score);

        // If scores are different, return result
        if (scoreComparison != 0) {
            return scoreComparison;
        }

        // If scores are same, compare names
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "Student{name='"
                + name
                + "', score="
                + score
                + "}";
    }
}

public class Day9Lab {

    public static void main(String[] args) {

        Set<Student> students =
                new TreeSet<>();

        students.add(
                new Student("Ravi", 80));

        students.add(
                new Student("Amit", 95));

        students.add(
                new Student("John", 70));

        students.add(
                new Student("Kiran", 80));

        students.add(
                new Student("Arjun", 95));

        System.out.println(
                "Students in sorted order:");

        for (Student student : students) {
            System.out.println(student);
        }
    }
}