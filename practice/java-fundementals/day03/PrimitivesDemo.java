public class PrimitivesDemo {

    public static void main(String[] args) {

        byte b = 100;
        short s = 1000;
        int i = 100000;
        long l = 10000000000L;

        float f = 10.5f;
        double d = 20.99;

        char c = 'A';
        boolean isJavaFun = true;

        System.out.println("Byte: " + b);
        System.out.println("Short: " + s);
        System.out.println("Int: " + i);
        System.out.println("Long: " + l);
        System.out.println("Float: " + f);
        System.out.println("Double: " + d);
        System.out.println("Char: " + c);
        System.out.println("Boolean: " + isJavaFun);

        // System.out.println("Max Int: " + Integer.MAX_VALUE);
        // System.out.println("Min Int: " + Integer.MIN_VALUE);

        int x = Integer.MAX_VALUE;
        // System.out.println("Before: " + x);

        x = x + 1;
        // System.out.println("After: " + x);

        int num = (int) (10.5 +  11.6);
        System.out.println("Casted Value: " + num);
    }
}