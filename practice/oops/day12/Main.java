public class Main {

    static class Shape {

        final void name() {
            System.out.println("I am a Shape");
        }
    }

    static class Circle extends Shape {

        void draw() {
            System.out.println("Drawing Circle");
        }
    }

    final static class CircleMath {

        static double area(double radius) {
            return 3.14159 * radius * radius;
        }
    }

    public static void main(String[] args) {

        Circle c = new Circle();

        c.name();
        c.draw();

        System.out.println();

        System.out.println("Area = " + CircleMath.area(5));
    }
}