import java.util.Scanner;

public class MenuApp {

    public static void add() {
        System.out.println("add stub");
    }

    public static void list() {
        System.out.println("list stub");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("\n1) Add");
            System.out.println("2) List");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    add();
                    break;

                case "2":
                    list();
                    break;

                case "3":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }

        sc.close();
    }
}