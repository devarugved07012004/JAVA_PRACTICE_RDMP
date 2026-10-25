public class Decisions {

    public static void main(String[] args) {

        System.out.println("=== Grade Tests ===");

        System.out.println("95 -> " + gradeBand(95));
        System.out.println("82 -> " + gradeBand(82));
        System.out.println("68 -> " + gradeBand(68));

        System.out.println();

        System.out.println("=== Even Tests ===");

        System.out.println("7 -> " + isEven(7));
        System.out.println("8 -> " + isEven(8));
        System.out.println("15 -> " + isEven(15));

        System.out.println();

        System.out.println("=== Leap Year Tests ===");

        System.out.println("2024 -> " + isLeapYear(2024));
        System.out.println("1900 -> " + isLeapYear(1900));
        System.out.println("2000 -> " + isLeapYear(2000));
    }

    public static String gradeBand(int score) {

        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

    public static boolean isEven(int n) {
        if (n % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isLeapYear(int year) {

        if (year % 400 == 0) {
            return true;
        }

        else if (year % 4 == 0 && year % 100 != 0) {
            return true;
        }

        return false;
    }
}