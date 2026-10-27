public class Main {

    // Returns Byte if conversion is safe, otherwise null
    public static Byte safeToByte(int n) {
        if (n >= Byte.MIN_VALUE && n <= Byte.MAX_VALUE) {
            return (byte) n;
        }
        return null;
    }

    public static void main(String[] args) {

        // 1. Widening conversion (no cast needed)
        int x = 100;
        long y = x;
        System.out.println("Widening int -> long:");
        System.out.println("x = " + x);
        System.out.println("y = " + y);

        // 2. Narrowing conversion (cast required)
        double d = 3.9;
        int i = (int) d;
        System.out.println("\nNarrowing double -> int:");
        System.out.println("d = " + d);
        System.out.println("i = " + i);

        // 3. Testing safeToByte
        int[] testValues = {100, 200, -200};

        System.out.println("\nTesting safeToByte:");

        for (int value : testValues) {
            Byte result = safeToByte(value);

            if (result != null) {
                System.out.println(value + " accepted -> " + result);
            } else {
                System.out.println(value + " rejected (out of byte range)");
            }
        }
    }
}