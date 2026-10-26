public class OperatorsLab {

    public static void main(String[] args) {

        // Integer vs Double Division
        System.out.println("5 / 2 = " + (5 / 2));
        System.out.println("5 / 2.0 = " + (5 / 2.0));

        int a = 5;

        // Post Increment
        System.out.println("Before a++ : " + a);
        a++;
        System.out.println("After a++ : " + a);

        // Pre Increment
        a = 5;
        System.out.println("Before ++a : " + a);
        ++a;
        System.out.println("After ++a : " + a);

        // Precedence
        System.out.println("2 + 3 * 4 = " + (2 + 3 * 4));
        System.out.println("(2 + 3) * 4 = " + ((2 + 3) * 4));

        // Short Circuit
        // System.out.println(false && boom());
        // System.out.println(true || boom());

        System.out.println(true && boom());
        System.out.println(false || boom());
    }

    public static boolean boom() {
        System.out.println("boom");
        return true;
    }
}