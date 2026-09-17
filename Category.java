/**
 * Expense categories a student typically deals with while living away from home.
 * Using an enum instead of raw Strings prevents typos like "Rnet" vs "Rent"
 * and lets us use it directly as a HashMap key.
 */
public enum Category {
    RENT,
    GROCERIES,
    FOOD_AND_DINING,
    TRANSPORT,
    UTILITIES,
    EDUCATION,
    HEALTH,
    ENTERTAINMENT,
    MISCELLANEOUS
}
