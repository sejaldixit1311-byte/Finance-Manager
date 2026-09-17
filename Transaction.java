import java.time.LocalDate;

/**
 * Common contract for anything that moves money in or out of the student's
 * account (Income or Expense). Demonstrates polymorphism: FinanceManager can
 * work with a single List<Transaction> without caring about the concrete type.
 */
public interface Transaction {
    double getAmount();
    LocalDate getDate();
    String getDescription();

    /** Human-readable one-line summary, used when printing transaction history. */
    String describe();
}
