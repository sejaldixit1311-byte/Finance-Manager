/**
 * Thrown when adding an expense would push total spending in a category
 * beyond the budget limit set for that category. It's a checked exception
 * on purpose: the caller (Main) is forced to handle it, which is exactly
 * what a real finance app should do -- warn the user, not crash.
 */
public class BudgetExceededException extends Exception {
    private final Category category;
    private final double budgetLimit;
    private final double attemptedTotal;

    public BudgetExceededException(Category category, double budgetLimit, double attemptedTotal) {
        super(String.format(
                "Budget exceeded for %s: limit is %.2f, this expense brings total to %.2f",
                category, budgetLimit, attemptedTotal));
        this.category = category;
        this.budgetLimit = budgetLimit;
        this.attemptedTotal = attemptedTotal;
    }

    public Category getCategory() {
        return category;
    }

    public double getBudgetLimit() {
        return budgetLimit;
    }

    public double getAttemptedTotal() {
        return attemptedTotal;
    }
}
