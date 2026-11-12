import java.util.*;

class Student {

    private String name;
    private int marks;

    public Student(
            String name,
            int marks)
    {
        this.name = name;
        this.marks = marks;
    }

    public int getMarks()
    {
        return marks;
    }

    @Override
    public String toString()
    {
        return name +
                "(" +
                marks +
                ")";
    }
}

public class Day12Grouping {

    public static void main(String[] args) {

        List<Student> students =
                new ArrayList<>();

        students.add(
                new Student("Ravi",85));

        students.add(
                new Student("Amit",92));

        students.add(
                new Student("Kiran",75));

        students.add(
                new Student("John",65));

        Map<String,
                List<Student>> groups =
                new HashMap<>();

        for(Student student : students)
        {
            String grade;

            if(student.getMarks() >= 80)
            {
                grade = "A";
            }
            else if(student.getMarks() >= 70)
            {
                grade = "B";
            }
            else
            {
                grade = "C";
            }

            groups.putIfAbsent(
                    grade,
                    new ArrayList<>());

            groups.get(grade)
                    .add(student);
        }

        System.out.println(groups);
    }
}