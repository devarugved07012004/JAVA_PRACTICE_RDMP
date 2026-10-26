import java.util.Scanner;

public class SafeInput {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read name
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        int age;

        // Keep asking until a valid age is entered
        while (true) {
            System.out.print("Enter your age: ");
            String input = sc.nextLine();

            try {
                age = Integer.parseInt(input);

                if (age < 0) {
                    System.out.println("Invalid number, try again");
                    continue;
                }

                break; // valid age
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, try again");
            }
        }

        System.out.println("Hello, " + name + " (" + age + ")");

        sc.close();
    }
}