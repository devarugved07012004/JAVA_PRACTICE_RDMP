public class LoopsLab {

    public static void main(String[] args) {

        int n = 10;

        // Sum using For Loop
        System.out.println("For Loop Sum: " + sumFor(n));

        // Sum using While Loop
        System.out.println("While Loop Sum: " + sumWhile(n));

        // Sum using Do-While Loop
        System.out.println("Do-While Loop Sum: " + sumDoWhile(n));

        System.out.println();

        // Print 5 stars
        System.out.println("Star Pattern:");

        for (int i = 1; i <= 5; i++) {
            System.out.print("*");
        }

        System.out.println("\n");

        // Break Demo
        System.out.println("Break Demo:");

        for (int i = 1; i <= 20; i++) {

            if (i == 7) {
                break;
            }

            System.out.println(i);
        }
    }

    // Sum using For Loop
    public static int sumFor(int n) {

        int sum = 0;

        for (int i = 1; i <= n; i++) {
            sum += i;
        }

        return sum;
    }

    // Sum using While Loop
    public static int sumWhile(int n) {

        int sum = 0;
        int i = 1;

        while (i <= n) {
            sum += i;
            i++;
        }

        return sum;
    }

    // Sum using Do-While Loop
    public static int sumDoWhile(int n) {

        int sum = 0;
        int i = 1;

        do {
            sum += i;
            i++;
        } while (i <= n);

        return sum;
    }
}