import java.util.*;
import java.util.stream.Collectors;

/**
 * The engine of the app. Holds all transactions in memory (data resets each
 * run, by design) and provides operations to add money in/out, manage
 * per-category budgets, and produce summaries.
 */
public class FinanceManager {

    private final List<Transaction> transactions = new ArrayList<>();
    private final Map<Category, Double> categoryBudgets = new EnumMap<>(Category.class);

    /** Adds income with no budget checking -- money coming in is always welcome. */
    public void addIncome(Income income) {
        transactions.add(income);
    }

    /**
     * Adds an expense. If a budget has been set for that category and this
     * expense would push spending past the limit, the expense is still
     * recorded (you did spend the money) but a BudgetExceededException is
     * thrown so the caller can warn the user.
     */
    public void addExpense(Expense expense) throws BudgetExceededException {
        transactions.add(expense);

        Category category = expense.getCategory();
        if (categoryBudgets.containsKey(category)) {
            double limit = categoryBudgets.get(category);
            double totalSoFar = getTotalSpentByCategory(category);
            if (totalSoFar > limit) {
                throw new BudgetExceededException(category, limit, totalSoFar);
            }
        }
    }

    public void setBudget(Category category, double limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Budget limit must be positive.");
        }
        categoryBudgets.put(category, limit);
    }

    public Map<Category, Double> getAllBudgets() {
        return Collections.unmodifiableMap(categoryBudgets);
    }

    public double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t instanceof Income)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpenses() {
        return transactions.stream()
                .filter(t -> t instanceof Expense)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpenses();
    }

    public double getTotalSpentByCategory(Category category) {
        return transactions.stream()
                .filter(t -> t instanceof Expense)
                .map(t -> (Expense) t)
                .filter(e -> e.getCategory() == category)
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /** Groups all expenses by category and sums them -- used for the summary report. */
    public Map<Category, Double> getSpendingByCategory() {
        return transactions.stream()
                .filter(t -> t instanceof Expense)
                .map(t -> (Expense) t)
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        () -> new EnumMap<>(Category.class),
                        Collectors.summingDouble(Expense::getAmount)));
    }

    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    /** Returns remaining budget for a category, or null if no budget was set. */
    public Double getRemainingBudget(Category category) {
        if (!categoryBudgets.containsKey(category)) {
            return null;
        }
        return categoryBudgets.get(category) - getTotalSpentByCategory(category);
    }
}
