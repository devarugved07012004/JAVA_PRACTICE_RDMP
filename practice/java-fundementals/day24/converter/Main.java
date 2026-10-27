package converter;
import converter.Tc;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Temperature Converter ===");
            System.out.println("1) C to F");
            System.out.println("2) F to C");
            System.out.println("0) Quit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter temperature in Celsius: ");
                    double c = sc.nextDouble();

                    double f =
                            Tc.celsiusToFahrenheit(c);

                    System.out.println(c + "°C = " + f + "°F");
                    break;

                case 2:
                    System.out.print("Enter temperature in Fahrenheit: ");
                    double fahrenheit = sc.nextDouble();

                    double celsius =
                            Tc.fahrenheitToCelsius(fahrenheit);

                    System.out.println(fahrenheit + "°F = "
                            + celsius + "°C");
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}