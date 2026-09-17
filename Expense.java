import java.time.LocalDate;

/**
 * Represents a single expense: money going OUT, tagged with a Category.
 */
public class Expense implements Transaction {
    private final double amount;
    private final LocalDate date;
    private final String description;
    private final Category category;

    public Expense(double amount, LocalDate date, String description, Category category) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive.");
        }
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String describe() {
        return String.format("[%s] EXPENSE  -%-10.2f %-16s %s", date, amount, category, description);
    }
}
