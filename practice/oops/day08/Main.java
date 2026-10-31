public class Main {

    // Parent Class
    static class Vehicle {
        String brand;
        int year;

        Vehicle(String brand, int year) {
            this.brand = brand;
            this.year = year;
        }

        void describe() {
            System.out.println("Brand: " + brand);
            System.out.println("Year: " + year);
        }
    }

    // Child Class
    static class Car extends Vehicle {
        int doors;

        Car(String brand, int year, int doors) {
            super(brand, year);
            this.doors = doors;
        }

        @Override
        void describe() {
            super.describe();
            System.out.println("Doors: " + doors);
        }
    }

    // Child Class
    static class Bike extends Vehicle {
        boolean hasBasket;

        Bike(String brand, int year, boolean hasBasket) {
            super(brand, year);
            this.hasBasket = hasBasket;
        }

        @Override
        void describe() {
            super.describe();
            System.out.println("Has Basket: " + hasBasket);
        }
    }

    public static void main(String[] args) {

        Car car = new Car("Honda", 2024, 4);
        Bike bike = new Bike("Hero", 2022, true);

        System.out.println("=== Car Details ===");
        car.describe();

        System.out.println();

        System.out.println("=== Bike Details ===");
        bike.describe();
    }
}