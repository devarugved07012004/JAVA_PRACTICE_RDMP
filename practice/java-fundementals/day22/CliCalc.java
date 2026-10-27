public class CliCalc {
    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Usage: java CliCalc <add|sub|mul|div> <a> <b>");
            return;
        }

        String operation = args[0];

        try {
            int a = Integer.parseInt(args[1]);
            int b = Integer.parseInt(args[2]);

            int result;
            switch (operation) {
                case "add" -> result = a + b;
                case "sub" -> result = a - b;
                case "mul" -> result = a * b;
                case "div" -> {
                    if (b == 0) {
                        System.out.println("Error: Cannot divide by zero.");
                        return;
                    }
                    result = a / b;
                }
                default -> {
                    System.out.println("Usage: java CliCalc <add|sub|mul|div> <a> <b>");
                    return;
                }
            }

            System.out.println("Result = " + result);

        } catch (NumberFormatException e) {
            System.out.println("Usage: java CliCalc <add|sub|mul|div> <a> <b>");
        }
    }
}