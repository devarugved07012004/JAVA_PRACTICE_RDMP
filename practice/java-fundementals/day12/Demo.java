public class Demo {

    // Overloaded method for 2 numbers
    static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Overloaded method for 3 numbers
    static int max(int a, int b, int c) {
        return max(max(a, b), c);
    }

    // Method to demonstrate call-by-value
    static void changeValue(int num) {
        System.out.println("Inside method (before change): " + num);

        num = 999; // changes only local copy

        System.out.println("Inside method (after change): " + num);
    }

    public static void main(String[] args) {

        // Calling overloaded methods
        System.out.println("Max of 10 and 20 = " + max(10, 20));
        System.out.println("Max of 10, 20 and 15 = " + max(10, 20, 15));

        // Call-by-value demonstration
        int x = 50;

        System.out.println("\nBefore method call: x = " + x);

        changeValue(x);

        System.out.println("After method call: x = " + x);
    }
}