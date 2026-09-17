import java.time.LocalDate;

/**
 * Represents money coming IN: allowance from home, part-time job pay,
 * scholarship, refund, etc.
 */
public class Income implements Transaction {
    private final double amount;
    private final LocalDate date;
    private final String description;
    private final String source;

    public Income(double amount, LocalDate date, String description, String source) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Income amount must be positive.");
        }
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.source = source;
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

    public String getSource() {
        return source;
    }

    @Override
    public String describe() {
        return String.format("[%s] INCOME   +%-10.2f %-16s %s", date, amount, source, description);
    }
}
