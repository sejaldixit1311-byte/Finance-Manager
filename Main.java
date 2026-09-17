import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

/**
 * Student Finance Manager
 * ------------------------
 * A console app for a student living away from home to track income,
 * expenses, and category budgets. All data is in-memory and resets when
 * the program exits.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final FinanceManager manager = new FinanceManager();

    public static void main(String[] args) {
        System.out.println("=======================================");
        System.out.println(" STUDENT FINANCE MANAGER");
        System.out.println("=======================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1 -> addIncomeFlow();
                case 2 -> addExpenseFlow();
                case 3 -> setBudgetFlow();
                case 4 -> printSummary();
                case 5 -> printSpendingByCategory();
                case 6 -> printAllTransactions();
                case 0 -> {
                    running = false;
                    System.out.println("Goodbye! Stay on budget.");
                }
                default -> System.out.println("Invalid option, try again.\n");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n---------------------------------------");
        System.out.println("1. Add Income");
        System.out.println("2. Add Expense");
        System.out.println("3. Set Category Budget");
        System.out.println("4. View Summary (Balance)");
        System.out.println("5. View Spending by Category");
        System.out.println("6. View All Transactions");
        System.out.println("0. Exit");
        System.out.println("---------------------------------------");
    }

    private static void addIncomeFlow() {
        System.out.println("\n-- Add Income --");
        double amount = readDouble("Amount: ");
        String source = readLine("Source (e.g. Allowance, Part-time job): ");
        String description = readLine("Description: ");
        LocalDate date = readDate("Date (yyyy-mm-dd, blank for today): ");

        Income income = new Income(amount, date, description, source);
        manager.addIncome(income);
        System.out.println("Income added. New balance: " + String.format("%.2f", manager.getBalance()));
    }

    private static void addExpenseFlow() {
        System.out.println("\n-- Add Expense --");
        double amount = readDouble("Amount: ");
        Category category = readCategory();
        String description = readLine("Description: ");
        LocalDate date = readDate("Date (yyyy-mm-dd, blank for today): ");

        Expense expense = new Expense(amount, date, description, category);
        try {
            manager.addExpense(expense);
            System.out.println("Expense added. New balance: " + String.format("%.2f", manager.getBalance()));
        } catch (BudgetExceededException e) {
            System.out.println("Expense added, but WARNING: " + e.getMessage());
        }
    }

    private static void setBudgetFlow() {
        System.out.println("\n-- Set Category Budget --");
        Category category = readCategory();
        double limit = readDouble("Monthly budget limit for " + category + ": ");
        manager.setBudget(category, limit);
        System.out.println("Budget set: " + category + " -> " + String.format("%.2f", limit));
    }

    private static void printSummary() {
        System.out.println("\n-- Summary --");
        System.out.printf("Total Income:   %.2f%n", manager.getTotalIncome());
        System.out.printf("Total Expenses: %.2f%n", manager.getTotalExpenses());
        System.out.printf("Balance:        %.2f%n", manager.getBalance());

        Map<Category, Double> budgets = manager.getAllBudgets();
        if (!budgets.isEmpty()) {
            System.out.println("\nBudget status:");
            for (Category c : budgets.keySet()) {
                Double remaining = manager.getRemainingBudget(c);
                String status = remaining != null && remaining < 0 ? "OVER BUDGET" : "OK";
                System.out.printf("  %-16s limit %.2f | remaining %.2f | %s%n",
                        c, budgets.get(c), remaining, status);
            }
        }
    }

    private static void printSpendingByCategory() {
        System.out.println("\n-- Spending by Category --");
        Map<Category, Double> spending = manager.getSpendingByCategory();
        if (spending.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        spending.forEach((category, total) ->
                System.out.printf("  %-16s %.2f%n", category, total));
    }

    private static void printAllTransactions() {
        System.out.println("\n-- All Transactions --");
        if (manager.getAllTransactions().isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        for (Transaction t : manager.getAllTransactions()) {
            System.out.println("  " + t.describe());
        }
    }

    // ---------- Input helper methods (handle invalid input gracefully) ----------

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value <= 0) {
                    System.out.println("Amount must be greater than 0.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(line);
            } catch (DateTimeParseException e) {
                System.out.println("Please use format yyyy-mm-dd, e.g. 2026-09-17.");
            }
        }
    }

    private static Category readCategory() {
        Category[] values = Category.values();
        System.out.println("Categories:");
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        while (true) {
            int choice = readInt("Choose category number: ");
            if (choice >= 1 && choice <= values.length) {
                return values[choice - 1];
            }
            System.out.println("Invalid category number, try again.");
        }
    }
}
