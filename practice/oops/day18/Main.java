import java.util.ArrayList;
import java.util.List;

public class Main {

    // =========================
    // Association
    // =========================
    static class Student {
        String name;

        Student(String name) {
            this.name = name;
        }
    }

    static class Teacher {
        String name;
        Student student; // Teacher knows Student

        Teacher(String name) {
            this.name = name;
        }

        void setStudent(Student student) {
            this.student = student;
        }

        void showStudent() {
            System.out.println(name + " teaches " + student.name);
        }
    }

    // =========================
    // Aggregation
    // =========================
    static class Department {
        List<Teacher> teachers;

        Department(List<Teacher> teachers) {
            this.teachers = teachers;
        }

        void showTeachers() {
            System.out.println("Department Teachers:");
            for (Teacher t : teachers) {
                System.out.println("- " + t.name);
            }
        }
    }

    // =========================
    // Composition
    // =========================
    static class Room {
        String type;

        Room(String type) {
            this.type = type;
        }
    }

    static class House {
        Room[] rooms;

        House() {
            // House creates its own rooms
            rooms = new Room[] {
                new Room("Bedroom"),
                new Room("Kitchen"),
                new Room("Hall")
            };
        }

        void showRooms() {
            System.out.println("House Rooms:");
            for (Room r : rooms) {
                System.out.println("- " + r.type);
            }
        }
    }

    public static void main(String[] args) {

        // =====================================
        // Association
        // =====================================
        System.out.println("=== Association ===");

        Student s = new Student("Rugved");
        Teacher t = new Teacher("Nitin");

        t.setStudent(s);

        t.showStudent();

        System.out.println();

        // =====================================
        // Aggregation
        // =====================================
        System.out.println("=== Aggregation ===");

        Teacher t1 = new Teacher("Ajinkya");
        Teacher t2 = new Teacher("Prafull");

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(t1);
        teachers.add(t2);

        Department dept = new Department(teachers);

        dept.showTeachers();

        System.out.println();

        // =====================================
        // Composition
        // =====================================
        System.out.println("=== Composition ===");

        House house = new House();

        house.showRooms();
    }
}