public class Main {

    public static void main(String[] args) {

        System.out.println("PI = " + MathUtils.PI);

        System.out.println("Below min: " + MathUtils.clamp(5, 10, 20));
        System.out.println("In range : " + MathUtils.clamp(15, 10, 20));
        System.out.println("Above max: " + MathUtils.clamp(25, 10, 20));
    }
}

class MathUtils {

    public static final double PI = 3.14159;

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }

        if (value > max) {
            return max;
        }

        return value;
    }
}