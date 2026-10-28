public class Main {

    static class BankAccount {
        private String owner;
        private double balance;

        public BankAccount(String owner, double balance) {
            if (balance < 0) {
                throw new IllegalArgumentException(
                        "Initial balance cannot be negative."
                );
            }

            this.owner = owner;
            this.balance = balance;
        }

        public void deposit(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException(
                        "Deposit amount must be greater than 0."
                );
            }

            balance += amount;
        }

        public void withdraw(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException(
                        "Withdrawal amount must be greater than 0."
                );
            }

            if (amount > balance) {
                throw new IllegalArgumentException(
                        "Insufficient funds."
                );
            }

            balance -= amount;
        }

        public double getBalance() {
            return balance;
        }

        @Override
        public String toString() {
            return "BankAccount{" +
                    "owner='" + owner + '\'' +
                    ", balance=" + balance +
                    '}';
        }
    }

    public static void main(String[] args) {

        BankAccount account =
                new BankAccount("Rugved", 1000);

        System.out.println("Initial Account:");
        System.out.println(account);

        // Happy path
        try {
            account.deposit(500);
            account.withdraw(200);

            System.out.println("\nAfter Valid Transactions:");
            System.out.println(account);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        // Failure Path 1
        try {
            account.deposit(-100);
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "\nDeposit Error: " + e.getMessage()
            );
        }

        // Failure Path 2
        try {
            account.withdraw(5000);
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "\nWithdraw Error: " + e.getMessage()
            );
        }

        System.out.println(
                "\nFinal Balance: "
                        + account.getBalance()
        );
    }
}