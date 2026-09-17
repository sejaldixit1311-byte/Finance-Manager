# Finance-Manager

A console-based Java application to help a student living away from home
track income, expenses, and category budgets. All data is stored in memory
and resets each time you run the program.

## How to Compile and Run

```bash
javac *.java
java Main
```

(Requires Java 17 or newer — uses `LocalDate` and switch expressions.)

## Features

- **Add Income** — log allowance, part-time job pay, scholarships, etc.
- **Add Expense** — log spending, tagged with a category (Rent, Groceries,
  Food & Dining, Transport, Utilities, Education, Health, Entertainment,
  Miscellaneous)
- **Set Category Budget** — cap how much you plan to spend per category
  per month
- **Budget Warnings** — if an expense pushes a category over its budget,
  the app still records it but warns you immediately
- **View Summary** — total income, total expenses, current balance, and
  budget status per category (OK / OVER BUDGET)
- **View Spending by Category** — a grouped breakdown of where your money
  is going
- **View All Transactions** — full chronological history in one place

## Project Structure & OOP Concepts Demonstrated

| File | Concept |
|---|---|
| `Transaction.java` | Interface — common contract for Income and Expense, enables polymorphism |
| `Category.java` | Enum — type-safe expense categories, usable as a HashMap/EnumMap key |
| `Income.java` | Implements `Transaction`; encapsulated fields with getters |
| `Expense.java` | Implements `Transaction`; validates input in constructor |
| `BudgetExceededException.java` | Custom checked exception — carries structured data (category, limit, total) instead of just a message |
| `FinanceManager.java` | Core service class — collections (`List`, `EnumMap`), Java Streams for aggregation, encapsulation of business rules |
| `Main.java` | Console UI — menu loop, input validation, exception handling (try/catch) |

### Design highlights worth mentioning in a viva/demo
- **Polymorphism**: `FinanceManager` stores all transactions as
  `List<Transaction>` and only distinguishes Income vs Expense where it
  actually matters (e.g. `instanceof` checks when summing).
- **Encapsulation**: all fields in model classes are `private final`;
  external code can only read them through getters.
- **Exception handling**: `BudgetExceededException` is a *checked*
  exception — the compiler forces `Main` to catch it, mirroring how a real
  app should never let a budget overrun crash silently or unnoticed.
- **Streams + Collectors**: `getSpendingByCategory()` uses
  `Collectors.groupingBy` with `summingDouble` to build the category
  breakdown in a few lines instead of manual loops.
- **Defensive input handling**: every console read is wrapped so bad
  input (letters where a number is expected, malformed dates) reprompts
  instead of crashing the program.

## Possible Extensions (if you want to go further)
- Persist data to a file or database between runs (currently in-memory only)
- Support multiple months and show month-over-month trends
- Export a spending report
- Add a simple savings-goal tracker
