public class Main {

    interface Printable {

        default void printHeader() {
            System.out.println("=== REPORT ===");
        }

        void print();
    }

    static class Report implements Printable {

        @Override
        public void print() {
            System.out.println("Printing Report");
        }
    }

    static class Invoice implements Printable {

        @Override
        public void printHeader() {
            System.out.println("*** INVOICE ***");
        }

        @Override
        public void print() {
            System.out.println("Printing Invoice");
        }
    }

    public static void main(String[] args) {

        Printable p1 = new Report();

        p1.printHeader();
        p1.print();

        System.out.println();

        Printable p2 = new Invoice();

        p2.printHeader();
        p2.print();
    }
}