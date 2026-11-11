import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Employee {

    private String name;
    private double salary;

    public Employee(String name,
                    double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return name +
                " : " +
                salary;
    }
}

public class Day10Lab {

    public static void main(String[] args) {

        List<Employee> employees =
                new ArrayList<>();

        employees.add(
                new Employee("Ravi", 50000));

        employees.add(
                new Employee("Amit", 70000));

        employees.add(
                new Employee("John", 50000));

        System.out.println(
                "Original");

        System.out.println(employees);

        employees.sort(

                Comparator
                        .comparingDouble(
                                Employee::getSalary)

                        .thenComparing(
                                Employee::getName)

        );

        System.out.println(
                "\nSalary Then Name");

        System.out.println(employees);

        employees.sort(

                Comparator
                        .comparingDouble(
                                Employee::getSalary)
                        .reversed()

        );

        System.out.println(
                "\nHighest Salary First");

        System.out.println(employees);
    }
}