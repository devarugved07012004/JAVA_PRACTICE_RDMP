public class main {

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

        void start() {
            System.out.println("Vehicle is starting...");
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
            System.out.println("Car");
            super.describe();
            System.out.println("Doors: " + doors);
        }

        @Override
        void start() {
            System.out.println("Car engine started with key.");
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
            System.out.println("Bike");
            super.describe();
            System.out.println("Has Basket: " + hasBasket);
        }

        @Override
        void start() {
            System.out.println("Bike started by pedaling.");
        }
    }

    public static void main(String[] args) {

        Vehicle[] fleet = {
            new Car("Honda", 2024, 4),
            new Bike("Hero", 2022, true),
            new Car("Toyota", 2023, 2)
        };

        for (Vehicle v : fleet) {
            v.describe();
            v.start();
            System.out.println("----------------");
        }
    }
}